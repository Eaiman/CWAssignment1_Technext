package com.technext.tassignmet1.share;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

/***
 * 
 * this class needs SEND_SMS permission in manifest file
 *
 */

public class SMSShare {
	protected String SENT = "SENT";
	protected String RECIEVED = "RECIEVED";
	private Context context;
	private SmsManager smsManager;
	
	public SMSShare(Activity activity){
		this.context = activity;
		smsManager = SmsManager.getDefault();
	}
	
	public void sendMessage(String number, String message){
		//Toast.makeText(context, "hello from sendMessage", Toast.LENGTH_SHORT).show();
		PendingIntent sentIntent;
		PendingIntent deliveryIntent;
		sentIntent = PendingIntent.getBroadcast(context, 0, new Intent(SENT), PendingIntent.FLAG_UPDATE_CURRENT);
		deliveryIntent = PendingIntent.getBroadcast(context, 0, new Intent(RECIEVED), PendingIntent.FLAG_UPDATE_CURRENT);
		context.registerReceiver(new SMSBroadcastReciever(), new IntentFilter(SENT));
		context.registerReceiver(new SMSBroadcastReciever(), new IntentFilter(RECIEVED));
		smsManager.sendTextMessage(number, null, message, sentIntent, deliveryIntent);
	}
	
	public void sendMessage(String message){
		Intent sendSmsIntent = new Intent();
		sendSmsIntent.setAction(Intent.ACTION_SEND);
		sendSmsIntent.putExtra(Intent.EXTRA_TEXT, message);
		sendSmsIntent.setType(HTTP.PLAIN_TEXT_TYPE);
		
		if(sendSmsIntent.resolveActivity(context.getPackageManager()) != null){
			context.startActivity(sendSmsIntent);
		}
	}
	
	public class SMSBroadcastReciever extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			switch(intent.getAction()){
			case "SENT":
				//Toast.makeText(context, "hello from reciever : sent", Toast.LENGTH_SHORT).show();
				switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
				break;
			case "RECIEVED":
				//Toast.makeText(context, "hello from reciever : recieved", Toast.LENGTH_SHORT).show();
				switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
				break;
			default:
				break;
			}
		}
		
	}
}
