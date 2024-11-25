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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.model.rh.Ausencias;
import com.ao.fiinikacomercial.model.rh.Deducoes;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.rh.AusenciaRepository;
import com.ao.fiinikacomercial.repository.rh.DeducaoRepository;
import com.ao.fiinikacomercial.repository.rh.FuncionarioRepository;
import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.RetribuicaoRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@Controller
public class ControllerDeducoes {
	
	
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
	
	
	
	//DISPLAY RETRIBUICOES AND DEDUCOES PAGE
			@RequestMapping("/fiinika/salarios/retribuicoes-e-deducoes/deducoes-e-retribuicoes-main")
			public String retDedPage(Model model, HttpServletRequest request, RedirectAttributes attribute,
					HttpSession session,HttpServletResponse response) throws IOException {

		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);	
											
				model.addAttribute("deducoes", deducaoRepository.findAllDeducoes(user.getIdEmpresaUser()));
				model.addAttribute("retribuicoes", retribuicaoRepository.findAllRetribuicoes(user.getIdEmpresaUser()));					
						
					return "/fiinika/salarios/retribuicoes-e-deducoes/deducoes-e-retribuicoes-main";
		   }else {
				
				attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
				return "redirect:/index";
				
				}
				

			}
	
		
		
		//CREATE DEDUCAO PAGE
				@RequestMapping("/fiinika/salarios/retribuicoes-e-deducoes/criar-deducao")
				public String createDeducaoPage(Model model, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
							
					return "/fiinika/salarios/retribuicoes-e-deducoes/criar-deducao";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				@PostMapping("/create-deducao")
				@ResponseBody
				public Map<String, Object> createDeducao(Model model, @Valid Deducoes deducao, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, responseR);
							
							    deducao.setCreator_id((int)id_user);
							    deducao.setEmpresa_id(user.getIdEmpresaUser());
							    deducao.setCreator_name(user.getUsername());
							    deducaoRepository.save(deducao);
							    
							    response.put("success", true);
							    response.put("message", "Successo!");
							    
							    return response;
					   }else {
							
						
						   response.put("success", false);
						    response.put("message", "Failed");
						    
						    return null;
							
							}
				}
		
		
		
	
				
				
				
				
		//EDIT DEDUCAO PAGE
				@RequestMapping("/fiinika/salarios/retribuicoes-e-deducoes/editar-deducao/{id}")
				public String editDeducaoPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
							
												
					model.addAttribute("deducao", deducaoRepository.findDeducaoById(id));
							//System.out.print(id_cat);
						
				
					return "/fiinika/salarios/retribuicoes-e-deducoes/editar-deducao";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				@PostMapping("/edit-deducao")
				@ResponseBody
				public Map<String, Object> editDeducao(Model model, @Valid Deducoes deducao, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, responseR);
							
							    deducao.setCreator_id((int)id_user);
							    deducao.setEmpresa_id(user.getIdEmpresaUser());
							    deducao.setCreator_name(user.getUsername());
							    
				    deducaoRepository.save(deducao);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    
				    return response;
					   }else {
						   return null;
					   }
				}
	

}
