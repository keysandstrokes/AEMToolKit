package com.greyarea.aemtoolkit.servlets;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;



public class RequestHandler {

	public static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
	private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean verifyResponse(String recaptchaResponse,String secretKey)  {
		if (recaptchaResponse == null || "".equals(recaptchaResponse) || "".equals(secretKey) || secretKey == null) {
			return false;
		}		
		try{
		
		URL obj = new URL(SITE_VERIFY_URL);
		HttpsURLConnection httpConnection = (HttpsURLConnection) obj.openConnection();
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("User-Agent", USER_AGENT);
		String postParams = "secret=" + secretKey + "&response=" + recaptchaResponse;


		httpConnection.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(new InputStreamReader(
				httpConnection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		org.apache.sling.commons.json.JSONObject jsonObject = new org.apache.sling.commons.json.JSONObject(response.toString());
		return jsonObject.getBoolean("success");
	
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
