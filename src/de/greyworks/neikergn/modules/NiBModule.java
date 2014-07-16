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
import de.greyworks.neikergn.containers.NiBItem;

public class NiBModule extends Observable implements ContentModule<NiBItem>,
		Observer {
	public static String fileName = "nib.json";
	private ArrayList<NiBItem> nibItems = new ArrayList<NiBItem>();
	HttpModule http = new HttpModule();
	Date lastUpdate = new Date(0);

	public NiBModule() {
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
				Log.d(Statics.TAG, "NiB update skipped (deltaT < 24h = "
						+ (upDiff / 24 / 60 / 60 / 1000) + ")");
			}
		}
	}

	public void forceUpdateWeb() {
		http.httpGet("http://www.neunkirchen-am-brand.de/app/nib_liste.php");
		http.addObserver(this);
	}

	@Override
	public boolean hasItems() {
		return !nibItems.isEmpty();
	}

	@Override
	public ArrayList<NiBItem> getItems() {
		return nibItems;
	}

	@Override
	public NiBItem getById(int id) {
		for (int i = 0; i < nibItems.size(); i++) {
			NiBItem item = nibItems.get(i);
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void cleanUp() {
		Collections.sort(nibItems);
		for (int i = nibItems.size() - 1; i > -1; i--) {
			NiBItem item = nibItems.get(i);
			if (item.getAge() > 90) {
				if(item.getLocalFile().exists()) {
					item.getLocalFile().delete();
				}
				nibItems.remove(i);
			}
		}
	}

	private void saveToFile() {
		JSONObject nibObj = new JSONObject();
		JSONArray nibArr = new JSONArray();
		try {
			nibObj.put("lastUpdate", lastUpdate.getTime());
			for (int i = 0; i < nibItems.size(); i++) {
				NiBItem item = nibItems.get(i);
				nibArr.put(item.toJSON());
			}
			nibObj.put("items", nibArr);
			FileModule.saveFile(Statics.ctx, nibObj.toString(), fileName);
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Save NiB: JSON Error");
			e.printStackTrace();
		}
	}

	private void loadFromFile() {
		try {
			JSONObject nibObj = new JSONObject(FileModule.loadFile(Statics.ctx,
					fileName));
			lastUpdate.setTime(nibObj.getLong("lastUpdate"));
			JSONArray nibArr = nibObj.getJSONArray("items");
			for (int i = 0; i < nibArr.length(); i++) {
				JSONObject msgIt = nibArr.getJSONObject(i);
				NiBItem it = NiBItem.fromJSON(msgIt);
				if (!nibItems.contains(it)) {
					nibItems.add(it);
				}
			}
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "Load NiB: JSON Error");
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
			JSONArray nibObjs = new JSONArray(http.getContent());
			for (int i = 0; i < nibObjs.length(); i++) {
				NiBItem item = NiBItem.fromWeb(nibObjs.getJSONObject(i));
				if (!nibItems.contains(item) && item.isValid()) {
					nibItems.add(item);
				}
			}
			this.cleanUp();
			lastUpdate = new Date();
			saveToFile();
			setChanged();
			notifyObservers();
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "JSON Error in NiB Module");
			e.printStackTrace();
		}
	}

}
