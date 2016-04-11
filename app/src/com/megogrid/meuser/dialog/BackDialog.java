package com.megogrid.meuser.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.megogrid.meuser.R;


public class BackDialog extends Dialog {
	
	public Button positive;
	public Button negative;
	public ImageView cross;

	public BackDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.setContentView(R.layout.dialog_back);
		
		positive = (Button) findViewById(R.id.yes_btn);
		negative = (Button) findViewById(R.id.no_btn);
		cross = (ImageView) findViewById(R.id.img_cross);
	}

}
