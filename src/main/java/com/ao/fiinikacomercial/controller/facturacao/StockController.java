package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Produto;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@Controller
public class StockController {
		
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	 @RequestMapping(value="/fiinika/stock/meu-stock", method=RequestMethod.GET)
	   public String PromocoesGerais(Model model, HttpSession session,HttpServletRequest httpServletRequest,HttpServletResponse response) throws IOException{
		 
				 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();				
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);
					    authenticatedUser.Dados(model, session, response);				    

			model.addAttribute("titulo","Stock main | Admin");	
			model.addAttribute("produtos",R.produtoRepository.findProductoEmpresa(user.getIdEmpresaUser()));	
			model.addAttribute("emStock",R.produtoRepository.countStock(3,user.getIdEmpresaUser()));	
			model.addAttribute("poucoStock",R.produtoRepository.countPoucoStock(3,user.getIdEmpresaUser()));	
			model.addAttribute("semStock",R.produtoRepository.countSemStock(0,user.getIdEmpresaUser()));	
			
			return "fiinika/stock/meu-stock";
			
			}else {
				return  "redirect:/login";
			}
	    }
	 
	 @RequestMapping(value="/admin/stock-in", method=RequestMethod.POST)
	 public ResponseEntity<String> AddStock(HttpServletRequest request,HttpSession session,	Model model,HttpServletResponse response,
			 @RequestParam(name="qtdStock",required=true) int qtd,
			 @RequestParam(name="produto",required=true) long id,
			 RedirectAttributes attributes) throws IOException{
		
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();				
		   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_logado = Long.parseLong(dadosUser[0]);
		    Usuario user = R.userRepository.findById(id_logado).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
		    
		    Produto prod = R.produtoRepository.findById(id).orElse(null);
		    if(prod!=null) {
		    	prod.setQtdStock(prod.getQtdStock()+qtd);
		    	R.produtoRepository.save(prod);
		    	
		    	 return ResponseEntity.status(HttpStatus.CREATED).body(prod.getQtdStock()+"");		    
		    }else {
		    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conflict: Falha");		
		    }

		   }else {
			   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Conflict: Não autorizado");		
		   }
		}
//	 
	 @RequestMapping(value="/admin/stock-out", method=RequestMethod.POST)
	 public ResponseEntity<String> RemStock(HttpServletRequest request,HttpSession session,Model model,HttpServletResponse response,
			 @RequestParam(name="qtdStock",required=true) int qtd,
			 @RequestParam(name="produto",required=true) long id,
			 RedirectAttributes attributes) throws IOException{
		
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();				
		   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_logado = Long.parseLong(dadosUser[0]);
		    Usuario user = R.userRepository.findById(id_logado).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
		    
		    Produto prod = R.produtoRepository.findById(id).orElse(null);
		    if(prod!=null) {
		    	prod.setQtdStock(prod.getQtdStock()-qtd);
		    	if(prod.getQtdStock() < 0) {
		    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
		    	}
		    	R.produtoRepository.save(prod);
		    	
		    	 return ResponseEntity.status(HttpStatus.CREATED).body(prod.getQtdStock()+"");		    
		    }else {
		    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conflict: Falha");		
		    }

		   }else {
			   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Conflict: Não autorizado");		
		   }
		}

}
