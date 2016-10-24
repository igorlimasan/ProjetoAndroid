package com.projeto.engsoft.projetoandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Igor on 20/10/2016.
 */




public class FragmentoPrincipal extends  BaseFragment {


    private List<Local> local;
    private Spinner spin;
    private EditText texto;
    private Button botao;
    private Locale loc = Locale.getDefault();

    private TextView textoInternet;
    private Button botaoInternet;

    private ImageView imageInternet;
    private View view;

    private DrawerLayout navDrawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.frag_principal, container, false);
        local = (List<Local>) getArguments().getSerializable("valor");


        spin = (Spinner) view.findViewById(R.id.criterios);
        texto = (EditText) view.findViewById(R.id.valor);
        botao = (Button) view.findViewById(R.id.pesq);
        navDrawerLayout = ((TelaPrincipal) getActivity()).getNavLayout();
        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();

            }
        });
        textoInternet = (TextView) view.findViewById(R.id.textoInternet);
        botaoInternet = (Button) view.findViewById(R.id.buttonInternet);
        botaoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickInternet();
            }
        });
        imageInternet = (ImageView) view.findViewById(R.id.imagemInternet);

        texto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Local res = null;
                    List<Local> resL;
                    if (!TextUtils.isEmpty(texto.getText().toString())) {
                        if (spin.getSelectedItemPosition() == 0) {
                            resL = new BuscaNome(local, texto.getText().toString()).busca();
                            if (resL.isEmpty()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Nada encontrado", Toast.LENGTH_SHORT).show();
                                texto.setText("");
                                texto.requestFocus();
                            } else {
                                Intent myIntent = new Intent(getActivity(), TelaInformacoes.class);
                                myIntent.putExtra("valor", resL.get(0));
                                startActivity(myIntent);
                            }
                        } else if (spin.getSelectedItemPosition() == 1) {
                            resL = new BuscaTipo(local, texto.getText().toString()).busca();
                            if (resL.isEmpty()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Nada encontrado", Toast.LENGTH_SHORT).show();
                                texto.setText("");
                                texto.requestFocus();
                            } else {
                                TelaListaEncontrados tle = new TelaListaEncontrados ();
                                Bundle args = new Bundle();
                                args.putSerializable("valor",(Serializable)resL);
                                tle.setArguments(args);
                                getFragmentManager().beginTransaction().replace(R.id.fragAtual,tle).commit();

                            }
                        } else if (spin.getSelectedItemPosition() == 2) {
                            resL = new BuscaComida(local, texto.getText().toString()).busca();
                            if (resL.isEmpty()) {
                                Toast.makeText(getActivity().getApplicationContext(), "Nada encontrado", Toast.LENGTH_SHORT).show();
                                texto.setText("");
                                texto.requestFocus();
                            } else {
                                TelaListaEncontrados tle = new TelaListaEncontrados ();
                                Bundle args = new Bundle();
                                args.putSerializable("valor",(Serializable)resL);
                                tle.setArguments(args);
                                getFragmentManager().beginTransaction().replace(R.id.fragAtual,tle).commit();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Digite um valor para pesquisa", Toast.LENGTH_SHORT).show();
                        texto.requestFocus();
                    }

                    return true;
                }
                return false;
            }
        });


        if (!isInternetAvailable()) {

            spin.setVisibility(view.GONE);
            texto.setVisibility(view.GONE);
            botao.setVisibility(view.GONE);
            navDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


            textoInternet.setVisibility(view.VISIBLE);
            botaoInternet.setVisibility(view.VISIBLE);
            imageInternet.setVisibility(view.VISIBLE);


        } else {

            spin.setVisibility(view.VISIBLE);
            texto.setVisibility(view.VISIBLE);
            botao.setVisibility(view.VISIBLE);
            navDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }


            LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Build the alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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




        }
        return view;
    }

    public void click() {
        Local res = null;
        List<Local> resL;
        if(!TextUtils.isEmpty(texto.getText().toString())){
            if(spin.getSelectedItemPosition() == 0){
                resL = new BuscaNome(local,texto.getText().toString()).busca();
                if(resL.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else{
                    Intent myIntent = new Intent(getActivity(), TelaInformacoes.class);
                    myIntent.putExtra("valor", resL.get(0));
                    startActivity(myIntent);
                }
            }

            else if(spin.getSelectedItemPosition() == 1){
                resL = new BuscaTipo(local,texto.getText().toString()).busca();
                if(resL.isEmpty()){
                    Toast.makeText(getActivity().getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else{
                    TelaListaEncontrados tle = new TelaListaEncontrados ();
                    Bundle args = new Bundle();
                    args.putSerializable("valor",(Serializable)resL);
                    tle.setArguments(args);
                    getFragmentManager().beginTransaction().add(R.id.fragAtual,tle).commit();
                }
            }
            else if(spin.getSelectedItemPosition() == 2)
            {
                resL = new BuscaComida(local,texto.getText().toString()).busca();
                if(resL.isEmpty())
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Nada encontrado",Toast.LENGTH_SHORT).show();
                    texto.setText("");
                    texto.requestFocus();
                }
                else
                {
                    TelaListaEncontrados tle = new TelaListaEncontrados ();
                    Bundle args = new Bundle();
                    args.putSerializable("valor",(Serializable)resL);
                    tle.setArguments(args);
                    getFragmentManager().beginTransaction().replace(R.id.fragAtual,tle).commit();
                }
            }
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(),"Digite um valor para pesquisa",Toast.LENGTH_SHORT).show();
            texto.requestFocus();
        }



//        double [] dados = {-23.14426,-45.77867};
//        Intent myIntent = new Intent(this, TelaMapa.class);
//        myIntent.putExtra("valor",dados);
//        this.startActivity(myIntent);


    }


    public void onClickInternet()
    {

        final ProgressDialog progress = new ProgressDialog(getActivity());

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

            textoInternet.setVisibility(view.GONE);
            botaoInternet.setVisibility(view.GONE);
            imageInternet.setVisibility(view.GONE);
            try {
                local = Connection.getInstance().sendGet();
            } catch (Exception e) {
                e.printStackTrace();
            }

            spin.setVisibility(View.VISIBLE);
            texto.setVisibility(View.VISIBLE);
            botao.setVisibility(View.VISIBLE);
            navDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // Build the alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


}
