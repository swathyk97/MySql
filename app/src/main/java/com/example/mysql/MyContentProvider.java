package com.example.mysql;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmResults;
import io.realm.RealmSchema;

import static com.example.mysql.TaskColums.*;


public class MyContentProvider extends ContentProvider {


    public static final String URI_PREFIX = "content://com.gadgeon.webcardio.testauthority/";
    public static final String AUTHORITY = "com.gadgeon.webcardio.testauthority";
    public static final Uri URI_DATA = Uri.parse(URI_PREFIX + Data.NAME);
    public static final Uri URI_DATA2 = Uri.parse(URI_PREFIX + Data2.NAME);


    private static final String TAG = MyContentProvider.class.getSimpleName();
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int CONTENT_DATA = 100;
    private static final int CONTENT_DATA2 = 101;


    static {

        URI_MATCHER.addURI(AUTHORITY, Data.NAME, CONTENT_DATA);
        URI_MATCHER.addURI(AUTHORITY, Data2.NAME, CONTENT_DATA2);


    }

    private String getTableName(int match) {
        switch (match) {
            case CONTENT_DATA:
                return Data.NAME;
            case CONTENT_DATA2:
                return Data2.NAME;


        }
        return null;
    }


    @Override
    public boolean onCreate() {


        Realm.init(getContext());
        RealmConfiguration config = new RealmConfiguration.Builder().name("myrealm.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);


        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Realm realm = Realm.getDefaultInstance();
        int match = URI_MATCHER.match(uri);

        MatrixCursor myCursor=null;

        try {
            switch (match) {


                case CONTENT_DATA:
                    RealmResults<Data> tasksRealmResults = realm.where(Data.class).findAll();

                     myCursor = new MatrixCursor(new String[]{CH_1_DATA, CH_2_DATA, String.valueOf(CH_1_COUNT),
                            String.valueOf(CH_2_COUNT)
                    });
                    for (Data myTask : tasksRealmResults) {
                        Object[] rowData = new Object[]{myTask.getCh1Count(), myTask.getCh0Count(), myTask.getChannel1Data()
                                , myTask.getChannel0Data()};
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", myTask.toString());
                    }
                    break;
                case CONTENT_DATA2:
                    RealmResults<Data2> tasksRealmResults1 = realm.where(Data2.class).findAll();
                    for (Data2 myTask : tasksRealmResults1) {
                        myCursor = new MatrixCursor(new String[]{NAME,AGE});
                        Object[] rowData = new Object[]{myTask.getName(),myTask.getAge()};
                        myCursor.addRow(rowData);
                        Log.v("RealmDB", myTask.toString());
                    }
                    break;


                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }


            myCursor.setNotificationUri(getContext().getContentResolver(), uri);
        } finally {
            realm.close();
        }
        return myCursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {


        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable final ContentValues values) {
        Uri returnUri;
        Realm realm = Realm.getDefaultInstance();
        int match = URI_MATCHER.match(uri);
        try {
            switch (match) {


                case CONTENT_DATA:
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Number currId = realm.where(Data.class).max(ID);
                            Integer nextId = (currId == null) ? 1 : currId.intValue() + 1;
                            Data myNewTask = realm.createObject(Data.class,nextId);
                            myNewTask.setChannel1Data(values.get(CH_1_DATA).toString());
                            myNewTask.setChannel0Data(values.get(CH_2_DATA).toString());
                            myNewTask.setCh1Count((short) values.get(String.valueOf(CH_1_COUNT)));
                            myNewTask.setCh0Count((short) values.get(String.valueOf(CH_2_COUNT)));
                        }
                    });
                    returnUri = ContentUris.withAppendedId(URI_DATA, '1');
                    break;
                case CONTENT_DATA2:
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            Data2 myNewTask = realm.createObject(Data2.class);
                            myNewTask.setName(values.get(NAME).toString());
                            myNewTask.setAge(values.get(AGE).toString());
                            ;
                        }
                    });
                    returnUri = ContentUris.withAppendedId(URI_DATA2, '1');
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            getContext().getContentResolver().notifyChange(uri, null);
        } finally {
            realm.close();
        }
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Realm realm = Realm.getDefaultInstance();
        int match = URI_MATCHER.match(uri);
        int count = 0;

        try {
            switch (match) {
                case CONTENT_DATA:

                    RealmResults<Data> tasksRealmResults = realm.where(Data.class).findAll();
                    realm.beginTransaction();
                    tasksRealmResults.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;
                case CONTENT_DATA2:

                    RealmResults<Data2> tasksRealmResults2 = realm.where(Data2.class).findAll();
                    realm.beginTransaction();
                    tasksRealmResults2.deleteAllFromRealm();
                    count++;
                    realm.commitTransaction();
                    break;

                default:
                    throw new IllegalArgumentException("Illegal delete URI");
            }
        } finally {
            realm.close();
        }
        if (count > 0) {
            //Notify observers of the change
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Realm realm = Realm.getDefaultInstance();
        int match = URI_MATCHER.match(uri);

        int nrUpdated = 0;
        try {
            switch (match) {
                case CONTENT_DATA:

                    RealmResults<Data> myTask = realm.where(Data.class).findAll();
                    realm.beginTransaction();
                    for (Data my : myTask) {
                        my.setChannel0Data((values.get((CH_1_DATA)).toString()));
                        my.setChannel1Data((values.get((CH_2_DATA)).toString()));

                    }
                    nrUpdated++;
                    realm.commitTransaction();
                    break;
                case CONTENT_DATA2:

                    RealmResults<Data2> myTask2 = realm.where(Data2.class).findAll();
                    realm.beginTransaction();
                    for (Data2 my : myTask2) {
                        my.setName((values.get((NAME)).toString()));
                        my.setAge((values.get((AGE)).toString()));

                    }
                    nrUpdated++;
                    realm.commitTransaction();
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }


        } finally {
            realm.close();
        }

        if (nrUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return nrUpdated;

    }

    class MyRealmMigration implements RealmMigration {


        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            if (oldVersion != 0) {

                final RealmObjectSchema data =
                        schema.get("Data");
                if(data==null) {
                    data.addField("id", int.class);

                }
                oldVersion++;
            }
        }
    }
}






