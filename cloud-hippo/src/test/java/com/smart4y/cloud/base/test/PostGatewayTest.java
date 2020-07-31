package com.smart4y.cloud.base.test;

import com.smart4y.cloud.core.toolkit.Kit;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Youtao
 * on 2020/7/22 16:41
 */
public class PostGatewayTest {

    private static final String url = "http://127.0.0.1:8888/actuator/metrics";

    public static void main(String[] args) throws IOException {
        String namesObject = sendUrl(url);
        System.out.println(namesObject);
        Map<String, Object> map = Kit.help().json().jsonToMap(namesObject);
        Object namesArray = map.get("names");
        List<String> list = Kit.help().json().fromJson(Kit.help().json().toJson(namesArray), List.class);

        list.forEach(name -> {
            try {
                String metric = sendUrl(url + "/" + name);
                System.out.println(metric);
            } catch (IOException e) {
            }
        });
    }

    private static String sendUrl(String url) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        Response execute = call.execute();
        ResponseBody body = execute.body();
        assert body != null;
        return body.string();
    }
}