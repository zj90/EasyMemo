package cn.zj90.memo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import cn.zj90.memo.R;
import cn.zj90.memo.db.NoteDAO;
import cn.zj90.memo.db.model.Note;
import cn.zj90.memo.util.AppUtil;

public class NewMemoActivity extends BaseActivity {

    // var
    private String noteTime;
    private int notePriority = 0;
    private String noteContent;

    // obj
    private NoteDAO noteDAO;

    // view
    private EditText etContent;
    private RadioGroup rgRadio;
    private Button btnSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beforeInit() {
        setContentView(R.layout.activity_newmemo);
        noteDAO = new NoteDAO(this);
    }

    @Override
    protected void initData() {
        noteTime = AppUtil.returnTime();
    }

    @Override
    protected void initView() {
        etContent = (EditText) findViewById(R.id.edit_content);
        rgRadio = (RadioGroup) findViewById(R.id.edit_radioGroup);
        btnSend = (Button) findViewById(R.id.act_newmemo_send);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initEvent() {
        rgRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.edit_radio0:
                        notePriority = Note.Priority.NORMAL;
                        break;
                    case R.id.edit_radio1:
                        notePriority = Note.Priority.IMPORTANT;
                        break;
                    case R.id.edit_radio2:
                        notePriority = Note.Priority.URGENT;
                        break;
                }
            }
        });

        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, etContent.getText().toString().trim());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "发送到其他APP"));
            }
        });
    }

    private void save() {
        noteContent = etContent.getText().toString().trim();
        if (noteContent.equals("")) {
            toast("请输入内容");
        } else {
            // add note
            Note note = new Note();
            note.setTime(noteTime);
            note.setPriority(notePriority);
            note.setContent(noteContent);
            noteDAO.add(note);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // save
        switch (item.getItemId()) {
            case R.id.addmenu_save:
                save();
                break;
            case android.R.id.home:
                break;
        }
        forward(HomeActivity.class);
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            forward(HomeActivity.class);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        noteDAO.close();
        super.onDestroy();
    }
}
