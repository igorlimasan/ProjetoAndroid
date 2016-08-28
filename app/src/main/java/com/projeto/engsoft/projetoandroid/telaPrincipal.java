package com.projeto.engsoft.projetoandroid;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class telaPrincipal extends AppCompatActivity {

    private List<Local> local;
    private TextView textResult = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //textResult = (TextView) findViewById(R.id.result);
        final StringBuilder result = new StringBuilder();


        try {
           local = Connection.getInstance().sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void onClick(View view) {
          Intent myIntent = new Intent(this, telaInformacoes.class);
          myIntent.putExtra("valor",local.get(0));
          this.startActivity(myIntent);

//        double [] dados = {-23.14426,-45.77867};
//        Intent myIntent = new Intent(this, telaMapa.class);
//        myIntent.putExtra("valor",dados);
//        this.startActivity(myIntent);


    }
}
