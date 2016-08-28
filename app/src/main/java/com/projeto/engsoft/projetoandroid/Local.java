package com.projeto.engsoft.projetoandroid;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

/**
 * Created by Aluno on 15/08/2016.
 */
public class Local implements Serializable{

    private String nome;
    private String tipo;
    private List<String> comidas;



    private double lat;
    private double longt;

    public Local(String nome, String tipo, List<String> comidas, double lat, double longt) {
        this.nome = nome;
        this.tipo = tipo;
        this.comidas = comidas;
        this.lat = lat;
        this.longt = longt;
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

    public double getLat() { return lat; }

    public double getLongt() {return longt; }



    @Override
    public String toString() {
        Locale loc = Locale.getDefault();
        if(loc.getLanguage().equals("pt"))
            return  "Nome: "+nome + "\nTipo: " + tipo + "\nComidas: " + comidas.toString().substring(1).replace(']',' ');
        else if(loc.getLanguage().equals("en"))
            return  "Name: "+nome + "\nType: " + tipo + "\nMenu Item: " + comidas.toString().substring(1).replace(']',' ');
        else if(loc.getLanguage().equals("es"))
            return  "Nombre: "+nome + "\nTipo: " + tipo + "\nComidas: " + comidas.toString().substring(1).replace(']',' ');
        return  "Name: "+nome + "\nType: " + tipo + "\nMenu Item: " + comidas.toString().substring(1).replace(']',' ');
    }
}
