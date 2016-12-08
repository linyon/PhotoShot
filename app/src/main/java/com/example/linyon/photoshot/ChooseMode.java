package com.example.linyon.photoshot;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseMode extends Activity {
  private GridView gridView;

    private int[] imgIds = {R.mipmap.oldremember,
            R.mipmap.blur, R.mipmap.graylevel,R.mipmap.dipan,
            R.mipmap.sketch,R.mipmap.blackwhite,R.mipmap.photo_about_72x72};

    class CustomBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return getResources().getStringArray(R.array.Mode).length;
        }

        @Override
        public Object getItem(int position) {
            return getResources().getStringArray(R.array.Mode)[position];

        }

        @Override
        public long getItemId(int position) {
            return position;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String str = getResources().getStringArray(R.array.Mode)[position];
            View row;
            if(convertView == null){
                position = parent.getChildCount();
                row = getLayoutInflater().inflate(R.layout.row_item, null);

                TextView tv = (TextView) row.findViewById(R.id.textView);

                Log.i("position:",String.valueOf(position));
                tv.setText(getResources().getStringArray(R.array.Mode)[position]);
            }else row = convertView;
            ImageView img = (ImageView) row.findViewById(R.id.imageView);
            img.setImageResource(imgIds[position]);
            img.setTag(str);

            return row;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_mode);
        gridView = (GridView) findViewById(R.id.recycler_view);
        //ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.test_data, android.R.layout.simple_list_item_1);
        //CustomBaseAdapter ba = new CustomBaseAdapter();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChooseMode.this, "選擇"+String.valueOf(position), Toast.LENGTH_SHORT).show();
            }
        });
        gridView.setAdapter(new CustomBaseAdapter());
    }
}
