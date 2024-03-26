package org.elypso.constatnt;

public class Constant {

    public static final String IMAGES_FOLDER = System.getProperty("user.home") + "/SPC V2/imagens/";
    //private static final String IMAGES_PATH = "/app/imagens"; // Caminho do volume no contêiner// Use quando fizer deploymente no docker
    public static final String IMAGES_PATH = "C:\\Users\\user\\Desktop\\SPC-V2\\imagens"; // Deployment no proproprio server ou run usando um jar
    public static final String FORWARD_SLASH = "/";
    public static final String IMAGEM_BRANCA = "imagem-branca.bmp";
    public static final String IMAGEM_FRONTAL_SEM_NOME = "imagem-frontal-sem-dados.bmp";
    public static final String IMAGEM_TRAZEIRA = "imagem-trazeira.bmp";
    public static final String IMAGEM_FRONTAL_GERADA_COM_DADOS = "ultima-imagem-frontal-gerada-com-dados.bmp";
    public static final String FRONT_IMAGE_PATH = IMAGES_PATH + FORWARD_SLASH + IMAGEM_FRONTAL_SEM_NOME;
    public static final String BACK_IMAGE_PATH = IMAGES_PATH + FORWARD_SLASH + IMAGEM_TRAZEIRA;
    public static final String OUTPUT_IMAGE_PATH = IMAGES_PATH + FORWARD_SLASH + IMAGEM_FRONTAL_GERADA_COM_DADOS;
    public static final String WHITE_IMAGE_PATH = IMAGES_PATH + FORWARD_SLASH + IMAGEM_BRANCA;
}
