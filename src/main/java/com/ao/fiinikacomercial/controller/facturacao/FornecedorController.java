package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
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

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Fornecedor;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.*;
import com.ao.fiinikacomercial.service.Repositorio;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.funcoes.Funcoes;

@Controller
public class FornecedorController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
 	 
	// View de todos os Fornecedores
	 @RequestMapping("/fiinika/fornecedores/todos-fornecedores")
	   public String Clientes(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			  
			model.addAttribute("titulo","Todos fornecedores");
			model.addAttribute("fornecedores", R.fornecedorRepository.findById_empresa(user.getIdEmpresaUser()));
			
			return "fiinika/fornecedores/todos-fornecedores";
			}else {
				return "redirect:/index";
			}
	    }
	 
	// View Perfil Fornecedor
		  @RequestMapping(value="/fiinika/fornecedores/perfil-do-fornecedor/{id}", method=RequestMethod.GET)
		   public String PerfilForncedor(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				String total_despesa = "";
				long id_decod = objFunc.Decodifica(id);
				
			    Optional <Fornecedor>  fornecedor = Optional.of(R.fornecedorRepository.findById(id_decod).orElse(null));
				model.addAttribute("titulo","Perfil fornecedor | fiinikaConta");
				model.addAttribute("activo5","activo");
				model.addAttribute("fornecedor", fornecedor.get());
				model.addAttribute("id",id);
				
				total_despesa = R.despesaRepository.TotalDespesaForenecedor(user.getIdEmpresaUser(),id_decod);
				if(total_despesa == null) {
					model.addAttribute("total_despesa",0);
				}else {
					model.addAttribute("total_despesa",total_despesa);
				}
				
				model.addAttribute("despesas",R.despesaRepository.DespesasForenecedor(user.getIdEmpresaUser(),objFunc.AnoActual(),objFunc.MesActual(),id_decod));
				
				//----LOg
				// R.SaveLog("Visualizou perfil de "+fornecedor.get().getNome_fornecedor(),4);
				return "fiinika/fornecedores/perfil-do-fornecedor";
				}else {
					return "redirect:/index";
				}
		    }
	
	 // View Criar Fornecedor
	 @RequestMapping("/fiinika/fornecedores/criar-fornecedor")
	   public String CriarFornecedor(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			 
			model.addAttribute("titulo","Criar fornecedor | fiinikaConta");
			model.addAttribute("activo5","activo");
			
			return "fiinika/fornecedores/criar-fornecedor";
			}else {
				return "redirect:/index";
			}
	    }
	 
