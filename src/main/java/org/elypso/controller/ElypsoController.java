package org.elypso.controller;

import org.elypso.domain.PrinterCenterResponse;
import org.elypso.domain.Pedido;
import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.elypso.service.ElypsoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = {"/elypso"})
public class ElypsoController {

    ElypsoService elypsoService;

    public ElypsoController(ElypsoService elypsoService) {
        this.elypsoService = elypsoService;
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> uploadArquivoExcel(@RequestParam("file") MultipartFile file, @RequestParam("impressora") String impressora, @RequestParam("fita") Fita fita, @RequestParam("lado") Lado lado) throws IOException {
        elypsoService.imprimirDadosDoExcel(file, impressora, fita, lado);
        return ResponseEntity.status(HttpStatus.OK).body("Dados dos clientes impressos com sucesso!");
    }

    @PostMapping("/executarOperacaoUnica")
    public ResponseEntity<Pedido> executarOperacaoUnica(@RequestBody Pedido pedido) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.executarOperacaoUnica(pedido));
    }

    @GetMapping("/iniciarSequencia/{impressora}")
    public ResponseEntity<PrinterCenterResponse> iniciarSequencia(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.iniciarSequencia(impressora));
    }

    @GetMapping("/inicializarProcessoImpressao")
    public ResponseEntity<PrinterCenterResponse> inicializarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.inicializarProcessoImpressao("Evolis Elypso", "JOB000001"));
    }

    @GetMapping("/configurarProcessoImpressao")
    public ResponseEntity<PrinterCenterResponse> configurarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.configurarProcessoImpressao(Fita.RM_KO, "JOB000001"));
    }

    @GetMapping("/definirBitmapImpressaoFrontal")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoFrontal() throws IOException, NomeOuNumeroVazioException, FileNotFoundException {
        Pedido pedido = new Pedido("NOME TESTE", "NUMERO CLIENTE", "NUMERO APOLICE", "Evolis Elypso", Fita.RM_KO, Lado.FRENTE_VERSO, "JOB000001");
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoFrontal(pedido));
    }

    @GetMapping("/definirBitmapImpressaoTrazeiro")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoTrazeiro() throws IOException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoTrazeiro("JOB000001"));
    }

    @GetMapping("/realizarImpressao")
    public ResponseEntity<PrinterCenterResponse> realizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.realizarImpressao("JOB000001"));
    }

    @GetMapping("/finalizarImpressao")
    public ResponseEntity<PrinterCenterResponse> finalizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarImpressao("JOB000001"));
    }

    @GetMapping("/finalizarSequencia/{impressora}")
    public ResponseEntity<PrinterCenterResponse> finalizarSequencia(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarSequencia(impressora));
    }

    @GetMapping("/ligarOuReinicializarHardwareImpressora/{impressora}")
    public ResponseEntity<PrinterCenterResponse> ligarOuReinicializarHardwareImpressora(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.ligarOuReinicializarHardwareImpressora(impressora));
    }

    @GetMapping("/status/{impressora}")
    public ResponseEntity<PrinterCenterResponse> verificarStatus(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarStatus(impressora));
    }

    @GetMapping("/reinicializarComunicacao/{impressora}")
    public ResponseEntity<PrinterCenterResponse> reinicializarComunicacoesComAImpressora(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.reinicializarComunicacoesComAImpressora(impressora));
    }

    @GetMapping("/verificarFita/{impressora}")
    public ResponseEntity<PrinterCenterResponse> verificarFita(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarFita(impressora));
    }

    @GetMapping("/getEvent/{impressora}")
    public ResponseEntity<PrinterCenterResponse> getEvent(@PathVariable("impressora") String impressora) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.getEvent(impressora));
    }
}
