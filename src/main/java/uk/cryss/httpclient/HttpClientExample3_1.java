package uk.cryss.httpclient;

// https://mkyong.com/java/apache-httpclient-examples/

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientExample3_1 {

	public static void main(String[] args) {
		// Sendo JSON POST 
		
		 try {
	            String result = sendPOST("https://httpbin.org/post");
	            System.out.println(result);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	private static String sendPOST(String url) throws IOException {
		String result = "";
		HttpPost post = new HttpPost(url);

		StringBuilder json = new StringBuilder();

		json.append("{");
		json.append("\"name\":\"mkyong\",");
		json.append("\"notes\":\"hello\"");
		json.append("}");

		// send a JSON data
		post.setEntity(new StringEntity(json.toString()));

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(post)) {
			
			HttpEntity entity = response.getEntity();
			Header headers = entity.getContentType();
			System.out.println(headers);

			result = EntityUtils.toString(response.getEntity());
		}
		return result;
	}

}
