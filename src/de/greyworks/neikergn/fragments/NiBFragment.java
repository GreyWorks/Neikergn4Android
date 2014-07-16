package de.greyworks.neikergn.fragments;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.greyworks.neikergn.OverviewActivity;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.NiBItem;
import de.greyworks.neikergn.modules.NiBModule;
import de.greyworks.neikergn.ui.NiBItemAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class NiBFragment extends Fragment implements Observer {
	NiBModule nibModule;
	ListView lstNib;

	public static NiBFragment newInstance() {
		NiBFragment fragment = new NiBFragment();
		return fragment;
	}

	public NiBFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		lstNib = (ListView) rootView.findViewById(R.id.list_lst);

		lstNib.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long id) {
				NiBItem item = nibModule.getItems().get(position);
				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager
						.beginTransaction()
						.replace(
								Statics.ovr.getFragContent(),
								ImageFragment.newInstance(
										Statics.extStor.getAbsolutePath()
												+ "/nib/" + item.getPicture(),
										item.getTitle(), ""))
						.addToBackStack(null).commit();
			}
		});

		nibModule = Statics.nibModule;

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
			nibModule.forceUpdateWeb();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_imBild));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		nibModule.addObserver(this);
		nibModule.updateContent();

	};

	@Override
	public void onPause() {
		super.onPause();
		nibModule.deleteObserver(this);
	};

	@Override
	public void update(Observable observable, Object data) {
		lstNib.setAdapter(new NiBItemAdapter(nibModule.getItems(), lstNib
				.getContext()));

	}
}