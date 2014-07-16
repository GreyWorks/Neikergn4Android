package de.greyworks.neikergn.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.NiBItem;

public class NiBItemAdapter extends BaseAdapter implements SpinnerAdapter {
	ArrayList<NiBItem> terminItems;
	Context ctx;
	LayoutInflater inflater;

	public NiBItemAdapter(ArrayList<NiBItem> terminItems, Context ctx) {
		super();
		this.terminItems = terminItems;
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return terminItems.size();
	}

	@Override
	public Object getItem(int position) {
		return terminItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NiBItem curItem = terminItems.get(position);
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_nib, parent,
					false);
			holder.title = (TextView) convertView.findViewById(R.id.nib_title);
			holder.info = (TextView) convertView.findViewById(R.id.nib_text);
			holder.img = (ImageView) convertView.findViewById(R.id.nib_img);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}
		new DownloadImageTask(holder.img)
				.execute("http://www.neunkirchen-am-brand.de/images/bdm/thumb/"
						+ curItem.getPicture());

		holder.title.setText(curItem.getTitle());
		holder.info.setText(curItem.getText());
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView info;
		ImageView img;
		Bitmap bmp;
	}

	private class DownloadImageTask extends AsyncTask<String, Void, File> {
		ImageView img;

		public DownloadImageTask(ImageView img) {
			this.img = img;
		}

		@Override
		protected File doInBackground(String... params) {

			// check if dir exists, else create it
			File nibDir = new File(Statics.extStor.getAbsolutePath() + "/nib");
			if (!nibDir.exists()) {
				nibDir.mkdirs();
			}
			String[] splits = params[0].split("/");
			File localFile = new File(Statics.extStor.getAbsolutePath()
					+ "/nib/" + splits[splits.length - 1]);
			if (!localFile.exists()) {
				// load from web
				if (Statics.DEBUG)
					Log.d(Statics.TAG, "Load image from storage");
				try {
					URL url = new URL(params[0]);
					URLConnection connection = url.openConnection();
					Bitmap bmp = BitmapFactory.decodeStream(connection
							.getInputStream());

					FileOutputStream fOut = new FileOutputStream(localFile);
					bmp.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
					fOut.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			return localFile;
		}

		@Override
		protected void onPostExecute(File result) {
			super.onPostExecute(result);
			if (result.exists()) {
				img.setImageURI(Uri.fromFile(result));
			}
		}
	}

}