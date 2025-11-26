package pt.ipleiria.estg.dei.books.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;



public class LivroBDHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "livrosDB";
    private static final String TABLE_NAME = "livros";
    private static final int DB_VERSION = 1;
    public static final String ID = "id";
    public static final String TITULO = "titulo";
    public static final String SERIE = "serie";
    public static final String AUTOR = "autor";
    public static final String ANO = "ano";
    public static final String CAPA = "capa";

    private final SQLiteDatabase database;

    public LivroBDHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        String createContactoTable = "CREATE TABLE " + TABLE_NAME +
                "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITULO + " TEXT NOT NULL, " +
                SERIE + " TEXT NOT NULL, " +
                AUTOR + " TEXT NOT NULL, " +
                ANO + " INTEGER NOT NULL, " +
                CAPA + " INTEGER" +
                ");";
        db.execSQL(createContactoTable);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);

    }

    public void adicionarLivroBD(Livro livro){
        ContentValues values = new ContentValues();
        values.put(ID, livro.getId());
        values.put(TITULO, livro.getTitulo());
        values.put(SERIE,livro.getSerie());
        values.put(AUTOR, livro.getAutor());
        values.put(ANO, livro.getAno());
        values.put(CAPA, livro.getCapa());
        long id = this.database.insert(TABLE_NAME, null, values);
        if(id > -1){
            livro.setId((int) id);
        }
    }
}
