package me.recruit.recruitme;

import android.os.Looper;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class HTTPUrlConnection {

	public static void sendJson(final JSONObject json, final String URL) {
		Thread t = new Thread() {

			public void run() {
				Looper.prepare(); //For Preparing Message Pool for the child Thread
				HttpClient client = new DefaultHttpClient();
				HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
				HttpResponse response;

				try {
					HttpPost post = new HttpPost(URL);

					StringEntity se = new StringEntity( json.toString());
					se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

					post.setEntity(se);
					response = client.execute(post);

					// Check response
					if (response != null){
						Log.d("HTTP_STATUS", String.valueOf(response.getStatusLine()));
						InputStream in = response.getEntity().getContent(); //Get the data in the entity
						String result = convertInputStreamToString(in);
						Log.d("HTTP_RESULT", result);
					}

				} catch(Exception e) {
					e.printStackTrace();
					Log.d("HTTP_URL_Error", "Cannot Estabilish Connection");
				}

				Looper.loop(); //Loop in the message queue
			}
		};

		t.start();
	}

	private static String convertInputStreamToString(InputStream inputStream) throws IOException {
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}


}
