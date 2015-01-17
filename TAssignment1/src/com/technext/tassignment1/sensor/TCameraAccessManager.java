package com.technext.tassignment1.sensor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

/***
 * 
 * This Class is to capture image through camera apps available in the system.</br>
 * Permission required: <strong>"android.permission.CAMERA" "android.permission.WRITE_EXTERNAL_STORAGE"</strong></br>
 * Uses Feature : <strong>"android.hardware.camera"</strong>. Add this feature in your manifest file.
 * Procedure: </br>
 * <ul>
 * <li>Initiate variable in <strong>onCreate(...)</strong> method of your activity with <strong>YourActivityClass.this</strong> parameter.</li>
 * <li>call <strong>TCameraAccess instance.openCameraApp()</strong> method. </li>
 * <li>in <strong>onActivityResult(...)</strong> method of your activity match the request code with <strong>TCameraAccess.IMAGE_CAPTURE_REQUEST_CODE</strong> and then get location info of the image captured by the camera app.</li>
 * </ul>
 * 
 * Member Methods:</br>
 * <ul>
 * <li><strong>openCameraApp()</strong>: call it to open default camera app available in the system.</li>
 * <li><strong>getImageFile()</strong>: returns the directory file of the image captured.</li>
 * <li><strong>getImagePath()</strong>: returns the directory path of the image captured.</li>
 * <li><strong>getImageFileUri()</strong>: returns the media file uri of the image captured.</li>
 * </ul>
 * <strong>CAUTION: Do not call getImageFileUri(), getImageFile() or getImageFile() method before calling openCameraApp() method. It will give you invalid file location. You will get exceptions if you use it.</strong></br>
 * <strong>N.B. :</strong>the value of <strong>IMAGE_CAPTURE_REQUEST_CODE</strong> is <strong>123</strong>. Do not use it for another request code while using <strong>TCameraAccess</strong></br>
 *
 */

public class TCameraAccessManager {
	
	private Activity context;
	public static final int IMAGE_CAPTURE_REQUEST_CODE = 123;
	private Uri imageFileUri;
	private static String imagePath;
	private static File imageFile;
	
	
	public TCameraAccessManager(Activity context){
		this.context = context;
		imageFileUri = getOutputMediaFileUri();
	}
	
	public File getImageFile(){
		return imageFile;
	}
	
	public Uri getImageFileUri(){
		return imageFileUri;
	}
	
	public String getAbsoluteImagePath(){
		return imagePath;
	}
	
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}

	/** Create a File for saving an image or video */
	@SuppressLint("SimpleDateFormat") 
	private static File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
        "IMG_"+ timeStamp + ".jpg");
        imagePath = mediaFile.getAbsolutePath();
        imageFile = mediaFile;
	    return mediaFile;
	}
	
	public void openCameraApp(){
		imageFileUri = getOutputMediaFileUri();
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		
		context.startActivityForResult(cameraIntent, IMAGE_CAPTURE_REQUEST_CODE);
	}
}

