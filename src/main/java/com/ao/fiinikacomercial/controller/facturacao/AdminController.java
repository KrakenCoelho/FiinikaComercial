package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.model.facturacao.PasswordResetToken;
import com.ao.fiinikacomercial.enums.RoleName;
import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.RoleModel;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.PasswordResetService;
import com.ao.fiinikacomercial.service.Repositorio;
import com.resend.core.exception.ResendException;

import okhttp3.Response;

@Controller
public class AdminController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	@Autowired
    private PasswordResetService passwordResetService;
	
	 public BigDecimal addBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.add(b);
	    }

	    public BigDecimal multiplyBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.multiply(b);
	    }
	    
	    public BigDecimal subBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.subtract(b);
	    }
	
	 @RequestMapping(value="/fiinika/dashboard", method=RequestMethod.GET)
	   public String DashboardAdmin(Model model, HttpSession session,HttpServletRequest request,HttpServletResponse response) throws Exception{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			    
			    model.addAttribute("titulo", "Dashboard");
				model.addAttribute("demo",session.getAttribute("demo"));	
			    model.addAttribute("servidor",request.getServerName());	
				session.setAttribute("demo", null); 
				
				//System.out.println(objFunc.geraUUID());	
				
				 String  total_factura = "0", total_despesa= "0",total_recibo="0",total_ft_recibo="0",total_reembolso="0",total_reembolsoFR="0";
			     String total_factura_ant = "0", total_despesa_ant= "0",total_recibo_ant="0",total_ft_recibo_ant="0",total_reembolso_ant="0",total_reembolso_antFR="0";;
			     Double total_reembolso_aux = (double) 0,total_reembolso_aux_ant = (double) 0,  v_facturado = (double) 0, v_despesa = (double) 0,v_recibo = (double) 0,v_reembolso = (double) 0;
			    
			  
			    switch (user.getTipo()) {
			    
					case "Admin":
						model.addAttribute("anos",R.FiltraAno(user.getIdEmpresaUser()));	
						model.addAttribute("meses",R.FiltraMes(objFunc.AnoActual(),user.getIdEmpresaUser()));	
						
						model.addAttribute("facturas", R.facturaRepository.FacturasLastsDocumentos(user.getIdEmpresaUser()));	 
					    total_factura =  R.facturaRepository.TotalFactura(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual());
					    total_despesa =  R.despesaRepository.TotalDespesas(user.getIdEmpresaUser(), objFunc.AnoActual(), objFunc.MesActual());
					    total_recibo =  R.reciboRepository.TotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual());
					    total_ft_recibo =  R.facturaRepository.TotalFacturaRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual());
					    total_reembolso = R.facturaRepository.TotalRembolsoFtAnuladas(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual()); // anuladas pagas
					    total_reembolsoFR = R.facturaRepository.TotalRembolsoFrAnuladas(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual()); // anuladas pagas
					   
					    // Para o mes anterior calculo da varicao MoM
					    total_factura_ant =  R.facturaRepository.TotalFactura(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior());
					    total_despesa_ant =  R.despesaRepository.TotalDespesas(user.getIdEmpresaUser(), objFunc.AnoActual(), objFunc.MesAnterior());
					    total_recibo_ant =  R.reciboRepository.TotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior());
					    total_ft_recibo_ant =  R.facturaRepository.TotalFacturaRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior());
					    total_reembolso_ant = R.facturaRepository.TotalRembolsoFtAnuladas(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior());
					    total_reembolso_antFR = R.facturaRepository.TotalRembolsoFrAnuladas(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior());
					     
					    for(List f: R.facturaRepository.findNotasReembolso(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual())) {
							 
					    	if(!f.get(1).toString().equals(R.facturaRepository.findFacturaByOrigemUniq(user.getIdEmpresaUser(),f.get(2).toString()))) {
					    		total_reembolso_aux = total_reembolso_aux + Double.parseDouble(f.get(1).toString());
					    	}
									
						}
					  
					  for(List f: R.facturaRepository.findNotasReembolso(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior())) {
						  
					    	if(!f.get(1).toString().equals(R.facturaRepository.findFacturaByOrigemUniq(user.getIdEmpresaUser(),f.get(2).toString()))) {
					    		total_reembolso_aux_ant = total_reembolso_aux_ant + Double.parseDouble(f.get(1).toString());
					    	}
									
						}   
					  
					break;
						
		            case "Vendedor":
		        	 
		        	    model.addAttribute("anos",R.FiltraAno(user.getIdEmpresaUser()));	
						model.addAttribute("meses",R.FiltraMes(objFunc.AnoActual(),user.getIdEmpresaUser()));	
		        	    model.addAttribute("facturas", R.facturaRepository.FacturasLastsDocumentosV(user.getId_usuario()));	 	        	 
		        	    
		        	    total_factura =  R.facturaRepository.TotalFacturaV(objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario());
		    		    total_despesa =  R.despesaRepository.TotalDespesasV(user.getIdEmpresaUser(), objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario());
		    		    total_recibo =  R.reciboRepository.TotalReciboV(objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario());
		    		    total_ft_recibo =  R.facturaRepository.TotalFacturaReciboV(objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario());
		    		    total_reembolso = R.facturaRepository.TotalRembolsoFtAnuladasV(objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario()); // anuladas pagas
		    		    total_reembolsoFR = R.facturaRepository.TotalRembolsoFrAnuladasV(objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario()); // anuladas pagas
		    		    
		    		    // Para o mes anterior calculo da varicao MoM
		    		    total_factura_ant =  R.facturaRepository.TotalFacturaV(objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario());
		    		    total_despesa_ant =  R.despesaRepository.TotalDespesasV(user.getIdEmpresaUser(), objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario());
		    		    total_recibo_ant =  R.reciboRepository.TotalReciboV(objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario());
		    		    total_ft_recibo_ant =  R.facturaRepository.TotalFacturaReciboV(objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario());
		    		    total_reembolso_ant = R.facturaRepository.TotalRembolsoFtAnuladasV(objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario());
		    		    total_reembolso_antFR = R.facturaRepository.TotalRembolsoFrAnuladasV(objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario());
		    		    
		    		    for(List f: R.facturaRepository.findNotasReembolsoV(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),user.getId_usuario())) {
		    				  
		    		    	if(!f.get(1).toString().equals(R.facturaRepository.findFacturaByOrigemUniq(user.getIdEmpresaUser(),f.get(2).toString()))) {
		    		    		total_reembolso_aux = total_reembolso_aux + Double.parseDouble(f.get(1).toString());
		    		    	}	    						
		    			}
		    		  
		    		  for(List f: R.facturaRepository.findNotasReembolsoV(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),user.getId_usuario())) {
		    			  
		    		    	if(!f.get(1).toString().equals(R.facturaRepository.findFacturaByOrigemUniq(user.getIdEmpresaUser(),f.get(2).toString()))) {
		    		    		total_reembolso_aux_ant = total_reembolso_aux_ant + Double.parseDouble(f.get(1).toString());
		    		    	}
		    						
		    			}
		    		  
						break;
		
					default:
						break;
				}
			     
			   
			    
			    
			      
			      if(total_factura == null) {
			    	  total_factura = "0";
				  }
			      if(total_despesa == null) {
			    	total_despesa = "0";
				  }
			      if(total_recibo == null) {
					  total_recibo = "0";
				  }
				  if(total_ft_recibo == null) {
					  total_ft_recibo = "0";
				  }
				  if(total_reembolso == null) {
					  total_reembolso = "0";
				  }
				  if(total_reembolsoFR == null) {
					  total_reembolsoFR = "0";
				  }
				  
				  if(total_factura_ant == null) {
					  total_factura_ant = "0";
				  }
			      if(total_despesa_ant == null) {
			    	total_despesa_ant = "0";
				  }
			      if(total_recibo_ant == null) {
					  total_recibo_ant = "0";
				  }
				  if(total_ft_recibo_ant == null) {
					  total_ft_recibo_ant = "0";
				  }
				  if(total_reembolso_ant == null) {
					  total_reembolso_ant = "0";
				  }	
				  if(total_reembolso_antFR == null) {
					  total_reembolso_antFR = "0";
				  }	
				  
				  
				  Double totalRecibo = Double.parseDouble(total_recibo)+Double.parseDouble(total_ft_recibo);
				  Double totalRecibo_ant = Double.parseDouble(total_recibo_ant)+Double.parseDouble(total_ft_recibo_ant);
				  Double totalReembolso = Double.parseDouble(total_reembolso)+ Double.parseDouble(total_reembolsoFR)+total_reembolso_aux;
				  Double totalReembolso_ant = Double.parseDouble(total_reembolso_ant)+ Double.parseDouble(total_reembolso_antFR)+total_reembolso_aux_ant;
				  
				//======================= calcular as variacoes =========
				if(!total_factura_ant.equals("0")) {
				  v_facturado = (Double.parseDouble(total_factura)-Double.parseDouble(total_factura_ant))/Double.parseDouble(total_factura_ant)*100;  
				}
				if(!total_despesa_ant.equals("0")) {
					  v_despesa = (Double.parseDouble(total_despesa)-Double.parseDouble(total_despesa_ant))/Double.parseDouble(total_despesa_ant)*100;  
				}
				
				if(totalRecibo_ant != 0) {
					  v_recibo = (totalRecibo-totalRecibo_ant)/totalRecibo_ant*100;  
				}
				if(totalReembolso_ant!=0) {
					v_reembolso = (totalReembolso-totalReembolso_ant)/totalReembolso_ant*100;
				}
			   
			   
				
				model.addAttribute("tipo_doc",new String[] {"PP","FT","FR","NC","RC"});
				model.addAttribute("link",new String[] {"/fiinika/documentos/proforma/visualizar-proforma/",
	                  "/fiinika/documentos/factura/visualizar-factura/",
	                  "/fiinika/documentos/factura/visualizar-factura/",
	                  "/fiinika/documentos/nota-de-credito/visualizar-nota-de-credito/",
	                  "--"});
				model.addAttribute("titulo","Dashboard | Admin");				
				
				
				model.addAttribute("total_factura", total_factura);
				model.addAttribute("total_despesa", total_despesa);	  
				model.addAttribute("total_recibo", totalRecibo);
				model.addAttribute("total_reembolso",totalReembolso);
				
				model.addAttribute("v_facturado", v_facturado);
				model.addAttribute("v_despesa", v_despesa);	  
				model.addAttribute("v_recibo", v_recibo);
				model.addAttribute("v_reembolso",v_reembolso);
				
			
				BigDecimal v1 = new BigDecimal(totalRecibo);
				BigDecimal v2 = new BigDecimal(total_despesa);
				
				model.addAttribute("result",subBigDecimals(v1,v2).toString());
			
				
				 List <List> documentos = null;	
				 Map<Long,String> doc_map = new HashMap<>();
				 
				for(List l: R.facturaRepository.FacturasLastsDocumentos(user.getIdEmpresaUser())) {
					doc_map.put(Long.parseLong(l.get(0).toString()),l.get(6).toString());					
				}
				
				for(List l: R.reciboRepository.findLastsRecibo()) {
					doc_map.put(Long.parseLong(l.get(0).toString()),l.get(1).toString());
					
				}
		 
			return "fiinika/dashboard";
		   }else {
			   System.out.println(objFunc.geraUUID());	
			   return "redirect:/login";
		   }
	 }
	 
	 
	 //----- View todos usuarios
	 @RequestMapping("/fiinika/utilizadores/todos-utilizadores")
	   public String UsuariosEmpresa(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
		
				model.addAttribute("titulo","Todos Utilizadores");
				model.addAttribute("usuarios",R.userRepository.findIdUserEmpresa(user.getIdEmpresaUser()));
				model.addAttribute("id_logado",id_user);
				model.addAttribute("superU",R.userRepository.findIdSuper(user.getIdEmpresaUser(),"Super"));
				
				//R.Role(model,session,response);		
				
				return "/fiinika/utilizadores/todos-utilizadores";
			}else {
				return "redirect:/index";
			}
	    }
	 
	 

	 
	 // View Editar Usuario
	  @RequestMapping(value="/fiinika/utilizadores/perfil-do-utilizador/{id}", method=RequestMethod.GET)
	   public String PerfilUtilizador(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
	  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);
			  
			 long id_decod = objFunc.Decodifica(id);
			
			model.addAttribute("titulo","Perfil do utilizador");			
			model.addAttribute("logado",session.getAttribute("userLogado"));
			model.addAttribute("usuario", R.userRepository.findById(id_decod).orElse(null));
			//model.addAttribute("logs", R._logRepository.findLogUsuario(id_decod));
			//model.addAttribute("lastlogin", R._logRepository.findLogUsuarioLastLogin(id_decod));
			model.addAttribute("aux", id);
			
			/*
			 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
		 	 List<Object> Listmes_aux= new ArrayList<>(); 
		 	 String controle="0";
				 
				 for(Log log:  R._logRepository.findLogUsuario(id_decod)) {	
					 String d = ""+log.getData();
					 Listmeses.add(d.substring(8));
					} 
				 Object[] meses = Listmeses.toArray();
					 for(int i=0; i<meses.length;i++) {	
						 if(!controle.equals((String)meses[i])) {
							 Listmes_aux.add((String) meses[i]);
					          controle= (String) meses[i];
					          R.ver((String) meses[i]);
						 }
					          
				} 
				//Object[] mesFinal = Listmes_aux.toArray();
					// return mesFinal;
			*/
	
			//R.Role(model,session,response);	
			return "fiinika/utilizadores/perfil-do-utilizador";
			}else {
				return "redirect:/index";
			}
	    }
	  
		 
		 //----- View dados empresa para edidtar
		 @RequestMapping("/fiinika/utilizadores/criar-utilizador")
		   public String CriarUsuario(Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
			  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);			   
				model.addAttribute("titulo","Criar Usuario");				
			
				//R.Role(model,session,response);	
				return "fiinika/utilizadores/criar-utilizador";
				}else {
					return "redirect:/index";
				}
		    }
		 
		  
		 // Salva dados do Usuario
		 @RequestMapping(value="/usuario/criar", method=RequestMethod.POST)
		 public ResponseEntity<String> SalvaUtilizador(HttpServletRequest request,@Valid Usuario user,HttpSession session,HttpServletResponse response,Model model,@RequestParam(name="foto_perfil_",required=false) MultipartFile file_foto,RedirectAttributes attributes) throws IOException{
			
			  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				// System.out.print(objSessao);
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user2 = R.userRepository.findById(id_user).orElse(null);
					    authenticatedUser.Dados(model, session, response);
			 
				try {			
					
					 if(R.userRepository.VerifyTelemovel(user.getTelemovel()+"",user2.getIdEmpresaUser()) != null ){
						 if(Integer.parseInt(R.userRepository.VerifyTelemovel(user.getTelemovel()+"",user2.getIdEmpresaUser())) == Integer.parseInt(user.getTelemovel())) {						
							 return ResponseEntity.ok().body("phone_existis");
						 }					 
					 }	
					 if(R.userRepository.VerifyEmail(user.getEmail(),user2.getIdEmpresaUser()) != null && !user.getEmail().equals("")){
						if(R.userRepository.VerifyEmail(user.getEmail(),user2.getIdEmpresaUser()).equals(user.getEmail())) {
							return ResponseEntity.ok().body("email_existis");	
							
						}
					 }
					 
					 
				
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e);
				}
			 
			  if (file_foto != null) {
			    user.setFoto_perfil(objFunc.singleFileUpload(request,file_foto,"users"));
			    }else {
			    	  user.setFoto_perfil("placeholder-face-big.png");
			    }
			  RoleModel role = new RoleModel();
			  if(user.getTipo().equals("Admin")) {
				    Object [] roleN = RoleName.getRoleByName("ROLE_ADMIN");						
					role.setRoleName((RoleName)roleN[0]);
					role.setRoleId(Long.parseLong(roleN[1].toString()));
			  }else {
				    Object [] roleN = RoleName.getRoleByName("ROLE_USER");						
					role.setRoleName((RoleName)roleN[0]);
					role.setRoleId(Long.parseLong(roleN[1].toString()));
			  }
			   
				
			  
				user.getRoles().add(role);	
				
			    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));			  
			    user.setDataCriacao(objFunc.DataActual(1));
			    user.setIdEmpresaUser(user2.getIdEmpresaUser());
			    user.setRole("Normal");
				R.userRepository.save(user);
				attributes.addFlashAttribute("mensagem", "Usuario adicionado com sucesso!");
				
				 return ResponseEntity.ok().body("Sucesso");
				 
		  }else {
				//response.sendRedirect("/index");
				return ResponseEntity.ok().body("logout");
			}
			
		 }
		 
		 
		 
  //===================================== Salva dados do User
		 @RequestMapping(value="/usuario/editar-salvar", method=RequestMethod.POST)
		 public ResponseEntity<String> UpdateSalvaUtilizador(HttpServletRequest request,@Valid Usuario newUser,@RequestParam String aux,
				 HttpSession session,HttpServletResponse response,Model model,@RequestParam(name="foto_perfil_",required=false) MultipartFile file_foto) throws IOException{
	    
		  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user2 = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);
			 
			 long id = objFunc.Decodifica(aux);
			 
			  Optional<Usuario> oldUser = R.userRepository.findById(id);// busca dados antigos
		        if(oldUser.isPresent()){
		            Usuario usuario = oldUser.get();
		            
		            try {			
		 			   if(!usuario.getTelemovel().toString().equals(newUser.getTelemovel()+"")){
		 					 if(R.userRepository.VerifyTelemovel(newUser.getTelemovel()+"",user2.getIdEmpresaUser()).equals(newUser.getTelemovel()+"") ) {	 
		 						if(R.userRepository.VerifyTelemovel(newUser.getTelemovel()+"",user2.getIdEmpresaUser()) != null  ){
		 							
		 						}
		 						    return ResponseEntity.ok().body("phone_existis");
		 					 		}
		 			   
		 			  
		 			 if(!usuario.getEmail().equals(newUser.getEmail())){
	 					 if(R.userRepository.VerifyEmail(newUser.getEmail(),user2.getIdEmpresaUser()).equals(newUser.getEmail()) ) {	 
	 						if(R.userRepository.VerifyEmail(newUser.getEmail(),user2.getIdEmpresaUser()) != null && !newUser.getEmail().equals("") ){
	 							
	 						}
	 						    return ResponseEntity.ok().body("email_existis");
	 					 		}
	 					  }

		 			     }
			 			
		               } catch (Exception e) {
			 				// TODO: handle exception
			 				System.out.println(e);
			 			}
		         
		            
	              if (file_foto != null) {
	            	  newUser.setFoto_perfil(objFunc.singleFileUpload(request,file_foto,"users"));					   
					    }else {
					    	newUser.setFoto_perfil(usuario.getFoto_perfil());
					    }
	              
		           
		          String t = "";
		          if(newUser.getTipo().equals("Admin")) {
		        	  t = "ROLE_ADMIN";
		          }else {
		        	  t = "ROLE_USER";
		          }
	     	      RoleModel role = new RoleModel();
				  Object [] roleN = RoleName.getRoleByName(t);						
				  role.setRoleName((RoleName)roleN[0]);
				  role.setRoleId(Long.parseLong(roleN[1].toString()));	
					 
		           newUser.setId_usuario(usuario.getId_usuario());
		           newUser.setDataCriacao(usuario.getDataCriacao());
		           newUser.setIdEmpresaUser(usuario.getIdEmpresaUser());
		           newUser.setRole(usuario.getRole());
		           
		           newUser.getRoles().add(role);
		          	
		        	   if(newUser.getPassword().equals("")){
					    	newUser.setPassword(usuario.getPassword());
					    }else {
					    	newUser.setPassword(new BCryptPasswordEncoder().encode(newUser.getPassword()));
					    }
		           
		           R.userRepository.save(newUser);
		           if(id_user == id) {
		        	    newUser.setUsername(newUser.getId_usuario()+"_"+newUser.getUsername());		 			  
				    	UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(newUser, null, newUser.getAuthorities());
				        SecurityContextHolder.getContext().setAuthentication(auth);		
		           }
		       	   //----LOg
				  // R.SaveLog("Editou perfil do user "+newUser.getNome(),3);
		            
		            return ResponseEntity.ok().body("Sucesso");
		        }
		        else {
		        	return ResponseEntity.ok().body("falha");
		        }
		 }else {
				//response.sendRedirect("/index");
				return ResponseEntity.ok().body("logout");
			}
			
			
		 }
