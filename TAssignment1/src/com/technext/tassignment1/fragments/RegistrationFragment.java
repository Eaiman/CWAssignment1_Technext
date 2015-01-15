package com.technext.tassignment1.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.dialog.SplashProgressDialog;
import com.technext.tassignment1.http.Client;
import com.technext.tassignment1.model.User;

public class RegistrationFragment extends Fragment implements OnClickListener{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private final static String ARG_SECTION_NUMBER = "section_number";
	
	
	EditText editTextEmail;
	EditText editTextPassword;
	EditText editTextConfirmPassword;
	Button buttonRegister;
	
	private RegistrationCompleteListener registrationCompleteListener;
	private SplashProgressDialog progress;
	
	public RegistrationFragment() {
	}
	
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static RegistrationFragment newInstance(int sectionNumber) {
		RegistrationFragment fragment = new RegistrationFragment();
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
		View rootView = inflater.inflate(R.layout.fragment_registration, container,
				false);
		
		
		editTextEmail = (EditText) rootView.findViewById(R.id.editTextEmailReg);
		editTextPassword = (EditText) rootView.findViewById(R.id.editTextPasswordReg);
		editTextConfirmPassword = (EditText) rootView.findViewById(R.id.editTextConfirmPasswordReg);
		buttonRegister = (Button) rootView.findViewById(R.id.buttonRegister);
		
		buttonRegister.setOnClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
		
		if (!(activity instanceof RegistrationCompleteListener)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks registration complete listener.");
		}

		registrationCompleteListener = (RegistrationCompleteListener) activity;
		
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonRegister){
			String email = editTextEmail.getText().toString();
			String password = editTextPassword.getText().toString();
			String confirmPassword = editTextConfirmPassword.getText().toString();
			if(email == null || email.equalsIgnoreCase("")){
				Toast.makeText(getActivity(), "Email Required", Toast.LENGTH_LONG).show();
			}else if(password == null || password.equalsIgnoreCase("")){
				Toast.makeText(getActivity(), "Password Required", Toast.LENGTH_LONG).show();
			}else if(confirmPassword == null || confirmPassword.equalsIgnoreCase("")){
				Toast.makeText(getActivity(), "Please Confirm Password", Toast.LENGTH_LONG).show();
			}else if(!confirmPassword.equals(password)){
				Toast.makeText(getActivity(), "Password doesn't match", Toast.LENGTH_LONG).show();
			}else{
				RequestParams params = new RequestParams();
				params.put(Client.PARAM_EMAIL,email);
				params.put(Client.PARAM_PASSWORD,password);
				params.put(Client.PARAM_PASSWORD_CONFIRMATION,confirmPassword);
				
				Client.raw_post(Client.URL_REGISTER, params, registrationResponseHandler);
			}
		}
		
	}
	
	AsyncHttpResponseHandler registrationResponseHandler = new AsyncHttpResponseHandler(){
		
		
		
		public void onStart() {
			progress = new SplashProgressDialog(getActivity());
            progress.show();
		};
		
		public void onSuccess(String response) {
			
			Toast.makeText(getActivity(), "Registration success", Toast.LENGTH_SHORT);
			Gson gson = new Gson();
			User user = gson.fromJson(response, User.class);
			Client.setUser(user);
			Client.saveSession(getActivity(), user);
			Log.e("User email", user.getEmail());
			Toast.makeText(getActivity(), "User email--> "+ user.getEmail(), Toast.LENGTH_LONG).show();
			registrationCompleteListener.onRegistrationComplete(user);
		};
		
		public void onFailure(Throwable arg0, String response) {
			//Toast.makeText(getActivity(), text, duration)
			Log.e("onfailure response", response);
			Toast.makeText(getActivity(), "Registration Failed", Toast.LENGTH_SHORT).show();
		};
		
		public void onFinish() {
			progress.dismiss();		
		};
		
	};
	
	public interface RegistrationCompleteListener{
		public void onRegistrationComplete(User user);
	}
	
}
