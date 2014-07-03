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
import de.greyworks.neikergn.Statics;

public class HttpModule extends Observable {
	String content;
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
	
	private class HttpTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			String page = "";

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			
			try {
				HttpResponse response = httpClient.execute(httpGet);
				page = response.toString();
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, encoding));
				String line = "";
				StringBuilder sb = new StringBuilder();
				while((line = reader.readLine()) != null) {
					if(sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(line);
				}
				page = sb.toString();
				reader.close();
				in.close();
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return page;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Statics.ovr.setProgressBar(-1);
			that.content = result;
			that.setChanged();
			that.notifyObservers();
		}
		
	}

}
