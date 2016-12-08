package com.example.linyon.photoshot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class PhotoAbout extends Activity {
    public static final String NAME = "file_name";
    public static final String ID = "file_id";
    public static final String PATH = "file_path";
    public static final String H = "file_h";
    public static final String W = "file_w";
    public String name,id,path;
    public int height,width;
    private TextView txt_photoname;
    private TextView txt_photoid;
    private TextView txt_photow;
    private TextView txt_photoh;
    /**
     * 拍照的圖片抓不到資訊
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_about);
        txt_photoname = (TextView) findViewById(R.id.photo_name);
        txt_photoid = (TextView) findViewById(R.id.photo_id);
        txt_photow = (TextView) findViewById(R.id.photo_width);
        txt_photoh = (TextView) findViewById(R.id.photo_height);
        getInfo();
        Log.i("select_ab_name", name + "");
        txt_photoname.setText(name);
        txt_photoid.setText(id);
        txt_photow.setText(String.valueOf(width));
        txt_photoh.setText(String.valueOf(height));
    }
    private void getInfo() {
        Bundle bundle = this.getIntent().getExtras();
        path = bundle.getString(PATH);
        name = bundle.getString(NAME);
        height = bundle.getInt(H);
        width = bundle.getInt(W);
        //saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径

    }
}
