package com.technext.tassignment1.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.robotium.solo.Solo;
import com.technext.tassignment1.MainActivity;
import com.technext.tassignment1.R;
import com.technext.tassignment1.fragments.LoginFragment;

public class TestLoginFragment extends ActivityInstrumentationTestCase2<MainActivity>{
	
	private MainActivity mActivity;
	EditText eMail;
	EditText passWord;
	Button login;
	Solo solo;

	public TestLoginFragment(String name) {
		super(MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
	}
	
//	private Fragment startFragment(Fragment fragment) {
//	      FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
//	      transaction.add(R.id.container, fragment, "tag");
//	      transaction.commit();
//	      getInstrumentation().waitForIdleSync();
//	      Fragment frag = mActivity.getSupportFragmentManager().findFragmentByTag("tag");
//	      return frag;
//	    }
	
	public void testLoginFragment(){
		String[] e = {"sohel.technext@gmail.com", "aqib@student.sust.edu"};
		String[] p = {"technext", "hello1234"};
		Toast.makeText(getActivity(), "UNIT TEST COMPLETED...", Toast.LENGTH_LONG).show();
		try {
			solo.wait(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		LoginFragment frag = new LoginFragment(){
//			
//		};
//		Fragment fragment = startFragment(frag);
//		eMail = (EditText)mActivity.findViewById(R.id.editTextEmail);
//		passWord = (EditText)mActivity.findViewById(R.id.editTextPassword);
//		login = (Button)mActivity.findViewById(R.id.buttonLogin);
//		for(int i=0;i<e.length;i++){
//			solo.clearEditText(eMail);
//			solo.clearEditText(passWord);
//			Log.e("TestLoginFrg", "Edit text changed@@S");
//			solo.enterText(eMail, e[i]);
//			solo.enterText(passWord, p[i]);
//			assertTrue(solo.searchButton("Login"));
//			solo.clickOnButton("Login");			
//		}
		
//		solo.waitForActivity("com.technext.tassignment1.MainActivity", 10000);
//		solo.assertCurrentActivity("The activity should be Main Tab", "MainTabActivity");
//		
//		solo.sendKey(Solo.MENU);
//		solo.clickOnText("Logout");
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
