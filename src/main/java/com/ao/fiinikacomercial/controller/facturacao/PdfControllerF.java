package com.ao.fiinikacomercial.controller.facturacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.*;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.PasswordResetService;
import com.ao.fiinikacomercial.service.Repositorio;


@Controller
public class PdfControllerF {
	
	
    private SpringTemplateEngine templateEngine;
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;  
	
    String local_current = "";
	static List tipo = Arrays.asList("invoice", "receipt", "credit-note");		



      public PdfControllerF(SpringTemplateEngine templateEngine) {
    	   this.templateEngine = templateEngine;
      }
      
	
	// Para as Facturas
	  @GetMapping("/fiinika/documentos/pdf/{emp}/{code}")	
	  public void displayPdfFactura(Model model,HttpServletResponse response,HttpServletRequest request, HttpSession session,  @PathVariable("emp") String nome_emp, @PathVariable("code") String code) throws IOException  {
		
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);
		    
		  long id_emp = objFunc.Decodifica(code);		
		  String [] server = request.getRequestURL().toString().split("/");
		  local_current=server[0]+"//"+server[1]+server[2];
		System.out.print(id_emp);
		  
	      try {
	    	// inline abre no browser e attachment faz download
	    	  Path file = Paths.get(generateFactura(id_emp).getAbsolutePath());
	    	 // Path file = Paths.get(generateFacturaTermica(id_emp).getAbsolutePath());	    	
	    	  String fileName = file.getFileName().toString().split("<separa>")[0];
	          if (Files.exists(file)) {
	              response.setContentType("application/pdf");
	              response.addHeader("Content-Disposition",
	                      "inline; filename=" + fileName);
	              Files.copy(file, response.getOutputStream());
	              response.getOutputStream().flush();
	          }
	        
	       } catch (Exception e) {
	           
	           System.out.println("ERRO:: "+e.getMessage());
	       }
		}else {
			response.sendRedirect("/index");
		}
	     
	  }
	  

	  
	  @GetMapping("/fiinika/documentos/pdf/recibo/{code}")	
	  public void displayPdfRecibo(Model model,HttpServletResponse response,HttpServletRequest request, HttpSession session, @PathVariable("code") String code) throws IOException  {
		
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);
		    
		  long id_emp = objFunc.Decodifica(code);
		  String [] server = request.getRequestURL().toString().split("/");
		  local_current=server[0]+"//"+server[1]+server[2];
		  
	      try {
	    	// inline abre no browser e attachment faz download
	    	  Path file = Paths.get(generateRecibo(id_emp).getAbsolutePath());
	    	 // Path file = Paths.get(generateFacturaTermica(id_emp).getAbsolutePath());
	    	  String fileName = file.getFileName().toString().split("<separa>")[0];
	          if (Files.exists(file)) {
	              response.setContentType("application/pdf");
	              response.addHeader("Content-Disposition",
	                      "inline; filename=" + fileName);
	              Files.copy(file, response.getOutputStream());
	              response.getOutputStream().flush();
	          }
	        
	       } catch (Exception e) {
	           
	           System.out.println("ERRO:: "+e.getMessage());
	       }
		}else {
			response.sendRedirect("/index");
		}
	     
	  }
	  
	  
	  @GetMapping("/fiinika/documentos/pdf/nota-credito/{code}")	
	  public void displayPdfNota(Model model,HttpServletResponse response,HttpServletRequest request, HttpSession session, @PathVariable("code") String code) throws IOException  {
		
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);
		    
		  long id_emp = objFunc.Decodifica(code);
		  String [] server = request.getRequestURL().toString().split("/");
		  local_current=server[0]+"//"+server[1]+server[2];
		  
	      try {
	    	// inline abre no browser e attachment faz download
	    	  Path file = Paths.get(generateNotaCredito(id_emp).getAbsolutePath());
	    	 // Path file = Paths.get(generateFacturaTermica(id_emp).getAbsolutePath());
	    	  String fileName = file.getFileName().toString().split("<separa>")[0];
	          if (Files.exists(file)) {
	              response.setContentType("application/pdf");
	              response.addHeader("Content-Disposition",
	                      "inline; filename=" + fileName);
	              Files.copy(file, response.getOutputStream());
	              response.getOutputStream().flush();
	          }
	        
	       } catch (Exception e) {
	           
	           System.out.println("ERRO:: "+e.getMessage());
	       }
		}else {
			response.sendRedirect("/index");
		}
	     
	  }
	  
	
	
	  
	// Para as Facturas
	  public File generateFactura(long id_factura) throws Exception {
		  
		 // Empresa empresa = R.empresaRepository.findById(id_empresa).orElse(null);	
		  Factura factura = R.facturaRepository.findById(id_factura).orElse(null);	
		   List  valor_imposto = new ArrayList<>();
	       Context context = new Context();
	       double total_inc_ret = 0;
	       Cliente cliente = R.clienteRepository.findById(factura.getId_clientefk()).orElse(null);
	       
	       context.setVariable("emp",R.empresaRepository.findById(factura.getId_empresafk()).orElse(null));
	       context.setVariable("fact",factura);
	       context.setVariable("cl",cliente);
	       Iterable <Item> items = R.itemRepository.findItem(factura.getId_factura());
	       context.setVariable("itens",items);
	       context.setVariable("coord",R.contaRepository.findById(factura.getId_contafk()).orElse(null));
	       context.setVariable("obs_txt",0);
	       context.setVariable("server",local_current);
	      
		      for(Subscricao sub :R.subscricaoRepository.findSubscricaDexa(factura.getId_empresafk())) {
		    	  context.setVariable("obs_txt",1);		    	
		      }
		      
		      
		      Map<String, List<Item>> gruposItens = new HashMap<>();
		        gruposItens.put("14.00", new ArrayList<>());
		        gruposItens.put("7.00", new ArrayList<>());
		        gruposItens.put("5.00", new ArrayList<>());
		        gruposItens.put("0.00", new ArrayList<>());
		        
		        // Mapa para armazenar as somas de preço e imposto
		        Map<String, Map<String, Double>> somas = new HashMap<>();
		        somas.put("14.00", new HashMap<>());
		        somas.get("14.00").put("preco", 0.0);
		        somas.get("14.00").put("imposto", 0.0);

		        somas.put("7.00", new HashMap<>());
		        somas.get("7.00").put("preco", 0.0);
		        somas.get("7.00").put("imposto", 0.0);

		        somas.put("5.00", new HashMap<>());
		        somas.get("5.00").put("preco", 0.0);
		        somas.get("5.00").put("imposto", 0.0);

		        somas.put("0.00", new HashMap<>());
		        somas.get("0.00").put("preco", 0.0);
		        somas.get("0.00").put("imposto", 0.0);
		        
		        for (Item item : items) {
		            // Formata a taxa para duas casas decimais			        	
		            String taxa = item.getTaxa_prod_item();
		            if(taxa.equals("00.00")) {
		        		taxa = "0.00";
		        	}
		          
		            // Adiciona o item ao grupo correspondente
		            gruposItens.get(taxa).add(item);

		            // Calcula o preço total e o imposto para a taxa
		            double precoTotal = Double.parseDouble(item.getPreco()) * item.getQtd();
		            double imposto = (Double.parseDouble(item.getTaxa_prod_item()) / 100) * precoTotal;

		            // Atualiza as somas
		            Map<String, Double> somaTaxa = somas.get(taxa);
		         
		            somaTaxa.put("preco", somaTaxa.get("preco") + precoTotal);
		            somaTaxa.put("imposto", somaTaxa.get("imposto") + imposto);
		         
		            valor_imposto.add((Double.parseDouble(item.getTaxa_prod_item())/100)*Double.parseDouble(item.getPreco()));
		    	    if(item.getTipo_item().equals("Serviço 6.5")) {
		    		   total_inc_ret = total_inc_ret + (Double.parseDouble(item.getPreco()))*item.getQtd();
		    	    }
		        }
	       
	      
	       
	       context.setVariable("v_imp",valor_imposto);
	       context.setVariable("total_inc_ret",total_inc_ret);
	       context.setVariable("somas",somas);
	       context.setVariable("hash_msg",objFunc.extrairCaracteres(factura.getHash_msg()));
	       
	       String html = templateEngine.process("fiinika/documentos/pdf/factura", context);
	       return renderPdf(html, cliente.getNomeCliente()+" "+factura.getCodigo_factura());
	   }

		// Para os Recibos
	  public File generateRecibo(long id_recibo) throws Exception {
		 
		  
		  Recibo recibo = R.reciboRepository.findById(id_recibo).orElse(null);	
		  Factura factura = R.facturaRepository.findById(recibo.getId_facturafk()).orElse(null);	
		   List  valor_imposto = new ArrayList<>();
	       Context context = new Context();
	       double total_inc_ret = 0;
	       Cliente cliente = R.clienteRepository.findById(factura.getId_clientefk()).orElse(null);
	     
	       context.setVariable("rec",recibo);
	       context.setVariable("emp",R.empresaRepository.findById(recibo.getId_empresa_fkR()).orElse(null));
	       context.setVariable("fact",factura);
	       context.setVariable("cl",cliente);
	       Iterable <Item> items = R.itemRepository.findItem(factura.getId_factura());
	       context.setVariable("itens",items);
	       context.setVariable("coord",R.contaRepository.findById(factura.getId_contafk()).orElse(null));
	       context.setVariable("obs_txt",0);
	       context.setVariable("server",local_current);
	      
		      for(Subscricao sub :R.subscricaoRepository.findSubscricaDexa(factura.getId_empresafk())) {
		    	  context.setVariable("obs_txt",1);
		    	 
		      }
	       
	       for(Item it: items) {
	    	   valor_imposto.add((Double.parseDouble(it.getTaxa_prod_item())/100)*Double.parseDouble(it.getPreco()));
	    	   if(it.getTipo_item().equals("Serviço 6.5")) {
	    		   total_inc_ret = total_inc_ret + (Double.parseDouble(it.getPreco())*it.getQtd());
	    	   }
	    	  
	       }
	       
	       context.setVariable("v_imp",valor_imposto);
	       context.setVariable("total_inc_ret",total_inc_ret);
	       
	       String html = templateEngine.process("fiinika/documentos/pdf/recibo", context);
	       return renderPdfRecibo(html, cliente.getNomeCliente()+" "+recibo.getCodigo_recibo());
	   }
	  
	  
		// Para as Notas de credito
	  public File generateNotaCredito(long id_nota) throws Exception {
		 
		  
		  // Empresa empresa = R.empresaRepository.findById(id_empresa).orElse(null);	
		   Factura factura = R.facturaRepository.findById(id_nota).orElse(null);	
		   List  valor_imposto = new ArrayList<>();
	       Context context = new Context();
	       double total_inc_ret = 0;
	       Cliente cliente = R.clienteRepository.findById(factura.getId_clientefk()).orElse(null);
	       context.setVariable("nota",R.nota_creditoRepository.findNotacreditoId(id_nota).orElse(null));
	       context.setVariable("emp",R.empresaRepository.findById(factura.getId_empresafk()).orElse(null));
	       context.setVariable("fact",factura);
	       context.setVariable("cl",cliente);
	       Iterable <ItemNotas> items = R.itemNotaRepository.findItemFactura(factura.getId_empresafk(),factura.getId_factura());
	       context.setVariable("itens",items);
	       context.setVariable("coord",R.contaRepository.findById(factura.getId_contafk()).orElse(null));
	       context.setVariable("obs_txt",0);
	       context.setVariable("server",local_current);
	      
		      for(Subscricao sub :R.subscricaoRepository.findSubscricaDexa(factura.getId_empresafk())) {
		    	  context.setVariable("obs_txt",1);
		    	 
		      }
		      
		      Map<String, List<ItemNotas>> gruposItens = new HashMap<>();
		        gruposItens.put("14.00", new ArrayList<>());
		        gruposItens.put("7.00", new ArrayList<>());
		        gruposItens.put("5.00", new ArrayList<>());
		        gruposItens.put("0.00", new ArrayList<>());
		        
		        // Mapa para armazenar as somas de preço e imposto
		        Map<String, Map<String, Double>> somas = new HashMap<>();
		        somas.put("14.00", new HashMap<>());
		        somas.get("14.00").put("preco", 0.0);
		        somas.get("14.00").put("imposto", 0.0);

		        somas.put("7.00", new HashMap<>());
		        somas.get("7.00").put("preco", 0.0);
		        somas.get("7.00").put("imposto", 0.0);

		        somas.put("5.00", new HashMap<>());
		        somas.get("5.00").put("preco", 0.0);
		        somas.get("5.00").put("imposto", 0.0);

		        somas.put("0.00", new HashMap<>());
		        somas.get("0.00").put("preco", 0.0);
		        somas.get("0.00").put("imposto", 0.0);
		        
		        for (ItemNotas item : items) {
		            // Formata a taxa para duas casas decimais			        	
		            String taxa = item.getTaxa_prod_nota();
		            if(taxa.equals("00.00")) {
		        		taxa = "0.00";
		        	}
		          
		            // Adiciona o item ao grupo correspondente
		            gruposItens.get(taxa).add(item);

		            // Calcula o preço total e o imposto para a taxa
		            double precoTotal = item.getPreco() * item.getQtd();
		            double imposto = (Double.parseDouble(item.getTaxa_prod_nota()) / 100) * precoTotal;

		            // Atualiza as somas
		            Map<String, Double> somaTaxa = somas.get(taxa);
		         
		            somaTaxa.put("preco", somaTaxa.get("preco") + precoTotal);
		            somaTaxa.put("imposto", somaTaxa.get("imposto") + imposto);
		         
		            valor_imposto.add((Double.parseDouble(item.getTaxa_prod_nota())/100)*item.getPreco());
		    	    if(item.getTipo_item_nota().equals("Serviço 6.5")) {
		    		   total_inc_ret = total_inc_ret + (item.getPreco())*item.getQtd();
		    	    }
		        }
	       
	      
	       
	       context.setVariable("v_imp",valor_imposto);
	       context.setVariable("total_inc_ret",total_inc_ret);
	       context.setVariable("somas",somas);
	       context.setVariable("hash_msg",objFunc.extrairCaracteres(factura.getHash_msg()));
	       
	       String html = templateEngine.process("fiinika/documentos/pdf/nota-de-credito", context);
	       return renderPdfRecibo(html,cliente.getNomeCliente()+" "+factura.getCodigo_factura());
	   }
	

	  
	  
