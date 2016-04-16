package br.com.rrodovalho.taskerbot.domain;

/**
 * Created by rrodovalho on 14/04/16.
 */
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.Nullable;

@Generated("org.jsonschema2pojo")
public class AllocationResponse {

    @SerializedName("success")
    @Expose
    private List<Integer> success = new ArrayList<Integer>();
    @SerializedName("failure")
    @Expose
    private Failure failure;

    /**
     *
     * @return
     * The success
     */
    public List<Integer> getSuccess() {
        return success;
    }

    /**
     *
     * @param success
     * The success
     */
    public void setSuccess(List<Integer> success) {
        this.success = success;
    }

    /**
     *
     * @return
     * The failure
     */
    public Failure getFailure() {
        return failure;
    }

    /**
     *
     * @param failure
     * The failure
     */
    public void setFailure(Failure failure) {
        this.failure = failure;
    }

}
