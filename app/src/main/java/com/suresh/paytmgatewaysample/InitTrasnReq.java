package com.suresh.paytmgatewaysample;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

public class InitTrasnReq {
    private static final String TAG = InitTrasnReq.class.getSimpleName();

    @SerializedName("requestType")
    private String mReqType = "Payment";

    @SerializedName("mid")
    private String mMerchantId = "KapilC22337440362583";

    //ask raja what should be the websitename
    private String mWebsiteName = "http://45.114.246.207:9090/KapilChitsBeta";

    @SerializedName("orderId")
    private String mOrderID;

    @SerializedName("txnAmount")
    private JSONObject mTransAmount;

    @SerializedName("userInfo")
    private JSONObject mUserInfo;

    public InitTrasnReq(String orderId, JSONObject transAmount, JSONObject userInfo) {
        mOrderID = orderId;
        mTransAmount = transAmount;
        mUserInfo = userInfo;
    }

    public String getmReqType() {
        return mReqType;
    }

    public String getmMerchantId() {
        return mMerchantId;
    }

    public String getmWebsiteName() {
        return mWebsiteName;
    }

    public String getmOrderID() {
        return mOrderID;
    }

    public JSONObject getmTransAmount() {
        return mTransAmount;
    }

    public JSONObject getmUserInfo() {
        return mUserInfo;
    }
}
