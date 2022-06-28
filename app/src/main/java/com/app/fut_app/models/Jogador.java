package com.app.fut_app.models;

import com.google.firebase.firestore.Exclude;

public class Jogador {

    @Exclude String id;
    public boolean situacao;
    public boolean chegada;
    private String nome;
    private String posicao;
    public int quantJogadores;

    public Jogador(){}

    public Jogador(String nome, String posicao){
        this.nome = nome;
        this.posicao = posicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getPosicao() { return posicao; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPosicao(String posicao) {this.posicao = posicao;}
}
