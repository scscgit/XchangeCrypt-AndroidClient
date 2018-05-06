package io.swagger.client.api;

import com.android.volley.VolleyError;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.swagger.client.ApiException;
import io.swagger.client.ApiInvoker;
import io.swagger.client.Pair;
import io.swagger.client.model.AccountWalletResponse;
import io.swagger.client.model.InlineResponse20010;

/**
 * Created by Peter on 05.05.2018.
 */

public class AccountApi {



    String basePath = "https://xchangecrypttest-convergencebackend.azurewebsites.net/api/v1/accountapi";
    ApiInvoker apiInvoker = ApiInvoker.getInstance();

    public void addHeader(String key, String value) {
        getInvoker().addDefaultHeader(key, value);
    }

    public ApiInvoker getInvoker() {
        return apiInvoker;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getBasePath() {
        return basePath;
    }


    public List<AccountWalletResponse> walletGet () throws TimeoutException, ExecutionException, InterruptedException, ApiException {
        Object postBody = null;

        // create path and map variables
        String path = "/wallets";

        // query params
        List<Pair> queryParams = new ArrayList<Pair>();
        // header params
        Map<String, String> headerParams = new HashMap<String, String>();
        // form params
        Map<String, String> formParams = new HashMap<String, String>();
        String[] contentTypes = {
                "application/x-www-form-urlencoded"
        };
        String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";

        if (contentType.startsWith("multipart/form-data")) {
            // file uploading
            MultipartEntityBuilder localVarBuilder = MultipartEntityBuilder.create();
            HttpEntity httpEntity = localVarBuilder.build();
            postBody = httpEntity;
        } else {
            // normal form params
        }

        String[] authNames = new String[] { "oauth" };

        try {
            String localVarResponse = apiInvoker.invokeAPI (basePath, path, "GET", queryParams, postBody, headerParams, formParams, contentType, authNames);
            if (localVarResponse != null) {

                List<AccountWalletResponse> accountWalletResponses = new ArrayList<>();
                JSONArray jsonarray = new JSONArray(localVarResponse);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String coinSymbol = jsonobject.getString("coinSymbol");
                    String walletPublicKey = jsonobject.getString("walletPublicKey");
                    Double balance = jsonobject.getDouble("balance");

                AccountWalletResponse r = new AccountWalletResponse(coinSymbol,walletPublicKey,balance);
                accountWalletResponses.add(r);
                }
                return accountWalletResponses;

              //  return (List<AccountWalletResponse>) ApiInvoker.deserialize(localVarResponse, "list", AccountWalletResponse.class);
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
