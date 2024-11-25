package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ao.fiinikacomercial.enums.RoleName;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Conta;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.RoleModel;
import com.ao.fiinikacomercial.model.facturacao.Subscricao;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;




@Controller
public class InicioController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	
	 @RequestMapping(value={"/login","/","/index"}, method=RequestMethod.GET)
	 public String Login(Model model) throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException{
	
			model.addAttribute("titulo","Login");			
						
		 return "login";
	    }
	 

	 
	 @RequestMapping(value="/inscricao", method=RequestMethod.GET)
	 public String CriarConta(Model model) throws ParseException{
		 
			model.addAttribute("titulo","Inscricao");	
						
		 return "inscricao";
	    }
	 

	   @RequestMapping(value="/salva-conta-empresa", method=RequestMethod.POST)
	    public ResponseEntity<Object> saveEncarregado(HttpServletRequest request,HttpSession session,
	    		Model model,@Valid Usuario usuario,	    		
	    		@RequestParam(name="password_aux",required=false) String password_aux) throws ParseException{	     
		 
		   
		    Usuario userOld = null;
		    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		    if(objSessao!=null) {
		    	 userDetails = (UserDetails) objSessao;
		    	 String []  dadosUser= userDetails.getUsername().split("_");
				 userOld = R.userRepository.findById(usuario.getId_usuario()).orElse(null);
		    }
		    
		 
		    
		     RoleModel role = new RoleModel();
			 Object [] roleN = RoleName.getRoleByName("ROLE_ADMIN");						
			 role.setRoleName((RoleName)roleN[0]);
			 role.setRoleId(Long.parseLong(roleN[1].toString()));		
			
					if(password_aux==null) {
						if(R.userRepository.existsByEmail(usuario.getEmail())) {		    		
				    	     return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email already registered!");
				    	}
						if(usuario.getTelemovel()!= null) {
					    	if(R.userRepository.existsByTelemovel(usuario.getTelemovel())) {		    		
					    	     return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Telemovel already registered!");
					    	}
						}
						usuario.getRoles().add(role);	
						
					    usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
					    usuario.setDataCriacao(objFunc.DataActual(1));	
					    usuario.setRole("Super");
					    usuario.setTipo("Admin");
					    usuario.setFoto_perfil("placeholder-face-big.png");
					    
				        Empresa empresa = new Empresa();
				        empresa.setNomeEmpresa(usuario.getUsername());
				   		empresa.setFoto_logotipo("Logo_default_Dexa.png");	
				        empresa.setControl_nif(0);
						R.empresaRepository.save(empresa);
						usuario.setIdEmpresaUser(empresa.getIdEmpresa());
						
						    Conta conta = new Conta();
						    conta.setIdEmpresafk(empresa.getIdEmpresa());
						    conta.setFoto_logotipo("caixa.jpg");
						    conta.setSigla("Caixa");
						    conta.setBanco("Caixa");
						    conta.setIban("Caixa");
						    conta.setDependencia("Caixa");
						    conta.setGestor("Caixa");
						    conta.setPadrao(0);
						    conta.setNumero_conta("Caixa");
						    
							R.contaRepository.save(conta);
				         
				         
							 Subscricao compra = new Subscricao();					 
							 compra.setData_hora(objFunc.DataTimeActual());
							 compra.setData_expiracao_pagamento(objFunc.AddDays(objFunc.DataActual(),1));
							 compra.setEstado("Paga");
							 compra.setId_pacotefk(8);
							// compra.setId_produtofk(objFunc.Decodifica(id_produto));
							 compra.setIdEmpresafk(empresa.getIdEmpresa());
							 compra.setPreco("1.0");
							 compra.setReferencia("");
							 compra.setData_exp_referencia("");
							 
							 R.subscricaoRepository.save(compra);
						
					  
					}else {	
						// EDITAR 					
						if(!usuario.getEmail().equals(userOld.getEmail())) {
								if(R.userRepository.existsByEmail(usuario.getEmail())) {						
									return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Email already registered!");}
				    	}
						if(usuario.getTelemovel()!= null) {
							if(!usuario.getTelemovel().equals(userOld.getTelemovel())) {
						    	if(R.userRepository.existsByTelemovel(usuario.getTelemovel())) {	
						    	      return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Telemovel already registered!");}
						    	}
						}
						
						usuario.getRoles().add(role);
						usuario.setDataCriacao(userOld.getDataCriacao());
					
						if(password_aux.equals("")) {
							 usuario.setPassword(userOld.getPassword());
						}else {
							  usuario.setPassword(new BCryptPasswordEncoder().encode(password_aux));
						}
					}
			
					session.setMaxInactiveInterval(60*60);	
					session.setAttribute("demo", 1);
			
		    R.userRepository.save(usuario);
		    
		    if(password_aux == null) {
		    	usuario.setUsername(usuario.getId_usuario()+"_"+usuario.getUsername());
		    	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		        SecurityContextHolder.getContext().setAuthentication(auth);
		    }
		  
	        return ResponseEntity.status(HttpStatus.CREATED).body("");
	    }
	   
		 @RequestMapping(value={"/test"}, method=RequestMethod.GET)
		 public String Test(Model model) throws ParseException, InvalidKeyException, NoSuchAlgorithmException, IOException{
		
				model.addAttribute("titulo","Login");			
							
			 return "test";
		    }

}
