package com.ao.fiinikacomercial.controller.rh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import com.ao.fiinikacomercial.model.rh.Funcionarios;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.rh.AusenciaRepository;
import com.ao.fiinikacomercial.repository.rh.DeducaoRepository;
import com.ao.fiinikacomercial.repository.rh.FuncionarioRepository;
import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.RetribuicaoRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;


@Controller
public class ControllerAusencias {
	
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
	
	
	
	//DISPLAY HISTORICO AUSENCIAS
		@RequestMapping("/fiinika/salarios/faltas/historico-de-faltas")
		public String historicoAusencias(Model model, HttpServletRequest request, RedirectAttributes attribute,
				HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
										
					model.addAttribute("ausencias", ausenciaRepository.findAllByEmpresaId(user.getIdEmpresaUser()));
					
					return "/fiinika/salarios/faltas/historico-de-faltas";
			   }else {
			
			attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
			return "redirect:/index";
			
			}
			

		}
		
		
		
		
		
		//CREATE AUSENCIA PAGE
				@RequestMapping("/fiinika/salarios/faltas/registar-falta")
				public String createAusenciaPage(Model model, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
												
					model.addAttribute("ausencias", ausenciaRepository.findAllByEmpresaId(user.getIdEmpresaUser()));
					model.addAttribute("user_id", id_user);
					model.addAttribute("user_name", user.getUsername());
					model.addAttribute("funcionarios", funcionarioRepository.findAllFuncionarios("Ativo", user.getIdEmpresaUser()));
					
						
					return "/fiinika/salarios/faltas/registar-falta";
				   }else {
						
						attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
						return "redirect:/index";
						
						}
				}
				
				
				@PostMapping("/create-ausencia")
				@ResponseBody
				public Map<String, Object> createAusencia(Model model, @Valid Ausencias ausencia, HttpServletRequest request, HttpSession session, HttpServletResponse response2) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, response2);
				    NumberFormat decimalFormat = NumberFormat.getInstance(Locale.getDefault());
					decimalFormat.setGroupingUsed(true);
					decimalFormat.setMaximumFractionDigits(2);
				    
				    for(Funcionarios funcionario: funcionarioRepository.findById(ausencia.getEmployee_id())) {
				    	ausencia.setEmployee_name(funcionario.getName());
				    	
				    	double absence_fee = Double.parseDouble(funcionario.getCost_per_hour().replaceAll(",", "")) * funcionario.getHours_per_day(); 
						ausencia.setAbsence_fee(decimalFormat.format(absence_fee));
				    }
				    
				    Date date = ausencia.getPeriod(); // Assuming getPeriod() returns a Date object

				    Calendar calendar = Calendar.getInstance();
				    calendar.setTime(date);

				    int year = calendar.get(Calendar.YEAR);
				    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);

				    ausencia.setYear(year);
				    ausencia.setMonth(month);
				    
				    ausencia.setEmpresa_id(user.getIdEmpresaUser());
				    
				    ausenciaRepository.save(ausencia);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    
				    return response;
				    
					   }else {							
						   response.put("success", false);							
						   return response;
						}
				}
		
		
		
	
				
				
				
				
		//VIEW AUSENCIA PAGE
				@RequestMapping("/fiinika/salarios/faltas/falta-ind/{id}")
				public String viewAusenciaPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
						
												
						model.addAttribute("ausencia", ausenciaRepository.findById(id));
						
					return "/fiinika/salarios/faltas/falta-ind";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}		
	
				
				@PostMapping("/justify-ausencia")
				@ResponseBody
				public Map<String, Object> justifyAusencia(Model model, @Valid Ausencias ausencia, HttpServletRequest request, HttpSession session, HttpServletResponse response2) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, response2);
				    ausencia.setEmpresa_id(user.getIdEmpresaUser());
				    ausenciaRepository.save(ausencia);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    	return response;
				    
					   }else {							
						   response.put("success", false);							
						   return response;
						}
				}		
				
				
				
}
