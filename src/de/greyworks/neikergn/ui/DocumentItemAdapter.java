package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.DocumentItem;

public class DocumentItemAdapter extends BaseAdapter implements SpinnerAdapter{
	ArrayList<DocumentItem> documentItems;
	Context ctx;
	LayoutInflater inflater;
	
	public DocumentItemAdapter(ArrayList<DocumentItem> documentItems, Context ctx) {
		super();
		this.documentItems = documentItems;
		this.ctx = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}



	@Override
	public int getCount() {
		return documentItems.size();
	}

	@Override
	public Object getItem(int position) {
		return documentItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DocumentItem curItem = documentItems.get(position);
		View spinView;
		
		spinView = inflater.inflate(R.layout.list_item_document, parent, false);
		
		TextView txTitle = (TextView) spinView.findViewById(R.id.doc_title);
		txTitle.setText(curItem.getTitle());
		
		if(!curItem.isDownloaded()) {
			spinView.setBackgroundColor(Color.LTGRAY);
		}
		
		return spinView;
	}
	
}