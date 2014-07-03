package de.greyworks.neikergn.fragments;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;
import de.greyworks.neikergn.containers.MessageItem;
import de.greyworks.neikergn.modules.HttpModule;

/**
 * A placeholder fragment containing a simple view.
 */
public class MessageContentFragment extends Fragment implements Observer {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */

	private WebView web;
	private int id;
	private MessageItem item;
	private String baseUrl = "http://www.neunkirchen-am-brand.de/app/aktuelles_text.php?id=";
	private String content = "";
	HttpModule http = new HttpModule();
	ProgressDialog pDiag;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static MessageContentFragment newInstance(int id) {
		MessageContentFragment fragment = new MessageContentFragment();
		Bundle args = new Bundle();
		args.putInt("id", id);
		fragment.setArguments(args);
		return fragment;
	}

	public MessageContentFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_web,
				container, false);
		Bundle args = this.getArguments();
		this.id = args.getInt("id");
		item = Statics.messageModule.getById(id);
		web = (WebView) rootView.findViewById(R.id.webView1);
		web.getSettings().setBuiltInZoomControls(true);
		web.getSettings().setDisplayZoomControls(false);

		web.getSettings().setLoadWithOverviewMode(true);

		pDiag = new ProgressDialog(Statics.ctx);
		pDiag.setMessage("Lade Meldung ...");
		pDiag.setCancelable(false);
		pDiag.show();
		// initialize http module;
		http.addObserver(this);
		http.httpGet(baseUrl + id);
		

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	/**
	 * executed when http module has finished
	 */
	@Override
	public void update(Observable observable, Object data) {
		content = "<table><tr><td><b>" + item.getTitle()
				+ "</b></td></tr><tr><td>" + item.getDate() + "<br />&nbsp;</td></tr><tr><td>"
				+ http.getContent().replaceAll("(\r\n|\n)", "<br />") + "</td></tr></table>";

		web.loadDataWithBaseURL("", content, "text/html", "UTF-8", "");
		pDiag.cancel();

	}
}