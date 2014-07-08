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
import de.greyworks.neikergn.modules.MessageModule;
import de.greyworks.neikergn.ui.MessageItemAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessagesFragment extends Fragment implements Observer {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */

	MessageModule messageModule;
	ListView lstMessages;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MessagesFragment newInstance() {
		MessagesFragment fragment = new MessagesFragment();
		return fragment;
	}

	public MessagesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_list, container,
				false);
		lstMessages = (ListView) rootView.findViewById(R.id.list_lst);

		messageModule = Statics.messageModule;

		lstMessages.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long id) {

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager
						.beginTransaction()
						.replace(
								R.id.container,
								MessageContentFragment.newInstance(messageModule
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
			messageModule.forceUpdateWeb();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_messages));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		messageModule.addObserver(this);
		messageModule.updateContent();

	};
	
	@Override
	public void onPause() {
		super.onPause();
		messageModule.deleteObserver(this);
	};

	

	@Override
	public void update(Observable observable, Object data) {
		lstMessages.setAdapter(new MessageItemAdapter(messageModule
				.getItems(), lstMessages.getContext()));

	}
}