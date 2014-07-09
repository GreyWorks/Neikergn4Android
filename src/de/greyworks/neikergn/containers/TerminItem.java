package de.greyworks.neikergn.containers;

import java.text.ParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.greyworks.neikergn.Statics;

public class TerminItem implements Comparable<TerminItem> {
	private int id;
	private String title = "";
	private String ort = "";
	private Date start = null;
	private Date end = null;
	private boolean more = false;

	private String infoCache = "";

	private boolean valid = false;

	public TerminItem() {

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

	public String getOrt() {
		return this.ort;
	}

	public String getDate() {
		return Statics.dateFormatOutFull.format(this.start);
	}

	public String getEnd() {
		return Statics.dateFormatOutFull.format(this.end);
	}

	public String getDateInfo() {
		if (this.end.getTime() - this.start.getTime() < 5) {
			return "Am " + this.getDate();
		} else {
			return "Von " + this.getDate() + " bis " + this.getEnd();
		}
	}

	public String getInfo() {
		if (infoCache.isEmpty()) {
			StringBuilder info = new StringBuilder();
			info.append(getDateInfo());
			if (getAge() > 0) {
				info.append("\n"
						+ ((getAge() < 2) ? "Gestern"
								: ("Vor " + getAge() + " Tagen")) + "\n");
			} else if (getAge() > -1) {
				info.append("\nHeute\n");
			} else if (getAge() > -2) {
				info.append("\nMorgen\n");
			} else if (getAge() > -8) {
				info.append("\nIn " + (-getAge()) + " Tagen\n");
			} else {
				info.append("\nIn " + (-getAge()) + " Tagen\n");
			}
			info.append(getOrt());
			infoCache = info.toString();
		}
		return infoCache;
	}

	public int getAge() {
		float devider = 1000 * 60 * 60 * 24;
		int diff = (int) (new Date().getTime() / devider)
				- (int) (this.start.getTime() / devider);
		return diff;
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("i", this.id);
			json.put("t", this.title);
			json.put("o", this.ort);
			json.put("s", this.start.getTime());
			json.put("e", this.end.getTime());
			json.put("m", this.more);
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "TerminItem to JSON fail");
			e.printStackTrace();
		}
		return json;
	};

	public static TerminItem fromJSON(JSONObject json) {
		TerminItem item = new TerminItem();
		try {
			item.id = json.getInt("i");
			item.title = json.getString("t");
			item.ort = json.getString("o");
			Long d = json.getLong("s");
			item.start = new Date(d);
			d = json.getLong("e");
			item.end = new Date(d);
			item.more = json.getBoolean("m");
			item.valid = true;
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "TerminItem from JSON fail");
			e.printStackTrace();
		}
		return item;
	}

	public static TerminItem fromWeb(JSONObject obj) {
		TerminItem item = new TerminItem();
		try {
			item.id = obj.getInt("i");
			item.title = obj.getString("t");
			item.ort = obj.getString("o");
			item.start = Statics.dateFormatInFull.parse(obj.getString("d")
					+ " " + obj.getString("b"));
			item.end = Statics.dateFormatInFull.parse(obj.getString("d") + " "
					+ obj.getString("e"));
			item.more = obj.getString("m").equals("Y");
			item.valid = true;
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "TerminItem from web JSON fail");
			e.printStackTrace();
		} catch (ParseException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "TerminItem from web date parsing fail");
			e.printStackTrace();
		}
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TerminItem) {
			TerminItem item = (TerminItem) o;
			if (this.id == item.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(TerminItem another) {
		Long diff = another.start.getTime() - this.start.getTime();
		if (diff < 0) {
			return -1;
		} else if (diff == 0) {
			return 0;
		} else {
			return 1;
		}
	}

}
