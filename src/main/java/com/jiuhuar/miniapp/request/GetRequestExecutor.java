package com.jiuhuar.miniapp.request;

import java.io.IOException;

import com.jiuhuar.miniapp.entity.WxError;
import com.jiuhuar.miniapp.exception.WxMiniException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mike on 17/5/12.
 */
public class GetRequestExecutor implements RequestExecutor<String> {

    @Override
    public String execute(String url, OkHttpClient client, RequestBody ignored) throws WxMiniException {
        Request request = new Request.Builder()
                .url(url).get().build();
        Response response;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new WxMiniException(e);
        }

        if (response.isSuccessful()) {
            try {
                String responseText = response.body().string();
                WxError error = WxError.toError(responseText);
                if (error.getErrcode() != 0) {
                    throw new WxMiniException(error);
                }
                return responseText;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new WxMiniException(response.toString());
        }
    }
}
