package org.elypso.commandsService;

import org.elypso.enumerations.Fita;
import org.springframework.stereotype.Service;

@Service
public class ElypsoCommandsService {

    String impressora = "Evolis Elypso";
    //String impressora = "Evolis Primacy";
    String timeout = "5000";
    //String fitaSelecionada = "RM_KO";
    //String fitaSelecionada = "RC_YMCKO"; // Colorida
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

    public String gerarComandoConfigurarProcessoImpressao(Fita fitaSelecionada) {

        return "{\n" +
                "\t\"jsonrpc\":\"2.0\",\n" +
                "\t\"id\":\"1\",\n" +
                "\t\"method\":\"PRINT.Set\",\n" +
                "\t\"params\":\n" +
                "\t{\n" +

                "\t\t\"data\":\"FColorBrightness=VAL20;FColorContrast=VAL20;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;IGIQLACC=VAL10;IGIQLACM=VAL10;IGIQLACY=VAL10;GSmoothing=ADVSMOOTH;Resolution=DPI600300\",\n" + // Usar se a fita for RM_KO (COLORIDA). Brilho adicionado
                //"\t\t\"data\":\"GRibbonType=" + fitaSelecionada + ";Duplex=NONE;IGIQLACC=VAL10;IGIQLACM=VAL10;IGIQLACY=VAL10;GSmoothing=ADVSMOOTH;Resolution=DPI600300\",\n" + // Usar se a fita for RM_KO (COLORIDA). Brilho nao adicionado

                //"\t\t\"data\":\"FColorBrightness=VAL12;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;Resolution=DPI600300\",\n" + // Se a fita for KO (PRETA)
                //"\t\t\"data\":\"FColorBrightness=VAL12;GRibbonType=" + fitaSelecionada + ";Duplex=NONE;Resolution=DPI1200\",\n" +

                //"\t\t\"data\":\"GRibbonType=" + fitaSelecionada + ";Duplex=HORIZONTAL;GDuplexType=DUPLEX_CC;Resolution=DPI600300\",\n" + // Se a impressoa for Duplex e nao SIMPLEX
                //"\t\t\"data\":\"GRibbonType=" + fitaSelecionada + ";Duplex=NONE;GDuplexType=DUPLEX_MM;Resolution=DPI600300\",\n" + // Se a impressoa for Duplex e nao SIMPLEX

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

    /**
     * Realiza a verificacao da impressora, caso haja um erro ou aviso relacionado a uma impressora especifica apos a realizacao do fluxo de impressao(Print.Begin ate Print.Print)
     */
    public String criarComandoGetEvent() {
        return "{\n" +
                " \"id\": \"1\",\n" +
                " \"jsonrpc\": \"2.0\",\n" +
                " \"method\": \"SUPERVISION.GetEvent\",\n" +
                " \"params\": {\n" +
                " \"device\": \"" + impressora + "\",\n" +
                " \"timeout\":\"" + timeout + "\"\n" +
                " }\n" +
                "}";
    }


    /**
     * Realiza o cancelamento do erro, caso a impressora tenha algum erro associada a mesma naquele momento, +erro ou aviso relacionados a uma impressora especifica apos a realizacao do fluxo de impressao(Print.Begin ate Print.Print)
     */
    public String criarComandoSetEvent(String erro) {
        return "{\n" +
                " \"id\": \"1\",\n" +
                " \"jsonrpc\": \"2.0\",\n" +
                " \"method\": \"SUPERVISION.SetEvent\",\n" +
                " \"params\": {\n" +
                " \"action\": \"" + erro + ":CANCEL\",\n" +
                " \"device\": \"" + impressora + "\",\n" +
                " \"timeout\":\"" + timeout + "\"\n" +
                " }\n" +
                "}";
    }


}
