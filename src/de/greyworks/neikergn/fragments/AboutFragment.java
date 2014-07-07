package de.greyworks.neikergn.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import de.greyworks.neikergn.OverviewActivity;
import de.greyworks.neikergn.R;
import de.greyworks.neikergn.Statics;

/**
 * A placeholder fragment containing a simple view.
 */
public class AboutFragment extends Fragment {

	int awesomeCounter = 0;

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

		TextView mailNeikergn = (TextView) rootView
				.findViewById(R.id.about_mail_webteam);
		mailNeikergn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMail("web-team@neikergn.de", "");
			}
		});

		TextView mailGrey = (TextView) rootView
				.findViewById(R.id.about_mail_grey);
		mailGrey.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sendMail("michael@neikergn.de", "[Neikergn4Android]");
			}
		});

		((TextView) rootView.findViewById(R.id.about_app))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						awesomeCounter++;
						TextView t = (TextView) v;
						if (awesomeCounter == 10) {
							t.setText(R.string.about_app_aa);
						} else if(awesomeCounter > 10) {
							t.setText(R.string.about_app);
							awesomeCounter = 0;
						}
					}
				});
		
		((ImageView) rootView.findViewById(R.id.img_greyworks)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://greyworks.de/?page=home"));
				startActivity(browserIntent);
			}
		});

		return rootView;
	}

	private void sendMail(String to, String subject) {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
		// email.putExtra(Intent.EXTRA_CC, new String[]{ to});
		// email.putExtra(Intent.EXTRA_BCC, new String[]{to});
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		// email.putExtra(Intent.EXTRA_TEXT, message);

		// need this to prompts email client only
		email.setType("message/rfc822");

		startActivity(Intent.createChooser(email, "Mail Client wählen :"));

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