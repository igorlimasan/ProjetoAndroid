package com.projeto.engsoft.projetoandroid;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;

public class telaInformacoes extends AppCompatActivity {
    private Local loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_informacoes);





        Intent intent = getIntent();
        loc = (Local) intent.getSerializableExtra("valor");
        TextView texto = (TextView) findViewById(R.id.resultText);
        texto.setText(loc.toString());
    }

    public void verMapa(View view) {
        Intent myIntent = new Intent(this, telaMapa.class);
        myIntent.putExtra("valor",loc);
        this.startActivity(myIntent);

//        double [] dados = {-23.14426,-45.77867};
//        Intent myIntent = new Intent(this, telaMapa.class);
//        myIntent.putExtra("valor",dados);
//        this.startActivity(myIntent);


    }

}
