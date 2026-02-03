package pt.ipleiria.estg.dei.books.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.modelo.Livro;

public interface LivrosListener {
    void
    onRefreshListaLivros(ArrayList<Livro> listaLivros);
}
