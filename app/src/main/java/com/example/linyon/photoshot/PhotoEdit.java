package com.example.linyon.photoshot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoEdit extends Activity implements View.OnClickListener {

    private ImageView ivImage;
    private ImageButton btn_choosemode;
    private ImageButton btn_about;
    private ImageButton btn_save;
    public static final String FILE_NAME = "file_name";
    public static final String FILE_PATH = "file_path";
    public static final String FILE_H = "file_h";
    public static final String FILE_W = "file_w";
    public String filePath,fileName,fileH,fileW;
    Bitmap bm,bm_1;
    public int it=7;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        btn_choosemode = (ImageButton) findViewById(R.id.btn_ChooseMode);
        btn_about = (ImageButton) findViewById(R.id.btn_About);
        btn_save = (ImageButton) findViewById(R.id.btn_Save);
        btn_choosemode.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        Log.i("oncreat:","true");
        getData();
    }
    private void getData(){ //獲取傳遞過來的值
        filePath = getIntent().getStringExtra(FILE_PATH);
        fileName = getIntent().getStringExtra(FILE_NAME);
        fileW = getIntent().getStringExtra(FILE_W);
        fileH = getIntent().getStringExtra(FILE_H);
        bm = PhotoMode.getimage(filePath); //利用圖片路徑抓圖片(Bitmap類別)
        ivImage.setImageBitmap(bm);// 將圖片顯示
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.i("onActivityResult:","true");
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == 1000 && resultCode == 1001)
        {
            Bundle bundle = data.getExtras();
            it = bundle.getInt("checked");
            byte[] b = bundle.getByteArray("data");
            bm_1 = BitmapFactory.decodeByteArray(b, 0, b.length);//將byte[]轉成Bitmap
            ivImage.setImageBitmap(bm_1);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ChooseMode:
                Intent intent = new Intent(PhotoEdit.this , ChooseMode.class);
                Bundle bundle = new Bundle();
                //把Bitmap轉成Byte
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG,100,bs);////質量壓縮，把壓縮後的數據存放到bs中
                bundle.putByteArray("data",bs.toByteArray()); //bs轉成位元陣列
                intent.putExtras(bundle);
                startActivityForResult(intent, 1000); //1000=requestcode
                break;
            case R.id.btn_About:
                About();
                break;
            case R.id.btn_Save:
                if(it!=7) Save();
                else Toast.makeText(PhotoEdit.this, "未先選擇效果", Toast.LENGTH_SHORT).show();
                break;
        }//end switch
    }
    private void About(){
        Intent intent = new Intent(PhotoEdit.this , PhotoAbout.class);
        Bundle bundle = new Bundle();
        bundle.putString(PhotoAbout.PATH,filePath);
        bundle.putString(PhotoAbout.NAME,fileName);
        bundle.putInt(PhotoAbout.H,bm.getHeight());
        bundle.putInt(PhotoAbout.W,bm.getWidth());
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,bs);
        bundle.putByteArray("data",bs.toByteArray());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    /*儲存*/
    public void Save(){
        final CharSequence[] items ={"覆蓋原圖","另存新檔","取消存檔"};
        new AlertDialog.Builder(PhotoEdit.this).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = items[which].toString();
                if(which==0){
                    save_as_ori(bm_1);
                    Toast.makeText(getApplicationContext(), name + "成功", Toast.LENGTH_SHORT).show();
                }else if(which==1){
                    save_as_other(bm_1);
                    Toast.makeText(getApplicationContext(), name + "成功", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                }

            }
        }).show();
    }
    private void save_as_ori(Bitmap bm_save){
        try {
            File file = new File(filePath);// 開啟檔案
            FileOutputStream out = new FileOutputStream(file);// 開啟檔案串流
            bm_save.compress(Bitmap.CompressFormat.PNG,100,out);// 將Bitmap壓縮成指定格式的圖片並寫入檔案串流
            out.flush ();// 刷新並關閉檔案串流
            out.close ();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace ();
        }
    }
    private void save_as_other(Bitmap bm_save){
        File appDir = new File(Environment.getExternalStorageDirectory(), "PhotoShot");// 首先保存图片
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis()+".jpg"; //圖片名稱以當前系統時間命名
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm_save.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {//其次把文件插入到系統圖庫
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //圖片並不會立刻顯示在圖庫中，所以最后通知圖庫去更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/PhotoShot/"+fileName))));
    }
}