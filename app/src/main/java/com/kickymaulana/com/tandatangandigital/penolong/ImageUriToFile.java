package com.kickymaulana.com.tandatangandigital.penolong;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ImageUriToFile {
    public static File convert(Uri imageUri, Activity activity) {
        Cursor cursor = null;//  w  w  w.  ja  va  2 s .  c o m
        try {
            String[] proj = { MediaStore.Images.Media.DATA,
                    //MediaStore.Images.Media._ID,
                    MediaStore.Images.ImageColumns.ORIENTATION
            };
            cursor = activity
                    .getContentResolver().query(imageUri, proj, null, null, null);
            int file_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int orientation_ColumnIndex = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
            if (cursor.moveToFirst()) {
                String orientation = cursor
                        .getString(orientation_ColumnIndex);
                return new File(cursor.getString(file_ColumnIndex));
            }
            cursor.close();
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static File getRealPathFromURI(Uri uri, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            File file = new File(context.getFilesDir(), uri.getLastPathSegment());
            FileOutputStream outputStream = new FileOutputStream(file);

            int read;
            byte[] buffers = new byte[1024];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            Log.d("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.d("File Path", "Path " + file.getPath());
            return new File(file.getPath().toString());
        } catch (Exception e) {
            Log.d("Exception", e.getMessage());
            return null;
        }

    }
}
