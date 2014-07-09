package de.greyworks.neikergn.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Observable;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import de.greyworks.neikergn.Statics;

public class HttpModule extends Observable {
	String content;
	int statusCode;
	String statusLine;
	HttpModule that = this;
	String encoding = "ISO-8859-1";

	public void httpGet(String uri) {
		Statics.ovr.setProgressBar(1);
		new HttpTask().execute(uri);
	}

	public void httpGet(String uri, String encoding) {
		this.encoding = encoding;
		httpGet(uri);
	}

	public String getContent() {
		return content;
	}

	public int getStatus() {
		if (statusCode != 200) {
			Statics.showToast("Netzwerkfehler. Status Code:" + statusLine);
			if (Statics.ERROR)
				Log.e(Statics.TAG, "HTTP ERROR - Code:" + statusLine);
		}
		return statusCode;
	}

	public boolean getSuccess() {
		return (getStatus() == 200);
	}

	private class HttpTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);

			try {
				HttpResponse response = httpClient.execute(httpGet);
				statusCode = response.getStatusLine().getStatusCode();
				statusLine = response.getStatusLine().toString();
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in, encoding));
				String line = "";
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(line);
				}
				content = sb.toString();
				reader.close();
				in.close();

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return content;
		}

		@Override
		protected void onPostExecute(String result) {
			Statics.ovr.setProgressBar(-1);
			that.setChanged();
			that.notifyObservers();
		}

	}

}
