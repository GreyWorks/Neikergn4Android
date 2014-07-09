package de.greyworks.neikergn.containers;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import de.greyworks.neikergn.Statics;

public class NewsItem implements Comparable<NewsItem> {
	private int id;
	private String title = "";
	private String preview = "";
	private int source = 0;
	private Date date = null;

	private static String[] srcShort = { "EN", "FT", "NN", "EN / NN", "NO" };
	private static String[] srcLong = { "Erlanger Nachrichten",
			"Fränkischer Tag", "Nürnberger Nachrichten",
			"Erlanger Nachrichten / Nürnberger Nachrichten", "neikergn online" };

	private boolean valid = false;

	public NewsItem() {

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

	public String getSource() {
		if (this.source < srcShort.length) {
			return srcShort[this.source];
		} else {
			return "N/A";
		}
	}

	public String getSourceLong() {
		if (this.source < srcShort.length) {
			return srcLong[this.source];
		} else {
			return "Unbekannte Quelle";
		}
	}

	public String getDate() {
		return Statics.dateFormatOut.format(this.date);
	}

	public int getAge() {
		long diff = new Date().getTime() - this.date.getTime();
		return (int) (diff / 1000 / 60 / 60 / 24);
	}

	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("i", this.id);
			json.put("t", this.title);
			json.put("p", this.preview);
			json.put("z", this.source);
			json.put("d", this.date.getTime());
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "NewsItem to JSON fail");
			e.printStackTrace();
		}
		return json;
	};

	public static NewsItem fromJSON(JSONObject json) {
		NewsItem item = new NewsItem();
		try {
			item.id = json.getInt("i");
			item.title = json.getString("t");
			item.preview = json.getString("p");
			item.source = json.getInt("z");
			Long d = json.getLong("d");
			item.date = new Date(d);
			item.valid = true;
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "NewsItem from JSON fail");
			e.printStackTrace();
		}
		return item;
	}

	public static NewsItem fromWeb(JSONObject obj) {
		NewsItem item = new NewsItem();
		try {
			item.id = obj.getInt("i");
			String d = obj.getString("d");
			item.date = Statics.dateFormatIn.parse(d);
			item.title = obj.getString("t");
			item.source = obj.getInt("z");
			item.valid = true;
		} catch (JSONException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "NewsItem from web JSON fail");
			e.printStackTrace();
		} catch (ParseException e) {
			if (Statics.ERROR)
				Log.e(Statics.TAG, "NewsItem from web date parsing fail");
			e.printStackTrace();
		}
		return item;
	}

	@Deprecated
	public void fromHTML(String html) {
		// parse TR String from html
		Pattern p = Pattern.compile("<td.*?>(.*?)</td>");
		Matcher m = p.matcher(html);
		if (m.find()) {
			// this.date = m.group(1);
			if (m.find()) {
				// this.source = m.group(1);
				if (m.find()) {
					this.title = m.group(1);
					if (m.find()) {
						this.id = Integer.parseInt(m.group(1).split("\"")[1]
								.split("\\/")[4]);
						// everything set. Validate
						valid = true;
					}
				}
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof NewsItem) {
			NewsItem item = (NewsItem) o;
			if (this.id == item.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(NewsItem another) {
		return another.id - this.id;
	}

}
