package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.TerminItem;

public class TerminItemAdapter extends BaseAdapter implements SpinnerAdapter {
	ArrayList<TerminItem> terminItems;
	Context ctx;
	LayoutInflater inflater;

	public TerminItemAdapter(ArrayList<TerminItem> terminItems, Context ctx) {
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
		TerminItem curItem = terminItems.get(position);

		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_twoline, parent,
					false);
			holder.title = (TextView) convertView.findViewById(R.id.label_title);
			holder.info = (TextView) convertView.findViewById(R.id.label_info);
			holder.spacer = (View) convertView.findViewById(R.id.v_spacer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		float age = curItem.getAge();
		int bgCol;
		if (age > 0)
			bgCol = parent.getResources().getColor(R.color.orange_50);
		else if (age > -1)
			bgCol = parent.getResources().getColor(R.color.orange_300);
		else if (age > -2)
			bgCol = parent.getResources().getColor(R.color.orange_200);
		else if (age > -8)
			bgCol = parent.getResources().getColor(R.color.orange_100);
		else
			bgCol = parent.getResources().getColor(R.color.orange_50);

		holder.spacer.setBackgroundColor(bgCol);
		holder.title.setBackgroundColor(bgCol);
		holder.title.setText(curItem.getTitle());
		holder.info.setText(curItem.getInfo());

		return convertView;
	}

	public class ViewHolder {
		TextView title;
		TextView info;
		View spacer;
	}

}