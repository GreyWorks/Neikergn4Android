package de.greyworks.neikergn.fragments;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import de.greyworks.neikergn.OverviewActivity;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.DocumentItem;
import de.greyworks.neikergn.modules.DocumentsModule;
import de.greyworks.neikergn.ui.DocumentItemAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class DocumentsFragment extends Fragment {

	DocumentsModule documentsModule;
	ListView lstDocs;

	BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(Statics.ctx, "Download abgeschlossen",
					Toast.LENGTH_SHORT).show();
			lstDocs.setAdapter(new DocumentItemAdapter(documentsModule
					.getItems(), lstDocs.getContext()));

		}
	};

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static DocumentsFragment newInstance() {
		DocumentsFragment fragment = new DocumentsFragment();
		return fragment;
	}

	public DocumentsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_documents,
				container, false);

		documentsModule = new DocumentsModule();
		documentsModule.updateContent();

		lstDocs = (ListView) rootView.findViewById(R.id.listDocuments);

		lstDocs.setAdapter(new DocumentItemAdapter(documentsModule.getItems(),
				lstDocs.getContext()));

		lstDocs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View v,
					int position, long id) {
				DocumentItem item = documentsModule.getItems().get(position);

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
		((OverviewActivity) activity).onSectionAttached(Statics.ctx
				.getString(R.string.title_documents));
		super.onAttach(activity);

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

}