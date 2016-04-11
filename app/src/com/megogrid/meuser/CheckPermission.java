package com.megogrid.meuser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

public class CheckPermission {
    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private Context act;
    private Map<String, Boolean> perms = new HashMap<String, Boolean>();
    private IRequestResponse requestResponse;
    private boolean isAllGranted;

    public CheckPermission(Context act) {
        this.act = act;
    }

    public void check(final List<String> permissionsList,
                      IRequestResponse requestResponse) {
        isAllGranted = true;
        this.requestResponse = requestResponse;
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> explainPermissions = new ArrayList<String>();
            final List<String> permissionsRequired = new ArrayList<String>();

            for (final String permission : permissionsList) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (act.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        System.out.println("<<<checking CheckPermission.addPermission not granted");
                        if (((Activity) act).shouldShowRequestPermissionRationale(permission)) {
                            System.out.println("<<<checking CheckPermission.addPermission explain permission ");
                            explainPermissions.add(permission);
                        } else {
                            permissionsRequired.add(permission);
                        }
                    }
                }
            }
            if (permissionsRequired.size() != 0 || explainPermissions.size() != 0) {
                if (explainPermissions.size() > 0) {
                    ((Activity) act).requestPermissions(explainPermissions
                                    .toArray(new String[explainPermissions.size()]),
                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                }
                if (permissionsRequired.size() > 0) {
                    String message = "You need to grant access to " + permissionsRequired.get(0);
                    for (int i = 1; i < permissionsRequired.size(); i++) {
                        message = message + ", " + permissionsRequired.get(i);
                    }
                    showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((Activity) act).requestPermissions(permissionsRequired.toArray(new String[permissionsRequired.size()]),
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        }
                    });
                }
            } else {
                requestResponse.onDone(perms, isAllGranted);
            }
        } else
            requestResponse.onDone(perms, isAllGranted);
    }

    public void check(final String permission, IRequestResponse requestResponse) {
        isAllGranted = true;
        this.requestResponse = requestResponse;
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> explainPermissions = new ArrayList<String>();
            final List<String> permissionsRequired = new ArrayList<String>();

            if (Build.VERSION.SDK_INT >= 23) {
                if (act.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("<<<checking CheckPermission.addPermission not granted");
                    if (((Activity) act).shouldShowRequestPermissionRationale(permission)) {
                        System.out.println("<<<checking CheckPermission.addPermission !act.shouldShowRequestPermission ");
                        explainPermissions.add(permission);
                    } else {
                        permissionsRequired.add(permission);
                    }
                }
            }
            if (explainPermissions.size() > 0) {
                ((Activity) act).requestPermissions(explainPermissions
                                .toArray(new String[explainPermissions.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            } else if (permissionsRequired.size() > 0) {
                String message = "You need to grant access to " + permission;
                showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) act).requestPermissions(new String[]{permission},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                });
            } else {
                requestResponse.onDone(perms, isAllGranted);
            }
        } else
            requestResponse.onDone(perms, isAllGranted);
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(act)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        System.out.println("<<<checking CheckPermission.onRequestPermissionsResult ");
        if (Build.VERSION.SDK_INT >= 23) {
            switch (requestCode) {
                case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                    for (int i = 0; i < permissions.length; i++) {
                        perms.put(
                                permissions[i],
                                grantResults[i] == PackageManager.PERMISSION_GRANTED);
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                            isAllGranted = false;
                    }

                    requestResponse.onDone(perms, isAllGranted);
                    break;
                case REQUEST_CODE_ASK_PERMISSIONS:
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                        isAllGranted = false;
                    perms.put(
                            permissions[0],
                            grantResults[0] == PackageManager.PERMISSION_GRANTED);
                    requestResponse.onDone(perms, isAllGranted);
                    break;
            }
        }
    }
    public interface IRequestResponse {
        void onDone(Map<String, Boolean> map, boolean isAllGranted);
    }

}
