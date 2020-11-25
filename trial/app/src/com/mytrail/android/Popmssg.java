package com.mytrail.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Popmssg extends AppCompatActivity {
    private ImageView mbirth;
    private ImageView manni;
    private ImageView mserv;
    private ImageView mothers;
    private Spinner spinner;
    private ImageView mcross;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setExitTransition(new Fade());
        getWindow().setAllowEnterTransitionOverlap(true);
        getWindow().setEnterTransition(new Fade());
        getWindow().setAllowReturnTransitionOverlap(true);







        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popmssg);
        mbirth=findViewById(R.id.birth);
        manni=findViewById(R.id.anni);
        mserv=findViewById(R.id.serv);
        mothers=findViewById(R.id.others);
        spinner=findViewById(R.id.spinner);
        mcross=findViewById(R.id.cross);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setElevation(20);
        }
        int width =dm.widthPixels;
        int height =dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.6));



        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-10;



        getWindow().setAttributes(params);
        mcross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popmssg.super.onBackPressed();
            }
        });




        Intent intent = getIntent();
        final String message = intent.getStringExtra("Number");
        mbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(String.valueOf(spinner.getSelectedItem()).equals("English")) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"+message));
                    sendIntent.putExtra("sms_body", "Wishing you a day filled with happiness and a year filled with joy . Happy birthday from team Honda Cars. ");
                    startActivity(sendIntent);
                }
                else if(String.valueOf(spinner.getSelectedItem()).equals("Hindi")){
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"+message));
                    sendIntent.putExtra("sms_body", "आपके लिए एक दिन खुशियों से भरा हो और एक साल खुशी से भरा हो। टीम होंडा कार्स की तरफ से जन्मदिन की बधाई। ");
                    startActivity(sendIntent);

                }

            }
        });
        manni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(spinner.getSelectedItem()).equals("English")) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"+message));
                    sendIntent.putExtra("sms_body", "May you continue to be a wonderful husband and wife to each other. Wishing you nothing but happiness, love, and joy in the years ahead.");
                    startActivity(sendIntent);
                }
                else if(String.valueOf(spinner.getSelectedItem()).equals("Hindi")){
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"+message));
                    sendIntent.putExtra("sms_body", "आप एक दूसरे के लिए एक अद्भुत पति-पत्नी बन सकते हैं। आपको आने वाले वर्षों में खुशी, प्यार और खुशी के अलावा कुछ नहीं चाहिए।");
                    startActivity(sendIntent);

                }

            }
        });
        mserv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(spinner.getSelectedItem()).equals("English")) {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"+message));
                    sendIntent.putExtra("sms_body", "Hi Sir, This is to remind you of your car service due date that is tomorrow. Kindly visit our nearby service center by 11:00 AM for a smooth experience. Looking forward to seeing you tomorrow.");
                    startActivity(sendIntent);
                }
                else if(String.valueOf(spinner.getSelectedItem()).equals("Hindi")){
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"+message));
                    sendIntent.putExtra("sms_body", "हाय सर, यह आपको आपकी कार सेवा की नियत तारीख की याद दिलाने की है जो कल है। एक सहज अनुभव के लिए कृपया सुबह 11:00 बजे तक हमारे नजदीकी सेवा केंद्र पर जाएँ। आपको कल देखने के लिए उत्सुक हैं।");
                    startActivity(sendIntent);

                }

            }
        });
        mothers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+message));

                startActivity(sendIntent);
            }
        });








    }
}
