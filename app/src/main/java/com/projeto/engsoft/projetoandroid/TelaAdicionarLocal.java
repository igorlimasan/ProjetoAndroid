package com.projeto.engsoft.projetoandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.pinball83.maskededittext.MaskedEditText;
import com.thomashaertel.widget.MultiSpinner;

import java.util.LinkedList;
import java.util.List;
import android.os.Handler;
import java.util.logging.LogRecord;

public class TelaAdicionarLocal extends AppCompatActivity {
    private EditText nome;
    private EditText logradouro;
    private EditText numero;
    private MaskedEditText cep;
    private Spinner tipo;
    private MultiSpinner comidas;
    private Spinner uf;
    private String valor;
    private String novoTipo = "";


    private List<String> comidasSelecionadas = new LinkedList<String>();
    private ArrayAdapter<String> adapterComidas;
    private ArrayAdapter<String> adapter;
    private String [] cs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_adicionar_local);

        nome = (EditText) findViewById(R.id.nome);
        logradouro = (EditText) findViewById(R.id.logradouro);
        numero = (EditText) findViewById(R.id.numero);
        cep = (MaskedEditText) findViewById(R.id.cep);
        tipo = (Spinner) findViewById(R.id.tipo);
        comidas = (MultiSpinner) findViewById(R.id.comidas);

        uf = (Spinner) findViewById(R.id.estado);
        if(!Connection.getInstance().getTipos().contains("Selecione um tipo..."))Connection.getInstance().getTipos().add(0,"Selecione um tipo...");


        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.texto_lista,Connection.getInstance().getTipos());
        tipo.setAdapter(adapter);


        adapterComidas = new ArrayAdapter<String>(getApplicationContext(),R.layout.texto_lista,Connection.getInstance().getComidas());
        comidas.setAdapter(adapterComidas,false,onSelectedListener);
        comidas.setText("Selecione as comidas...");

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    public void onClick(View view)
    {
        /*http://maps.google.com/maps/api/geocode/json?address=&components=country:br&sensor=false*/
        if(TextUtils.isEmpty(nome.getText().toString().trim())){
            nome.setError("Digite algum valor");
        }
        else if(TextUtils.isEmpty(logradouro.getText().toString().trim())){
            logradouro.setError("Digite algum valor");
        }
        else if(TextUtils.isEmpty(numero.getText().toString().trim())){
            numero.setError("Digite algum valor");
        }
        else if( TextUtils.isEmpty(cep.getUnmaskedText().replace(" ","").trim()) || cep.getUnmaskedText().replace(" ","").trim().length()<8){
            cep.setError("Digite algum valor");
        }
        else if(comidasSelecionadas.size() == 0){
            comidas.setError("Selecione as comidas...");

        }
        else if(tipo.getSelectedItemPosition() == 0 ){
            Toast.makeText(getApplicationContext(),"Selecione o tipo",Toast.LENGTH_LONG).show();

        }
        else if(uf.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(),"Selecione o estado",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),novoTipo,Toast.LENGTH_LONG).show();
            //logradouro,numero,cep,estado;
            String enderecoCompleto = logradouro.getText().toString().trim()+","+numero.getText().toString()+","+
                    cep.getUnmaskedText().replace(" ","").trim()+","+uf.getSelectedItem().toString();
            double[] latlng = Connection.getInstance().getLocalLatLong(enderecoCompleto);
            Local local = new Local(nome.getText().toString().trim(),tipo.getSelectedItem().toString(),comidasSelecionadas,latlng[0],latlng[1],enderecoCompleto);
            Connection.getInstance().inserirLocal(local);
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Local Cadastrado");
            alertDialog.setMessage("Local cadastrado com sucesso");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        }


    }
    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            comidasSelecionadas.clear();

            for(int i = 0; i<comidas.getSelected().length;i++){
                if(comidas.getSelected()[i] == true)
                {

                    comidasSelecionadas.add(adapterComidas.getItem(i));
                }
            }
            if(comidasSelecionadas.size() == 0) comidas.setText("Selecione as comidas...");
            else comidas.setText(comidasSelecionadas.size()+" Comidas");



        }
    };



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
