package org.elypso.domain;

import org.elypso.ebumerations.Fita;

public class Pedido {

    private String nome;
    private String numero;
    private Fita fita;

    public Pedido() {
    }

    public Pedido(String nome, String numero, Fita fita) {
        this.nome = nome;
        this.numero = numero;
        this.fita = fita;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Fita getFita() {
        return fita;
    }

    public void setFita(Fita fita) {
        this.fita = fita;
    }
}
