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
			Color.LTGRAY,
			Color.LTGRAY,
			Color.GRAY

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
				ctx.getString(R.string.title_about)};

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View spinView;

		spinView = inflater.inflate(R.layout.list_item_nav_drawer, parent,
				false);

		TextView txTitle = (TextView) spinView.findViewById(R.id.label_title);

		if (position == selection) {
			spinView.setBackgroundColor(entityColor[position]);
		} else {
			txTitle.setTextColor(entityColor[position]);
		}
		txTitle.setText(entityText[position]);

		return spinView;
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

}