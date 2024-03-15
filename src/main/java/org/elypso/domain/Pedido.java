package org.elypso.domain;

import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;

public class Pedido {

    private String nome;
    private String numero;
    private String numeroCliente;

    private Fita fita;

    private Lado lado;

    public Pedido() {
    }

    public Pedido(String nome, String numero, String numeroCliente, Fita fita, Lado lado) {
        this.nome = nome;
        this.numero = numero;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
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
