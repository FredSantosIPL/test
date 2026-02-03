package pt.ipleiria.estg.dei.books.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedList;

public class LivroDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "livrosDB";
    private static final int DB_VERSION = 1;
    private final SQLiteDatabase database;


    //Tabela Livros
    private static final String TABLE_NAME = "livros";
    public static final String ID = "id";
    public static final String TITULO = "nome";
    public static final String SERIE = "serie";
    public static final String AUTOR = "autor";
    public static final String ANO = "ano";
    public static final String CAPA = "capa";

    public LivroDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLivroTable = "CREATE TABLE " + TABLE_NAME +
                "( " + ID + " INTEGER PRIMARY KEY, " +
                TITULO + " TEXT NOT NULL, " +
                SERIE + " TEXT NOT NULL, " +
                AUTOR + " TEXT NOT NULL, " +
                ANO + " INTEGER NOT NULL, " +
                CAPA + " TEXT" +
                ");";
        db.execSQL(createLivroTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public Livro adicionarLivroBD(Livro livro){
        ContentValues values = new ContentValues();

        values.put(TITULO,livro.getTitulo());
        values.put(SERIE,livro.getSerie());
        values.put(AUTOR,livro.getAutor());
        values.put(ANO,livro.getAno());
        values.put(CAPA, livro.getCapa());

        long id = this.database.insert(TABLE_NAME, null, values);
        // id>-1 significa que correu bem
        if (id > -1){
            //livro.setId((int)id);
            return livro;
        }
        return null;
    }

    public ArrayList<Livro> getAllLivrosBD(){
        ArrayList<Livro> livros = new ArrayList<>();
        Cursor cursor = this.database.query(TABLE_NAME, new String[]{ ID, ANO, CAPA, TITULO, SERIE, AUTOR}, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                livros.add(new Livro(
                        //alternativa colocar fora do livros.add      int aux = cursor.getColumnIndex("ID");
                        // e depois dentro do livros.add utilizar     cursor.getInt(aux),
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return livros;
    }

    public boolean editarLivroBD(Livro livro){
        ContentValues values = new ContentValues();

        values.put(TITULO,livro.getTitulo());
        values.put(SERIE,livro.getSerie());
        values.put(AUTOR,livro.getAutor());
        values.put(ANO,livro.getAno());
        //values.put(CAPA, livro.getCapa());
        return this.database.update(TABLE_NAME, values,
                "id = ?", new String[]{"" + livro.getId()}) > 0;
    }

    public boolean removerLivroBD(long id){
        int affectedRows =  this.database.delete(TABLE_NAME, "id = ?",
                new String[]{"" + id});

        return affectedRows > 0;
    }

    public void removerAllLivrosBD() {
        this.database.delete(TABLE_NAME, null, null);
    }
}
