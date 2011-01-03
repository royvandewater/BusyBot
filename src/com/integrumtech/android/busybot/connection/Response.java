package com.integrumtech.android.busybot.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;

import com.integrumtech.android.busybot.Integrum;

import android.util.Log;


public class Response {

	private HttpResponse response;
	private String body;

	/**
	 * Instantiates an instance of Response, a wrapper for HttpResponse
	 * @param httpResponse instance of HttpResponse
	 */
	public Response(HttpResponse httpResponse) {
		this.response = httpResponse;
	}

	/**
	 * Returns the HTTP status code of the response
	 * @return integer status code
	 */
	public int getStatusCode() {
		return response.getStatusLine().getStatusCode();
	}

	/**
	 * Converts the HttpRequest content into a string, caches it for future retrieval
	 * and returns it. If the cached response body is already present, it returns
	 * that instead
	 * @return body of the HTTP request in string form
	 */
	public String getBody() {
		if(body == null) {
			try {
				body = streamToString(response.getEntity().getContent());
				return body;
			} catch (IllegalStateException e) {
				Log.e(Integrum.TAG, e.getStackTrace().toString());
				return "";
			} catch (IOException e) {
				Log.e(Integrum.TAG, e.getStackTrace().toString());
				return "";
			}
		} else {
			return body;
		}
	}

	public static String streamToString(InputStream is) throws IOException {
		/*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
	}
}
