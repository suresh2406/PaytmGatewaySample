package com.suresh.paytmgatewaysample;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class InitTransRes {
    private static final String TAG = InitTransRes.class.getSimpleName();
    @SerializedName("head")
    private JSONObject head;

    @SerializedName("body")
    private  JSONObject body;

    @SerializedName("txnToken")
    private String transToken;

    @SerializedName("isPromoCodeValid")
    private String isPromoCodeValid;

    @SerializedName("authenticated")
    private String authenticated;

    public JSONObject getHead() {
        return head;
    }

    public JSONObject getBody() {
        return body;
    }

    public String getTransToken() {
        return transToken;
    }

    public String getIsPromoCodeValid() {
        return isPromoCodeValid;
    }

    public String getAuthenticated() {
        return authenticated;
    }

    @NotNull
    public String toString(){
        return "Head : "+head.toString() +
                " \n Body : "+body.toString() +
                " \n transToken : "+transToken;
    }
}

