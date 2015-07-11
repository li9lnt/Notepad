package com.example.house.notepad;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends Activity {

    private ListView lsv_memo;                   // メモ一覧
    private Button btn_create;                  // 新規作成ボタン
    private ArrayAdapter<ThumbInfo> adapter;    //lsv_memoとmemo_list用のアダプタ
    public ArrayList<ThumbInfo> memo_list;

    public static final int EDIT_NEW = 0;
    public static final int EDIT_UPDATE = 1;
    public static final String SELECTED_POSITION = "selected_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        memo_list = new ArrayList<ThumbInfo>();

        ArrayList<DtoItem> item_list = DaoItem.findAll(getApplicationContext());
        for (int i  = 0; i < item_list.size(); i++) {
            ThumbInfo thumb_info = new ThumbInfo();
            thumb_info.id = item_list.get(i).id;
            thumb_info.statement = Converter.convertShortStatement(item_list.get(i).content);
            thumb_info.update_time = Converter.convertCalendarToString(item_list.get(i).update_time);
            memo_list.add(thumb_info);
        }

        adapter = new AdapterListItem(this, 0, memo_list);
        lsv_memo = (ListView)findViewById(R.id.listView);
        lsv_memo.setAdapter(adapter);

        lsv_memo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message = parent.toString() + " " + view.toString() + " " + String.valueOf(position) + " " + String.valueOf(id);
                Log.d("Notepad_Debug", message);

                // 編集画面にクリックされたメモのidを渡す
                // 選択されたアイテムの位置も渡す（編集後にリスト画面を更新する必要があるため）
                ThumbInfo thumb_info = (ThumbInfo)((ListView)parent).getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putLong(DaoItem.COLUMN_ID, thumb_info.id);
                bundle.putInt(SELECTED_POSITION, position);

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtras(bundle);

                startActivityForResult(intent, EDIT_UPDATE);
            }
        });

        btn_create = (Button)findViewById(R.id.button);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, EDIT_NEW);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_NEW:
            case EDIT_UPDATE:
                if (resultCode == EditActivity.EDIT_SAVE) {

                    Bundle bundle = data.getExtras();
                    long id = bundle.getLong(DaoItem.COLUMN_ID);
                    DtoItem item = DaoItem.findById(getApplicationContext(), id);
                    ThumbInfo thumb_info = new ThumbInfo();
                    thumb_info.id = item.id;
                    thumb_info.statement = Converter.convertShortStatement(item.content);
                    thumb_info.update_time = Converter.convertCalendarToString(item.update_time);

                    if (requestCode == EDIT_NEW) {
                        adapter.add(thumb_info);
                    } else if (requestCode == EDIT_UPDATE) {
                        int position = bundle.getInt(SELECTED_POSITION);
                        memo_list.get(position).statement = thumb_info.statement;
                        adapter.notifyDataSetChanged();
                    }
                }
                else if (resultCode == EditActivity.EDIT_CANCEL) {
                    ;
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
