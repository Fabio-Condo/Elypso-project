package org.elypso.service;

import org.elypso.commands.ElypsoCommandsService;
import org.elypso.model.Pedido;
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


@Service
public class ElypsoService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    ElypsoCommandsService elypsoCommandsService;

    SocketService socketService;

    @Autowired
    public ElypsoService(ElypsoCommandsService elypsoCommandsService, SocketService socketService) {
        this.elypsoCommandsService = elypsoCommandsService;
        this.socketService = socketService;
    }

    public Pedido executarOperacaoUnica(Pedido pedido) throws IOException {

        String answer = "";

        for (int i = 1; i < FINALIZAR_SEQUENCIA; i++) {

            if (i == INICIALIZAR_SEQUENCIA) {
                answer = iniciarSequencia();
            } else if (i == INICIALIZAR_PROCESSO_IMPRESSAO) {
                answer = inicializarProcessoImpressao();
            } else if (i == CONFIGURAR_PROCESSO_IMPRESSAO) {
                answer = configurarProcessoImpressao();
            } else if (i == DEFINIR_BITMAP_FRONTAL) {
                answer = definirBitmapImpressaoFrontal(pedido);
            } else if (i == DEFINIR_BITMAP_TRAZEIRO) {
                answer = definirBitmapImpressaoTrazeiro();
            } else if (i == REALIZAR_IMPRESSAO) {
                answer = realizarImpressao();
            } else if (i == FINALIZAR_IMPRESSAO) {
                answer = finalizarImpressao();
            } else {
                answer = finalizarSequencia();  // I = FINALIZAR_SEQUENCIA
            }

            if(answer.contains("error")){
                LOGGER.error("Erro do pedido " + i);
                // Lancar uma exception
                break;
            }

        }

        return pedido;
    }

    public String iniciarSequencia() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoIniciarSequencia();
        return pegarResposta(socket, request);
    }

    public String inicializarProcessoImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoInicializarProcessoImpressao();
        return pegarResposta(socket, request);
    }

    public String configurarProcessoImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoConfigurarProcessoImpressao();
        return pegarResposta(socket, request);
    }

    public String definirBitmapImpressaoFrontal(Pedido pedido) throws IOException {
        Socket socket = socketService.iniciarSocket();

        String imagePath = "imagens/cartao_de_saude_test_print_front.bmp";
        String outputImagePath = "imagem_com_nome.bmp";

        try {
            adicionarNomeNumeroNaImagem(imagePath, outputImagePath, pedido);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imagemEmDadosBase64 = converterBMPImageParaString(outputImagePath);
        String request = elypsoCommandsService.gerarComandoDefinirBitmapImpressaoFrontal(imagemEmDadosBase64);
        return pegarResposta(socket, request);
    }

    public String definirBitmapImpressaoTrazeiro() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String filePath = "imagens/cartao_de_saude_test_print_back.bmp";
        String imagemEmDadosBase64 = converterBMPImageParaString(filePath);
        String request = elypsoCommandsService.gerarComandoDefinirBitmapImpressaoTrazeiro(imagemEmDadosBase64);
        return pegarResposta(socket, request);
    }

    public String realizarImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoRealizarImpressao();
        return pegarResposta(socket, request);
    }

    public String finalizarImpressao() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoFinalizarImpressao();
        return pegarResposta(socket, request);
    }

    public String finalizarSequencia() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoFinalizarSequencia();
        return pegarResposta(socket, request);
    }

    public String ligarOuReinicializarHardwareImpressora() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandosLigarOuReinicializarHardwareImpressora();
        return pegarResposta(socket, request);
    }

    public String verificarStatus() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoVerificarStatusImpressora();
        return pegarResposta(socket, request);
    }

    public String reinicializarComunicacoesComAImpressora() throws IOException {
        Socket socket = socketService.iniciarSocket();
        String request = elypsoCommandsService.gerarComandoReinicializarComunicacoesComAImpressora();
        return pegarResposta(socket, request);
    }

    public String converterBMPImageParaString(String filePath) throws IOException {
        // Caminho para o arquivo BMP
        //String filePath = "imagens/BackVarnish.bmp";
        //String filePath = "imagens/safeline.bmp";

        // Lê os bytes da imagem BMP
        byte[] imageBytes = Files.readAllBytes(Paths.get(filePath));

        // Codifica os bytes em base64
        String base64EncodedImage = Base64.getEncoder().encodeToString(imageBytes);

        // Imprime a string codificada em base64
        System.out.println(base64EncodedImage);

        return base64EncodedImage;
    }

    public String pegarResposta(Socket socket, String request ) throws IOException {

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
        LOGGER.info("Resposta: " + answer.replaceAll("\u0000", ""));

        return answer.replaceAll("\u0000", ""); // This is done because the Evolis Socket returns a lot of null objects which make the files much larger.

    }

    public void adicionarNomeNumeroNaImagem(String imagePath, String outputImagePath, Pedido pedido) throws IOException {

        if(pedido.getNome().equals("") || pedido.getNumero().equals("")){
            LOGGER.error("O nome ou número está vázio");
            // Lanca uma exception
        }

        // Carrega a imagem original
        File inputFile = new File(imagePath);
        BufferedImage imagem = ImageIO.read(inputFile);

        // Cria um objeto Graphics2D para desenhar na imagem (Neste caso, crio 2 objectos, nome e numero)
        Graphics2D g2d_nome = imagem.createGraphics();
        Graphics2D g2d_numero = imagem.createGraphics();

        // Configura a fonte e a cor do texto
        Font fonteNome = new Font("Arial", Font.BOLD, 50);
        g2d_nome.setFont(fonteNome);
        g2d_nome.setColor(Color.WHITE);

        Font fonteNumero = new Font("Arial", Font.PLAIN, 30);
        g2d_numero.setFont(fonteNumero);
        g2d_numero.setColor(Color.WHITE);

        // Define a posição onde o nome e o número serão adicionados (neste exemplo, no canto superior esquerdo)
        int xNome = 70;
        int yNome = 500;
        int xNumero = 70;
        int yNumero = 620;

        // Desenha o nome e o número na imagem
        g2d_nome.drawString(pedido.getNome(), xNome, yNome);
        g2d_numero.drawString(pedido.getNumero(), xNumero, yNumero);

        // Libera os recursos do objeto Graphics2D
        g2d_nome.dispose();
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
