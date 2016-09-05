package com.projeto.engsoft.projetoandroid;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class telaPrincipal extends AppCompatActivity {

    private List<Local> local;
    private TextView textResult = null;
    private ListView lv;
    private Spinner spin;
    private EditText texto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //textResult = (TextView) findViewById(R.id.result);
        final StringBuilder result = new StringBuilder();
        spin = (Spinner) findViewById(R.id.criterios);
        spin.requestFocus();
        texto = (EditText) findViewById(R.id.valor);


        try {
           local = Connection.getInstance().sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lv=(ListView) findViewById(R.id.lista);
        List<String> lista = Connection.getInstance().returnNames(local);
        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.texto_lista,lista));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent = new Intent(telaPrincipal.this, telaInformacoes.class);
                myIntent.putExtra("valor",local.get(position));
                startActivity(myIntent);
            }
        });



    }

    public void onClick(View view) {
        Local res = null;
        List<Local> resL;
        if(!TextUtils.isEmpty(texto.getText().toString()))
        {
            if(spin.getSelectedItemPosition() == 0)
            {
                res = new BuscaNome(local,texto.getText().toString()).busca();
                if(res==null)
                {
                    Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else
                {
                    Intent myIntent = new Intent(this, telaInformacoes.class);
                    myIntent.putExtra("valor", res);
                    this.startActivity(myIntent);
                }
            }

            if(spin.getSelectedItemPosition() == 1)
            {
                resL = new BuscaTipo(local,texto.getText().toString()).busca();
                if(resL.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else
                {
                    Intent myIntent = new Intent(this, telaMapa.class);
                    myIntent.putExtra("valor", resL.get(0));
                    this.startActivity(myIntent);
                }
            }


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Digite um valor para pesquisa",Toast.LENGTH_SHORT);
            texto.requestFocus();
        }



//        double [] dados = {-23.14426,-45.77867};
//        Intent myIntent = new Intent(this, telaMapa.class);
//        myIntent.putExtra("valor",dados);
//        this.startActivity(myIntent);


    }
}
