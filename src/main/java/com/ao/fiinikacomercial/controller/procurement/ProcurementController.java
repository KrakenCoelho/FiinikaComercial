package com.ao.fiinikacomercial.controller.procurement;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;



import java.util.Arrays;

import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.model.procurement.DescProposta;
import com.ao.fiinikacomercial.model.procurement.Pagamento;
import com.ao.fiinikacomercial.model.procurement.Pedido;
import com.ao.fiinikacomercial.model.procurement.PedidoSector;
import com.ao.fiinikacomercial.model.procurement.Proposta;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.facturacao.EmpresaRepository;
import com.ao.fiinikacomercial.repository.facturacao.ProdutoRepository;
import com.ao.fiinikacomercial.repository.facturacao.SectoresRepository;
import com.ao.fiinikacomercial.repository.procurement.DescPropostaRepository;
import com.ao.fiinikacomercial.repository.procurement.PagamentoRepository;
import com.ao.fiinikacomercial.repository.procurement.PedidoRepository;
import com.ao.fiinikacomercial.repository.procurement.PedidoSectorRepository;
import com.ao.fiinikacomercial.repository.procurement.PropostaRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.ProxyPay;
import com.ao.fiinikacomercial.service.Repositorio;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.Attachment;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;

import okhttp3.*;






@Controller
public class ProcurementController {

	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	SectoresRepository sectoresRepository;
	
	@Autowired
	PedidoSectorRepository pedidosectorRepository;
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	PropostaRepository propostaRepository;
	
	@Autowired
	DescPropostaRepository descPropostaRepository;
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	@Autowired
	PagamentoRepository pagamentoRepository;
	
	
	
	 // Long id = 2l;
	
	Funcoes anexo = new Funcoes();
	//APIRef api = new APIRef();
	@Autowired
	ProxyPay api;
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;
	
	UserDetails userDetails;
	
	
	
	
	
	
	
	
//	 __       __                                                                __  __        __                     
//	 |  \     /  \                                                              |  \|  \      |  \                    
//	 | $$\   /  $$  ______   __    __   _______         ______    ______    ____| $$ \$$  ____| $$  ______    _______ 
//	 | $$$\ /  $$$ /      \ |  \  |  \ /       \       /      \  /      \  /      $$|  \ /      $$ /      \  /       \
//	 | $$$$\  $$$$|  $$$$$$\| $$  | $$|  $$$$$$$      |  $$$$$$\|  $$$$$$\|  $$$$$$$| $$|  $$$$$$$|  $$$$$$\|  $$$$$$$
//	 | $$\$$ $$ $$| $$    $$| $$  | $$ \$$    \       | $$  | $$| $$    $$| $$  | $$| $$| $$  | $$| $$  | $$ \$$    \ 
//	 | $$ \$$$| $$| $$$$$$$$| $$__/ $$ _\$$$$$$\      | $$__/ $$| $$$$$$$$| $$__| $$| $$| $$__| $$| $$__/ $$ _\$$$$$$\
//	 | $$  \$ | $$ \$$     \ \$$    $$|       $$      | $$    $$ \$$     \ \$$    $$| $$ \$$    $$ \$$    $$|       $$
//	  \$$      \$$  \$$$$$$$  \$$$$$$  \$$$$$$$       | $$$$$$$   \$$$$$$$  \$$$$$$$ \$$  \$$$$$$$  \$$$$$$  \$$$$$$$ 
//	                                                  | $$                                                            
//	                                                  | $$                                                            
//	                                                   \$$                                                            
//	
	
	
	
