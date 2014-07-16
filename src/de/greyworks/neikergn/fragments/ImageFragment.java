package de.greyworks.neikergn.fragments;

import java.io.File;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.greyworks.neikergn.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImageFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */

	private String path = "";
	private String title = "";
	private String info = "";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ImageFragment newInstance(String path, String title,
			String info) {
		ImageFragment fragment = new ImageFragment();
		Bundle args = new Bundle();
		args.putString("path", path);
		args.putString("title", title);
		args.putString("info", info);
		fragment.setArguments(args);
		return fragment;
	}

	public ImageFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image, container,
				false);
		Bundle args = this.getArguments();
		this.path = args.getString("path");
		this.title = args.getString("title");
		this.info = args.getString("info");

		((TextView) rootView.findViewById(R.id.label_title))
				.setText(this.title);
		((TextView) rootView.findViewById(R.id.label_info)).setText(this.info);
		ImageView img = (ImageView) rootView.findViewById(R.id.image);
		boolean isSet = false;
		if (this.path.length() > 0) {
			File f = new File(path);
			if (f.exists()) {
				img.setImageURI(Uri.fromFile(f));
				isSet = true;
			}
		}
		if(!isSet) {
			img.setAlpha(0.5f);
		}

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

}