package com.example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.tv.TvContract;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.eventtest.BuildConfig;
import com.example.eventtest.R;

import java.io.File;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //get destination to update file and set Uri
        //TODO: First I wanted to store my update .apk file on internal storage for my app but apparently android does not allow you to open and install
        //aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
        //solution, please inform us in comment
//        Uri apkURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider",File.createTempFile());
//
//        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
//        String fileName = "1614017695_6492_pms.apk";
//        destination += apkURI;
//        final Uri uri = Uri.parse("content://" + destination);
//
//        //Delete update file if exists
//        File file = new File(destination);
//        if (file.exists())
//            //file.delete() - test this, I think sometimes it doesnt work
//            file.delete();
//
//        //get url of app on server
//        String url = "http://pmsapp.co.in/storage/app/1614017695_6492_pms.apk";
//
//        //set downloadmanager
//        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
//        request.setDescription("downloading..");
//        request.setTitle("APK");
//
//        //set destination
//        request.setDestinationUri(uri);
//
//        // get download service and enqueue file
//        final DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//        final long downloadId = manager.enqueue(request);
//
//        //set BroadcastReceiver to install app when .apk is downloaded
//        BroadcastReceiver onComplete = new BroadcastReceiver() {
//            public void onReceive(Context ctxt, Intent intent) {
//                Intent install = new Intent(Intent.ACTION_VIEW);
//                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                install.setDataAndType(uri,
//                        manager.getMimeTypeForDownloadedFile(downloadId));
//                startActivity(install);
//
//                unregisterReceiver(this);
//                finish();
//            }
//        };
//
//        //register receiver for when .apk download is compete
//        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
//        //
//        //        Intent intent = new Intent(Intent.ACTION_VIEW);
//        //        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "1614017695_6492_pms.apk")), "application/vnd.android.package-archive");
//        //        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        //        startActivity(intent);

        if(isNetworkAvailable(this) && isStoragePermissionGranted())
        downloadUpdate();
    }

    String upadte_url = "http://pmsapp.co.in/storage/app/1614017695_6492_pms.apk";

    private void downloadUpdate(){
        Log.e("in","in");
        ProgressBar downloadAppProgess = new ProgressBar(TestActivity.this);
        try{
            final File apk_file_path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "1614017695_6492_pms.apk");
            if (apk_file_path.exists()) apk_file_path.delete();

            Log.e("update","Downloading request on url :"+upadte_url);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(upadte_url));
            request.setDescription("2.1");
            request.setTitle("app");
            //set destination
            Log.e("file path",apk_file_path.toString());
            final Uri uri = Uri.parse("file://" + apk_file_path);
            request.setDestinationUri(uri);

            // get download service and enqueue file
            DownloadManager manager = (DownloadManager)getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

            final long downloadId = manager.enqueue(request);
            Log.e("download id",downloadId+"");

            downloadAppProgess.setVisibility(View.VISIBLE);
           // update_now.setVisibility(View.GONE);
            new Thread(() -> {
                boolean downloading = true;
                while(downloading) {
                    DownloadManager.Query q = new DownloadManager.Query();
                    q.setFilterById(downloadId);
                    Cursor cursor = manager.query(q);
                    cursor.moveToFirst();

                    final int bytes_downloaded = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                    if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                        downloading = false;
                    }
                    int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                    if (bytes_total != 0) {
                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);
                        runOnUiThread(() -> downloadAppProgess.setProgress(dl_progress));
                    }
                    cursor.close();

                }
            }).start();


            //set BroadcastReceiver to install app when .apk is downloaded
            BroadcastReceiver onComplete = new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    //BroadcastReceiver on Complete
                    if (apk_file_path.exists()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

//                            FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
//                                    BuildConfig.APPLICATION_ID + ".provider", apk_file_path);
                            Uri apkUri = FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                                    BuildConfig.APPLICATION_ID + ".provider", apk_file_path);
                            intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        } else {
                            Uri apkUri = Uri.fromFile(apk_file_path);
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(apkUri, manager.getMimeTypeForDownloadedFile(downloadId));
                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        startActivity(intent);
                    }else{

                        downloadAppProgess.setVisibility(View.GONE);
                       // update_now.setVisibility(View.VISIBLE);
                    }
                    unregisterReceiver(this);
                }
            };
           registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        }catch (Exception e){
            Log.e("error",e.getMessage());
            e.printStackTrace();
            downloadAppProgess.setVisibility(View.GONE);
           // update_now.setVisibility(View.VISIBLE);
        }

    }
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if (activeNetworkInfo != null) { // connected to the internet
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        Toast.makeText(context, "no internet", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Storage permission is granted");
                return true;
            } else {
                Log.v("TAG","Storage permission is revoked");
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Storage permission is granted");
            return true;
        }
    }
}