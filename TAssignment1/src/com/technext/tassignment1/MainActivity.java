package com.technext.tassignment1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.technext.tassignment1.fragments.LoginFragment;
import com.technext.tassignment1.fragments.LoginFragment.LoginSuccessListener;
import com.technext.tassignment1.fragments.ProfileFragment;
import com.technext.tassignment1.fragments.RegistrationFragment;
import com.technext.tassignment1.fragments.RegistrationFragment.RegistrationCompleteListener;
import com.technext.tassignment1.http.Client;
import com.technext.tassignment1.model.User;
import com.utils.ImageCache.ImageCacheParams;
import com.utils.ImageFetcher;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks,LoginSuccessListener, RegistrationCompleteListener {

	 private static final String IMAGE_CACHE_DIR = "cwc_tassignment1";
	 public static ImageFetcher imageLoader; //use to load image from internet
	 int screenWidth;
	 int screenHeight;

	
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
		
		
		final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
		
		initImageLoader(screenHeight,screenWidth);
		if(Client.getUserFromSession(getApplicationContext()) == null){
			Toast.makeText(getApplicationContext(), "user logged out", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getApplicationContext(), "user logged in", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	 @Override
	 public void onPause() {
	     super.onPause();
	     imageLoader.setPauseWork(false);
	     imageLoader.setExitTasksEarly(true);
	     imageLoader.flushCache();
	 }
	 @Override
	 public void onResume() {
	     super.onResume();
	     imageLoader.setExitTasksEarly(false);
	 }
	 
	 @Override
	 public void onDestroy() {
	     super.onDestroy();
	     imageLoader.closeCache();
	 }

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						chooseFragment(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
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
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private Fragment fragment = null;
	
	private Fragment chooseFragment(int position){
		switch (position) {
		case 1:
			fragment = LoginFragment.newInstance(position);
			break;

		case 2:
			fragment = RegistrationFragment.newInstance(position);
			break;

		case 3:
			fragment = ProfileFragment.newInstance(position);
			break;
			
		default:
			fragment = PlaceholderFragment.newInstance(position);
			break;
		}
		
		return fragment;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void onloginComplete(User user) {
		Toast.makeText(getApplicationContext(), "In Activity email--> "+user.getEmail(), Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onRegistrationComplete(User user) {
		Toast.makeText(getApplicationContext(), "In Activity email--> "+user.getEmail(), Toast.LENGTH_SHORT).show();
		
	}
	
	private void initImageLoader(int screenHeight, int screenWidth){
		int longest = (screenHeight > screenWidth ? screenHeight : screenHeight) / 2;
		 ImageCacheParams cacheParams = new ImageCacheParams(MainActivity.this, IMAGE_CACHE_DIR);
		 cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
		 imageLoader = new ImageFetcher(MainActivity.this, longest);
		 imageLoader.setLoadingImage(R.drawable.empty_photo);
		 imageLoader.useLoadingImageForFadein(true);
		 imageLoader.addImageCache(MainActivity.this.getSupportFragmentManager(), cacheParams);
	}

}
