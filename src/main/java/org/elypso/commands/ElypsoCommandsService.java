package org.elypso.commands;

import org.springframework.stereotype.Service;

import static org.elypso.constatnt.TipoFita.*;

@Service
public class ElypsoCommandsService {

    String impressora = "Evolis Elypso";
    //String impressora = "Evolis Primacy";
    String timeout = "5000";
    //String fitaSelecionada = FITA_PRETA_COM_OVERLAY;
    String fitaSelecionada = FITA_COLORIDA; // Colorida
    String sessao = "JOB000001";

    public String gerarComandoIniciarSequencia() {
        String com = "Ss";

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"CMD.SendCommand\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"command\":\"" + com + "\",\n" +
                "\t\t\"device\":\"" + impressora + "\", \n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandoInicializarProcessoImpressao() {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Begin\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"device\":\"" + impressora + "\",\n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}\n";
    }

    public String gerarComandoConfigurarProcessoImpressao() {

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Set\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"data\":\"FColorBrightness=VAL20;FColorContrast=VAL20;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;Resolution=DPI600300\",\n" +
                //"\t\t\"data\":\"FColorBrightness=VAL12;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;Resolution=DPI600300\",\n" +
                //"\t\t\"data\":\"FColorBrightness=VAL12;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;Resolution=DPI1200\",\n" +
                //"\t\t\"data\":\"GRibbonType=" + fitaSelecionada + ";Duplex=HORIZONTAL;GDuplexType=DUPLEX_CC;Resolution=DPI600300\",\n" +
                //"\t\t\"data\":\"GRibbonType=" + fitaSelecionada + ";Duplex=NONE;GDuplexType=DUPLEX_MM;Resolution=DPI600300\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\",\n" +
                "\t\t\"session\":\"" + sessao + "\"\n" +
                "\t}\n" +
                "}";

                // Duplex funciona no Primacy
                // Duplex=HORIZONTAL -> Nao aceita

    }

    public String gerarComandoDefinirBitmapImpressaoFrontal(String dadosBase64) {

        dadosBase64 = "base64:" + dadosBase64;

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.SetBitmap\",\n" +
                "\t\"params\":\n" +
                "\t{ \n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\",\n" +
                "                \"face\":\"front\",\n" +
                "                \"panel\":\"color\",\n" +
                "                \"data\":\"" + dadosBase64 + "\"\n" +
                "    }\n" +
                "}";

        /*
        return "{\n" +
                "\t\"jsonrpc\": \"2.0\",\n" +
                "\t\"id\": \"1\",\n" +
                "\t\"method\": \"PRINT.SetBitmap\",\n" +
                "\t\"params\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"session\": \"" + sessao + "\",\n" +
                "\t\t\t\"timeout\": \"" + timeout + "\",\n" +
                "\t\t\t\"face\": \"front\",\n" +
                "\t\t\t\"panel\": \"resin\",\n" +
                "\t\t\t\"data\": \"" + dadosBase64 + "\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"session\": \"" + sessao + "\",\n" +
                "\t\t\t\"timeout\": \"" + timeout + "\",\n" +
                "\t\t\t\"face\": \"back\",\n" +
                "\t\t\t\"panel\": \"resin\",\n" +
                "\t\t\t\"data\": \"" + dadosBase64 + "\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
         */

    }

    public String gerarComandoDefinirBitmapImpressaoTrazeiro(String dadosBase64) {

        dadosBase64 = "base64:" + dadosBase64;

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.SetBitmap\",\n" +
                "\t\"params\":\n" +
                "\t{ \n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\",\n" +
                "                \"face\":\"back\",\n" +
                "                \"panel\":\"color\",\n" +
                "                \"data\":\"" + dadosBase64 + "\"\n" +
                "    }\n" +
                "}";

    }

    public String gerarComandoRealizarImpressao() {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Print\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandoFinalizarImpressao() {
        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.End\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"session\":\"" + sessao + "\",\n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandoFinalizarSequencia() {
        String com = "Se";

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"CMD.SendCommand\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"command\":\"" + com + "\",\n" +
                "\t\t\"device\":\"" + impressora + "\", \n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";
    }

    public String gerarComandosLigarOuReinicializarHardwareImpressora() {
        String com = "Srs";

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"CMD.SendCommand\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +
                "\t\t\"command\":\"" + com + "\",\n" +
                "\t\t\"device\":\"" + impressora + "\", \n" +
                "\t\t\"timeout\":\"" + timeout + "\"\n" +
                "\t}\n" +
                "}";

    }

    public String gerarComandoVerificarStatusImpressora() {

        return "{\r\n" +
                "	\"jsonrpc\":\"2.0\",\r\n" +
                "	\"id\":\"1\",\r\n" +
                "	\"method\":\"SUPERVISION.List\",\r\n" +
                "	\"params\":\r\n" +
                "	{\r\n" +
                "		\"device\":\"" + impressora + "\", \n" +
                "       \"timeout\":\"" + timeout + "\",\n" +
                "        \"level\":\"2\"\r\n" +
                "	}\r\n" +
                "}\r\n" +
                "";
    }

    public String gerarComandoReinicializarComunicacoesComAImpressora() {
        return "{\n" +
                " \"id\": \"1\",\n" +
                " \"jsonrpc\": \"2.0\",\n" +
                " \"method\": \"CMD.ResetCom\",\n" +
                " \"params\": {\n" +
                " \"device\": \"" + impressora + "\"\n" +
                " \"timeout\": \"" + timeout + "\",\n" +
                " }\n" +
                "}\n";
    }

    public String gerarComandoVerificarFita(int opcao) {

        String comando1 = "Rkt;type;";
        String comando2 = "Rkt;label;";

        if (opcao == 1) {
            return "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"id\":\"1\",\n" +
                    "\t\"method\":\"CMD.SendCommand\",\n" +
                    "\t\"params\":\n" +
                    "\t{\n" +
                    "\t\t\"command\":\"" + comando1 + "\",\n" +
                    "\t\t\"device\":\"" + impressora + "\", \n" +
                    "\t\t\"timeout\":\"" + timeout + "\"\n" +
                    "\t}\n" +
                    "}";
        } else {
            return "{\n" +
                    "\t\"jsonrpc\":\"2.0\",\n" +
                    "\t\"id\":\"1\",\n" +
                    "\t\"method\":\"CMD.SendCommand\",\n" +
                    "\t\"params\":\n" +
                    "\t{\n" +
                    "\t\t\"command\":\"" + comando2 + "\",\n" +
                    "\t\t\"device\":\"" + impressora + "\", \n" +
                    "\t\t\"timeout\":\"" + timeout + "\"\n" +
                    "\t}\n" +
                    "}";
        }
    }
}
