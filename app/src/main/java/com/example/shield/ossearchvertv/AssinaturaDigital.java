package com.example.shield.ossearchvertv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shield.ossearchvertv.EmailServicos.SendMailTask;
import com.example.shield.ossearchvertv.Helper.CaptureSignatureView;
import com.example.shield.ossearchvertv.Helper.Servicos;

import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.shield.ossearchvertv.AtendimentoOS.servicosExecutados;

public class AssinaturaDigital extends AppCompatActivity {

    CaptureSignatureView mSig;
    LinearLayout mContent;
    private MultipartBody.Part corpoProblema;
    private RequestBody tipoDeDadoDaFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura_digital);


        mContent = findViewById(R.id.linearLayout);
        mSig = new CaptureSignatureView(this, null);
        mContent.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

    }


    public void limpar(View view) {
        mSig.ClearCanvas();
    }

    public void download(View view) {

        byte[] desenhoDaAssinatura = mSig.getBytes();

        //pegando valor da activity anterior
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String parametroOS = bundle.getString("os");
        String servicosExecutado = bundle.getString("servico");
        String usuario = bundle.getString("user");
        String nome = bundle.getString("nome");
        String anotacao = bundle.getString("anotacao");
        String contra = bundle.getString("contra");
        String obser = bundle.getString("obser");
        byte[] imagem = bundle.getByteArray("fotoProblema");
        String email = bundle.getString("email");
//        if(email.equals(""))
//        {
//            email = "ti@vertv.com.br";
//        }
//
//        //MAIL FORM//
//
//        String fromEmail = "tecnicosvertv@gmail.com";
//        String fromPassword = "vertvtec";
//        String toMails = email+",charles@vertv.com.br";
//        List<String> toEmailList = Arrays.asList(toMails.split("\\s*,\\s*"));
//        String emailSubject = "[VERTV] Comprovante de Realização de Ordem de Serviço "+parametroOS;
//        String emailBody =  "<h1 style=color:#ff9900>COMPROVANTE OS</h1>"+
//                            "<p><b>Numero da OS: </b>" + parametroOS + "</p>" +
//                            "<p><b>Contrato: </b>" + contra + "</p>" +
//                            "<p><b>Nome Assinante: </b>" + nome + "</p>" +
//                            "<p><b>Tecnico: </b>" + usuario + "</p>" +
//                            "<p><b>motivo da OS: </b>" + obser + "</p>";

        //FIM MAIL FORM//

//        RequestBody tipoDeDadoDaAssinatura = RequestBody.create(MediaType.parse(
//                "multipart/form-data"), desenhoDaAssinatura);
//
//        MultipartBody.Part corpoDaAssinatura = MultipartBody.Part.createFormData("assinatura",
//                "desenhoNome", tipoDeDadoDaAssinatura);
//
//        assert parametroOS != null;
//        RequestBody ordem =
//                RequestBody.create(
//                        okhttp3.MultipartBody.FORM, parametroOS); //os

        //DADOS DA FOTO TIRADA NA ACTIVITT ANTERIOR
//        assert imagem != null;
//        RequestBody tipoDeDadoDaFoto = RequestBody.create(MediaType.parse(
//                "multipart/form-data"), imagem);
//
//        MultipartBody.Part corpoProblema = MultipartBody.Part.createFormData("problema",
//                "desenhoNome", tipoDeDadoDaFoto);
        //FIM DADOS DA FOTO TIRADA NA ACTIVITT ANTERIOR

        //converte a assinatura para ImageView, devido ela ser um bitmap
        converteByteArrayToImageView(desenhoDaAssinatura);

        //Log.i("SendMailActivity", "Send Button Clicked.");
//        Log.i("SendMailActivity", "To List: " + toEmailList);
//        new SendMailTask(AssinaturaDigital.this).execute(fromEmail, fromPassword, toEmailList, emailSubject, emailBody);

        Intent intentEnvia = new Intent(getApplicationContext(), BluetoothPrinterConnection.class);
        Bundle bundleEnvia = new Bundle();
        bundleEnvia.putString("os",parametroOS);
        bundleEnvia.putString("contra",contra);
        bundleEnvia.putString("nome",nome);
        bundleEnvia.putString("obser",obser);
        bundleEnvia.putString("email", email);
        bundleEnvia.putString("anotacao", anotacao);
        bundleEnvia.putString("user", usuario);
        bundleEnvia.putString("servicoExecutado", servicosExecutado);
        bundleEnvia.putByteArray("fotoProblema",imagem);
        bundleEnvia.putByteArray("assinaturaAssinante",desenhoDaAssinatura);

        intentEnvia.putExtras(bundleEnvia);
        startActivity(intentEnvia);

//        //Envia as requisições para o servidor
//        Servicos.retrofitEnviaOS(parametroOS, usuario, contra, nome, servicosExecutado, anotacao, obser, email);
//        Servicos.enviaImagemRetrofit(ordem, corpoDaAssinatura, null);//envia a assinatura
//        Servicos.enviaImagemRetrofit(ordem, null, corpoProblema);//envia a foto
        finish();
    }

    public void converteByteArrayToImageView(byte[] byteArray) {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = findViewById(R.id.imageView1);
        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                image.getHeight(), false));
    }
}

