package br.com.rrodovalho.taskerbot.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Alocacao {

    @SerializedName("inicio")
    @Expose
    private String inicio;
    @SerializedName("termino")
    @Expose
    private String termino;
    @SerializedName("tarefa")
    @Expose
    private String tarefa;
    @SerializedName("comentario")
    @Expose
    private String comentario;

    /**
     *
     * @return
     * The inicio
     */
    public String getInicio() {
        return inicio;
    }

    /**
     *
     * @param inicio
     * The inicio
     */
    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    /**
     *
     * @return
     * The termino
     */
    public String getTermino() {
        return termino;
    }

    /**
     *
     * @param termino
     * The termino
     */
    public void setTermino(String termino) {
        this.termino = termino;
    }

    /**
     *
     * @return
     * The tarefa
     */
    public String getTarefa() {
        return tarefa;
    }

    /**
     *
     * @param tarefa
     * The tarefa
     */
    public void setTarefa(String tarefa) {
        this.tarefa = tarefa;
    }

    /**
     *
     * @return
     * The comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     *
     * @param comentario
     * The comentario
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }


    @Override
    public String toString() {
        return "Alocacao -> " +
                "inicio='" + inicio + '\'' +
                ", termino='" + termino + '\'' +
                ", tarefa='" + tarefa + '\'' +
                ", comentario='" + comentario + '\'' +
                "<-";
    }
}