//============================= API ================================
		// Para as Facturas
			  @GetMapping("/documentos/pdf-api/{emp}/{code}")	
			  public void displayPdfFacturaAPI(HttpServletResponse response,HttpServletRequest request, HttpSession session,  @PathVariable("emp") String nome_emp, @PathVariable("code") String code) throws IOException  {
								  
				  long id_emp = 0;
				try {		
					// se naõ conter os tipos de docuemntos requeridos
					if(!tipo.contains(nome_emp)) {
						 response.sendRedirect("/404");
					}
						
					 id_emp = objFunc.Decodifica(code);	
					if(nome_emp.equals("invoice") || nome_emp.equals("credit-note")) {
					 if(R.facturaRepository.findCodeFactura(code).toString().equals("[]")) {					
						 response.sendRedirect("/404");
					 }
				  }
				  if(nome_emp.equals("nota-credito")) {
					 if(R.reciboRepository.findBhyCodeRecibo(code).toString().equals("[]")) {					
						 response.sendRedirect("/404");
					 }
				  }
				} catch (Exception e) {
					// TODO: handle exception
					response.sendRedirect("/404");
				}
					
				
				 
				  String [] server = request.getRequestURL().toString().split("/");
				  local_current=server[0]+"//"+server[1]+server[2];
				 // R.ver("Current"+local_current);
				  Path file = null;
			      try {
			    	// inline abre no browser e attachment faz download
			    	  if(nome_emp.equals("invoice")) {
			    		  file = Paths.get(generateFactura(id_emp).getAbsolutePath());
			    	  }
			    	  if(nome_emp.equals("receipt")) {
			    		  file = Paths.get(generateRecibo(id_emp).getAbsolutePath());
			    	  }
			    	  if(nome_emp.equals("credit-note")) {
			    		  file = Paths.get(generateNotaCredito(id_emp).getAbsolutePath());
			    	  }
			    	  
			    	  
			    	   
			    	 // Path file = Paths.get(generateFacturaTermica(id_emp).getAbsolutePath());	    	
			    	  String fileName = file.getFileName().toString().split("<separa>")[0];
			          if (Files.exists(file)) {
			              response.setContentType("application/pdf");
			              response.addHeader("Content-Disposition",
			                      "inline; filename=" + fileName);
			              Files.copy(file, response.getOutputStream());
			              response.getOutputStream().flush();
			          }
			        
			       } catch (Exception e) {
			           
			           System.out.println("ERRO:: "+e.getMessage());
			       }
				
			     
	}  
			  
			  
