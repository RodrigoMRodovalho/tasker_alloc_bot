package br.com.rrodovalho.taskerbot.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Tasker {

    @SerializedName("credential")
    @Expose
    private Credential credential;
    @SerializedName("expedientes")
    @Expose
    private List<Expediente> expedientes = new ArrayList<Expediente>();

    /**
     *
     * @return
     * The credential
     */
    public Credential getCredential() {
        return credential;
    }

    /**
     *
     * @param credential
     * The credential
     */
    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    /**
     *
     * @return
     * The expedientes
     */
    public List<Expediente> getExpedientes() {
        return expedientes;
    }

    /**
     *
     * @param expedientes
     * The expedientes
     */
    public void setExpedientes(List<Expediente> expedientes) {
        this.expedientes = expedientes;
    }

}