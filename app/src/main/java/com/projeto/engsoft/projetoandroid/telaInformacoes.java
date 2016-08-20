package com.projeto.engsoft.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class telaInformacoes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_informacoes);


        Intent intent = getIntent();
        String res = intent.getStringExtra("valor");
        TextView texto = (TextView) findViewById(R.id.resultText);
        texto.setText(res);
    }

}
