package com.ao.fiinikacomercial.security;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Conta;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.facturacao.SubscricaoRepository;
import com.ao.fiinikacomercial.repository.facturacao.userRepository;
import com.ao.fiinikacomercial.security.AuthenticationNotFoundException;
import com.ao.fiinikacomercial.service.Repositorio;

@Component
public class AuthenticatedUser implements AuthenticationFailureHandler {
	
	@Autowired
    public Repositorio R;
	private  Funcoes objFunc = new Funcoes();
	
	public UserDetails getAuthenticatedUserDetails() {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		 if(auth != null && auth.isAuthenticated()) { 
			 Object principal = auth.getPrincipal();
			 if(principal instanceof UserDetails) {
				 return (UserDetails) principal;
			 }
		 }
		 throw new AuthenticationNotFoundException("Usuário não autenticado");
	}
	
	public Object getAuthenticatedUserDetails2() {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		 if(auth != null && auth.isAuthenticated()) { 
			 Object principal = auth.getPrincipal();
			 if(principal instanceof UserDetails) {
				 return (UserDetails) principal;
			 }
		 }else {
			 return null;
		 }
		// throw new AuthenticationNotFoundException("Usuário não autenticado");
		return null;
	}
	
	 public void Dados(Model model,HttpSession session,HttpServletResponse response) throws IOException {
		 Authentication auth2 = SecurityContextHolder.getContext().getAuthentication();	   
	    	
			 Usuario user = null;
			 if(auth2 != null && auth2.isAuthenticated()) { 
				 Object principal = auth2.getPrincipal();
				 if(principal instanceof UserDetails) {
					 UserDetails userDetails =  (UserDetails) principal;
					 String []  dadosUser= userDetails.getUsername().split("_");				
					 long id_user = Long.parseLong(dadosUser[0]);
					 user = R.userRepository.findById(id_user).orElse(null);
					 Empresa empresa = R.empresaRepository.findEmpresa(user.getIdEmpresaUser()).iterator().next();					   
				      int dias_sub = -1;
				     Iterable  <Conta> contaP = R.contaRepository.findContaPadrao(user.getIdEmpresaUser());	
				     List <List> dadosSub = R.subscricaoRepository.findSubscricaDexa("Paga",user.getIdEmpresaUser());
						 for(List sub : dadosSub) {	
							 try {
								 dias_sub = (int) objFunc.DiferencaDatas(objFunc.DataActual(), sub.get(1).toString());
								 if(dias_sub < 0) {				
										// response.sendRedirect("http://" + request.getServerName() + request.getRequestURI() + (request.getQueryString() != null ? "?" + request.getQueryString() : ""));
										response.sendRedirect("/fiinika/configuracoes/escolher-pacote");
									}
								 session.setAttribute("dias_subscricao",dias_sub);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							 model.addAttribute("tipo_sub",Integer.parseInt(sub.get(3).toString()));
								 
						}
						 if(dadosSub.toString().equals("[]")) {						
							 session.setAttribute("dias_subscricao",-1);
							}
						
						if(user.getTipo().equals("Vendedor")) {				
							model.addAttribute("display_none","display:none");
						}	
						model.addAttribute("logado",dadosUser[1]);
						model.addAttribute("foto_perfil",user.getFoto_perfil());
						model.addAttribute("regime",empresa.getRegime());
				 }
				 
			 }
	 }

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		 response.setStatus(HttpStatus.UNAUTHORIZED.value());
   	     response.getWriter().write("Credenciais incorretas");
		// TODO Auto-generated method stub
		
	}
	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();	
		
		 return auth.getAuthorities();
	}
	
	public boolean hasAuthority(UserDetails userDetails,String authorityToCheck) {
		
		// boolean hasAuthority = userDetails.getAuthorities().stream()
	    boolean hasAuthority = getAuthorities().stream()
	            .map(GrantedAuthority::getAuthority)
	            .anyMatch(authorityToCheck::equals);
	    
		return hasAuthority;
	}
	
	
	public void setNewAuthorities(String role) {
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		 if(auth != null && auth.isAuthenticated()) { 
			 Object principal = auth.getPrincipal();
			 
			 if(principal instanceof UserDetails) {
				 
				 UserDetails userDetails =  (UserDetails) principal;
				// Crie uma nova lista de autoridades com a nova autoridade adicionada
				 List<GrantedAuthority> updatedAuthorities = new ArrayList<>(userDetails.getAuthorities());
				 updatedAuthorities.add(new SimpleGrantedAuthority(role));
				 // Crie um novo UserDetails com as autoridades atualizadas
				 UserDetails updatedUserDetails = new User(userDetails.getUsername(),"[PROTECTED]",true, true, true,true, updatedAuthorities);				
				 // Crie um novo objeto Authentication com o UserDetails atualizado
				 Authentication newAuthentication = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedUserDetails.getAuthorities());
				 // Defina o novo objeto Authentication no contexto de segurança
				 SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			 }
		 }
		
	}
	  /*
	   for (GrantedAuthority authority : userDetails.getAuthorities()) {
	        System.out.println("Authority: " + authority.getAuthority());
	    }*/

}
