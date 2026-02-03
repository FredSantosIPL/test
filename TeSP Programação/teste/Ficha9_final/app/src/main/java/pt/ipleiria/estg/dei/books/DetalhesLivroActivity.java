package pt.ipleiria.estg.dei.books;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import pt.ipleiria.estg.dei.books.listeners.LivroListener;
import pt.ipleiria.estg.dei.books.listeners.LivrosListener;
import pt.ipleiria.estg.dei.books.modelo.Livro;
import pt.ipleiria.estg.dei.books.modelo.SingletonGestorLivros;

public class DetalhesLivroActivity extends AppCompatActivity implements LivroListener {

    public static final String IDLIVRO = "idLivro";

    EditText etTitulo, etAutor, etAno, etSerie;

    private FloatingActionButton fabGuardar;
    private Livro livro;
    ImageView imgCapa;

    Integer idLivro = -1;
    public static final String DEFAULT_IMG =
            "http://amsi.dei.estg.ipleiria.pt/img/ipl_semfundo.png";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String tokenAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhes_livro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });


        livro = SingletonGestorLivros.getInstance(getApplicationContext()).getLivro(idLivro);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitulo = findViewById(R.id.etTitulo);
        etSerie = findViewById(R.id.etSerie);
        etAutor = findViewById(R.id.etAutor);
        etAno = findViewById(R.id.etAno);
        imgCapa = findViewById(R.id.imgCapa);
        fabGuardar = findViewById(R.id.fabGuardar);
        SingletonGestorLivros.getInstance(getApplicationContext()).setLivroListener(this);


        idLivro = getIntent().getIntExtra("idLivro", -1);

        //edicao do livro
        if (idLivro!=-1){
            livro = SingletonGestorLivros.getInstance(getApplicationContext()).getLivro(idLivro);
            setTitle(getString(R.string.txt_detalhes)+livro.getTitulo());
            etTitulo.setText(livro.getTitulo());
            etSerie.setText(livro.getSerie());
            etAutor.setText(livro.getAutor());
            etAno.setText(livro.getAno()+"");
            //imgCapa.setImageResource(livro.getCapa());
            Glide.with(getApplicationContext())
                    .load(livro.getCapa())
                    .placeholder(R.drawable.logoipl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
            fabGuardar.setImageResource(R.drawable.ic_action_guardar);
        }

        //criar livro
        else{
            setTitle(getString(R.string.txt_adicionar_livro));
            fabGuardar.setImageResource(R.drawable.ic_action_adicionar);

        }

        fabGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getToken();

                //se livro diferente de -1 estamos a editar
                if(idLivro!=-1){
                    if(isLivroValido()){
                        //livro.setId(0);
                        livro.setSerie(etSerie.getText().toString());
                        livro.setTitulo(etTitulo.getText().toString());
                        livro.setAutor(etAutor.getText().toString());
                        livro.setAno(Integer.parseInt(etAno.getText().toString()));

                        SingletonGestorLivros.getInstance(getApplicationContext()).editarLivroAPI(tokenAPI, livro, getApplicationContext());
//                        setResult(RESULT_OK);
//                        finish();
                    }
                }else if(isLivroValido()){
                    livro = new Livro(
                            0,
                            Integer.parseInt(etAno.getText().toString()),
                           DEFAULT_IMG,
                            etTitulo.getText().toString(),
                            etSerie.getText().toString(),
                            etAutor.getText().toString()
                    );
                    SingletonGestorLivros.getInstance(getApplicationContext()).adicionarLivroAPI(tokenAPI, livro, getApplicationContext());
//                    setResult(RESULT_OK);
//                    finish();
                }

            }
        });


    }

    private void getToken() {
        sharedPreferences = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tokenAPI = sharedPreferences.getString(MenuMainActivity.TOKEN, "SEM TOKEN");
    }

    private boolean isLivroValido() {
        //validacoes
        String titulo = etTitulo.getText().toString();
        String serie = etSerie.getText().toString();
        String autor = etAutor.getText().toString();
        String ano = etAno.getText().toString();

        if (titulo.length()<3) {
            etTitulo.setError(getString(R.string.txt_erro_titulo_invalido));
            return false;
        }
        if (serie.length()<3) {
            etSerie.setError(getString(R.string.txt_erro_serie_invalida));
            return false;
        }
        if (autor.length()<3) {
            etAutor.setError(getString(R.string.txt_erro_autor_invalido));
            return false;
        }
        if (!isAnoValido(ano)) {
            etSerie.setError(getString(R.string.txt_erro_ano_invalido));
            return false;
        }

        return true;
    }

    private boolean isAnoValido(String ano) {
        if (ano.length()!=4){
            etAno.setError(getString(R.string.txt_erro_ano_invalido));
            return false;
        }
        //verificar so o ano é inteiro
        int anoInt;
        try {
            anoInt = Integer.parseInt(ano);
        }catch (NumberFormatException e){
            etAno.setError(getString(R.string.txt_erro_ano_invalido));
            return false;
        }

        //validar se o ano é inferior ao ano atual
        int anoAtual = java.util.Calendar.getInstance().get(Calendar.YEAR);
        if (anoInt <= 1000 ||anoInt > anoAtual){
            etAno.setError(getString(R.string.txt_erro_ano_invalido));
            return false;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (idLivro!=-1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_remover, menu);
        }
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //aqui apanha os clicks nos botões do Menu
        if (item.getItemId() == R.id.itemRemover){
            //aqui o codigo para remover
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.txt_remover_livro)
                    .setIcon(android.R.drawable.ic_delete)
                    .setMessage(R.string.txt_tem_a_certeza_que_pretende_remover_o_livro)
                    .setPositiveButton(getString(R.string.txt_sim), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SingletonGestorLivros.getInstance(getApplicationContext()).removerLivroAPI(livro, getApplicationContext());
                            //podemos passar dados pelo RESULT_OK criando um intent
//                            setResult(RESULT_OK);
//                            finish();
                        }
                    })
                        .setNegativeButton(getString(R.string.txt_cancelar), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               //Neste caso não faz nada
                            }
                        }).show();

            //Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefreshDetalhes(int op) {
        setResult(RESULT_OK);
        finish();
    }
}