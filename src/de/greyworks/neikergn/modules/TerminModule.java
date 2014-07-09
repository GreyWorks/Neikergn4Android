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
import de.greyworks.neikergn.containers.TerminItem;

public class TerminModule extends Observable implements
		ContentModule<TerminItem>, Observer {
	public static String fileName = "termine.json";
	private ArrayList<TerminItem> terminItems = new ArrayList<TerminItem>();
	HttpModule http = new HttpModule();
	Date lastUpdate = new Date(0);

	public TerminModule() {
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
			setChanged();
			notifyObservers();
			if (Statics.DEBUG) {
				Log.d(Statics.TAG, "Termin update skipped (deltaT < 24h = "
						+ (upDiff / 24 / 60 / 60 / 1000) + ")");
			}
		}
	}

	public void forceUpdateWeb() {
		http.httpGet("http://www.neunkirchen-am-brand.de/app/termine_liste.php");
		http.addObserver(this);
	}

	@Override
	public boolean hasItems() {
		return !terminItems.isEmpty();
	}

	@Override
	public ArrayList<TerminItem> getItems() {
		return terminItems;
	}

	@Override
	public TerminItem getById(int id) {
		for (int i = 0; i < terminItems.size(); i++) {
			TerminItem item = terminItems.get(i);
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	public int getIdFirstToday() {
		float j;
		for (int i = 0; i < terminItems.size(); i++) {
			j = terminItems.get(i).getAge();
			if (j < 0) {
				return i;
			}
		}
		return -1;

	}

	@Override
	public void cleanUp() {
		// no iterator to prevent concurrency problems
		// caused by removing iterating item
		int myAge;
		for (int i = terminItems.size() - 1; i > -1; i--) {
			myAge = terminItems.get(i).getAge();
			if (myAge > 7 || myAge < -31) {
				terminItems.remove(i);
			}
		}
		Collections.sort(terminItems, Collections.reverseOrder());
	}

	private void saveToFile() {
		JSONObject terminObj = new JSONObject();
		JSONArray terminArr = new JSONArray();
		try {
			terminObj.put("lastUpdate", lastUpdate.getTime());
			for (int i = 0; i < terminItems.size(); i++) {
				TerminItem item = terminItems.get(i);
				terminArr.put(item.toJSON());
			}
			terminObj.put("items", terminArr);
			FileModule.saveFile(Statics.ctx, terminObj.toString(), fileName);
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Save Termine: JSON Error");
			e.printStackTrace();
		}
	}

	private void loadFromFile() {
		try {
			JSONObject terminObj = new JSONObject(FileModule.loadFile(
					Statics.ctx, fileName));
			lastUpdate.setTime(terminObj.getLong("lastUpdate"));
			JSONArray terminArr = terminObj.getJSONArray("items");
			for (int i = 0; i < terminArr.length(); i++) {
				JSONObject terminIt = terminArr.getJSONObject(i);
				TerminItem it = TerminItem.fromJSON(terminIt);
				if (!terminItems.contains(it)) {
					terminItems.add(it);
				}
			}
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Load Termine: JSON Error");
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
			JSONArray terminObjs = new JSONArray(http.getContent());
			for (int i = 0; i < terminObjs.length(); i++) {
				TerminItem item = TerminItem.fromWeb(terminObjs
						.getJSONObject(i));
				// no items more than 31 days in the future
				if (!terminItems.contains(item) && item.getAge() > -30) {
					terminItems.add(item);
				}
			}
			this.cleanUp();
			lastUpdate = new Date();
			saveToFile();
			setChanged();
			notifyObservers();
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "JSON Error in Termin Module");
			e.printStackTrace();
		}
	}

}
