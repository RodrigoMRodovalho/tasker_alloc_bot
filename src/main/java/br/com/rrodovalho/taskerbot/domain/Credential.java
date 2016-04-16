package br.com.rrodovalho.taskerbot.domain;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Credential {

    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("pass")
    @Expose
    private String pass;

    /**
     *
     * @return
     * The user
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @param user
     * The user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     *
     * @return
     * The pass
     */
    public String getPass() {
        return pass;
    }

    /**
     *
     * @param pass
     * The pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

}