	 @RequestMapping("/fiinika/procurement/meus-pedidos/novo-pedido")
	   public String dashboardEmpresaDash(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			  
			  model.addAttribute("todos", sectoresRepository.findAll());
			  model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
			  
		
			  
			  
			  
			  
			return "fiinika/procurement/meus-pedidos/novo-pedido";
			}else {
				return "redirect:/index";
			}
	    }
	
	
	
	
		
	
	 @PostMapping({"/savepedido"})
	   public ResponseEntity<String> saveafiliado(Model model, @Valid Pedido pedido,@RequestParam (name="sector")Long[] sectores,@RequestParam (name="dFC")String dFC,@RequestParam (name="dIC")String dIC, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException, ParseException, ResendException {
	      	
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			
			 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			 	pedido.setDataInicioConcurso(dateFormat.parse(dIC));
				pedido.setDataFechoConcurso(dateFormat.parse(dFC));  
			 	pedido.setEstado("Pendente");
			 	pedido.setDataCriacao(new Date());
			 	pedidoRepository.save(pedido);
			 	PedidoSector ps = new PedidoSector();
			 	//System.out.println(pedido.getDataInicioConcurso());
			 	
			 	
			 	
			 	for(int i=0; i<sectores.length; i++) {
					ps.setPedidoIdFK(pedido.getIdPedido());
					ps.setSectorIdFK(sectores[i]);
					pedidosectorRepository.save(ps);
					ps=new PedidoSector();
				}
			 	for(List empresaEmail:pedidoRepository.EmpresasAfectesAoPedoido(pedido.getIdPedido())) {
			 		
			 		String emailDaeEmpresaQuePediu= empresaEmail.get(1).toString() ;
				 	
				 	 if(emailDaeEmpresaQuePediu.equals("")   || emailDaeEmpresaQuePediu==null) {
				 		 
				 	 }else {
				 	
				 	 /// String base64EncodedPdf = encodeFileToBase64(pdfFile);
	                    
				 		 
				 		 
				 		 
	                    String htmlMessage = "<html>" +
	                            "<body style='font-family: Arial, sans-serif;'>" +
	                            "<div style='background-color: #f9f9f9; padding: 20px;'>" +
	                            "<h2 style='color: #1E579C;'>Saudações prezados </h2>" +
	                            //"<p>Estamos felizes em informar que o seu salário referente a <strong>" + currentDate + "</strong> foi processado com sucesso.</p>" +
	                            "<p> Recebeu um novo pedido ,por favor visite a plataforma  </p>" +
	                            "<p>Agradecemos pelo seu esforço e dedicação à empresa. Valorizamos muito o seu trabalho e contribuição.</p>" +
	                            "<p>Atenciosamente,</p>" +
	                            "<p><strong>Equipe Fiinika</strong></p>" +
	                            "<hr style='border: 1px solid #ddd;'/>" +
	                            "<p style='font-size: 12px; color: #888;'>Esta é uma mensagem automática. Por favor, não responda.</p>" +
	                            "</div>" +
	                            "</body>" +
	                            "</html>";
	                    
	                    String Empresa = empresaRepository.NomeEmpresa(user.getIdEmpresaUser());
	                    // Send the email with the PDF attachment
	                    Resend resend = new Resend("re_PdWcAqGc_LNykrApEYiKwwt6wY8Du7jAP");
	                    SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
	                            .from("Fiinika <geral@fiinika-comercial.com>")
	                            .to(emailDaeEmpresaQuePediu)
	                            .subject("Novo Pedido: " + Empresa)
	                            .html(htmlMessage)
	                            //.addAttachment(Attachment.builder()
	                              //      .fileName(currentDate + " " + safeEmployeeName + ".pdf")
	                                //    .content(base64EncodedPdf)
	                                  //  .build()
	                           // )
	                            .build();

	                  //  SendEmailResponse data = resend.emails().send(sendEmailRequest);
	                    
	                    
	                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	                    
	                    Runnable tarefa = () -> {
	                        try {
	                            // Aqui vai a lógica de envio de email com a função resend
	                            SendEmailResponse data = resend.emails().send(sendEmailRequest);
	                            System.out.println("Email enviado com sucesso: " + data);
	                        } catch (Exception e) {
	                            System.out.println("Falha ao enviar email: " + e.getMessage());
	                        }
	                    };

	                    // Agendar para executar após 5 segundos
	                    scheduler.schedule(tarefa, 5, TimeUnit.SECONDS);

	                    // Encerrar o scheduler após execução
	                    scheduler.shutdown();
	                    
	                    //System.out.println("EMAIL RESPONSE: " + data);
				 	 }
				 	
			 		
			 	}
			
			 			         return ResponseEntity.ok().body("ok");
		   }else {
				 return ResponseEntity.ok().body("logout");
			}
	   }
	 
	 
	 @RequestMapping("/fiinika/procurement/meus-pedidos/meus-pedidos-historico")
	   public String dashboardEmpresaDashHistorico(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			  
		  	  model.addAttribute("concursos", pedidoRepository.meusPedidos(user.getIdEmpresaUser())  );
			  model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
			  model.addAttribute("estado", pedidoRepository.estadoesp(user.getIdEmpresaUser()));
			  model.addAttribute("ano", pedidoRepository.anoesp(user.getIdEmpresaUser()));
			  model.addAttribute("mes", pedidoRepository.mesesp(user.getIdEmpresaUser()));
			return "/fiinika/procurement/meus-pedidos/meus-pedidos-historico";
			}else {
				return "redirect:/index";
			}
	    }
	 
	 
	 
