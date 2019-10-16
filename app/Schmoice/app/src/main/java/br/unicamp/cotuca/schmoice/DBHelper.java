package br.unicamp.cotuca.schmoice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "schmoice.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Jogo (" +
                "_ID INT PRIMARY KEY," +
                "info ntext NOT NULL);");
        db.execSQL("CREATE TABLE Fase (" +
                "_ID INT PRIMARY KEY," +
                "info ntext NOT NULL);");
        db.execSQL("CREATE TABLE Nivel (" +
                "_ID INT PRIMARY KEY," +
                "info ntext NOT NULL);");
        db.execSQL("CREATE TABLE NivelFase (" +
                "_ID INT PRIMARY KEY," +
                "info ntext NOT NULL);");
        db.execSQL("CREATE TABLE Escolha (" +
                "_ID INT PRIMARY KEY," +
                "info ntext NOT NULL);");;
        db.execSQL("CREATE TABLE EscolhaNivel (" +
                "_ID INT PRIMARY KEY," +
                "info ntext NOT NULL);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Implementar a atualização do banco baseado nas versões (oldVersion e newVersion)
    }
}
