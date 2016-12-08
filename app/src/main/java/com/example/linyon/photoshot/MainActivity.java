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

import java.util.Calendar;

public class MainActivity extends Activity implements View.OnClickListener{

    private ImageButton btnSelect;
    private ImageButton btnTake;
    private ImageButton btnAbout;
    private String path,name,id,pic_width,pic_height; // 圖片檔案位置
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    public static final int REQUEST_PERMISSON_CAMERA = 2;
    Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_SelectPhoto:
                select_image();
                break;
            case R.id.btn_TakePhoto:
                take_photo();
                break;
            case R.id.btn_AboutUs:
                //about_us();
                break;
        }//end switch
    }
    // 啟動選擇方式
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //if (requestCode == SELECT_FILE) // 從圖庫開啟
                //onActivityResult(data);
            //else if (requestCode == REQUEST_CAMERA) // 拍照
            //Bitmap bmp = getScaleBitmap(this, getTempImage().getPath());
            /*if (null != bmp) {
                postImage.setImageBitmap(bmp);
            }*/
            onActivityResult(data);
            return;
        }
    }

    private void select_image(){
        Intent intent1 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent1.setType("image/*");
        startActivityForResult(Intent.createChooser(intent1, "選擇開啟圖庫"), SELECT_FILE);
    }
    private void edit_image(){
        if(TextUtils.isEmpty(path)){
            Toast.makeText(MainActivity.this, "請選擇照片", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MainActivity.this, PhotoEdit.class);
        intent.putExtra(PhotoEdit.FILE_PATH,path);
        intent.putExtra(PhotoEdit.FILE_NAME,name);
        intent.putExtra(PhotoEdit.FILE_W,pic_width);
        intent.putExtra(PhotoEdit.FILE_H,pic_height);
        MainActivity.this.startActivity(intent);
    }
    private void  take_photo(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    /*private void about_us(){
        Intent intent = new Intent(MainActivity.this, AboutUs.class);
        MainActivity.this.startActivity(intent);
    }*/
    /*接收拍照結果*/
    /*private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        name = System.currentTimeMillis() + ".jpg";
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),name); // 輸出檔案名稱
        path = destination + ""; // 輸出檔案位置
        FileOutputStream fo;
        try {
            destination.createNewFile(); // 建立檔案
            fo = new FileOutputStream(destination); // 輸出
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("select_onCapture:" , path);
        Log.i("select_on_name:" , name);
        Intent intent = new Intent(MainActivity.this, PhotoEdit.class);
        intent.putExtra(PhotoEdit.FILE_PATH,path);
        intent.putExtra(PhotoEdit.FILE_NAME,name);
        MainActivity.this.startActivity(intent);
    }*/

    @SuppressWarnings("deprecation")
    private void onActivityResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA, MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.MediaColumns.WIDTH, MediaStore.MediaColumns.HEIGHT};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null, null);
        if (cursor != null){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            int name_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int width_idex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH);
            int height_idex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT);
            cursor.moveToFirst();
            path = cursor.getString(column_index); // 選擇的照片位置
            name = cursor.getString(name_index);
            pic_width = cursor.getString(width_idex);
            pic_height = cursor.getString(height_idex);
            edit_image();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 紀錄圖片檔案位置
        SharedPreferences preferencesSave = getApplicationContext()
                .getSharedPreferences("image",
                        android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesSave.edit();
        editor.putString("selectedImagePath", path); // 紀錄最後圖片位置
        editor.commit();
        Log.i("onDestroy", "onDestroy");
    }
}