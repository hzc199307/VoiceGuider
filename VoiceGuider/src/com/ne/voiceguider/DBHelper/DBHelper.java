package com.ne.voiceguider.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * �÷��� DBHelper dbHelper = new DBHelper(this); dbHelper.createDataBase();
 * SQLiteDatabase db = dbHelper.getWritableDatabase(); Cursor cursor =
 * db.query() db.execSQL(sqlString); ע�⣺execSQL��֧�ִ�;�Ķ���SQL��� ��execSQL��Դ��ע��
 * (Multiple statements separated by ;s are not supported.)
 * ����assets�µ����ݿ��ļ�ֱ�Ӹ��Ƶ�DB_PATH�������ݿ��ļ���С������1M����
 * ����г���1M�Ĵ��ļ�������Ҫ�ȷָ�ΪN��С�ļ���Ȼ��ʹ��copyBigDatabase()�滻copyDatabase()
 */

/**
 * �й����ݿ�Ĳ���  Ŀǰ��Ҫ������Ǳ������ݿ��ļ�������
 * @ClassName: DBHelper 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��21�� ����6:58:56 
 *
 */
public class DBHelper extends SQLiteOpenHelper {
	// �û����ݿ��ļ��İ汾
	private static final int DB_VERSION = 1;
	// ���ݿ��ļ�Ŀ����·��ΪϵͳĬ��λ�ã�com.ne.voiceguider ����İ���
	private static String DB_PATH = "/data/data/com.ne.voiceguider/databases/";
	/*
	 * //�����������ݿ��ļ������SD���Ļ� private static String DB_PATH =
	 * android.os.Environment.getExternalStorageDirectory().getAbsolutePath() +
	 * "Ŀ��·��";
	 */
	// ����������̬�����ֱ���Ŀ���ļ������ƺ���assets�ļ����µ��ļ���
	private static String DB_NAME = "city_scene.db";
	private static String ASSETS_NAME = "db/city_scene.db";

	private SQLiteDatabase myDataBase = null;
	private final Context myContext;

	//	/**
	//	 * ������ݿ��ļ��ϴ�ʹ��FileSplit�ָ�ΪС��1M��С�ļ� �����зָ�Ϊ attribution.db.3h1 ~~
	//	 * attribution.db.3h15
	//	 */
	//	// ��һ���ļ�����׺
	//	private static final int ASSETS_SUFFIX_BEGIN = 0;
	//	// ���һ���ļ�����׺
	//	private static final int ASSETS_SUFFIX_END = 15;

	/**
	 * ��SQLiteOpenHelper�����൱�У������иù��캯��
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param name
	 *            ���ݿ�����
	 * @param factory
	 *            һ�㶼��null
	 * @param version
	 *            ��ǰ���ݿ�İ汾��ֵ���������������ǵ�����״̬
	 */
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		// ����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, null, version);
		this.myContext = context;
	}

	public DBHelper(Context context, String name, int version) {
		this(context, name, null, version);
	}

	public DBHelper(Context context, String name) {
		this(context, name, DB_VERSION);
	}

	public DBHelper(Context context) {
		this(context, DB_PATH + DB_NAME);
	}

	/**
	 * �������ݿ�
	 * 
	 * @throws IOException
	 */
	public boolean createDataBase() throws IOException {
		boolean dbExist = checkDataBase();//boolean dbExist = false;//
		if (dbExist) {
			// ���ݿ��Ѵ��ڣ������κβ���
			Log.v("", "���ݿ�û����");
		} else {
			// �������ݿ�
			Log.v("", "���ݿ⵼����");
			try {
				File dir = new File(DB_PATH);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File dbf = new File(DB_PATH + DB_NAME);
				if (dbf.exists()) {
					dbf.delete();
				}
				SQLiteDatabase.openOrCreateDatabase(dbf, null);
				// ����asseets�е����ݿ��ļ���DB_PATH��
				return copyDataBase();
			} catch (IOException e) {
				throw new Error("���ݿⴴ��ʧ��");
			}
		}
		return false;
	}

	/**
	 * ������ݿ��Ƿ���Ч
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		String myPath = DB_PATH + DB_NAME;
		try {
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * ����assets�ļ��е����ݿ⵽ָ��·�� ʹ��������������и���
	 **/
	private boolean copyDataBase() throws IOException{

		InputStream myInput = myContext.getAssets().open(ASSETS_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
		
		return true;
	}

	/**
	 * ����assets�µĴ����ݿ��ļ���ָ��·�� ʹ��������������и���
	 **/
	//	private void copyBigDataBase() throws IOException {
	//		InputStream myInput;
	//		String outFileName = DB_PATH + DB_NAME;
	//		OutputStream myOutput = new FileOutputStream(outFileName);
	//		for (int i = ASSETS_SUFFIX_BEGIN; i < ASSETS_SUFFIX_END + 1; i++) {
	//			myInput = myContext.getAssets().open(ASSETS_NAME + ".3h" + i);
	//			byte[] buffer = new byte[1024];
	//			int length;
	//			while ((length = myInput.read(buffer)) > 0) {
	//				myOutput.write(buffer, 0, length);
	//			}
	//			myOutput.flush();
	//			myInput.close();
	//		}
	//		myOutput.close();
	//	}

	@Override
	public synchronized void close() {
		if (myDataBase != null) {
			myDataBase.close();
		}
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	/**
	 * ���ݿⴴ��ʱִ�У��������Ԥ�Ƶ����ݿ⣬��������ЩдһЩ���������ӳ�ʼ�����ݵĲ��� �磺db.execSQL("create table
	 * cookdata (_id integer primary key,cook_name varchar(20),cook_sort
	 * varchar(20))");
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/**
		 * ���ݿ�����ʱִ�У�ǰ�����Ƕ����DB_VERSION�������ݿ�汾���ڰ汾����ʱִ�� һ����һЩ���ݱ��ݺͻָ��������ݿ�Ĳ�����
		 */
	}

	/**
	 * �����ݵĲ�ѯ
	 * @param sql
	 * @param args
	 * @return
	 */
	public Cursor query(String sql, String[] args) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}
}
