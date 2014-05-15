package com.e1858.common;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.e1858.ceapp.R;

public class NewDataToast extends Toast {
	private MediaPlayer mPlayer;
	private boolean isSound;
	public NewDataToast(Context context) {
		this(context,false);
		// TODO Auto-generated constructor stub
	}
	public NewDataToast(Context context,boolean isSound){
		super(context);
		this.isSound=isSound;
		mPlayer=MediaPlayer.create(context, R.raw.newdatatoast);
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mp.release();
			}
		});
	}
	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		if(isSound){
			mPlayer.start();
		}
	}
	public void setIsSound(boolean isSound){
		this.isSound=isSound;
	}
	//更新提醒
	public static NewDataToast makeText(Context context, CharSequence text, boolean isSound) {
		NewDataToast result = new NewDataToast(context, isSound);	
        LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        DisplayMetrics dm = context.getResources().getDisplayMetrics();        
        View v = inflate.inflate(R.layout.new_data_toast, null);
        v.setMinimumWidth(dm.widthPixels);//设置控件最小宽度为手机屏幕宽度
        TextView tv = (TextView)v.findViewById(R.id.new_data_toast_message);
        tv.setText(text);
        result.setView(v);
        result.setDuration(600);
        result.setGravity(Gravity.TOP, 0, (int)(dm.density*75));
        return result;
    }

	
}
