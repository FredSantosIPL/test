package pt.ipleiria.estg.dei.books.modelo;

// garante que a classe só existe uma vez no projeto todo

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pt.ipleiria.estg.dei.books.DetalhesLivroActivity;
import pt.ipleiria.estg.dei.books.MenuMainActivity;
import pt.ipleiria.estg.dei.books.R;
import pt.ipleiria.estg.dei.books.listeners.LivroListener;
import pt.ipleiria.estg.dei.books.listeners.LivrosListener;
import pt.ipleiria.estg.dei.books.listeners.LoginListener;
import pt.ipleiria.estg.dei.books.utils.LivroJsonParser;


public class SingletonGestorLivros {
    private static SingletonGestorLivros instance = null;
    private ArrayList<Livro> livros;
    private LivroDBHelper livrosBD = null;

    private static RequestQueue volleyQueue;

    private String mUrlAPILivros = "http://amsi.dei.estg.ipleiria.pt/api/livros";

   // private static String TOKEN = "AMSI-TOKEN";
    private LivrosListener livrosListener;
    private LoginListener loginListener;

    private LivroListener livroListener;
    private String getmUrlAPILogin = "http://amsi.dei.estg.ipleiria.pt/api/auth/login";


    public static synchronized SingletonGestorLivros getInstance(Context context){
        if (instance == null){
            instance = new SingletonGestorLivros(context);
            volleyQueue = Volley.newRequestQueue(context);
        }
        return instance;
    }
    public SingletonGestorLivros(Context context) {

        livros = new ArrayList<>();

        livrosBD = new LivroDBHelper(context);
    }

    public void setLivrosListener(LivrosListener livrosListener) {
        this.livrosListener = livrosListener;
    }

    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void setLivroListener(LivroListener livroListener) {
        this.livroListener = livroListener;
    }

    public void loginAPI(final String email, final String password, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request = new StringRequest(Request.Method.POST, getmUrlAPILogin, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    String token = LivroJsonParser.parserJsonLogin(s);
                    if(loginListener != null) {
                        loginListener.onValidateLogin(token, email);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError.networkResponse != null){
                        int statusCode = volleyError.networkResponse.statusCode;
                        System.out.println("-->STATUS: " + statusCode);
                    }else{
                        System.out.println("-->Erro: TIMEOUT OU SEM NET");
                    }
                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override 
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email",email);
                    params.put("password", password);
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void editarLivroAPI(String tokenAPI, final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request = new StringRequest(Request.Method.PUT, mUrlAPILivros+"/"+livro.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    editarLivroBD(livro);
                    if (livroListener != null){
                        livroListener.onRefreshDetalhes(MenuMainActivity.EDIT);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("titulo", livro.getTitulo());
                    params.put("serie", livro.getSerie());
                    params.put("autor", livro.getAutor());
                    params.put("ano", livro.getAno()+"");
                    String capa = livro.getCapa();
                    params.put("capa", capa == null ? DetalhesLivroActivity.DEFAULT_IMG : capa);

                    params.put("token", tokenAPI);
                    return params;
                }
            };
            volleyQueue.add(request);
        }

    }

    public void removerLivroAPI(final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else {
            StringRequest request = new StringRequest(Request.Method.DELETE, mUrlAPILivros + "/" + livro.getId(), new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    //remove da BD e o metodo chamado remove do Singleton
                    removerLivroBD(livro.getId());
                    if (livroListener != null){
                        livroListener.onRefreshDetalhes(MenuMainActivity.DEL);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            volleyQueue.add(request);
        }
    }

    public void getAllLivrosAPI( final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, mUrlAPILivros, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray jsonArray) {
                    //atualiza o singleton
                    livros = LivroJsonParser.parserJsonLivros(jsonArray);
                    //atualiza a BD
                    adicionarLivrosBD(livros);
                    if (livrosListener != null) {
                        livrosListener.onRefreshListaLivros(livros);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //params.put("token", tokenAPI);
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    //faz o pedido POST a API para adicionar um livro
    public void adicionarLivroAPI(String tokenAPI, final Livro livro, final Context context){
        if(!LivroJsonParser.isConnectionInternet(context)){
            Toast.makeText(context, R.string.txt_nao_tem_internet, Toast.LENGTH_SHORT).show();
        }else{
            StringRequest request = new StringRequest(Request.Method.POST, mUrlAPILivros, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    Livro livro = LivroJsonParser.parserJsonLivro(s);
                    adicionarLivroBD(livro);
                    if (livroListener != null){
                        livroListener.onRefreshDetalhes(MenuMainActivity.ADD);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("titulo", livro.getTitulo());
                    params.put("serie", livro.getSerie());
                    params.put("autor", livro.getAutor());
                    params.put("ano", livro.getAno()+"");
                    String capa = livro.getCapa();
                    params.put("capa", capa == null ? DetalhesLivroActivity.DEFAULT_IMG : capa);

                    params.put("token", tokenAPI);
                    return params;
                }
            };
            volleyQueue.add(request);
        }
    }

    public void adicionarLivroBD(Livro livro){
        Livro auxLivro = livrosBD.adicionarLivroBD(livro);
        if (auxLivro != null){
            livros.add(auxLivro);
        }

    }

    public void adicionarLivrosBD(ArrayList<Livro> livros){
        livrosBD.removerAllLivrosBD();
        for (Livro l: livros){
            livrosBD.adicionarLivroBD(l);
        }

    }

    public void editarLivroBD(Livro livro){
        Livro l =  getLivro(livro.getId());
        if (l != null){
            livrosBD.editarLivroBD(livro);
        }
    }

    public void removerLivroBD(int idLivro){
        Livro l = getLivro(idLivro);
        if (l != null) {
            if (livrosBD.removerLivroBD(l.getId())) {
                livros.remove(l);
            }
        }
    }

    public ArrayList<Livro> getLivrosBD() {
        livros = livrosBD.getAllLivrosBD();
        return new ArrayList<>(livros);
    }

    public Livro getLivro(int idLivro){
       // Livro livro = null;

        for (Livro livro: livros) {
            if (livro.getId()==idLivro){
                return livro;
            }
        }
        return null;
    }
}
