package com.wows.status;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter {

    static final String KEY_ID = "id";
    static final String KEY_TRANSLATE = "translate";
    static final String KEY_TRANSLATED = "translated";
    static final String KEY_MENSAGEM = "mensagem";
    static final String DATABASE_NAME = "wowsstats";
    static final String DATABASE_TABLENAME_MENSAGEM = "mensagem";
    static final int DATABASE_VERSION = 1;

    static final String DATABASE_CREATE_MENSAGEM = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLENAME_MENSAGEM + "(" +
            KEY_ID + " integer primary key autoincrement," +
            KEY_MENSAGEM + " text not null unique ON CONFLICT ABORT," +
            KEY_TRANSLATE + " text," +
            KEY_TRANSLATED + " boolean DEFAULT 0);";

    final Context context;
    DataBaseHelper dataBaseHelper;
    SQLiteDatabase db;

    public DBAdapter(Context cont) {
        context = cont;
        dataBaseHelper = new DataBaseHelper(context);
        db = dataBaseHelper.getWritableDatabase();
    }

    public void close() {
        dataBaseHelper.close();
        db.close();
    }

    public String getMensagemTranslated(int id) {
        String value = "ok";
        Cursor cursor = db.query(DATABASE_TABLENAME_MENSAGEM,
                new String[]{KEY_TRANSLATE}, KEY_ID + "=?", new String[]{id + ""}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(cursor.getColumnIndex(KEY_TRANSLATE));
            cursor.close();
        }

        return value;

    }

    public long insertMensagem(String mensagem, String translate) throws SQLException {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MENSAGEM, mensagem);
        initialValues.put(KEY_TRANSLATE, translate);
        return db.insert(DATABASE_TABLENAME_MENSAGEM, null, initialValues);
    }

    public List<Mensagem> getAllMensagem() throws SQLException {

        List<Mensagem> mensagemList = new ArrayList<>();

        Cursor cursor = db.query(DATABASE_TABLENAME_MENSAGEM,
                new String[]{"id", KEY_MENSAGEM, KEY_TRANSLATE, KEY_TRANSLATED}, null, null, null, null, "id ASC");

        Mensagem mensagem;
        if (cursor.moveToFirst()) {
            do {
                mensagem = new Mensagem();
                mensagem.setId(cursor.getInt(0));
                mensagem.setMensagem(cursor.getString(1));
                mensagem.setTranslated(cursor.getString(2));
                if (cursor.getInt(3) == 0)
                    mensagem.setIsTranlated(false);
                else
                    mensagem.setIsTranlated(true);
                mensagemList.add(mensagem);
            } while (cursor.moveToNext());
        }
        return mensagemList;

    }


    public boolean updateMensagem(int id, String translate) throws SQLException {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TRANSLATE, translate);
        contentValues.put(KEY_TRANSLATED, 1);

        return db.update(DATABASE_TABLENAME_MENSAGEM, contentValues, "id=" + id, null) > 0;

    }

    public int getCountMensagemTranslated() {
        Cursor cursor;
        int count = 0;

        cursor = db.rawQuery("select count(" + KEY_TRANSLATED + ") from " + DATABASE_TABLENAME_MENSAGEM
                + " where " + KEY_TRANSLATED + " =1", null);
        if (cursor.moveToFirst())
            count = cursor.getInt(0);
        cursor.close();
        return count;

    }

    public int getCountMensagem() {
        Cursor cursor;
        int count = 0;

        cursor = db.rawQuery("select count(" + KEY_MENSAGEM + ") from " + DATABASE_TABLENAME_MENSAGEM, null);
        if (cursor.moveToFirst())
            count = cursor.getInt(0);
        cursor.close();

        return count;


    }

    private static class DataBaseHelper extends SQLiteOpenHelper {


        DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            try {
                 sqLiteDatabase.execSQL(DATABASE_CREATE_MENSAGEM);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLENAME_MENSAGEM);
            onCreate(sqLiteDatabase);
        }

    }

    public class Mensagem {
        int id;
        String mensagem;
        String translated;
        boolean isTranlated;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMensagem() {
            return mensagem;
        }

        public void setMensagem(String mensagem) {
            this.mensagem = mensagem;
        }

        public String getTranslated() {
            return translated;
        }

        public void setTranslated(String translated) {
            this.translated = translated;
        }

        public boolean getIsTranlated() {
            return isTranlated;
        }

        public void setIsTranlated(boolean isTranlated) {
            this.isTranlated = isTranlated;
        }


    }


}
