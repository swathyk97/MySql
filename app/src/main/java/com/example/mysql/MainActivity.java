package com.example.mysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    Realm realm;
    DBHelper db;
    public short ch1Count=500;
    public short ch0Count=500;
    String data1 = "00000100020003000400050006000700080009000a000b000c000d000e000f0010001100120013001400150016001700180019001a001b001c001d001e001f0020002100220023002400250026002700280029002a002b002c002d002e002f0030003100320033003400350036003700380039003a003b003c003d003e003f0040004100420043004400450046004700480049004a004b004c004d004e004f0050005100520053005400550056005700580059005a005b005c005d005e005f0060006100620063006400650066006700680069006a006b006c006d006e006f00700071007200730074007500760077007800790000";
    byte[] channelData = data1.getBytes();
    private Button mBtnNext,mBtnFetch,mBtnDelete,update;
    private Button mBtnNext2,mBtnFetch2,mBtnDelete2,update2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db=new DBHelper(this);
       init();
        Realm.init(getApplicationContext());
        realm=Realm.getDefaultInstance();

        Log.d(TAG, "begin");
       //for(int x = 1; x <= 2000; x = x+1) {
            boolean insert = db.insertRecord(data1, data1, ch0Count, ch1Count);

            if (insert == true) {
                Toast.makeText(getApplicationContext(), "inserted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "inserted");
          // }

       }
        Log.d(TAG, "end");




            Cursor rs = db.getData();

                rs.moveToLast();
                    Log.d(TAG, "channel0" + rs.getString(0));
                    Log.d(TAG, "channel1" + rs.getString(1));
                    Log.d(TAG, "count0" + rs.getString(2));
                    Log.d(TAG, "count1" + rs.getString(3));






    }

    private void init() {
        mBtnNext=findViewById(R.id.button);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(TaskColums.CH_1_DATA,data1);
                values.put( TaskColums.CH_2_DATA,data1);
                values.put(String.valueOf(TaskColums.CH_1_COUNT),ch1Count);
                values.put(String.valueOf(TaskColums.CH_2_COUNT),ch0Count);


                Uri uri = getContentResolver().insert(
                        MyContentProvider.URI_DATA, values);
                Log.d(TAG, "inserted"+uri);
                Toast.makeText(getApplicationContext(),"inserted" , Toast.LENGTH_SHORT).show();
            }
        });
        mBtnNext2=findViewById(R.id.button5);
        mBtnNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(TaskColums.NAME,"saranya");
                values.put( TaskColums.AGE,"33");



                Uri uri = getContentResolver().insert(
                        MyContentProvider.URI_DATA2, values);
                Log.d(TAG, "inserted"+uri);
                Toast.makeText(getApplicationContext(),"inserted" , Toast.LENGTH_SHORT).show();
            }
        });
        mBtnFetch2=findViewById(R.id.button6);
        mBtnFetch2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String projection[]={TaskColums.NAME,TaskColums.AGE};
                Cursor mCursor = getContentResolver().query(MyContentProvider.URI_DATA2, projection , null, null);
                Log.d(TAG,"swathy"+mCursor);
                List<String> words = new ArrayList<>();
                List<String> array = new ArrayList<>();
                if(mCursor!=null && mCursor.moveToFirst()){
                    do {

                        String word = mCursor.getString(mCursor.getColumnIndex(String.valueOf(TaskColums.NAME)));
                        String arr = mCursor.getString(mCursor.getColumnIndex(String.valueOf(TaskColums.AGE)));
                        Log.d(TAG,"word"+word);
                        Log.d(TAG, "arr"+arr);
                        words.add(word);
                        array.add(arr);
                        Toast.makeText(MainActivity.this, "inside the cursor ", Toast.LENGTH_LONG).show();

                    }  while(mCursor.moveToNext());

                }else{
                    Toast.makeText(MainActivity.this, "Nothing is inside the cursor ", Toast.LENGTH_LONG).show();
                }
                mCursor.close();
            }
        });

      mBtnFetch=findViewById(R.id.button2); mBtnFetch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                String projection[]={TaskColums.CH_1_DATA,TaskColums.CH_2_DATA, String.valueOf(TaskColums.CH_1_COUNT), String.valueOf(TaskColums.CH_2_COUNT)};
                Cursor mCursor = getContentResolver().query(MyContentProvider.URI_DATA, projection , null, null);
                Log.d(TAG,"swathy"+mCursor);
                List<String> words = new ArrayList<>();
                List<String> array = new ArrayList<>();
                if(mCursor!=null && mCursor.moveToFirst()){
                    do {

                        String word = mCursor.getString(mCursor.getColumnIndex(String.valueOf(TaskColums.CH_1_COUNT)));
                        String arr = mCursor.getString(mCursor.getColumnIndex(String.valueOf(TaskColums.CH_2_COUNT)));
                        Log.d(TAG,"word"+word);
                        Log.d(TAG, "arr"+arr);
                        words.add(word);
                        array.add(arr);
                        Toast.makeText(MainActivity.this, "inside the cursor ", Toast.LENGTH_LONG).show();

                    }  while(mCursor.moveToNext());

                }else{
                    Toast.makeText(MainActivity.this, "Nothing is inside the cursor ", Toast.LENGTH_LONG).show();
                }
                mCursor.close();
            }
        });

        mBtnDelete=findViewById(R.id.button3);
        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(String.valueOf(MyContentProvider.URI_DATA));
                getContentResolver().delete(uri, null, null);
                Log.d(TAG,"swathy"+uri);


            }
        });

        mBtnDelete2=findViewById(R.id.button7);
        mBtnDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(String.valueOf(MyContentProvider.URI_DATA2));
                getContentResolver().delete(uri, null, null);
                Log.d(TAG,"swathy2"+uri);


            }
        });

        update=findViewById(R.id.button4);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(TaskColums.CH_1_DATA,"swathy");
                values.put( TaskColums.CH_2_DATA,"kutty");
                values.put(String.valueOf(TaskColums.CH_1_COUNT),800);
                values.put(String.valueOf(TaskColums.CH_2_COUNT),800);


                int uri = getContentResolver().update(
                        MyContentProvider.URI_DATA, values,null,null);
                Log.d(TAG, "updated"+uri);
                Toast.makeText(getApplicationContext(),"updated" , Toast.LENGTH_SHORT).show();


            }
        });
        update2=findViewById(R.id.button8);
        update2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(TaskColums.NAME,"reshma");
                values.put( TaskColums.AGE,"55");

                int uri = getContentResolver().update(
                        MyContentProvider.URI_DATA2, values,null,null);
                Log.d(TAG, "updated"+uri);
                Toast.makeText(getApplicationContext(),"updated" , Toast.LENGTH_SHORT).show();


            }
        });
      }


}
