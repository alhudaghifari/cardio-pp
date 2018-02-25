package com.urbannightdev.cardiopp.helper;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ghifar on 25/02/18.
 */

public class AmbilToken {

    private String myToken;

    public class getToken extends AsyncTask<Location, Integer, Void> {

        private String tokensaya;

        public void getTokenKu() {

        }

        public String getTokensaya() {
            return tokensaya;
        }

        public void setTokensaya(String tokensaya) {
            this.tokensaya = tokensaya;
        }

        @Override
        protected Void doInBackground(Location... locations) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
            Request request = new Request.Builder()
                    .url("https://api.mainapi.net/token")
                    .post(body)
                    .addHeader("Authorization", "Basic RWQwTDZCOVJPcHdTU242NnZWblVzZGo5MGFRYTpfVEx2TmpMRVBORVQwVjRyeWR4VHlFQm5ZNXNh") // get token
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Postman-Token", "0803bf9d-0182-42a6-8a9b-c26113190a91")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());
                myToken = jsonObject.getString("access_token");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public String getMyToken() {
        return myToken;
    }

    public void setMyToken(String myToken) {
        this.myToken = myToken;
    }
}
