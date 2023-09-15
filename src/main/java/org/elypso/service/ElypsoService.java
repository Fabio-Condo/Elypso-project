package org.elypso.service;

import org.elypso.commands.ElypsoCommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        String filePath = "imagens/cartao_de_saude_test_print_front.bmp";
        String imagemEmDadosBase64 = converterBMPImageParaString(filePath);
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
}