// =================== Editar utilizador ========================0 
		  @RequestMapping(value="/fiinika/utilizadores/editar-utilizador/{id}", method=RequestMethod.GET)
		   public String EditarUtilizador(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
			  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				// System.out.print(objSessao);
		    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
				  
				long id_decod = objFunc.Decodifica(id);
				
				model.addAttribute("titulo","Perfil do utilizador");			
				model.addAttribute("logado",session.getAttribute("userLogado"));
				model.addAttribute("usuario", R.userRepository.findById(id_decod).orElse(null));
				//model.addAttribute("logs", R._logRepository.findLogUsuario(id_decod));
				//model.addAttribute("lastlogin", R._logRepository.findLogUsuarioLastLogin(id_decod));
				model.addAttribute("aux", id);
				model.addAttribute("id_logado",id_user);
				model.addAttribute("superU",R.userRepository.findIdSuper(user.getIdEmpresaUser(),"Super"));
	
				//R.Role(model,session,response);	
				return "fiinika/utilizadores/editar-utilizador";
				}else {
					return "redirect:/index";
				}
		    }
		  
		  
		  @RequestMapping(value = "/ajax/docs_user_pesquisa", method = RequestMethod.GET)
			 public @ResponseBody List<List> findAllPesquisaUtilizador(@RequestParam(value = "nome", required = false) String nome_produto,
					 								 @RequestParam(value = "nome", required = false) String mes, HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
			
				 List<List> documentos = new ArrayList<>();
				 List Ids_codificado = new ArrayList<> ();
				  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					// System.out.print(objSessao);
			    if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
				    authenticatedUser.Dados(model, session, response);
					
					 //Pesquisa os tres
					if(!nome_produto.equals("")) {
					//R.facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
						return Documemtos(R.userRepository.findPesquisaDinamica(user.getIdEmpresaUser(),nome_produto),documentos);	 
					 }			 
					     return null;
			    }else {
			    	 return null;
			    }
					 }	
			 
		 //---------------------------------- AJAX FILTRO Despesas --------------------------------		 
			 @RequestMapping(value = "/ajax/pesquisa_tipo", method = RequestMethod.GET)
			 public @ResponseBody List<List> findAll(@RequestParam(value = "tipo", required = false) String tipo,
					 								 @RequestParam(value = "nome", required = false) String mes, HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
			
				 List<List> documentos = new ArrayList<>();
				 List Ids_codificado = new ArrayList<> ();
				  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					// System.out.print(objSessao);
			    if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
				    authenticatedUser.Dados(model, session, response);
					
					 //Pesquisa os tres
					if(!tipo.equals("")) {
						return Documemtos(R.userRepository.findPesquisaTipo(user.getIdEmpresaUser(),tipo),documentos);	 
					 }			
				
						 
					     return null;
			    }else {
			    	 return null;
			    }
					 }	
			 
			 
		//------------------------ Retorna lista_docs e lista de isd codificados
					 public List<List> Documemtos(Iterable <Usuario> lista,List<List> ListMain) {
							
						 boolean b = false;
						 List Ids_codificado = new ArrayList<> ();
						 for(Usuario c: lista) {
								try {						
									Ids_codificado.add(objFunc.Codifica(c.getId_usuario()));
									b = true;							
								} catch (Exception e) {
									
							  }							
							}
						 if(b) {
							 ListMain.add((List) lista);	
						     ListMain.add(Ids_codificado);					
							
							}
						
						 return ListMain;
				}
					 
	 /*	    
	    
		
		$$$$$$$\  $$$$$$$$\  $$$$$$\  $$\   $$\ $$$$$$$\  $$$$$$$$\ $$$$$$$\   $$$$$$\  $$$$$$$\         $$$$$$\  $$$$$$$$\ $$\   $$\ $$\   $$\  $$$$$$\  
		$$  __$$\ $$  _____|$$  __$$\ $$ |  $$ |$$  __$$\ $$  _____|$$  __$$\ $$  __$$\ $$  __$$\       $$  __$$\ $$  _____|$$$\  $$ |$$ |  $$ |$$  __$$\ 
		$$ |  $$ |$$ |      $$ /  \__|$$ |  $$ |$$ |  $$ |$$ |      $$ |  $$ |$$ /  $$ |$$ |  $$ |      $$ /  \__|$$ |      $$$$\ $$ |$$ |  $$ |$$ /  $$ |
		$$$$$$$  |$$$$$\    $$ |      $$ |  $$ |$$$$$$$  |$$$$$\    $$$$$$$  |$$$$$$$$ |$$$$$$$  |      \$$$$$$\  $$$$$\    $$ $$\$$ |$$$$$$$$ |$$$$$$$$ |
		$$  __$$< $$  __|   $$ |      $$ |  $$ |$$  ____/ $$  __|   $$  __$$< $$  __$$ |$$  __$$<        \____$$\ $$  __|   $$ \$$$$ |$$  __$$ |$$  __$$ |
		$$ |  $$ |$$ |      $$ |  $$\ $$ |  $$ |$$ |      $$ |      $$ |  $$ |$$ |  $$ |$$ |  $$ |      $$\   $$ |$$ |      $$ |\$$$ |$$ |  $$ |$$ |  $$ |
		$$ |  $$ |$$$$$$$$\ \$$$$$$  |\$$$$$$  |$$ |      $$$$$$$$\ $$ |  $$ |$$ |  $$ |$$ |  $$ |      \$$$$$$  |$$$$$$$$\ $$ | \$$ |$$ |  $$ |$$ |  $$ |
		\__|  \__|\________| \______/  \______/ \__|      \________|\__|  \__|\__|  \__|\__|  \__|       \______/ \________|\__|  \__|\__|  \__|\__|  \__|
		                                                                                                                                                 
		     
		    * 
		    */
		   
		 
		   @RequestMapping(value="/fiinika/seguranca/esqueceu-a-senha", method=RequestMethod.GET)
			 public String RecuperarSenha(Model model, HttpServletResponse httpServletResponse) throws ParseException, IOException{
			   
			        
					model.addAttribute("titulo","Esqueceu a Senha");					
								
				 return "fiinika/seguranca/esqueceu-a-senha";		    
		   }
		   
		   @RequestMapping(value="/fiinika/seguranca/esqueceu-a-senha-tel", method=RequestMethod.GET)
			 public String RecuperarSenha2(Model model, HttpServletResponse httpServletResponse) throws ParseException, IOException{
			   
			        
					model.addAttribute("titulo","Esqueceu a Senha");					
								
				 return "fiinika/seguranca/esqueceu-a-senha-tel";		    
		   }
		   
		 
		   @RequestMapping(value="/verifica-email", method=RequestMethod.POST)
		    public ResponseEntity<Object> EnviaLinkRecuoperacao(HttpServletRequest request,HttpSession session, 		
		    		@RequestParam(name="email",required=false) String email) throws ParseException, ResendException{	     
			 /*
			   if(!R.userRepository.existsByEmail(email)) {
				   return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found: Email mot exists!");
			   }*/
			   
			   passwordResetService.requestPasswordReset(email,request);
			    
		        return ResponseEntity.status(HttpStatus.CREATED).body("");
		    }
		   
		   @RequestMapping(value="/verifica-telemovel", method=RequestMethod.POST)
		    public ResponseEntity<Object> EnviaCodigoRecuoperacao(HttpServletRequest request,HttpSession session, 		
		    		@RequestParam(name="telemovel",required=true) String telemovel) throws ParseException, ResendException, IOException{	     
			 		 
			   if(!objFunc.isDigit(telemovel)) {				
					   return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
			       }
				 
			   if(telemovel.length()>9 || telemovel.length()<9) {
				   return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
		       }
			   Usuario user = R.userRepository.findByTelemovel(telemovel).orElse(null);
			   if(user!=null) {  
				   String codigo = objFunc.GeraCodigo(4);
					R.userRepository.updateCodigoVerificacao(codigo,telemovel);			
				    String msg = "Codigo de recuperacao de senha: "+codigo;					
				    	
				    	Response r = objFunc.enviarSmsSingle(telemovel, msg);
						r.body().close();
						String token = UUID.randomUUID().toString();
			            PasswordResetToken passwordResetToken = new PasswordResetToken();
			            passwordResetToken.setToken(token);
			            passwordResetToken.setUsuario(user);
			            R.passwordRepository.save(passwordResetToken);
			            session.setAttribute("telefone_ss",telemovel);
						 return ResponseEntity.status(HttpStatus.CREATED).body("");
						 
				  }else {				 
					  return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
				}		    
		    
		    }
		   
		   
		   @RequestMapping(value="/fiinika/seguranca/codigo-de-seguranca", method=RequestMethod.GET)
			 public String CodigoRecuperacao(Model model, HttpServletResponse httpServletResponse,HttpSession session) throws ParseException, IOException{
			        
					model.addAttribute("titulo","Código de Segurança");
					model.addAttribute("telemovel",session.getAttribute("telefone_ss"));
								
				 return "fiinika/seguranca/codigo-de-seguranca";		    
		   }
		   
			 @RequestMapping(value="/verifica-codigo", method=RequestMethod.POST)
			 ResponseEntity<String> VerificaCodigoVerificacao(HttpServletRequest request,HttpSession session,Model model,@RequestParam("codigo") String codigo,
					 @RequestParam("telemovel") String telemovel ,RedirectAttributes attributes){
				
					System.out.println(telemovel+" "+codigo);
					    Usuario user = R.userRepository.findByTelemovelAndCodigo(telemovel,codigo).orElse(null);				    
					     if(user!=null){
					    	 PasswordResetToken passResetTk =  R.passwordRepository.findFirstByUsuarioOrderByIdDesc(user).orElse(null);;						
							 return ResponseEntity.status(HttpStatus.CREATED).body(passResetTk.getToken());
					     }else {
					    	 return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
					     }
					     
			 }
		   
		   @RequestMapping(value="/fiinika/seguranca/criar-nova-senha/{token}", method=RequestMethod.GET)
			 public String NovoPin(@PathVariable String token,Model model, HttpServletResponse httpServletResponse) throws ParseException, IOException{
			   
			        
					model.addAttribute("titulo","Criar Nova Senha");
					model.addAttribute("token",token);
								
				 return "fiinika/seguranca/criar-nova-senha";		    
		   }
		   
		    @RequestMapping(value="/reset-password", method=RequestMethod.POST)
		    public ResponseEntity<Object> resetPassword(@RequestParam("token") String token, @RequestParam("password") String newPassword) {
		        if (passwordResetService.resetPassword(token, newPassword)) {
		        	 return ResponseEntity.status(HttpStatus.CREATED).body("");
		        } else {
		            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Falha");
		        }
		    }
		 
		
		

}
