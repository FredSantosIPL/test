package pt.ipleiria.estg.dei.books.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.R;
import pt.ipleiria.estg.dei.books.modelo.Livro;


public class ListaLivrosAdaptador extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    ArrayList<Livro> livros;

    public ListaLivrosAdaptador(Context context, ArrayList<Livro> livros) {
        this.context = context;
        this.livros = livros;
    }

    @Override
    public int getCount() {
        return livros.size();
    }

    @Override
    public Object getItem(int position) {
        return livros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return livros.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Livro livro = livros.get(position);
        if (inflater == null){
            inflater = LayoutInflater.from(context);

        }

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_lista_livro, null);
        }

        ViewHolderLista viewHolder = (ViewHolderLista) convertView.getTag();
        if (viewHolder == null){
            viewHolder = new ViewHolderLista(convertView);
            convertView.setTag(viewHolder);   // funciona como o putExtra mas para avista
        }

        viewHolder.update(livro);

        return convertView;
    }


    private class ViewHolderLista{
        private TextView tvTitulo, tvSerie, tvAno, tvAutor;
        private ImageView imgCapa;

        public ViewHolderLista(View view) {
            tvTitulo = view.findViewById(R.id.etTitulo);
            tvSerie = view.findViewById(R.id.etSerie);
            tvAutor = view.findViewById(R.id.etAutor);
            tvAno = view.findViewById(R.id.etAno);
            imgCapa = view.findViewById(R.id.imgCapa);
        }

        public void update(Livro livro){
            tvTitulo.setText(livro.getTitulo());
            tvSerie.setText(livro.getSerie());
            tvAutor.setText(livro.getAutor());
            tvAno.setText(livro.getAno()+"");
            //imgCapa.setImageResource(livro.getCapa());
            Glide.with(context)
                    .load(livro.getCapa())
                    .placeholder(R.drawable.logoipl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgCapa);
        }
    }
}
