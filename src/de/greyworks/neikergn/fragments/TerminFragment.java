package de.greyworks.neikergn.fragments;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import de.greyworks.neikergn.OverviewActivity;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.modules.TerminModule;
import de.greyworks.neikergn.ui.TerminItemAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class TerminFragment extends Fragment implements Observer {
	TerminModule terminModule;
	TerminItemAdapter adapter;
	ListView lstTermine;

	public static TerminFragment newInstance() {
		TerminFragment fragment = new TerminFragment();
		return fragment;
	}

	public TerminFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_termine, container,
				false);
		lstTermine = (ListView) rootView.findViewById(R.id.trmList);

		terminModule = Statics.terminModule;
		adapter = new TerminItemAdapter(terminModule.getItems(), lstTermine
				.getContext());
		lstTermine.setAdapter(adapter);

		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_refresh) {
			terminModule.forceUpdateWeb();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_termine));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		terminModule.addObserver(this);
		terminModule.updateContent();

	};

	@Override
	public void onPause() {
		super.onPause();
		terminModule.deleteObserver(this);
	};

	@Override
	public void update(Observable observable, Object data) {
		adapter.notifyDataSetChanged();
		int scrollTo = terminModule.getIdFirstToday();
		lstTermine.smoothScrollToPositionFromTop(scrollTo, 30, 300);

	}
}