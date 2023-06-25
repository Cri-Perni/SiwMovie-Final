package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.service.OidcUserDetailsServiceImpl;

@ControllerAdvice
public class GlobalController{

    @Autowired
    private OidcUserDetailsServiceImpl oidcUserDetailsService;
   
   
    @ModelAttribute("userDetails") 

    public UserDetails getUser() {
        UserDetails user = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof UsernamePasswordAuthenticationToken)) {
            user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            DefaultOidcUser oidcUser = (DefaultOidcUser) oauthToken.getPrincipal();
            user = (UserDetails) this.oidcUserDetailsService.convertToUserDetails(oidcUser);
        }

        return user;
    }
}