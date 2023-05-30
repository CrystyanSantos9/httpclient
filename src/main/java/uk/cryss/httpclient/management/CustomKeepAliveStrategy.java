package uk.cryss.httpclient.management;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class CustomKeepAliveStrategy {
	
	
	ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
		
		@Override
		public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
			HeaderElementIterator iterator = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
			
			while(iterator.hasNext()) {
				HeaderElement header = iterator.nextElement();
				
				String param = header.getName();
				String value = header.getValue();
				
				if(value != null && param.equalsIgnoreCase("timeout")) {
					return Long.parseLong(value) * 1000; //valor de keep alive do servidor 
				}
			}
			return 5 * 50; //se não mantém conexão por 5 segundos 
		}
	};
	
	
	
}
