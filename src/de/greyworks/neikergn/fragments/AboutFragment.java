package de.greyworks.neikergn.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import de.greyworks.neikergn.OverviewActivity;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment {

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();
		return fragment;
	}

	public AboutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_about, container,
				false);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_about));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();

	};

	@Override
	public void onPause() {
		super.onPause();
	};

}