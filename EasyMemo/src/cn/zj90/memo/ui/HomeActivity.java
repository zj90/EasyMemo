package cn.zj90.memo.ui;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.zj90.memo.R;
import cn.zj90.memo.db.NoteDAO;
import cn.zj90.memo.db.model.Note;
import cn.zj90.memo.db.table.NoteTable;
import cn.zj90.memo.ui.preferences.AboutActivity;
import cn.zj90.memo.ui.preferences.FeedBackActivity;
import cn.zj90.memo.ui.preferences.SettingsActivity;
import cn.zj90.memo.util.PreferencesUtil;

public class HomeActivity extends BaseActivity {
    // var
    public List<Note> noteList;
    private int itemDistance;
    private long lastExitTime = 0;

    private static final String DOWNLOAD_URL = "http://shouji.baidu.com/software/item?docid=7187096&from=as";
    private static final String DOWNLOAD_URL2 = "http://apk.91.com/Soft/Android/cn.zj90.memo.activity-1-1.0.0.html";

    // obj
    private NoteDAO noteDAO;
    private MainListAdapter _adapter;

    // view
    private ListView lvMain;
    private ListView drawerList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UmengUpdateAgent.setUpdateListener(null);
        adapteListView();
    }

    @Override
    protected void beforeInit() {
        setContentView(R.layout.activity_home);
        // use umeng for update
        UmengUpdateAgent.update(this);
    }

    @Override
    protected void initData() {
        noteDAO = new NoteDAO(this);
    }

    @Override
    protected void initView() {
        lvMain = (ListView) findViewById(R.id.lv_activity_main_main);
        initNavigationDrawer();
        adapteListView();
    }

    private void initNavigationDrawer() {
        // set drawer listview
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.navigation_drawer);
        int[] iconItems = {R.drawable.settings, R.drawable.update, R.drawable.heart, R.drawable.mail, R.drawable.about};
        String[] titleItems = {"应用设置", "软件更新", "推荐给好友", "意见反馈", "关于简单备忘录"};
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < titleItems.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("icon", iconItems[i]);
            map.put("title", titleItems[i]);
            dataList.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, dataList,
                R.layout.item_home_navigation, new String[]{"icon", "title"},
                new int[]{R.id.navigation_icon, R.id.navigation_title});
        drawerList.setAdapter(adapter);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {
        };
        drawerLayout.setDrawerListener(drawerToggle);

        // set drawer actionbar
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeButtonEnabled(true);
    }

    @Override
    protected void initEvent() {
        lvMain.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note note = noteList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt(NoteTable.NOTE_ID, note.getId());
                bundle.putString(NoteTable.NOTE_TIME, note.getTime());
                bundle.putInt(NoteTable.NOTE_PRIORITY, note.getId());
                bundle.putString(NoteTable.NOTE_CONTENT, note.getContent());
                forward(EditMemoActivity.class, bundle);
            }
        });

        lvMain.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int finalPosition = position;
                final Note note = noteList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setItems(new String[]{"编辑", "删除", "返回"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putInt(NoteTable.NOTE_ID, note.getId());
                                bundle.putString(NoteTable.NOTE_TIME, note.getTime());
                                bundle.putInt(NoteTable.NOTE_PRIORITY, note.getId());
                                bundle.putString(NoteTable.NOTE_CONTENT, note.getContent());
                                forward(EditMemoActivity.class, bundle);
                                break;
                            case 1:
                                delete(note, finalPosition);
                                break;
                            case 2:
                                break;
                        }
                    }
                }).show();
                return true;
            }
        });

        drawerList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (position) {
                    case 0:
                        forward(SettingsActivity.class);
                        break;
                    // update
                    case 1:
                        UmengUpdateAgent.setUpdateAutoPopup(false);
                        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                            @Override
                            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                                switch (updateStatus) {
                                    case UpdateStatus.Yes:
                                        UmengUpdateAgent.showUpdateDialog(HomeActivity.this, updateInfo);
                                        break;
                                    case UpdateStatus.No:
                                        Toast.makeText(HomeActivity.this, "已经是最新版本", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        });
                        UmengUpdateAgent.update(HomeActivity.this);
                        break;
                    // promote to friends
                    case 2:
                        intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        Spanned sp = Html.fromHtml("简单备忘录,简单好用的备忘录,下载地址1：" + HomeActivity.DOWNLOAD_URL + "\n" + "下载地址2:"
                                + HomeActivity.DOWNLOAD_URL2);
                        intent.putExtra(Intent.EXTRA_TEXT, sp.toString());
                        intent.setType("text/plain");
                        startActivity(Intent.createChooser(intent, "推荐给好友"));
                        break;
                    // advice
                    case 3:
                        forward(FeedBackActivity.class);
                        break;
                    // about
                    case 4:
                        forward(AboutActivity.class);
                        break;
                }
            }
        });

    }

    private void delete(Note note, int position) {
        // list delete
        noteList.remove(note);
        // db delete
        noteDAO.delete(note.getId());
        // update litview
        lvMain.setAdapter(_adapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void adapteListView() {
        Context context = this.getApplicationContext();
        PreferencesUtil.initShardPreferences(context);
        Boolean isSortByEvent = PreferencesUtil.readBoolean(PreferencesUtil.Settings.SORT_EVENT);
        Boolean isSortByTime = PreferencesUtil.readBoolean(PreferencesUtil.Settings.SORT_TIME);
        itemDistance = Integer.parseInt(PreferencesUtil.readString(PreferencesUtil.Settings.ITEM_DISTANCE));

        if (isSortByEvent == true && isSortByTime == true) {
            noteList = noteDAO.readNoteWithPriorityTimeIncre();
        } else if (isSortByEvent == true && isSortByTime == false) {
            noteList = noteDAO.readNoteWithPriorityTimeDecre();
        } else if (isSortByEvent == false && isSortByTime == true) {
            noteList = noteDAO.readNoteTimeIncre();
        } else {
            noteList = noteDAO.readNoteTimeDecre();
        }
        _adapter = new MainListAdapter(this);
        lvMain.setAdapter(_adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.addItem:
                forward(NewMemoActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - lastExitTime) > 1000) {
            toast("再按一次退出程序");
            lastExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        noteDAO.close();
        super.onDestroy();
    }

    private class MainListAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public MainListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return noteList.size();
        }

        @Override
        public Object getItem(int position) {
            return noteList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_home_note, null);
                holder = new ViewHolder();
                holder.rootView = convertView.findViewById(R.id.item_home_note_root);
                holder.priorityView = convertView.findViewById(R.id.item_isImportant);
                holder.content = (TextView) convertView.findViewById(R.id.item_content);
                holder.time = (TextView) convertView.findViewById(R.id.item_time);
                holder.btnDelete = (Button) convertView.findViewById(R.id.item_home_note_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // change noteitem distance
            holder.rootView.setPadding(0,itemDistance,0,itemDistance);
            // set data
            final Note note = noteList.get(position);
            int priority = note.getPriority();
            if (priority == Note.Priority.IMPORTANT) {
                holder.priorityView.setBackgroundColor(getResources().getColor(R.color.orange));
            } else if (priority == Note.Priority.URGENT) {
                holder.priorityView.setBackgroundColor(getResources().getColor(R.color.red));
            }
            holder.time.setText(note.getTime());
            holder.content.setText(note.getShortContent());

            final int finalPosition = position;
            holder.btnDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(note, position);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public View rootView;
            public View priorityView;
            public TextView content;
            public TextView time;
            public Button btnDelete;
        }
    }


}
