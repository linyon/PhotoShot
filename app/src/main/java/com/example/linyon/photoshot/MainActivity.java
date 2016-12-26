package com.example.linyon.photoshot;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements View.OnClickListener{

    private ImageButton btnSelect;
    private ImageButton btnTake;
    private ImageButton btnAbout;
    private String path,name,pic_width,pic_height;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSelect = (ImageButton) findViewById(R.id.btn_SelectPhoto);
        btnTake = (ImageButton) findViewById(R.id.btn_TakePhoto);
        btnAbout = (ImageButton) findViewById(R.id.btn_AboutUs);
        btnSelect.setOnClickListener(this);
        btnTake.setOnClickListener(this);
        btnAbout.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) { //主畫面按鈕按下後須執行的動作
        switch (v.getId()) {
            case R.id.btn_SelectPhoto:
                select_image();
                break;
            case R.id.btn_TakePhoto:
                take_photo();
                break;
            case R.id.btn_AboutUs:
                about_us();
                break;
        }//end switch
    }
    //啟動選擇方式。一旦選擇完圖片或拍完照片，onActivityResult()方法將會被調用
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) { //有選到或拍到照片時
            Uri selectedImageUri = data.getData(); //使用getData取得相片儲存後的uri
            //下方字串陣列放 圖片的絕對路徑、名稱、寬、高
            String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.MediaColumns.WIDTH, MediaStore.MediaColumns.HEIGHT};
            //使用managedQuery()方法, 從媒體存儲(MediaStore)獲取圖片內容
            Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
            if (cursor != null){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                int name_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
                int width_idex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);
                int height_idex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);
                cursor.moveToFirst(); //將指標移至第一筆資料
                path = cursor.getString(column_index); //選擇的照片位置
                name = cursor.getString(name_index);
                pic_width = cursor.getString(width_idex);
                pic_height = cursor.getString(height_idex);
                edit_image();
            }
            return;
        }
    }
    private void select_image(){
        //ACTION_PICK是用來選擇一個URI目錄下的資料
        Intent intent1 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
        startActivityForResult(Intent.createChooser(intent1, "選擇開啟圖庫"), SELECT_FILE);

    }
    private void edit_image(){
        if(TextUtils.isEmpty(path)){ //判斷path是否為空返回值是一个boolean
            Toast.makeText(MainActivity.this, "請選擇照片", Toast.LENGTH_SHORT).show();
            return;
        }
        //跳到PhotoEdit並傳值
        Intent intent = new Intent(MainActivity.this, PhotoEdit.class);
        intent.putExtra(PhotoEdit.FILE_PATH,path);
        intent.putExtra(PhotoEdit.FILE_NAME,name);
        intent.putExtra(PhotoEdit.FILE_W,pic_width);
        intent.putExtra(PhotoEdit.FILE_H,pic_height);
        MainActivity.this.startActivity(intent);
    }
    private void  take_photo(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //使用Intent調用其他服務幫忙拍照
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void about_us(){
        Intent intent = new Intent(MainActivity.this, AboutUs.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferencesSave = getApplicationContext()// 紀錄圖片檔案位置
                .getSharedPreferences("image",
                        android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesSave.edit();
        editor.putString("selectedImagePath", path); // 紀錄最後圖片位置
        editor.commit();
        Log.i("onDestroy", "onDestroy");
    }
}