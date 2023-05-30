package uk.cryss.httpclient;

import java.io.IOException;
import java.net.SocketException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class HttpClientExample1_1 {
	
	//configura retentativa da chamada 
	static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
	    @Override
	    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
	        return executionCount < 5;
	    }
	};
	
    public static void main(String[] args) throws ClientProtocolException, IOException {
    	
//        CloseableHttpClient httpClient = HttpClients.createDefault();
    	

//      Modifica o Socket para manipular o socket   
        SocketConfig socketConfig = SocketConfig.custom().setSoLinger(1).setTcpNoDelay(true).build();
        
        CloseableHttpClient httpClient = HttpClients.custom()
        		//  .setRetryHandler(requestRetryHandler)
        		  .setDefaultSocketConfig(socketConfig)
        		  .build();
        


        
      //  String url = "https://httpbin.org/get";
       String urlLocal = "http://192.168.1.68:8081/animes";

		try {
            
           HttpGet request = new HttpGet(urlLocal);
        //   HttpGet request = new HttpGet(url);
            
            
           // setando tempo da chamada
//            RequestConfig requestConfig = RequestConfig.custom()
//        			.setConnectionRequestTimeout(5000)
//        			.setConnectTimeout(5000)
//        			.setSocketTimeout(5000)
//        			.build();
            
//            request.setConfig(requestConfig);
   
            request.addHeader("custom-key", "mykong");
            request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

            // executa req, usado instancia do tipo GET
            for(var i=0; i<10; i++) {
            	Thread.sleep(2000);
            	
            	CloseableHttpResponse response = httpClient.execute(request);
            	
            	  try {
                      // Get HttpResponse Status
                      System.out.println(response.getProtocolVersion()); // HTTP/1.1
                      System.out.println(response.getStatusLine().getStatusCode()); // 200
                      System.out.println(response.getStatusLine().getReasonPhrase()); // OK
                      System.out.println(response.getStatusLine().toString()); // HTTP/1.1 200 OK

                      HttpEntity entity = response.getEntity();

                      if (entity != null) {
                          // return it as a String
                          String result = EntityUtils.toString(entity);
                          System.out.println(result);
                      }

                  } finally {
                      response.close();
                  }

            }
           // CloseableHttpResponse response = httpClient.execute(request);

          
        } 
		
		catch(SocketException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
            httpClient.close();
        }
    }
}