//	 @RequestMapping(
//			    value = {"/pedido/procurar/"},
//			    method = {RequestMethod.POST}
//			)
//			@ResponseBody
//			public List<List> ProcurarPedido(Model model, @Valid Long empresa_id, @Valid String search, @Valid String ano, @Valid String mes, @Valid String estado, HttpServletRequest request) {
//			    if (empresa_id != 0L && !search.equals("") && !ano.equals("") && !mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodos(empresa_id, search, ano, mes, estado);
//			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && mes.equals("") && estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaDescricao(empresa_id, search);
//			    } else if (empresa_id != 0L && !search.equals("") && !ano.equals("") && mes.equals("") && estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaDescricaoAno(empresa_id, search, ano);
//			    } else if (empresa_id != 0L && !search.equals("") && !ano.equals("") && !mes.equals("") && estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaDescricaoAnoMes(empresa_id, search, ano, mes);
//			    } else if (empresa_id != 0L && search.equals("") && ano.equals("") && mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaEstado(empresa_id, estado);
//			    } else if (empresa_id != 0L && search.equals("") && ano.equals("") && !mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaMesEstado(empresa_id, mes, estado);
//			    } else if (empresa_id != 0L && search.equals("") && !ano.equals("") && !mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaAnoMesEstado(empresa_id, ano, mes, estado);
//			    } else if (empresa_id != 0L && search.equals("") && ano.equals("") && !mes.equals("") && estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaMes(empresa_id, mes);
//			    } else if (empresa_id != 0L && search.equals("") && !ano.equals("") && !mes.equals("") && estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaAnoMes(empresa_id, ano, mes);
//			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && !mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaDescricaoMesEstado(empresa_id, search, mes, estado);
//			    } else if (empresa_id != 0L && search.equals("") && !ano.equals("") && mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaAnoEstado(empresa_id, ano, estado);
//			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && mes.equals("") && !estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaDescricaoEstado(empresa_id, search, estado);
//			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && !mes.equals("") && estado.equals("")) {
//			        return this.pedidoRepository.proctodosEmpresaMesDescricao(empresa_id, search, mes);
//			    } else {
//			        return empresa_id != 0L && search.equals("") && !ano.equals("") && mes.equals("") && estado.equals("") ? this.pedidoRepository.proctodosEmpresaAno(empresa_id, ano) : this.pedidoRepository.meusPedidos(empresa_id);
//			    }
//			}

	 @RequestMapping(
			    value = {"/pedido/procurar/"},
			    method = {RequestMethod.POST}
			)
			@ResponseBody
			public List<Pedido> ProcurarPedido(Model model, @Valid Long empresa_id, @Valid String search, @Valid String ano, @Valid String mes, @Valid String estado, HttpServletRequest request) {
			 	
		 			System.out.println(pedidoRepository.proctodos1(empresa_id, search,ano,mes,estado));
			        return this.pedidoRepository.proctodos1(empresa_id, search,ano,mes,estado);
			 
			    
			}
	 
	 @RequestMapping("/fiinika/procurement/meus-pedidos/pedido-main/{idpedido}")
	   public String pedidoMain(@PathVariable("idpedido")String idpedido,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			Long decod =anexo.Decodifica(idpedido);
			  model.addAttribute("td", pedidoRepository.findById(decod).orElse(null)  );
			  model.addAttribute("sectores", pedidoRepository.sectoresdopedido(decod)  );
			  model.addAttribute("categorias", categoriaRepository.findAllCategoria(user.getIdEmpresaUser())  );
			  model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
			  model.addAttribute("artigos", propostaRepository.propostadopedido(decod));
			  model.addAttribute("pagos", propostaRepository.propostapaga(decod));
			  model.addAttribute("Emp", propostaRepository.empresaspropostas(decod));
			  
			return "fiinika/procurement/meus-pedidos/pedido-main";
			}else {
				return "redirect:/index";
			}
	    }
	
	 
	 @PostMapping({"/savepag"})
	   public ResponseEntity<String> savepag(Model model, @Valid Pagamento pagamento
			   ,@RequestParam (name="prodIds")Long [] prodIds
			   ,@RequestParam (name="qtd")String [] qtd
			   ,@RequestParam (name="globo")String [] globo
			   , HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException {
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
				System.out.println(empresa.getNomeEmpresa());
				authenticatedUser.Dados(model, session, response);
				
				
				List prdId = new ArrayList<>();
			for(int i = 0; i<prodIds.length;i++){
				if(prodIds[i]!=null && !prodIds[i].equals("")) {
					
					
					descPropostaRepository.actualizqtd(qtd[i],globo[i], prodIds[i]);
					prdId.add(prodIds[i]);
					//System.out.println(prodIds[i]);
				}
			
			}


		// pagamento.setData_pag(new Date());	
		 pagamento.setReferencia(api.gerarReferencia(empresa, Double.parseDouble(pagamento.getValor()),"CompraProcurement" ,  request));
		 //pagamento.setReferencia(api.gh(request, empresa, pagamento.getValor(),anexo.GeraReferencia() , "2024-09-10"));
		 pagamento.setEmpresa_que_paga(user.getIdEmpresaUser());
		 
		 pagamento.setEntidade("01068");
		 pagamento.setEstatuto("Pendente");
		 pagamento.setTipo("Pagamento");
		 pagamento.setProdutos_ids(prdId.toString());
		 pagamentoRepository.save(pagamento);
			
	         return ResponseEntity.ok().body(pagamento.getReferencia()); 
	   }
			 return ResponseEntity.ok().body("oko"); 
	 }
	 
	 @RequestMapping("/fiinika/procurement/meus-pedidos/pagamento-page---procurement/{referencia}")
	   public String PagamentoPage(@PathVariable("referencia")String referencia,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			  
			 model.addAttribute("info", pagamentoRepository.referenciainfo(referencia)  );
			  
			return "fiinika/procurement/meus-pedidos/pagamento-page---procurement";
			}else {
				return "redirect:/index";
			}
	    }
	 
	
	 
	 
	 @GetMapping({"/olhapag/{referencia}"})
	   public ResponseEntity<String> verifref(@PathVariable("referencia") String referencia, Model model, RedirectAttributes attributes, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ParseException {
	   
	     
	      return pagamentoRepository.re(referencia).equals("Pendente") ? ResponseEntity.ok().body("Pendente") : ResponseEntity.ok().body("pago");
	   }
	 
	 @PostMapping({"/confirmapagamento"})
	 @ResponseBody
	 public String confirmapagamento(@RequestParam(value = "referencia",required = true) String reference,HttpServletRequest request,HttpServletResponse response,HttpSession sessionD) throws MessagingException {
	     pagamentoRepository.actuapag( reference);
	     String arr= pagamentoRepository.arra(reference); 
	    // System.out.println(arr);
	     Long id_proposta = pagamentoRepository.proposta(reference);
	     Long id_pedido = pagamentoRepository.pedidoId(reference);
	     
	     propostaRepository.propo(id_proposta);
	     
	     pedidoRepository.actualizaPago(id_pedido);
	     
	     List<Integer> list = Arrays.stream(arr.substring(1, arr.length() - 1).split(","))
              .map(String::trim)        
              .map(Integer::parseInt)    
              .collect(Collectors.toList()); 
  
	     for (Integer  tr :list) {
	           
	            
	            descPropostaRepository.actualizadp(tr);
	            
	           int quantidade = descPropostaRepository.quantidade(tr);
	           produtoRepository.actualizqtd_stock(quantidade, tr);
	           // System.out.println(tr);
	        }
	     
	     
	   
	      return reference;
	   }
	 
	
	   @PostMapping({"/rejeitarproposta/{id}"})
	   public ResponseEntity<Void> rejeitarprpoposta(@PathVariable("id") Long id, Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ParseException, IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);

	         propostaRepository.rejeitarporposta(id);
	          
	         
	      }
	      return ResponseEntity.ok().build();
	      //return control ? "redirect:/promotores/eventos-promotor/evento-ind-main-promotor/"+ anexo.Codifica(id) : "redirect:/index";
	   }
	 
	   @PostMapping({"/negarentrega/{id}"})
	   public ResponseEntity<Void> negarEntrega(@PathVariable("id") Long id, Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ParseException, IOException {
			 
		   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);


					
	         propostaRepository.negarentrega(id);
	          
	         
	      }
	      return ResponseEntity.ok().build();
	      //return control ? "redirect:/promotores/eventos-promotor/evento-ind-main-promotor/"+ anexo.Codifica(id) : "redirect:/index";
	   }
	 
	   
	   @PostMapping({"/confirmarentrega/{idproposta}/{idpedido}"})
	   public ResponseEntity<Void> confirmarEntrega(@PathVariable("idproposta") Long idproposta,@PathVariable("idpedido") String idpedido, Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ParseException, IOException {
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
					
	         propostaRepository.confirmarentrega(idproposta);
	          
	         }
	      
	      return ResponseEntity.ok().build();
	      //return control ? "redirect:/promotores/eventos-promotor/evento-ind-main-promotor/"+ anexo.Codifica(id) : "redirect:/index";
	   }
	 
	 
	   
	   
