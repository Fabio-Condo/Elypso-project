package org.elypso.domain;

import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;

public class Pedido {

    private String nome;
    private String numeroApolice;
    private String numeroCliente;

    private Fita fita;

    private Lado lado;

    public Pedido() {
    }

    public Pedido(String nome, String numeroApolice, String numeroCliente, Fita fita, Lado lado) {
        this.nome = nome;
        this.numeroApolice = numeroApolice;
        this.numeroCliente = numeroCliente;
        this.fita = fita;
        this.lado = lado;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    public String getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(String numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public Fita getFita() {
        return fita;
    }

    public void setFita(Fita fita) {
        this.fita = fita;
    }

    public Lado getLado() {
        return lado;
    }

    public void setLado(Lado lado) {
        this.lado = lado;
    }
}
