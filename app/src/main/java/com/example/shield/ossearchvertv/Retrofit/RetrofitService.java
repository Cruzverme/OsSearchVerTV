package com.example.shield.ossearchvertv.Retrofit;

import android.widget.EditText;

import com.example.shield.ossearchvertv.OrdemServico;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    /*@GET("criar_user.php") //FUNCIONAL
    Call<RespostaServidor> criarUsuario(@Query("usuario") String usuario,
                                        @Query("password") String password,
                                        @Query("nome") String nome,
                                        @Query("equipe") String equipe);*/
    @FormUrlEncoded
    @POST("confirma_login.php")
    Call<RespostaServidor> logarUsuario(@Field("usuario") String usuario,
                                        @Field("password") String password);
    //@FormUrlEncoded
    @GET("envia_os.php")
    Call<RespostaServidor> enviaOS(@Query("os") String os,
                                   @Query("tecnico") String tecnico,
                                   @Query("contrato") String contrato,
                                   @Query("assinante") String assinante,
                                   @Query("servicoExecutado") String servico,
                                   @Query("anotacaoTecnico") String anotacao,
                                   @Query("observacao") String observacao,
                                   @Query("celularComprovante") String celularParaEnvio);
//@Query("equipe") String equipe, estava no envia

    @GET("envia_cliente_ausente.php")
    Call<RespostaServidor> enviaAusente(@Query("os") String os,
                                        @Query("tecnico") String tecnico,
                                        @Query("anotacaoTecnico") String anotacao);

    @GET("token/consultaToken.php")
    Call<RespostaServidor> consultaToken(@Query("token") String token);

    @GET("token/salvaToken.php")
    Call<RespostaServidor> salvaToken(@Query("os") String os,
                                      @Query("enviado_para") String telefone);
    @Multipart
    @POST("envia_assinatura_digital.php")
    Call<RespostaServidor> enviaAssinatura(@Part("os") RequestBody os,
                                           @Part MultipartBody.Part assinatura,
                                           @Part MultipartBody.Part problema);

    @GET("verifica_cpf.php")
    Call<RespostaServidor> verificaCPF(@Query("cpf") String cpf,
                                       @Query("contrato") String contrato);

    @GET("lista_servico_executado.php")
    Call<RespostaServidor> listaServicosExecutados();


}