package com.example.adapter;

import com.example.neituime.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

public class myProgressDialog extends Dialog {

	private Context context = null;
    private static myProgressDialog customProgressDialog = null;
     
    public myProgressDialog(Context context){
        super(context);
        this.context = context;
    }
     
    public myProgressDialog(Context context, int theme) {
        super(context, theme);
    }
     
    public static myProgressDialog createDialog(Context context){
        customProgressDialog = new myProgressDialog(context, R.style.CustomProgressDialog);
        customProgressDialog.setContentView(R.layout.loading_dialog);
        customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        ImageView imageView = (ImageView) customProgressDialog.findViewById(R.id.loadingImageView);
//      AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
//      animationDrawable.start();
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.loading_dialog_tips);
        LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		imageView.startAnimation(operatingAnim);
        return customProgressDialog;
    }
  
    public void onWindowFocusChanged(boolean hasFocus){
         
        if (customProgressDialog == null){
            return;
        }
    }
  
    /**
     *
     * [Summary]
     *       setTitile 标题
     * @param strTitle
     * @return
     *
     */
    public myProgressDialog setTitile(String strTitle){
        return customProgressDialog;
    }
     
    /**
     *
     * [Summary]
     *       setMessage 提示内容
     * @param strMessage
     * @return
     *
     */
    public myProgressDialog setMessage(String strMessage){
        TextView tvMsg = (TextView)customProgressDialog.findViewById(R.id.id_tv_loadingmsg);
         
        if (tvMsg != null){
            tvMsg.setText(strMessage);
        }
         
        return customProgressDialog;
    }

	@Override
	public void setCancelable(boolean flag) {
		super.setCancelable(flag);
	}

}
