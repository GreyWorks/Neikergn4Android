package de.greyworks.neikergn.containers;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.greyworks.neikergn.Statics;

public class NiBItem implements Comparable<NiBItem>{
	private int id;
	private String title = "";
	private String picture = "";
	private String text = "";
	private Date date = new Date();

	private boolean valid = false;

	public NiBItem() {

	}

	public boolean isValid() {
		return this.valid;
	}

	public int getId() {
		return this.id;
	}

	public String getTitle() {
		if (valid) {
			return this.title;
		} else {
			return "Invalid.";
		}
	}
	
	public String getText() {
		return this.text;
	}

	public String getPicture() {
		return this.picture;
	}

	public String getDate() {
		return Statics.dateFormatOut.format(this.date);
	}
	
	public int getAge() {
		long diff = new Date().getTime() - this.date.getTime();
		return (int)(diff / 1000 / 60 / 60 / 24);
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("i", this.id);
			json.put("t", this.title);
			json.put("m", this.text);
			json.put("f", this.picture);
			json.put("d", this.date.getTime());
		} catch (JSONException e) {
			Log.e(Statics.TAG, "NiBItem to JSON Fail");
			e.printStackTrace();
		}
		return json;
	};

	public static NiBItem fromJSON(JSONObject json) {
		NiBItem item = new NiBItem();
		try {
			item.id = json.getInt("i");
			item.title = json.getString("t");
			item.text = json.getString("m");
			item.picture = json.getString("f");
			Long d = json.getLong("d");
			item.date = new Date(d);
			item.valid = true;
		} catch (JSONException e) {
			Log.e(Statics.TAG, "NiBItem from JSON Fail");
			e.printStackTrace();
		}
		return item;
	}
	
	public static NiBItem fromWeb(JSONObject obj) {
		NiBItem item = new NiBItem();
		try {
			item.id = obj.getInt("i");
			item.title = obj.getString("t");
			item.text = obj.getString("m");
			item.picture = obj.getString("f");
			String d = obj.getString("d");
			item.date = Statics.dateFormatIn.parse(d);
			item.valid = true;
		} catch (JSONException e) {
			Log.e(Statics.TAG, "NiBItem from web date parsing fail");
			e.printStackTrace();
		}  catch (ParseException e) {
			Log.e(Statics.TAG, "NiBItem from web date parsing fail");
			e.printStackTrace();
		}
		return item;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof NiBItem) {
			NiBItem item = (NiBItem) o;
			if (this.id == item.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(NiBItem another) {
		return another.id - this.id;
	}



}
