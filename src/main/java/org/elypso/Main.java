package org.elypso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONObject;

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

        // Sua string JSON
        String jsonString = "{\n" +
                "\t\"jsonrpc\": \"2.0\",\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"method\": \"PRINT.SetBitmap\",\n" +
                "\t\"params\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"session\": \"" + "sessao" + "\",\n" +
                "\t\t\t\"timeout\": \"" + "timeout" + "\",\n" +
                "\t\t\t\"face\": \"front\",\n" +
                "\t\t\t\"panel\": \"resin\",\n" +
                "\t\t\t\"data\": \"" + "dadosBase64" + "\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"session\": \"" + "sessao" + "\",\n" +
                "\t\t\t\"timeout\": \"" + "timeout" + "\",\n" +
                "\t\t\t\"face\": \"back\",\n" +
                "\t\t\t\"panel\": \"resin\",\n" +
                "\t\t\t\"data\": \"" + "dadosBase64" + "\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";

        try {
            // Parse a string JSON para um objeto JSON
            JSONObject jsonObject = new JSONObject(jsonString);

            // Agora você pode acessar os valores do objeto JSON
            /*String nome = jsonObject.getString("nome");
            int idade = jsonObject.getInt("idade");
            String cidade = jsonObject.getString("cidade");

            // Exibe os valores
            System.out.println("Nome: " + nome);
            System.out.println("Idade: " + idade);
            System.out.println("Cidade: " + cidade);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}