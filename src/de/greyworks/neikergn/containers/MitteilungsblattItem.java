package de.greyworks.neikergn.containers;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import de.greyworks.neikergn.Statics;

public class MitteilungsblattItem implements Comparable<MitteilungsblattItem> {
	private int id;
	private String title = "";
	private Date date;
	private String fileName = "";
	private int size;
	private boolean valid = false;


	public MitteilungsblattItem() {

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

	public String getDate() {
		return Statics.dateFormatOut.format(this.date);
	}

	public String getFileName() {
		return fileName;
	}

	public File getLocalFile() {
		return new File(Statics.extStor + "/mitteilungsblatt/" + this.fileName);
	}

	public String getUrl() {
		return "http://www.neunkirchen-am-brand.de/pdf/mblatt/" + this.fileName;
	}

	public boolean isDownloaded() {
		return getLocalFile().exists();
	}
	
	public String getSizeString() {
		return this.size + " KiB";
	}

	public void download() {
		if (!isDownloaded()) {
			File folder = new File(Statics.extStor + "/mitteilungsblatt/");
			if (!folder.exists()) {
				folder.mkdirs();
			}

			DownloadManager.Request request = new Request(Uri.parse(getUrl()));
			request.setDescription("Neikergn App Download");
			request.setTitle("Mitteilungsblatt" + getFileName());
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
			request.setDestinationInExternalFilesDir(Statics.ctx, null,
					"mitteilungsblatt/" + getFileName());
			DownloadManager manager = (DownloadManager) Statics.ctx
					.getSystemService(Context.DOWNLOAD_SERVICE);
			manager.enqueue(request);
		}
	}
	


	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("i", this.id);
			json.put("t", this.title);
			json.put("d", this.date.getTime());
			json.put("f", this.fileName);
			json.put("s", this.size);
		} catch (JSONException e) {
			Log.e(Statics.TAG, "MitteilungsblattItem to JSON fail");
			e.printStackTrace();
		}
		return json;
	};

	public static MitteilungsblattItem fromJSON(JSONObject json) {
		MitteilungsblattItem item = new MitteilungsblattItem();
		try {
			item.id = json.getInt("i");
			item.title = json.getString("t");
			item.date = new Date(json.getLong("d"));
			item.fileName = json.getString("f");
			item.size = json.getInt("s");
			item.valid = true;
		} catch (JSONException e) {
			Log.e(Statics.TAG, "MitteilungsblattItem from JSON fail");
			e.printStackTrace();
		}
		return item;
	}
	
	public static MitteilungsblattItem fromWeb(JSONObject json) {
		MitteilungsblattItem item = new MitteilungsblattItem();
		try {
			item.id = json.getInt("i");
			item.title = json.getString("t");
			item.date = Statics.dateFormatInShort.parse(json.getString("d").substring(0, 8));
			item.fileName = json.getString("d");
			item.size = json.getInt("s");
			item.valid = true;
		} catch (JSONException e) {
			Log.e(Statics.TAG, "MitteilungsblattItem from web JSON fail");
			e.printStackTrace();
		} catch (ParseException e) {
			Log.e(Statics.TAG, "MitteilungsblattItem from web Date fail");
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
			Matcher mDate = Pattern.compile("(\\d\\d.\\d\\d.\\d\\d\\d\\d)")
					.matcher(m.group(1));
			if (mDate.find()) {
//				this.date = mDate.group(1);
			}
			if (m.find()) {
				Matcher mUrl = Pattern.compile("href=\"(.*?)\"").matcher(
						m.group(1));
				if (mUrl.find()) {
//					this.url = mUrl.group(1);
				}
				if (m.find()) {
					this.title = m.group(1);
					if (m.find()) {
						Matcher mPages = Pattern.compile("(\\d+)").matcher(
								m.group(1));
						if (mPages.find()) {
//							this.pages = Integer.parseInt(mPages.group(1));
//							if (this.pages != 0 && !this.title.isEmpty()
//									&& !this.url.isEmpty()) {
//								this.valid = true;
//							}
						}
					}
				}
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MitteilungsblattItem) {
			MitteilungsblattItem item = (MitteilungsblattItem) o;
			if (this.id == item.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(MitteilungsblattItem another) {
		return another.id - this.id;
	}

}
