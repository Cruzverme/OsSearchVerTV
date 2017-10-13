package com.example.shield.ossearchvertv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BuscaOsUnica extends AppCompatActivity {

    private Button buscar;
    private EditText textOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_os_unica);

        textOS = (EditText) findViewById(R.id.OsID);
        buscar = (Button) findViewById(R.id.botaoBuscar);

        //pegando valor da activity anterior MainActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String usuarioLocal = bundle.getString("user");

        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetOS.class);

                Bundle bundle = new Bundle();
                bundle.putString("os", textOS.getText().toString());
                bundle.putString("user", usuarioLocal);
                intent.putExtras(bundle);
                startActivity(intent);
                textOS.setText("");
            }
        });
    }
}
