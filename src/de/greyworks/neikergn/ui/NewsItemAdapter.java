package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.NewsItem;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class NewsItemAdapter extends BaseAdapter implements SpinnerAdapter {
	ArrayList<NewsItem> newsItems;
	Context ctx;
	LayoutInflater inflater;
	SparseArray<View> views = new SparseArray<View>();

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
		View spinView;
		String info = "";
		if (views.indexOfKey(curItem.getId()) > 0) {
			return views.get(curItem.getId());
		}

		spinView = inflater.inflate(R.layout.list_item_twoline, parent, false);
		int age = curItem.getAge();
		int bgCol;

		if (age < 1) {
			bgCol = parent.getResources().getColor(R.color.lgreen_300);
		} else if (age < 2) {
			bgCol = parent.getResources().getColor(R.color.lgreen_200);
			info = " - 1 Tag alt";
		} else if (age < 8) {
			bgCol = parent.getResources().getColor(R.color.lgreen_100);
			info = " - " + curItem.getAge() + " Tage alt";
		} else {
			bgCol = parent.getResources().getColor(R.color.lgreen_50);
			info = " - " + curItem.getAge() + " Tage alt";
		}
		spinView.findViewById(R.id.v_spacer).setBackgroundColor(bgCol);
		spinView.findViewById(R.id.label_title).setBackgroundColor(bgCol);

		TextView txTitle = (TextView) spinView.findViewById(R.id.label_title);
		txTitle.setText(Html.fromHtml(curItem.getTitle()));

		TextView txInfo = (TextView) spinView.findViewById(R.id.label_info);
		txInfo.setText(curItem.getDate() + " - " + curItem.getSource() + info);

		views.put(curItem.getId(), spinView);

		return spinView;
	}

}