// ============================== RENDIRIZA O PDF =================================================	 
	   private File renderPdf(String html, String nomeFile) throws IOException, DocumentException {
		   nomeFile = nomeFile.replace("/", "_") + "<separa>";
	       File file = File.createTempFile(nomeFile,".pdf");	  
	 
	       OutputStream outputStream = new FileOutputStream(file);
	       ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
	       
	       renderer.setDocumentFromString(html);
	       renderer.layout();	
	       renderer.createPDF(outputStream,false);	
	       html = html.replace("Original","Duplicado");
	       for (int i = 1; i < 2; i++) {
               renderer.setDocumentFromString(html);
               renderer.layout();
               //renderer.writeNextDocument();
               renderer.writeNextDocument(i);
           }
	       
	     
	       renderer.finishPDF(); 
	       outputStream.close();
	       file.deleteOnExit();
	       return file;
	   }
	   
	   private File renderPdfRecibo(String html, String nomeFile) throws IOException, DocumentException {
		   nomeFile = nomeFile.replace("/", "_") + "<separa>";
	       File file = File.createTempFile(nomeFile, ".pdf");
	       OutputStream outputStream = new FileOutputStream(file);
	       ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
	       
	       renderer.setDocumentFromString(html);
	       renderer.layout();
	       renderer.createPDF(outputStream);
	       outputStream.close();
	       file.deleteOnExit();
	       return file;
	   }
	   
	   
	   
	   
	   

}
