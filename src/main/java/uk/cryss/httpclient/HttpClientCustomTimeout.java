package uk.cryss.httpclient;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientCustomTimeout {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		// TODO Auto-generated method stub

		int timeout = 5;
		int connectionTimeout = 5;
		int socketTimeout = 5;
		
		RequestConfig config = RequestConfig.custom()
				  .setConnectTimeout(connectionTimeout * 1000)
				  .setConnectionRequestTimeout(timeout * 1000)
				  .setSocketTimeout(socketTimeout * 1000)
				  .build();
		
		
		CloseableHttpClient client = 
				  HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		
		// Usando cliente web criado
		
	//	final HttpGet request = new HttpGet("http://192.168.1.68:3000");
		final HttpGet request = new HttpGet("http://192.168.1.6:8080");
		
		
		
		
		CloseableHttpResponse response = client.execute(request);
		
	
	 	  try {
              // Get HttpResponse Status
              System.out.println(response.getProtocolVersion()); // HTTP/1.1
              System.out.println(response.getStatusLine().getStatusCode()); // 200
              System.out.println(response.getStatusLine().getReasonPhrase()); // OK
              System.out.println(response.getStatusLine().toString()); // HTTP/1.1 200 OK
             
              HttpEntity entity = response.getEntity();
              
              OutputStream output = new PrintStream(System.out);
              
              

              if (entity != null) {
            	  entity.writeTo(output);
                  // return it as a String
                  String result = EntityUtils.toString(entity);
                  System.out.println(result);
              }

          } finally {
              response.close();
          }
		
	}

}
