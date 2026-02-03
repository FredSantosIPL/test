package pt.ipleiria.estg.dei.books.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.books.modelo.Livro;

public class LivroJsonParser
{



    public static ArrayList<Livro> parserJsonLivros(JSONArray response){
        ArrayList<Livro> livros = new ArrayList<>();
        try{
            for (int i=0; i< response.length(); i++){
                JSONObject livro = response.getJSONObject(i);
                int idLivro = livro.getInt("id");
                String titulo = livro.getString("titulo");
                String autor = livro.getString("autor");
                String serie = livro.getString("serie");
                int ano = livro.getInt("ano");
                String capa = livro.getString("capa");

                //Se receber um array dentro da resposta, temos de fazer outro FOR para colocar dentro do outro array

                Livro auxLivro = new Livro(idLivro, ano, capa, titulo, serie, autor);

                livros.add(auxLivro);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

        return livros;
    }

    public static Livro parserJsonLivro(String response){
        Livro auxLivro = null;
        try{
            JSONObject livro = new JSONObject(response);

            int idLivro = livro.getInt("id");
            String titulo = livro.getString("titulo");
            String autor = livro.getString("autor");
            String serie = livro.getString("serie");
            int ano = livro.getInt("ano");
            String capa = livro.getString("capa");

            auxLivro = new Livro(idLivro, ano, capa, titulo, serie, autor);

        }catch (JSONException e) {
            e.printStackTrace();
        }

        return auxLivro;
    }

    public static String parserJsonLogin(String response){
        String token = null;
        try{
            JSONObject login = new JSONObject(response);
            if (login.getBoolean("success")){
                token = login.getString("token");
            }

        }catch (JSONException e){
            e.printStackTrace();
        }

        return token;
    }

    public static boolean isConnectionInternet(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //necessita de permissões de acesso a internet
        //e acesso ao estado la ligação
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
