package com.projeto.engsoft.projetoandroid;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.List;

public class TelaListaEncontrados extends BaseFragment {
    List<Local> found;
    ListView lv;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_lista_encontrados,container,false);
        found = (List<Local>) getArguments().getSerializable("valor");
        if(found.size() == Connection.getInstance().numeroLugares()) ((TelaPrincipal)getActivity()).setTitle(((TelaPrincipal)getActivity()).getString(R.string.locais_cad));
        else ((TelaPrincipal) getActivity()).setTitle(((TelaPrincipal) getActivity()).getString(R.string.title_activity_lista_encontrados));


        lv = (ListView) view.findViewById(R.id.lista);

        lv.setAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.texto_lista,Connection.getInstance().returnNames(found)));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(found.size() == Connection.getInstance().numeroLugares()){
                    Intent myIntent = new Intent(getActivity(), TelaInformacoes.class);
                    myIntent.putExtra("valor", found.get(position));
                    startActivity(myIntent);
                }
                else
                {
                    Intent myIntent = new Intent(getActivity(), TelaMapa.class);
                    myIntent.putExtra("valor", found.get(position));
                    startActivity(myIntent);
                }
            }
        });
        return view;
    }

    @Override
    public boolean onBackPressed() {

        Fragment frag = new FragmentoPrincipal();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragAtual, frag).commit();
        return super.onBackPressed();

    }
}
