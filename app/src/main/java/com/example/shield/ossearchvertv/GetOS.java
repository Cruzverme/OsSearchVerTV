package com.example.shield.ossearchvertv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shield.ossearchvertv.EmailServicos.SendMailTask;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.makeText;

public class GetOS extends AppCompatActivity {

    private TextView os, endereco, contra, nome, telComercial,telResidencial,telCelular, alternativo,obser1;
    EditText textoServ;
    ProgressDialog progresso;
    Button botaoEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_os);

        os = (TextView) findViewById(R.id.textViewOS);
        contra = (TextView) findViewById(R.id.textViewContra);
        nome = (TextView) findViewById(R.id.textViewNome);
        endereco = (TextView) findViewById(R.id.textViewEndereco);
        obser1 = (TextView) findViewById(R.id.textViewObser);
        telComercial = (TextView) findViewById(R.id.textViewTelComercial);
        telResidencial = (TextView) findViewById(R.id.textViewTelResidencial);
        telCelular = (TextView) findViewById(R.id.textViewTelCelular);
        //alternativo = (TextView) findViewById(R.id.naoecontradoID);
        botaoEmail = (Button) findViewById(R.id.botaoEmailID);
        textoServ = (EditText) findViewById(R.id.textoServExecID);

        //pegando valor da activity anterior
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String parametroOS = bundle.getString("os");
        String usuario = bundle.getString("user");

        listenerButton(parametroOS, usuario);
    }

    private void listenerButton(String parametroOS, String tecnico) {
        //POPUP de LOADING
        progresso = new ProgressDialog(GetOS.this);
        progresso.setTitle("Carregando...");
        progresso.show();

        //chama o retrofit para ele trabalhar
        retrofitEscravo(parametroOS, tecnico);
    }

    private void retrofitEscravo(String os_var, final String tecnicoLocal) {

        //pegando data e hora do celular
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss");
        Date data = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        final Date dataAtual = calendar.getTime();


        //Minha URLBASE
        RetrofitService service = ServiceGenerator.createService(RetrofitService.class);
        Call<RespostaServidor> call = service.mostrarOS(os_var);

        call.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response)
            {
                final RespostaServidor respostaServidor = response.body(); //pega o json

                if(response.isSuccessful())
                {
                    if (respostaServidor != null) //se o body nao está vazio
                    {
                        if (respostaServidor.getSuccess() == 1)
                        {
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

     /*############################################ ENVIA EMAIL #########################################################*/
                            botaoEmail.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String body = "Técnico: "  + tecnicoLocal +
                                            "\nOS: " + os.getText().toString() +
                                            "\nContrato: "+ contra.getText().toString() +
                                            "\nNome: "+ nome.getText().toString() +
                                            "\nEndereço: "+ endereco.getText().toString() +
                                            "\nComercial: "+ telComercial.getText().toString() +
                                            "\nResidencial: " + telResidencial.getText().toString() +
                                            "\nCelular: "+ telCelular.getText().toString() +
                                            "\nServiço Executado: \n" + textoServ.getText().toString() +
                                            "\n" +
                                            "\nData Execução: " + dateFormat.format(dataAtual);
                                    try {
                                        new SendMailTask(GetOS.this).execute(tecnicoLocal,body);// emailSubject, emailBody);contra.setText("");
                                        os.setText("");
                                        contra.setText("");
                                        nome.setText("");
                                        endereco.setText("");
                                        telCelular.setText("");
                                        telResidencial.setText("");
                                        telComercial.setText("");
                                        textoServ.setText("");
                                        obser1.setText("");
                                    }catch (android.content.ActivityNotFoundException ex){
                                        Toast.makeText(getApplicationContext(),"Não foi possível enviar o email, tente novamente.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
     /*###############################################FIM ENVIA EMAIL ########################################################*/
                        }else{
                            //                alternativo.setVisibility(View.VISIBLE);
                            os.setVisibility(View.INVISIBLE);
                            endereco.setVisibility(View.INVISIBLE);
                            contra.setVisibility(View.INVISIBLE);
                            nome.setVisibility(View.INVISIBLE);
                            telComercial.setVisibility(View.INVISIBLE);
                            telResidencial.setVisibility(View.INVISIBLE);
                            telCelular.setVisibility(View.INVISIBLE);
                            alternativo.setVisibility(View.INVISIBLE);
                            obser1.setVisibility(View.INVISIBLE);
                            botaoEmail.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                progresso.dismiss();
                Log.e("ERRO:", "SEFERRAEW" + t);
                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor" + t, Toast.LENGTH_LONG).show();

            }
        });


    }

}