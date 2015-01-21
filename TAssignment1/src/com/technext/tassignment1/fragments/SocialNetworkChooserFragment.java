package com.technext.tassignment1.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.googleplus.GooglePlusSocialNetwork;
import com.github.gorbin.asne.instagram.InstagramSocialNetwork;
import com.github.gorbin.asne.twitter.TwitterSocialNetwork;
import com.squareup.picasso.Picasso;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.content.ShareDataContent;
import com.technext.tassignment1.fragments.LoginFragment.LoginSuccessListener;
import com.technext.tassignmet1.share.TMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocialNetworkChooserFragment extends Fragment implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener {
    public static SocialNetworkManager mSocialNetworkManager;
    /**
     * SocialNetwork Ids in ASNE:
     * 1 - Twitter
     * 2 - LinkedIn
     * 3 - Google Plus
     * 4 - Facebook
     * 5 - Vkontakte
     * 6 - Odnoklassniki
     * 7 - Instagram
     */
    private ImageView facebook;
    private ImageView twitter;
    private ImageView googleplus;
    private ImageView instagram;
    private ImageView smsView;
    private ImageView emailView;
    
    private TMessaging tMessaging;

    public SocialNetworkChooserFragment() {
    }
    
    public static SocialNetworkChooserFragment newInstance(int sectionNumber) {
    	SocialNetworkChooserFragment fragment = new SocialNetworkChooserFragment();
		Bundle args = new Bundle();
		args.putInt(MainActivity.ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.app_name);

        tMessaging = new TMessaging(getActivity());
        
        // init buttons and set Listener
        facebook = (ImageView) rootView.findViewById(R.id.facebook);
        twitter = (ImageView) rootView.findViewById(R.id.twitter);
        googleplus = (ImageView) rootView.findViewById(R.id.googleplus);
        instagram = (ImageView) rootView.findViewById(R.id.instagram);
        smsView = (ImageView) rootView.findViewById(R.id.sms);
        emailView = (ImageView) rootView.findViewById(R.id.mail);
        
        Picasso picasso = Picasso.with(getActivity());
        picasso.load(R.raw.share_facebook).into(facebook);
        picasso.load(R.raw.share_twitter).into(twitter);
        picasso.load(R.raw.share_googleplus).into(googleplus);
        picasso.load(R.raw.share_instagram).into(instagram);
        picasso.load(R.raw.share_sms).into(smsView);
        picasso.load(R.raw.share_mail).into(emailView);
        
        facebook.setOnClickListener(loginClick);
        twitter.setOnClickListener(loginClick);
        googleplus.setOnClickListener(loginClick);
        instagram.setOnClickListener(loginClick);
        smsView.setOnClickListener(loginClick);
        emailView.setOnClickListener(loginClick);

        //Get Keys for initiate SocialNetworks
        String TWITTER_CONSUMER_KEY = getActivity().getString(R.string.twitter_consumer_key);
        String TWITTER_CONSUMER_SECRET = getActivity().getString(R.string.twitter_consumer_secret);
        String TWITTER_CALLBACK_URL = "oauth://ASNE";
        String INSTAGRAM_CLIENT_ID = getActivity().getString(R.string.instagram_client_id);
        String INSTAGRAM_CLIENT_SECRET = getActivity().getString(R.string.instagram_client_secret);

        //Chose permissions
        ArrayList<String> fbScope = new ArrayList<String>();
        fbScope.addAll(Arrays.asList("public_profile, email"));
//        String linkedInScope = "r_basicprofile+r_fullprofile+rw_nus+r_network+w_messages+r_emailaddress+r_contactinfo";

        //Use manager to manage SocialNetworks
        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(MainActivity.SOCIAL_NETWORK_TAG);

        //Check if manager exist
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager FacebookSocialNetwork
            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
            mSocialNetworkManager.addSocialNetwork(fbNetwork);

            //Init and add to manager TwitterSocialNetwork
            TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET, TWITTER_CALLBACK_URL);
            mSocialNetworkManager.addSocialNetwork(twNetwork);

            //Init and add to manager GooglePlusSocialNetwork
            GooglePlusSocialNetwork gpNetwork = new GooglePlusSocialNetwork(this);
            mSocialNetworkManager.addSocialNetwork(gpNetwork);
            
            // Init and add to manager GooglePlusSocialNetwork
            InstagramSocialNetwork instagramNetwork = new InstagramSocialNetwork(this, INSTAGRAM_CLIENT_ID, INSTAGRAM_CLIENT_SECRET, TWITTER_CALLBACK_URL, null);
            mSocialNetworkManager.addSocialNetwork(instagramNetwork);

            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, MainActivity.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                    initSocialNetwork(socialNetwork);
                }
            }
        }
        return rootView;
    }
    
    public SocialNetworkManager getSocialNetworkManager(){
    	return mSocialNetworkManager;
    }

    private void initSocialNetwork(SocialNetwork socialNetwork){
//        if(socialNetwork.isConnected()){
//            switch (socialNetwork.getID()){
//                case FacebookSocialNetwork.ID:
//                    facebook.setText("Show Facebook profile");
//                    break;
//                case TwitterSocialNetwork.ID:
//                    twitter.setText("Show Twitter profile");
//                    break;
//                case GooglePlusSocialNetwork.ID:
//                    googleplus.setText("Show GooglePlus profile");
//                    break;
//                case InstagramSocialNetwork.ID:
//                    instagram.setText("Show Instagram profile");
//                    break;
//            }
//        }
    }
    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
            initSocialNetwork(socialNetwork);
        }
    }

    //Login listener

    private View.OnClickListener loginClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int networkId = -1;
            switch (view.getId()){
                case R.id.facebook:
                    networkId = FacebookSocialNetwork.ID;
                    break;
                case R.id.twitter:
                    networkId = TwitterSocialNetwork.ID;
                    break;
                case R.id.googleplus:
                    networkId = GooglePlusSocialNetwork.ID;
                    break;
                case R.id.instagram:
                    networkId = InstagramSocialNetwork.ID;
                    break;
            }
            
            if(networkId != -1){
	            SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
	            if(!socialNetwork.isConnected()) {
	                if(networkId != 0) {
	                    socialNetwork.requestLogin();
	                    MainActivity.showProgress("Loading social person");
	                } else {
	                    Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
	                }
	            } else {
	                startProfile(socialNetwork.getID());
	            }
            }else{
            	switch (view.getId()){
                case R.id.sms:
                	// SMS Share
                	tMessaging.sendMessage(ShareDataContent.message);
                    break;
                    
                case R.id.mail:
                	// Mail share
                	tMessaging.sendEmail("", "technext", "testing");
                    break;
            	}
            }
        }
    };

    @Override
    public void onLoginSuccess(int networkId) {
        MainActivity.hideProgress();
        startProfile(networkId);
    }

    @Override
    public void onError(int networkId, String requestID, String errorMessage, Object data) {
        MainActivity.hideProgress();
        Toast.makeText(getActivity(), "ERROR: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    private void startProfile(int networkId){
        ShareContentFragment profile = ShareContentFragment.newInstannce(networkId);
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack("profile")
                .replace(R.id.container, profile)
                .commit();
    }
    
    @Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				MainActivity.ARG_SECTION_NUMBER));

	}
    
    private AlertDialog.Builder alertDialogInit(String title, String message){
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setCancelable(true);
        return ad;
    }
}