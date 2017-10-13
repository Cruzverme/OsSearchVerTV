package com.example.shield.ossearchvertv.Retrofit;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shield on 06/10/2017.
 *  É nela que estará nossa URL base para acessar o endpoint;
 *  É aonde estará nosso interceptador para mostrar as requisições e respostas do servidor;
 *  Principalmente, é aonde instanciaremos o nosso objeto retrofit para abrir a chamada com webservice.
 */

public class ServiceGenerator {
    //URL base do endpoint. Deve sempre terminar com /
    public static final String API_BASE_URL = "http://192.168.80.5/sisspc/demos/";

    public static <S> S createService(Class<S> serviceClass) {

        //Instancia do interceptador das requisições
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS);

        httpClient.addInterceptor(loggingInterceptor);

        //Instânica Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient.build())
                .build();

        return retrofit.create(serviceClass);
    }
}