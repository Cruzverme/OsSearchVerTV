package com.example.shield.ossearchvertv;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AtendimentoOS extends AppCompatActivity {

    static Spinner servicosExecutados;
    String parametroOS,usuario,contra,obser,nome,celular;
    EditText celularParaEnviar, anotacaoTecnica;
    TextView confirmacaoFoto;
    static ProgressBar carregamento;
    RequestBody ordem, tipoDeDadoDaAssinatura;
    MultipartBody.Part corpoProblema;
    Button botaoFoto, botaoAvancar;
    private static ArrayList<String> listaServicosExecutados = new ArrayList<String>();
    byte[] imagem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atendimento_os);

        carregamento = findViewById(R.id.progressBar);
        celularParaEnviar = findViewById(R.id.textoCelular);
        anotacaoTecnica = findViewById(R.id.editAnotacao);
        botaoFoto = findViewById(R.id.btnFoto);
        botaoAvancar = findViewById(R.id.botaoAvancar);
        confirmacaoFoto = findViewById(R.id.fotoTiradaTexto);

        carregamento.setVisibility(View.VISIBLE);
        //pegando valor da activity anterior
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        parametroOS = bundle.getString("os");
        servicosExecutados = findViewById(R.id.selecaoServicoID);
        usuario = bundle.getString("user");
        nome = bundle.getString("nome");
        contra = bundle.getString("contra");
        obser = bundle.getString("obser");
        celular = bundle.getString("celular");

        retrofitServicosExecutados();

        //chama o retrofit para ele trabalhar
        //retrofitEnviaAtendimento(parametroOS, usuario);
    }

    public void retrofitServicosExecutados() {
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
                        carregamento.setVisibility(View.GONE);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                R.layout.spinner_layout_personalizado, getListaServicosExecutados());//

                        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout_personalizado);

                        //instancia o spinner com os valores

                        servicosExecutados.setAdapter(adapter);
                        servicosExecutados.setPrompt("Serviços");
                        //muda cor da seta seletora do spinner
                        servicosExecutados.getBackground().setColorFilter(getResources().getColor(R.color.colorVertvOrange), PorterDuff.Mode.SRC_ATOP);

                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                carregamento.setVisibility(View.GONE);
            }
        });
    }

    public static ArrayList<String> getListaServicosExecutados() {
        return listaServicosExecutados;
    }


    public void enviaOS(View view) {
//        Servicos.retrofitEnviaOS(parametroOS,usuario,contra,nome,
//                String.valueOf(servicosExecutados.getSelectedItem()),
//                anotacaoTecnica.getText().toString(),obser,"1212121");

//        Servicos.enviaImagemRetrofit(ordem, null,corpoProblema);

        Intent intent = new Intent(getApplicationContext(), AssinaturaDigital.class);
        Bundle bundle = new Bundle();
        bundle.putString("os",parametroOS);
        bundle.putString("contra",contra);
        bundle.putString("nome",nome);
        bundle.putString("obser",obser);
        bundle.putString("celular",celular);
        bundle.putString("anotacao", String.valueOf(anotacaoTecnica.getText()));
        bundle.putString("user", usuario);
        bundle.putString("servico", String.valueOf(servicosExecutados.getSelectedItem()));
        bundle.putByteArray("fotoProblema",imagem);
        intent.putExtras(bundle);

        Log.d("TAGA",String.valueOf(servicosExecutados.getSelectedItem()));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //Area da FOTO
    @SuppressLint("SetTextI18n")
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
                    imagem = Servicos.converterBitmapParaArray(bitmap);

                    tipoDeDadoDaAssinatura = RequestBody.create(MediaType.parse(
                            "multipart/form-data"),imagem);

                    corpoProblema = MultipartBody.Part.createFormData("problema",
                            "desenhoNome",tipoDeDadoDaAssinatura);


                    ordem =
                            RequestBody.create(
                                    okhttp3.MultipartBody.FORM, os); //os

                    tipoDeDadoDaAssinatura = RequestBody.create(MediaType.parse(
                            "multipart/form-data"),imagem);

                    corpoProblema = MultipartBody.Part.createFormData("problema",
                            "desenhoNome",tipoDeDadoDaAssinatura);

                    confirmacaoFoto.setText("FOTO CAPTURADA");
                    confirmacaoFoto.setVisibility(View.VISIBLE);
                    //Servicos.enviaAssinaturaRetrofit(ordem, null,corpoProblema);

                }else if( resultCode == RESULT_CANCELED)
                {
                    Toast.makeText(getBaseContext(), "A captura foi cancelada",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "A câmera foi fechada",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void tirarFoto(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }


    private void retrofitEnviaAtendimento(final String os_var, final String tecnicoLocal)
    {
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

                        } else {
                            //Dismissing the loading progressbar
                            //progresso.dismiss();
                            carregamento.setVisibility(View.INVISIBLE);

                            finish();
                            Toast.makeText(AtendimentoOS.this, respostaServidor.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                //progresso.dismiss();
                carregamento.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Erro na chamada ao servidor" + t, Toast.LENGTH_LONG).show();

            }
        });
    }

    //Fim Area da Foto

}