// =============================================	 View Editar Fornecedor 	==========================
	  @RequestMapping(value="/fiinika/fornecedores/editar-fornecedor/{id}", method=RequestMethod.GET)
	   public String EditarFornecedor(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			  
				long id_decod = objFunc.Decodifica(id);
				model.addAttribute("titulo","Editar fornecedor | fiinikaConta");
				model.addAttribute("activo5","activo");
				model.addAttribute("fornecedor", R.fornecedorRepository.findByIdF(user.getIdEmpresaUser(),id_decod).orElse(null));
				model.addAttribute("aux",id);
				
				String total_despesa = R.despesaRepository.TotalDespesaForenecedor(user.getIdEmpresaUser(),id_decod);
				if(total_despesa != null) {
					model.addAttribute("style","display:none");
				}
			
			return "fiinika/fornecedores/editar-fornecedor";
			}else {
				return "redirect:/index";
			}
	    }
	  
	 // Salva dados do Fornecedor
	 @RequestMapping(value="/fornecedores/criar", method=RequestMethod.POST)
	 public ResponseEntity<String> SalvaFornecedor(HttpServletRequest request,@Valid Fornecedor fornecedor,
			 HttpSession session,Model model,@RequestParam(value="img_logo", required=false) MultipartFile file_logo,
			 RedirectAttributes attributes,HttpServletResponse response) throws IOException{
		
		 
 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
		 
			try {			
				if(fornecedor.getTelemovel()!=null) {				
					 if(Integer.parseInt(R.fornecedorRepository.VerifyTelemovel(user.getIdEmpresaUser(),fornecedor.getTelemovel()+"")) == fornecedor.getTelemovel()) {						
						 return ResponseEntity.ok().body("phone_existis");
					 }					 
				 
			}
				
				if(fornecedor.getEmail()!=null) {
				 if(R.fornecedorRepository.VerifyEmail(fornecedor.getEmail()) != null && !fornecedor.getEmail().equals("")){
					if(R.fornecedorRepository.VerifyEmail(fornecedor.getEmail()).equals(fornecedor.getEmail())) {
						return ResponseEntity.ok().body("email_existis");	
						
					}
				 }
			}
				
				if(fornecedor.getNif()!=null) {				
						if(R.fornecedorRepository.VerifyNif(user.getIdEmpresaUser(),fornecedor.getNif()).equals(fornecedor.getNif())) {	
							return ResponseEntity.ok().body("nif_existis");				
						}
				}
			
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		   if (file_logo != null) {
			   fornecedor.setFoto_logotipo(objFunc.singleFileUpload(request,file_logo,"fornecedores"));
		    }else{
		    	fornecedor.setFoto_logotipo("placeholder-face-big.png");
		    }
		   fornecedor.setData_cadastro(objFunc.DataActual());
		   fornecedor.setIdEmpresafk(user.getIdEmpresaUser());
		   
			R.fornecedorRepository.save(fornecedor);
			attributes.addFlashAttribute("mensagem", "Fornecedor adicionado com sucesso!");
			//----LOg
			// R.SaveLog("Criou fornecedor "+fornecedor.getNome_fornecedor(),2);
			 return ResponseEntity.ok().body("Sucesso");
			 
	 }else {
			//response.sendRedirect("/index");
			return ResponseEntity.ok().body("logout");
		}
		}
	
	// Para o admin: Salva os dados alterados da empresa 
		 @RequestMapping(value="/fornecedores/edit_salvar", method=RequestMethod.POST)
		   public ResponseEntity<String> UpdateCliente(HttpSession session,Model model,HttpServletRequest request,
				   @Valid Fornecedor newFornecedor,@RequestParam String aux,@RequestParam String nome_foto_perfil,
				   @RequestParam(value="img_logo", required = false) MultipartFile file_logo, RedirectAttributes attributes,
				   HttpServletResponse response) throws IOException {
				
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			    long id = objFunc.Decodifica(aux);			 
				
			  Optional<Fornecedor> oldFornecedor = R.fornecedorRepository.findById(id);// busca dados antigos
		        if(oldFornecedor.isPresent()){
		            Fornecedor fornecedor = oldFornecedor.get();		            
		         try {
		            
		            if(newFornecedor.getTelemovel() != null  ){
		 			   if(!fornecedor.getTelemovel().toString().equals(newFornecedor.getTelemovel()+"")){
		 					 if(R.fornecedorRepository.VerifyTelemovel(user.getIdEmpresaUser(),newFornecedor.getTelemovel()+"").equals(newFornecedor.getTelemovel()+"") ) {
		 						    return ResponseEntity.ok().body("phone_existis");
		 					 		}
		 					  }
		            	}
		            if(newFornecedor.getNif() != null  ){
		 			  if(!fornecedor.getNif().equals(newFornecedor.getNif())){
		 					 if(R.fornecedorRepository.VerifyNif(user.getIdEmpresaUser(),newFornecedor.getNif()).equals(newFornecedor.getNif()) ) {
		 						    return ResponseEntity.ok().body("nif_existis");
		 					 		}
		 					  }
		            }
		 			  
		 			 if(!fornecedor.getEmail().equals(newFornecedor.getEmail())){
	 					 if(R.fornecedorRepository.VerifyEmail(newFornecedor.getEmail()).equals(newFornecedor.getEmail()) ) {	 
	 						if(R.fornecedorRepository.VerifyEmail(newFornecedor.getEmail()) != null && !newFornecedor.getEmail().equals("") ){
	 							 attributes.addFlashAttribute("mensagem", "Não foi possível registar este numero de telemovel");
	 						}
	 						    return ResponseEntity.ok().body("email_existis");
	 					 		}
	 					  }

		 			
		 			} catch (Exception e) {
		 				// TODO: handle exception
		 				System.out.println(e);
		 			}

		 		   
		 		  if (file_logo != null) {
		 			 newFornecedor.setFoto_logotipo(objFunc.singleFileUpload(request,file_logo,"fornecedores"));					   
				    }else {
				    	newFornecedor.setFoto_logotipo(fornecedor.getFoto_logotipo());
				    }
		 		   
		        
		 		  newFornecedor.setData_cadastro(fornecedor.getData_cadastro());
		 		  newFornecedor.setIdEmpresafk(user.getIdEmpresaUser());
		 		  newFornecedor.setIdFornecedor(id);
				   
		           R.fornecedorRepository.save(newFornecedor);
		          
		         //----LOg
				//	 R.SaveLog("Editou perfil de "+fornecedor.getNome_fornecedor(),3);
					 
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
		 @RequestMapping(value = "/ajax_delete/fornecedor", method = RequestMethod.GET)
		 public @ResponseBody HashMap<Integer, String> Delete_Ajax(@RequestParam(value = "id", required = true) String id_banner,HttpSession session) {
			 HashMap<Integer, String> lista = new HashMap<Integer, String>();	 
			
			 try {
					
			      long id_decod = objFunc.Decodifica(id_banner);					 
				  Fornecedor fornecedor = R.fornecedorRepository.findById(id_decod).orElse(null);
				//----LOg
					// R.SaveLog("Apagou fornecedor "+fornecedor.getNome_fornecedor(),5);
				  R.fornecedorRepository.delete(fornecedor);	
				  lista.put(1,"Ok");
				} catch (Exception e) {
					 lista.put(1,"No");
			} 
		     return lista;
		 }
		 
		 
		 @RequestMapping(value = "/ajax/docs_fornecedor_pesquisa", method = RequestMethod.GET)
		 public @ResponseBody List<List> findAllPesquisa(@RequestParam(value = "nome", required = false) String nome_produto,
				 								 @RequestParam(value = "nome", required = false) String mes, HttpSession session,Model model) {				 
		
			 List<List> documentos = new ArrayList<>();
			 List Ids_codificado = new ArrayList<> ();
			// R.DadosGeral(model,session);
				 //Pesquisa os tres
				if(!nome_produto.equals("")) {
				//R._facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
					return Documemtos(R.fornecedorRepository.findPesquisaDinamica(nome_produto),documentos);	 
				 }		
			
					 
				     return null;
				 }	
		 
		 
		// Retorna lista_docs e lista de isd codificados
				 public List<List> Documemtos(Iterable <Fornecedor> lista,List<List> ListMain) {
						
					 boolean b = false;
					 List Ids_codificado = new ArrayList<> ();
					 for(Fornecedor c: lista) {
							try {						
								Ids_codificado.add(objFunc.Codifica(c.getIdFornecedor()));
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
