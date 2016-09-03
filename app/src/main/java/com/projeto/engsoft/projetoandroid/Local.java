package com.projeto.engsoft.projetoandroid;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Aluno on 15/08/2016.
 */
public class Local implements Serializable{

    private String nome;
    private String tipo;
    private List<String> comidas;
    private double lat;
    private double longt;
    private String endereco;


    public Local(String nome, String tipo, List<String> comidas, double lat, double longt, String endereco) {
        this.nome = nome;
        this.tipo = tipo;
        this.comidas = comidas;
        this.lat = lat;
        this.longt = longt;
        this.endereco = endereco;

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

    public String getEndereco() {return endereco;}






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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Local local = (Local) o;
        return Double.compare(local.lat, lat) == 0 &&
                Double.compare(local.longt, longt) == 0 &&
                Objects.equals(nome, local.nome) &&
                Objects.equals(tipo, local.tipo) &&
                Objects.equals(comidas, local.comidas) &&
                Objects.equals(endereco, local.endereco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, tipo, comidas, lat, longt, endereco);
    }
}
