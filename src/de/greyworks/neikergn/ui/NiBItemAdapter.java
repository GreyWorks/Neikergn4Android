package de.greyworks.neikergn.ui;

import java.util.ArrayList;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.containers.NiBItem;

public class NiBItemAdapter extends BaseAdapter implements SpinnerAdapter{
	ArrayList<NiBItem> terminItems;
	Context ctx;
	LayoutInflater inflater;
	SparseArray<View> views = new SparseArray<View>();
	
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
		View spinView;
		
		if(views.indexOfKey(curItem.getId())>0){
			return views.get(curItem.getId());
		}
		spinView = inflater.inflate(R.layout.list_item_nib, parent, false);
		
		TextView txTitle = (TextView) spinView.findViewById(R.id.nib_title);
		txTitle.setText(curItem.getTitle());
		
		txTitle = (TextView) spinView.findViewById(R.id.nib_text);
		txTitle.setText(curItem.getText());
		
		WebView web = (WebView) spinView.findViewById(R.id.nib_web);
		String content = "<img src=\"http://www.neunkirchen-am-brand.de/images/bdm/thumb/" + curItem.getPicture() + "\" width=\"100%\" alt=\"Neunkirchen im Bild\">";

		web.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
		
		views.put(curItem.getId(), spinView);
		return spinView;
	}
	
}