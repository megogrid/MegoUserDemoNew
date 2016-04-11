package com.megogrid.meuser;

import com.megogrid.activities.MegoUserSDK_;
import com.megogrid.activities.ProfileDetailsResponse;
import com.megogrid.activities.UserProfileDetails;
import com.megogrid.beans.ProfileCustomField;
import com.megogrid.megouser.MegoUserSDK;
import com.megogrid.megouser.sdkinterfaces.IAdvanceHandler;
import com.megogrid.megouser.sdkinterfaces.IResponseHandler;
import com.megogrid.megouser.sdkinterfaces.IUserDetail;
import com.megogrid.megouser.sdkinterfaces.UserDetails;
import com.megogrid.meuser.R;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.megogrid.megouser.MegoUser;
import com.megogrid.megouser.MegoUserConfig;
import com.megogrid.megouser.MegoUserException;
import com.megogrid.meuser.db.ImageDetail;
import com.megogrid.meuser.db.SavedDbHelper;
import com.megogrid.meuserdemo1.library.Constants;
import com.megogrid.meuserdemo1.library.Toaster;
import com.megogrid.meuserdemo1.library.UriToUrl;

import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends Activity {

    private Animation animation;
    private RelativeLayout top_holder;
    private RelativeLayout bottom_holder;
    private MegoUser megoUser;

    private RelativeLayout step_number;
    private TextView account, FileBackup,
            FileRestore, PrefBackup, PrefRestore, Configuration, Settings, Help, logout;
    private ImageView logout_line;
    private Uri imageUri;
    private boolean click_status = true;
    private CustomDrawerLayout drawer_layout;
    private ImageView imageView_back;
    private RelativeLayout whatYouWantInLeftDrawer;
    private static final String YOUR_BOX_ID = "KPDFC20UD";

    private static String GOOGLE_ID = " 720282980579-smn98g7o513htrhf6nnapth4t6bfgklu.apps.googleusercontent.com";
    private static final int FILE_CHOOSER = 3;
    private static final int VERSION_CHOOSER = 9;
    private static final String PREFERENCE_NAME = "AppBackup";
    private SavedDbHelper helper;
    private ArrayList<ImageDetail> detail;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private final int pref_backup_version = 1;
    private final int file_backup_version = 2;
    private String filetype = null;
    private String fileurl = null;
    private CheckPermission checkPermission;
    MegoUserSDK_ megoUserSDK;
    ArrayList<String> str;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("<<< oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        str = new ArrayList<String>();
        str.add(Manifest.permission.INTERNET);
        str.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        str.add(Manifest.permission.ACCESS_FINE_LOCATION);
        str.add(Manifest.permission.ACCESS_NETWORK_STATE);
        str.add(Manifest.permission.ACCESS_WIFI_STATE);
        str.add(Manifest.permission.CHANGE_WIFI_STATE);
        str.add(Manifest.permission.GET_ACCOUNTS);
        str.add(Manifest.permission.RECEIVE_SMS);
        if (getIntent().getBooleanExtra("Exit me", false)) {
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }

//        megoUserSDK = MegoUserSDK_.getInstance(MainActivity.this, new IAdvanceHandler() {
//            @Override
//            public void onResponse(MegoUserSDK.MegoUserType responsetype, MegoUserException exception, ProfileDetailsResponse profileDetailsResponse) {
//
//            }
//        });


        preferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        editor = preferences.edit();


        checkPermission = new CheckPermission(this);
        checkPermission.check(str, new CheckPermission.IRequestResponse() {
            @Override
            public void onDone(Map<String, Boolean> map, boolean isAllGranted) {
                if (isAllGranted) {
                    helper = new SavedDbHelper(MainActivity.this);
                    helper.getReadableDatabase();
                    detail = helper.showAllDetail();

                    new MegoUserConfig.ConfigBuilder(MainActivity.this).setGoogleKey(GOOGLE_ID).setPasswordValidation(6, 20)
                            .setAppBackground(MegoUserConfig.ImageFormat.JPG, R.drawable.back1, R.drawable.back2, R.drawable.back3, R.drawable.back4)
                            .setAppIcon(MegoUserConfig.ImageFormat.PNG, R.drawable.user).build();
                    megoUser = MegoUser.getInstance(MainActivity.this);
                    megoUser.initiateRegistration();
                }
            }
        });

        whatYouWantInLeftDrawer = (RelativeLayout) findViewById(R.id.whatYouWantInLeftDrawer);
        drawer_layout = (CustomDrawerLayout) findViewById(R.id.drawer_layout1);
        top_holder = (RelativeLayout) findViewById(R.id.camera_button);
        bottom_holder = (RelativeLayout) findViewById(R.id.image_button);
        imageView_back = (ImageView) findViewById(R.id.imgViewForMenu1);
        account = (TextView) findViewById(R.id.account);
        logout = (TextView) findViewById(R.id.logout);
        logout_line = (ImageView) findViewById(R.id.iv_logout_line);
        FileBackup = (TextView) findViewById(R.id.filebackup);
        FileRestore = (TextView) findViewById(R.id.filerestore);
        PrefBackup = (TextView) findViewById(R.id.Prefbackup);
        PrefRestore = (TextView) findViewById(R.id.Prefrestore);
        Configuration = (TextView) findViewById(R.id.Configuration);
        Settings = (TextView) findViewById(R.id.Settings);
        Help = (TextView) findViewById(R.id.help);

//sdd dd
        ProfileCustomField profileCustomField = new ProfileCustomField();
        profileCustomField.id = "adarsh";
        profileCustomField.name = "SnakeEye";
        profileCustomField.unit = "sdd";
        profileCustomField.value = "Master";


        ProfileCustomField profileCustomField2 = new ProfileCustomField();
        profileCustomField2.id = "XJXQZ097M";
        profileCustomField2.name = "adarsh";
        profileCustomField2.unit = "fff";
        profileCustomField2.value = "Master";

        ProfileCustomField profileCustomField3 = new ProfileCustomField();
        profileCustomField3.id = "XJXQZ097M";
        profileCustomField2.name = "SnakeEye";
        profileCustomField3.unit = "fff";
        profileCustomField3.value = "Master";

        ArrayList<ProfileCustomField> profileCustomFields = new ArrayList<>();
        profileCustomFields.add(profileCustomField);
        profileCustomFields.add(profileCustomField2);
        profileCustomFields.add(profileCustomField3);

//        try {
//            System.out.println("surender custom  ");
//            megoUserSDK.updateProfileDetails(new UserProfileDetails.ProfileBuilder(MainActivity.this).
//                    setCustom(profileCustomField));
//        } catch (MegoUserException e) {
//            System.out.println("surender custom MegoUserException " + e.getMessage().toString());
//        }


        imageView_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer_layout.isDrawerOpen(whatYouWantInLeftDrawer)) {
                    drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                } else {
                    drawer_layout.openDrawer(whatYouWantInLeftDrawer);
                }
            }
        });

        account.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                try {
                    MegoUser.getInstance(MainActivity.this).showAccount();
                } catch (MegoUserException e) {
                    e.printStackTrace();
                }
            }
        });

        logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                try {
                    boolean status = MegoUser.logout(MainActivity.this);
                    if (status) {
                        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        logout_line.setVisibility(View.GONE);
                        logout.setVisibility(View.GONE);
                        finish();
                    }
                } catch (MegoUserException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FileBackup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                showFileChooser();
            }
        });

        FileRestore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                filetype = "restore";
                Intent intent = new Intent(MainActivity.this, SharePrefTesting.class);
                startActivityForResult(intent, VERSION_CHOOSER);

            }
        });

        PrefBackup.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
