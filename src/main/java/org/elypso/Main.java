package org.elypso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;


@SpringBootApplication
public class Main {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
        System.out.println("Hello elypso!");

        /*
        try {
            adicionarNomeNaImagem("imagens/cartao_de_saude_test_print_front.bmp", "imagem_com_nome.bmp", "FABIO CONDO");
            System.out.println("Nome adicionado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

    }

    public static void adicionarNomeNaImagem(String imagePath, String outputImagePath, String nome) throws IOException {
        // Carrega a imagem original
        File inputFile = new File(imagePath);
        BufferedImage imagem = ImageIO.read(inputFile);

        // Cria um objeto Graphics2D para desenhar na imagem
        Graphics2D g2d = imagem.createGraphics();

        // Configura a fonte e a cor do texto
        Font fonte = new Font("Arial", Font.BOLD, 36);
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
}