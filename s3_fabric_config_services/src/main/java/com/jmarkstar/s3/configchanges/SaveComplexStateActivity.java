package com.jmarkstar.s3.configchanges;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jmarkstar.s3.R;

import java.util.ArrayList;
import java.util.List;

public class SaveComplexStateActivity extends AppCompatActivity {

    private static final String TAG_RETAINED_FRAGMENT = "StoreDataRetainedFragment";

    private StoreDataRetainedFragment mRetainedFragment;

    private TextView mTvText;

    public static void start(Context context) {
        Intent starter = new Intent(context, SaveComplexStateActivity.class);
        context.startActivity(starter);
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_complex_state);

        mTvText = (TextView)findViewById(R.id.tv_text);

        //Buscar el fragment retenido en el reinicio del activity.
        FragmentManager fm = getSupportFragmentManager();
        mRetainedFragment = (StoreDataRetainedFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);

        //si aun no existe, creamos el fragment
        if(mRetainedFragment == null){

            //agregamos el fragment
            mRetainedFragment = new StoreDataRetainedFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();

            //cargamos la data
            mRetainedFragment.setData(loadMyList());
        }

        //despues de haber guardado la data en el fragment, este fragment se convierte en
        // un almacenador y podemos aceder a esa data en cualquier parte del activity.
        showList();

    }

    @Override protected void onPause() {
        super.onPause();
        // https://developer.android.com/reference/android/app/Activity.html#isFinishing()
        // isFinishing.- verifica si el activity esta en el proceso de finalizacion. podria ser por que se esta llamando a finish()
        // comunmente, este metodo se usa en el onPause() para determinar si es solo una pausa o si el app se va a cerrar completamente.

        if(isFinishing()) {
            //si el usuario esta cerrando el app, ya no necesitaremos mas el fragment asi que lo removemos.
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(mRetainedFragment).commit();
        }
    }

    private void showList(){
        StringBuilder sb = new StringBuilder();
        for(MyDataObject data : mRetainedFragment.getData()){
            sb.append(data.getVar2()+"\n");
        }
        mTvText.setText(sb.toString());
    }

    /** Supongamos que esta informacion viene de un servico rest o de la base de datos local.
     * */
    private List<MyDataObject> loadMyList() {
        List<MyDataObject> list = new ArrayList<>();
        for(int i =1; i<=15; i++){
            list.add(new MyDataObject(i, "data"+i));
        }
        return list;
    }
}