//                editor.putString("json", detail.toString());
//                editor.commit();
                System.out.println("<<<checking PrefBackup " + preferences.getAll().toString());
                try {
                    MegoUser.getInstance(MainActivity.this).createPreferenceBackup(pref_backup_version, preferences);
                } catch (MegoUserException e) {
                    e.printStackTrace();
                }
            }
        });

        PrefRestore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
//                editor.putString("json", detail.toString());
//                editor.commit();
                try {
                    MegoUser.getInstance(MainActivity.this).restorePreference(pref_backup_version, preferences);
                } catch (MegoUserException e) {
                    e.printStackTrace();
                }
                System.out.println("<<<checking Prefrestore " + preferences.getAll().toString());
            }
        });

        Settings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                startActivity(new Intent(MainActivity.this, SavedData.class));
            }
        });

        Help.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
                Intent intent = new Intent(MainActivity.this, Help.class);
                startActivity(intent);
            }
        });

//        Configuration.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawer_layout.closeDrawer(whatYouWantInLeftDrawer);
//                Intent i = new Intent(MainActivity.this, ConnectActivity.class);
//                startActivity(i);
//            }
//        });

    }

    @Override
    protected void onStart() {
        System.out.println("<<< onstart");
        overridePendingTransition(0, 0);
        flyIn();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("<<< onresume");
        if (MegoUser.isLoggedIn(MainActivity.this)) {
            logout.setVisibility(View.VISIBLE);
            logout_line.setVisibility(View.VISIBLE);
        }
        flyIn();
    }

    private BroadcastReceiver finishReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
            LocalBroadcastManager.getInstance(MainActivity.this)
                    .unregisterReceiver(finishReceiver);
        }
    };

    @Override
    protected void onStop() {
        System.out.println("<<< onstop");
        overridePendingTransition(0, 0);
        super.onStop();
    }

    private void displayGallery() {
        System.out.println("<<<checking  demo Gallery displayGallery");
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(
                Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, Constants.REQUEST_GALLERY);
        } else {
            Toaster.make(getApplicationContext(), R.string.no_media);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();

//                try {
                fileurl = getPath(this, uri);
                System.out.println("<<<checking onActivityResult " + uri + "---\\---" + fileurl);
                filetype = "backup";
                Intent intent = new Intent(MainActivity.this, SharePrefTesting.class);
                startActivityForResult(intent, VERSION_CHOOSER);

//                } catch (URISyntaxException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            }
        } else if (requestCode == Constants.REQUEST_CAMERA) {
            try {
                if (resultCode == RESULT_OK) {
                    System.out.println("<<<checking demo camera onActivityResult if ");
                    displayPhotoActivity(1);
                } else {
                    System.out.println("<<<checking demo camera onActivityResult else ");
                    UriToUrl.deleteUri(getApplicationContext(), imageUri);
                }
            } catch (Exception e) {
                Toaster.make(getApplicationContext(),
                        R.string.error_img_not_found);
            }
        } else if (resultCode == RESULT_OK
                && requestCode == Constants.REQUEST_GALLERY) {

            try {
                System.out.println("<<<checking demo Gallery onActivityResult  ");
                imageUri = data.getData();
                displayPhotoActivity(2);
            } catch (Exception e) {
                Toaster.make(getApplicationContext(),
                        R.string.error_img_not_found);
            }
        } else if (resultCode == RESULT_OK
                && requestCode == VERSION_CHOOSER) {
            System.out.println("<<<checking onactivityresult version" + data.getStringExtra("result"));
            Long version = Long.parseLong(data.getStringExtra("result"));
            try {
                if (filetype.equalsIgnoreCase("backup")) {
                    MegoUser.getInstance(MainActivity.this).createFileBackup(fileurl, version);
                } else if (filetype.equalsIgnoreCase("restore")) {
                    MegoUser.getInstance(MainActivity.this).restoreFile(version);
                }
            } catch (MegoUserException e) {
                Toaster.make(getApplicationContext(),
                        e.toString());
            } catch (Exception e) {
                Toaster.make(getApplicationContext(),
                        R.string.error_img_not_found);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("unused")
    private void displayCamera() {
        System.out.println("<<<checking demo camera displayCamera");
        imageUri = getOutputMediaFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.REQUEST_CAMERA);
    }

    private Uri getOutputMediaFile() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Camera Pro");
        values.put(MediaStore.Images.Media.DESCRIPTION, "www.appsroid.org");
        return getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private void displayPhotoActivity(int source_id) {
        System.out.println("<<<checking  demo displayPhotoActivity ");
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        intent.putExtra(Constants.EXTRA_KEY_IMAGE_SOURCE, source_id);
        intent.setData(imageUri);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void flyOut(final String method_name) {
        if (click_status) {
            click_status = false;

            animation = AnimationUtils.loadAnimation(this,
                    R.anim.holder_top_back);
            top_holder.startAnimation(animation);

            animation = AnimationUtils.loadAnimation(this,
                    R.anim.holder_bottom_back);
            bottom_holder.startAnimation(animation);

            animation.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation arg0) {
                }

                @Override
                public void onAnimationRepeat(Animation arg0) {
                }

                @Override
                public void onAnimationEnd(Animation arg0) {
                    callMethod(method_name);
                }
            });
        }
    }

    private void callMethod(String method_name) {
        if (method_name.equals("finish")) {
            overridePendingTransition(0, 0);
            finish();
        } else {
            try {
                Method method = getClass().getDeclaredMethod(method_name);
                method.invoke(this, new Object[]{});
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        System.out.println("<<< onbackpressed");
        flyOut("finish");
        super.onBackPressed();
    }

    public void startGallery(View view) {
        flyOut("displayGallery");
    }

    public void startCamera(View view) {
        flyOut("displayCamera");
    }

    private void flyIn() {
        click_status = true;

        animation = AnimationUtils.loadAnimation(this, R.anim.holder_top);
        top_holder.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.holder_bottom);
        bottom_holder.startAnimation(animation);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"), FILE_CHOOSER);
    }

    //    public String getPath(Context context, Uri contentURI) throws URISyntaxException {
//        String result;
//        Cursor cursor = context.getContentResolver().query(contentURI, null,
//                null, null, null);
//        if (cursor == null) { // Source is Dropbox or other similar local file
//            // path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor
//                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }
    public String getPath1(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


}
