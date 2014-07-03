package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.MessageItem;

public class MessageItemAdapter extends BaseAdapter implements SpinnerAdapter{
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
		View spinView;
		
		spinView = inflater.inflate(R.layout.list_item_news, parent, false);
		int age = curItem.getAge();
		int bgCol;
		Resources res = parent.getResources();
		if(age < 0)
			bgCol = res.getColor(R.color.blue_2);
		else if(age < 1)
			bgCol = res.getColor(R.color.blue_0);
		else if(age < 2)
			bgCol = res.getColor(R.color.blue_1);
		else if(age < 8)
			bgCol = res.getColor(R.color.blue_2);
		else
			bgCol = Color.WHITE;
		spinView.setBackgroundColor(bgCol);
		
		TextView txTitle = (TextView) spinView.findViewById(R.id.label_nav);
		txTitle.setText(Html.fromHtml(curItem.getTitle()));
		
		TextView txInfo = (TextView) spinView.findViewById(R.id.date_source);
		txInfo.setText(curItem.getDate() + "\n" + Html.fromHtml(curItem.getPreview()));
		
		return spinView;
	}
	
}