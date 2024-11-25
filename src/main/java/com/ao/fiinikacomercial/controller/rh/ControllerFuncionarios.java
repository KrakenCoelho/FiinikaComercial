package com.ao.fiinikacomercial.controller.rh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.annotation.MultipartConfig;
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
import com.ao.fiinikacomercial.model.rh.Funcionarios;
import com.ao.fiinikacomercial.repository.rh.AusenciaRepository;
import com.ao.fiinikacomercial.repository.rh.CategoriaFRepository;
import com.ao.fiinikacomercial.repository.rh.DeducaoRepository;
import com.ao.fiinikacomercial.repository.rh.FuncionarioRepository;
import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.RetribuicaoRepository;
import com.ao.fiinikacomercial.repository.rh.VencimentoRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@MultipartConfig
@Controller
public class ControllerFuncionarios {
	
	@Autowired
	private AusenciaRepository ausenciaRepository;
	//@Autowired
	//private CambioRepository cambioRepository;
	@Autowired
	private CategoriaFRepository categoriaRepository;
	@Autowired
	private DeducaoRepository deducaoRepository;
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private ProcessamentoRepository processamentoRepository;
	@Autowired
	private RetribuicaoRepository retribuicaoRepository;
	@Autowired
	private VencimentoRepository vencimentoRepository;
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	
	//DISPLAY FUNCIONARIOS 
		@RequestMapping("/fiinika/salarios/funcionarios/todos-funcionarios")
		public String displayFuncionariosPage(Model model, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
										
					model.addAttribute("funcionarios", funcionarioRepository.findAllFuncionarios("Ativo", user.getIdEmpresaUser()));
					
					for (Funcionarios col : funcionarioRepository.findAllFuncionarios("Ativo", user.getIdEmpresaUser())) {
						for (Funcionarios col2 : funcionarioRepository.findById(col.getId())) {
							model.addAttribute("category", categoriaRepository.findNameById(col2.getCategory()));
						}
					}					
				
			    return "/fiinika/salarios/funcionarios/todos-funcionarios";
		     }else {
			
			//attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
			return "redirect:/index";
			
			}
			

		}
		
		
		//CREATE FUNCIONARIO PAGE
				@RequestMapping("/fiinika/salarios/funcionarios/criar-funcionario")
				public String createFuncionarioPage(Model model, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

					 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, response);
							
							
							model.addAttribute("empresa", R.empresaRepository.findEmpresa(user.getIdEmpresaUser()));
												
							model.addAttribute("categorias", categoriaRepository.findAllCategorias(user.getIdEmpresaUser()));
							model.addAttribute("deducoes", deducaoRepository.findAllDeducoes(user.getIdEmpresaUser()));
							model.addAttribute("retribuicoes", retribuicaoRepository.findAllRetribuicoes(user.getIdEmpresaUser()));
							
					  
							return "/fiinika/salarios/funcionarios/criar-funcionario";
					}else {
					
						attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
						return "redirect:/index";					
					}
					

				}
				
				
				@PostMapping("/create-funcionario")
				@ResponseBody
				public Map<String, Object> createFuncionario(Model model, @Valid Funcionarios funcionario, @RequestParam("files") MultipartFile files, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				   
				    
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, responseR);
					
					funcionario.setEmpresa_id(user.getIdEmpresaUser());
				    NumberFormat decimalFormat = NumberFormat.getInstance(Locale.getDefault());
					decimalFormat.setGroupingUsed(true); // this will also round numbers, 3
					decimalFormat.setMaximumFractionDigits(2);
				    
				    String realPathtoUploads = request.getServletContext().getRealPath("imgs/bis/");
				    String filename = "no-file";

		            if (!files.isEmpty()) {
		                byte[] bytes = files.getBytes();
		                String novoNome = new Date().getTime() + files.getOriginalFilename().substring(files.getOriginalFilename().length() - 5);
		                novoNome = novoNome.replace(")", "");
		                Path caminho = Paths.get(realPathtoUploads + novoNome);
		                Files.write(caminho, bytes);
		                funcionario.setId_document(novoNome);
		                filename = files.getOriginalFilename();
		                Files.write(Paths.get(realPathtoUploads + filename), bytes);
		            }
		            
		          //CALCULATE COST PER HOUR
					double number_of_days_worked = funcionario.getDays_per_week()*4;
					int work_hours = funcionario.getHours_per_day();
					double hours_worked = number_of_days_worked * work_hours;
					double total_salary = Double.parseDouble(funcionario.getTotal_salary_kwanza().replaceAll(",", "").replaceAll("KZ", ""));
					
					funcionario.setCategory_name(categoriaRepository.findNameById(funcionario.getCategory()));
				

					double extras = total_salary / hours_worked;
					funcionario.setCost_per_hour(decimalFormat.format(extras));

					//SET ADMISSION MONTH AND YEAR
					LocalDate setDate = LocalDate.parse(funcionario.getAdmission_date());
					funcionario.setMonth_of_admission(setDate.getMonth().toString());
					funcionario.setYear_of_admission(setDate.getYear());
		            
				    
				    funcionarioRepository.save(funcionario);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    response.put("id", funcionario.getId());
				    
				    return response;
			   }else {
				   return null;
			   }
				}
		
		
		
	
				
				
				
				
				
				
				
				
				
				
				
		//VIEW FUNCIONARIO PROFILE PAGE
				@RequestMapping("/fiinika/salarios/funcionarios/perfil-do-funcionario/{id}")
				public String viewFuncionarioPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

					 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, response);
												
							model.addAttribute("funcionario", funcionarioRepository.findById(id));
							model.addAttribute("vencimentos", vencimentoRepository.findByFuncionarioId(id));
							model.addAttribute("ausencias", ausenciaRepository.findByEmployeeId(id));
							//System.out.print(id_cat);
						
						
					
				
					return "/fiinika/salarios/funcionarios/perfil-do-funcionario";
				   }
					else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}		
	
				
				
				
		//EDIT FUNCIONARIO PAGE
				@RequestMapping("/fiinika/salarios/funcionarios/editar-funcionario/{id}")
				public String editFuncionarioPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

					 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, response);
												
							model.addAttribute("funcionario", funcionarioRepository.findById(id));
							model.addAttribute("categorias", categoriaRepository.findAllCategorias(user.getIdEmpresaUser()));
							model.addAttribute("deducoes", deducaoRepository.findAllDeducoes(user.getIdEmpresaUser()));
							model.addAttribute("retribuicoes", retribuicaoRepository.findAllRetribuicoes(user.getIdEmpresaUser()));
							
							model.addAttribute("empresa", R.empresaRepository.findEmpresa(user.getIdEmpresaUser()));
							System.out.println(user.getIdEmpresaUser());
						
							return "/fiinika/salarios/funcionarios/editar-funcionario";
					   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				
				
				
				
				@PostMapping("/edit-funcionario")
				@ResponseBody
				public Map<String, Object> justifyAusencia(Model model, @Valid Funcionarios funcionario, @RequestParam("files") MultipartFile files, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, responseR);
							
							funcionario.setEmpresa_id(user.getIdEmpresaUser());
				   
				    NumberFormat decimalFormat = NumberFormat.getInstance(Locale.getDefault());
					decimalFormat.setGroupingUsed(true); // this will also round numbers, 3
					decimalFormat.setMaximumFractionDigits(2);
				    
				    String realPathtoUploads = request.getServletContext().getRealPath("imgs/uploads/");
				    String filename = "no-file";

		            if (!files.isEmpty()) {
		                byte[] bytes = files.getBytes();
		                String novoNome = new Date().getTime() + files.getOriginalFilename().substring(files.getOriginalFilename().length() - 5);
		                novoNome = novoNome.replace(")", "");
		                Path caminho = Paths.get(realPathtoUploads + novoNome);
		                Files.write(caminho, bytes);
		                funcionario.setId_document(novoNome);
		                filename = files.getOriginalFilename();
		                Files.write(Paths.get(realPathtoUploads + filename), bytes);
		            }
		            
		          //CALCULATE COST PER HOUR
					double number_of_days_worked = funcionario.getDays_per_week()*4;
					int work_hours = funcionario.getHours_per_day();
					double hours_worked = number_of_days_worked * work_hours;
					double total_salary = Double.parseDouble(funcionario.getTotal_salary_kwanza().replaceAll(",", "").replaceAll("KZ", ""));
					
					funcionario.setCategory_name(categoriaRepository.findNameById(funcionario.getCategory()));
				

					double extras = total_salary / hours_worked;
					funcionario.setCost_per_hour(decimalFormat.format(extras));

					//SET ADMISSION MONTH AND YEAR
					LocalDate setDate = LocalDate.parse(funcionario.getAdmission_date());
					funcionario.setMonth_of_admission(setDate.getMonth().toString());
					funcionario.setYear_of_admission(setDate.getYear());
				    
				    funcionarioRepository.save(funcionario);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    response.put("id", funcionario.getId());
				    
				    return response;
					   }else {
						   return null;
					   }
				}	
				
			@PostMapping("/ajax_get_category_details")
			@ResponseBody
			public String getCategoriaDetaials(Model model, @Valid int id_cat) {
				
				return categoriaRepository.findBSById(id_cat);
						
			}

}
