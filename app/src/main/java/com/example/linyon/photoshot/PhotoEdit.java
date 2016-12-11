package com.example.linyon.photoshot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.linyon.photoshot.PhotoMode.getimage;

public class PhotoEdit extends Activity implements View.OnClickListener {

    private ImageView ivImage;
    private ImageButton btn_selectagain;
    private ImageButton btn_choosemode;
    private ImageButton btn_about;
    private ImageButton btn_save;
    private int CHOOSE_MODE = 4;
    public static final String FILE_NAME = "file_name";
    public static final String FILE_MODE = "file_mode";
    public static final String FILE_PATH = "file_path";
    public static final String FILE_H = "file_h";
    public static final String FILE_W = "file_w";
    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String SAVE_FILE_PATH = "save_file_path";
    public static final String IMAGE_IS_EDIT = "image_is_edit";
    public String filePath,fileName,fileH,fileW;// 需要编辑图片路径
    public String saveFilePath;// 生成的新图片路径
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1,mode = 6;
    Bitmap bm,bm_1;
    public int it=6;
    public boolean checked = false;
    private int[] imgIds = {R.mipmap.oldremember,
            R.mipmap.blur, R.mipmap.graylevel,R.mipmap.dipan
            ,R.mipmap.sketch,R.mipmap.blackwhite,R.mipmap.photo_about_72x72};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        btn_selectagain = (ImageButton) findViewById(R.id.btn_SelectPhotoAgain);
        btn_choosemode = (ImageButton) findViewById(R.id.btn_ChooseMode);
        btn_about = (ImageButton) findViewById(R.id.btn_About);
        btn_save = (ImageButton) findViewById(R.id.btn_Save);
        btn_selectagain.setOnClickListener(this);
        btn_choosemode.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        Log.i("oncreat:","true");
        getData();
    }
    private void getData() {
        filePath = getIntent().getStringExtra(FILE_PATH);
        fileName = getIntent().getStringExtra(FILE_NAME);
        fileW = getIntent().getStringExtra(FILE_W);
        fileH = getIntent().getStringExtra(FILE_H);
        bm = getimage(filePath);
        //saveFilePath = getIntent().getStringExtra(EXTRA_OUTPUT);// 保存图片路径
        /*BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // 不顯示照片
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = 1;
        options.inJustDecodeBounds = false; // 顯示照片
        bm = BitmapFactory.decodeFile(filePath, options);
        Log.i("selectedImagePath1", filePath + "");*/
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
            bm_1 = BitmapFactory.decodeByteArray(b, 0, b.length);
            ivImage.setImageBitmap(bm_1);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ChooseMode:

                ChooseMode();
                break;
            case R.id.btn_SelectPhotoAgain:
                //Toast.makeText(PhotoEdit.this, "選擇濾鏡", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PhotoEdit.this , ChooseMode.class);
                Bundle bundle = new Bundle();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG,50,bs);
                bundle.putByteArray("data",bs.toByteArray());
                intent.putExtras(bundle);
                //intent.putExtra(PhotoEdit.FILE_PATH,filePath);
                startActivityForResult(intent, 1000);
                break;
            case R.id.btn_About:
                About();
                break;
            case R.id.btn_Save:
                if(it!=6) Save();
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
        Log.i("select2", filePath);
        Log.i("selectw", String.valueOf(bm.getWidth()));
        Log.i("selecth", String.valueOf(bm.getHeight()));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void Save(){ /**未完成*/
        final CharSequence[] items ={"覆蓋原圖","另存新檔","取消存檔"};
        new AlertDialog.Builder(PhotoEdit.this).setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = items[which].toString();
                        Toast.makeText(getApplicationContext(), "選擇" + name, Toast.LENGTH_SHORT).show();
                        if(which==0){
                            save_as_ori(bm_1);
                        }else if(which==1){
                            save_as_other(bm_1);
                        }else{
                            dialog.dismiss();
                        }
                    }
                }).show();

    }
    private void save_as_ori(Bitmap bm_save){ //有問題
        try {
            // 取得外部儲存裝置路徑
            String path = Environment.getExternalStorageDirectory().toString();
            // 開啟檔案

            File file = new File(path, "Image.png");
            // 開啟檔案串流
            FileOutputStream out = new FileOutputStream(file);
            // 將 Bitmap壓縮成指定格式的圖片並寫入檔案串流
            bm_save.compress ( Bitmap. CompressFormat.PNG , 90 , out);
            // 刷新並關閉檔案串流
            out.flush ();
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
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "PhotoShot");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
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
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(this.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/PhotoShot/"+fileName))));
    }
    private void ChooseMode(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoEdit.this);
        builder.setTitle(R.string.Choose_Mode);
        BaseAdapter adapter = new ListItemAdapter();
        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        it = item;
                        switch (item) {
                            case 0: // 懷舊
                                bm_1 = PhotoMode.oldRemeber(bm);
                                break;
                            case 1: // 模糊效果
                                bm_1 = PhotoMode.blurImage(bm);
                                break;
                            case 2: //
                                bm_1 = PhotoMode.graylevle(bm);
                                break;
                            case 3: //
                                bm_1 = PhotoMode.diPian(bm);
                                break;
                            case 4:
                                bm_1 = PhotoMode.sketch(bm);
                                break;
                            case 5:
                                bm_1 = PhotoMode.blackwhite(bm);
                                break;
                            case 6:
                                bm_1 = bm;
                                break;
                            default: // 取消
                                dialog.dismiss(); // 關閉對畫框
                                break;
                        }
                        ivImage.setImageBitmap(bm_1);
                    }
                };
        builder.setAdapter(adapter, listener);
        builder.show();
    }
    class ListItemAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imgIds.length;
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position,View contentView,ViewGroup parent) {
            TextView textView = new TextView(PhotoEdit.this);
            //获得array.xml中的数组资源getStringArray返回的是一个String数组
            String text = getResources().getStringArray(R.array.Mode)[position];
            textView.setText(text);
            //设置字体大小
            textView.setTextSize(18);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);
            //设置水平方向上居中
            textView.setGravity(android.view.Gravity.CENTER_VERTICAL);
            textView.setMinHeight(65);
            //设置文字颜色
            textView.setTextColor(Color.BLACK);
            //设置图标在文字的左边
            textView.setCompoundDrawablesWithIntrinsicBounds(imgIds[position], 0, 0, 0);
            //设置textView的左上右下的padding大小
            textView.setPadding(15, 0, 15, 0);
            //设置文字和图标之间的padding大小
            textView.setCompoundDrawablePadding(15);
            return textView;
        }


    }
}