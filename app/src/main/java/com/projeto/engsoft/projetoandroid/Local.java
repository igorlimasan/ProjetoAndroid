package com.projeto.engsoft.projetoandroid;

import java.util.List;

/**
 * Created by Aluno on 15/08/2016.
 */
public class Local {

    private String nome;
    private String tipo;
    private List<String> comidas;

    public Local(String nome, String tipo, List<String> comidas) {
        this.nome = nome;
        this.tipo = tipo;
        this.comidas = comidas;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public List<String> getComidas() {
        return comidas;
    }



    @Override
    public String toString() {
        return  nome + " " + " "+ tipo + " " + " " + comidas;
    }
}
