package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.MitteilungsblattItem;

public class MitteilungsblattItemAdapter extends BaseAdapter implements
		SpinnerAdapter {
	ArrayList<MitteilungsblattItem> mitteilungsblattItems;
	Context ctx;
	LayoutInflater inflater;

	public MitteilungsblattItemAdapter(
			ArrayList<MitteilungsblattItem> mitteilungsblattItems, Context ctx) {
		super();
		this.mitteilungsblattItems = mitteilungsblattItems;
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mitteilungsblattItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mitteilungsblattItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MitteilungsblattItem curItem = mitteilungsblattItems.get(position);
		View spinView;

		spinView = inflater.inflate(R.layout.list_item_twoline, parent, false);

		TextView txDatePages = (TextView) spinView
				.findViewById(R.id.label_title);
		txDatePages.setText(curItem.getDate());

		TextView txTitle = (TextView) spinView.findViewById(R.id.label_info);
		txTitle.setText(Html.fromHtml(curItem.getTitle()) + "\n"
				+ curItem.getSizeString());

		int bgCol;
		if (curItem.isDownloaded()) {
			bgCol = parent.getResources().getColor(R.color.lgreen_50);
		} else {
			bgCol = Color.LTGRAY;
		}

		spinView.findViewById(R.id.v_spacer).setBackgroundColor(bgCol);
		spinView.findViewById(R.id.label_title).setBackgroundColor(bgCol);

		return spinView;
	}

}