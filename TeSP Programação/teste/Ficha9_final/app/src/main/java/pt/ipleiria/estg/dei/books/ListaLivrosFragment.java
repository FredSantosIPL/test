package pt.ipleiria.estg.dei.books;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.adaptadores.ListaLivrosAdaptador;
import pt.ipleiria.estg.dei.books.listeners.LivrosListener;
import pt.ipleiria.estg.dei.books.modelo.Livro;
import pt.ipleiria.estg.dei.books.modelo.SingletonGestorLivros;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaLivrosFragment newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaLivrosFragment extends Fragment implements LivrosListener {


    private ListView lvLivros;

    private ArrayList<Livro> livros;

    private ListaLivrosAdaptador adaptador;
    private FloatingActionButton fabLista;

    private SearchView searchView;
    private String token;

    public ListaLivrosFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista_livros, container, false);
        lvLivros = view.findViewById(R.id.lvLivros);
        SingletonGestorLivros.getInstance(getContext()).getAllLivrosAPI(getContext());



        fabLista = view.findViewById(R.id.fabLista);
        setHasOptionsMenu(true);

        //dizer o Singleton para criar o setter do LivrosListener
        SingletonGestorLivros.getInstance(getContext()).setLivrosListener(this);

        fabLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getContext(), DetalhesLivroActivity.class);
                startActivityForResult(intent, MenuMainActivity.ADD);
            }
        });

        lvLivros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvTitulo = view.findViewById(R.id.etTitulo);
                String titulo = tvTitulo.getText().toString();

                //titulo = livros.get(position).getTitulo();

               // Livro livro = livros.get(position);
                //int idLivro = livro.getId();
//                livros = SingletonGestorLivros.getInstance(getContext()).getLivrosBD();
//                Livro livro = livros.get(position);

                Intent intent = new Intent (getContext(), DetalhesLivroActivity.class);
                intent.putExtra(DetalhesLivroActivity.IDLIVRO, (int) id);
                startActivityForResult(intent, MenuMainActivity.EDIT);

                Toast.makeText(getContext(), titulo, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK){


            SingletonGestorLivros.getInstance(getContext()).getAllLivrosAPI(getContext());
            //lvLivros.setAdapter(new ListaLivrosAdaptador(getContext(), livros));

            if (requestCode == MenuMainActivity.ADD){
                Toast.makeText(getContext(), R.string.txt_livro_adicionado_com_sucesso, Toast.LENGTH_LONG).show();
            } else if (requestCode == MenuMainActivity.EDIT){
                Toast.makeText(getContext(), R.string.txt_livro_modificado_com_sucesso, Toast.LENGTH_LONG).show();
            }

        }

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_pesquisa, menu);
        MenuItem itemPesquisa = menu.findItem(R.id.itemPesquisa);
        searchView = (SearchView) itemPesquisa.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Livro> tempLivros = new ArrayList<>();
                for (Livro livro: SingletonGestorLivros.getInstance(getContext()).getLivrosBD()) {
                    if (livro.getTitulo().toLowerCase().contains(newText.toLowerCase())){
                        tempLivros.add(livro);
                    }
                }
                adaptador = new ListaLivrosAdaptador(getContext(), tempLivros);
                lvLivros.setAdapter(adaptador);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }

    @Override
    public void onRefreshListaLivros(ArrayList<Livro> listaLivros) {
        lvLivros.setAdapter(new ListaLivrosAdaptador(getContext(), listaLivros));
    }
}