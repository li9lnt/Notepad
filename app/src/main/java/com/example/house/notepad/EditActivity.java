package com.example.house.notepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;


public class EditActivity extends Activity {

    private EditText etx_content; // テキストフィールド
    private Button btn_save; //保存ボタン
    private Button btn_cancel; // キャンセルボタン
    long row_id; //編集画面の表示内容はDBのどのレコードを示すか(0以上)/-1のときはDBになし
    int selected_position; // リスト画面で何番目のメモが選択されたか

    public static final int EDIT_CANCEL = 0;
    public static final int EDIT_SAVE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etx_content = (EditText)findViewById(R.id.editText);

        // DBからリスト画面でクリックされたメモの内容を取得
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            // リストのアイテムをクリックして編集画面に来たとき
            row_id = bundle.getLong(DaoItem.COLUMN_ID);
            selected_position = bundle.getInt(MainActivity.SELECTED_POSITION);
            DtoItem item = DaoItem.findById(getApplicationContext(), row_id);
            etx_content.setText(item.content);
        } else {
            // 新規作成ボタンを押して編集画面に来たとき
            row_id = -1;
            selected_position = -1;
        }

        btn_save = (Button)findViewById(R.id.save_button);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DtoItem item = new DtoItem();
                item.create_time = Calendar.getInstance();
                item.update_time = Calendar.getInstance();
                item.mark = 0;
                item.content = String.valueOf(etx_content.getText());

                if (row_id >= 0) {
                    // 編集による更新
                    item.id = row_id;
                    DaoItem.update(getApplicationContext(), item);
                } else {
                    // 新規作成
                    // 新規に追加されたレコードのidを取得
                    row_id = DaoItem.insert(getApplicationContext(), item);
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(DaoItem.COLUMN_ID, row_id);
                bundle.putInt(MainActivity.SELECTED_POSITION, selected_position);
                intent.putExtras(bundle);
                int resultCode = EDIT_SAVE;
                setResult(resultCode, intent);
                finish();
            }
        });

        btn_cancel = (Button)findViewById(R.id.cancel_button);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                int resultCode = EDIT_CANCEL;
                setResult(resultCode, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
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
