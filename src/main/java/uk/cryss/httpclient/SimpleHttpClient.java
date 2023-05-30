package uk.cryss.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * Hello world!
 *
 */
public class SimpleHttpClient {

    static int timeout = 10;
    static int socketTimeout = 13;

    static RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(timeout * 1000)
            .setConnectionRequestTimeout(timeout * 1000)
            .setSocketTimeout(socketTimeout * 1000)
            .build();

    final static CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

    public static void simpleGet() throws Exception {
        HttpGet request = new HttpGet("https://api-support-aws.sensedia.com/csp/prd/tests/timeout");

        CloseableHttpResponse response = httpClient.execute(request);
        //Lida com dados da resposta como um objeto
        HttpEntity entity = response.getEntity();

        System.out.println(EntityUtils.toString(entity));
    }

    public static void main(String[] args) throws Exception {
        simpleGet();
    }
}