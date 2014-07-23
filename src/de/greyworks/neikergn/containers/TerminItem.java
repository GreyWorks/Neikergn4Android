package de.greyworks.neikergn.containers;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;
import android.util.Log;
import android.widget.CalendarView;
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

	public Date getDate() {
		return this.start;
	}

	public String getDateString() {
		return Statics.dateFormatOutFull.format(this.start);
	}

	public String getEndString() {
		return Statics.dateFormatOutFull.format(this.end);
	}

	public String getInfo() {
		if (infoCache.isEmpty()) {
			Date now = new Date();
			int flags = DateUtils.FORMAT_SHOW_DATE
					| DateUtils.FORMAT_SHOW_WEEKDAY
					| DateUtils.FORMAT_ABBREV_WEEKDAY;
			// show time only if not starting at midnight
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			if (cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) > 0) {
				flags = flags | DateUtils.FORMAT_SHOW_TIME;
			}
			// if within a week, show "in x days" or "x days ago"
			if (Math.abs(now.getTime() - start.getTime()) < DateUtils.WEEK_IN_MILLIS) {
				infoCache = (String) DateUtils.getRelativeTimeSpanString(
						this.start.getTime(), now.getTime(),
						DateUtils.DAY_IN_MILLIS, 0)
						+ "\n";
			}
			infoCache += DateUtils.formatDateRange(Statics.ctx,
					this.start.getTime(), this.end.getTime(), flags);
		}
		return infoCache;
	}

	public int getAge() {
		long msInDay = 1000 * 60 * 60 * 24;
		long time = new Date().getTime();
		long msGoneInDay = time % msInDay;
		long diff = (time - msGoneInDay) - this.start.getTime();

		return (int) Math.ceil(diff / msInDay);
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
			// prevent problems when end is earlier than start
			if (item.start.getTime() > item.end.getTime()) {
				item.end = item.start;
			}
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
		long diff = another.start.getTime() - this.start.getTime();
		if (diff < 0) {
			return -1;
		} else if (diff == 0) {
			return 0;
		} else {
			return 1;
		}
	}

}
