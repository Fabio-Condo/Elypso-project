package org.elypso.controller;

import org.elypso.domain.ElypsoResponse;
import org.elypso.domain.Pedido;
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
    public ResponseEntity<Pedido> executarOperacaoUnica(@RequestBody Pedido pedido) throws IOException, PedidoComandoException, NomeOuNumeroVazioException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.executarOperacaoUnica(pedido));
    }

    @GetMapping("/iniciarSequencia")
    public ResponseEntity<ElypsoResponse> iniciarSequencia() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.iniciarSequencia());
    }

    @GetMapping("/inicializarProcessoImpressao")
    public ResponseEntity<ElypsoResponse> inicializarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.inicializarProcessoImpressao());
    }

    @GetMapping("/configurarProcessoImpressao")
    public ResponseEntity<ElypsoResponse> configurarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.configurarProcessoImpressao());
    }

    @GetMapping("/definirBitmapImpressaoFrontal")
    public ResponseEntity<ElypsoResponse> definirBitmapImpressaoFrontal() throws IOException, NomeOuNumeroVazioException {
        Pedido pedido = new Pedido("NOME TESTE", "NUMERO TESTE");
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoFrontal(pedido));
    }

    @GetMapping("/definirBitmapImpressaoTrazeiro")
    public ResponseEntity<ElypsoResponse> definirBitmapImpressaoTrazeiro() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoTrazeiro());
    }

    @GetMapping("/realizarImpressao")
    public ResponseEntity<ElypsoResponse> realizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.realizarImpressao());
    }

    @GetMapping("/finalizarImpressao")
    public ResponseEntity<ElypsoResponse> finalizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarImpressao());
    }

    @GetMapping("/finalizarSequencia")
    public ResponseEntity<ElypsoResponse> finalizarSequencia() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarSequencia());
    }

    @GetMapping("/ligarOuReinicializarHardwareImpressora")
    public ResponseEntity<ElypsoResponse> ligarOuReinicializarHardwareImpressora() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.ligarOuReinicializarHardwareImpressora());
    }

    @GetMapping("/status")
    public ResponseEntity<ElypsoResponse> verificarStatus() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarStatus());
    }

    @GetMapping("/reinicializarComunicacao")
    public ResponseEntity<ElypsoResponse> reinicializarComunicacoesComAImpressora() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.reinicializarComunicacoesComAImpressora());
    }
}
