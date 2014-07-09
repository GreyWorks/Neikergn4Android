package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.MessageItem;

public class MessageItemAdapter extends BaseAdapter implements SpinnerAdapter {
	ArrayList<MessageItem> messageItems;
	Context ctx;
	LayoutInflater inflater;

	public MessageItemAdapter(ArrayList<MessageItem> messageItems, Context ctx) {
		super();
		this.messageItems = messageItems;
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return messageItems.size();
	}

	@Override
	public Object getItem(int position) {
		return messageItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MessageItem curItem = messageItems.get(position);
		ViewHolder holder = new ViewHolder();
		if(convertView == null){
			convertView = inflater.inflate(R.layout.list_item_twoline, parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.label_title);
			holder.info = (TextView) convertView.findViewById(R.id.label_info);
			holder.spacer = convertView.findViewById(R.id.v_spacer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int age = curItem.getAge();
		int bgCol;
		Resources res = parent.getResources();
		if (age < 0)
			bgCol = res.getColor(R.color.lblue_50);
		else if (age < 1)
			bgCol = res.getColor(R.color.lblue_300);
		else if (age < 2)
			bgCol = res.getColor(R.color.lblue_200);
		else if (age < 8)
			bgCol = res.getColor(R.color.lblue_100);
		else
			bgCol = res.getColor(R.color.lblue_50);
		holder.spacer.setBackgroundColor(bgCol);
		holder.title.setBackgroundColor(bgCol);
		holder.title.setText(Html.fromHtml(curItem.getTitle()));
		holder.info.setText(curItem.getDate() + "\n"
				+ Html.fromHtml(curItem.getPreview()));

		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView info;
		View spacer;
	}

}