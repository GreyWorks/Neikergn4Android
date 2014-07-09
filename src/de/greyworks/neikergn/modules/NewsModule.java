package de.greyworks.neikergn.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.NewsItem;

public class NewsModule extends Observable implements ContentModule<NewsItem>,
		Observer {
	public static final int MOD_ID = 1;
	private static String fileName = "news.json";
	private ArrayList<NewsItem> newsItems = new ArrayList<NewsItem>();
	private HttpModule http = new HttpModule();
	private Date lastUpdate = new Date(0);

	public NewsModule() {
	}

	private void saveToFile() {
		JSONObject newsObj = new JSONObject();
		JSONArray newsArr = new JSONArray();
		try {
			newsObj.put("lastUpdate", lastUpdate.getTime());
			for (int i = 0; i < newsItems.size(); i++) {
				NewsItem item = newsItems.get(i);
				newsArr.put(item.toJSON());
			}
			newsObj.put("items", newsArr);
			FileModule.saveFile(Statics.ctx, newsObj.toString(), fileName);
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Save News: JSON Error");
			e.printStackTrace();
		}
	}

	private void loadFromFile() {
		try {
			JSONObject newsObj = new JSONObject(FileModule.loadFile(
					Statics.ctx, fileName));
			lastUpdate.setTime(newsObj.getLong("lastUpdate"));
			JSONArray newsArr = newsObj.getJSONArray("items");
			for (int i = 0; i < newsArr.length(); i++) {
				JSONObject newsIt = newsArr.getJSONObject(i);
				NewsItem it = NewsItem.fromJSON(newsIt);
				if (!newsItems.contains(it)) {
					newsItems.add(it);
				}
			}
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Load News: JSON Error");
			e.printStackTrace();
		}

	}

	/**
	 * get or update news
	 */
	@Override
	public void updateContent() {
		File f = new File(Statics.ctx.getFilesDir().getAbsolutePath() + "/"
				+ fileName);
		if (f.exists()) {
			loadFromFile();
		}
		Long upDiff = new Date().getTime() - lastUpdate.getTime();
		if (upDiff > 1000 * 60 * 20) {
			forceUpdateWeb();
		} else {
			setChanged();
			notifyObservers();
			if (Statics.DEBUG) {
				Log.d(Statics.TAG, "News update skipped (deltaT < 20m = "
						+ (upDiff / 60 / 1000) + ")");
			}
		}
	}

	@Override
	public void forceUpdateWeb() {
		http.httpGet("http://www.neunkirchen-am-brand.de/app/pressem_liste.php");
		http.addObserver(this);
	}

	@Override
	public boolean hasItems() {
		return !newsItems.isEmpty();
	}

	@Override
	public ArrayList<NewsItem> getItems() {
		return newsItems;
	}

	@Override
	public NewsItem getById(int id) {
		for (int i = 0; i < newsItems.size(); i++) {
			NewsItem item = newsItems.get(i);
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void cleanUp() {
		Collections.sort(newsItems);
		int myAge;
		for (int i = newsItems.size() - 1; i > -1; i--) {
			myAge = newsItems.get(i).getAge();
			if (myAge > 14) {
				newsItems.remove(i);
			}
		}
	}

	/**
	 * observable update - items from http module
	 */
	@Override
	public void update(Observable observable, Object data) {
		http.deleteObserver(this);
		// if network error - take the easy way out
		if (!http.getSuccess()) {
			return;
		}
		try {
			JSONArray newsObjs = new JSONArray(http.getContent());
			for (int i = 0; i < newsObjs.length(); i++) {
				NewsItem item = NewsItem.fromWeb(newsObjs.getJSONObject(i));
				if (!newsItems.contains(item)) {
					newsItems.add(item);
				}
			}
			this.cleanUp();
			lastUpdate = new Date();
			saveToFile();
			setChanged();
			notifyObservers();
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "JSON Error in News Module");
			e.printStackTrace();
		}

	}
}
