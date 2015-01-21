package com.technext.tassignment1.fragments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import com.displayer.CircleImageView;
import com.enrique.stackblur.StackBlurManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.content.ImageGalleryManager;
import com.technext.tassignment1.dialog.SplashProgressDialog;
import com.technext.tassignment1.http.Client;
import com.technext.tassignment1.model.ErrorResponse;
import com.technext.tassignment1.model.User;
import com.technext.tassignment1.utils.CommonUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ProfileFragment extends Fragment implements OnClickListener {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private final static String ARG_SECTION_NUMBER = "section_number";

	public static final int IMAGE_GALLERY_REQUEST_CODE = 12;

	private CircleImageView imageViewProfile;
	private ImageView imageViewProfileBg;
	private ImageView imageViewUploadPhoto;

	// labels
	private TextView labelFirstName;
	private TextView labelLastName;
	private TextView labelEmail;
	private TextView labelPassword;
	private TextView labelNewPassword;
	private TextView labelHeadingName;
	

	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextEmail;
	private EditText editTextPassword;
	private EditText editTextNewPassword;
	private int profileImageSize;

	private SplashProgressDialog progress;
	private StackBlurManager blurManger;
	private Button buttonEdit;
	private Button buttonSave;

	// private ImageGalleryManager imageGalleryManager;

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

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				MainActivity.screenWidth, MainActivity.screenHeight * 2 / 5);
		imageViewProfileBg = (ImageView) rootView
				.findViewById(R.id.imageViewProfileBg);
		/*
		 * imageViewProfileBg.setLayoutParams(params);
		 * Picasso.with(getActivity()).
		 * load(Client.getUser().getProfile_pic_url())
		 * .placeholder(R.drawable.empty_photo) .error(R.drawable.empty_photo)
		 * .into(imageViewProfileBg, new Callback.EmptyCallback(){
		 * 
		 * @Override public void onError() {
		 * 
		 * 
		 * }
		 * 
		 * @Override public void onSuccess() { Picasso.with(getActivity()).
		 * load(Client.getUser().getProfile_pic_url())
		 * 
		 * .into(target);
		 * 
		 * } });
		 */

		imageViewProfile = (CircleImageView) rootView
				.findViewById(R.id.imageViewProfile);
		imageViewUploadPhoto = (ImageView) rootView
				.findViewById(R.id.imageViewUploadPhoto);

		// labels
		labelFirstName = (TextView) rootView.findViewById(R.id.labelFirstName);
		labelLastName = (TextView) rootView.findViewById(R.id.labelLastName);
		labelEmail = (TextView) rootView.findViewById(R.id.labelEmail);
		labelPassword = (TextView) rootView.findViewById(R.id.labelPassword);
		labelNewPassword = (TextView) rootView
				.findViewById(R.id.labelNewPassword);
		labelHeadingName = (TextView) rootView.findViewById(R.id.labelHeadingName);
		// edit text

		editTextFirstName = (EditText) rootView
				.findViewById(R.id.editTextInfoFirstName);

		editTextLastName = (EditText) rootView
				.findViewById(R.id.editTextInfoLasttName);

		editTextEmail = (EditText) rootView
				.findViewById(R.id.editTextInfoEmail);

		editTextPassword = (EditText) rootView
				.findViewById(R.id.editTextInfoPassword);

		editTextNewPassword = (EditText) rootView
				.findViewById(R.id.editTextInfoNewPassword);

		
		imageViewUploadPhoto.setOnClickListener(this);

		buttonEdit = (Button) rootView.findViewById(R.id.buttonEdit);
		buttonEdit.setOnClickListener(this);

		buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
		buttonSave.setOnClickListener(this);
		setValues();
		setDisabledFields();

		profileImageSize = MainActivity.screenWidth < MainActivity.screenHeight ? (MainActivity.screenWidth / 2)
				: (MainActivity.screenHeight / 2);

		Picasso.with(getActivity()).load(Client.getUser().getProfile_pic_url())
				.placeholder(R.drawable.empty_photo)
				.error(R.drawable.empty_photo)
				.resize(profileImageSize, profileImageSize).centerCrop()
				.into(imageViewProfile);
		// MainActivity.imageLoader.loadImage(Client.getUser().getProfile_pic_url(),
		// imageViewProfile, null);

		/*
		 * ArrayList<String> pathList = getImagePaths();
		 * Toast.makeText(getActivity(), "Path list size-->"+ pathList.size(),
		 * Toast.LENGTH_LONG).show(); Log.e("path size-->", ""+pathList.size());
		 * 
		 * Iterator<String> iter = pathList.iterator();
		 * 
		 * while(iter.hasNext()){ String path = iter.next(); Log.e("path---> ",
		 * path); }
		 */

		return rootView;
	}

	private void setEnabledFields() {
		editTextFirstName.setEnabled(true);
		editTextLastName.setEnabled(true);

		editTextPassword.setEnabled(true);
		editTextNewPassword.setEnabled(true);
		buttonEdit.setVisibility(View.GONE);
		buttonSave.setVisibility(View.VISIBLE);
		
		labelPassword.setVisibility(View.VISIBLE);
		labelNewPassword.setVisibility(View.VISIBLE);
		editTextPassword.setVisibility(View.VISIBLE);
		editTextNewPassword.setVisibility(View.VISIBLE);
	}

	private void setDisabledFields() {
		editTextFirstName.setEnabled(false);
		editTextLastName.setEnabled(false);
		editTextPassword.setEnabled(false);
		editTextNewPassword.setEnabled(false);
		editTextEmail.setEnabled(false);
		buttonEdit.setVisibility(View.VISIBLE);
		buttonSave.setVisibility(View.GONE);
		
		labelPassword.setVisibility(View.GONE);
		labelNewPassword.setVisibility(View.GONE);
		editTextPassword.setVisibility(View.GONE);
		editTextNewPassword.setVisibility(View.GONE);
	}

	private void setValues() {
		editTextFirstName.setText(Client.getUser().getFirstname());
		editTextLastName.setText(Client.getUser().getLastname());
		editTextEmail.setText(Client.getUser().getEmail());
		labelHeadingName.setText(Client.getUser().getFirstname()+ " "+ Client.getUser().getLastname());
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));

	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.imageViewUploadPhoto) {
			Toast.makeText(getActivity(), "CLicked", Toast.LENGTH_SHORT).show();
			// imageGalleryManager.openGallery();
			Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK,
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(imageGalleryIntent,
					IMAGE_GALLERY_REQUEST_CODE);
		} else if (v.getId() == R.id.buttonEdit) {
			setEnabledFields();

		} else if (v.getId() == R.id.buttonSave) {
			String firstname = editTextFirstName.getText().toString();
			String lastname = editTextLastName.getText().toString();

			String password = editTextPassword.getText().toString();
			String newPassword = editTextNewPassword.getText().toString();

			if (firstname == null || firstname.equalsIgnoreCase("")) {
				Toast.makeText(getActivity(), "Firstname Required",
						Toast.LENGTH_LONG).show();
			} else if (lastname == null || lastname.equalsIgnoreCase("")) {
				Toast.makeText(getActivity(), "Lastname Required",
						Toast.LENGTH_LONG).show();
			} else if ((newPassword != null && !newPassword
					.equalsIgnoreCase(""))
					&& (password == null || password.equals(""))) {
				Toast.makeText(getActivity(), "Please Enter Current Password",
						Toast.LENGTH_LONG).show();
			} else {
				RequestParams params = new RequestParams();
				params.put(Client.PARAM_USER_ID, Client.getUser().getId()
						.toString());
				params.put(Client.PARAM_SESSION_TOKEN, Client.getUser()
						.getSession_token());
				params.put(Client.PARAM_FIRSTNAME, firstname);
				params.put(Client.PARAM_LASTNAME, lastname);
				if (password != null && newPassword != null) {
					params.put(Client.PARAM_PASSWORD, password);
					params.put(Client.PARAM_NEW_PASSWORD, newPassword);
				}

				Client.post(getActivity(), Client.URL_PROFILE_EDIT, params,
						profileEditResponseHandler);

			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Toast.makeText(getActivity(), "Fragment onactivity", Toast.LENGTH_SHORT)
				.show();
		if (requestCode == IMAGE_GALLERY_REQUEST_CODE) {
			if(data != null && data.getData() != null){
				String path = data.getData().getPath();
				Toast.makeText(getActivity(), "path--> " + path, Toast.LENGTH_LONG)
						.show();
				Log.e("path", path);
				String filePath = getPath(getActivity(),data.getData());
				File file = new File(filePath);
				if (file.exists()) {
					/*
					 * Picasso.with(getActivity()).load(file)
					 * .placeholder(R.drawable.empty_photo)
					 * .error(R.drawable.empty_photo) .resize(profileImageSize,
					 * profileImageSize).centerCrop() .into(imageViewProfile);
					 */

					RequestParams params = new RequestParams();
					params.put("user_id", Client.getUser().getId().toString());
					params.put("session_token", Client.getUser().getSession_token());
					try {
						params.put("profile_pic", file);
					} catch (FileNotFoundException e) { 												// e.printStackTrace(); 
						
					}
					Client.post(getActivity(), Client.URL_UPLOAD_PRO_PIC,
							params, uploadImageResponseHandler);

				}
			}
		

		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	public String getRealPathFromURIs(Uri contentUri) {

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().managedQuery(contentUri, proj, // Which
																		// columns
																		// to
																		// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	AsyncHttpResponseHandler uploadImageResponseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			progress = new SplashProgressDialog(getActivity());
			progress.show();
		};

		public void onSuccess(int statusCode, String response) {
			Gson gson = new Gson();
			User user = gson.fromJson(response, User.class);
			Client.saveSession(getActivity(), user);
			Client.setUser(user);

			Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT)
					.show();
			Log.e("new url--> ", user.getProfile_pic_url());
			Picasso.with(getActivity()).load(Client.getUser().getProfile_pic_url())
			.placeholder(R.drawable.empty_photo)
			.error(R.drawable.empty_photo)
			.resize(profileImageSize, profileImageSize).centerCrop()
			.into(imageViewProfile);
		};

		public void onFailure(Throwable arg0, String arg1) {

		};

		public void onFinish() {
			progress.dismiss();
		};
	};

	private ArrayList<String> getImagePaths() {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor mImageCursor = getActivity().managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, proj, null, null,
				null);

		ArrayList<String> pathArray = new ArrayList<String>();

		mImageCursor.moveToFirst();
		while (!mImageCursor.isAfterLast()) {

			pathArray.add(getRealPathFromCursor(mImageCursor));
			mImageCursor.moveToNext();
		}

		return pathArray;
	}

	private String getRealPathFromCursor(Cursor cursor) {
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		return cursor.getString(column_index);
	}

	private Target target = new Target() {

		@Override
		public void onPrepareLoad(Drawable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onBitmapLoaded(Bitmap bitmap, LoadedFrom arg1) {
			if (bitmap != null) {

				Toast.makeText(getActivity(), "Bitmap not null",
						Toast.LENGTH_SHORT).show();
				float ratio = bitmap.getWidth() / bitmap.getHeight();
				int newWidth;
				int newHieght;
				if (bitmap.getWidth() > MainActivity.screenWidth
						|| bitmap.getHeight() > MainActivity.screenHeight) {
					if (MainActivity.screenWidth < MainActivity.screenHeight) {
						newWidth = MainActivity.screenWidth;
					} else {
						newWidth = MainActivity.screenHeight;
					}
					newHieght = (int) (newWidth / ratio);

					Bitmap newBitmap = Bitmap.createBitmap(newWidth, newHieght,
							Config.ARGB_8888);
					Toast.makeText(getActivity(),
							"new width+" + newWidth + " hi+ " + newHieght,
							Toast.LENGTH_SHORT).show();
					blurManger = new StackBlurManager(newBitmap);
					bitmap.recycle();
					Toast.makeText(
							getActivity(),
							"--> " + newBitmap.getWidth() + " X "
									+ newBitmap.getHeight(), Toast.LENGTH_SHORT)
							.show();
				} else {
					blurManger = new StackBlurManager(bitmap);
					Toast.makeText(
							getActivity(),
							"--> " + bitmap.getWidth() + " X "
									+ bitmap.getHeight(), Toast.LENGTH_SHORT)
							.show();
				}

				blurManger.process(65);
				// String path =
				// Environment.getExternalStorageDirectory()+"/TAssignment/profileBg.PNG";

				imageViewProfileBg.setImageBitmap(blurManger
						.returnBlurredImage());
			}

		}

		@Override
		public void onBitmapFailed(Drawable arg0) {
			// TODO Auto-generated method stub

		}
	};

	AsyncHttpResponseHandler profileEditResponseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			progress = new SplashProgressDialog(getActivity());
			progress.show();
		};

		public void onSuccess(int arg0, String response) {
			Gson gson = new Gson();
			User user = gson.fromJson(response, User.class);
			Client.saveSession(getActivity(), user);
			Client.setUser(user);
			setValues();
			setDisabledFields();
		};

		public void onFailure(Throwable arg0, String response) {
			Gson gson = new Gson();
			try{
				ErrorResponse errorResponse = gson.fromJson(response, ErrorResponse.class);
				if(errorResponse != null){
					if(errorResponse.getMessage() != null){
						Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}catch(Exception e){
				Toast.makeText(getActivity(), "Failed.Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
			}
			
		};

		public void onFinish() {
			progress.dismiss();
		};
	};
	
	public String getRealPathFromURI(Context context, Uri contentUri) {
		  Cursor cursor = null;
		  try { 
		    String[] proj = { MediaStore.Images.Media.DATA };
		    cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		  } finally {
		    if (cursor != null) {
		      cursor.close();
		    }
		  }
		}
	
	
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider
	        if (isExternalStorageDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            if ("primary".equalsIgnoreCase(type)) {
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }

	            // TODO handle non-primary volumes
	        }
	        // DownloadsProvider
	        else if (isDownloadsDocument(uri)) {

	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	            return getDataColumn(context, contentUri, null, null);
	        }
	        // MediaProvider
	        else if (isMediaDocument(uri)) {
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);
	        }
	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	public static String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

}
