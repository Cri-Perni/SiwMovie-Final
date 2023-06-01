package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;


import it.uniroma3.siw.repository.UserRepository;

@Service
public class OidcUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Qui puoi implementare la logica per caricare l'utente dal tuo sistema di autenticazione

        // Ad esempio, se l'oggetto DefaultOidcUser rappresenta un utente autenticato tramite OAuth2,
        // puoi effettuare una query nel tuo database per ottenere le informazioni dell'utente
        // e costruire un oggetto UserDetails personalizzato

        DefaultOidcUser oidcUser = getUserFromDatabase(username);
        if (oidcUser != null) {
            return convertToUserDetails(oidcUser);
        }

        throw new UsernameNotFoundException("User not found");
    }

    private DefaultOidcUser getUserFromDatabase(String username) {
        // Implementa la logica per ottenere l'oggetto DefaultOidcUser dal tuo database o da un altro sistema di autenticazione
        // Restituisci null se l'utente non viene trovato
        // Puoi utilizzare ad esempio JPA o JDBC per interrogare il database
        // e ottenere le informazioni dell'utente in base al suo nome utente (username)
        return null;
    }

    public UserDetails convertToUserDetails(DefaultOidcUser oidcUser) {
        // Converti l'oggetto DefaultOidcUser in un oggetto UserDetails personalizzato
        // Esempio di conversione:

        // Recupera le informazioni necessarie dall'oggetto oidcUser (es. nome utente, ruoli, attributi aggiuntivi)
        String username = oidcUser.getFullName();
        GrantedAuthority authority = oidcUser.getAuthorities().iterator().next();
        // Costruisci un oggetto User (o una tua implementazione personalizzata di UserDetails) utilizzando le informazioni ottenute
         
        // Aggiungi eventuali altri attributi personalizzati

        // Restituisci l'oggetto User convertito
        return User.withUsername(username)
        .password("") // OidcUser doesn't provide password, so setting it as empty string
        .authorities(authority)
        .accountExpired(false)
        .accountLocked(false)
        .credentialsExpired(false)
        .disabled(false)
        .build();
    }
}

