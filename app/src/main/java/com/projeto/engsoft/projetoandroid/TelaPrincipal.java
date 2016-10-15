package com.projeto.engsoft.projetoandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


    private ListView sideMenu;
    private DrawerLayout  navLayout;
    private ActionBarDrawerToggle navDrawerToggle;
    private String navTitulo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Integer[] navImage={
                R.drawable.ic_action_search,
                R.drawable.ic_action_place
        };

        String[] navTitles = {
                "Pesquisar",
                "Locais"
        };

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        navLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navTitulo = getTitle().toString();

        //textResult = (TextView) findViewById(R.id.result);
        final StringBuilder result = new StringBuilder();
        spin = (Spinner) findViewById(R.id.criterios);
        spin.requestFocus();
        texto = (EditText) findViewById(R.id.valor);
        lv=(ListView) findViewById(R.id.lista);
        botao = (Button) findViewById(R.id.pesq);
        sideMenu = (ListView) findViewById(R.id.lista_itens);
        sideMenu.setAdapter(new CustomList(this,navTitles,navImage));

        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        lv.setVisibility(View.GONE);

        textoInternet = (TextView) findViewById(R.id.textoInternet);
        botaoInternet = (Button) findViewById(R.id.buttonInternet);
        imageInternet = (ImageView) findViewById(R.id.imagemInternet);

        texto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Local res = null;
                    List<Local> resL;
                    if(!TextUtils.isEmpty(texto.getText().toString())){
                        if(spin.getSelectedItemPosition() == 0){
                            resL = new BuscaNome(local,texto.getText().toString()).busca();
                            if(resL.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                                texto.setText("");
                                texto.requestFocus();
                            }
                            else{
                                Intent myIntent = new Intent(TelaPrincipal.this, TelaInformacoes.class);
                                myIntent.putExtra("valor", resL.get(0));
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(myIntent);
                                finish();
                            }
                        }

                        else if(spin.getSelectedItemPosition() == 1){
                            resL = new BuscaTipo(local,texto.getText().toString()).busca();
                            if(resL.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                                texto.setText("");
                                texto.requestFocus();
                            }
                            else{
                                Intent myIntent = new Intent(TelaPrincipal.this, TelaListaEncontrados.class);
                                myIntent.putExtra("valor", (Serializable)resL);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(myIntent);
                                finish();                            }
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
                                Intent myIntent = new Intent(TelaPrincipal.this, TelaListaEncontrados.class);
                                myIntent.putExtra("valor", (Serializable)resL);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(myIntent);
                                finish();
                            }
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Digite um valor para pesquisa",Toast.LENGTH_SHORT).show();
                        texto.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });







        if(!isInternetAvailable())
        {

            spin.setVisibility(View.GONE);
            texto.setVisibility(View.GONE);

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


            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Build the alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Localização não ativada");
                builder.setMessage("Para melhor experiência com o Find a Food ative o servico de Localização");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Show location settings when the user acknowledges the alert dialog
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
                Dialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }

            sideMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if(position == 1){
                        Intent myIntent = new Intent(TelaPrincipal.this, TelaListaEncontrados.class);
                        myIntent.putExtra("valor", (Serializable)trazerDados());
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(myIntent);
                        finish();

                    }

                    if(position == 0) navLayout.closeDrawer(sideMenu);

                }
            });











        }










    }

    public void onClick(View view) {
        Local res = null;
        List<Local> resL;
        if(!TextUtils.isEmpty(texto.getText().toString())){
            if(spin.getSelectedItemPosition() == 0){
                resL = new BuscaNome(local,texto.getText().toString()).busca();
                if(resL.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else{
                    Intent myIntent = new Intent(this, TelaInformacoes.class);
                    myIntent.putExtra("valor", resL.get(0));
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(myIntent);
                    finish();
                }
            }

            else if(spin.getSelectedItemPosition() == 1){
                resL = new BuscaTipo(local,texto.getText().toString()).busca();
                if(resL.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else{
                    Intent myIntent = new Intent(this, TelaListaEncontrados.class);
                    myIntent.putExtra("valor", (Serializable)resL);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(myIntent);
                    finish();
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
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(myIntent);
                    finish();
                }
            }
        }
        else{
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

    private void setupDrawer() {
        navDrawerToggle = new ActionBarDrawerToggle(this, navLayout,
                R.string.nav_open, R.string.app_name) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                getSupportActionBar().setTitle(R.string.nav_open);
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

                getSupportActionBar().setTitle(navTitulo);
            }
        };

        navDrawerToggle.setDrawerIndicatorEnabled(true);
        navLayout.setDrawerListener(navDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (navDrawerToggle.onOptionsItemSelected(item)) return true;
        return false;
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        navDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navDrawerToggle.onConfigurationChanged(newConfig);
    }
}
