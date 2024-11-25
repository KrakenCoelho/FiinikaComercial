package com.ao.fiinikacomercial.controller.rh;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.rh.Ausencias;
import com.ao.fiinikacomercial.model.rh.Funcionarios;
import com.ao.fiinikacomercial.model.rh.Processamentos;
import com.ao.fiinikacomercial.model.rh.Retribuicoes;
import com.ao.fiinikacomercial.model.rh.Vencimentos;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.rh.AusenciaRepository;
import com.ao.fiinikacomercial.repository.rh.DeducaoRepository;
import com.ao.fiinikacomercial.repository.rh.FuncionarioRepository;
import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.RetribuicaoRepository;
import com.ao.fiinikacomercial.repository.rh.VencimentoRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;

@MultipartConfig
@Controller
public class ControllerProcessamentos {
	
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
	@Autowired
	private VencimentoRepository vencimentoRepository;
	
	
	
	//DISPLAY PROCESSAMENTOS 
		@RequestMapping("/fiinika/salarios/processamento-de-salarios/historico-de-processamentos")
		public String displayProcessamentosPage(Model model, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					System.out.println(user.getIdEmpresaUser());					
					model.addAttribute("processamentos", processamentoRepository.findAllProcessamentos(user.getIdEmpresaUser()));
					
					
			return "/fiinika/salarios/processamento-de-salarios/historico-de-processamentos";
			   }else {
			
			attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
			return "redirect:/index";
			
			}
			

		}
		
		
		
		
		
		//CREATE PROCESSAMENTO PAGE
				@RequestMapping("/fiinika/salarios/processamento-de-salarios/processar-salario")
				public String createProcessamentoPage(Model model, HttpServletRequest request, RedirectAttributes attribute,
						HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					model.addAttribute("user_name", user.getUsername());
					model.addAttribute("user_id", user.getId_usuario());
												
				    model.addAttribute("funcionarios", funcionarioRepository.findAllFuncionarios("Ativo", user.getIdEmpresaUser()));
						
			   return "/fiinika/salarios/processamento-de-salarios/processar-salario";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				@PostMapping("/create-processamento")
				@ResponseBody
				public Map<String, Object> createProcessamento(Model model, @Valid Vencimentos vencimento, @Valid Processamentos processamento, HttpServletRequest request,
						HttpSession session,HttpServletResponse responseR) throws IOException, MessagingException, ParseException {
				    Map<String, Object> response = new HashMap<>();
					 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, responseR);
							
							System.out.println("START DATE: " + processamento.getStart_date());
							System.out.println("END DATE: " + processamento.getStart_date());
							System.out.println("CREATION DATE: " + processamento.getStart_date());
							
						    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				   
						    NumberFormat decimalFormat = NumberFormat.getInstance(Locale.getDefault());
							decimalFormat.setGroupingUsed(true); // this will also round numbers, 3
							decimalFormat.setMaximumFractionDigits(2);
						    
						    //SET ADMISSION MONTH AND YEAR
						    Date date = processamento.getCreation_date();
						    Calendar calendar = Calendar.getInstance();
						    calendar.setTime(date);
						    int year = calendar.get(Calendar.YEAR);
						    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
						    
						    
						    //processamento.setCreation_date(new Date());
						    
						    System.out.println("DATA: "+processamento.getCreation_date());
						    System.out.println("DATA 2: "+processamento.getEnd_date());
						    
						    processamento.setYear(year);
						    processamento.setMonth(month);
						    processamento.setEmpresa_id(user.getIdEmpresaUser());
						    
						    processamentoRepository.save(processamento);
						    
						    int[] funcionarios_id = convertStringToIntArray(processamento.getFuncionarios_ids());
						    
						    for(int i=0; i < funcionarios_id.length; i++) { // COLLABORATORS ARRAY LOOP
						        
						        for(Funcionarios f : funcionarioRepository.findById(funcionarios_id[i])) {
						            Vencimentos newVencimento = new Vencimentos(); // Create a new Vencimentos object for each iteration
						            
						            newVencimento.setProcessamento_id(processamento.getId());
						            newVencimento.setAdmin_id(processamento.getAdmin_id());
						            newVencimento.setAdmin_name(processamento.getAdmin_name());
						            newVencimento.setFuncionario_id(f.getId());
						            newVencimento.setAddress(f.getAddress());
						            newVencimento.setName(f.getName());
						            newVencimento.setGender(f.getGender());
						            newVencimento.setNif(f.getNif());
						            newVencimento.setIban(f.getIban());
						            newVencimento.setInss(f.getInss());
						            newVencimento.setInss_company(f.getInss_company());
						            newVencimento.setIrt_fee(f.getIrt_fee());
						            newVencimento.setAddress(f.getAddress());
						            newVencimento.setEmail(f.getEmail());
						            newVencimento.setPhone(f.getPhone());
						            newVencimento.setAlt_phone(f.getAlt_phone());
						            newVencimento.setCategory(f.getCategory());
						            newVencimento.setType_of_contract(f.getType_of_contract());
						            newVencimento.setId_document(f.getId_document());
						            newVencimento.setAdmission_date(f.getAdmission_date());
						            newVencimento.setCategory_name(f.getCategory_name());
						            newVencimento.setDays_per_week(f.getDays_per_week());
						            newVencimento.setHours_per_day(f.getHours_per_day());
						            newVencimento.setCost_per_hour(f.getCost_per_hour());
						            newVencimento.setBase_salary(f.getBase_salary());
						            newVencimento.setRetributions(f.getRetributions());
						            newVencimento.setDeductions(f.getDeductions());
						            newVencimento.setTotal_deductions(f.getTotal_deductions());
						            newVencimento.setTotal_retributions(f.getTotal_retributions());
						            newVencimento.setTotal_deductions(f.getTotal_deductions());
						            newVencimento.setTotal_salary_dollar(f.getTotal_salary_dollar());
						            newVencimento.setTotal_salary_euro(f.getTotal_salary_euro());
						            newVencimento.setTotal_salary_kwanza(f.getTotal_salary_kwanza());
						            newVencimento.setHours_worked(f.getHours_worked());
						            newVencimento.setDays_worked(f.getDays_worked());
						            newVencimento.setExtra_hours(f.getExtra_hours());
						            newVencimento.setStatus("X");
						            newVencimento.setMonth_of_admission(f.getMonth_of_admission());
						            newVencimento.setYear_of_admission(f.getYear_of_admission());
						            
						            newVencimento.setSubsidy_christmas(f.getSubsidy_christmas());
						            newVencimento.setSubsidy_executive(f.getSubsidy_executive());
						            newVencimento.setSubsidy_family(f.getSubsidy_family());
						            newVencimento.setSubsidy_food(f.getSubsidy_food());
						            newVencimento.setSubsidy_rent(f.getSubsidy_rent());
						            newVencimento.setSubsidy_transport(f.getSubsidy_transport());
						            newVencimento.setSubsidy_vacation(f.getSubsidy_vacation());
						            newVencimento.setSubsidy_other(f.getSubsidy_other());
						            
						            newVencimento.setIrt_fee(f.getIrt_fee());
						            
						            newVencimento.setCreation_date(new Date());
						            newVencimento.setStart_date(processamento.getStart_date());
						            newVencimento.setEnd_date(processamento.getEnd_date());
						            						            		
						            	
						          //FOR ABSENCE FEE
									Date d = new Date();
									SimpleDateFormat formatter = new SimpleDateFormat("MM");
									String month_ = "";
									String date_num = formatter.format(d);
									double absence_fee = 0;
									
									if(date_num.equals("01")) {month_ = "JANUARY";}; if(date_num.equals("02")) {month_ = "FEBRUARY";}; if(date_num.equals("03")) {month_ = "MARCH";};
									if(date_num.equals("04")) {month_ = "APRIL";}; if(date_num.equals("05")) {month_ = "MAY";}; if(date_num.equals("06")) {month_ = "JUNE";}; 
									if(date_num.equals("07")) {month_ = "JULY";}; if(date_num.equals("08")) {month_ = "AUGUST";}; if(date_num.equals("09")) {month_ = "SEPTEMBER";};
									if(date_num.equals("10")) {month_ = "OCTOBER";}; if(date_num.equals("11")) {month_ = "NOVEMBER";}; if(date_num.equals("12")) {month_ = "DECEMBER";};
									
									
										for(Ausencias ausencia : ausenciaRepository.findAllByMonthAndStatus(month_, "X", f.getId())) {
											
											absence_fee = absence_fee + Double.parseDouble(ausencia.getAbsence_fee().replaceAll(",", ""));
											
										}
										
										newVencimento.setAbsence_fee(decimalFormat.format(absence_fee)+" KZ");
										
										
										double base_salary = Double.parseDouble(f.getBase_salary().replaceAll(",", "").replaceAll("KZ", ""));
										double total_retributions = Double.parseDouble(f.getTotal_retributions().replaceAll(",", "").replaceAll("KZ", ""));
										
										double gross_salary = base_salary + total_retributions;
										newVencimento.setGross_salary(decimalFormat.format(gross_salary)+ " KZ");
										
										double total_salary = Double.parseDouble(f.getTotal_salary_kwanza().replaceAll(",", "").replaceAll("KZ", ""));
										double total_deductions = Double.parseDouble(f.getTotal_deductions().replaceAll(",", "").replaceAll("KZ", ""));
										double inss = Double.parseDouble(f.getInss().replaceAll(",", "").replaceAll("KZ", ""));
										double irt_fee = Double.parseDouble(f.getIrt_fee().replaceAll(",", "").replaceAll("KZ", ""));
										
										total_deductions = total_deductions + absence_fee;
										total_salary = total_salary - absence_fee;
										
										double total_deduction_other = total_deductions - (irt_fee + inss + absence_fee);
										
										newVencimento.setDeduction_other(decimalFormat.format(total_deduction_other)+" KZ");
										newVencimento.setTotal_deductions(decimalFormat.format(total_deductions)+" KZ");
										
										String ts_kwanza = decimalFormat.format(total_salary);
										newVencimento.setTotal_salary_kwanza(ts_kwanza+" KZ");
										for(Empresa e : R.empresaRepository.findEmpresa(user.getIdEmpresaUser())) {

											String ts_dollar = decimalFormat.format(total_salary / Double.parseDouble(e.getCambioDollar()));
											newVencimento.setTotal_salary_dollar("$ "+ts_dollar);
											
											String ts_euro = decimalFormat.format(total_salary / Double.parseDouble(e.getCambioEuro()));
											newVencimento.setTotal_salary_euro("€ "+ts_euro);
											
										}
										
								
										
										newVencimento.setEmpresa_id(user.getIdEmpresaUser());
						            vencimentoRepository.save(newVencimento);
				        }
				    
				    }
				    response.put("success", true);
				    response.put("message", "Successo!");
				    response.put("id", processamento.getId());
				    
				    return response;
				}else {
					 return null;
				}
				}

				private int[] convertStringToIntArray(String str) {
				    if (str == null || str.isEmpty()) {
				        return new int[0]; // Return an empty array if the input string is null or empty
				    }
				    String[] strArray = str.split(",");
				    int[] intArray = new int[strArray.length];
				    for (int i = 0; i < strArray.length; i++) {
				        intArray[i] = Integer.parseInt(strArray[i].trim());
				    }
				    return intArray;
				}

				
				
				
				
				
				
				
				
				
				
		//VIEW PRE-PROCESSAMENTO  PAGE
				@RequestMapping("/fiinika/salarios/processamento-de-salarios/pre-processamento/{id}")
				public String viewPreProcessamentoPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
							
												
							model.addAttribute("processamento", processamentoRepository.findById(id));
							model.addAttribute("vencimentos", vencimentoRepository.findByProcessamentoId(id));
							model.addAttribute("ven_num", vencimentoRepository.countByProcessmatoId(id));
							
							model.addAttribute("empresa", R.empresaRepository.findEmpresa(user.getIdEmpresaUser()));
							//System.out.print(id_cat);
							
					return "/fiinika/salarios/processamento-de-salarios/pre-processamento";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}		
	
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
		//EDIT PRE-PROCESSAMENTO PAGE
				@RequestMapping("fiinika/salarios/processamento-de-salarios/editar-pre-processamento-ind/{id}")
				public String editProcessamentoPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
						
							for(Vencimentos vencimento : vencimentoRepository.findById(id)) {
								model.addAttribute("processamento", processamentoRepository.findById(vencimento.getProcessamento_id()));
								model.addAttribute("deducoes", deducaoRepository.findAllDeducoes(user.getIdEmpresaUser()));
								model.addAttribute("retribuicoes", retribuicaoRepository.findAllRetribuicoes(user.getIdEmpresaUser()));
							}					
							
							model.addAttribute("vencimento", vencimentoRepository.findById(id));
							
						
				
					return "fiinika/salarios/processamento-de-salarios/editar-pre-processamento-ind";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				
				
				
				
				@PostMapping("/edit-processamento")
				public String editProcessamento(Model model, @Valid Processamentos processamento, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException, MessagingException {
				   
					 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);
							authenticatedUser.Dados(model, session, response);
							
						    processamento.setEmpresa_id(user.getIdEmpresaUser());
						    processamentoRepository.save(processamento);
				    
					   	attribute.addFlashAttribute("message", "Successo!");
						return "redirect:/fiinika/salarios/processamento-de-salarios/mapa-salarial/"+processamento.getId();
					   }else {			
						   return "";
						}
				}
				
				@PostMapping("/update-processamento")
				@ResponseBody
				public Map<String, Object> updateProcessamento(Model model, @Valid Processamentos processamento, HttpServletRequest request,HttpSession session,HttpServletResponse response2) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);
						authenticatedUser.Dados(model, session, response2);
						
					    processamento.setEmpresa_id(user.getIdEmpresaUser());
					    processamentoRepository.save(processamento);
						    
						    response.put("success", true);
						    response.put("message", "Successo!");
				    
				           return response;
					   }else {							
						   response.put("success", false);							
						   return response;
						}
				}
				
				
				@PostMapping("/edit-pre-processamento")
				@ResponseBody
				public Map<String, Object> editPreProcessamento(Model model, @Valid Vencimentos vencimento, HttpServletRequest request,HttpSession session,HttpServletResponse response2) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    
				    Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);
						authenticatedUser.Dados(model, session, response2);
						
						vencimento.setEmpresa_id(user.getIdEmpresaUser());
				        vencimentoRepository.save(vencimento);
				    
					    response.put("success", true);
					    response.put("message", "Successo!");
					    response.put("id", vencimento.getProcessamento_id());
					    
					    return response;
				   }else {							
					   response.put("success", false);							
					   return response;
					}
				}
				
				
				
				
				@PostMapping("/cancel-processamento")
				@ResponseBody
				public Map<String, Object> cancelProcessamento(Model model, @Valid int processamento_id, HttpServletRequest request) throws IOException, MessagingException {
				    Map<String, Object> response = new HashMap<>();
				    
				    processamentoRepository.updateProcessamento("X", processamento_id);
				    
				    response.put("success", true);
				    response.put("message", "Successo!");
				    
				    return response;
				}
				
				
				
				
				
				
				
				
				
		//EDIT MAPA SALARIAL PAGE
				@RequestMapping("/fiinika/salarios/processamento-de-salarios/mapa-salarial/{id}")
				public String mapaSalarialPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
							
							model.addAttribute("id", id);					
							model.addAttribute("processamento", processamentoRepository.findById(id));
							model.addAttribute("vencimentos", vencimentoRepository.findByProcessamentoId(id));
							
							model.addAttribute("empresa", R.empresaRepository.findEmpresa(user.getIdEmpresaUser()));
						
					return "/fiinika/salarios/processamento-de-salarios/mapa-salarial";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				
				//SEND RECIBOS
				@PostMapping("/send_recibos")
				@ResponseBody
				public Iterable<Vencimentos> ajaxSendRecibos(Model model, @Valid int processamento_id, HttpServletRequest request) throws ResendException, IOException {
				    System.out.println("Processamento_id: " + processamento_id);

				    vencimentoRepository.updateRecieveStatus("Y", processamento_id);
				    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

				    for (Processamentos p : processamentoRepository.findById(processamento_id)) {
				        
				        String funcionarios_ids = p.getFuncionarios_ids();
				        String[] idArray = funcionarios_ids.split(",");

				        // Loop through the array and send email with attachment
				        for (String id : idArray) {
				            int idAsInt = Integer.parseInt(id); // Convert the string to an int

				            String email = funcionarioRepository.findEmailById(idAsInt);
				            String employeeName = funcionarioRepository.findNameById(idAsInt);

				            // Generate PDF by sending 'fact' to PHP script
				            int funcionarioId = Integer.parseInt(id);  // Assuming vencimentoId is the same as the funcionario ID
				            boolean isPdfGenerated = generatePDF(funcionarioId, processamento_id, request);

				            if (isPdfGenerated) {
				                // Specify the path to your recibos folder and PDF file
				                String safeEmployeeName = employeeName.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
				                String attachmentPath = request.getServletContext().getRealPath("imgs/recibos/" + currentDate + " " + safeEmployeeName + ".pdf");
				                File pdfFile = new File(attachmentPath);

				                // Check if the PDF file exists
				                if (pdfFile.exists()) {
				                    // Encode the PDF file to Base64
				                    String base64EncodedPdf = encodeFileToBase64(pdfFile);
				                    
				                    String htmlMessage = "<html>" +
				                            "<body style='font-family: Arial, sans-serif;'>" +
				                            "<div style='background-color: #f9f9f9; padding: 20px;'>" +
				                            "<h2 style='color: #4CAF50;'>Olá " + employeeName + ",</h2>" +
				                            "<p>Estamos felizes em informar que o seu salário referente a <strong>" + currentDate + "</strong> foi processado com sucesso.</p>" +
				                            "<p>Por favor, encontre o seu recibo de pagamento anexado a este e-mail. Recomendamos que você o revise e entre em contato caso tenha alguma dúvida ou questão.</p>" +
				                            "<p>Agradecemos pelo seu esforço e dedicação à empresa. Valorizamos muito o seu trabalho e contribuição.</p>" +
				                            "<p>Atenciosamente,</p>" +
				                            "<p><strong>Equipe Fiinika</strong></p>" +
				                            "<hr style='border: 1px solid #ddd;'/>" +
				                            "<p style='font-size: 12px; color: #888;'>Esta é uma mensagem automática. Por favor, não responda.</p>" +
				                            "</div>" +
				                            "</body>" +
				                            "</html>";

				                    // Send the email with the PDF attachment
				                    Resend resend = new Resend("re_PdWcAqGc_LNykrApEYiKwwt6wY8Du7jAP");
				                    SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
				                            .from("Fiinika <geral@musicash.co>")
				                            .to(email)
				                            .subject("Recibo de Pagamento - " + currentDate)
				                            .html(htmlMessage)
				                            .addAttachment(Attachment.builder()
				                                    .fileName(currentDate + " " + safeEmployeeName + ".pdf")
				                                    .content(base64EncodedPdf)
				                                    .build()
				                            )
				                            .build();

				                    SendEmailResponse data = resend.emails().send(sendEmailRequest);
				                    System.out.println("EMAIL RESPONSE: " + data);
				                } else {
				                    System.out.println("PDF file not found for ID: " + idAsInt);
				                }
				            } else {
				                System.out.println("Failed to generate PDF for ID: " + idAsInt);
				            }
				        }
				    }

				    return vencimentoRepository.findByProcessamentoId(processamento_id);
				}

				// Method to generate PDF by calling the PHP script
				/*private boolean generatePDF(int factId, int factId2, HttpServletRequest request) {
				    try {
				        // Construct the URL with the query parameter
				        String urlString = "https://" + getHostname(request) + "/pdf/recibo2.php?fact=" + factId +"&fact2="+ factId2;
				        URL url = new URL(urlString);

				        // Open a connection to the URL
				        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				        connection.setRequestMethod("GET");

				        // Check if the request was successful
				        int responseCode = connection.getResponseCode();
				        if (responseCode == HttpURLConnection.HTTP_OK) {
				            System.out.println("PDF generated successfully for fact ID: " + factId);
				            return true;
				        } else {
				            System.out.println("Failed to generate PDF. Response Code: " + responseCode);
				            return false;
				        }

				    } catch (Exception e) {
				        e.printStackTrace();
				        return false;
				    }
				}*/
				
				
				private boolean generatePDF(int factId, int factId2, HttpServletRequest request) throws IOException {
				    String url = "http://localhost/pdf/recibo2.php?fact=" + factId +"&fact2="+ factId2; 
				    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				    connection.setRequestMethod("GET");

				    int responseCode = connection.getResponseCode();
				    if (responseCode == HttpURLConnection.HTTP_OK) {
			            System.out.println("PDF generated successfully for fact ID: " + factId);
			            return true;
			        } else {
			            System.out.println("Failed to generate PDF. Response Code: " + responseCode);
			            return false;
			        }
				}
				

				// Helper method to encode a file to Base64
				private String encodeFileToBase64(File file) throws IOException {
				    FileInputStream fileInputStream = new FileInputStream(file);
				    byte[] fileBytes = new byte[(int) file.length()];
				    fileInputStream.read(fileBytes);
				    fileInputStream.close();
				    return Base64.getEncoder().encodeToString(fileBytes);
				}

				// Helper method to get the hostname
				private String getHostname(HttpServletRequest request) {
				    // Return the hostname dynamically from the request
				    return request.getServerName(); // This will return "localhost" or the actual hostname in production
				}

				
				
				
				
				
				
				
				//DISPLAY PROCESSAMENTOS 
				@RequestMapping("/fiinika/salarios/doc-banco/{id}")
				public String displayProcessamentosPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
							
					model.addAttribute("processamento", processamentoRepository.findById(id));
					model.addAttribute("vencimentos", vencimentoRepository.findByProcessamentoId(id));
							
						
					return "/fiinika/salarios/doc-banco";
			   }else {
					
					attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
					return "redirect:/index";
					
					}
					

				}
				
				

}
