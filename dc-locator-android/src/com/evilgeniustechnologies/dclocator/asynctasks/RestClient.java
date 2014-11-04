package com.evilgeniustechnologies.dclocator.asynctasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.http.AndroidHttpClient;
import android.os.StrictMode;
import android.util.Log;

public class RestClient {
	 
	private static final String TAG = "RestClient";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
 
	/** Methods supported by RestClient */
	public enum RequestMethod {
		GET, POST
	}
 
	/** URL of the request */
	private URL mUrl = null;
 
	/** Request mHeaders */
	private ArrayList<Header> mHeaders = null;
 
	/** Request mParameters */
	private ArrayList<NameValuePair> mParameters = null;
 
	/** HTTP client that will launch the request */
	private AndroidHttpClient mHttpClient = null;
 
	/** Request to be send */
	private HttpPost mHttpRequest = null;
 
	/** Response received from the server */
	private org.apache.http.HttpResponse mHttpResponse = null;
 
	private UsernamePasswordCredentials mCredentials = null;
 
	/**
	 * Creates a new instance of the REST client
	 * 
	 * @param mUrl
	 * @throws MalformedURLException
	 */
	public RestClient(String url) throws MalformedURLException {
		mUrl = new URL(url);
		mHeaders = new ArrayList<Header>();
		mParameters = new ArrayList<NameValuePair>();
		mHttpClient = AndroidHttpClient.newInstance(USER_AGENT);
	}
 
	/**
	 * Add a header to the request
	 * 
	 * @param header
	 *            Header to add
	 */
	public void addHeader(Header header) {
		mHeaders.add(header);
	}
 
	/**
	 * Execute an HTTP request
	 * 
	 * @param request
	 *            Request to be executed
	 */
	private void executeRequest() {
 
		try {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
 
			mHttpResponse = mHttpClient.execute(mHttpRequest);
			int s= mHttpResponse.getStatusLine().getStatusCode();
			mHttpResponse.getStatusLine().getReasonPhrase();
			Log.d("reason", mHttpResponse.getStatusLine().getReasonPhrase());//
			Log.d("responesi", String.valueOf(s));
		
		
		} catch (ClientProtocolException e) {
 
			Log.e(TAG, "Error in HTTP protocol");
			e.printStackTrace();
			mHttpRequest.abort();
			mHttpResponse = null;
 
		} catch (IOException e) {
 
			Log.e(TAG, "I/O Error");
			e.printStackTrace();
			mHttpRequest.abort();
			mHttpResponse = null;
 
		} finally {
			// shutdown connection to ensure deallocation of all system
			// resources
			mHttpClient.close();
		}
	}
 
		public void execute(RequestMethod method) throws NoSuchMethodException,
			URISyntaxException {
		execute(method, null, null);
	}
 
	/**
	 * Create a full HTTP request adding mParameters and mHeaders. And finally
	 * execute the request calling executeRequest
	 * 
	 * @param method
	 *            method to be execute
	 * @throws NoSuchMethodException
	 *             throw when Request method does not exist
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 */
	public void execute(RequestMethod method, String user, String pass)
			throws NoSuchMethodException, URISyntaxException {
 
		mHttpRequest = null;
 
		switch (method) {
 
		// URL?param1=value&...&paramN=value
		case GET: {
			String params = "";
 
			if (!mParameters.isEmpty()) {
				params = "?";
 
				int i;
				try {
					for (i = 0; i < mParameters.size() - 1; i++) {
						params += mParameters.get(i).getName()
								+ "="
								+ URLEncoder.encode(mParameters.get(i)
										.getValue(), "UTF-8") + "&";
					}
 
					params += mParameters.get(i).getName()
							+ "="
							+ URLEncoder.encode(mParameters.get(i).getValue(),
									"UTF-8");
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "Unsupported encoding, check if UTF-8 is used");
					e.printStackTrace();
				}
			}
 
		//	mHttpRequest = new HttpGet(mUrl.toURI() + params);
 
		}
			break;
 
		case POST: {
			mHttpRequest = new HttpPost(mUrl.toURI());
			mHttpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded" );

			if (!mParameters.isEmpty()) {
				UrlEncodedFormEntity encodedParams = null;
				try {
					encodedParams = new UrlEncodedFormEntity(mParameters,
							HTTP.UTF_8);
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "POST unsupported encoding");
					e.printStackTrace();
					return;
				}

				mHttpRequest.setEntity(encodedParams);
		
			}
		}
			break;
 
		default:
			throw new NoSuchMethodException();
		}
 
		if (user != null && pass != null) {
			mCredentials = new UsernamePasswordCredentials(user, pass);
 
			try {
				Header header = new BasicScheme().authenticate(mCredentials,
						mHttpRequest);
				mHeaders.add(header);
			} catch (AuthenticationException e) {
				Log.e(TAG, "Authentication header error");
				e.printStackTrace();
			}
		}
 
		for (Header header : mHeaders) {
			mHttpRequest.addHeader(header);
		}
 
		executeRequest();
	}
 
	public void addParam(String param, String value) {
		mParameters
				.add(new BasicNameValuePair(param,value));
	}
 
	public int getResponse() {
		int s= mHttpResponse.getStatusLine().getStatusCode();
		mHttpResponse.getStatusLine().getReasonPhrase();
		return s;
	}
}
