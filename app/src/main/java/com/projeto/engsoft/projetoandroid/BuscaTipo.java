package com.projeto.engsoft.projetoandroid;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 03/09/2016.
 */
public class BuscaTipo {
    private List<Local> locais;
    private String valor;

    public BuscaTipo(List<Local> locais, String valor) {
        this.locais = locais;
        this.valor = valor;
    }


    public List<Local> busca(){
        List<Local> res = new LinkedList<Local>();

        for(Local l:locais)
        {
            if(l.getTipo().toLowerCase().equals(valor.toLowerCase()))
            {
                res.add(l);
            }
        }
        return res;
    }
}
