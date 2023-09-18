package org.elypso.service;

import org.elypso.commands.ElypsoCommandsService;
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

@Service
public class ElypsoService {

    ElypsoCommandsService elypsoCommandsService;

    SocketService socketService;

    @Autowired
    public ElypsoService(ElypsoCommandsService elypsoCommandsService, SocketService socketService) {
        this.elypsoCommandsService = elypsoCommandsService;
        this.socketService = socketService;
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

    public String definirBitmapImpressaoFrontal() throws IOException {
        Socket socket = socketService.iniciarSocket();
        //String filePath = "imagens/cartao_de_saude_test_print_front.bmp";

        String imagePath = "imagens/cartao_de_saude_test_print_front.bmp";
        String outputImagePath = "imagem_com_nome.bmp";
        String nome = "FABIO CONDO";

        try {
            adicionarNomeNaImagem(imagePath, outputImagePath, nome);
            System.out.println("Nome adicionado com sucesso!");
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
            System.out.println(answer);
            break;
        }

        /*
        for (int i=0; i<answer.length(); i++) {  // Percorrer para buscar oque interessa
            char c = answer.charAt(i);
            if(c == "}".charAt(0)){
                break;
            }
            System.out.println(c);
        }
        */

        socket.close();

        return answer;
    }

    public void adicionarNomeNaImagem(String imagePath, String outputImagePath, String nome) throws IOException {
        // Carrega a imagem original
        File inputFile = new File(imagePath);
        BufferedImage imagem = ImageIO.read(inputFile);

        // Cria um objeto Graphics2D para desenhar na imagem
        Graphics2D g2d = imagem.createGraphics();

        // Configura a fonte e a cor do texto
        Font fonte = new Font("Arial", Font.PLAIN, 50);
        g2d.setFont(fonte);
        g2d.setColor(Color.WHITE);

        // Define a posição onde o nome será adicionado (neste exemplo, no canto superior esquerdo)
        int x = 70;
        int y = 500;

        // Desenha o nome na imagem
        g2d.drawString(nome, x, y);

        // Libera os recursos do objeto Graphics2D
        g2d.dispose();

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
