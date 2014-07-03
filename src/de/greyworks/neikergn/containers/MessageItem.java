package de.greyworks.neikergn.containers;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import de.greyworks.neikergn.Statics;
import android.util.Log;

public class MessageItem implements Comparable<MessageItem>{
	private int id;
	private String title = "";
	private String picture = "";
	private String preview = "";
	private Date date = new Date();

	private boolean valid = false;

	public MessageItem() {

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
	
	public String getPreview() {
		return this.preview;
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
			json.put("k", this.preview);
			json.put("l", this.picture);
			json.put("d", this.date.getTime());
		} catch (JSONException e) {
			Log.e(Statics.TAG, "NewsItem to JSON Fail");
			e.printStackTrace();
		}
		return json;
	};

	public static MessageItem fromJSON(JSONObject json) {
		MessageItem item = new MessageItem();
		try {
			item.id = json.getInt("i");
			item.title = json.getString("t");
			item.preview = json.getString("k");
			item.picture = json.getString("l");
			Long d = json.getLong("d");
			item.date = new Date(d);
			item.valid = true;
		} catch (JSONException e) {
			Log.e(Statics.TAG, "NewsItem from JSON Fail");
			e.printStackTrace();
		}
		return item;
	}
	
	public static MessageItem fromWeb(JSONObject obj) {
		MessageItem item = new MessageItem();
		try {
			item.id = obj.getInt("i");
			item.title = obj.getString("t");
			item.preview = obj.getString("k");
			item.picture = obj.getString("l");
			String d = obj.getString("d");
			item.date = Statics.dateFormatIn.parse(d);
			item.valid = true;
		} catch (JSONException e) {
			Log.e(Statics.TAG, "MessageItem from web date parsing fail");
			e.printStackTrace();
		}  catch (ParseException e) {
			Log.e(Statics.TAG, "MessageItem from web date parsing fail");
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
			Matcher mPic = Pattern.compile("src=\"(.*?)\"").matcher(m.group(1));
			if(mPic.find()) {
				this.picture = mPic.group(1);
			}
			if (m.find()) {
				Matcher mHead = Pattern.compile("(\\d\\d.\\d\\d.\\d\\d\\d\\d) - (.*?)</b>").matcher(m.group(1));
				if(mHead.find()) {
//					this.date = mHead.group(1);
					this.title = mHead.group(2);
				}
				String[] splits = m.group(1).split("<br />");
				if(splits.length > 1) {
					this.preview = splits[1];
				}
				
				Log.d(Statics.TAG, String.valueOf(splits.length));
				
			}
			this.valid = true;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof MessageItem) {
			MessageItem item = (MessageItem) o;
			if (this.id == item.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(MessageItem another) {
		return (int) (another.date.getTime() - this.date.getTime());
	}



}
