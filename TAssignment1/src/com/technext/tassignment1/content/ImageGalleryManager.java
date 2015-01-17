package com.technext.tassignment1.content;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

/***
 * 
 * This Class is to access image files through Image Gallery apps available in the system.</br>
 * Permission required: <strong>"android.permission.MEDIA_CONTENT_CONTROL" "android.permission.READ_EXTERNAL_STORAGE"</strong></br>
 * Procedure: </br>
 * <ul>
 * <li>Initiate variable in <strong>onCreate(...)</strong> method of your activity with <strong>YourActivityClass.this</strong> parameter.</li>
 * <li>call <strong>ImageGalleryManager instance.openGallery()</strong> method. </li>
 * <li>You will recieve the <strong>Uri</strong> of the image selected in <strong>onActivityResult(...)</strong> method of your activity with request code :  <strong>ImageGalleryManager.IMAGE_GALLERY_REQUEST_CODE</strong></li>
 * </ul>
 * <strong>N.B. :</strong>the value of <strong>IMAGE_GALLERY_REQUEST_CODE</strong> is <strong>12</strong>. Do not use it for another request code while using <strong>ImageGalleryManager</strong></br>
 *
 */

public class ImageGalleryManager {
	
	private Fragment context;
	
	public static final int IMAGE_GALLERY_REQUEST_CODE = 12; 
	
	public ImageGalleryManager(Fragment context){
		this.context = context;
	}
	
	public void openGallery(){
		Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		context.startActivityForResult(imageGalleryIntent, IMAGE_GALLERY_REQUEST_CODE);
	}
}
