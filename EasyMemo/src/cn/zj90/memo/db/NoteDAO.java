package cn.zj90.memo.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.zj90.memo.db.model.Note;
import cn.zj90.memo.db.table.NoteTable;

/**
 * @author ZhangJian
 * @date 2014-12-4 上午11:07:11
 */
public class NoteDAO {

	private DBOpenHelper dbOpenHelper;
	private SQLiteDatabase db;

	public NoteDAO(Context context) {
		dbOpenHelper = new DBOpenHelper(context);
		db = dbOpenHelper.getWritableDatabase();
	}

	public void add(Note note) {
		ContentValues cv = new ContentValues();
		cv.put(NoteTable.NOTE_TIME, note.getTime());
		cv.put(NoteTable.NOTE_PRIORITY, note.getPriority());
		cv.put(NoteTable.NOTE_CONTENT, note.getContent());
		db.insert(NoteTable.TABLE_NAME, null, cv);
	}

	public void delete(int id) {
		String strId = String.valueOf(id);
		db.delete(NoteTable.TABLE_NAME, "id=?", new String[]{strId});
	}

	public void update(Note note) {
		ContentValues cv = new ContentValues();
		cv.put(NoteTable.NOTE_TIME, note.getTime());
		cv.put(NoteTable.NOTE_PRIORITY, note.getPriority());
		cv.put(NoteTable.NOTE_CONTENT, note.getContent());
		db.update(NoteTable.TABLE_NAME, cv, "id=?", new String[]{String.valueOf(note.getId())});
	}

	public List<Note> readNoteWithPriorityTimeIncre() {
		List<Note> result = new ArrayList<Note>();
		queryNoteByPriorityInOrder(Note.Priority.URGENT, result);
		queryNoteByPriorityInOrder(Note.Priority.IMPORTANT, result);
		queryNoteByPriorityInOrder(Note.Priority.NORMAL, result);
		return result;
	}

	public List<Note> readNoteWithPriorityTimeDecre() {
		List<Note> result = new ArrayList<Note>();
		queryNoteByPriorityReverseOrder(Note.Priority.URGENT, result);
		queryNoteByPriorityReverseOrder(Note.Priority.IMPORTANT, result);
		queryNoteByPriorityReverseOrder(Note.Priority.NORMAL, result);
		return result;
	}

	public List<Note> readNoteTimeIncre() {
		List<Note> result = new ArrayList<Note>();
		Cursor cursor = db.query(NoteTable.TABLE_NAME, null, null, null, null, null, null);
		readNoteInOrder(cursor, result);
		return result;
	}

	public List<Note> readNoteTimeDecre() {
		List<Note> result = new ArrayList<Note>();
		Cursor cursor = db.query(NoteTable.TABLE_NAME, null, null, null, null, null, null);
		readNoteReverseOrder(cursor, result);
		return result;
	}

	public void close() {
		db.close();
		dbOpenHelper.close();
	}

	private void queryNoteByPriorityInOrder(int priority, List<Note> result) {
		Cursor cursor = db.query(NoteTable.TABLE_NAME, null, NoteTable.NOTE_PRIORITY + "=?",
				new String[]{String.valueOf(priority)}, null, null, null);
		readNoteInOrder(cursor, result);
	}

	private void queryNoteByPriorityReverseOrder(int priority, List<Note> result) {
		Cursor cursor = db.query(NoteTable.TABLE_NAME, null, NoteTable.NOTE_PRIORITY + "=?",
				new String[]{String.valueOf(priority)}, null, null, null);
		readNoteReverseOrder(cursor, result);
	}

	private void readNoteInOrder(Cursor cursor, List<Note> noteList) {
		while (cursor.moveToNext()) {
			Note note = new Note();
			note.setId(cursor.getInt(0));
			note.setTime(cursor.getString(1));
			note.setPriority(Integer.valueOf(cursor.getString(2)));
			note.setContent(cursor.getString(3));
			String shortContent = cursor.getString(3);
			if (shortContent.length() > 10) {
				shortContent = shortContent.substring(0, 9) + "...";
			}
			note.setShortContent(shortContent);
			noteList.add(note);
		}
		cursor.close();
	}

	private void readNoteReverseOrder(Cursor cursor, List<Note> noteList) {
		if (cursor.moveToLast()) {
			do {
				Note note = new Note();
				note.setId(cursor.getInt(0));
				note.setTime(cursor.getString(1));
				note.setPriority(Integer.valueOf(cursor.getString(2)));
				note.setContent(cursor.getString(3));
				String shortContent = cursor.getString(3);
				if (shortContent.length() > 10) {
					shortContent = shortContent.substring(0, 9) + "...";
				}
				note.setShortContent(shortContent);
				noteList.add(note);
			} while (cursor.moveToPrevious());
		}
		cursor.close();
	}


}
