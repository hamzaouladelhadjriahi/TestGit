package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =0 ;
    Button sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    String phoneNo;
    String message;
    TextView textViewNumbers;
    EditText nameEnter;
    boolean etat=false;
    RecyAdapt adapter;
    TextView textView;
    boolean check=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(MainActivity.this,MainActivity3.class);
        startActivity(intent);
        MySmsReceiver receiver=new MySmsReceiver();
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(receiver,intentFilter);

        sendBtn = (Button) findViewById(R.id.btnSendSMS);
        txtphoneNo = (EditText) findViewById(R.id.editText1);
        txtMessage = (EditText) findViewById(R.id.editText2);
        nameEnter=findViewById(R.id.edit2);
        textView=findViewById(R.id.textView1);



        RecyclerView recyclerView=findViewById(R.id.myrecy);





        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.READ_SMS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        }
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)!=PackageManager.PERMISSION_GRANTED){
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_CONTACTS)){
//                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
//            }
//        }



        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                phoneNo = txtphoneNo.getText().toString();
                message = txtMessage.getText().toString();
                sending();



            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                 adapter=new RecyAdapt( getContact(),txtphoneNo);
                recyclerView.setItemViewCacheSize(getContact().size());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));


            }
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
               etat=true;
            }
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check==false){
                   adapter.selectAll();
                   check=true;
                   Toast.makeText(MainActivity.this,String.valueOf(check),Toast.LENGTH_SHORT).show();
                }
                else{
                    adapter.unselectall();
                    check=false;
                    Toast.makeText(MainActivity.this,String.valueOf(check),Toast.LENGTH_SHORT).show();

                }
            }
        });



        nameEnter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable);
            }
        });



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    etat = true;
                    getContact();
//                    textViewNumbers.append("\n"+String.valueOf(grantResults.length));
//                    textViewNumbers.append("\n"+String.valueOf(grantResults[0]));
//                    textViewNumbers.append("\n"+String.valueOf(grantResults[1]));

                } else {
                    Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();

                }
            }
        }
    }
    public static   void nottify(){
//                NotificationCompat.Builder builder=new NotificationCompat.Builder(MainActivity.this,"My Notification");
//                builder.setContentTitle("My Title");
//                builder.setContentText("you are sending a msg");
//                builder.setSmallIcon(R.drawable.ic_launcher_background);
//                builder.setAutoCancel(true);
//
//                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(MainActivity.this);
//                managerCompat.notify(1,builder.build());
//        Toast.makeText(MainActivity.this, "SMS sent.", Toast.LENGTH_LONG).show();

    }



    private void sending() {
        if (etat) {
            if (!phoneNo.equals("") && !message.equals("")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
            }
        }
    }
    private List<Cont>   getContact(){
        List<Cont> contList=new ArrayList<Cont>();
        Cont cont;
        HashMap<String,String> conta=new HashMap<>();
        ContentResolver contentResolver=getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor=contentResolver.query(uri,null,null,null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
        if (cursor.getCount()>=0){
            while (cursor.moveToNext()){
                String name= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                conta.put(name,number);
                cont=new Cont(name,number);
                contList.add(cont);


            }

        }
    return contList;
    }


}

