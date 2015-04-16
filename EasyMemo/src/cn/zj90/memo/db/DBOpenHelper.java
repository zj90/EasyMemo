package cn.zj90.memo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.zj90.memo.db.table.NoteTable;

public class DBOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "memo.db";
	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_NOTE_TABLE_SQL = "create table " + NoteTable.TABLE_NAME
			+ "("
			+ NoteTable.NOTE_ID + " integer primary key autoincrement,"
			+ NoteTable.NOTE_TIME + " text,"
			+ NoteTable.NOTE_PRIORITY + " text,"
			+ NoteTable.NOTE_CONTENT + " text"
			+ ");";

	
	public DBOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_NOTE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("drop table if exists " + NoteTable.TABLE_NAME);
		onCreate(db);
	}

}
