package com.example.shield.ossearchvertv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.Servicos;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import retrofit2.Call;

public class ausencia_form extends AppCompatActivity {

    private Button enviaAusente;
    private EditText os_ausente, anotacao_ausente;
    private String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ausencia_form);

        //pegando valor da activity anterior
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        usuario = bundle.getString("user");


        enviaAusente = (Button) findViewById(R.id.botaoAusente);
        os_ausente = (EditText) findViewById(R.id.os_ausenteEdit);
        anotacao_ausente = (EditText) findViewById(R.id.obs_ausenteEdit);

    }

    public void clienteAusenteEnviar(View view) {
        Servicos.retrofitClienteAusente(getApplicationContext(),String.valueOf(os_ausente.getText()),usuario, String.valueOf(anotacao_ausente.getText()));
        Toast.makeText(getApplicationContext(),"Enviado", Toast.LENGTH_SHORT).show();
        finish();
    }



}