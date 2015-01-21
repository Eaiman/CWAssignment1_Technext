package com.technext.tassignment1.fragments;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.listener.OnPostingCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.squareup.picasso.Picasso;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.content.ShareDataContent;

public class ShareContentFragment extends Fragment implements OnRequestSocialPersonCompleteListener, OnClickListener {
//    private String message = "Need simple social networks integration? Check this lbrary:";
//    private String link = "https://github.com/gorbin/ASNE";

    private static final String NETWORK_ID = "NETWORK_ID";
    private SocialNetwork socialNetwork;
    private int networkId;
    private ImageView photo;
    private TextView name;
    private Button share_text, share_link, share_photo;
    private RelativeLayout frame;
    private final int TEXT = 0, LINK = 1, PHOTO = 2; 
    private Bundle postParams = new Bundle();

    public static ShareContentFragment newInstannce(int id) {
    	ShareContentFragment fragment = new ShareContentFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public ShareContentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        networkId = getArguments().containsKey(NETWORK_ID) ? getArguments().getInt(NETWORK_ID) : 0;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Profile");

        View rootView = inflater.inflate(R.layout.profile_fragment, container, false);

        frame = (RelativeLayout) rootView.findViewById(R.id.frame);
        photo = (ImageView) rootView.findViewById(R.id.imageView);
        name = (TextView) rootView.findViewById(R.id.name);
        share_text = (Button) rootView.findViewById(R.id.share_text);
        share_text.setOnClickListener(this);
        
        share_link = (Button) rootView.findViewById(R.id.share_link);
        share_link.setOnClickListener(this);
        
        share_photo = (Button) rootView.findViewById(R.id.share_photo);
        share_photo.setOnClickListener(this);
        colorProfile(networkId);
        
        if(networkId == InstagramSocialNetwork.ID){
        	share_text.setVisibility(View.GONE);
        	share_link.setVisibility(View.GONE);
        }

        socialNetwork = SocialNetworkChooserFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();

        MainActivity.showProgress("Loading social person");
        return rootView;
    }
    
    public SocialNetwork getSocialNetwork(){
    	return socialNetwork;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                socialNetwork.logout();
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        MainActivity.hideProgress();
        name.setText(socialPerson.name);
        Picasso.with(getActivity())
                .load(socialPerson.avatarURL)
                .into(photo);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "oops!!! Something goes wrong", Toast.LENGTH_LONG).show();
    }

    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == 12 && resultCode == Activity.RESULT_OK){
    		Uri selectedImage = data.getData();		
			
			if(networkId == GooglePlusSocialNetwork.ID) {
				ContentResolver cr = getActivity().getContentResolver();
	    	    String mime = cr.getType(selectedImage);
	    	    postParams = new Bundle();
	            postParams.putString(SocialNetwork.BUNDLE_MESSAGE, ShareDataContent.caption);
	            postParams.putString(SocialNetwork.BUNDLE_URI, selectedImage.toString());
	            postParams.putString(SocialNetwork.BUNDLE_MIME_TYPE, mime);
                socialNetwork.requestPostDialog(postParams, postingComplete);
            }else{
            	String filePath = getPath(getActivity(), selectedImage);
            	if(filePath == null){
            		Toast.makeText(getActivity(), "oops!!! Something goes wrong", Toast.LENGTH_LONG).show();
            		return;
            	}
            	File file = new File(filePath);
            	socialNetwork.requestPostPhoto(file, ShareDataContent.caption, postingComplete);
            	if(networkId != InstagramSocialNetwork.ID) {
            		MainActivity.showProgress("Please wait...");
            	}
            }
			
		}
    };

    private OnPostingCompleteListener postingComplete = new OnPostingCompleteListener() {
        @Override
        public void onPostSuccessfully(int socialNetworkID) {
        	MainActivity.hideProgress();
            Toast.makeText(getActivity(), "Successfully Shared", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
        	MainActivity.hideProgress();
            Toast.makeText(getActivity(), "Error while sending: " + errorMessage, Toast.LENGTH_LONG).show();
        }
    };

    private void colorProfile(int networkId){
        int color = getResources().getColor(R.color.dark);
        int image = R.drawable.user;
        switch (networkId) {
            case TwitterSocialNetwork.ID:
                color = getResources().getColor(R.color.twitter);
                image = R.drawable.twitter_user;
                break;
            case GooglePlusSocialNetwork.ID:
                color = getResources().getColor(R.color.googleplus);
                image = R.drawable.user;
                break;
            case FacebookSocialNetwork.ID:
                color = getResources().getColor(R.color.facebook);
                image = R.drawable.com_facebook_profile_picture_blank_square;
                break;
            case InstagramSocialNetwork.ID:
                color = getResources().getColor(R.color.instagram);
                image = R.drawable.user;
                break;
        }
        frame.setBackgroundColor(color);
        name.setTextColor(color);
        photo.setBackgroundColor(color);
        photo.setImageResource(image);
    }

    private AlertDialog.Builder alertDialogInit(String title, String message){
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setCancelable(true);
        return ad;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	        case R.id.share_text:
	            shareContent(TEXT);
	            break;
	        case R.id.share_link:
	            shareContent(LINK);
	            break;
	        case R.id.share_photo:
	        	Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        		startActivityForResult(imageGalleryIntent, 12);
	            break;
	        default:
	        	Toast.makeText(getActivity(), "oops!!! Something goes wrong", Toast.LENGTH_LONG).show();
	            break;
	    }
	}
	
	private void shareContent(final int condition) {
            AlertDialog.Builder ad = null;
            postParams = new Bundle();
            
            if(condition == TEXT){
            	ad = alertDialogInit("Would you like to post Text:", ShareDataContent.message);
            	postParams.putString(SocialNetwork.BUNDLE_MESSAGE, ShareDataContent.message);
            }else if(condition == LINK){
            	ad = alertDialogInit("Would you like to post Link:", ShareDataContent.linkToShare);
	            postParams.putString(SocialNetwork.BUNDLE_CAPTION, ShareDataContent.caption);
	            postParams.putString(SocialNetwork.BUNDLE_MESSAGE, ShareDataContent.message);
	            postParams.putString(SocialNetwork.BUNDLE_LINK, ShareDataContent.linkToShare);
            }else if(condition == PHOTO){
            	Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        		startActivityForResult(imageGalleryIntent, 12);
        		return;
            }
            
            if(ad == null){
            	return;
            }
            
            ad.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	
                    if(networkId == GooglePlusSocialNetwork.ID) {
                        socialNetwork.requestPostDialog(postParams, postingComplete);
                    } else {
                    	MainActivity.showProgress("Please wait...");
                    	if(condition == TEXT){
                    		socialNetwork.requestPostMessage(ShareDataContent.message, postingComplete);
                    	}else if(condition == LINK){
                    		socialNetwork.requestPostLink(postParams, ShareDataContent.message, postingComplete);
                    	}
                    }
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
            ad.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
            ad.create().show();
    }
	
//	@Override
//	public void onAttach(Activity activity) {
//		// TODO Auto-generated method stub
//		super.onAttach(activity);
//		((MainActivity) activity).onSectionAttached(getArguments().getInt(
//				MainActivity.ARG_SECTION_NUMBER));
//
//	}
	
	// ---------------------------------------------------------
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