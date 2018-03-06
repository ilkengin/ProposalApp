package com.ilkengin.proposalapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.ilkengin.proposalapp.R;

/**
 * Created by ilkengin on 19.09.2017.
 */

public class PermissionOperations {

    private static final String TAG = PermissionOperations.class.getSimpleName();


    /**
     * Return the current state of the permissions needed.
     */
    public static boolean checkPermissions(Context context, String permName) {
        int permissionState = ActivityCompat.checkSelfPermission(context, permName);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(final Activity activity, final String permName) {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity,  Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            Utils.showSnackbar(activity, R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startPermissionRequest(activity,permName);
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startPermissionRequest(activity,permName);
        }


    }

    public static void startPermissionRequest(Activity activity, String permName) {
        ActivityCompat.requestPermissions(activity, new String[]{permName}, Constants.MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

}
