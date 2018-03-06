package com.ilkengin.proposalapp.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ilkengin on 19.09.2017.
 */

public class FileOperations {

    private static final String TAG = FileOperations.class.getSimpleName();


    public static File getFileFromRaw(Context context, String fileName) {
        File tempFile = null;
        try{
            int id = context.getResources().getIdentifier(fileName, "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(id);
            tempFile = File.createTempFile("pre", "suf");
            copyFile(inputStream, new FileOutputStream(tempFile));

            // Now some_file is tempFile .. do what you like
        } catch (IOException e) {
            throw new RuntimeException("Can't create temp file ", e);
        }
        return tempFile;
    }

    public static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

}
