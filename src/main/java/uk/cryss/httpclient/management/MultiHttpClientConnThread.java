package uk.cryss.httpclient.management;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class MultiHttpClientConnThread extends Thread {
	private CloseableHttpClient client;
	private HttpGet get;

	public MultiHttpClientConnThread(CloseableHttpClient client, HttpGet get) {
		super();
		this.client = client;
		this.get = get;
	}

	public void run() {

		try {
			getResponse();

		} catch (ClientProtocolException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private synchronized void getResponse() throws IOException, ClientProtocolException {

		System.out.println("Current thread -->>> " + currentThread());
		CloseableHttpResponse response = client.execute(get);
		EntityUtils.consume(response.getEntity());

		// Get HttpResponse Status
		System.out.println(response.getProtocolVersion()); // HTTP/1.1
		System.out.println(response.getStatusLine().getStatusCode()); // 200
		System.out.println(response.getStatusLine().getReasonPhrase()); // OK
		System.out.println(response.getStatusLine().toString()); // HTTP/1.1 200 OK

		// se houver dados eu passo o response para string
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// return it as a String
			String result = EntityUtils.toString(entity);
			System.out.println(result);
		}

	}

}
