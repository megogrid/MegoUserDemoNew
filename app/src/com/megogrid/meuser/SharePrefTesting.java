package com.megogrid.meuser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.megogrid.meuser.R;

public class SharePrefTesting extends Activity {
    private Button submit;
    private SharedPreferences prefrence;
    private TextView versiontext;
    private String[] version_arr;
    private RelativeLayout gender_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.versioncheck);
        gender_layout = (RelativeLayout) findViewById(R.id.gender_layout);
        versiontext = (TextView) findViewById(R.id.spinner_version);
        submit = (Button) findViewById(R.id.btn_save);
        version_arr = getResources().getStringArray(R.array.megouser_version);
        submit.setOnClickListener(new OnClickListener() {
            String version = null;

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                version = versiontext.getText().toString().trim();
                System.out.println("<<<checking testing " + version);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", version);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        gender_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showDropDialog(versiontext, "version", version_arr);
            }
        });
    }

    private void showDropDialog(final TextView textView, final String title,
                                final String[] arrayList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setIcon(null);
        builder.setItems(arrayList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String strName = arrayList[item];
                textView.setText(strName);
                textView.setError(null);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
