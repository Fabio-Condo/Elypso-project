package org.elypso.controller;

import org.elypso.domain.PrinterCenterResponse;
import org.elypso.domain.Pedido;
import org.elypso.exception.domain.FileNotFoundException;
import org.elypso.exception.domain.NomeOuNumeroVazioException;
import org.elypso.exception.domain.PedidoComandoException;
import org.elypso.service.ElypsoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(path = {"/elypso"})
public class ElypsoController {

    ElypsoService elypsoService;

    public ElypsoController(ElypsoService elypsoService) {
        this.elypsoService = elypsoService;
    }

    @PostMapping("/executarOperacaoUnica")
    public ResponseEntity<Pedido> executarOperacaoUnica(@RequestBody Pedido pedido) throws IOException, PedidoComandoException, NomeOuNumeroVazioException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.executarOperacaoUnica(pedido));
    }

    @GetMapping("/iniciarSequencia")
    public ResponseEntity<PrinterCenterResponse> iniciarSequencia() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.iniciarSequencia());
    }

    @GetMapping("/inicializarProcessoImpressao")
    public ResponseEntity<PrinterCenterResponse> inicializarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.inicializarProcessoImpressao());
    }

    @GetMapping("/configurarProcessoImpressao")
    public ResponseEntity<PrinterCenterResponse> configurarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.configurarProcessoImpressao());
    }

    @GetMapping("/definirBitmapImpressaoFrontal")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoFrontal() throws IOException, NomeOuNumeroVazioException, FileNotFoundException {
        Pedido pedido = new Pedido("NOME TESTE", "NUMERO TESTE");
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoFrontal(pedido));
    }

    @GetMapping("/definirBitmapImpressaoTrazeiro")
    public ResponseEntity<PrinterCenterResponse> definirBitmapImpressaoTrazeiro() throws IOException, FileNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoTrazeiro());
    }

    @GetMapping("/realizarImpressao")
    public ResponseEntity<PrinterCenterResponse> realizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.realizarImpressao());
    }

    @GetMapping("/finalizarImpressao")
    public ResponseEntity<PrinterCenterResponse> finalizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarImpressao());
    }

    @GetMapping("/finalizarSequencia")
    public ResponseEntity<PrinterCenterResponse> finalizarSequencia() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarSequencia());
    }

    @GetMapping("/ligarOuReinicializarHardwareImpressora")
    public ResponseEntity<PrinterCenterResponse> ligarOuReinicializarHardwareImpressora() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.ligarOuReinicializarHardwareImpressora());
    }

    @GetMapping("/status")
    public ResponseEntity<PrinterCenterResponse> verificarStatus() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarStatus());
    }

    @GetMapping("/reinicializarComunicacao")
    public ResponseEntity<PrinterCenterResponse> reinicializarComunicacoesComAImpressora() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.reinicializarComunicacoesComAImpressora());
    }

    @GetMapping("/verificarFita")
    public ResponseEntity<PrinterCenterResponse> verificarFita() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarFita());
    }
}
