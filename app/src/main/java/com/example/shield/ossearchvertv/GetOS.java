package com.example.shield.ossearchvertv;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.Servicos;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetOS extends AppCompatActivity {

    private TextView os, endereco, contra, nome, telComercial, telResidencial, telCelular, obser1;
    EditText celularParaEnviar;
    String parametroOS;
    ProgressBar carregamento;
    Button botaoOS;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_os);

        os = findViewById(R.id.textViewOS);
        contra = findViewById(R.id.textViewContra);
        nome = findViewById(R.id.textViewNome);
        endereco = findViewById(R.id.textViewEndereco);
        obser1 = findViewById(R.id.textViewObser);
        telComercial = findViewById(R.id.textViewTelComercial);
        telResidencial = findViewById(R.id.textViewTelResidencial);
        telCelular = findViewById(R.id.textViewTelCelular);
        botaoOS = findViewById(R.id.botaoAvancar);
        carregamento = findViewById(R.id.progressBar);

        //pegando valor da activity anterior
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        parametroOS = bundle.getString("os");
        String usuario = bundle.getString("user");

        listenerButton(parametroOS, usuario);
    }


    private void listenerButton(String parametroOS, String tecnico) {

        //POPUP de LOADING
        carregamento.setVisibility(View.VISIBLE);

        retrofitEscravo(parametroOS,tecnico);

    }

    private void retrofitEscravo(final String os_var, final String tecnicoLocal) {

        //pegando data e hora do celular
        //@SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        //final Date dataAtual = calendar.getTime();


        //Minha URLBASE
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> call = service.mostrarOS(os_var);

        call.enqueue(new Callback<RespostaServidor>()
        {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                final RespostaServidor respostaServidor = response.body(); //pega o json

                if (response.isSuccessful()) {
                    if (respostaServidor != null) //se o body nao está vazio
                    {
                        if (respostaServidor.getSuccess() == 1)
                        {
                            //Dismissing the loading progressbar
                            carregamento.setVisibility(View.INVISIBLE);

                            os.setText(respostaServidor.getOs().get(0).getOs());
                            contra.setText(respostaServidor.getOs().get(0).getContra());
                            nome.setText(respostaServidor.getOs().get(0).getNome());
                            endereco.setText(respostaServidor.getOs().get(0).getEndereco());
                            obser1.setText(respostaServidor.getOs().get(0).getObser());
                            telComercial.setText(respostaServidor.getOs().get(0).getComercial());
                            telResidencial.setText(respostaServidor.getOs().get(0).getResidencial());
                            telCelular.setText(respostaServidor.getOs().get(0).getCelular());


                            /*############################################ ENVIA OS PARA SERVIDOR #########################################################*/
                            botaoOS.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    carregamento.setVisibility(View.VISIBLE);
//                                    final String celularParaEnvio = celularParaEnviar.getText().toString();

                                    //Intent intent = new Intent(getApplicationContext(), AssinaturaDigital.class);
                                    Intent intent = new Intent(getApplicationContext(), AtendimentoOS.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("os", os.getText().toString());
                                    bundle.putString("contra",contra.getText().toString());
                                    bundle.putString("nome",nome.getText().toString());
                                    bundle.putString("obser",obser1.getText().toString());
                                    bundle.putString("celular",telCelular.getText().toString());
                                    bundle.putString("user", tecnicoLocal);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    carregamento.setVisibility(View.GONE);
                                    finish();
                                }
                            });

                            /*###############################################FIM ENVIA EMAIL ########################################################*/
                        } else {
                            //Dismissing the loading progressbar
                            carregamento.setVisibility(View.INVISIBLE);

                            finish();
                            Toast.makeText(GetOS.this, respostaServidor.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                //progresso.dismiss();
                carregamento.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Erro na chamada ao servidor" + t, Toast.LENGTH_LONG).show();

            }
        });
    }

    //AKI ESTAVA O SELECIONAR OPS

    //AKI ESTAVA O ENVIA OS
}