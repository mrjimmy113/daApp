package com.quang.daapp.ultis;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

public class CommonUltis {

    public static final int GALLERY_REQUEST_CODE = 0;

    public static String getPathFromURI(Uri uri, Activity activity) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.getContentResolver().query(uri,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    public static Intent getPickFromGalleryIntent(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        // Launching the Intent
        return intent;
    }

    public static void checkPermissions(Context context, Activity activity){

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);

        }

    }

}
