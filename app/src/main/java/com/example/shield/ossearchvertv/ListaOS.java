package com.example.shield.ossearchvertv;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.shield.ossearchvertv.Helper.Servicos;
import com.example.shield.ossearchvertv.Retrofit.RespostaServidor;
import com.example.shield.ossearchvertv.Retrofit.RetrofitService;
import com.example.shield.ossearchvertv.Retrofit.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaOS extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listaOS;
    private static ArrayList<String> listaDeOrdemServico = new ArrayList<String>();
    private static ProgressBar carregamento;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_os);

        listaOS = (ListView) findViewById(R.id.osAreaID);
        carregamento = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutID);

        // Seta o Listener para atualizar o conteudo quando o gesto for feito
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setColorSchemeResources(
                R.color.colorVertvOrange
        );

        //pegando valor da activity anterior MainActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        final String usuarioLocal = bundle.getString("user");

        retrofitOrdemServicoLista(usuarioLocal);

        listaOS.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object valorDoItemClicado = listaOS.getItemAtPosition(i);

                //envia as infos para a proxima activity
                Intent intent = new Intent(getApplicationContext(), GetOS.class);

                Bundle bundle = new Bundle();

                bundle.putString("os", String.valueOf(valorDoItemClicado));
                bundle.putString("user", usuarioLocal);
                intent.putExtras(bundle);
                startActivity(intent);

                ///// fim envia prox activity \\\\\

                carregamento.setVisibility(ProgressBar.INVISIBLE);
            }
        });

        listaOS.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount)
            {
                boolean enable = false;

                if(listaOS != null && listaOS.getChildCount() > 0)
                {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listaOS.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listaOS.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }

                swipeRefreshLayout.setEnabled(enable);
            }
        });
    }

    public void retrofitOrdemServicoLista(String tecnico) {
        final RetrofitService service = ServiceGenerator.createService(RetrofitService.class);

        //POPUP de LOADING
        carregamento.setVisibility(View.VISIBLE);

        Call<RespostaServidor> callList = service.listarOS(tecnico);

        callList.enqueue(new Callback<RespostaServidor>() {
            @Override
            public void onResponse(Call<RespostaServidor> call, Response<RespostaServidor> response) {
                final RespostaServidor respostaServidor = response.body();

                if (response.isSuccessful())
                {
                    if (respostaServidor.getSuccess() == 1)
                    {
                        listaDeOrdemServico.clear();
                        listaDeOrdemServico.addAll(respostaServidor.getOrdemServicoLista());

                        carregamento.setVisibility(View.INVISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.lista_os_layout, getListaOrdemServico());


                    listaOS.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<RespostaServidor> call, Throwable t) {
                carregamento.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static ArrayList<String> getListaOrdemServico()
    {
        return listaDeOrdemServico;
    }

    @Override
    public void onRefresh()
    {
        //pegando valor da activity anterior MainActivity
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        assert bundle != null;
        final String usuarioLocal = bundle.getString("user");

        retrofitOrdemServicoLista(usuarioLocal);
    }

}
