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

    private TextView os, endereco, contra, nome, telComercial, telResidencial, telCelular, obser1, token;
    EditText anotacaoTecnica, celularParaEnviar;
    String parametroOS;
    Spinner servicosExecutados;
    ProgressDialog progresso, progressoInterno;
    Button botaoOS, botaoFoto;
    RequestBody ordem, tipoDeDadoDaAssinatura;
    MultipartBody.Part corpoProblema;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_os);

        os = (TextView) findViewById(R.id.textViewOS);
        contra = (TextView) findViewById(R.id.textViewContra);
        nome = (TextView) findViewById(R.id.textViewNome);
        endereco = (TextView) findViewById(R.id.textViewEndereco);
        obser1 = (TextView) findViewById(R.id.textViewObser);
        botaoFoto = (Button) findViewById(R.id.btnFoto);
        telComercial = (TextView) findViewById(R.id.textViewTelComercial);
        telResidencial = (TextView) findViewById(R.id.textViewTelResidencial);
        telCelular = (TextView) findViewById(R.id.textViewTelCelular);
        botaoOS = (Button) findViewById(R.id.botaoEnviaOS);
        anotacaoTecnica = (EditText) findViewById(R.id.textoAnotacoesID);
        servicosExecutados = (Spinner) findViewById(R.id.selecaoServicoID);
        token = (EditText) findViewById(R.id.tokenID);
        celularParaEnviar = (EditText) findViewById(R.id.textoCelular);

        //pegando valor da activity anterior
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        parametroOS = bundle.getString("os");
        String usuario = bundle.getString("user");

        final String token_enviado_para = bundle.getString("celular");

        Servicos.retrofitServicosExecutados();

        listenerButton(parametroOS, usuario,token_enviado_para);
    }


    private void listenerButton(String parametroOS, String tecnico, String token_enviado_para) {

        //POPUP de LOADING
        progresso = new ProgressDialog(GetOS.this);
        progresso.setTitle("Carregando...");
        progresso.show();

        //chama o retrofit para ele trabalhar
        retrofitEscravo(parametroOS, tecnico, token_enviado_para);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 )
        {
            if (resultCode == RESULT_OK)
            {
                if (data != null)
                { String os = parametroOS;

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    //Bitmap reduzido = BITMAP_RESIZER(bitmap,300,300);
                    byte [] imagem = Servicos.converterBitmapParaArray(bitmap);

                    ordem =
                            RequestBody.create(
                                    okhttp3.MultipartBody.FORM, os); //os

                    tipoDeDadoDaAssinatura = RequestBody.create(MediaType.parse(
                            "multipart/form-data"),imagem);

                    corpoProblema = MultipartBody.Part.createFormData("problema",
                            "desenhoNome",tipoDeDadoDaAssinatura);

                    //Servicos.enviaAssinaturaRetrofit(ordem, null,corpoProblema);

                }else if( resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT);
                }
            }
        }
    }


    private void retrofitEscravo(final String os_var, final String tecnicoLocal, final String token_enviado_para) {

        //pegando data e hora do celular
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        final Date dataAtual = calendar.getTime();


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
                            Servicos.retrofitTokenGenerator(os_var, token_enviado_para); //envia token para usuario destinado
                            //Dismissing the loading progressbar
                            progresso.dismiss();

                            os.setText(respostaServidor.getOs().get(0).getOs());
                            contra.setText(respostaServidor.getOs().get(0).getContra());
                            nome.setText(respostaServidor.getOs().get(0).getNome());
                            endereco.setText(respostaServidor.getOs().get(0).getEndereco());
                            obser1.setText(respostaServidor.getOs().get(0).getObser());
                            telComercial.setText(respostaServidor.getOs().get(0).getComercial());
                            telResidencial.setText(respostaServidor.getOs().get(0).getResidencial());
                            telCelular.setText(respostaServidor.getOs().get(0).getCelular());

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item,Servicos.getListaServicosExecutados());

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            servicosExecutados.setAdapter(adapter);

/*############################################ ENVIA OS PARA SERVIDOR #########################################################*/
                            botaoOS.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    progressoInterno = new ProgressDialog(GetOS.this);
                                    progressoInterno.setTitle("Carregando...");
                                    progressoInterno.show();

                                    final String celularParaEnvio = celularParaEnviar.getText().toString();

                                    final Call<RespostaServidor> verificaToken = service.consultaToken(token.getText().toString());

                                    verificaToken.enqueue(new Callback<RespostaServidor>()
                                    {
                                        @Override
                                        public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                                            RespostaServidor tokenResultado = response.body();

                                            if (tokenResultado.getSuccess() == 1)
                                            {
                                                try {
                                                    progressoInterno.dismiss();

                                                    Servicos.retrofitEnviaOS(os.getText().toString(), tecnicoLocal, contra.getText().toString(),
                                                            nome.getText().toString(), String.valueOf(servicosExecutados.getSelectedItem()),
                                                            anotacaoTecnica.getText().toString(), obser1.getText().toString(), celularParaEnvio);

                                                    Servicos.enviaAssinaturaRetrofit(ordem, null,corpoProblema);

                                                    Intent intent = new Intent(getApplicationContext(), AssinaturaDigital.class);

                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("os", os.getText().toString());
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);

                                                    finish();
                                                } catch (android.content.ActivityNotFoundException ex) {
                                                    progressoInterno.dismiss();
                                                    Toast.makeText(getApplicationContext(), "Não foi possível enviar ao Servidor, tente novamente.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                progressoInterno.dismiss();
                                                Toast.makeText(getApplicationContext(), tokenResultado.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<RespostaServidor> call, Throwable t) {
                                            progressoInterno.dismiss();
                                            finish();
                                            Toast.makeText(getApplicationContext(), "Não foi possível acessar servidor!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

     /*###############################################FIM ENVIA EMAIL ########################################################*/
                        } else {
                            //Dismissing the loading progressbar
                            progresso.dismiss();

                            finish();
                            Toast.makeText(GetOS.this, respostaServidor.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                progresso.dismiss();
                Toast.makeText(getApplicationContext(), "Erro na chamada ao servidor" + t, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void tirarFoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }




    //AKI ESTAVA O SELECIONAR OPS

    //AKI ESTAVA O ENVIA OS
}