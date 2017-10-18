package com.example.shield.ossearchvertv.Retrofit;

/**
 * Created by Shield on 06/10/2017.
 */

import com.example.shield.ossearchvertv.OrdemServico;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class RespostaServidor {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("os")
    @Expose
    private List<OrdemServico> os = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("comissao")
    @Expose
    private String comissao;
    @SerializedName("servicos")
    @Expose
    private List<String> servicos = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getComissao(){ return comissao;}

    public void setComissao(String comissao){ this.comissao = comissao;}

    public List<OrdemServico> getOs() {
        return os;
    }

    public void setOs(List<OrdemServico> os) {
        this.os = os;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getServicos() {
        return servicos;
    }

    public void setServicos(List<String> servicos) {
        this.servicos = servicos;
    }
}
