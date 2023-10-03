package org.elypso;

import org.elypso.domain.Pedido;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.elypso.constatnt.Constant.*;


@SpringBootApplication
public class Main {
    public static void main(String[] args) throws NomeOuNumeroVazioException, IOException, FileNotFoundException {
        SpringApplication.run(Main.class, args);
        System.out.println("Hello elypso!");
        new File(IMAGES_FOLDER).mkdirs();

        //visualizarFontesInstaladasNoSistemaOperacional();
        //adicionarNomeNumeroNaImagem();
    }

    public static void adicionarNomeNumeroNaImagem() throws IOException, NomeOuNumeroVazioException, FileNotFoundException {

        Pedido pedido = new Pedido("FABIO CONDO", "MSSSC 23006016 - 01", null);

        String imagePath = IMAGES_FOLDER + FORWARD_SLASH + IMAGEM_FRONTAL_SEM_NOME;
        String outputImagePath = IMAGES_FOLDER + FORWARD_SLASH + IMAGEM_FRONTAL_GERADA_COM_DADOS;

        if(pedido.getNome().equals("") || pedido.getNumero().equals("")){
            //LOGGER.error("O nome ou número está vázio");
            throw new NomeOuNumeroVazioException("O nome ou número está vázio");
        }

        // Crie um objeto File com o caminho completo para o arquivo
        File arquivo = new File(IMAGES_FOLDER, IMAGEM_FRONTAL_SEM_NOME);

        // Verifique se o arquivo existe
        if (!arquivo.exists()) {
            throw new FileNotFoundException("A imagem não existe no diretório.");
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
        Font fonteNome = new Font("Agency FB", Font.BOLD, 260);  // Bebas Neue
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
        g2d_numeroClientTextBold.dispose();
        g2d_numero.dispose();

        // Salva a imagem resultante
        File outputFile = new File(outputImagePath);
        ImageIO.write(imagem, "bmp", outputFile);
    }

    public static void visualizarFontesInstaladasNoSistemaOperacional(){
        String[] fontNames = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (String fontName : fontNames) {
            System.out.println(fontName);
        }
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        //corsConfiguration.setAllowedOrigins(Collections.singletonList("https://fabio-condo.github.io"));
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}