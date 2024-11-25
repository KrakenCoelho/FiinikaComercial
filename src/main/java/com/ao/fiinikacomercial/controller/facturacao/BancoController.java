package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Conta;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.service.Repositorio;
import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.funcoes.*;
import java.util.HashMap;
import java.util.Map;
  
@Controller
public class BancoController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;


	
	 @RequestMapping(value="/fiinika/bancos/todos-bancos", method=RequestMethod.GET)
	   public String TodasContas(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
	   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
	
		
			model.addAttribute("titulo","Todos bancos");	
			
			model.addAttribute("bancos", R.contaRepository.findById_empresa(user.getIdEmpresaUser()));
		  
			//R.Role(model,session,response);	
			return "fiinika/bancos/todos-bancos";
			
			}else {
				return  "redirect:/index";
			}
	    }
	 
	 
	 // View Editar Usuario
	  @RequestMapping(value="/fiinika/bancos/banco-page/{id}", method=RequestMethod.GET)
	   public String ContaIndividual(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			  
			    long id_decod = objFunc.Decodifica(id);
			
			
			model.addAttribute("titulo","Banco Page");	
			model.addAttribute("banco", R.contaRepository.findById(id_decod,user.getIdEmpresaUser()));
			model.addAttribute("aux",id);
			
			model.addAttribute("facturas", R.facturaRepository.findFacturaByIdConta(user.getIdEmpresaUser(),id_decod));
			model.addAttribute("recibos", R.reciboRepository.findReciboIdConta(user.getIdEmpresaUser(),id_decod));
			model.addAttribute("despesas",R.despesaRepository.findByIdConta(user.getIdEmpresaUser(),id_decod));
			model.addAttribute("tipo_doc",new String[] {"PP","FT","FR","NC","RC","DP"});
			model.addAttribute("leng",new Integer[] {1,2,3,4,5,6});
			
			
			
			//========================================= VARIACOES =================================
		    String total_factura = "0",total_recibo="0",total_ft_recibo="0",total_reembolso="0";
		    String total_factura_ant = "0",total_recibo_ant="0",total_ft_recibo_ant="0",total_reembolso_ant="0";
		    Double total_reembolso_aux = (double) 0,total_reembolso_aux_ant = (double) 0,  v_facturado = (double) 0, v_despesa = (double) 0,v_recibo = (double) 0,v_reembolso = (double) 0;
		    
		    total_factura =  R.facturaRepository.TotalFacturaBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod);
		    total_recibo =  R.reciboRepository.TotalReciboBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod);
		    total_ft_recibo =  R.facturaRepository.TotalFacturaReciboBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod);
		    total_reembolso = R.facturaRepository.TotalRembolsoFtAnuladasBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod); // anuladas pagas
		    
		    // Para o mes anterior calculo da varicao MoM
		    total_factura_ant =  R.facturaRepository.TotalFacturaBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
		    total_recibo_ant =  R.reciboRepository.TotalReciboBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
		    total_ft_recibo_ant =  R.facturaRepository.TotalFacturaReciboBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
		    total_reembolso_ant = R.facturaRepository.TotalRembolsoFtAnuladasBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
		    
		    
		    if(total_factura == null) {
		    	  total_factura = "0";
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
			  
			  if(total_factura_ant == null) {
				  total_factura_ant = "0";
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
			  
			  for(List f: R.facturaRepository.findNotasReembolsoBanco(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod)) {
				  
			    	if(!f.get(1).toString().equals(R.facturaRepository.findFacturaByOrigemUniq(user.getIdEmpresaUser(),f.get(2).toString()))) {
			    		total_reembolso_aux = total_reembolso_aux + Double.parseDouble(f.get(1).toString());
			    	}
							
				}
			  
			  for(List f: R.facturaRepository.findNotasReembolso(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),id_decod)) {
				  
			    	if(!f.get(1).toString().equals(R.facturaRepository.findFacturaByOrigemUniq(user.getIdEmpresaUser(),f.get(2).toString()))) {
			    		total_reembolso_aux_ant = total_reembolso_aux_ant + Double.parseDouble(f.get(1).toString());
			    	}
							
				}
			  Double totalRecibo = Double.parseDouble(total_recibo)+Double.parseDouble(total_ft_recibo);
			  Double totalRecibo_ant = Double.parseDouble(total_recibo_ant)+Double.parseDouble(total_ft_recibo_ant);
			  Double totalReembolso = Double.parseDouble(total_reembolso)+total_reembolso_aux;
			  Double totalReembolso_ant = Double.parseDouble(total_reembolso_ant)+total_reembolso_aux_ant;
			  
			//======================= calcular as variacoes =========
			if(!total_factura_ant.equals("0")) {
			  v_facturado = (Double.parseDouble(total_factura)-Double.parseDouble(total_factura_ant))/Double.parseDouble(total_factura_ant)*100;  
			}				
			
			if(totalRecibo_ant != 0) {
				  v_recibo = (totalRecibo-totalRecibo_ant)/totalRecibo_ant*100;  
			}
			if(totalReembolso_ant!=0) {
				v_reembolso = (totalReembolso-totalReembolso_ant)/totalReembolso_ant*100;
			}
			
			model.addAttribute("total_factura", total_factura);  
			model.addAttribute("total_recibo", totalRecibo);
			model.addAttribute("total_reembolso",totalReembolso);
			
			model.addAttribute("v_facturado", v_facturado);				
			model.addAttribute("v_recibo", v_recibo);
			model.addAttribute("v_reembolso",v_reembolso);
	
			
			return "fiinika/bancos/banco-page";
			}else {
				return "redirect:/index";
			}
	    }
	  
		 
		 //----- View dados empresa para edidtar
		 @RequestMapping("/fiinika/bancos/criar-conta")
		   public String CriarUsuario(Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
			   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					
				   model.addAttribute("titulo","Criar conta");	
				   
			//	R.Role(model,session,response);	
		        	return "fiinika/bancos/criar-conta";
				}else {
					return "redirect:/index";
				}
		    }
		 
		  
		 // Salva dados do Cliente
		 @RequestMapping(value="/admin/salva-conta", method=RequestMethod.POST)
		 public ResponseEntity<String> SalvaConta(HttpServletRequest request,@Valid Conta conta,HttpSession session,Model model,RedirectAttributes attributes
				 ,HttpServletResponse response) throws IOException{
			
			   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
		     
			    String [] f_b = conta.getBanco().split("=");
			    
			    if(R.contaRepository.VerifyIban(conta.getIban(),user.getIdEmpresaUser()) != null && !conta.getIban().equals("")){
					if(R.contaRepository.VerifyIban(conta.getIban(),user.getIdEmpresaUser()).equals(conta.getIban())) {
						return ResponseEntity.ok().body("iban_existis");	
						
					}
				 }
			 
			    conta.setIdEmpresafk(user.getIdEmpresaUser());
			    conta.setFoto_logotipo(f_b[0]+".png");
			    conta.setSigla(f_b[0]);
			    conta.setBanco(f_b[1]);
			    
				R.contaRepository.save(conta);
				
				
				 return ResponseEntity.ok().body("Sucesso");
				 
		 }else {
				//response.sendRedirect("/index");
				return ResponseEntity.ok().body("logout");
			}
			
		 }
		 
		 // View Editar Usuario
		  @RequestMapping(value="/fiinika/bancos/editar-banco/{id}", method=RequestMethod.GET)
		   public String EditarBanco(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
			   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
				  
				   long id_decod = objFunc.Decodifica(id);
				
					model.addAttribute("titulo","Editar conta");	
					model.addAttribute("banco", R.contaRepository.findById(id_decod).orElse(null));
					model.addAttribute("aux",id);		
					
				//	session.setAttribute("userLogado", null);
				//	session.setAttribute("userLogadoEmail", null);
				//	session.setAttribute("userLogadoPass",null);
					
			//	R.Role(model,session,response);	
				return "fiinika/bancos/editar-banco";
				}else {
					return "redirect:/index";
				}
		    }
		  
		// Salva dados do Cliente
			 @RequestMapping(value="/admin/salva-conta-editada", method=RequestMethod.POST)
			 public ResponseEntity<String> SalvaContaEditada(HttpServletRequest request,@Valid Conta newConta,@RequestParam(value="aux") String aux,HttpSession session,Model model,RedirectAttributes attributes,HttpServletResponse response) throws IOException{
				
		   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				 long id_conta = objFunc.Decodifica(aux);
				
				 
				 Optional<Conta> oldConta = R.contaRepository.findById(id_conta);// busca dados antigos
			        if(oldConta.isPresent()){
			            Conta conta = oldConta.get();
				 
				    String [] f_b = newConta.getBanco().split("=");
				    
				   
				   
				    if(!conta.getIban().equals(newConta.getIban())){	 					
	 						if(R.contaRepository.VerifyIban(newConta.getIban(),user.getIdEmpresaUser()) != null && !newConta.getIban().equals("") ){
	 							 if(R.contaRepository.VerifyIban(newConta.getIban(),user.getIdEmpresaUser()).equals(newConta.getIban()) ) {	 
	 							 attributes.addFlashAttribute("mensagem", "Não foi possível registar Iban, já existe");
	 						}
	 						return ResponseEntity.ok().body("iban_existis");	
	 					 		}
	 					  }
				    
				    newConta.setIdConta(id_conta);
				    newConta.setIdEmpresafk(user.getIdEmpresaUser());
				    newConta.setFoto_logotipo(f_b[0]+".png");
				    newConta.setSigla(f_b[0]);
				    newConta.setBanco(f_b[1]);
				    
			    	if(newConta.getPadrao() == 1) {
			    		long id_contaP = 0;
			    		Iterable  <Conta> contaP = R.contaRepository.findContaPadrao(user.getIdEmpresaUser());
					       for(Conta c:contaP) {
					    	   id_contaP = contaP.iterator().next().getIdConta();
					       }
						R.contaRepository.updateContaPadrao(0,id_contaP);
						
					}
			    	
			    	R.contaRepository.save(newConta);
					attributes.addFlashAttribute("mensagem", "Conta alterada com sucesso!");
					
					 return ResponseEntity.ok().body("Sucesso");
			        }
			        else {
			        	return ResponseEntity.ok().body("falha");
			    }
			     
			 }else {
				  
				   // response.sendRedirect("/index");
					return ResponseEntity.ok().body("logout");
				}
			 }
				
		  }

		 
	
		

