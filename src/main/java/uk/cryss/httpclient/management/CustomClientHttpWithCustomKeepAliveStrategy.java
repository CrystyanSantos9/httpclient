package uk.cryss.httpclient.management;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class CustomClientHttpWithCustomKeepAliveStrategy {

	public static void main(String[] args) {

		for (int i = 0; i <= 1000; i++) {
			Thread thread1 = new Thread(new Runnable() {

				@Override
				public synchronized void run() {
					makeRequest();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread());

				}
			});

			thread1.start();
			
			try {
				thread1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	//Método que faz a chamada
	private static void makeRequest() {
		// criando pooling
		PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();

		poolingConnManager.setMaxTotal(5);
		poolingConnManager.setDefaultMaxPerRoute(2);
		poolingConnManager.setDefaultSocketConfig(SocketConfig.custom().setSoLinger(0).setSoTimeout(1000).build());

		// criando request addres
		HttpGet get1 = new HttpGet("https://proxy-local.crysscloud.uk/api/hello");
		HttpPost post1 = new HttpPost("https://proxy-local.crysscloud.uk/api/login");
		HttpPost postMarcarConsulta = new HttpPost("http://proxy-local.crysscloud.uk/api/consultas");

		HttpGet get2 = new HttpGet("http://192.168.1.110:8081/hello");
		
		HttpGet get3 = new HttpGet("http://192.168.1.70:8000");

		// adcionando headers
		get1.addHeader("custom-key", "mkyong");
		get1.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

		post1.addHeader("content-type", "application/json");
		postMarcarConsulta.addHeader("content-type", "application/json");
		postMarcarConsulta.addHeader("Authorization",
				"Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkFQSSBWb2xsLm1lZCIsImV4cCI6MTY3Nzk5MjQxM30.jtSvw_i_ACI44z-_X8qC1kbDCzn-TIBGWUKFLSA0fxI");
		// crinado content
		StringBuilder json = new StringBuilder();

		json.append("{");
		json.append("\"login\":\"admin\",");
		json.append("\"senha\":\"123456\"");
		json.append("}");

		// criando content dados consulta
		StringBuilder json2 = new StringBuilder();
		
		/*
		 * Format
		 * 
		 * {
				"medico_id": 1,
				"paciente_id": 1,
				"data_consulta": "2023-10-10T10:00:00"
			}
		 * 
		 * 
		*/

		json2.append("{");
		json2.append("\"idMedico\":"+ 1L +",");
		json2.append("\"idPaciente\":"+ 1L +",");
		json2.append("\"data\":\"2023-10-10T10:00:00\"");
		json2.append("}");

		// Including json body data
		try {
			//post1.setEntity(new StringEntity(json.toString()));
			postMarcarConsulta.setEntity(new StringEntity(json2.toString()));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// configurando cliente web
		try (

				// configurando cliente web
				CloseableHttpClient client = HttpClients.custom().setConnectionManager(poolingConnManager)
						.setKeepAliveStrategy(new CustomKeepAliveStrategy().myStrategy).build();

				// executing request type
				CloseableHttpResponse response = client.execute(get3);

		) {

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

			response.close();
			client.close();
			poolingConnManager.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
