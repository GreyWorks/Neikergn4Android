package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.TerminItem;

public class TerminItemAdapter extends BaseAdapter implements SpinnerAdapter{
	ArrayList<TerminItem> terminItems;
	Context ctx;
	LayoutInflater inflater;
	SparseArray<View> views = new SparseArray<View>();
	
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
		View spinView;
		
		if(views.indexOfKey(curItem.getId()) > 0) {
			return views.get(curItem.getId());
		}
		spinView = inflater.inflate(R.layout.list_item_termin, parent, false);
		float age = curItem.getAge();
		if(age > 0)
			spinView.setBackgroundColor(Color.WHITE);
		else if(age > -1)
			spinView.setBackgroundColor(parent.getResources().getColor(R.color.orange_0));
		else if(age > -2)
			spinView.setBackgroundColor(parent.getResources().getColor(R.color.orange_1));
		else if(age > -8)
			spinView.setBackgroundColor(parent.getResources().getColor(R.color.orange_2));
		else
			spinView.setBackgroundColor(Color.WHITE);
		
		TextView txTitle = (TextView) spinView.findViewById(R.id.trm_title);
		txTitle.setText(curItem.getTitle());
		
		txTitle = (TextView) spinView.findViewById(R.id.trm_date);
		txTitle.setText(curItem.getInfo());
		views.put(curItem.getId(), spinView);
		
		return spinView;
	}
	
}