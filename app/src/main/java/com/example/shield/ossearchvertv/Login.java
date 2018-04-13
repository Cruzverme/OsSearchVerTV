package com.example.shield.ossearchvertv;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.Permissao;
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
    private static ProgressBar carregamento;

    public static final String PREFERENCIAS = "preferencia";

    public static final String USERLOGADO = "USUARIO";

    SharedPreferences sharedPreferences;

    //array de permissoes do APP, apenas para android 23>
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1,this,permissoesNecessarias);

        logar = (Button) findViewById(R.id.logarID);
        usuario = (EditText) findViewById(R.id.usuarioID);
        senha = (EditText) findViewById(R.id.senhaID);
        carregamento = (ProgressBar) findViewById(R.id.progressBar);

        senha.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                // If the event is a key-down event on the "enter" button
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
//                    progresso = new ProgressDialog(Login.this);
//                    progresso.setTitle("aguarde...");
//                    progresso.show();
                    carregamento.setVisibility(ProgressBar.VISIBLE);
                    testarOnline();
                    return true;
                }
                return false;
            }
        });

        listener();
    }



    private void listener() {

        logar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                progresso = new ProgressDialog(Login.this);
//                progresso.setTitle("aguarde...");
//                progresso.show();
                carregamento.setVisibility(ProgressBar.VISIBLE);
                testarOnline();
            }
        });
    }

    private void testarOnline()
    {
        if(isOnline())//checa se o celular esta com rede
        {
            if (usuario.getText().length() == 0 || senha.getText().length() == 0) {
                //progresso.dismiss();
                carregamento.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Informação Incompleta", Toast.LENGTH_LONG).show();
                senha.setText("");
            } else {
                retrofitEscravo(usuario.getText().toString(), senha.getText().toString());
            }
        }else{
            //progresso.dismiss();
            carregamento.setVisibility(ProgressBar.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Não estava online logar!", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }
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

                            Toast.makeText(getApplicationContext(), "Bem Vindo " + usuarioLocal, Toast.LENGTH_SHORT).show();

                            //zera a entrada do usuario
                            senha.setText("");
                            usuario.setText("");

                            //joga pra proxima activity
                            Intent bemVindo = new Intent(getApplicationContext(), ListaOS.class);//TokenSender.class

                            Bundle bundle = new Bundle();
                            bundle.putString("user",usuarioLocal);
                            bemVindo.putExtras(bundle);
                            startActivity(bemVindo);


                            //progresso.dismiss();
                            carregamento.setVisibility(ProgressBar.INVISIBLE);
                            finish();
                        } else {
                            carregamento.setVisibility(View.INVISIBLE);
                            Toast.makeText(Login.this, respostaServidor.getMessage() , Toast.LENGTH_SHORT).show();
                            senha.setText("");
                        }
                    }
                } else {
                    //progresso.dismiss();
                    carregamento.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Resposta nula do servidor", Toast.LENGTH_LONG).show();
                    senha.setText("");
                }
            }
            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t)
            {
                //progresso.dismiss();
                carregamento.setVisibility(ProgressBar.INVISIBLE);
                Toast.makeText(Login.this, "Sem Comunicação Com Servidor", Toast.LENGTH_SHORT).show();
                senha.setText("");
            }
        });
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for( int resultado : grantResults ){

            if( resultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }

        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}