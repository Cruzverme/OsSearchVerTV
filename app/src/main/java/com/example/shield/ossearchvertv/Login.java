package com.example.shield.ossearchvertv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private Button logar;
    private TextView botao_cadastrar;
    private EditText usuario, senha;
    private ProgressDialog progresso;
    private RespostaServidor resposta = new RespostaServidor();

    public static final String PREFERENCIAS = "preferencia";

    public static final String USERLOGADO = "USUARIO";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logar = (Button) findViewById(R.id.logarID);
        usuario = (EditText) findViewById(R.id.usuarioID);
        senha = (EditText) findViewById(R.id.senhaID);
        botao_cadastrar = (TextView) findViewById(R.id.botao_cadastrarID);
        //sharedPreferences = getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE);

//        botao_cadastrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent cadastro = new Intent(getApplicationContext(), CadastrarUsuario.class);
//                startActivity(cadastro);
//            }
//        });

        listener();
    }

    private void listener() {

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline())//checa se o celular esta com rede
                {
                    if (usuario.getText().length() == 0 || senha.getText().length() == 0) {
                        progresso.dismiss();
                        Toast.makeText(getApplicationContext(), "Informação Incompleta", Toast.LENGTH_LONG).show();
                        senha.setText("");
                    } else {
                        progresso = new ProgressDialog(Login.this);
                        progresso.setTitle("processando...");
                        progresso.show();

                        retrofitEscravo(usuario.getText().toString(), senha.getText().toString());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Não estava online para enviar e-mail!", Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
            }
        });
    }

    private void retrofitEscravo(final String usuarioLocal, String password)
    {
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> call =service.logarUsuario(usuarioLocal, password);


        call.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response)
            {
                if (response.isSuccessful()){
                    RespostaServidor respostaServidor = response.body();
                    if (respostaServidor != null) {
                        if (respostaServidor.getSuccess() == 1) {
                            //progresso.dismiss();
                            Toast.makeText(getApplicationContext(), "Bem Vindo" + usuarioLocal, Toast.LENGTH_SHORT).show();

                            //joga pra proxima activity
                            Intent bemVindo = new Intent(getApplicationContext(), BuscaOsUnica.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("user",usuarioLocal);
                            bemVindo.putExtras(bundle);
                            startActivity(bemVindo);

                            //zera a entrada do usuario
                            senha.setText("");
                            usuario.setText("");
                        } else {
                            Toast.makeText(Login.this, "Usuário inexistente ou Informação incorreta", Toast.LENGTH_SHORT).show();
                            senha.setText("");
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Resposta nula do servidor", Toast.LENGTH_LONG).show();
                    senha.setText("");
                }

            }
            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t)
            {
                Toast.makeText(Login.this, "Falha ao Logar", Toast.LENGTH_SHORT).show();
                senha.setText("");
            }

        });
        progresso.dismiss();
    }

    private boolean isOnline() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        catch(Exception ex){
            Toast.makeText(getApplicationContext(), "Erro ao verificar se estava online! " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
