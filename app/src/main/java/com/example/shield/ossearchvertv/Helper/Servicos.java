package com.example.shield.ossearchvertv.Helper;

import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Shield on 14/11/2017.
 */

public final class Servicos extends AppCompatActivity {

    private static ArrayList<String> listaServicosExecutados = new ArrayList<String>();
    private static String mensagem;

    public static void retrofitServicosExecutados() {
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<RespostaServidor> callList = service.listaServicosExecutados();

        callList.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                final RespostaServidor respostaServidorServicos = response.body();

                if (response.isSuccessful()) {
                    if (respostaServidorServicos.getSuccess() == 1) {
                        listaServicosExecutados.clear();
                        listaServicosExecutados.add("SELECIONE O SERVIÇO EXECUTADO");
                        listaServicosExecutados.addAll(respostaServidorServicos.getServicos());
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

    public static void retrofitEnviaOS(final String os, final String tecnico, final String contrato, final String nomeAssinante, final String servicoExecutado,
                                       final String anotacaoTecnica, final String imagem, final String observacao, final String celularParaEnvio) {

        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        Call<RespostaServidor> caller = service.enviaOS(os, tecnico, contrato, nomeAssinante,
                servicoExecutado, anotacaoTecnica, observacao, imagem, celularParaEnvio);
        caller.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                final RespostaServidor respostaServidor2 = response.body(); //pega o json

                if (response.isSuccessful())
                {
                    if (respostaServidor2 != null) //se o body nao está vazio
                    {
                        if (respostaServidor2.getSuccess() == 1)
                        {
                            String body = "## COMPROVANTE DE OS ##" +
                                    "\n\nTÉCNICO: " + tecnico +
                                    "\nOS: " + os +
                                    "\nCONTRATO: " + contrato +
                                    "\nASSINANTE: " + nomeAssinante +
                                    "\nSERVICO: " + servicoExecutado +
                                    "\n\n VerTV Agradece";

                            enviaSMS(celularParaEnvio,body);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> caller, Throwable t) {

            }
        });
    }


    public static void retrofitTokenGenerator( final String os, final String celular)
    {
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> caller = service.salvaToken(os,celular);

        caller.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response)
            {
                final RespostaServidor resposta = response.body();
                if (response.isSuccessful())
                {
                    if (resposta != null)
                    {
                        if (resposta.getSuccess() == 1)
                        {
                            String msg_token = "Para concluir informe ao técnico o codigo a seguir: " + resposta.getToken()
                                                + "\nVERTV Agradece";

                            enviaSMS(celular,msg_token);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                System.exit(0);
            }
        });
    }

    /*Envio do SMS*/
    public static boolean enviaSMS(String telefone, String mensagem){
        try{

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);


            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
