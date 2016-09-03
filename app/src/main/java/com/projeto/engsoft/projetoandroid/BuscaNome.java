package com.projeto.engsoft.projetoandroid;

import java.util.List;

/**
 * Created by User on 03/09/2016.
 */
public class BuscaNome implements Busca {
    private List<Local> locais;
    private String valor;

    public BuscaNome(List<Local> locais, String valor) {
        this.locais = locais;
        this.valor = valor;
    }

    @Override
    public Local busca(){

        for(Local l:locais)
        {
            if(l.getNome().toLowerCase().equals(valor.toLowerCase()))
            {
                return l;
            }
        }
        return null;
    }
}
