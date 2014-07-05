package de.greyworks.neikergn.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.MessageItem;

public class MessageModule extends Observable implements
		ContentModule<MessageItem>, Observer {
	public static String fileName = "messages.json";
	private ArrayList<MessageItem> messageItems = new ArrayList<MessageItem>();
	HttpModule http = new HttpModule();
	Date lastUpdate = new Date(0);

	public MessageModule() {
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
			Log.d(Statics.TAG, "Message update skipped (deltaT < 24h = "
					+ (upDiff / 24 / 60 / 60 / 1000) + ")");
		}
	}

	public void forceUpdateWeb() {
		http.httpGet("http://www.neunkirchen-am-brand.de/app/aktuelles_liste.php");
		http.addObserver(this);
	}

	@Override
	public boolean hasItems() {
		return !messageItems.isEmpty();
	}

	@Override
	public ArrayList<MessageItem> getItems() {
		return messageItems;
	}

	@Override
	public MessageItem getById(int id) {
		for (int i = 0; i < messageItems.size(); i++) {
			MessageItem item = messageItems.get(i);
			if (item.getId() == id) {
				return item;
			}
		}
		return null;
	}

	@Override
	public void cleanUp() {
		for (Iterator<MessageItem> iterator = messageItems.iterator(); iterator
				.hasNext();) {
			MessageItem item = (MessageItem) iterator.next();
			if (item.getAge() > 30) {
				messageItems.remove(item);
			}
		}
		Collections.sort(messageItems);
	}

	private void saveToFile() {
		JSONObject msgObj = new JSONObject();
		JSONArray msgArr = new JSONArray();
		try {
			msgObj.put("lastUpdate", lastUpdate.getTime());
			for (int i = 0; i < messageItems.size(); i++) {
				MessageItem item = messageItems.get(i);
				msgArr.put(item.toJSON());
			}
			msgObj.put("items", msgArr);
			FileModule.saveFile(Statics.ctx, msgObj.toString(), fileName);
		} catch (JSONException e) {
			Log.e(Statics.TAG, "Save Messages: JSON Error");
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
				MessageItem it = MessageItem.fromJSON(msgIt);
				if (!messageItems.contains(it)) {
					messageItems.add(it);
				}
			}
		} catch (JSONException e) {
			Log.e(Statics.TAG, "Load Messages: JSON Error");
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
			JSONArray msgObjs = new JSONArray(http.getContent());
			for (int i = 0; i < msgObjs.length(); i++) {
				MessageItem item = MessageItem
						.fromWeb(msgObjs.getJSONObject(i));
				if (!messageItems.contains(item)) {
					messageItems.add(item);
				}
			}
			this.cleanUp();
			lastUpdate = new Date();
			saveToFile();
			setChanged();
			notifyObservers();
		} catch (JSONException e) {
			Log.e(Statics.TAG, "JSON Error in News Module");
			e.printStackTrace();
		}
	}

}
