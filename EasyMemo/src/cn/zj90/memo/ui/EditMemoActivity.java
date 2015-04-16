package cn.zj90.memo.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import cn.zj90.memo.R;
import cn.zj90.memo.db.NoteDAO;
import cn.zj90.memo.db.model.Note;
import cn.zj90.memo.db.table.NoteTable;

public class EditMemoActivity extends BaseActivity {

    // var
    private int noteId;
    private String noteTime;
    private int notePriority;
    private String noteContent;
    private int contentLength;

    // obj
    private NoteDAO noteDAO;

    // view
    private EditText mContent;
    private Button btnSend;
    private Button btnDelete;
    private RadioGroup rgPriority;
    private RadioButton rbNormal, rbImportant, rbUrgent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void beforeInit() {
        setContentView(R.layout.activity_editmemo);
        noteDAO = new NoteDAO(this);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        noteId = bundle.getInt(NoteTable.NOTE_ID);
        noteTime = bundle.getString(NoteTable.NOTE_TIME);
        notePriority = bundle.getInt(NoteTable.NOTE_PRIORITY);
        noteContent = bundle.getString(NoteTable.NOTE_CONTENT);
        contentLength = noteContent.length();
    }

    @Override
    protected void initView() {
        mContent = (EditText) findViewById(R.id.detail_content);
        mContent.setText(noteContent);
        mContent.setCursorVisible(false);
        // 点击后光标出现在文字后
        mContent.setSelection(contentLength);
        btnSend = (Button) findViewById(R.id.detail_bnSend);
        btnDelete = (Button) findViewById(R.id.detail_bnDelete);
        // 判断 重要时间 button
        rgPriority = (RadioGroup) findViewById(R.id.detail_radioGroup);
        rbNormal = (RadioButton) findViewById(R.id.detail_radio0);
        rbImportant = (RadioButton) findViewById(R.id.detail_radio1);
        rbUrgent = (RadioButton) findViewById(R.id.detail_radio2);
        // 初始化显示事件优先级
        if (notePriority == 0) {
            rbNormal.setChecked(true);
        } else if (notePriority == 1) {
            rbImportant.setChecked(true);
        } else if (notePriority == 2) {
            rbUrgent.setChecked(true);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initEvent() {

        mContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mContent.setCursorVisible(true);
            }
        });

        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, mContent.getText().toString().trim());
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "发送到其他APP"));
            }
        });

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditMemoActivity.this);
                builder.setTitle("确认删除？").setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteDAO.delete(noteId);
                        forward(HomeActivity.class);
                    }
                }).setPositiveButton("取消", null).show();
            }
        });

        rgPriority.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.detail_radio0:
                        notePriority = 0;
                        break;
                    case R.id.detail_radio1:
                        notePriority = 1;
                        break;
                    case R.id.detail_radio2:
                        notePriority = 2;
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_deatil_save:
                updateDB();
                break;
            case android.R.id.home:
                break;
        }
        forward(HomeActivity.class);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("是否保存修改？")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateDB();
                        forward(HomeActivity.class);
                    }
                })
                .setNegativeButton("不保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        forward(HomeActivity.class);
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        noteDAO.close();
        super.onDestroy();
    }

    private void updateDB() {
        noteContent = mContent.getText().toString().trim();
        if (noteContent.equals("")) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_LONG).show();
        } else {
            Note note = new Note();
            note.setId(noteId);
            note.setTime(noteTime);
            note.setPriority(notePriority);
            note.setContent(noteContent);
            noteDAO.update(note);
            forward(HomeActivity.class);
        }
    }
}
