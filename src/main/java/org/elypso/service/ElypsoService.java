package org.elypso.service;

import com.google.gson.Gson;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elypso.commandsService.ElypsoCommandsService;
import org.elypso.domain.PrinterCenterResponse;
import org.elypso.domain.Pedido;
import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.ImpressoraSemFitaException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.elypso.repository.ElypsoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.elypso.constatnt.ComandosElypsoPrimacy.*;
import static org.elypso.constatnt.Constant.*;


@Service
public class ElypsoService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    //private static final String IMAGES_PATH = "/app/imagens"; // Caminho do volume no contêiner// Use quando fizer deploymente no docker
    public static final String IMAGES_PATH = "C:\\Users\\user\\Desktop\\spc-imagens-docker"; // Deployment no proproprio server ou run usando um jar

    String frontImagePath   = IMAGES_PATH + FORWARD_SLASH + IMAGEM_FRONTAL_SEM_NOME;
    String backImagePath    = IMAGES_PATH + FORWARD_SLASH + IMAGEM_TRAZEIRA;
    String outputImagePath  = IMAGES_PATH + FORWARD_SLASH + IMAGEM_FRONTAL_GERADA_COM_DADOS;
    String whiteImagePath    = IMAGES_PATH + FORWARD_SLASH + IMAGEM_BRANCA;

    ElypsoCommandsService elypsoCommandsService;
    SocketService socketService;
    ElypsoRepository elypsoRepository;

    public ElypsoService(ElypsoCommandsService elypsoCommandsService, SocketService socketService, ElypsoRepository elypsoRepository) {
        this.elypsoCommandsService = elypsoCommandsService;
        this.socketService = socketService;
        this.elypsoRepository = elypsoRepository;
    }

    public void imprimirDadosDoExcel(MultipartFile file, String impressora, Lado lado) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Supondo que os dados estejam na primeira planilha

            int totalLinhas = sheet.getPhysicalNumberOfRows();
            System.out.println("Total de linhas no arquivo: " + totalLinhas);

            for (int i = 3; i < totalLinhas; i++) { // Começa do 1 para pular a linha de cabeçalho
                Row row = sheet.getRow(i);

                if (row == null) {
                    continue; // Pula para a próxima linha se a linha estiver vazia
                }

                // Lendo os dados de cada coluna
                String nomeCliente = row.getCell(1).getStringCellValue();
                String numeroCliente = row.getCell(2).getStringCellValue();
                String numeroApolice = row.getCell(4).getStringCellValue();
                // Adicione mais campos conforme necessário

                // Imprimindo os dados do funcionário
                Pedido pedido= new Pedido(nomeCliente, numeroCliente, numeroApolice, impressora, lado);
                executarOperacaoUnica(pedido);
            }

            workbook.close();
        } catch (PedidoComandoException | FileNotFoundException | NomeOuNumeroVazioException e) {
            throw new RuntimeException(e);
        }
    }

    public Pedido executarOperacaoUnica(Pedido pedido) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException {

        PrinterCenterResponse printerCenterResponse = null;

        printerCenterResponse = verificarFita(pedido.getImpressora());
        Fita fitaSelecionada = getTipoFita(printerCenterResponse.getResult());
        pedido.setNome(pedido.getNome().toUpperCase());
        pedido.setFita(fitaSelecionada);
        pedido.setSessao(gerarSessao());

        for (int i = 1; i < FINALIZAR_SEQUENCIA; i++) {

            if (i == INICIALIZAR_SEQUENCIA) {
                printerCenterResponse = iniciarSequencia(pedido.getImpressora());
            } else if (i == INICIALIZAR_PROCESSO_IMPRESSAO) {
                printerCenterResponse = inicializarProcessoImpressao(pedido.getImpressora(), pedido.getSessao());
            } else if (i == CONFIGURAR_PROCESSO_IMPRESSAO) {
                printerCenterResponse = configurarProcessoImpressao(pedido.getImpressora(), pedido.getFita(), pedido.getSessao());
            } else if (i == DEFINIR_BITMAP_FRONTAL) {
                if (pedido.getLado() == Lado.FRENTE || pedido.getLado() == Lado.FRENTE_VERSO){
                    printerCenterResponse = definirBitmapImpressaoFrontal(pedido);
                }else {
                    printerCenterResponse = definirBitmapImpressaoFrontalBrancaUsadaCasoNaoSejaEscolhidoLadoFrontal(pedido.getSessao());
                }
            } else if (i == DEFINIR_BITMAP_TRAZEIRO) {
                if (pedido.getLado() == Lado.VERSO || pedido.getLado() == Lado.FRENTE_VERSO){
                    printerCenterResponse = definirBitmapImpressaoTrazeiro(pedido.getSessao());
                }
            } else if (i == REALIZAR_IMPRESSAO) {
                printerCenterResponse = realizarImpressao(pedido.getSessao());
            } else if (i == FINALIZAR_IMPRESSAO) {
                printerCenterResponse = finalizarImpressao(pedido.getSessao());
            } else {
                printerCenterResponse = finalizarSequencia(pedido.getImpressora());  // I = FINALIZAR_SEQUENCIA
            }

            analisarErroOuRespostaRetornadaPeloPrinterCenter(printerCenterResponse, i, pedido);
            verificarEventoImpressoraELimparErro(pedido);

        }

        return salvarPedido(pedido);
    }

    public Pedido salvarPedido(Pedido pedido){
        pedido.setId(null); // Garantido que é um registo. Se remover, vai fazer updates do primeiro registo
        pedido.setDate(new Date());
        LOGGER.info("Salvando pedido: " + pedido.getNome());
        return elypsoRepository.save(pedido);
    }

    public Page<Pedido> listarPedidos(String name, Pageable pageable) {
        return elypsoRepository.findByAnyProperty(name, pageable);
    }

    public void verificarEventoImpressoraELimparErro(Pedido pedido){
        Runnable task = () -> {
            try {
                Thread.sleep(3000);
                PrinterCenterResponse printerCenterResponse = getEvent(pedido.getImpressora()); // Pegar o erro, caso exista
                if(!printerCenterResponse.getResult().equals("NONE")){

                    // Dividir a string pelo caractere ":"   -> // EX: DEF_NO_RIBBON:CANCEL
                    String[] partes = printerCenterResponse.getResult().split(":"); // Dividir a resposta e pegar a primeira parte que contem o erro
                    String erro = partes[0];
                    String accoes = partes[1];

                    setEvent(erro, pedido.getImpressora());

                    finalizarImpressao(pedido.getSessao());
                    finalizarSequencia(pedido.getImpressora());
                    ligarOuReinicializarHardwareImpressora(pedido.getImpressora());
                    reinicializarComunicacoesComAImpressora(pedido.getImpressora());

                    // Criar o metodo que vai lancar todos erros
                    throw new ImpressoraSemFitaException(printerCenterResponse.getResult());
                }
            } catch (IOException | InterruptedException | ImpressoraSemFitaException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void analisarErroOuRespostaRetornadaPeloPrinterCenter(PrinterCenterResponse printerCenterResponse, int indiceComando, Pedido pedido) throws PedidoComandoException, IOException {

        if(printerCenterResponse.getError() != null){

            LOGGER.error("Erro na operação " + indiceComando);
            LOGGER.error("Codigo do erro (Printer Center): " + printerCenterResponse.getError().getCode());
            LOGGER.error("Mensagem do erro (Printer Center): " + printerCenterResponse.getError().getMessage());

            if(printerCenterResponse.getError().getMessage().contains("Communication session already reserved")){
                finalizarImpressao(pedido.getSessao());
                finalizarSequencia(pedido.getImpressora());
                ligarOuReinicializarHardwareImpressora(pedido.getImpressora());
                reinicializarComunicacoesComAImpressora(pedido.getImpressora());
                throw new PedidoComandoException("Communication session already reserved");
            }

            //if(printerCenterResponse.getError().getMessage().contains("Printing session already in progress for the device")){
            //    finalizarImpressao(pedido.getSessao());
            //    finalizarSequencia();
            //    ligarOuReinicializarHardwareImpressora();
            //    reinicializarComunicacoesComAImpressora();
            //    throw new PedidoComandoException("Printing session already in progress for the device");
            //}

            //if(printerCenterResponse.getError().getMessage().contains("Invalid printing session")){
            //    finalizarImpressao(pedido.getSessao());
            //    finalizarSequencia();
            //    ligarOuReinicializarHardwareImpressora();
            //    reinicializarComunicacoesComAImpressora();
            //    throw new PedidoComandoException("Invalid printing session");
            //}

            //if(printerCenterResponse.getError().getMessage().contains("Communication session already reserved")){
            //    finalizarImpressao(pedido.getSessao());
            //    finalizarSequencia();
            //    ligarOuReinicializarHardwareImpressora();
            //    reinicializarComunicacoesComAImpressora();
            //    throw new PedidoComandoException("Communication session already reserved");
            //}

            throw new PedidoComandoException("Erro na operação " + indiceComando + ". Mensagem do printer center: " + printerCenterResponse.getError().getMessage()); // Vai lancar a Excepption e fazer um break
        }
    }

    public PrinterCenterResponse iniciarSequencia(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoIniciarSequencia(impressora));
    }

    public PrinterCenterResponse inicializarProcessoImpressao(String impressora, String sessao) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoInicializarProcessoImpressao(impressora, sessao));
    }

    public PrinterCenterResponse configurarProcessoImpressao(String impressora, Fita fita, String sessao) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoConfigurarProcessoImpressao(impressora, fita, sessao));
    }

    public PrinterCenterResponse definirBitmapImpressaoFrontal(Pedido pedido) throws NomeOuNumeroVazioException, IOException, FileNotFoundException {
        verificarExistenciaArquivoNoDiretorio(IMAGES_PATH, IMAGEM_FRONTAL_SEM_NOME);
        adicionarNomeNumeroNaImagem(frontImagePath, outputImagePath, pedido);
        String imagemEmDadosBase64 = converterBMPImageParaString(outputImagePath);
        verificarExistenciaArquivoNoDiretorio(IMAGES_PATH, IMAGEM_FRONTAL_GERADA_COM_DADOS); // Depois de gerar a imagem frontal com dados (nome e numero), verifica se existe para impressao
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoDefinirBitmapImpressaoFrontal(imagemEmDadosBase64, pedido.getSessao()));
    }

    public PrinterCenterResponse definirBitmapImpressaoFrontalBrancaUsadaCasoNaoSejaEscolhidoLadoFrontal(String sessao) throws IOException, FileNotFoundException {
        verificarExistenciaArquivoNoDiretorio(IMAGES_PATH, IMAGEM_BRANCA);
        String imagemEmDadosBase64 = converterBMPImageParaString(whiteImagePath);
        verificarExistenciaArquivoNoDiretorio(IMAGES_PATH, IMAGEM_FRONTAL_GERADA_COM_DADOS); // Depois de gerar a imagem frontal com dados (nome e numero), verifica se existe para impressao
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoDefinirBitmapImpressaoFrontal(imagemEmDadosBase64, sessao));
    }

    public PrinterCenterResponse definirBitmapImpressaoTrazeiro(String sessao) throws IOException, FileNotFoundException {
        verificarExistenciaArquivoNoDiretorio(IMAGES_PATH, IMAGEM_TRAZEIRA);
        String imagemEmDadosBase64 = converterBMPImageParaString(backImagePath);
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoDefinirBitmapImpressaoTrazeiro(imagemEmDadosBase64, sessao));
    }

    public PrinterCenterResponse realizarImpressao(String sessao) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoRealizarImpressao(sessao));
    }

    public PrinterCenterResponse finalizarImpressao(String sessao) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoFinalizarImpressao(sessao));
    }

    public PrinterCenterResponse finalizarSequencia(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoFinalizarSequencia(impressora));
    }

    public PrinterCenterResponse verificarStatus(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoVerificarStatusImpressora(impressora));
    }

    public PrinterCenterResponse verificarFita(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoVerificarFita(2, impressora));
    }

    public PrinterCenterResponse getEvent(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.criarComandoGetEvent(impressora));
    }

    public PrinterCenterResponse setEvent(String erro, String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.criarComandoSetEvent(erro, impressora));
    }

    public PrinterCenterResponse ligarOuReinicializarHardwareImpressora(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandosLigarOuReinicializarHardwareImpressora(impressora));
    }

    public PrinterCenterResponse reinicializarComunicacoesComAImpressora(String impressora) throws IOException {
        return enviarPedidoViaSocket(elypsoCommandsService.gerarComandoReinicializarComunicacoesComAImpressora(impressora));
    }

    public void verificarExistenciaArquivoNoDiretorio(String diretorio, String nomeArquivo) throws FileNotFoundException {
        File arquivo = new File(diretorio, nomeArquivo);
        if (!arquivo.exists()) {
            throw new FileNotFoundException("A imagem " + nomeArquivo + " não existe no diretório " + diretorio);
        }
    }

    public String converterBMPImageParaString(String filePath) throws IOException {
        // Caminho para o arquivo BMP
        //String filePath = "imagens/safeline.bmp";

        // Lê os bytes da imagem BMP
        byte[] imageBytes = Files.readAllBytes(Paths.get(filePath));

        // Codifica os bytes em base64
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);

        // Imprime a string codificada em base64
        System.out.println(base64EncodedImage);

        return base64EncodedImage;
    }

    public PrinterCenterResponse enviarPedidoViaSocket(String request) throws IOException {

        Socket socket = socketService.iniciarSocket();

        char[] data = new char[1024];

        String answer = "";

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        byte[] byteRequest = request.getBytes("UTF-8");

        out.write(byteRequest);

        out.flush();

        while( (br.read(data)) != -1 ) {
            answer = new String(data);
            break;
        }

        socket.close();

        LOGGER.info("Pedido: " + request);
        LOGGER.info("Resposta: " + answer.replaceAll("\u0000", "")); // This is done because the Evolis Socket returns a lot of null objects which make the files much larger.

        return converterJsonEmObjecto(answer);
    }

    public PrinterCenterResponse converterJsonEmObjecto(String answer){
        String jsonString = answer.replaceAll("\u0000", "");

        // Criar um objeto Gson
        Gson gson = new Gson();

        // Converter a string JSON em um objeto Java
        PrinterCenterResponse printerCenterResponse = gson.fromJson(jsonString, PrinterCenterResponse.class);

        // Agora você pode acessar os campos do objeto Java
        //System.out.println("id: " + printerCenterResponse.getId());
        //System.out.println("jsonrpc: " + printerCenterResponse.getJsonrpc());
        //System.out.println("result: " + printerCenterResponse.getResult());
        //System.out.println("error code: " + printerCenterResponse.getError().getCode());
        //System.out.println("error message: " + printerCenterResponse.getError().getMessage());

        return printerCenterResponse;
    }

    public Fita getTipoFita(String status) {
        switch (status) {
            case "Monochrome KO":
                return Fita.RM_KO;
            case "Color YMCKO":
                return Fita.RC_YMCKO;
            case "Monochrome Metallic Gold":
                return Fita.RM_KMETALGOLD;
            case "Monochrome White":
                return Fita.RM_KWHITE;
            case "Monochrome Metallic Silver":
                return Fita.RM_KMETALSILVER;
            case "Monochrome Black":
                return Fita.RM_KBLACK;
            default:
                return null;
        }
    }

    public void adicionarNomeNumeroNaImagem(String imagePath, String outputImagePath, Pedido pedido) throws NomeOuNumeroVazioException, IOException {

        if(pedido.getNome().equals("") || pedido.getNome() == null){
            LOGGER.error("O nome ou número está vázio");
            throw new NomeOuNumeroVazioException("O nome está vázio ou nullo");
        }

        // Carrega a imagem original
        File inputFile = new File(imagePath);
        BufferedImage imagem = ImageIO.read(inputFile);

        // Cria um objeto Graphics2D para desenhar na imagem (Neste caso, crio 2 objectos, nome e numero)
        Graphics2D g2d_nome = imagem.createGraphics();
        Graphics2D g2d_numeroClientText = imagem.createGraphics();
        Graphics2D g2d_numeroClientTextBold = imagem.createGraphics();
        Graphics2D g2d_numeroClientTextValue = imagem.createGraphics();
        Graphics2D g2d_numeroApolice = imagem.createGraphics();

        // Configura a fonte e a cor do texto
        Font fonteNome = new Font("Agency FB", Font.BOLD, 260);
        g2d_nome.setFont(fonteNome);
        g2d_nome.setColor(Color.WHITE);

        Font numeroClientText = new Font("Arial", Font.PLAIN, 120);
        g2d_numeroClientText.setFont(numeroClientText);
        g2d_numeroClientText.setColor(Color.WHITE);

        Font numeroClientTextBold = new Font("Arial", Font.BOLD, 120);
        g2d_numeroClientTextBold.setFont(numeroClientTextBold);
        g2d_numeroClientTextBold.setColor(Color.WHITE);

        Font numeroClientTextValue = new Font("Arial", Font.PLAIN, 120);
        g2d_numeroClientTextValue.setFont(numeroClientTextValue);
        g2d_numeroClientTextValue.setColor(Color.WHITE);

        Font fonteNumeroApolice = new Font("Arial", Font.PLAIN, 120);
        g2d_numeroApolice.setFont(fonteNumeroApolice);
        g2d_numeroApolice.setColor(Color.WHITE);

        // Define a posição onde o nome e o número serão adicionados (neste exemplo, no canto superior esquerdo)
        //int xNome = 170;
        //int yNome = 500;
        //int xNumero = 170;
        //int yNumero = 620;

        int xNome = 265;
        int yNome = 2075;
        int xNumeroClientText = 265;
        int yNumeroClientText = 2295;
        int xNumeroClientTextBold = 410;
        int yNumeroClientTextBold = 2295;
        int xNumeroClientTextValue = 850;
        int yNumeroClientTextValue = 2295;
        int xNumeroApolice = 265;
        int yNumeroApolice = 2480;

        // Desenha o nome e o número na imagem
        g2d_nome.drawString(pedido.getNome().toUpperCase(), xNome, yNome);
        g2d_numeroClientText.drawString("nº", xNumeroClientText, yNumeroClientText);
        g2d_numeroClientTextBold.drawString("cliente:", xNumeroClientTextBold, yNumeroClientTextBold);
        g2d_numeroClientTextValue.drawString(pedido.getNumeroCliente().toUpperCase(), xNumeroClientTextValue, yNumeroClientTextValue);
        g2d_numeroApolice.drawString(pedido.getNumeroApolice().toUpperCase(), xNumeroApolice, yNumeroApolice);

        // Libera os recursos do objeto Graphics2D
        g2d_nome.dispose();
        g2d_numeroClientText.dispose();
        g2d_numeroClientTextBold.dispose();
        g2d_numeroApolice.dispose();

        // Salva a imagem resultante
        File outputFile = new File(outputImagePath);
        ImageIO.write(imagem, "bmp", outputFile);
    }

    public String gerarSessao() {
        return "JOB" + RandomStringUtils.randomNumeric(6);
    }

    public BufferedImage gerarImagemBrancaVazia() {
        int width = 1016;
        int height = 648;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Preencher a imagem com cor branca
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        g2d.dispose();

        return image;
    }

    // Para testes, nao eliminar -> Gerar uma imagem branca com nome
    public void gerarImagemComNome(){
        int width = 1016;
        int height = 648;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Preencher o fundo com uma cor
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        // Adicionar texto no meio da imagem
        String nome = "FÁBIO CONDO";
        g2d.setColor(Color.BLACK);
        Font font = new Font("Arial", Font.PLAIN, 60);
        g2d.setFont(font);

        // Centralizar
        //FontMetrics metrics = g2d.getFontMetrics(font);
        //int x = (width - metrics.stringWidth(nome)) / 2;
        //int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent();

        int x = 70;
        int y = 500;

        g2d.drawString(nome, x, y);

        g2d.dispose();

        try {
            ImageIO.write(image, "bmp", new File("imagem_com_nome.bmp"));
            System.out.println("Imagem BMP criada com sucesso!");
        } catch (IOException e) {
            System.err.println("Erro ao criar a imagem BMP: " + e.getMessage());
        }
    }
}
