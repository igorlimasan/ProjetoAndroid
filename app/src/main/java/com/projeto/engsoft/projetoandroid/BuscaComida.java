package com.projeto.engsoft.projetoandroid;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Aluno on 05/09/2016.
 */
public class BuscaComida implements Busca {

    private List<Local> locais;
    private String valor;

    public BuscaComida(List<Local> locais, String valor) {
        this.locais = locais;
        this.valor = valor;
    }


    public List<Local> busca(){
        List<Local> res = new LinkedList<Local>();

        for(Local l:locais)
        {
            if(l.getComidas().contains(valor)) res.add(l);
        }
        return res;
    }
}
