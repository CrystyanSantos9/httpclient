package uk.cryss.httpclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientSingleTone {

	public static void main(String[] args) {
		
		String result;
		
		for(var i=0; i<100;i++) {
			
			try {
				Thread.sleep(500);
				
				result = sendPOST("https://httpbin.org/post");			
				System.out.println(result);
				
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
			
//			new Thread(()-> {
//				String result;
//				try {
//					result = sendPOST("https://httpbin.org/post");
//					System.out.println(result);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			
//			}).start();
		}
	

	}

	private static String sendPOST(String url) throws IOException {

		String result = "";
		HttpPost post = new HttpPost(url);
		//post usando urlEnconded
		// add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("username", "abc"));
        urlParameters.add(new BasicNameValuePair("password", "123"));
        urlParameters.add(new BasicNameValuePair("custom", "secret"));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.custom()
        		.setMaxConnPerRoute(25)
        		.setMaxConnTotal(50)
        		.setDefaultRequestConfig(RequestConfig.custom()
        				.setConnectionRequestTimeout(10000)
        				.build())
        		.build();
        
             CloseableHttpResponse response = httpClient.execute(post)){
        	 result = EntityUtils.toString(response.getEntity());
        }
        return result;
	}

}
