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
import org.springframework.http.HttpStatus;
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
import com.ao.fiinikacomercial.model.facturacao.Categoria;
import com.ao.fiinikacomercial.model.facturacao.Produto;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.*;
import com.ao.fiinikacomercial.service.Repositorio;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.funcoes.Funcoes;

@Controller
public class ProdutoController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
 
//View  Categorias
	 @RequestMapping("/fiinika/produtos/todos-produtos")
	   public String ViewCategoria(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			  
			model.addAttribute("titulo","Todos produtos");
			model.addAttribute("produtos", R.produtoRepository.findAllProduto(user.getIdEmpresaUser()));	
			model.addAttribute("categorias", R.categoriaRepository.findById_empresa(user.getIdEmpresaUser()));	
		
			return "fiinika/produtos/todos-produtos";
			}else {
				return "redirect:/index";
			}
	    }
	
	//View  Todos produtos
		 @RequestMapping(value="/fiinika/produtos/produto-page/{id}", method=RequestMethod.GET)
		   public String ViewDentroCategoria(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
			
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
	            long id_decod = objFunc.Decodifica(id);
				
				model.addAttribute("titulo","Produto page");
				model.addAttribute("produto", R.produtoRepository.findProdutoById(id_decod,user.getIdEmpresaUser()));
				model.addAttribute("aux",id);
			
				return "/fiinika/produtos/produto-page";
				}else {
					return "redirect:/index";
				}
		    }
	
     //View criar Produto
	 @RequestMapping("/fiinika/produtos/criar-produto")
	   public String ViewCriarProduto(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				
			model.addAttribute("titulo","Criar produto");
			
			Iterable <Categoria> cat = R.categoriaRepository.findById_empresa(user.getIdEmpresaUser());
			if(cat.toString().equals("[]")) {
				model.addAttribute("catg",0);
			}else {
				model.addAttribute("catg",1);
			}
			model.addAttribute("categorias",R.categoriaRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("isencao",R.isencaoRepository.findAll());
		
			return "fiinika/produtos/criar-produto";
			}else {
				return "redirect:/index";
			}
	    }
	// Salva dados do Produto
			 @RequestMapping(value="/produto/criar", method=RequestMethod.POST)
			 public ResponseEntity<String> SalvaProduto(HttpServletRequest request,@Valid Produto produto,HttpSession session,Model model,@RequestParam(value="img_produto",required= false) MultipartFile file_produto,@RequestParam("id_categoriafk") Long id_catfk,RedirectAttributes attributes,HttpServletResponse response) throws IOException{
				
				 
				 
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					
					if(!produto.getReferencia().equals("")) {
						if(R.produtoRepository.existsByReferenciaAndIdEmpresaFkp(produto.getReferencia(),user.getIdEmpresaUser())) {		    		
				    	     return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Referencia already registered!");
				    	}
					}
				   if (file_produto != null) {
					   produto.setFoto_produto(objFunc.singleFileUpload(request,file_produto,"produtos"));
				    }else{
				    	produto.setFoto_produto("placeholder-face-big.png");
				    }
				 //  R.ver(produto.getPreco_produto());
					 produto.setPreco_produto(objFunc.RemovePV(produto.getPreco_produto()));
				     switch ((int)produto.getId_isencaofk()) {
						case 524:
							 produto.setTaxa_prod("14.00");
							break;
						case 563:
							 produto.setTaxa_prod("7.00");
							break;
						case 564:
							 produto.setTaxa_prod("5.00");
							break;
						case 565:
							 produto.setTaxa_prod("2.00");
							break;
						default:
							 produto.setTaxa_prod("00.00");		
							break;
						}				
			
				    produto.setIdEmpresaFkp(user.getIdEmpresaUser());
				    R.produtoRepository.save(produto);				    
				      
					attributes.addFlashAttribute("mensagem", "Produto adicionado com sucesso!");
					attributes.addFlashAttribute("id_cat", produto.getId_categoriafk());
					
					 return ResponseEntity.ok().body("Sucesso");
					 
			 }else {
					//response.sendRedirect("/index");
					return ResponseEntity.ok().body("logout");
				}
				}
			 
	 // View Editar Produto
	  @RequestMapping(value="/fiinika/produtos/editar-produto/{id}", method=RequestMethod.GET)
	   public String EditarProduto(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		  
		  long id_decod = objFunc.Decodifica(id);
				  
					 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
						
				  if(R.itemRepository.countIdProd(id_decod,user.getIdEmpresaUser())== 0) {
					  model.addAttribute("display","display:block");
				  }else {
					  model.addAttribute("display","display:none");
				   }
				  
					model.addAttribute("titulo","Editar produto");
					model.addAttribute("categorias",R.categoriaRepository.findAll());
					model.addAttribute("produto", R.produtoRepository.findById(id_decod).orElse(null));			
					model.addAttribute("isencao",R.isencaoRepository.findAll());
					model.addAttribute("aux",id);
					
					  return "fiinika/produtos/editar-produto";
					}else {
						return "redirect:/index";
					}
			    }
			  
	  // Salva dados editados do produto
		 @RequestMapping(value="/produto/edit_salvar", method=RequestMethod.POST)
		 public ResponseEntity<String>  SalvaDadosProduto(HttpServletRequest request,@Valid Produto newProduto,HttpSession session,@RequestParam("nome_foto_perfil") String nome_foto_perfil,@RequestParam("aux") String aux,@RequestParam(value="img_produto",required=false) MultipartFile file_produto,Model model,RedirectAttributes attributes,HttpServletResponse response) throws IOException{
			
			 
			Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
						
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
				    long id_decod = objFunc.Decodifica(aux);
					
					 newProduto.setIdProduto(id_decod);
					
					 try {
					
					 Optional<Produto> oldProduto = R.produtoRepository.findById(id_decod);// busca dados antigos
					 
					 
					 if(!newProduto.getReferencia().equals("")) {
						 if(!newProduto.getReferencia().equals(oldProduto.get().getReferencia())) {
							if(R.produtoRepository.existsByReferenciaAndIdEmpresaFkp(newProduto.getReferencia(),user.getIdEmpresaUser())) {		    		
					    	     return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Referencia already registered!");
					    	}
						 }
						}
					
					 if(oldProduto.isPresent()){
				            Produto prod = oldProduto.get();
				            newProduto.setQtdStock(prod.getQtdStock());
					 
					  if (file_produto!=null) {
			 			   if(nome_foto_perfil.equals(newProduto.getFoto_produto())) {
			 				  newProduto.setFoto_produto(prod.getFoto_produto());
			 	        	 }else {
			 	        		newProduto.setFoto_produto(objFunc.singleFileUpload(request,file_produto,"produtos"));
			 	        	 }
			 		   }else {			 			       
			 			   newProduto.setFoto_produto(nome_foto_perfil);			 			     
			 			    }
					  
				     }
					 newProduto.setPreco_produto(objFunc.RemovePV(newProduto.getPreco_produto()));
					 
					 switch ((int)newProduto.getId_isencaofk()) {
						case 524:
							newProduto.setTaxa_prod("14.00");
							break;
						case 563:
							newProduto.setTaxa_prod("7.00");
							break;
						case 564:
							newProduto.setTaxa_prod("5.00");
							break;
						case 565:
							newProduto.setTaxa_prod("2.00");
							break;
						default:
							newProduto.setTaxa_prod("00.00");		
							break;
						}			
					 
					
					} catch (Exception e) {
						// TODO: handle exception
						//R.ver(e+"");
					}
					   newProduto.setIdEmpresaFkp(user.getIdEmpresaUser());
					   R.produtoRepository.save(newProduto);
					
						attributes.addFlashAttribute("mensagem", "Alteração efectuada com sucesso!");
						
						 return ResponseEntity.ok().body("Sucesso");
						 
						 
				 }else {
						//response.sendRedirect("/index");
						return ResponseEntity.ok().body("logout");
					}
				 }
				
				 @RequestMapping(value = "/ajax_delete/produto", method = RequestMethod.GET)
				 public @ResponseBody HashMap<Integer, String> Delete_Ajax(@RequestParam(value = "id", required = true) String id_banner,HttpSession session) {
					 HashMap<Integer, String> lista = new HashMap<Integer, String>();	 
					
					 try {
							
					      long id_decod = objFunc.Decodifica(id_banner);					 
					      Produto produto= R.produtoRepository.findById(id_decod).orElse(null);
					        R.produtoRepository.delete(produto);
						  lista.put(1,"Ok");
						} catch (Exception e) {
							 lista.put(1,"No");
					} 
				     return lista;
				 }
				 
				 
				//---------------------------------- AJAX FILTRO Despesas
				 
		 @RequestMapping(value = "/ajax/docs_produtos", method = RequestMethod.GET)
		 public @ResponseBody List<List> findAll(@RequestParam(value = "categoria", required = false) String categoria,
				 								 @RequestParam(value = "nome", required = false) String mes, HttpSession session, Model model,HttpServletResponse response) throws IOException {				 
		
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
				if(!categoria.equals("")) {
				//R._facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
					return R.Documemtos(R.produtoRepository.findProdutoCategoriaSearch(Long.parseLong(categoria)),documentos);	 
				 }			
			
					 
				     return null;
		   }else {
			   return null;
		   }
				 }		
		 
		 
		 @RequestMapping(value = "/ajax/docs_produtos_pesquisa", method = RequestMethod.GET)
		 public @ResponseBody List<List> findAllPesquisa(@RequestParam(value = "nome", required = false) String nome_produto,
				 								 @RequestParam(value = "nome", required = false) String mes, HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
			Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();		
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				
					 List<List> documentos = new ArrayList<>();
					 List Ids_codificado = new ArrayList<> ();
						
				 //Pesquisa os tres
				if(!nome_produto.equals("")) {
				//R._facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
					return R.Documemtos(R.produtoRepository.findPesquisaDinamica(user.getIdEmpresaUser(),nome_produto),documentos);	 
				 }			
			
					 
				     return null;
		   }else {
			   return null;
		   }
				 }		
		 
		 @RequestMapping(value = "/ajax/docs_ref_produto", method = RequestMethod.GET)
		 public @ResponseBody Optional<Produto> findReferencia(@RequestParam(value = "ref", required = false) String ref,
				 								  HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
			Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();		
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				
				return R.produtoRepository.findByReferenciaAndIdEmpresaFkp(ref, user.getIdEmpresaUser());						
				
		   }else {
			    return null;
		   }
				 }		
				
				
				 
				 
}
