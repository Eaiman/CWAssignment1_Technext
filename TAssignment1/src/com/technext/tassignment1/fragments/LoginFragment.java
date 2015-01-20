package com.technext.tassignment1.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.plus.PlusShare;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.dialog.SplashProgressDialog;
import com.technext.tassignment1.fragments.RegistrationFragment.RegistrationCompleteListener;
import com.technext.tassignment1.http.Client;
import com.technext.tassignment1.model.User;
import com.technext.tassignment1.utils.CommonUtils;

public class LoginFragment extends Fragment implements OnClickListener{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private final static String ARG_SECTION_NUMBER = "section_number";
	
	
	
	private EditText editTextEmail;
	private EditText editTextPassword;
	private Button buttonLogin;
	
	private LoginSuccessListener loginSuccessListener;
	private SplashProgressDialog progress;
	
	public LoginFragment(){
		
	}
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static LoginFragment newInstance(int sectionNumber) {
		LoginFragment fragment = new LoginFragment();
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
		View rootView = inflater.inflate(R.layout.fragment_login, container,
				false);
		
		editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmail);
		editTextPassword = (EditText) rootView.findViewById(R.id.editTextPassword);
		buttonLogin = (Button) rootView.findViewById(R.id.buttonLogin);
		buttonLogin.setOnClickListener(this);
		
		
		Button shareButton = (Button) rootView.findViewById(R.id.share_button);
		shareButton.setOnClickListener(new OnClickListener() {
		    @Override
		    public void onClick(View v) {
		      // Launch the Google+ share dialog with attribution to your app.
		      Intent shareIntent = new PlusShare.Builder(getActivity())
		          .setType("text/plain")
		          .setText("Welcome to the Google+ platform.")
		          .setContentUrl(Uri.parse("https://developers.google.com/+/"))
		          .getIntent();

		      startActivityForResult(shareIntent, 0);
		    }
		});
		
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
		
		if (!(activity instanceof LoginSuccessListener)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks login success listener.");
		}

		loginSuccessListener = (LoginSuccessListener) activity;

	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonLogin){
			String email = editTextEmail.getText().toString();
			String password = editTextPassword.getText().toString();
			if(email == null || email.equalsIgnoreCase("") || !CommonUtils.isValidEmail(email)){
				Toast.makeText(getActivity(), "Please Provide a valid Email Address", Toast.LENGTH_SHORT).show();
			}else if(password == null || password.equalsIgnoreCase("")){
				Toast.makeText(getActivity(), "Password Required", Toast.LENGTH_SHORT).show();
			}else{
				RequestParams params = new RequestParams();
				params.put(Client.PARAM_EMAIL, email);
				params.put(Client.PARAM_PASSWORD, password);
				Client.raw_post(Client.URL_LOGIN, params, loginResponseHandler);
			}
		}
		
	}
	
	AsyncHttpResponseHandler loginResponseHandler = new AsyncHttpResponseHandler(){
		
		public void onStart() {
			progress = new SplashProgressDialog(getActivity());
            progress.show();
		};
		
		public void onSuccess(int statusCode, String response) {
			Toast.makeText(getActivity(), "Login success", Toast.LENGTH_SHORT).show();
			Gson gson = new Gson();
			User user = gson.fromJson(response, User.class);
			Client.setUser(user);
			Client.saveSession(getActivity(), user);
			Log.e("User email", user.getEmail());
			Toast.makeText(getActivity(), "User email--> "+ user.getEmail(), Toast.LENGTH_LONG).show();
			loginSuccessListener.onloginComplete(user);
		};
		
		public void onFailure(Throwable arg0, String arg1) {
			Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
		};
		
		public void onFinish() {
			progress.dismiss();
		};
		
	};
	
	public interface LoginSuccessListener{
		public void onloginComplete(User user);
	}
}
