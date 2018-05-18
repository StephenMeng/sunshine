package team.stephen.sunshine.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    public static final String DEFAULT_CHARSET = "utf-8";

    public static String okrHttpGet(String url) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        return httpClient.newCall(request).execute().body().string();
    }

    public static HttpResponse httpGet(String url) throws IOException {
        return httpGet(url, null);
    }

    public static HttpResponse httpGet(String url, Map<String, String> map) throws IOException {
        return httpGet(url, map, null);
    }

    public static HttpResponse httpGet(String url, Map<String, String> map, String proxyStr) throws IOException {
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManagerShared(true);
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpGet get = new HttpGet(url);
        RequestConfig requestConfig = null;
        if (proxyStr != null) {
            HttpHost proxy = new HttpHost(proxyStr.substring(0, proxyStr.indexOf(":")),
                    Integer.parseInt(proxyStr.substring(proxyStr.indexOf(":") + 1)));
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000).setConnectTimeout(30000)
                    .setProxy(proxy)
                    .setConnectionRequestTimeout(30000).build();
        } else {
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(20000).setConnectTimeout(20000)
                    .setConnectionRequestTimeout(20000).build();
        }


        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                get.setHeader(entry.getKey(), entry.getValue());
            }
        }
        get.setConfig(requestConfig);
        HttpResponse httpResponse = httpClient.execute(get);
        return httpResponse;
    }

    public static HttpResponse httpPost(String url, Map<String, String> map) {
        return httpPost(url, map, true, DEFAULT_CHARSET);
    }

    public static HttpResponse httpPost(String url, Map<String, String> map, Boolean needResponse, String charset) {
        HttpClientBuilder clientBuilder = HttpClients.custom().setConnectionManagerShared(true);
        CloseableHttpClient httpClient = clientBuilder.build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000).setConnectTimeout(5000)
                .setConnectionRequestTimeout(5000).build();
        try {
            HttpPost post = new HttpPost(url);
            post.setConfig(requestConfig);

            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
//                StringEntity entity = new StringEntity("pageIndex=2&pageSize=10&action=anarea&jihua=&fieldid=&type=&xueke=&bumen=&area=上海");
                post.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(post);
            if (needResponse)
                return httpResponse;

        } catch (Exception e) {
            e.printStackTrace();
            LogRecod.error(e);
        }
        return null;
    }
}
