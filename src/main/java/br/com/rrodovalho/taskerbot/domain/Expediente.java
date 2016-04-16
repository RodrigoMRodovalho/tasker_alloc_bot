package br.com.rrodovalho.taskerbot.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Expediente {

    @SerializedName("data")
    @Expose
    private String data;
    @SerializedName("alocacoes")
    @Expose
    private List<Alocacao> alocacoes = new ArrayList<Alocacao>();

    /**
     *
     * @return
     * The data
     */
    public String getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The alocacoes
     */
    public List<Alocacao> getAlocacoes() {
        return alocacoes;
    }

    /**
     *
     * @param alocacoes
     * The alocacoes
     */
    public void setAlocacoes(List<Alocacao> alocacoes) {
        this.alocacoes = alocacoes;
    }

}