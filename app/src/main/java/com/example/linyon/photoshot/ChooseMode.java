package com.example.linyon.photoshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class ChooseMode extends Activity {
    private ListView mListView;
    private ListAdapter mAdapter;
    private ImageButton b_closemode;
    private int[] imgids = {
            R.mipmap.oldremeber,
            R.mipmap.blur, R.mipmap.graylevel,R.mipmap.backsheet,
            R.mipmap.sketchmode,R.mipmap.blackwhite,R.mipmap.sharp,
            R.mipmap.cancelmode
    };
    Bitmap bm,bm_1;
    public class ListAdapter extends BaseAdapter {
        private Context mContext;
        public ListAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return getResources().getStringArray(R.array.Mode).length;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView == null ) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(R.layout.row_item, null);
                holder =new Holder();
                holder.textView = (TextView)convertView.findViewById(R.id.textView);
                holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);//用convertView的setTag將Holder設置到Tag，以便系統第二次繪製ListView時從Tag中取出
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.textView.setText(getResources().getStringArray(R.array.Mode)[position]);
            holder.imageView.setImageResource(imgids[position]);
            return convertView;
        }

        class Holder {
            TextView textView;
            ImageView imageView;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_mode);
        b_closemode = (ImageButton)findViewById(R.id.b_closemode);
        b_closemode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        byte[] b = bundle.getByteArray("data");
        bm = BitmapFactory.decodeByteArray(b, 0, b.length);//將byte[]轉成Bitmap

        mListView = (ListView)findViewById(R.id.staggered_recycler);
        mAdapter = new ListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
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
                        bm_1 = PhotoMode.sharpenImage(bm);
                        break;
                    default: // 取消
                        bm_1 = bm;
                        break;
                }
                Intent intent = new Intent(ChooseMode.this , PhotoEdit.class);
                Bundle bundle = new Bundle();
                bundle.putInt("checked",position);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bm_1.compress(Bitmap.CompressFormat.JPEG,50,bs); //質量壓縮50%
                bundle.putByteArray("data",bs.toByteArray());
                intent.putExtras(bundle);
                setResult(1001, intent);
                Log.i("setResult:","true");
                ChooseMode.this.finish();
            }
        });
    }

}