//	   _______                   __  __        __                                                  __                                                       
//	   |       \                 |  \|  \      |  \                                                |  \                                                      
//	   | $$$$$$$\  ______    ____| $$ \$$  ____| $$  ______    _______         ______   __    __  _| $$_     ______    ______   _______    ______    _______ 
//	   | $$__/ $$ /      \  /      $$|  \ /      $$ /      \  /       \       /      \ |  \  /  \|   $$ \   /      \  /      \ |       \  /      \  /       \
//	   | $$    $$|  $$$$$$\|  $$$$$$$| $$|  $$$$$$$|  $$$$$$\|  $$$$$$$      |  $$$$$$\ \$$\/  $$ \$$$$$$  |  $$$$$$\|  $$$$$$\| $$$$$$$\|  $$$$$$\|  $$$$$$$
//	   | $$$$$$$ | $$    $$| $$  | $$| $$| $$  | $$| $$  | $$ \$$    \       | $$    $$  >$$  $$   | $$ __ | $$    $$| $$   \$$| $$  | $$| $$  | $$ \$$    \ 
//	   | $$      | $$$$$$$$| $$__| $$| $$| $$__| $$| $$__/ $$ _\$$$$$$\      | $$$$$$$$ /  $$$$\   | $$|  \| $$$$$$$$| $$      | $$  | $$| $$__/ $$ _\$$$$$$\
//	   | $$       \$$     \ \$$    $$| $$ \$$    $$ \$$    $$|       $$       \$$     \|  $$ \$$\   \$$  $$ \$$     \| $$      | $$  | $$ \$$    $$|       $$
//	    \$$        \$$$$$$$  \$$$$$$$ \$$  \$$$$$$$  \$$$$$$  \$$$$$$$         \$$$$$$$ \$$   \$$    \$$$$   \$$$$$$$ \$$       \$$   \$$  \$$$$$$  \$$$$$$$ 
//	                                                                                                                                                         
//	                                                                                                                                                         
	                                                                                                                                                         
	   
	   
	   
	 @RequestMapping(value="/fiinika/procurement/pedidos-externos/minhas-propostas-historico", method=RequestMethod.GET)
	   public String MinhasPropostasHistorico(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
         
			
			 model.addAttribute("td", pedidoRepository.veresp(user.getIdEmpresaUser()));
			 model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
			 model.addAttribute("estado", propostaRepository.propostaEstado(user.getIdEmpresaUser()));
			 model.addAttribute("ano", propostaRepository.propostaaNO(user.getIdEmpresaUser()));
			 model.addAttribute("mes", propostaRepository.propostaMes(user.getIdEmpresaUser()));
			 
			 
			return "fiinika/procurement/pedidos-externos/minhas-propostas-historico";
			}else {
				return "redirect:/index";
			}
	    }
	 
	 
	 

 @PostMapping({"/propostas/procurar/"})
			@ResponseBody
			public List<List> ProcurarPropostas(Model model, @Valid Long empresa_id, @Valid String search, @Valid String ano, @Valid String mes, @Valid String estado_proposta, HttpServletRequest request) {
              System.out.println(empresa_id);	 
              System.out.println(estado_proposta);
              System.out.println(ano);
              System.out.println(mes);
              if (empresa_id != 0L && !search.equals("") && !ano.equals("") && !mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodos(empresa_id, search, ano, mes, estado_proposta);
			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && mes.equals("") && estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaDescricao(empresa_id, search);
			    } else if (empresa_id != 0L && !search.equals("") && !ano.equals("") && mes.equals("") && estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaDescricaoAno(empresa_id, search, ano);
			    } else if (empresa_id != 0L && !search.equals("") && !ano.equals("") && !mes.equals("") && estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaDescricaoAnoMes(empresa_id, search, ano, mes);
			    } else if (empresa_id != 0L && search.equals("") && ano.equals("") && mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaEstado(empresa_id, estado_proposta);
			    } else if (empresa_id != 0L && search.equals("") && ano.equals("") && !mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaMesEstado(empresa_id, mes, estado_proposta);
			    } else if (empresa_id != 0L && search.equals("") && !ano.equals("") && !mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaAnoMesEstado(empresa_id, ano, mes, estado_proposta);
			    } else if (empresa_id != 0L && search.equals("") && ano.equals("") && !mes.equals("") && estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaMes(empresa_id, mes);
			    } else if (empresa_id != 0L && search.equals("") && !ano.equals("") && !mes.equals("") && estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaAnoMes(empresa_id, ano, mes);
			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && !mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaDescricaoMesEstado(empresa_id, search, mes, estado_proposta);
			    } else if (empresa_id != 0L && search.equals("") && !ano.equals("") && mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaAnoEstado(empresa_id, ano, estado_proposta);
			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && mes.equals("") && !estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaDescricaoEstado(empresa_id, search, estado_proposta);
			    } else if (empresa_id != 0L && !search.equals("") && ano.equals("") && !mes.equals("") && estado_proposta.equals("")) {
			        return this.pedidoRepository.procuratodosEmpresaDescricaoMes(empresa_id, search, mes);
			    } else {
			        return empresa_id != 0L && search.equals("") && !ano.equals("") && mes.equals("") && estado_proposta.equals("") ? this.pedidoRepository.procuratodosEmpresaAno(empresa_id, ano) : this.pedidoRepository.veresp(empresa_id);
			    }
			}

	 
	 @RequestMapping(value="/fiinika/procurement/pedidos-externos/minha-proposta-resultado/{idpedido}/{idproposta}", method=RequestMethod.GET)
	   public String minhapropostaresultad(@PathVariable("idpedido")String idpedido,@PathVariable("idproposta")Long idproposta,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
       
			  Long decod =anexo.Decodifica(idpedido);
			  model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
			  System.out.println(decod);
			  model.addAttribute("sectores", pedidoRepository.sectoresdopedido(decod)  );
			 // model.addAttribute("est", propostaRepository.estadoDaPorposta(decod,idproposta));
			  model.addAttribute("artigos", propostaRepository.propostafeitas(decod,user.getIdEmpresaUser())  );
			  model.addAttribute("vd", pedidoRepository.pedidoresultado(decod,idproposta)  );
		
			return "fiinika/procurement/pedidos-externos/minha-proposta-resultado";
			}else {
				return "redirect:/index";
			}
	    }
		 
	 @RequestMapping("/fiinika/procurement/pedidos-externos/concursos-abertos")
	   public String concursosAbertos(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			  
			 model.addAttribute("concursos", pedidoRepository.concursosabertos(user.getIdEmpresaUser())  );
			 model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
		
			return "fiinika/procurement/pedidos-externos/concursos-abertos";
			}else {
				return "redirect:/index";
			}
	    }
	 
	 @RequestMapping("/fiinika/procurement/pedidos-externos/fazer-proposta/{idconcurso}")
	   public String fazerProposta(@PathVariable("idconcurso")String idconcurso,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			
			Long decod =anexo.Decodifica(idconcurso);
			 // model.addAttribute("td", pedidoRepository.findById(decod).orElse(null)  );
			 model.addAttribute("td", pedidoRepository.pedidoesp(decod));
			  model.addAttribute("sectores", pedidoRepository.sectoresdopedido(decod)  );
			  model.addAttribute("categorias", categoriaRepository.findAllCategoria(user.getIdEmpresaUser())  );
			  model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
		
			return "fiinika/procurement/pedidos-externos/fazer-proposta";
			}else {
				return "redirect:/index";
			}
	    }
	 
	 
	 @PostMapping("/proposta/{idconcurso}")
	   public ResponseEntity<String> porposta(Model model, @Valid Proposta proposta,
			   @Valid DescProposta descProposta,@PathVariable("idconcurso")String idconcurso,
			   @RequestParam("id_categoria") Long[] id_categoria,// @RequestParam("empresa_prop_id") Long[] empresa_prop_id,
			   @RequestParam("id_produto") Long[] id_produto,//@RequestParam("concurso_id") Long[] concurso_id,
			   @RequestParam("preco") String[] preco,@RequestParam("taxa_prod") String[] taxa_prod,
			   //@RequestParam("qtd") String[] qtd,@RequestParam("subtotal") String[] subtotal,
			   @RequestParam("ref_cat") String[] ref_cat,@RequestParam("tipo_prod") String[] tipo_prod,
			   //@RequestParam (name="sector")Long[] sectores,
			   HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException, ResendException {
	      	
		   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				
				 	Long decod= anexo.Decodifica(idconcurso); 
				 	
				 	if(propostaRepository.jafez(decod,user.getIdEmpresaUser())>= 1) {
				 		  return ResponseEntity.ok().body("jafez");
				 	}else {
				 	
				 	proposta.setIdconcu(decod);
				 	proposta.setDataProposta(new Date());
				 	proposta.setEstadoProposta("Pendente");
				 	
				 	
				 	propostaRepository.save(proposta);
				 	//PedidoSector ps = new PedidoSector();
				 	
				 	
				 	for(int i=0; i<ref_cat.length; i++) {
				 		descProposta.setId_categoria(id_categoria[i]);
				 		descProposta.setId_produto(id_produto[i]);
				 		descProposta.setConcurso_id(decod);
				 		descProposta.setEmpresa_prop_id(proposta.getEmpresaPropostaId());
				 		descProposta.setPreco(preco[i]);
				 		//descProposta.setQtd(qtd[i]);
				 		descProposta.setRef_cat(ref_cat[i]);
				 		//descProposta.setSubtotal(subtotal[i]);
				 		descProposta.setTaxa_prod(taxa_prod[i]);
				 		descProposta.setTipo_prod(tipo_prod[i]);
				 		descProposta.setEstadodp("Pendente");		
						descPropostaRepository.save(descProposta);
						descProposta=new DescProposta();
						//System.out.println(empresas[i]);
					}
				 	
				 	pedidoRepository.actualizaEstado(decod);
				 	
				 	String emailDaeEmpresaQuePediu= empresaRepository.email(decod) ;
				 	
				 	 if(emailDaeEmpresaQuePediu.equals("")   || emailDaeEmpresaQuePediu==null) {
				 		 
				 	 }else {
				 	
				 	 /// String base64EncodedPdf = encodeFileToBase64(pdfFile);
	                    
	                    String htmlMessage = "<html>" +
	                            "<body style='font-family: Arial, sans-serif;'>" +
	                            "<div style='background-color: #f9f9f9; padding: 20px;'>" +
	                            "<h2 style='color: #1E579C;'>Saudações prezados </h2>" +
	                            //"<p>Estamos felizes em informar que o seu salário referente a <strong>" + currentDate + "</strong> foi processado com sucesso.</p>" +
	                            "<p> Recebeu uma nova proposta referente ao pedido" +""+" ,por favor visite a plataforma  </p>" +
	                            "<p>Agradecemos pelo seu esforço e dedicação à empresa. Valorizamos muito o seu trabalho e contribuição.</p>" +
	                            "<p>Atenciosamente,</p>" +
	                            "<p><strong>Equipe Fiinika</strong></p>" +
	                            "<hr style='border: 1px solid #ddd;'/>" +
	                            "<p style='font-size: 12px; color: #888;'>Esta é uma mensagem automática. Por favor, não responda.</p>" +
	                            "</div>" +
	                            "</body>" +
	                            "</html>";
	                    
	                    String Empresa = empresaRepository.NomeEmpresa(user.getIdEmpresaUser());
	                    // Send the email with the PDF attachment
	                    Resend resend = new Resend("re_PdWcAqGc_LNykrApEYiKwwt6wY8Du7jAP");
	                    SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
	                            .from("Fiinika <geral@fiinika-comercial.com>")
	                            .to(emailDaeEmpresaQuePediu)
	                            .subject("Nova Prosposta: " + Empresa)
	                            .html(htmlMessage)
	                            //.addAttachment(Attachment.builder()
	                              //      .fileName(currentDate + " " + safeEmployeeName + ".pdf")
	                                //    .content(base64EncodedPdf)
	                                  //  .build()
	                           // )
	                            .build();
	                    
	                    
	                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	                    
	                    Runnable tarefa = () -> {
	                        try {
	                            // Aqui vai a lógica de envio de email com a função resend
	                            SendEmailResponse data = resend.emails().send(sendEmailRequest);
	                            System.out.println("Email enviado com sucesso: " + data);
	                        } catch (Exception e) {
	                            System.out.println("Falha ao enviar email: " + e.getMessage());
	                        }
	                    };

	                    // Agendar para executar após 5 segundos
	                    scheduler.schedule(tarefa, 5, TimeUnit.SECONDS);

	                    // Encerrar o scheduler após execução
	                    scheduler.shutdown();
	            
				 	 }
				 	
				
				 	
			         return ResponseEntity.ok().body("ok");}
		   }else {
			   return ResponseEntity.ok().body("logout");
		   }
	     
	        
	   }
	 
	 
	 




	 
