package de.greyworks.neikergn.fragments;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import android.widget.Toast;
import de.greyworks.neikergn.OverviewActivity;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.MitteilungsblattItem;
import de.greyworks.neikergn.modules.MitteilungsblattModule;
import de.greyworks.neikergn.ui.MitteilungsblattItemAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MitteilungsblattFragment extends Fragment implements Observer {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */

	MitteilungsblattModule mitteilungsblattModule;
	ListView lstMitteilungsblatt;

	BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(Statics.ctx, "Download abgeschlossen",
					Toast.LENGTH_SHORT).show();
			lstMitteilungsblatt.setAdapter(new MitteilungsblattItemAdapter(
					mitteilungsblattModule.getItems(),
					lstMitteilungsblatt.getContext()));

		}
	};

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MitteilungsblattFragment newInstance() {
		MitteilungsblattFragment fragment = new MitteilungsblattFragment();
		return fragment;
	}

	public MitteilungsblattFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_mitteilungsblatt,
				container, false);
		lstMitteilungsblatt = (ListView) rootView
				.findViewById(R.id.listMitteilungsblatt);

		mitteilungsblattModule = new MitteilungsblattModule();
		mitteilungsblattModule.addObserver(this);
		mitteilungsblattModule.updateContent();

		lstMitteilungsblatt.setAdapter(new MitteilungsblattItemAdapter(
				mitteilungsblattModule.getItems(),
				lstMitteilungsblatt.getContext()));

		lstMitteilungsblatt.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long id) {

				MitteilungsblattItem item = mitteilungsblattModule
						.getItems().get(position);

				if (item.isDownloaded()) {
					Intent intent = new Intent();
					intent.setAction(android.content.Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(item.getLocalFile()),
							"application/pdf");
					try {
						startActivity(intent);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(Statics.ctx,
								"Keine passende App zum Öffnen gefunden",
								Toast.LENGTH_LONG).show();
					}
				} else {
					item.download();
				}

			}

		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_mitteilungsblatt));
	}

	@Override
	public void onResume() {
		super.onResume();
		Statics.ctx.registerReceiver(onDownloadComplete, new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE));

	}

	@Override
	public void onPause() {
		super.onPause();
		Statics.ctx.unregisterReceiver(onDownloadComplete);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_refresh) {
			mitteilungsblattModule.forceUpdateWeb();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void update(Observable observable, Object data) {
		lstMitteilungsblatt.setAdapter(new MitteilungsblattItemAdapter(
				mitteilungsblattModule.getItems(),
				lstMitteilungsblatt.getContext()));

	}
}