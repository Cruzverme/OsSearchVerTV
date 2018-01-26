package com.example.shield.ossearchvertv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.Servicos;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenSender extends AppCompatActivity {

    private EditText os, celular;
    private Button botaoSolicitar;
    private static ProgressBar carregamento;
    private TextView ausencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_sender);

        os = (EditText) findViewById(R.id.editTextOS);
        ausencia = (TextView) findViewById(R.id.ausente);
        celular = (EditText) findViewById(R.id.editTextCelular);
        botaoSolicitar = (Button) findViewById(R.id.buttonSolicitar);
        carregamento = (ProgressBar) findViewById(R.id.progressBar);

        //pegando valor da activity anterior MainActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        final String usuarioLocal = bundle.getString("user");


        ausencia.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                carregamento.setVisibility(ProgressBar.VISIBLE);
                Intent intentAusente = new Intent(getApplicationContext(),ausencia_form.class);

                Bundle bundleAusente = new Bundle();
                bundleAusente.putString("user", usuarioLocal);
                intentAusente.putExtras(bundleAusente);

                startActivity(intentAusente);
                carregamento.setVisibility(ProgressBar.INVISIBLE);
            }
        });




        botaoSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                carregamento.setVisibility(ProgressBar.VISIBLE);

                String ordem = os.getText().toString();
                String nCelular = celular.getText().toString();

                //envia as infos para a proxima activity
                Intent intent = new Intent(getApplicationContext(), GetOS.class);

                Bundle bundle = new Bundle();

                bundle.putString("os", ordem);
                bundle.putString("user", usuarioLocal);
                bundle.putString("celular",nCelular);
                intent.putExtras(bundle);
                startActivity(intent);
                ///// fim envia prox activity \\\\\

                carregamento.setVisibility(ProgressBar.INVISIBLE);

                os.setText("");
                celular.setText("");
            }
        });
    }
}