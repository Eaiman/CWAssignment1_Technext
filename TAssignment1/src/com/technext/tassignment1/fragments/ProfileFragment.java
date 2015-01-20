package com.technext.tassignment1.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import com.displayer.CircleImageView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.content.ImageGalleryManager;
import com.technext.tassignment1.dialog.SplashProgressDialog;
import com.technext.tassignment1.http.Client;
import com.technext.tassignment1.model.User;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ProfileFragment extends Fragment implements OnClickListener{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private final static String ARG_SECTION_NUMBER = "section_number";
	
	public static final int IMAGE_GALLERY_REQUEST_CODE = 12; 
	
	private CircleImageView imageViewProfile;
	private ImageView imageViewProfileBg;
	private ImageView imageViewUploadPhoto;
	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextEmail;
	private EditText editTextPassword;
	private EditText editTextNewPassword;
	private int profileImageSize;
	
	private SplashProgressDialog progress;
	
	//private ImageGalleryManager imageGalleryManager;
	
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
		
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MainActivity.screenWidth, MainActivity.screenHeight*2/5);
		imageViewProfileBg = (ImageView) rootView.findViewById(R.id.imageViewProfileBg);
		
		imageViewProfileBg.setLayoutParams(params);
		
		imageViewProfile = (CircleImageView) rootView.findViewById(R.id.imageViewProfile);
		imageViewUploadPhoto = (ImageView) rootView.findViewById(R.id.imageViewUploadPhoto);
		
		editTextFirstName = (EditText) rootView.findViewById(R.id.editTextInfoFirstName);
		editTextFirstName.setEnabled(false);
		editTextFirstName.setText(Client.getUser().getFirstname());
		
		editTextLastName = (EditText) rootView.findViewById(R.id.editTextInfoLasttName);
		editTextLastName.setEnabled(false);
		editTextLastName.setText(Client.getUser().getLastname());
		
		editTextEmail = (EditText) rootView.findViewById(R.id.editTextInfoEmail);
		editTextEmail.setEnabled(false);
		editTextEmail.setText(Client.getUser().getEmail());
		
		editTextPassword = (EditText) rootView.findViewById(R.id.editTextInfoPassword);
		editTextPassword.setEnabled(false);
		editTextPassword.setVisibility(View.GONE);
		
		editTextNewPassword = (EditText) rootView.findViewById(R.id.editTextInfoNewPassword);
		editTextNewPassword.setEnabled(false);
		editTextNewPassword.setVisibility(View.GONE);
		
		
		imageViewUploadPhoto.setImageResource(R.drawable.upload_mage_icon);
		imageViewUploadPhoto.setOnClickListener(this);
		
		profileImageSize = MainActivity.screenWidth < MainActivity.screenHeight ? (MainActivity.screenWidth/2) : (MainActivity.screenHeight/2);
		
		Picasso.with(getActivity())
		  .load(Client.getUser().getProfile_pic_url())
		   .placeholder(R.drawable.empty_photo)
		   .error(R.drawable.empty_photo)
		  .resize(profileImageSize, profileImageSize)
		  .centerCrop()
		  .into(imageViewProfile);
		//MainActivity.imageLoader.loadImage(Client.getUser().getProfile_pic_url(), imageViewProfile, null);
		
	/*	ArrayList<String> pathList = getImagePaths();
		Toast.makeText(getActivity(), "Path list size-->"+ pathList.size(), Toast.LENGTH_LONG).show();
		Log.e("path size-->", ""+pathList.size());
		
		Iterator<String> iter = pathList.iterator();
		
		while(iter.hasNext()){
			String path = iter.next();
			Log.e("path---> ", path);
		}*/
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
		//imageGalleryManager = new ImageGalleryManager(ProfileFragment.this);
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
			//imageGalleryManager.openGallery();
			Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(imageGalleryIntent, IMAGE_GALLERY_REQUEST_CODE);
		}	
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		//super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(getActivity(), "On activity result--> "+ resultCode, Toast.LENGTH_SHORT).show();
		if(requestCode == IMAGE_GALLERY_REQUEST_CODE){
			Toast.makeText(getActivity(), "On activity result--> "+ resultCode, Toast.LENGTH_SHORT).show();
			String path = data.getData().getPath();
			Toast.makeText(getActivity(), "path--> "+path, Toast.LENGTH_LONG).show();
			Log.e("path", path);
			String filePath = getRealPathFromURI(data.getData());
			Toast.makeText(getActivity(), "path + "+ filePath, Toast.LENGTH_SHORT).show();
			File file = new File(filePath);
			
				Toast.makeText(getActivity(), "file exist", Toast.LENGTH_SHORT).show();
			
				Picasso.with(getActivity())
				  .load(file)
				   .placeholder(R.drawable.empty_photo)
				   .error(R.drawable.empty_photo)
				  .resize(profileImageSize, profileImageSize)
				  .centerCrop()
				  .into(imageViewProfile);
			
			
			/*RequestParams params = new RequestParams();
			params.put("user_id", Client.getUser().getId().toString());
			params.put("session_token", Client.getUser().getSession_token());
			try {
				params.put("profile_pic", file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Client.post(getActivity(), Client.URL_UPLOAD_PRO_PIC, params, uploadImageResponseHandler);*/
		
		}
			
	}
	
	public String getRealPathFromURI(Uri contentUri) {

        // can post image
        String [] proj={MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery( contentUri,
                        proj, // Which columns to return
                        null,       // WHERE clause; which rows to return (all rows)
                        null,       // WHERE clause selection arguments (none)
                        null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
	
	AsyncHttpResponseHandler uploadImageResponseHandler = new AsyncHttpResponseHandler(){
		public void onStart() {
			progress = new SplashProgressDialog(getActivity());
			progress.show();
		};
		public void onSuccess(int statusCode, String response) {
			Gson gson = new Gson();
			User user = gson.fromJson(response, User.class);
			Client.saveSession(getActivity(), user);
			Client.setUser(user);
			
			Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
			Log.e("new url--> ", user.getProfile_pic_url());
			MainActivity.imageLoader.loadImage(user.getProfile_pic_url(), imageViewProfile, null);
		};
		public void onFailure(Throwable arg0, String arg1) {
			
		};
		public void onFinish() {
			progress.dismiss();
		};
	};
	
	private ArrayList<String> getImagePaths(){
		  String [] proj={MediaStore.Images.Media.DATA};
			Cursor mImageCursor = getActivity().managedQuery( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					proj, null, null, null );
			
			ArrayList<String> pathArray = new ArrayList<String>();
			 
			mImageCursor.moveToFirst();
			while (!mImageCursor.isAfterLast()) {
				
				pathArray.add(getRealPathFromCursor(mImageCursor));
				mImageCursor.moveToNext();
			}
			
			return pathArray;
	}
	
	private String getRealPathFromCursor(Cursor cursor){
		 int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        return cursor.getString(column_index);
	}
}
