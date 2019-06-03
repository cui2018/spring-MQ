package com.properpush.util;

import okhttp3.OkHttpClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: cui
 * @Date: 2019/1/8 11:42
 * @Description:
 */
public class HttpClient extends ClientUtil {

    private static OkHttpClient client = new OkHttpClient();

    /**
     * 私有化工具类的构造函数，避免对工具类的实例化
     */
    protected HttpClient() { }

    /**
     * 静态方法调用私有构造函数，以覆盖对构造函数的测试
     */
    static {
        new HttpClient();
    }

    public static ResponseEntity<byte[]> get(String url) throws IOException {
        return perform(client, url, GET, null, null, null);
    }

    public static ResponseEntity<byte[]> get(String url, int timeout) throws IOException {
        client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return perform(client, url, GET, null, null, null);
    }

    public static ResponseEntity<byte[]> get(String url, Map<String, String> headers) throws IOException {
        return perform(client, url, GET, headers, null, null);
    }

    public static ResponseEntity<byte[]> post(String url, MediaType type, String data) throws IOException {
        return perform(client, url, POST, null, type, data);
    }

    public static ResponseEntity<byte[]> post(String url, MediaType type, String data, int timeout) throws IOException {
        client = new OkHttpClient.Builder().readTimeout(timeout, TimeUnit.MILLISECONDS).build();
        return perform(client, url, POST, null, type, data);
    }


    public static ResponseEntity<byte[]> post(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, POST, headers, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, null, type, data);
    }

    public static ResponseEntity<byte[]> put(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, PUT, headers, type, data);
    }

    public static ResponseEntity<byte[]> delete(String url) throws IOException {
        return perform(client, url, DELETE, null, null, null);
    }

    public static ResponseEntity<byte[]> delete(String url, Map<String, String> headers) throws IOException {
        return perform(client, url, DELETE, headers, null, null);
    }

    public static ResponseEntity<byte[]> delete(String url, MediaType type, String data) throws IOException {
        return perform(client, url, DELETE, null, type, data);
    }

    public static ResponseEntity<byte[]> delete(String url, Map<String, String> headers, MediaType type, String data) throws IOException {
        return perform(client, url, DELETE, headers, type, data);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
