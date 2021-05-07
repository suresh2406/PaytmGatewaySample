package com.suresh.paytmgatewaysample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//import com.paytm.pg.merchant.PaytmChecksum;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button mInitTransButton;
    private EditText etAmount;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        etAmount = findViewById(R.id.et_amount);
        mInitTransButton = findViewById(R.id.button_get_token);
        mInitTransButton.setOnClickListener(v -> {
//            getToken();
//            callInitTransAPI();
            executeInitTransAPI();
        });
    }

    private void callInitTransAPI() {
        String amount = etAmount.getText().toString();
        String merchantId = getString(R.string.merchantID);
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        InitTrasnReq initTrasnReqBody = getInitTransReqBody(amount);
        Call<InitTransRes> initTransReq = apiInterface.getTransactionKey(initTrasnReqBody,
                getInitTransReqHeader(initTrasnReqBody), merchantId, getOrderId());
        initTransReq.enqueue(new Callback<InitTransRes>() {
            @Override
            public void onResponse(Call<InitTransRes> call, Response<InitTransRes> response) {
                Log.d(TAG, "onResponse : " + response.body().toString());
                InitTransRes initTransRes = response.body();
                Log.d(TAG, "onResponse : " + initTransRes.toString());
                Log.d(TAG, "responce headers : " + response.headers().toString());
            }

            @Override
            public void onFailure(Call<InitTransRes> call, Throwable t) {

            }
        });
    }

    private InitTrasnReq getInitTransReqBody(String amount) {
        InitTrasnReq initTrasnReq = null;
        JSONObject transAmount = new JSONObject();
        JSONObject userInfo = new JSONObject();
        try {
            transAmount.put("value", amount);
            transAmount.put("currency", "INR");
            userInfo.put("custId", "CUST_001");
            initTrasnReq = new InitTrasnReq(getOrderId(), transAmount, userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return initTrasnReq;
    }

    private JSONObject getInitTransReqHeader(InitTrasnReq initTrasnReq) {
        JSONObject jsonObject = null;
        String merchantKey = getString(R.string.merchantKey);
        try {
            String paytmCheckSum = PaytmChecksum.generateSignature(initTrasnReq.toString(), merchantKey);
            jsonObject = new JSONObject();
            jsonObject.put("signature", paytmCheckSum);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private String getOrderId() {
        String randomInt = "";
        Random random = new Random();
        randomInt = String.valueOf(random.nextInt(10000));
        return randomInt;
    }

    private void executeInitTransAPI() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getToken();
            }
        });
    }


    public void getToken() {
        String merchantId = getString(R.string.merchantID);
        String merchantKey = getString(R.string.merchantKey);
        String orderId = getString(R.string.orderID);
        JSONObject paytmParams = new JSONObject();
        JSONObject body = new JSONObject();
        try {
            body.put("requestType", "Payment");
            body.put("mid", merchantId);
            body.put("websiteName", "WEBSTAGING");
            body.put("orderId", orderId);
//            body.put("callbackUrl", "https://merchant.com/callback");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject txnAmount = new JSONObject();
        try {
            txnAmount.put("value", "1.00");
            txnAmount.put("currency", "INR");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("custId", "CUST_001");
            body.put("txnAmount", txnAmount);
            body.put("userInfo", userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
         * Generate checksum by parameters we have in body
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */

        String checksum = null;
        try {
            checksum = PaytmChecksum.generateSignature(body.toString(), merchantKey);
            Log.d(TAG, "execute api checksum : " + checksum);
        } catch (Exception e) {
            Log.d(TAG,"checksum error");
            e.printStackTrace();
        }

        JSONObject head = new JSONObject();
        try {
            head.put("signature", checksum);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            paytmParams.put("body", body);
            paytmParams.put("head", head);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String post_data = paytmParams.toString();


        URL url = null;
        try {
            /* for Staging */
            url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid=KapilC22337440362583&orderId=123456");
            /* for Production */
            // URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                DataOutputStream requestWriter = new DataOutputStream(connection.getOutputStream());
                requestWriter.writeBytes(post_data);
                requestWriter.close();
                String responseData = "";
                InputStream is = connection.getInputStream();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(is));
                if ((responseData = responseReader.readLine()) != null) {
                    Log.d(TAG, "Response: " + responseData);
                }
                responseReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}