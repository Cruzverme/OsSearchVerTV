package com.example.shield.ossearchvertv;

/**
 * Created by Shield on 06/10/2017.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarUsuario extends AppCompatActivity {

    private EditText usuario, senha, nome;
    private Button cadastrar;
    private Spinner selectEquipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        usuario = (EditText) findViewById(R.id.usuario_cadastrarID);
        senha = (EditText) findViewById(R.id.senha_cadastrarID);
        nome = (EditText) findViewById(R.id.nomeCompletoID);
        cadastrar = (Button) findViewById(R.id.cadastrarID);
        selectEquipes = (Spinner) findViewById(R.id.selectEquipeID);

        ArrayList<String> listaEquipes = new ArrayList<String>();
        listaEquipes.add("VERTV");
        listaEquipes.add("MRG Instalações");
        listaEquipes.add("FROTA EXPRESS");
        listaEquipes.add("INFOMAX");
        listaEquipes.add("LCC E LC");
        listaEquipes.add("THIAGO EXPRESS");
        listaEquipes.add("TRISAT");


        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_spinner_item,listaEquipes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectEquipes.setAdapter(adapter);

        listener();
    }

    private void listener() {
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuario.getText().length() == 0 || senha.getText().length() == 0
                        || nome.getText().length() == 0)
                {
                    Toast.makeText(getApplicationContext(),"Informação Incompleta",Toast.LENGTH_LONG).show();
                }else
                {
       /*             retrofitEscravo(usuario.getText().toString(),senha.getText().toString(), nome.getText().toString(),
                            String.valueOf(selectEquipes.getSelectedItem()) );*/
                }
                usuario.setText("");
                senha.setText("");
                nome.setText("");
            }
        });
    }

    /*private void retrofitEscravo(final String usuario, String password, String nome, String equipe)
    {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> call =service.criarUsuario(usuario, password, nome, equipe);

        call.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response)
            {
                if (response.isSuccessful())
                {
                    RespostaServidor respostaServidor = response.body();
                    Log.d("SUCESSO", "Sucesso :" + respostaServidor.getSuccess());
                    if (respostaServidor != null)
                    {
                        if (respostaServidor.getSuccess() == 1) {
                            Toast.makeText(getApplicationContext(), respostaServidor.getMessage(), Toast.LENGTH_SHORT).show();

                            Intent bemVindo = new Intent(getApplicationContext(), Login.class);
                            startActivity(bemVindo);
                        } else {
                            Toast.makeText(CadastrarUsuario.this, respostaServidor.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Resposta nula do servidor", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t)
            {
                Toast.makeText(CadastrarUsuario.this, "Falha ao Logar", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
}
