package com.example.shield.ossearchvertv.ClassesMortas;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shield.ossearchvertv.GetOS;
import com.example.shield.ossearchvertv.R;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscaOsUnica extends AppCompatActivity {

    private EditText textOS;
    ProgressDialog progresso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_os_unica);

        textOS = (EditText) findViewById(R.id.OsID);
        Button buscar = (Button) findViewById(R.id.botaoBuscar);


        //pegando valor da activity anterior MainActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        assert bundle != null;
        final String usuarioLocal = bundle.getString("user");

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //POPUP de LOADING
            progresso = new ProgressDialog(BuscaOsUnica.this);
            progresso.setTitle("Carregando...");
            progresso.show();

            retrofitVerificadorOS(textOS.getText().toString(),usuarioLocal);

            }
        });
    }

    private void retrofitVerificadorOS(final String os_var, final String tecnicoLocal) {

        //Minha URLBASE
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> call = service.mostrarOS(os_var);

        call.enqueue(new Callback<RespostaServidor>()
        {
                         @Override
                         public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response)
                         {
                             final RespostaServidor respostaServidor = response.body(); //pega o json
                             if (response.isSuccessful()) {
                                 if (respostaServidor != null) //se o body nao está vazio
                                 {
                                     //progresso.dismiss();
                                     if (respostaServidor.getSuccess() == 1) {

                                         Intent intent = new Intent(getApplicationContext(), GetOS.class);

                                         Bundle bundle = new Bundle();
                                         bundle.putString("os", os_var);
                                         bundle.putString("user", tecnicoLocal);
                                         intent.putExtras(bundle);
                                         startActivity(intent);
                                         textOS.setText("");
                                         progresso.dismiss();
                                     }else{
                                         progresso.dismiss();
                                         Toast.makeText(getApplicationContext(), respostaServidor.getMessage(), Toast.LENGTH_SHORT).show();
                                     }
                                 }
                             }
                         }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                progresso.dismiss();
            }
        });
    }
}