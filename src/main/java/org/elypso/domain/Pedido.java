package org.elypso.domain;

import org.elypso.enumerations.Fita;
import org.elypso.enumerations.Lado;

public class Pedido {

    private String nome;

    private String numeroCliente;

    private String numeroApolice;

    private String impressora;

    private Fita fita;

    private Lado lado;

    private  String sessao;

    public Pedido() {
    }

    public Pedido(String nome, String numeroCliente, String numeroApolice, String impressora, Fita fita, Lado lado, String sessao) {
        this.nome = nome;
        this.numeroCliente = numeroCliente;
        this.numeroApolice = numeroApolice;
        this.impressora = impressora;
        this.fita = fita;
        this.lado = lado;
        this.sessao = sessao;
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

    public String getImpressora() {
        return impressora;
    }

    public void setImpressora(String impressora) {
        this.impressora = impressora;
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

    public String getSessao() {
        return sessao;
    }

    public void setSessao(String sessao) {
        this.sessao = sessao;
    }
}
