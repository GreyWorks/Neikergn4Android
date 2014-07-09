package de.greyworks.neikergn.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;

public class NavItemAdapter extends BaseAdapter implements SpinnerAdapter {

	LayoutInflater inflater;
	String[] entityText;
	public int selection = -1;
	public static int[] entityColor = new int[] {
			Statics.ctx.getResources().getColor(R.color.lgreen_300),
			Statics.ctx.getResources().getColor(R.color.lblue_300),
			Statics.ctx.getResources().getColor(R.color.pink_300),
			Statics.ctx.getResources().getColor(R.color.orange_300),
			Color.LTGRAY, Color.LTGRAY, Color.GRAY

	};

	public NavItemAdapter(Context ctx) {
		super();

		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		entityText = new String[] { ctx.getString(R.string.title_news),
				ctx.getString(R.string.title_messages),
				ctx.getString(R.string.title_imBild),
				ctx.getString(R.string.title_termine),
				ctx.getString(R.string.title_mitteilungsblatt),
				ctx.getString(R.string.title_documents),
				ctx.getString(R.string.title_about) };

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_nav_drawer,
					parent, false);
			holder.title = (TextView) convertView.findViewById(R.id.label_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == selection) {
			convertView.setBackgroundColor(entityColor[position]);
			holder.title.setTextColor(Color.DKGRAY);
		} else {
			convertView.setBackgroundColor(Color.DKGRAY);
			holder.title.setTextColor(entityColor[position]);
		}
		holder.title.setText(entityText[position]);

		return convertView;
	}

	@Override
	public int getCount() {
		return entityText.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView title;
	}

}