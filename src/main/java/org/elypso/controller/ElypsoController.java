package org.elypso.controller;

import org.elypso.model.Pedido;
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
    public ResponseEntity<String> executarOperacaoUnica(@RequestBody Pedido pedido) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.executarOperacaoUnica(pedido));
    }

    @GetMapping("/iniciarSequencia")
    public ResponseEntity<String> iniciarSequencia() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.iniciarSequencia());
    }

    @GetMapping("/inicializarProcessoImpressao")
    public ResponseEntity<String> inicializarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.inicializarProcessoImpressao());
    }

    @GetMapping("/configurarProcessoImpressao")
    public ResponseEntity<String> configurarProcessoImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.configurarProcessoImpressao());
    }

    @GetMapping("/definirBitmapImpressaoFrontal")
    public ResponseEntity<String> definirBitmapImpressaoFrontal() throws IOException {
        Pedido pedido = new Pedido("NOME TESTE", "NUMERO TESTE");
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoFrontal(pedido));
    }

    @GetMapping("/definirBitmapImpressaoTrazeiro")
    public ResponseEntity<String> definirBitmapImpressaoTrazeiro() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.definirBitmapImpressaoTrazeiro());
    }

    @GetMapping("/realizarImpressao")
    public ResponseEntity<String> realizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.realizarImpressao());
    }

    @GetMapping("/finalizarImpressao")
    public ResponseEntity<String> finalizarImpressao() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.finalizarImpressao());
    }

    @GetMapping("/ligarOuReinicializarHardwareImpressora")
    public ResponseEntity<String> ligarOuReinicializarHardwareImpressora() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.ligarOuReinicializarHardwareImpressora());
    }

    @GetMapping("/status")
    public ResponseEntity<String> verificarStatus() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.verificarStatus());
    }

    @GetMapping("/reinicializarComunicacao")
    public ResponseEntity<String> reinicializarComunicacoesComAImpressora() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(elypsoService.reinicializarComunicacoesComAImpressora());
    }
}
