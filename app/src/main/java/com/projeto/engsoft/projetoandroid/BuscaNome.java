package com.projeto.engsoft.projetoandroid;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
    public List<Local> busca(){
        List<Local> lista = new LinkedList<Local>();

        for(Local l:locais)
        {
            if(l.getNome().toLowerCase().equals(valor.toLowerCase()))
            {
                lista.add(l);
                return lista;
            }
        }
        return null;
    }
}
