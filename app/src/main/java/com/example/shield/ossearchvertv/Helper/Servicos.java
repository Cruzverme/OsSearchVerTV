package com.example.shield.ossearchvertv.Helper;

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

                        for (String servidor: respostaServidorServicos.getServicos())
                        {
                            listaServicosExecutados.add("SELECIONE O SERVIÇO EXECUTADO");
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
}