//	 @GetMapping("/ajax/produtos")
//	 @ResponseBody
//     public List<Produto> findAllProdutos(@RequestParam(value = "id_categoria",required = true) Long id_categoria, HttpSession session) {
//	 List produto = this.produtoRepository.findProductoforCategoria2(id_categoria);
//	   return produto;
//		   }
//	 
//	 @GetMapping("/ajax/produtos_data")
//	 @ResponseBody
//	 public Optional<Produto> findDataProduto(@RequestParam(value = "id_produto",required = true) Long id_produto, HttpSession session) {
//		     Optional<Produto> produto = this.produtoRepository.findById(id_produto);
//		      return produto;
//		   }
	 
	 
	
	   
	   
	   
	@SuppressWarnings("static-access")
	@GetMapping( "/fiinika/procurement/pedidos-externos/entregar-main/{idpedido}")
	public String entegarMain(@PathVariable("idpedido") String idPedido,Model model, HttpServletRequest request,HttpServletResponse response,HttpSession session) throws ParseException, IOException {
		   Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			Long decod = anexo.Decodifica(idPedido);
			
			model.addAttribute("info", empresaRepository.dadosEntrega(decod)  );				
	 
			return "fiinika/procurement/pedidos-externos/entregar-main";
			}else {
				return "redirect:/index";
			}
		}
	
	
	 @RequestMapping("/fiinika/procurement/pedidos-externos/facturacao")
	   public String Facturacao(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);
			  
			 model.addAttribute("facturacao", pagamentoRepository.Facturacaoesp(user.getIdEmpresaUser())  );
			 model.addAttribute("idEmpresa", user.getIdEmpresaUser() );
		
			return "/fiinika/procurement/pedidos-externos/facturacao";
			}else {
				return "redirect:/index";
			}
	    }
	

	   
	  private static final String API_KEY = "a95teci62t7v15d63s3om2recu4fp5v3";
	    private static final String HMAC_ALGO = "HmacSHA256";
	 
	 //@GetMapping("/s")
	 public void mainA() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
	        String url = "http://localhost:8080/procurement/update_status";
	       // String url = "https://biuryshine.musicash.co/payment/musicash/update_status";
	        String payload = "{\"amount\":\"301729.26\",\"custom_fields\":{\"callback_url\":\"http://localhost:8080/procurement/update_status\",\"telemovel\":\"920120120\",\"name\":\"ABC\"},\"datetime\":\"2024-07-03T13:16:25Z\",\"entity_id\":1068,\"fee\":\"10000.00\",\"id\":783907683652,\"parameter_id\":null,\"period_end_datetime\":\"2024-06-12T10:00:00Z\",\"period_id\":7839,\"period_start_datetime\":\"2024-07-03T10:00:00Z\",\"product_id\":1,\"reference_id\":163340260,\"terminal_id\":\"0000000000\",\"terminal_location\":\"Internet\",\"terminal_period_id\":0,\"terminal_transaction_id\":0,\"terminal_type\":\"MB\",\"transaction_id\":2}";
	        // Create HMAC signature
	        Mac sha256_HMAC = Mac.getInstance(HMAC_ALGO);
	        SecretKeySpec secret_key = new SecretKeySpec(API_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
	        sha256_HMAC.init(secret_key);
	        
	        String signature = Hex.encodeHexString(sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8)));

	        OkHttpClient client = new OkHttpClient();
	        
	        MediaType mediaType = MediaType.parse("application/json");
	        okhttp3.RequestBody body = okhttp3.RequestBody.create(payload, mediaType);
	
	        Request request = new Request.Builder()
	                .url(url)
	                .addHeader("Content-Type", "application/json")
	                .addHeader("X-Signature", signature)
	                .post(body)
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	           // if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

	            System.out.println("Response Status Code: " + response.code());
	            System.out.println("Response Body: " + response.body().string());
	        }
	    }
	 
	 private String getBody(HttpServletRequest request) throws IOException {
	        StringBuilder buffer = new StringBuilder();
	        BufferedReader reader = request.getReader();
	        String line;
	        while ((line = reader.readLine()) != null) {
	            buffer.append(line);
	        }
	        return buffer.toString();
	    }
	 
		 
	 @SuppressWarnings("deprecation")
	@PostMapping("/procurement/update_status")
  public ResponseEntity<String> confirmarpagamento(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
      try {
      String payload = getBody(request);
      String headerSignature = request.getHeader("X-Signature");
   // System.out.println("payload "+payload);    
      String body = getBody(request);
      Mac sha256_HMAC = Mac.getInstance(HMAC_ALGO);
      SecretKeySpec secret_key = new SecretKeySpec(API_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
      sha256_HMAC.init(secret_key);

      String signature = Hex.encodeHexString(sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8)));    
   // System.out.println(signature+ " Received Signature: " + headerSignature);
      
      if (signature.equals(headerSignature)) {
          ObjectMapper mapper = new ObjectMapper();
          mapper.enable(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS);

          Map<String, Object> payment = mapper.readValue(payload, Map.class);
          System.out.println(payment);
          // handle payment event
          String referencia = payment.get("reference_id").toString();
      
          Double valor = Double.parseDouble(payment.get("amount").toString());
          String idEmp = payment.get("transaction_id").toString();
          String data_pagamento = payment.get("datetime").toString();
          //String id = payment.get("id").toString();
          
          JSONObject jsonObject = new JSONObject(payment);
          JSONObject custom_fields = jsonObject.getJSONObject("custom_fields");

         // String email = custom_fields.get("email").toString();
          //String token = custom_fields.get("token").toString();
          
          System.out.println(referencia);//+" "+email+" "+token+" "+id);
        //  String hora_pagamento="",data_pagamento_;
       
        try {    
           
//           data_pagamento_ = data_pagamento.substring(0,10);    
//           hora_pagamento = data_pagamento.substring(11);
           Empresa empresaUser = R.empresaRepository.findById(Long.parseLong(idEmp)).orElse(null);
           
           int length = referencia.length(); // Capturando o comprimento da string

           switch (length) {
               case 8:
                   referencia = "0" + referencia;
                   break;
               case 7:
                   referencia = "00" + referencia;
                   break;
               case 6:
                   referencia = "000" + referencia;
                   break;
               case 5:
                   referencia = "0000" + referencia;
                   break;
               default:
                   // Não faz nada se o comprimento for diferente
                   break;
           }
           System.out.println(idEmp+"iddopedido: "+empresaUser);
           if(empresaUser != null) {
        	   pagamentoRepository.actuapag(referencia);
      	     String arr= pagamentoRepository.arra(referencia); 
      	     System.out.println(arr);
      	     Long id_proposta = pagamentoRepository.proposta(referencia);
      	     Long id_pedido = pagamentoRepository.pedidoId(referencia);
      	     
      	     propostaRepository.propo(id_proposta);
      	     
      	     pedidoRepository.actualizaPago(id_pedido);
      	     
      	     List<Integer> list = Arrays.stream(arr.substring(1, arr.length() - 1).split(","))
                    .map(String::trim)        
                    .map(Integer::parseInt)    
                    .collect(Collectors.toList()); 
        
      	     for (Integer  tr :list) {
      	           
      	            
      	            descPropostaRepository.actualizadp(tr);
      	            
      	           
      	            System.out.println("iddopedido: "+tr);
      	        }
                           
           }
                       
           } catch (Exception e) {            
               System.out.println(e);//151119040
           }
           System.out.println(referencia);
          return ResponseEntity.ok("");
      }else {
           System.out.println("Falha");
          return ResponseEntity.status(400).body("");
      }

} catch (Exception e) {
// TODO: handle exception
System.out.println("Falha "+e);
return ResponseEntity.status(400).body("");
}
  }
}
