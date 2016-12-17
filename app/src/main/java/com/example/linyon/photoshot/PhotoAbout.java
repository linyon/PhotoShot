package com.example.linyon.photoshot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoAbout extends Activity {
    public static final String NAME = "file_name";
    public static final String ID = "file_id";
    public static final String PATH = "file_path";
    public static final String H = "file_h";
    public static final String W = "file_w";
    public String name,path;
    public int height,width;
    private TextView txt_photoname;
    private TextView txt_photoloc;
    private TextView txt_photow;
    private TextView txt_photoh;
    private ImageButton b_closeinfo;
    Bitmap bm;
    private ImageView img_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo_about);
        txt_photoname = (TextView) findViewById(R.id.photo_name);
        txt_photoname.setSelected(true);
        txt_photoloc = (TextView) findViewById(R.id.photo_loc);
        txt_photoloc.setSelected(true);
        txt_photow = (TextView)findViewById(R.id.photo_width);
        txt_photoh = (TextView)findViewById(R.id.photo_height);
        b_closeinfo = (ImageButton)findViewById(R.id.close_button);
        img_about = (ImageView)findViewById(R.id.img_about);
        getInfo();
        b_closeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getInfo() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        byte[] b = bundle.getByteArray("data");
        bm = BitmapFactory.decodeByteArray(b, 0, b.length);
        path = bundle.getString(PATH);
        name = bundle.getString(NAME);
        height = bundle.getInt(H);
        width = bundle.getInt(W);
        img_about.setImageBitmap(PhotoMode.toRoundCorner(bm,100.f));
        txt_photoname.setText(name);
        txt_photoloc.setText(path);
        txt_photow.setText(String.valueOf(width));
        txt_photoh.setText(String.valueOf(height));
    }
}
