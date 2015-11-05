package eu.pericles.processcompiler.unittests;

import static org.junit.Assert.fail;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;

import org.junit.Test;

public class ERMRRequestsTests {
	
	final String ermr = "https://141.5.102.86/api/triple";

	@Test
	public void test() {
		String repo = "testJerome";

		try {	
			Client client = ClientBuilder.newBuilder()
					.hostnameVerifier(getAllTrustHostnameVerifier())
					.sslContext(getAllTrustSSLContext())
					.build();
			
			Builder builder = client.target(ermr).path(repo).request();
			
			String response = builder.get(String.class);
			System.out.println("Response: " + response);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	private HostnameVerifier getAllTrustHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		return hostnameVerifier;
	}

	private SSLContext getAllTrustSSLContext() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, getAllTrustManager(), new SecureRandom());
		return sslContext;
	}
	
	private TrustManager[] getAllTrustManager() {
		TrustManager[] trustAllManager = new TrustManager[] {
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() { return null; }
					public void checkClientTrusted(X509Certificate[] certs, String authType) { }
					public void checkServerTrusted(X509Certificate[] certs, String authType) { }
					}
				};
		return trustAllManager;
	}

}
