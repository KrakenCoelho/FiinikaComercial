package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.util.HashMap;

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

//import com.ao.fiinikacomercial.model.Fornecedor;
import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Categoria;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.service.Repositorio;
import com.ao.fiinikacomercial.security.AuthenticatedUser;

@Controller
public class CategoriaController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	 //----- View todos usuarios
	 @RequestMapping("/fiinika/categorias/todas-categorias")
	   public String Categorias(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
		
				model.addAttribute("titulo","Todas categorias");
				model.addAttribute("categorias",R.categoriaRepository.findAllCategoria(user.getIdEmpresaUser()));
				
				//R.Role(model,session,response);	
				return "/fiinika/categorias/todas-categorias";
			}else {
				return "redirect:/index";
			}
	    }
	 
	 
	//View  produtos dentro da Categoria
	 @RequestMapping(value="/fiinika/categorias/categoria-page/{id}", method=RequestMethod.GET)
	   public String ViewDentroCategoria(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		
		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
            long id_decod = objFunc.Decodifica(id);
			
			model.addAttribute("titulo","Categoria page");
			 if(R.produtoRepository.countProdCategoria(id_decod) != 0) {
				 model.addAttribute("produtos", R.produtoRepository.findProdutoByIdCategoria(id_decod));
			 }else {
				 model.addAttribute("nome_categoria", R.categoriaRepository.findById(id_decod).get().getNomeCategoria());
			 }
					
			model.addAttribute("aux",id);
			
			//R.Role(model,session,response);	
			return "/fiinika/categorias/categoria-page";
			}else {
				return "redirect:/index";
			}
	    }
	 
		
	 //View criar Categoria
		 @RequestMapping("/fiinika/categorias/criar-categoria")
		   public String ViewCriarCategoria(Model model, HttpSession session,HttpServletResponse response) throws IOException{
			 
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				// System.out.print(objSessao);
			   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				model.addAttribute("titulo","Criar categoria");
				
				//R.Role(model,session,response);	
				return "/fiinika/categorias/criar-categoria";
				}else {
					return "redirect:/index";
				}
		    }
		 
		 
		// Salva dados da Categoria
			 @RequestMapping(value="/categoria/criar", method=RequestMethod.POST)
			 public ResponseEntity<String> SalvaCategoria(Model model,HttpServletRequest request,@Valid Categoria categoria,HttpSession session,RedirectAttributes attributes,HttpServletResponse response) throws IOException{
				
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				
				   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
			        categoria.setIdEmpresafk(user.getIdEmpresaUser());
			     
				   R.categoriaRepository.save(categoria);
					
				  attributes.addFlashAttribute("mensagem", "Categoria adicionada com sucesso!");
			      return ResponseEntity.ok().body("Sucesso");
			      
			 }else {
					//response.sendRedirect("/index");
					return ResponseEntity.ok().body("logout");
				}
				}
			 
			//View  produtos dentro da Categoria
			 @RequestMapping(value="/fiinika/categorias/editar-categoria/{id}", method=RequestMethod.GET)
			   public String ViewEditar(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
				
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			// System.out.print(objSessao);
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
		            long id_decod = objFunc.Decodifica(id);
					
					model.addAttribute("titulo","Editar categoria");
					model.addAttribute("categoria", R.categoriaRepository.findById(id_decod).orElse(null));
					model.addAttribute("aux",id);
					model.addAttribute("cont",R.itemRepository.countIdCateg(id_decod,user.getIdEmpresaUser()));
				
					return "/fiinika/categorias/editar-categoria";
					}else {
						return "redirect:/index";
					}
			    }
			 
			// Salva dados da Categoria editada
			 @RequestMapping(value="/categoria/editada", method=RequestMethod.POST)
			 public ResponseEntity<String> SalvaCategoriaEditada(HttpSession session,Model model,HttpServletRequest request,@Valid Categoria categoria,@RequestParam("aux")  String aux ,RedirectAttributes attributes,HttpServletResponse response) throws IOException{
				
		      Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			// System.out.print(objSessao);
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					 long id_decod = objFunc.Decodifica(aux);
					 
				     categoria.setIdCategoria(id_decod);
				     categoria.setIdEmpresafk(user.getIdEmpresaUser());	
				     
					 R.categoriaRepository.save(categoria);
					
				  attributes.addFlashAttribute("mensagem", "Categoria adicionada com sucesso!");
			      return ResponseEntity.ok().body("Sucesso");
			      
			 }else {
					//response.sendRedirect("/index");
					return ResponseEntity.ok().body("logout");
				}
				}
			 
			 
			 
			 @RequestMapping(value = "/ajax_delete/categoria", method = RequestMethod.GET)
			 public @ResponseBody HashMap<Integer, String> Delete_Ajax(@RequestParam(value = "id", required = true) String id_banner,HttpSession session) {
				 HashMap<Integer, String> lista = new HashMap<Integer, String>();	 
				
				 try {						
				      long id_decod = objFunc.Decodifica(id_banner);					 
					  Categoria categoria = R.categoriaRepository.findById(id_decod).orElse(null);
					  R.categoriaRepository.delete(categoria);	
					  lista.put(1,"Ok");
					} catch (Exception e) {
						 lista.put(1,"No");
				} 
			     return lista;
			 }

}
