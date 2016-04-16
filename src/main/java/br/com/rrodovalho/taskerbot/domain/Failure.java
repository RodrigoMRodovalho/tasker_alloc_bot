package br.com.rrodovalho.taskerbot.domain;

/**
 * Created by rrodovalho on 14/04/16.
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Failure {

    @SerializedName("reason")
    @Expose
    private List<String> reason = new ArrayList<String>();
    @SerializedName("index")
    @Expose
    private List<Integer> index = new ArrayList<Integer>();

    /**
     *
     * @return
     * The index
     */
    public List<Integer> getIndex() {
        return index;
    }

    /**
     *
     * @param index
     * The index
     */
    public void setIndex(List<Integer> index) {
        this.index = index;
    }

    /**
     *
     * @return
     * The reason
     */
    public List<String> getReason() {
        return reason;
    }

    /**
     *
     * @param reason
     * The reason
     */
    public void setReason(List<String> reason) {
        this.reason = reason;
    }

}
