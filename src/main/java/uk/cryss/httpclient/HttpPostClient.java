package uk.cryss.httpclient;

import java.io.IOException;
import java.net.SocketException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpPostClient {

	public static void main(String[] args) {

		// "https://httpbin.org/post"

		String url = "http://192.168.1.110:8081/animes";
		String urlSensedia = "https://api-support-aws.sensedia.com/csp/prd/tests/animes";

		// Sendo JSON POST

		try {
			// String result = sendPOST(urlSensedia);
			// System.out.println(result);

			sendGET(urlSensedia);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String sendPOST(String url) throws IOException {

		String result = "";
		HttpPost post = new HttpPost(url);

		post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		StringBuilder json = new StringBuilder();

//			json.append("{");
//			json.append("\"name\":\"mkyong\",");
//			json.append("\"notes\":\"hello\"");
//			json.append("}");

		json.append("{");
		json.append("\"name\":\"dbz 3\"");
		json.append("}");

		// send a JSON data
		post.setEntity(new StringEntity(json.toString()));

		try (CloseableHttpClient httpClient = HttpClients.custom().setMaxConnPerRoute(25).setMaxConnTotal(50)
				.setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(10000).build()).build();

				CloseableHttpResponse response = httpClient.execute(post)) {

			HttpEntity entity = response.getEntity();
			Header headers = entity.getContentType();
			System.out.println(headers);

			result = EntityUtils.toString(response.getEntity());
		}
		return result;
	}

	private static void sendGET(String url) throws ClientProtocolException, IOException {
		// Modifica o Socket para manipular o socket
		SocketConfig socketConfig = SocketConfig.custom().setSoLinger(0).setTcpNoDelay(true).build();

		CloseableHttpClient httpClient = HttpClients.custom().setMaxConnPerRoute(25).setMaxConnTotal(50)
				// .setRetryHandler(requestRetryHandler)
				.setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(10000).build())
				.setDefaultSocketConfig(socketConfig).build();

		try {

			HttpGet request = new HttpGet(url);

			request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

			CloseableHttpResponse response = httpClient.execute(request);

			try {
				// Get HttpResponse Status
				System.out.println(response.getProtocolVersion()); // HTTP/1.1
				System.out.println(response.getStatusLine().getStatusCode()); // 200
				System.out.println(response.getStatusLine().getReasonPhrase()); // OK
				System.out.println(response.getStatusLine().toString()); // HTTP/1.1 200 OK
				System.out.println(response.getEntity().getContentType()); // Application/json

				HttpEntity entity = response.getEntity();

				if (entity != null) {
					// return it as a String

					String result = EntityUtils.toString(entity);
					System.out.println(result);

				}

			} finally {
				response.close();
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} finally {
			httpClient.close();
		}
	}

}
