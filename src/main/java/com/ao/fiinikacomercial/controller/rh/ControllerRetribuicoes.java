package com.ao.fiinikacomercial.controller.rh;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.model.rh.Retribuicoes;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.rh.AusenciaRepository;
import com.ao.fiinikacomercial.repository.rh.DeducaoRepository;
import com.ao.fiinikacomercial.repository.rh.FuncionarioRepository;
import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.RetribuicaoRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@Controller
public class ControllerRetribuicoes {
	
	
	@Autowired
	private AusenciaRepository ausenciaRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private DeducaoRepository deducaoRepository;
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private ProcessamentoRepository processamentoRepository;
	@Autowired
	private RetribuicaoRepository retribuicaoRepository;
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
		
		
		//CREATE RETRIBUICAO PAGE
				@RequestMapping("/fiinika/salarios/retribuicoes-e-deducoes/criar-retribuicao")
				public String createRetribuicaoPage(Model model, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					
					model.addAttribute("user_name", user.getUsername());
					model.addAttribute("user_id", user.getId_usuario());
							
					return "/fiinika/salarios/retribuicoes-e-deducoes/criar-retribuicao";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				@PostMapping("/create-retribuicao")
				@ResponseBody
				public Map<String, Object> createRetribuicao(Model model, @Valid Retribuicoes retribuicao, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, responseR);
				    System.out.println("ID: "+retribuicao.getCreator_id());
				    
				    retribuicao.setEmpresa_id(user.getIdEmpresaUser());
					 retribuicao.setCreator_id((int) user.getIdEmpresaUser());
					 retribuicao.setCreator_name(user.getUsername());
				    retribuicaoRepository.save(retribuicao);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    
				    return response;
			   }else {
				   return null;
			   }
		 } 
		
		
		
	
				
				
				
				
		//EDIT RETRIBUICAO PAGE
				@RequestMapping("/fiinika/salarios/retribuicoes-e-deducoes/editar-retribuicao/{id}")
				public String editRetribuicaoPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);						
												
					model.addAttribute("retribuicao", retribuicaoRepository.findRetribuicaoById(id));						
						
					return "/fiinika/salarios/retribuicoes-e-deducoes/editar-retribuicao";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				@PostMapping("/edit-retribuicao")
				@ResponseBody
				public Map<String, Object> editRetribuicao(Model model, @Valid Retribuicoes retribuicao, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, responseR);	
					 Retribuicoes retrOld = retribuicaoRepository.findById(retribuicao.getId()).orElse(null);
					 retribuicao.setEmpresa_id(user.getIdEmpresaUser());
					 retribuicao.setCreator_id((int) user.getIdEmpresaUser());
					 retribuicao.setCreator_name(user.getUsername());
					 
				    retribuicaoRepository.save(retribuicao);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
			   }else {
				   
			   }
				    return response;
				}
	
	

}
