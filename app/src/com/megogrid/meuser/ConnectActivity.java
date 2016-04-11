//package com.megogrid.meuser;
//
//import android.app.Activity;
//import android.media.Image;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.megogrid.megoauth.AuthorisedPreference;
//import com.megogrid.megoauth.MegoAuthorizer;
//import com.megogrid.meuser.R;
//
//import com.megogrid.activities.MegoUserData;
//
//
//public class ConnectActivity extends Activity implements View.OnClickListener {
//
//    private RelativeLayout card1, card2, card3;
//    private AuthorisedPreference authorisedPreference;
//    private ImageView imgViewForMenu1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.connect_main);
//        authorisedPreference = new AuthorisedPreference(ConnectActivity.this);
//        initView();
//    }
//
//    private void initView() {
//        card1 = (RelativeLayout) findViewById(R.id.rl_card1);
//        card2 = (RelativeLayout) findViewById(R.id.rl_card2);
//        card3 = (RelativeLayout) findViewById(R.id.rl_card3);
//        imgViewForMenu1 = (ImageView) findViewById(R.id.imgViewForMenu1);
//
//        card1.setOnClickListener(this);
//        card2.setOnClickListener(this);
//        card3.setOnClickListener(this);
//        imgViewForMenu1.setOnClickListener(this);
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        int id = view.getId();
//        if (id == card1.getId()) {
//            finish();
//        } else if (id == card2.getId()) {
//            authorisedPreference.clear();
//            MegoUserData.getInstance(ConnectActivity.this).clear();
//            authorisedPreference.setMeUserConfig("email");
//            Toast.makeText(ConnectActivity.this, "Please relaunch application", Toast.LENGTH_SHORT).show();
//            finish();
//        } else if (id == card3.getId()) {
//            authorisedPreference.clear();
//            MegoUserData.getInstance(ConnectActivity.this).clear();
//            authorisedPreference.setMeUserConfig("phone");
//            Toast.makeText(ConnectActivity.this, "Please relaunch application", Toast.LENGTH_SHORT).show();
//            finish();
//        } else if (id == imgViewForMenu1.getId()) {
//            finish();
//        }
//        MegoAuthorizer.clearList();
//    }
//}
