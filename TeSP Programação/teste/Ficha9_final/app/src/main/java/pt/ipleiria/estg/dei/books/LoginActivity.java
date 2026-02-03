package pt.ipleiria.estg.dei.books;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import pt.ipleiria.estg.dei.books.listeners.LoginListener;
import pt.ipleiria.estg.dei.books.modelo.SingletonGestorLivros;

public class LoginActivity extends AppCompatActivity implements LoginListener {

    private TextView email;
    private TextView password;
    private Button btnLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email= findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        SingletonGestorLivros.getInstance(getApplicationContext()).setLoginListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEmailValido(email.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Email inválido!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isPasswordValida(password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Password inválida!!", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    return;
                }

                SingletonGestorLivros.getInstance(getApplicationContext()).loginAPI(email.getText().toString(), password.getText().toString(), getApplicationContext());

                //Tenho de passar o email por parametro
//                Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
//                intent.putExtra(MenuMainActivity.EMAIL, email.getText().toString());
//                startActivity(intent);
//                finish();


                //Toast.makeText(LoginActivity.this, "Login efetuado com sucesso...", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private boolean isEmailValido(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValida(String password){

        if ((password==null)||(password.isEmpty())){
            return false;
        }else if (password.length()<4){
            return false;
        }else
            return true;
    }

    @Override
    public void onValidateLogin(String token, String email) {
        if(token != null) {
            Intent intent = new Intent(getApplicationContext(), MenuMainActivity.class);
            intent.putExtra(MenuMainActivity.EMAIL, email);
            intent.putExtra(MenuMainActivity.TOKEN, token);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, R.string.txt_erro_de_credenciais, Toast.LENGTH_SHORT).show();
        }

    }

    /*
    public void onClickLogin(View view) {


        if (!isEmailValido()){
            Toast.makeText(this, "Email inválido!!", Toast.LENGTH_SHORT).show();
            email.setText("");
            return;
        }

        if (!isPasswordValida()){
            Toast.makeText(this, "Password inválida!!", Toast.LENGTH_SHORT).show();
            password.setText("");
            return;
        }

        Toast.makeText(this, "Correu tudo bem...  .|.", Toast.LENGTH_SHORT).show();
    }

    */
}