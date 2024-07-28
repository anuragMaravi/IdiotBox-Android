package com.anuragmaravi.idiotbox.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class PermissionUtils {

    public static final int PERMISSION_CALLBACK_CONSTANT = 100;
    public static final int REQUEST_PERMISSION_SETTING = 101;
    private static final String[] permissionsRequired = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private static SharedPreferences permissionStatus;

    public static boolean checkAndRequestPermissions(FragmentActivity activity) {
        permissionStatus = activity.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        if (ActivityCompat.checkSelfPermission(activity, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(activity, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsRequired[2])) {
                showPermissionDialog(activity);
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                redirectToSettings(activity);
            } else {
                ActivityCompat.requestPermissions(activity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.apply();
            return false;
        }
        return true;
    }

    private static void showPermissionDialog(FragmentActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Need Multiple Permissions")
                .setMessage("This app needs Storage and Location permissions.")
                .setPositiveButton("Grant", (dialog, which) -> {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(activity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    private static void redirectToSettings(FragmentActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Need Multiple Permissions")
                .setMessage("This app needs Storage and Location permissions.")
                .setPositiveButton("Grant", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    Toast.makeText(activity.getApplicationContext(), "Go to Permissions to Grant Storage and Location", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                .show();
    }

    public static boolean arePermissionsGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean areAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void showPermissionDeniedDialog(FragmentActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle("Permissions Denied")
                .setMessage("Unable to get required permissions.")
                .setPositiveButton("OK", (dialog, which) -> dialog.cancel())
                .show();
    }
}
