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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

public class ChooseMode extends Activity {
    private GridView gridView;
    //private RecyclerView mRecyclerView;
    private ListView mListView;
    private ListAdapter mAdapter;
    private int[] imgIds = {R.mipmap.oldremember,
            R.mipmap.blur, R.mipmap.graylevel,R.mipmap.dipan,
            R.mipmap.sketch,R.mipmap.blackwhite,R.mipmap.photo_about_72x72};
    public String filePath;
    Bitmap bm,bm_1;
    public class ListAdapter extends BaseAdapter {
        private Context mContext;
        private View mLastView;
        private int mLastPosition;

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
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.textView.setText(getResources().getStringArray(R.array.Mode)[position]);
            holder.imageView.setImageResource(imgIds[position]);
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
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        byte[] b = bundle.getByteArray("data");
        Log.i("mode:",String.valueOf(b.length));
        Log.i("mode:","2");
        bm = BitmapFactory.decodeByteArray(b, 0, b.length);

        mListView = (ListView)findViewById(R.id.staggered_recycler);
        mAdapter = new ListAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(ChooseMode.this, "選擇"+String.valueOf(position), Toast.LENGTH_SHORT).show();
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
                        bm_1 = bm;
                        break;
                    default: // 取消
                        break;
                }
                Intent intent = new Intent(ChooseMode.this , PhotoEdit.class);
                Bundle bundle = new Bundle();
                bundle.putInt("checked",position);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bm_1.compress(Bitmap.CompressFormat.JPEG,50,bs);
                bundle.putByteArray("data",bs.toByteArray());
                intent.putExtras(bundle);
                setResult(1001, intent);
                ChooseMode.this.finish();
            }
        });
    }

}
