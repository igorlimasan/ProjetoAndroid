package com.projeto.engsoft.projetoandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.Locale;


public class TelaPrincipal extends AppCompatActivity {

    private List<Local> local;
    private ListView lv;
    private Spinner spin;
    private EditText texto;
    private Button botao;
    private Locale loc = Locale.getDefault();

    private TextView textoInternet;
    private Button botaoInternet;

    private ImageView imageInternet;


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
        lv=(ListView) findViewById(R.id.lista);
        botao = (Button) findViewById(R.id.pesq);



        textoInternet = (TextView) findViewById(R.id.textoInternet);
        botaoInternet = (Button) findViewById(R.id.buttonInternet);
        imageInternet = (ImageView) findViewById(R.id.imagemInternet);




        if(!isInternetAvailable())
        {

            spin.setVisibility(View.GONE);
            texto.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
            botao.setVisibility(View.GONE);
            textoInternet.setVisibility(View.VISIBLE);
            botaoInternet.setVisibility(View.VISIBLE);
            imageInternet.setVisibility(View.VISIBLE);

        }
        else
        {
            trazerDados();
            spin.setVisibility(View.VISIBLE);
            texto.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);

            List<String> lista = Connection.getInstance().returnNames(local);
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.texto_lista,lista));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Intent myIntent = new Intent(TelaPrincipal.this, TelaInformacoes.class);
                    myIntent.putExtra("valor", local.get(position));
                    startActivity(myIntent);
                }
            });
        }









    }

    public void onClick(View view) {
        Local res = null;
        List<Local> resL;
        if(!TextUtils.isEmpty(texto.getText().toString()))
        {
            if(spin.getSelectedItemPosition() == 0)
            {
                res = new BuscaNome(local,texto.getText().toString()).busca().get(0);
                if(res==null)
                {
                    Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else
                {
                    Intent myIntent = new Intent(this, TelaInformacoes.class);
                    myIntent.putExtra("valor", res);
                    this.startActivity(myIntent);
                }
            }

            else if(spin.getSelectedItemPosition() == 1)
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
                    Intent myIntent = new Intent(this, TelaListaEncontrados.class);
                    myIntent.putExtra("valor", (Serializable)resL);
                    this.startActivity(myIntent);
                }
            }
            else if(spin.getSelectedItemPosition() == 2)
            {
                resL = new BuscaComida(local,texto.getText().toString()).busca();
                if(resL.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else
                {
                    Intent myIntent = new Intent(this, TelaListaEncontrados.class);
                    myIntent.putExtra("valor", (Serializable)resL);
                    this.startActivity(myIntent);
                }
            }




        }
        else
        {
            Toast.makeText(getApplicationContext(),"Digite um valor para pesquisa",Toast.LENGTH_SHORT).show();
            texto.requestFocus();
        }



//        double [] dados = {-23.14426,-45.77867};
//        Intent myIntent = new Intent(this, TelaMapa.class);
//        myIntent.putExtra("valor",dados);
//        this.startActivity(myIntent);


    }

    public void onClickInternet(View view)
    {
        final ProgressDialog progress = new ProgressDialog(this);

        if(loc.getLanguage().equals("en"))
        {
            progress.setTitle("Connecting");
            progress.setMessage("Please wait...");
        }
        else if(loc.getLanguage().equals("pt")){
            progress.setTitle("Conectando");
            progress.setMessage("Por Favor, aguarde...");
        }
        else if(loc.getLanguage().equals("es")){
            progress.setTitle("Conectando");
            progress.setMessage("Cargando...");
        }

        progress.show();

        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                progress.cancel();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 2000);
        if(isInternetAvailable())
        {

            textoInternet.setVisibility(View.GONE);
            botaoInternet.setVisibility(View.GONE);
            imageInternet.setVisibility(View.GONE);

            trazerDados();
            spin.setVisibility(View.VISIBLE);
            texto.setVisibility(View.VISIBLE);
            lv.setVisibility(View.VISIBLE);
            botao.setVisibility(View.VISIBLE);
            List<String> lista = Connection.getInstance().returnNames(local);
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.texto_lista,lista));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Intent myIntent = new Intent(TelaPrincipal.this, TelaInformacoes.class);
                    myIntent.putExtra("valor", local.get(position));
                    startActivity(myIntent);
                }
            });
        }
    }





    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }

    public List<Local> trazerDados()
    {
        try {
            local=Connection.getInstance().sendGet();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            return local;
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
