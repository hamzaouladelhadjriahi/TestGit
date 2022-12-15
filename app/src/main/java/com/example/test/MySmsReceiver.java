package com.example.test;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MySmsReceiver extends BroadcastReceiver {

    private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {


        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");

        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            Toast.makeText(context, "ok1", Toast.LENGTH_LONG).show();

            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                strMessage += "SMS from " + msgs[i].getOriginatingAddress();
                strMessage += " :" + msgs[i].getMessageBody() + "\n";
                // Log and display the SMS message.

                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
//                NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"My Notification");
//                builder.setContentTitle("My Title");
//                builder.setContentText("you are sending a msg");
//                builder.setSmallIcon(R.drawable.ic_launcher_background);
//                builder.setAutoCancel(true);
//
//                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(context);
//                managerCompat.notify(1,builder.build());
            }
        }
    }




//    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
//    private static final String TAG = "SMSBroadcastReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Log.i(TAG, "Intent recieved: " + intent.getAction());
//
//        if (intent.getAction().equals( SMS_RECEIVED)) {
//            Toast.makeText(context, "ok1", Toast.LENGTH_LONG).show();
//
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                Toast.makeText(context, "ok2", Toast.LENGTH_LONG).show();
//
//                Object[] pdus = (Object[])bundle.get("pdus");
//                final SmsMessage[] messages = new SmsMessage[pdus.length];
//                for (int i = 0; i < pdus.length; i++) {
//                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
//                }
//                if (messages.length > -1) {
//                    Toast.makeText(context, "ok", Toast.LENGTH_LONG).show();
//                    Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
//
//                }
//            }
//        }
//    }
}