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
import de.greyworks.neikergn.containers.MitteilungsblattItem;

public class MitteilungsblattModule extends Observable implements
		ContentModule<MitteilungsblattItem>, Observer {
	public static String fileName = "mitteilungsblatt.json";
	private ArrayList<MitteilungsblattItem> mitteilungsblattItems = new ArrayList<MitteilungsblattItem>();
	HttpModule http = new HttpModule();
	Date lastUpdate = new Date(0);

	public MitteilungsblattModule() {
	}

	/**
	 * get or update news
	 */
	public void updateContent() {
		File f = new File(Statics.ctx.getFilesDir().getAbsolutePath() + "/"
				+ fileName);
		if (f.exists()) {
			loadFromFile();
		}
		Long upDiff = new Date().getTime() - lastUpdate.getTime();
		if (upDiff > 1000 * 60 * 60 * 24) {
			forceUpdateWeb();
		} else {
			if (Statics.DEBUG) {
				Log.d(Statics.TAG,
						"Mitteilungsblatt update skipped (deltaT < 24h = "
								+ (upDiff / 24 / 60 / 60 / 1000) + ")");
			}
		}
	}

	public void forceUpdateWeb() {
		// this.mitteilungsblattItems.clear();
		http.addObserver(this);
		http.httpGet("http://www.neunkirchen-am-brand.de/app/mblatt_liste.php");
	}

	private void saveToFile() {
		JSONObject mObj = new JSONObject();
		JSONArray mArr = new JSONArray();
		try {
			mObj.put("lastUpdate", lastUpdate.getTime());
			for (int i = 0; i < mitteilungsblattItems.size(); i++) {
				MitteilungsblattItem item = mitteilungsblattItems.get(i);
				mArr.put(item.toJSON());
			}
			mObj.put("items", mArr);
			FileModule.saveFile(Statics.ctx, mObj.toString(), fileName);
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Save Mitteilungsblatt: JSON Error");
			e.printStackTrace();
		}
	}

	private void loadFromFile() {
		try {
			JSONObject msgObj = new JSONObject(FileModule.loadFile(Statics.ctx,
					fileName));
			lastUpdate.setTime(msgObj.getLong("lastUpdate"));
			JSONArray newsArr = msgObj.getJSONArray("items");
			for (int i = 0; i < newsArr.length(); i++) {
				JSONObject msgIt = newsArr.getJSONObject(i);
				MitteilungsblattItem it = MitteilungsblattItem.fromJSON(msgIt);
				if (!mitteilungsblattItems.contains(it)) {
					mitteilungsblattItems.add(it);
				}
			}
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Load Mitteilungsblatt: JSON Error");
			e.printStackTrace();
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
			JSONArray mBlattObjs = new JSONArray(http.getContent());
			for (int i = 0; i < mBlattObjs.length(); i++) {
				MitteilungsblattItem item = MitteilungsblattItem
						.fromWeb(mBlattObjs.getJSONObject(i));
				if (!mitteilungsblattItems.contains(item)) {
					mitteilungsblattItems.add(item);
				}
			}
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "JSON Error in Mitteilungsblatt Module");
			e.printStackTrace();
		}
		this.cleanUp();
		lastUpdate = new Date();
		saveToFile();
		setChanged();
		notifyObservers();
	}

	@Override
	public boolean hasItems() {
		return !mitteilungsblattItems.isEmpty();
	}

	@Override
	public ArrayList<MitteilungsblattItem> getItems() {
		return mitteilungsblattItems;
	}

	@Override
	public MitteilungsblattItem getById(int id) {
		for (int i = 0; i < mitteilungsblattItems.size(); i++) {
			MitteilungsblattItem item = mitteilungsblattItems.get(i);
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void cleanUp() {
		Collections.sort(mitteilungsblattItems);
		while (mitteilungsblattItems.size() > 24) {
			MitteilungsblattItem item = mitteilungsblattItems
					.get(mitteilungsblattItems.size() - 1);
			if (item.getLocalFile().exists()) {
				item.getLocalFile().delete();
			}
			mitteilungsblattItems.remove(item);
		}
	}

}
