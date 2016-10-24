package com.projeto.engsoft.projetoandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.Locale;


public class TelaPrincipal extends AppCompatActivity {
    private ListView sideMenu;



    private DrawerLayout  navLayout;
    private ActionBarDrawerToggle navDrawerToggle;
    private String navTitulo;

    private List<Local> local;




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

        sideMenu = (ListView) findViewById(R.id.lista_itens);
        sideMenu.setAdapter(new CustomList(this,navTitles,navImage));

        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(R.string.app_name);
        try {
            local = Connection.getInstance().sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FrameLayout frameFrag = (FrameLayout) findViewById(R.id.fragAtual);
        Fragment frag = new FragmentoPrincipal();
        Bundle args = new Bundle();
        args.putSerializable("valor",(Serializable)local);
        frag.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(frameFrag.getId(), frag).commit();

        sideMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if(position == 0){
                       navLayout.closeDrawer(sideMenu);
                        FrameLayout frameFrag = (FrameLayout) findViewById(R.id.fragAtual);
                        Fragment frag = new FragmentoPrincipal();
                        Bundle args = new Bundle();
                        args.putSerializable("valor",(Serializable)local);
                        frag.setArguments(args);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(frameFrag.getId(), frag).commit();
                    }
                    else if (position == 1){
                        navLayout.closeDrawer(sideMenu);
                        TelaListaEncontrados tle = new TelaListaEncontrados ();
                        Bundle args = new Bundle();
                        args.putSerializable("valor",(Serializable)local);
                        tle.setArguments(args);
                        getFragmentManager().beginTransaction().replace(R.id.fragAtual,tle).commit();
                    }

                }
            });

    }
    public DrawerLayout getNavLayout() {
        return navLayout;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        navDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setActionBarTitle(String title){
        getActionBar().setTitle(title);
    }
}
