package com.properpush.util;

import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.Map;

/**
 * @Auther: cui
 * @Date: 2019/1/8 11:33
 * @Description:
 */
public class ClientUtil {
    protected static final String PUT = "PUT";
    protected static final String GET = "GET";
    protected static final String POST = "POST";
    protected static final String DELETE = "DELETE";

    protected static ResponseEntity<byte[]> perform(OkHttpClient client,
                                                    String url, String method,
                                                    Map<String, String> headers, MediaType type,
                                                    String data) throws IOException {
        okhttp3.Response response = createCall(client, url, method, headers, type, data).execute();
        return converter(response);
    }

    private static Call createCall(OkHttpClient client,
                                   String url, String method,
                                   Map<String, String> headers, MediaType type,
                                   String data) {
        Request.Builder builder = new Request.Builder();
        builder = builder.url(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                builder = builder.addHeader(header.getKey(), header.getValue());
            }
        }
        okhttp3.RequestBody body = null;
        if (StringUtils.isNotBlank(data)) {
            Assert.notNull(type);
            body = RequestBody.create(okhttp3.MediaType.parse(type.toString()), data);
        }
        builder = builder.method(method, body);
        Request request = builder.build();
        return client.newCall(request);
    }

    protected static ResponseEntity<byte[]> converter(okhttp3.Response response) throws IOException {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.putAll(response.headers().toMultimap());
        return new ResponseEntity<>(response.body().bytes(), headers, HttpStatus.valueOf(response.code()));
    }


}
