package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Cliente;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.*;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;
import java.util.Base64;

@Controller
public class ClienteController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	

	
	// View de todos os clientes
	 @RequestMapping("/fiinika/clientes/todos-clientes")
	   public String Clientes(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				
			    model.addAttribute("titulo","Todos clientes");
			    model.addAttribute("clientes", R.clienteRepository.findAllClientes(user.getIdEmpresaUser()));
			
			return "fiinika/clientes/todos-clientes";
			}else {
				return "redirect:/index";
			}
	    }
	
	// View Editar Cliente
	
		  @RequestMapping(value="/fiinika/clientes/perfil-do-cliente/{id}", method=RequestMethod.GET)
		   public String PerfilCliente(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
			  
			  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				// System.out.print(objSessao);
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);
						authenticatedUser.Dados(model, session, response);
				  
			try {
					  
				long id_decod = objFunc.Decodifica(id);
				String original = id_decod+"";
				String encodedString = Base64.getEncoder().encodeToString(original.getBytes());
				
			 
			    
			    Optional <Cliente>  cliente = Optional.of(R.clienteRepository.findById(user.getIdEmpresaUser(),id_decod).orElse(null));
				model.addAttribute("titulo","Perfil cliente");
				model.addAttribute("activo3","activo");
				model.addAttribute("aux",id);
				model.addAttribute("enc",encodedString);
				model.addAttribute("cliente", cliente.get());
				model.addAttribute("recibos", R.reciboRepository.findReciboIByCliente(user.getIdEmpresaUser(),id_decod));
				model.addAttribute("facturas", R.facturaRepository.findFacturaByIdCliente(user.getIdEmpresaUser(),id_decod));
				model.addAttribute("tipo_doc",new String[] {"PP","FT","FR","NC","RC"});
				model.addAttribute("leng",new Integer[] {1,2,3,4,5});
				
			
				
				//========================================= VARIACOES =================================
			    String total_factura = "0",total_recibo="0",total_ft_recibo="0",total_reembolso="0";
			    String total_factura_ant = "0",total_recibo_ant="0",total_ft_recibo_ant="0",total_reembolso_ant="0";
			    Double total_reembolso_aux = (double) 0,total_reembolso_aux_ant = (double) 0,  v_facturado = (double) 0, v_despesa = (double) 0,v_recibo = (double) 0,v_reembolso = (double) 0;
			    
			    total_factura =  R.facturaRepository.TotalFactura(objFunc.AnoActual(), objFunc.MesActual(),id_decod);
			    total_recibo =  R.reciboRepository.TotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod);
			    total_ft_recibo =  R.facturaRepository.TotalFacturaRecibo(objFunc.AnoActual(), objFunc.MesActual(),id_decod);
			    total_reembolso = R.facturaRepository.TotalRembolsoFtAnuladas(objFunc.AnoActual(), objFunc.MesActual(),id_decod); // anuladas pagas
			    
			    // Para o mes anterior calculo da varicao MoM
			    total_factura_ant =  R.facturaRepository.TotalFactura(objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
			    total_recibo_ant =  R.reciboRepository.TotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
			    total_ft_recibo_ant =  R.facturaRepository.TotalFacturaRecibo(objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
			    total_reembolso_ant = R.facturaRepository.TotalRembolsoFtAnuladas(objFunc.AnoActual(), objFunc.MesAnterior(),id_decod);
			    
			    
			      
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
				  
				  for(List f: R.facturaRepository.findNotasReembolso(user.getIdEmpresaUser(),objFunc.AnoActual(), objFunc.MesActual(),id_decod)) {
					  
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
				
				model.addAttribute("link", new String[] { "/fiinika/documentos/proforma/visualizar-proforma/",
						"/fiinika/documentos/factura/visualizar-factura/", "/fiinika/documentos/factura/visualizar-factura/",
						"/fiinika/documentos/nota-de-credito/visualizar-nota-de-credito/", "--" });
		
				//----LOg
			// R.SaveLog("Visualizou perfil de "+cliente.get().getNome_cliente(),4);
			  } catch (Exception e) {
					
				}
				return "fiinika/clientes/perfil-do-cliente";
				}else {
					return "redirect:/index";
				}
		    }
	 
	 // View Criar Cliente
	 @RequestMapping("/fiinika/clientes/criar-novo-cliente")
	   public String CriarCliente(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				
			    model.addAttribute("titulo","Criar cliente");
		
		
			return "fiinika/clientes/criar-novo-cliente";
			}else {
				return "redirect:/login";
			}
	    }
	
	 
	 // Salva dados do Cliente
	 @RequestMapping(value="/cliente/criar", method=RequestMethod.POST)
	 public ResponseEntity<String> SalvaCliente(HttpServletRequest request,@Valid Cliente cliente,HttpSession session,Model model,@RequestParam(value="img_logo",required=false) MultipartFile file_logo,RedirectAttributes attributes,HttpServletResponse response) throws IOException{
	
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
		 		 
			try {			
				
				 if(R.clienteRepository.VerifyTelemovel(cliente.getTelemovel()+"",user.getIdEmpresaUser()) != null ){
					 if(Integer.parseInt(R.clienteRepository.VerifyTelemovel(cliente.getTelemovel()+"",user.getIdEmpresaUser())) == cliente.getTelemovel()) {						
						 return ResponseEntity.ok().body("phone_existis");
					 }					 
				 }	
				 if(R.clienteRepository.VerifyEmail(cliente.getEmail(),user.getIdEmpresaUser()) != null && !cliente.getEmail().equals("")){
					if(R.clienteRepository.VerifyEmail(cliente.getEmail(),user.getIdEmpresaUser()).equals(cliente.getEmail())) {
						return ResponseEntity.ok().body("email_existis");	
						
					}
				 }
				 if(R.clienteRepository.VerifyNif(cliente.getNif(),user.getIdEmpresaUser()) != null ){
						if(R.clienteRepository.VerifyNif(cliente.getNif(),user.getIdEmpresaUser()).equals(cliente.getNif())) {	
							return ResponseEntity.ok().body("nif_existis");				
						}
					 }
			
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		 
		 
			   if (file_logo != null) {
			    cliente.setFoto_logotipo(objFunc.singleFileUpload(request,file_logo,"clientes"));
			    }else {
			    	 cliente.setFoto_logotipo("placeholder-face-big.png");
			    }
			    if(cliente.getTipo_cliente().equals("Particular")){
			    	cliente.setNif("999999999");
			    }
			    cliente.setData_cadastro(objFunc.DataActual());
			    cliente.setCriado_por(user.getUsername());
			    cliente.setIdEmpresafk(user.getIdEmpresaUser());
			    cliente.setIdCriadorfk(user.getId_usuario());
			    
				R.clienteRepository.save(cliente);
				//----LOg
				 //R.SaveLog("Criou cliente "+cliente.getNome_cliente(),2);
				
				return ResponseEntity.ok().body("Sucesso");		
				
	 }else {
			//response.sendRedirect("/index");
			return ResponseEntity.ok().body("logout");
		}
		}
	 
	
	 // View Editar Cliente
	  @RequestMapping(value="/fiinika/clientes/editar-cliente/{id}", method=RequestMethod.GET)
	   public String EditarCliente(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			
					long id_decod = objFunc.Decodifica(id);
					model.addAttribute("titulo","Editar cliente");
					model.addAttribute("aux",id);
					
					Optional <Cliente> cliente =  R.clienteRepository.findById(id_decod);
					model.addAttribute("cliente", cliente.get());		
//					if(R.clienteRepository.countIdCliente(id_decod)== 0) {
//						  model.addAttribute("display","display:block");
//					}else {
//						  model.addAttribute("display","display:none");
//					}
					
					if(cliente.get().getTipo_cliente().equals("Particular")) {
						model.addAttribute("style","display:none");			
					}
				
			 
			return "/fiinika/clientes/editar-cliente";
			}else {
				return "redirect:/login";
			}
	    }
	 
	// Para o admin: Salva os dados alterados do cliente
		 @RequestMapping(value="/cliente/edit_salvar", method=RequestMethod.POST)
		  public ResponseEntity<String> UpdateCliente(HttpServletRequest request,HttpSession session,Model model,@Valid Cliente newCliente,@RequestParam String aux,@RequestParam String nome_foto_perfil,@RequestParam(value="img_logo",required=false) MultipartFile file_logo, RedirectAttributes attributes,HttpServletResponse response) throws IOException {
		
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
				    long id = objFunc.Decodifica(aux);
				
			 
			  Optional<Cliente> oldCliente = R.clienteRepository.findById(id);// busca dados antigos
		        if(oldCliente.isPresent()){
		            Cliente cliente = oldCliente.get();
		            
		            try {			
		 			   if(!cliente.getTelemovel().toString().equals(newCliente.getTelemovel()+"")){
		 					 if(R.clienteRepository.VerifyTelemovel(newCliente.getTelemovel()+"",user.getIdEmpresaUser()).equals(newCliente.getTelemovel()+"") ) {	 
		 						if(R.clienteRepository.VerifyTelemovel(newCliente.getTelemovel()+"",user.getIdEmpresaUser()) != null  ){
		 							 attributes.addFlashAttribute("mensagem", "Não foi possível registar este numero de telemovel");
		 						}
		 						    return ResponseEntity.ok().body("phone_existis");
		 					 		}
		 					  }
		 			  if(!cliente.getNif().equals(newCliente.getNif())){
		 					 if(R.clienteRepository.VerifyNif(newCliente.getNif(),user.getIdEmpresaUser()).equals(newCliente.getNif()) ) {	 
		 						if(R.clienteRepository.VerifyNif(newCliente.getNif(),user.getIdEmpresaUser()) != null  ){
		 							 attributes.addFlashAttribute("mensagem", "Não foi possível registar este numero de telemovel");
		 						}
		 						    return ResponseEntity.ok().body("nif_existis");
		 					 		}
		 					  }
		 			  
		 			 if(!cliente.getEmail().equals(newCliente.getEmail())){
	 					 if(R.clienteRepository.VerifyEmail(newCliente.getEmail(),user.getIdEmpresaUser()).equals(newCliente.getEmail()) ) {	 
	 						if(R.clienteRepository.VerifyEmail(newCliente.getEmail(),user.getIdEmpresaUser()) != null && !newCliente.getEmail().equals("") ){
	 							 attributes.addFlashAttribute("mensagem", "Não foi possível registar este numero de telemovel");
	 						}
	 						    return ResponseEntity.ok().body("email_existis");
	 					 		}
	 					  }

			 			
			 			} catch (Exception e) {
			 				// TODO: handle exception
			 				System.out.println(e);
			 			}
		         
		            
		              if (file_logo!=null) {
			 			   if(nome_foto_perfil.equals(cliente.getFoto_logotipo())) {
			 				  newCliente.setFoto_logotipo(cliente.getFoto_logotipo());
			 	        	 }else {
			 	        		newCliente.setFoto_logotipo(objFunc.singleFileUpload(request,file_logo,"clientes"));
			 	        	 }
			 		   }else {
			 			  newCliente.setFoto_logotipo(nome_foto_perfil);
			 			     
			 			   }
		           
		              
		           if(newCliente.getTipo_cliente().equals("Particular")){
		        	 newCliente.setNif("999999999");
				    }
		           newCliente.setIdCliente(id);
		           newCliente.setData_cadastro(cliente.getData_cadastro());
		           newCliente.setIdEmpresafk(cliente.getIdEmpresafk());
		           newCliente.setCriado_por(cliente.getCriado_por());
		           newCliente.setIdCriadorfk(cliente.getIdCriadorfk());
		           R.clienteRepository.save(newCliente);	
		           
		       	   //----LOg
				 //  R.SaveLog("Editou perfil de "+cliente.getNomeCliente(),3);
		            
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
		 
		//---------------------------- Para admin: Elimina uma Fornecedor ------------------------------------
		 @RequestMapping(value = "/ajax_delete/cliente", method = RequestMethod.GET)
		 public @ResponseBody HashMap<Integer, String> Delete_Ajax(@RequestParam(value = "id", required = true) String id_banner,HttpSession session) {
			 HashMap<Integer, String> lista = new HashMap<Integer, String>();	 
			
			 try {					
			      long id_decod = objFunc.Decodifica(id_banner);					 
				  Cliente cliente = R.clienteRepository.findById(id_decod).orElse(null);
				  
				  //----LOg
				//  R.SaveLog("Apagou cliente "+cliente.getNomeCliente(),5);
				  
				  R.clienteRepository.delete(cliente);	
				  lista.put(1,"Ok");
				} catch (Exception e) {
					 lista.put(1,"No");
			} 
		     return lista;
		 }
		 
		 
		 @RequestMapping(value = "/ajax/docs_cliente_pesquisa", method = RequestMethod.GET)
		 public @ResponseBody List<List> findAllPesquisa(@RequestParam(value = "nome", required = false) String nome_produto,
				 								 @RequestParam(value = "nome", required = false) String mes, HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
		
			 List<List> documentos = new ArrayList<>();
			 List Ids_codificado = new ArrayList<> ();
		   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
		        authenticatedUser.Dados(model, session, response);
				 //Pesquisa os tres
				if(!nome_produto.equals("")) {
				//R._facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
					return Documemtos(R.clienteRepository.findPesquisaDinamica(user.getIdEmpresaUser(),nome_produto),documentos);	 
				 }			
				 return null;
				   }else {
					   return null;
				   }	 
				    
				}	
		 
		 
		// Retorna lista_docs e lista de isd codificados
				 public List<List> Documemtos(Iterable <Cliente> lista,List<List> ListMain) {
						
					 boolean b = false;
					 List Ids_codificado = new ArrayList<> ();
					 for(Cliente c: lista) {
							try {						
								Ids_codificado.add(objFunc.Codifica(c.getIdCliente()));
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


}
