package com.projeto.engsoft.projetoandroid;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Igor on 20/10/2016.
 */

public class BaseFragment extends Fragment {
    public TelaPrincipal mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity	=	(TelaPrincipal) this.getActivity();
    }

    public boolean onBackPressed(){
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){

    }
}
