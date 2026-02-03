package pt.ipleiria.estg.dei.books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class MenuMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String EMAIL = "email";

    public static final int ADD = 100;
    public static final int EDIT = 200;

    public static final String TOKEN = "token";
    public static final int DEL = 300;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private String email;
    private FragmentManager fragmentManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, toolbar, R.string.ndOpen, R.string.ndClose);
        toggle.syncState();
        drawer.addDrawerListener(toggle);
        carregarCabecalho(); //TODO:criar método

        navigationView.setNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();
        carregarFragmentoInicial();
    }

    private void carregarFragmentoInicial() {
        //aqui carrega o fragmento
        Fragment fragment = new ListaLivrosFragment();
        fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

    }

    //carrega os dados que vem do log in no cabeçalho
    private void carregarCabecalho() {
        email= getIntent().getStringExtra(EMAIL);
        token = getIntent().getStringExtra(TOKEN);

        //aceder à sharedPreference e definir o modo de acesso
        sharedPreferences = getSharedPreferences("DADOS_USER", Context.MODE_PRIVATE);
        //definir o Editor para permitir guardar / alterar o valor
        editor = sharedPreferences.edit();
        if (email!=null || token != null) {
            if (email != null) {
                editor.putString(EMAIL, email);
            }
            if (token != null){
                editor.putString(TOKEN, token);
            }
            editor.apply();
        }else{
            email = sharedPreferences.getString(EMAIL, "Sem email");
        }

        View headerView = navigationView.getHeaderView(0);
        TextView nav_tvEmail = headerView.findViewById(R.id.tvEmail);
        nav_tvEmail.setText(email);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        if (menuItem.getItemId() == R.id.navEstatico){
            fragment = new ListaLivrosFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Estatico");
        } else if (menuItem.getItemId() == R.id.navDinamico){
            //fragment = new DinamicoFragment();
            setTitle(menuItem.getTitle());
            System.out.println("--> Nav Dinamico");
        }else if (menuItem.getItemId() == R.id.navEmail){

            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"+ email));
            intent.putExtra(Intent.EXTRA_SUBJECT, "PSI - AMSI 2025/2026");
            startActivity(intent);

            System.out.println("--> Nav Email");
        }

        if (fragment != null){
            fragmentManager.beginTransaction().replace(R.id.contentFragment, fragment).commit();

        }


        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}