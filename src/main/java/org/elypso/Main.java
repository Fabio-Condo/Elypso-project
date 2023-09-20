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
    }
}