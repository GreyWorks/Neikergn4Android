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
		if(views.indexOfKey(curItem.getId()) > 0) {
			return views.get(curItem.getId());
		}

		spinView = inflater.inflate(R.layout.list_item_news, parent, false);
		int age = curItem.getAge();
		if (age < 1) {
			spinView.setBackgroundColor(parent.getResources().getColor(
					R.color.green_0));
		} else if (age < 2) {
			spinView.setBackgroundColor(parent.getResources().getColor(
					R.color.green_1));
			info = " - 1 Tag alt";
		} else if (age < 8) {
			spinView.setBackgroundColor(parent.getResources().getColor(
					R.color.green_2));
			info = " - " + curItem.getAge() + " Tage alt";
		} else {
			spinView.setBackgroundColor(Color.WHITE);
			info = " - " + curItem.getAge() + " Tage alt";
		}

		TextView txTitle = (TextView) spinView.findViewById(R.id.label_nav);
		txTitle.setText(Html.fromHtml(curItem.getTitle()));

		TextView txInfo = (TextView) spinView.findViewById(R.id.date_source);
		txInfo.setText(curItem.getDate() + " - " + curItem.getSource()
				+ info);
		
		views.put(curItem.getId(), spinView);

		return spinView;
	}

}