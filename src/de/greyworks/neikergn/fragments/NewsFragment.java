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
import de.greyworks.neikergn.modules.NewsModule;
import de.greyworks.neikergn.ui.NewsItemAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsFragment extends Fragment implements Observer {
	NewsModule newsModule;
	ListView lstNews;

	public static NewsFragment newInstance() {
		NewsFragment fragment = new NewsFragment();
		return fragment;
	}

	public NewsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		lstNews = (ListView) rootView.findViewById(R.id.list_lst);

		newsModule = Statics.newsModule;

		lstNews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long id) {

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
						.replace(
								Statics.ovr.getFragContent(),
								NewsContentFragment.newInstance(newsModule
										.getItems().get(position).getId()))
						.addToBackStack(null).commit();

			}

		});

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
			newsModule.forceUpdateWeb();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_news));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		newsModule.addObserver(this);
		newsModule.updateContent();

	};

	@Override
	public void onPause() {
		super.onPause();
		newsModule.deleteObserver(this);
	};

	@Override
	public void update(Observable observable, Object data) {
		lstNews.setAdapter(new NewsItemAdapter(newsModule.getItems(), lstNews
				.getContext()));

	}
}