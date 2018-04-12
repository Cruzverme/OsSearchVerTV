package com.example.shield.ossearchvertv;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.Servicos;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaOS extends AppCompatActivity {

    private ListView listaOS;
    private static ArrayList<String> listaDeOrdemServico = new ArrayList<String>();
    static ProgressDialog progresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_os);

        listaOS = (ListView) findViewById(R.id.osAreaID);

        //pegando valor da activity anterior MainActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        final String usuarioLocal = bundle.getString("user");

        retrofitOrdemServicoLista(usuarioLocal);
    }

    public void retrofitOrdemServicoLista(String tecnico) {
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        //POPUP de LOADING
        progresso = new ProgressDialog(ListaOS.this);
        progresso.setTitle("Carregando...");
        progresso.show();


        Call<RespostaServidor> callList = service.listarOS(tecnico);

        callList.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                final RespostaServidor respostaServidor = response.body();

                if (response.isSuccessful())
                {
                    if (respostaServidor.getSuccess() == 1)
                    {
                        listaDeOrdemServico.clear();
                        listaDeOrdemServico.addAll(respostaServidor.getOrdemServicoLista());
                        Log.d("TAG", String.valueOf(listaDeOrdemServico));
                        progresso.dismiss();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.lista_os_layout, getListaOrdemServico());
                    Log.d("TAGA", String.valueOf(getListaOrdemServico()));

                    listaOS.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {

            }
        });
    }

    public static ArrayList<String> getListaOrdemServico()
    {
        return listaDeOrdemServico;
    }
}
