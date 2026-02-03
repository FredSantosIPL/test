package pt.ipleiria.estg.dei.books.listeners;

public interface LoginListener {
    void onValidateLogin(final String token, final String email);

}
