package de.greyworks.neikergn.containers;

import java.io.File;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import de.greyworks.neikergn.Statics;

public class DocumentItem implements Comparable<DocumentItem> {
	private String title = "";
	private String url = "";

	private boolean valid = false;

	public DocumentItem() {

	}

	public DocumentItem(String title, String url) {
		this.title = title;
		this.url = url;
		this.valid = true;
	}

	public String getTitle() {
		if (valid) {
			return this.title;
		} else {
			return "Invalid.";
		}
	}
	
	public String getFileName() {
		String[] splits = this.url.split("/");
		return splits[splits.length - 1];
	}

	public File getLocalFile() {
		return new File(Statics.extStor + "/" + getFileName());
	}

	public String getUrl() {
		return "http://www.neunkirchen-am-brand.de/" + this.url;
	}

	public boolean isDownloaded() {
		return getLocalFile().exists();
	}

	public void download() {
		if (!isDownloaded()) {
			DownloadManager.Request request = new Request(Uri.parse(getUrl()));
			request.setDescription("Neikergn App Download");
			request.setTitle(getTitle());
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
			request.setDestinationInExternalFilesDir(Statics.ctx, null,
					getFileName());
			DownloadManager manager = (DownloadManager) Statics.ctx
					.getSystemService(Context.DOWNLOAD_SERVICE);
			manager.enqueue(request);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof DocumentItem) {
			DocumentItem item = (DocumentItem) o;
			if (this.title.equals(item.title)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(DocumentItem another) {
		return 0;
	}

}
