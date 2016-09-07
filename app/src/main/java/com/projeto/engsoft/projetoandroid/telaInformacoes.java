package com.projeto.engsoft.projetoandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class TelaInformacoes extends AppCompatActivity{
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
        Intent myIntent = new Intent(this, TelaMapa.class);
        myIntent.putExtra("valor",loc);
        this.startActivity(myIntent);

//        double [] dados = {-23.14426,-45.77867};
//        Intent myIntent = new Intent(this, TelaMapa.class);
//        myIntent.putExtra("valor",dados);
//        this.startActivity(myIntent);


    }

}
