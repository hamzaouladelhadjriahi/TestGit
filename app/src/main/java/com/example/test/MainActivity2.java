package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.StringTokenizer;

public class MainActivity2 extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.textmsgs);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            if (checkSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                getAllSms(this);
            }
        }
    }
    public void getAllSms(Context context) {

        String smsDate;
        StringTokenizer tokenizer;
        String number;
        String body;
        Date dateFormat;
        String type;
        String id;
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {

            totalSMS = c.getCount();
            if (c.moveToFirst()) {


                for (int j = 0; j < totalSMS; j++) {
                    smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    dateFormat= new Date(Long.valueOf(smsDate));

                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sente";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }
                    c.moveToNext();
                    if(number.equals("Secure")) {
                        tokenizer=new StringTokenizer(body," ");
                        while (tokenizer.hasMoreElements()) {
                            String st = tokenizer.nextToken();
                            if (st.equals("est")){
                            id=tokenizer.nextToken();
                            textView.append(String.valueOf(id));

                                break;
                            }
                        }

//                            textView.append(number + " : " + body + " ( " + String.valueOf(dateFormat) + " )" + "\n\n");

                        break;
                    }
                }
            }

            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
    }
}