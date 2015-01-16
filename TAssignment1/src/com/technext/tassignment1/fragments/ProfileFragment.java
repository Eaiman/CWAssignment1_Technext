package com.technext.tassignment1.fragments;

import com.displayer.CircleImageView;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.content.ImageGalleryManager;
import com.technext.tassignment1.http.Client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements OnClickListener{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private final static String ARG_SECTION_NUMBER = "section_number";
	
	private CircleImageView imageViewProfile;
	private ImageView imageViewUploadPhoto;
	
	private ImageGalleryManager imageGalleryManager;
	
	public ProfileFragment() {
	}
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ProfileFragment newInstance(int sectionNumber) {
		ProfileFragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container,
				false);
		
		imageViewProfile = (CircleImageView) rootView.findViewById(R.id.imageViewProfile);
		imageViewUploadPhoto = (ImageView) rootView.findViewById(R.id.imageViewUploadPhoto);
		
		imageViewUploadPhoto.setImageResource(R.drawable.upload_mage_icon);
		imageViewUploadPhoto.setOnClickListener(this);
		
		MainActivity.imageLoader.loadImage(Client.getUser().getProfile_pic_url_300x300(), imageViewProfile, null);
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
		imageGalleryManager = new ImageGalleryManager(activity);
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.imageViewUploadPhoto){
			Toast.makeText(getActivity(), "CLicked", Toast.LENGTH_SHORT).show();
			imageGalleryManager.openGallery();
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Toast.makeText(getActivity(), "On activity result--> "+ resultCode, Toast.LENGTH_SHORT).show();
	}
}
