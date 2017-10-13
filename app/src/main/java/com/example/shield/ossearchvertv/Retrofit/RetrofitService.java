package com.example.shield.ossearchvertv.Retrofit;

import com.example.shield.ossearchvertv.OrdemServico;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Shield on 06/10/2017.
 */

public interface RetrofitService {
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })


    @GET("get_all_products.php")
    public void mostraTudo(Callback<List<OrdemServico>> response);

    //@FormUrlEncoded FUNCIONAL
    @GET("get_os_spec.php")
    Call<RespostaServidor> mostrarOS(@Query("os") String os);

    @GET("criar_user.php") //FUNCIONAL
    Call<RespostaServidor> criarUsuario(@Query("usuario") String usuario,
                                        @Query("password") String password,
                                        @Query("nome") String nome,
                                        @Query("equipe") String equipe);

    @GET("confirma_login.php")
    Call<RespostaServidor> logarUsuario(@Query("usuario") String usuario,
                                        @Query("password") String password);
    //@FormUrlEncoded
    @GET("envia_os.php")
    Call<RespostaServidor> enviaOS(@Query("os") String os,
                                   @Query("tecnico") String tecnico,
                                   @Query("equipe") String equipe,
                                   @Query("contrato") String contrato,
                                   @Query("assinante") String assinante,
                                   @Query("servicoExecutado") String servico,
                                   @Query("anotacoes") String anotacao,
                                   @Query("observacao") String observacao,
                                   @Query("imagem") String imagem);
}
