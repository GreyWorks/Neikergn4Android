package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.NewsItem;

public class NewsItemAdapter extends BaseAdapter implements SpinnerAdapter {
	ArrayList<NewsItem> newsItems;
	Context ctx;
	LayoutInflater inflater;

	public NewsItemAdapter(ArrayList<NewsItem> newsItems, Context ctx) {
		super();
		this.newsItems = newsItems;
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return newsItems.size();
	}

	@Override
	public Object getItem(int position) {
		return newsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		NewsItem curItem = newsItems.get(position);

		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_twoline, parent,
					false);
			holder.title = (TextView) convertView
					.findViewById(R.id.label_title);
			holder.info = (TextView) convertView.findViewById(R.id.label_info);
			holder.spacer = convertView.findViewById(R.id.v_spacer);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int age = curItem.getAge();
		int bgCol;
		String info = "";
		if (age < 1) {
			bgCol = parent.getResources().getColor(R.color.lgreen_300);
			info = " (Heute)";
		} else if (age < 2) {
			bgCol = parent.getResources().getColor(R.color.lgreen_200);
			info = " (Gestern)";
		} else if (age < 8) {
			bgCol = parent.getResources().getColor(R.color.lgreen_100);
			info = " (vor " + age + " Tagen)";
		} else {
			bgCol = parent.getResources().getColor(R.color.lgreen_50);
			info = " (vor " + age + " Tagen)";
		}
		holder.spacer.setBackgroundColor(bgCol);
		holder.title.setBackgroundColor(bgCol);
		holder.title.setText(Html.fromHtml(curItem.getTitle()));
		holder.info.setText(curItem.getSource() + " - " + curItem.getDate() + info);
		return convertView;
	}

	static class ViewHolder {
		TextView title;
		TextView info;
		View spacer;
	}

}
