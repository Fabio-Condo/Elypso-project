package org.elypso.service;

import com.google.gson.Gson;
import org.elypso.commandsService.ElypsoCommandsService;
import org.elypso.domain.PrinterCenterResponse;
import org.elypso.domain.Pedido;
import org.elypso.enumerations.Fita;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static org.elypso.constatnt.ComandosElypsoPrimacy.*;
import static org.elypso.constatnt.Constant.*;


@Service
public class ElypsoService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    String frontImagePath = IMAGES_FOLDER + FORWARD_SLASH + IMAGEM_FRONTAL_SEM_NOME;
    String backImagePath = IMAGES_FOLDER + FORWARD_SLASH + IMAGEM_TRAZEIRA;
    String outputImagePath = IMAGES_FOLDER + FORWARD_SLASH + IMAGEM_FRONTAL_GERADA_COM_DADOS;
    ElypsoCommandsService elypsoCommandsService;
    SocketService socketService;

    @Autowired
    public ElypsoService(ElypsoCommandsService elypsoCommandsService, SocketService socketService) {
        this.elypsoCommandsService = elypsoCommandsService;
        this.socketService = socketService;
    }

    public Pedido executarOperacaoUnica(Pedido pedido) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException {

        PrinterCenterResponse printerCenterResponse;

        for (int i = 1; i < FINALIZAR_SEQUENCIA; i++) {

            if (i == INICIALIZAR_SEQUENCIA) {
                printerCenterResponse = iniciarSequencia();
            } else if (i == INICIALIZAR_PROCESSO_IMPRESSAO) {
                printerCenterResponse = inicializarProcessoImpressao();
            } else if (i == CONFIGURAR_PROCESSO_IMPRESSAO) {
                printerCenterResponse = configurarProcessoImpressao(pedido.getFita());
            } else if (i == DEFINIR_BITMAP_FRONTAL) {
                printerCenterResponse = definirBitmapImpressaoFrontal(pedido);
            } else if (i == DEFINIR_BITMAP_TRAZEIRO) {
                printerCenterResponse = definirBitmapImpressaoTrazeiro();
            } else if (i == REALIZAR_IMPRESSAO) {
                printerCenterResponse = realizarImpressao();
            } else if (i == FINALIZAR_IMPRESSAO) {
                printerCenterResponse = finalizarImpressao();
            } else {
                printerCenterResponse = finalizarSequencia();  // I = FINALIZAR_SEQUENCIA
            }

            analisarErroOuRespostaRetornadaPeloPrinterCenter(printerCenterResponse, i);

        }

        return pedido;
    }

    public void analisarErroOuRespostaRetornadaPeloPrinterCenter(PrinterCenterResponse printerCenterResponse, int indiceComando) throws PedidoComandoException, IOException {

        if(printerCenterResponse.getError() != null){

            LOGGER.error("Erro na operação " + indiceComando);
            LOGGER.error("Codigo do erro (Printer Center): " + printerCenterResponse.getError().getCode());
            LOGGER.error("Mensagem do erro (Printer Center): " + printerCenterResponse.getError().getMessage());

            if(printerCenterResponse.getError().getMessage().contains("Communication session already reserved")){
                finalizarImpressao();
                finalizarSequencia();
                ligarOuReinicializarHardwareImpressora();
                reinicializarComunicacoesComAImpressora();
                throw new PedidoComandoException("Communication session already reserved");
            }

            throw new PedidoComandoException("Erro na operação " + indiceComando + ". Mensagem do printer center: " + printerCenterResponse.getError().getMessage()); // Vai lancar a Excepption e fazer um break
        }
    }

    public PrinterCenterResponse iniciarSequencia() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoIniciarSequencia();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse inicializarProcessoImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoInicializarProcessoImpressao();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse configurarProcessoImpressao(Fita fita) throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoConfigurarProcessoImpressao(fita);
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse definirBitmapImpressaoFrontal(Pedido pedido) throws NomeOuNumeroVazioException, IOException, FileNotFoundException {
        Socket socket = socketService.iniciarSocket();
        verificarExistenciaArquivoNoDiretorio(IMAGES_FOLDER, IMAGEM_FRONTAL_SEM_NOME);
        adicionarNomeNumeroNaImagem(frontImagePath, outputImagePath, pedido);
        String imagemEmDadosBase64 = converterBMPImageParaString(outputImagePath);
        verificarExistenciaArquivoNoDiretorio(IMAGES_FOLDER, IMAGEM_FRONTAL_GERADA_COM_DADOS); // Depois de gerar a imagem frontal com dados (nome e numero), verifica se existe para impressao
        String request = elypsoCommandsService.gerarComandoDefinirBitmapImpressaoFrontal(imagemEmDadosBase64);
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse definirBitmapImpressaoTrazeiro() throws IOException, FileNotFoundException {
        Socket socket = socketService.iniciarSocket();
        verificarExistenciaArquivoNoDiretorio(IMAGES_FOLDER, IMAGEM_TRAZEIRA);
        String imagemEmDadosBase64 = converterBMPImageParaString(backImagePath);
        String request = elypsoCommandsService.gerarComandoDefinirBitmapImpressaoTrazeiro(imagemEmDadosBase64);
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse realizarImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoRealizarImpressao();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse finalizarImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoFinalizarImpressao();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse finalizarSequencia() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoFinalizarSequencia();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse verificarStatus() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoVerificarStatusImpressora();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse verificarFita() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoVerificarFita(2);
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse ligarOuReinicializarHardwareImpressora() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandosLigarOuReinicializarHardwareImpressora();
        return pegarResposta(socket, request);
    }

    public PrinterCenterResponse reinicializarComunicacoesComAImpressora() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoReinicializarComunicacoesComAImpressora();
        return pegarResposta(socket, request);
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

    public PrinterCenterResponse pegarResposta(Socket socket, String request ) throws IOException {

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

    public void adicionarNomeNumeroNaImagem(String imagePath, String outputImagePath, Pedido pedido) throws NomeOuNumeroVazioException, IOException {

        if(pedido.getNome().equals("") || pedido.getNumero().equals("") || pedido.getNome() == null || pedido.getNumero() == null){
            LOGGER.error("O nome ou número está vázio");
            throw new NomeOuNumeroVazioException("O nome ou número está vázio ou nullo");
        }

        // Carrega a imagem original
        File inputFile = new File(imagePath);
        BufferedImage imagem = ImageIO.read(inputFile);

        // Cria um objeto Graphics2D para desenhar na imagem (Neste caso, crio 2 objectos, nome e numero)
        Graphics2D g2d_nome = imagem.createGraphics();
        Graphics2D g2d_numeroClientText = imagem.createGraphics();
        Graphics2D g2d_numeroClientTextBold = imagem.createGraphics();
        Graphics2D g2d_numero = imagem.createGraphics();

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

        Font fonteNumero = new Font("Arial", Font.PLAIN, 120);
        g2d_numero.setFont(fonteNumero);
        g2d_numero.setColor(Color.WHITE);

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
        int xNumero = 265;
        int yNumero = 2480;

        // Desenha o nome e o número na imagem
        g2d_nome.drawString(pedido.getNome(), xNome, yNome);
        g2d_numeroClientText.drawString("nº", xNumeroClientText, yNumeroClientText);
        g2d_numeroClientTextBold.drawString("cliente:", xNumeroClientTextBold, yNumeroClientTextBold);
        g2d_numero.drawString(pedido.getNumero(), xNumero, yNumero);

        // Libera os recursos do objeto Graphics2D
        g2d_nome.dispose();
        g2d_numeroClientText.dispose();
        g2d_numeroClientTextBold.dispose();
        g2d_numero.dispose();

        // Salva a imagem resultante
        File outputFile = new File(outputImagePath);
        ImageIO.write(imagem, "bmp", outputFile);
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
