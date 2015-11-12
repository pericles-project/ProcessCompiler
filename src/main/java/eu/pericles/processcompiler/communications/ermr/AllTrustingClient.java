package eu.pericles.processcompiler.communications.ermr;

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

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class AllTrustingClient {
	
	static Client client;
	
	/**
	 * Returns a Client instance for a RESTful API that disables the certificate
	 * validation in Java SSL connections with self-signed servers. The client
	 * is built through a ClientBuilder with an all-trusting hostname verifier
	 * and an all-trusting trust manager
	 * 
	 * @return a Client that trusts all host names and credentials in SSL
	 *         connections
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	public static Client getAllTrustingClient() throws KeyManagementException, NoSuchAlgorithmException {
		client = ClientBuilder.newBuilder()
				.hostnameVerifier(getAllTrustingHostnameVerifier())
				.sslContext(getAllTrustingSSLContext())
				.build();
		return client;
	}

	public static Client getAllTrustingClientWithCredentials(String user, String password) throws KeyManagementException, NoSuchAlgorithmException {
		return getAllTrustingClient().register(HttpAuthenticationFeature.basic(user, password));
	}

	/**
	 * Returns a host name verifier that trust all host names by returning
	 * always true when calling the function verify(String hostname, SSLSession
	 * session)
	 * 
	 * @return a HostnameVerifier trusting all host names
	 */
	private static HostnameVerifier getAllTrustingHostnameVerifier() {
		HostnameVerifier hostnameVerifier = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		return hostnameVerifier;
	}

	/**
	 * Returns a SSL context where all the credentials are trusted by creating a
	 * trust manager that does not validate certificate chains
	 * 
	 * @return a SSLContext where the TrustManager trusts everything
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 */
	private static SSLContext getAllTrustingSSLContext() throws KeyManagementException, NoSuchAlgorithmException {
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, getAllTrustingTrustManager(), new SecureRandom());
		return sslContext;
	}

	/**
	 * Returns a trust manager that does not validate certificate chains
	 * 
	 * @return a TrustManager that trusts all credentials
	 */
	private static TrustManager[] getAllTrustingTrustManager() {
		TrustManager[] allTrustManager = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };
		return allTrustManager;
	}

}
