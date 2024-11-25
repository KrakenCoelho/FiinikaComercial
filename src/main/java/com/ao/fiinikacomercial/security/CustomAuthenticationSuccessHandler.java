package com.ao.fiinikacomercial.security;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.facturacao.userRepository;

public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Autowired
    public userRepository usuarioRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
    	
    	for(GrantedAuthority auth: authentication.getAuthorities()) {    	
        // Verifique o tipo de usuário (user ou admin)
        if (auth.getAuthority().equals("ROLE_SUPER")) {
            // Se for um administrador, redirecione para a página de admin        	
            //setDefaultTargetUrl("/admin/dashboard-do-admin");   // 
            response.getWriter().write("1");
        }  
        if (auth.getAuthority().equals("ROLE_ADMIN")) {
            // Caso contrário, redirecione para a página do usuário comum       
             //setDefaultTargetUrl("/cliente/dashboard");  //
             response.getWriter().write("2");
        }
        if (auth.getAuthority().equals("ROLE_USER")) {
        
             	response.getWriter().write("3");
        }
    
    	}
      //  super.onAuthenticationSuccess(request, response, authentication);
    }
    /*
    public int usuario(Authentication auth2) {
    	auth2 = SecurityContextHolder.getContext().getAuthentication();	   
    	int r = 0;
		 Usuario user = null;
		 if(auth2 != null && auth2.isAuthenticated()) { 
			 Object principal = auth2.getPrincipal();
			 if(principal instanceof UserDetails) {
				 UserDetails userDetails =  (UserDetails) principal;
				 String []  dadosUser= userDetails.getUsername().split("_");				
				 r = Integer.parseInt(dadosUser[2].toString());
			 }
			 return r;
		 }else {
		
		 return r;
		 }
    }*/
}

