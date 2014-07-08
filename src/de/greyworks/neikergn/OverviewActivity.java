package de.greyworks.neikergn;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import de.greyworks.neikergn.fragments.AboutFragment;
import de.greyworks.neikergn.fragments.DocumentsFragment;
import de.greyworks.neikergn.fragments.MessagesFragment;
import de.greyworks.neikergn.fragments.MitteilungsblattFragment;
import de.greyworks.neikergn.fragments.NewsFragment;
import de.greyworks.neikergn.fragments.NiBFragment;
import de.greyworks.neikergn.fragments.TerminFragment;
import de.greyworks.neikergn.modules.MessageModule;
import de.greyworks.neikergn.modules.NewsModule;
import de.greyworks.neikergn.modules.NiBModule;
import de.greyworks.neikergn.modules.TerminModule;

public class OverviewActivity extends Activity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private ProgressBar mProgress;
	private int progressCount;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Initialize statics
		Statics.ctx = this;
		Statics.ovr = this;
		Statics.extStor = Statics.ctx.getExternalFilesDir(null);
		Statics.newsModule = new NewsModule();
		Statics.messageModule = new MessageModule();
		Statics.terminModule = new TerminModule();
		Statics.nibModule = new NiBModule();

		setContentView(R.layout.activity_overview);

		mProgress = (ProgressBar) findViewById(R.id.progress);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

	}

	/**
	 * switch to selected menu item
	 */
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getFragmentManager();
		if (fragmentManager.getBackStackEntryCount() > 0) {
			// clear back stack on menu item select
			fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0)
					.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		Drawable bg = getResources().getDrawable(R.drawable.d_lgreen_900);
		switch (position) {

		case 0:
			// switch to news
			fragmentManager.beginTransaction()
					.replace(R.id.container, NewsFragment.newInstance())
					.commit();
			bg = getResources().getDrawable(R.drawable.d_lgreen_900);
			break;

		case 1:
			// switch to messages
			fragmentManager.beginTransaction()
					.replace(R.id.container, MessagesFragment.newInstance())
					.commit();
			bg = getResources().getDrawable(R.drawable.d_lblue_900);
			break;
		case 2:
			// switch to NIB
			fragmentManager.beginTransaction()
					.replace(R.id.container, NiBFragment.newInstance())
					.commit();
			bg = getResources().getDrawable(R.drawable.d_pink_900);
			break;
		case 3:
			// switch to appointments
			fragmentManager.beginTransaction()
					.replace(R.id.container, TerminFragment.newInstance())
					.commit();
			bg = getResources().getDrawable(R.drawable.d_dorange_900);
			break;
		case 4:
			// switch to mitteilungsblatt
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							MitteilungsblattFragment.newInstance()).commit();
			bg = getResources().getDrawable(R.drawable.d_grey_800);
			break;
		case 5:
			// siwtch to documents
			fragmentManager.beginTransaction()
					.replace(R.id.container, DocumentsFragment.newInstance())
					.commit();
			bg = getResources().getDrawable(R.drawable.d_grey_800);
			break;
		case 6:
			// switch to about
			fragmentManager.beginTransaction()
					.replace(R.id.container, AboutFragment.newInstance())
					.commit();
			bg = getResources().getDrawable(R.drawable.d_grey_900);
			break;
		default:
			// what the... well, yeah, you should not be here.
		}
		getActionBar().setBackgroundDrawable(bg);

	}

	/**
	 * sets action bar title
	 * 
	 * @param string
	 *            title to set
	 */
	public void onSectionAttached(String string) {
		mTitle = string;
	}

	/**
	 * rebuild Action bar
	 */
	public void restoreActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.overview, menu);
			restoreActionBar();
			return true;
		}
		getMenuInflater().inflate(R.menu.overview, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}

	public void setProgressBar(int i) {
		if (i == 0) {
			progressCount = 0;
		} else {
			progressCount += i;
			if (progressCount < 0)
				progressCount = 0;
		}
		mProgress.setIndeterminate(progressCount > 0);

	}

}
