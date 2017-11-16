package com.example.shield.ossearchvertv.Helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shield on 14/11/2017.
 */

public final class Servicos {

    static ArrayList<String> listaServicosExecutados = new ArrayList<String>();

    public static void retrofitServicosExecutados()
    {
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<RespostaServidor> callList = service.listaServicosExecutados();

        callList.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                final RespostaServidor respostaServidorServicos = response.body();

                if (response.isSuccessful())
                {
                    if (respostaServidorServicos.getSuccess() == 1)
                    {
                        listaServicosExecutados.clear();
                        listaServicosExecutados.add("SELECIONE O SERVIÇO EXECUTADO");
                        for (String servidor: respostaServidorServicos.getServicos())
                        {
                            listaServicosExecutados.add(servidor);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {

            }
        });
    }

    public static ArrayList<String> getListaServicosExecutados() {
        return listaServicosExecutados;
    }

    public static void retrofitEnviaOS(final String os, final String tecnico, final String equipe, final String contrato, final String nomeAssinante, final String servicoExecutado,
                                 final String anotacaoTecnica, final String imagem, final String observacao) {

        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<RespostaServidor> caller = service.enviaOS(os, tecnico, equipe, contrato, nomeAssinante,
                servicoExecutado, anotacaoTecnica, observacao, imagem);

        caller.enqueue(new Callback<RespostaServidor>()
        {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {

                final RespostaServidor respostaServidor2 = response.body(); //pega o json
                if (response.isSuccessful()) {
                    if (respostaServidor2 != null) //se o body nao está vazio
                    {
                        if (respostaServidor2.getSuccess() == 1) {
                            Log.i("OS", "ENVIADA");
                        }else{
                            Log.i("OS", "NAO ENVIADA");
                        }
                    }
                }else {
                    Log.d("ERRO: ","ALGO NAO ESTA CERTO");
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> caller, Throwable t) {

            }
        });
    }
}
