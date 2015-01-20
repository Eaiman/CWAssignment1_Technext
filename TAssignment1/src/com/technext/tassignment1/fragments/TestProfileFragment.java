package com.technext.tassignment1.fragments;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.content.ImageGalleryManager;
import com.technext.tassignment1.http.Client;

public class TestProfileFragment extends Fragment implements OnRequestSocialPersonCompleteListener {
    private String message = "Need simple social networks integration? Check this lbrary:";
    private String link = "https://github.com/gorbin/ASNE";

    private static final String NETWORK_ID = "NETWORK_ID";
    private SocialNetwork socialNetwork;
    private int networkId;
    private ImageView photo;
    private TextView name;
    private TextView id;
    private TextView info;
    private Button friends;
    private Button share;
    private RelativeLayout frame;

    public static TestProfileFragment newInstannce(int id) {
    	TestProfileFragment fragment = new TestProfileFragment();
        Bundle args = new Bundle();
        args.putInt(NETWORK_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    public TestProfileFragment() {
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
        id = (TextView) rootView.findViewById(R.id.id);
        info = (TextView) rootView.findViewById(R.id.info);
        friends = (Button) rootView.findViewById(R.id.friends);
        friends.setOnClickListener(friendsClick);
        share = (Button) rootView.findViewById(R.id.share);
        share.setOnClickListener(shareClick);
        colorProfile(networkId);

        socialNetwork = TestMainFragment.mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();

        MainActivity.showProgress("Loading social person");
        return rootView;
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
        id.setText(socialPerson.id);
        String socialPersonString = socialPerson.toString();
        String infoString = socialPersonString.substring(socialPersonString.indexOf("{")+1, socialPersonString.lastIndexOf("}"));
        info.setText(infoString.replace(", ", "\n"));
        Picasso.with(getActivity())
                .load(socialPerson.avatarURL)
                .into(photo);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Log.e("Person requestID", requestID);
        Log.e("Person errorMessage", errorMessage);
        Log.e("Person data", data.toString());
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    private View.OnClickListener friendsClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            FriendsFragment friends = FriendsFragment.newInstannce(networkId);
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .addToBackStack("friends")
//                    .replace(R.id.container, friends)
//                    .commit();
        	
        	Toast.makeText(getActivity(), "Show FriendList Clicked", Toast.LENGTH_SHORT).show();;
        }
    };

    private View.OnClickListener shareClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder ad = alertDialogInit("Would you like to post Link:", link);
            ad.setPositiveButton("Post link", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Bundle postParams = new Bundle();
                    postParams.putString(SocialNetwork.BUNDLE_NAME, "Simple and easy way to add social networks for android application");
                    postParams.putString(SocialNetwork.BUNDLE_LINK, link);
                    if(networkId == GooglePlusSocialNetwork.ID) {
                        socialNetwork.requestPostDialog(postParams, postingComplete);
                    }if(networkId == InstagramSocialNetwork.ID) {
                    	Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                		startActivityForResult(imageGalleryIntent, 12);
                        //socialNetwork.requestPostPhoto(postParams, postingComplete);
                    } else {
                        socialNetwork.requestPostLink(postParams, message, postingComplete);
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
    };
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode == 12){
			Toast.makeText(getActivity(), "On activity result--> "+ resultCode, Toast.LENGTH_SHORT).show();
			String path = data.getData().getPath();
			Toast.makeText(getActivity(), "path--> "+path, Toast.LENGTH_LONG).show();
			Log.e("path", path);
			String filePath = getRealPathFromURI(data.getData());
			Toast.makeText(getActivity(), "path + "+ filePath, Toast.LENGTH_SHORT).show();
			File file = new File(filePath);
			
			socialNetwork.requestPostPhoto(file, "CWC Test", postingComplete);
		}
    };
    
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

    private OnPostingCompleteListener postingComplete = new OnPostingCompleteListener() {
        @Override
        public void onPostSuccessfully(int socialNetworkID) {
            Toast.makeText(getActivity(), "Sent", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(int socialNetworkID, String requestID, String errorMessage, Object data) {
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
        friends.setBackgroundColor(color);
        share.setBackgroundColor(color);
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
}