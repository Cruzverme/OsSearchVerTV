package com.example.shield.ossearchvertv;

/**
 * Created by Shield on 06/10/2017.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.w3c.dom.Text;

import java.util.List;

public class OrdemServico {

    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("codcid")
    @Expose
    private String codcid;
    @SerializedName("contra")
    @Expose
    private String contra;
    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("endereco")
    @Expose
    private String endereco;
    @SerializedName("obs1")
    @Expose
    private String observacao;
    @SerializedName("comercial")
    @Expose
    private String comercial;
    @SerializedName("residencial")
    @Expose
    private String residencial;
    @SerializedName("celular")
    @Expose
    private String celular;
    @SerializedName("servicos")
    @Expose
    private List<String> servicos = null;
    @SerializedName("token_enviado_para")
    @Expose
    private String enviado_para;


    public OrdemServico(){

    }


    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getObser(){return observacao;}

    public void set(String observacao){this.observacao = observacao; }

    public String getComercial() {
        return comercial;
    }

    public void setComercial(String comercial) {
        this.comercial = comercial;
    }

    public String getResidencial() {
        return residencial;
    }

    public void setResidencial(String residencial) {
        this.residencial = residencial;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public List<String> getServicos() {
        return servicos;
    }

    public void setServicos(List<String> servicos) {
        this.servicos = servicos;
    }

    public String getEnviado_para() {
        return enviado_para;
    }
}
