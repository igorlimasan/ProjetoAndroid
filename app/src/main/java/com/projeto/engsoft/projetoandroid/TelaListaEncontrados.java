package com.projeto.engsoft.projetoandroid;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

public class TelaListaEncontrados extends AppCompatActivity {
    List<Local> found;
    ListView lv;


    private ListView sideMenu;
    private DrawerLayout navLayout;
    private ActionBarDrawerToggle navDrawerToggle;
    private String navTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_encontrados);

        Integer[] navImage={
                R.drawable.ic_action_search,
                R.drawable.ic_action_place
        };

        String[] navTitles = {
                "Pesquisar",
                "Locais"
        };

        Intent intent = getIntent();
        found = (List<Local>)intent.getSerializableExtra("valor");
        if(found.size() == Connection.getInstance().numeroLugares()) navTitulo = getString(R.string.lista_cadastrados);
        else navTitulo = getString(R.string.title_activity_lista_encontrados);
        setTitle(navTitulo);


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


        navLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        sideMenu = (ListView) findViewById(R.id.lista_itens);
        sideMenu.setAdapter(new CustomList(this,navTitles,navImage));
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        sideMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 0){
                    Intent myIntent = new Intent(TelaListaEncontrados.this, TelaPrincipal.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(myIntent);
                    finish();

                }
                if(position == 1) navLayout.closeDrawer(sideMenu);

            }
        });




    }



    private void setupDrawer() {

        if(found.size() == Connection.getInstance().numeroLugares()) {
            navDrawerToggle = new ActionBarDrawerToggle(this, navLayout,
                    R.string.nav_open, R.string.lista_cadastrados) {

                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {

                    getSupportActionBar().setTitle(R.string.nav_open);
                }

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {

                    getSupportActionBar().setTitle(navTitulo);
                }
            };

            navDrawerToggle.setDrawerIndicatorEnabled(true);
            navLayout.setDrawerListener(navDrawerToggle);
        }
        else{
            navDrawerToggle = new ActionBarDrawerToggle(this, navLayout,
                    R.string.nav_open, R.string.lista_cadastrados) {

                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {

                    getSupportActionBar().setTitle(R.string.nav_open);
                }

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {

                    getSupportActionBar().setTitle(navTitulo);
                }
            };

            navDrawerToggle.setDrawerIndicatorEnabled(true);
            navLayout.setDrawerListener(navDrawerToggle);
        }
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
