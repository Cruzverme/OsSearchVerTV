package com.example.shield.ossearchvertv;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.CaptureSignatureView;
import com.example.shield.ossearchvertv.Helper.Servicos;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssinaturaDigital extends AppCompatActivity {

    CaptureSignatureView mSig;
    LinearLayout mContent;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura_digital);


        mContent = (LinearLayout) findViewById(R.id.linearLayout);
        mSig = new CaptureSignatureView(this, null);
        mContent.addView(mSig, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        Button botaoClear = (Button) findViewById(R.id.limparID);
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

        RequestBody tipoDeDadoDaAssinatura = RequestBody.create(MediaType.parse(
                "multipart/form-data"),desenhoDaAssinatura);

        MultipartBody.Part corpoDaAssinatura = MultipartBody.Part.createFormData("assinatura",
                "desenhoNome",tipoDeDadoDaAssinatura);

        assert parametroOS != null;
        RequestBody ordem =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, parametroOS); //os

        converteByteArrayToImageView(desenhoDaAssinatura);
        Servicos.enviaAssinaturaRetrofit(ordem,corpoDaAssinatura,null);
        Toast.makeText(getApplicationContext(), "OS Enviada", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void converteByteArrayToImageView(byte[] byteArray)
    {
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.imageView1);
        image.setImageBitmap(Bitmap.createScaledBitmap(bmp, image.getWidth(),
                image.getHeight(), false));
    }

}
