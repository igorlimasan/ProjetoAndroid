package com.projeto.engsoft.projetoandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TelaListaEncontrados extends AppCompatActivity {
    List<Local> found;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_encontrados);
        Intent intent = getIntent();
        found = (List<Local>)intent.getSerializableExtra("valor");
        if(found.size() == Connection.getInstance().numeroLugares()) setTitle("Locais cadastrados");
        else setTitle(R.string.title_activity_lista_encontrados);


        lv = (ListView) findViewById(R.id.lista);

        lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.texto_lista,Connection.getInstance().returnNames(found)));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(found.size() == Connection.getInstance().numeroLugares()){
                    Intent myIntent = new Intent(TelaListaEncontrados.this, TelaInformacoes.class);
                    myIntent.putExtra("valor", found.get(position));
                    startActivity(myIntent);
                }
                else
                {
                    Intent myIntent = new Intent(TelaListaEncontrados.this, TelaMapa.class);
                    myIntent.putExtra("valor", found.get(position));
                    startActivity(myIntent);
                }
            }
        });


    }
}
