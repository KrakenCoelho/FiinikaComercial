package com.ao.fiinikacomercial.controller.facturacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import okhttp3.*;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.service.Repositorio;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ao.fiinikacomercial.service.ProxyPay;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.Pacote;
import com.ao.fiinikacomercial.model.facturacao.Subscricao;
import com.ao.fiinikacomercial.model.facturacao.Usuario;

import org.apache.commons.codec.binary.Hex;
import org.json.*;

@Controller
public class SubscricaoController {
		
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;
	@Autowired
	ProxyPay proxpay;
	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
		
		
		
		   public String TodosPagamentos() throws IOException {
		
			 String json_respons = "";			
				 
				try {
				
					
					OkHttpClient client = new OkHttpClient();

					Request request = new Request.Builder()
					  .url("https://api.proxypay.co.ao/payments")
					  .get()
					  .addHeader("Authorization", "Token " + "a95teci62t7v15d63s3om2recu4fp5v3")
					  .addHeader("Accept", "application/vnd.proxypay.v2+json")
					  .build();

					   Response response = client.newCall(request).execute();
					   json_respons = response.body().string();
					   
					  // System.out.println(response.body().toString()+"valido "+json_respons);
					   response.body().close();
					   System.out.println(json_respons);
					   return json_respons;					
					
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			 
			  return "fiinika/pagamentos";
			
	}
	 


	//-----------------------------------------------------------------------	 
		// View Despesas
		 @RequestMapping("/fiinika/configuracoes/escolher-pacote")
		   public String EscolherPacote(Model model, HttpSession session,HttpServletResponse response) throws ParseException, IOException, InvalidKeyException, NoSuchAlgorithmException{
			 
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				// System.out.print(objSessao);
				   if(objSessao!=null) {
					 
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);
			    
			    /*			   
			    Descarta("764506822831");
			    Descarta("764507000907");
			  
			    Descarta("740203720499");
			    Descarta("740108331255");
			    Descarta("740108517940");
			    Descarta("740200633257");
			   */
			    
			 //  Descarta("765408325062");
			 //  TodosPagamentos();
			    
			    model.addAttribute("titulo","Escolher Pacote");
			    model.addAttribute("pacotes",R.pacoteRepository.findAll());
			   
			  //  R.Role(model,session,response);
				return "fiinika/configuracoes/escolher-pacote";
				
				}else {
					return "redirect:/index";
				}
		    }
		 		 
		 

		 
		 //  GERA A REFERENCIA
		// ================================================= FAZ SUBSCRIÇÃO	==========================	 
		 @RequestMapping(value="/fiinika/configuracoes/pagamento-page/{id}", method=RequestMethod.GET)
		   public String PagamentoPage(@PathVariable String id,Model model, HttpSession session,HttpServletRequest resquest,HttpServletResponse response) throws JSONException, ParseException, IOException{
			 
			 long id_pacote = objFunc.Decodifica(id);
			 
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
				// System.out.print(objSessao);
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);	
		
						Pacote pacote = R.pacoteRepository.findById(id_pacote).orElse(null);
						model.addAttribute("titulo","Pagamento page");
						model.addAttribute("pacote",pacote);
						Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
					    String referencia = "",dataExpRef="", token="";
				
				
					 Subscricao subsc = R.subscricaoRepository.findSubscricaao("Pendente",user.getIdEmpresaUser(),id_pacote).orElse(null);
					// Se for null gera uma nova referencia
					 if(subsc == null) {
						  System.out.println("Cria nova");
							 float preco = R.pacoteRepository.findPacotePreco(id_pacote);
							 token = objFunc.geraUUID();
							 referencia = objFunc.GeraReferencia().substring(0, objFunc.GeraReferencia().length() - 1);
							 dataExpRef = objFunc.AddDays(null, 2);
							 proxpay.SentProxypay(resquest,empresa.getNomeEmpresa(),token, user.getEmail(),user.getTelemovel(), preco+"", referencia,dataExpRef);							
						
							 Subscricao compra = new Subscricao();
							 compra.setToken(token);
							 compra.setData_hora(objFunc.DataTimeActual());
							 compra.setData_exp_referencia(objFunc.AddDays(null,5));
							 compra.setEstado("Pendente");
							 compra.setId_pacotefk(id_pacote);
							// compra.setId_produtofk(objFunc.Decodifica(id_produto));
							 compra.setIdEmpresafk(user.getIdEmpresaUser());
							 compra.setPreco(preco+"");
							 compra.setReferencia(referencia);
							 compra.setTipo(1);							 
							 R.subscricaoRepository.save(compra);
						
					}else {
						  long d = objFunc.DiferencaDatas(objFunc.DataActual(), subsc.getData_exp_referencia()+"");
						  referencia = subsc.getReferencia(); 
						  System.out.println("Com data ativa");
							// ACTUALIZA UMA NOVA REF NO SISTEMA E CRIA UMA NOVA NA PROXI PAy
							if(d < 0) {
								  System.out.println("Sem data ativa faz update");
								 token = objFunc.geraUUID();
								 referencia = objFunc.GeraReferencia().substring(0, objFunc.GeraReferencia().length() - 1);
								 dataExpRef = objFunc.AddDays(null,2);
								 proxpay.SentProxypay(resquest,empresa.getNomeEmpresa(),token, user.getEmail(),user.getTelemovel(), subsc.getPreco(), referencia,dataExpRef);
								 
								 R.subscricaoRepository.updateReferencia(referencia,token,dataExpRef,subsc.getIdSubscricao());
								
							}
					}	
						
						model.addAttribute("id_pacote_aux", id);						
						model.addAttribute("referencia",referencia);
						model.addAttribute("montante",pacote.getPreco());
						
						//R.Role(model,session,response);
						return "fiinika/configuracoes/pagamento-page";
				}else {
					return "redirect:/index";
				}
		    }
		 
		    private static final String API_KEY = "a95teci62t7v15d63s3om2recu4fp5v3";
		    private static final String HMAC_ALGO = "HmacSHA256";

		    @PostMapping("/payment/fiinikacomercial/update_status")
		    public ResponseEntity<String> handlePayment(HttpServletRequest request) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		    	try {   
		        String payload = getBody(request);
		        String headerSignature = request.getHeader("X-Signature");
		       // System.out.println("payload "+payload);	
		        String body = getBody(request);
		        Mac sha256_HMAC = Mac.getInstance(HMAC_ALGO);
		        SecretKeySpec secret_key = new SecretKeySpec(API_KEY.getBytes(StandardCharsets.UTF_8), HMAC_ALGO);
		        sha256_HMAC.init(secret_key);

		        String signature = Hex.encodeHexString(sha256_HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8)));	
		      //  System.out.println(signature+ " Received Signature: " + headerSignature);
		        
		        if (signature.equals(headerSignature)) {
		            ObjectMapper mapper = new ObjectMapper();
		            Map<String, Object> payment = mapper.readValue(payload, Map.class);

		            // handle payment event
		            String referencia =  payment.get("reference_id").toString();
		            Double valor = Double.parseDouble(payment.get("amount").toString());
		            String idpagamento = payment.get("transaction_id").toString();
		            String data_pagamento =  payment.get("datetime").toString();
		            String id =  payment.get("id").toString();
		            
		            JSONObject jsonObject = new JSONObject(payment);
		            JSONObject custom_fields = jsonObject.getJSONObject("custom_fields");
	  
		            String telemovel = custom_fields.get("telemovel").toString();
		            String token = custom_fields.get("token").toString();
		            
		            System.out.println(referencia+" "+telemovel+" "+token+" "+id);
		            String hora_pagamento="",data_pagamento_,data_exp="";
		   		 
		   		 try {	
		   			 
		   			data_pagamento_ =  data_pagamento.substring(0,10);	
		   			hora_pagamento = data_pagamento.substring(11);
		   			Usuario user = R.userRepository.findByTelemovel(telemovel).orElse(null);
		   			if(user != null) {
		   				Subscricao pag = R.subscricaoRepository.findByIdEmpresafkAndReferenciaAndToken(user.getIdEmpresaUser(), referencia, token).orElse(null);
		   				if(pag != null) {
		   					Pacote pacote = R.pacoteRepository.findById(pag.getId_pacotefk()).orElse(null);
		   					/*    Busca os dias que faltam da o-pais  */
							int dias = 0;									
							List <List> dadosSub = null;
							dadosSub = R.subscricaoRepository.findSubscricaDexa("Paga",user.getIdEmpresaUser());
							for(List sub : dadosSub) {	
								// data_exp - data_pag
								 dias = (int)objFunc.DiferencaDatas(objFunc.DataActual(), sub.get(1).toString());
								 if(dias < 0 ){
										dias = 0;
									}
							 }
							
							
							data_exp = objFunc.AddDays(data_pagamento_,(pacote.getDias()+dias));
							
		   					pag.setData_pagamento(data_pagamento);
		   					pag.setEstado("Paga");
		   					pag.setData_hora(data_pagamento+" "+hora_pagamento);
		   					pag.setData_expiracao_pagamento(data_exp);
		   					pag.setTipo(1);
		   					
		   					
		   				 R.subscricaoRepository.updateEstadoCompraCliente("Paga",data_pagamento_,hora_pagamento,data_exp,1,referencia);
		   					R.subscricaoRepository.save(pag);
		   				}				
		   			}
		   						 
		   			} catch (Exception e) {			
		   				System.out.println(e);//151119040
		   			}
		   		    System.out.println(referencia+" "+telemovel+" "+token+" "+id);
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
		    
		    
		    private String getBody(HttpServletRequest request) throws IOException {
		        StringBuilder buffer = new StringBuilder();
		        BufferedReader reader = request.getReader();
		        String line;
		        while ((line = reader.readLine()) != null) {
		            buffer.append(line);
		        }
		        return buffer.toString();
		    }
			// =============================== PARA O fiinika CONTA  ================================	 
			 @RequestMapping(value = "/api/fiinikaconta/update_status", method = RequestMethod.POST)
			 public  @ResponseBody String RecebePagamentos(HttpServletResponse response,
					 @RequestParam(value = "referencia", required = true) String referencia, 
					 @RequestParam(value = "valor", required = true) String valor,
					 @RequestParam(value = "data_pagamento", required = true) String data_pagamento) throws IOException{
			      
				
				 System.out.println(referencia+" "+valor+" "+data_pagamento);
				 
				 
				 String data_exp = null,ret_id_pacote = null,nome_pacote="",hora_pagamento="",data_pagamento_;
				 try {					
			     int id_pacote = 0; long id_usuario = 0;
			     
					data_pagamento_ =  data_pagamento.substring(0,10);	
					hora_pagamento = data_pagamento.substring(11);
					
					List <List> list = null;
				    list = R.subscricaoRepository.findPacoteSubscricao(referencia);	
					//	System.out.println(referencia[i] + " "+ _compraRepository.findPacoteSubscricao(referencia[i]));	
						if(!list.toString().equals("[]")) {
							for(List L : list) {	
							ret_id_pacote = L.get(0).toString();							
							id_usuario = Long.parseLong(L.get(1).toString());
							
							if(ret_id_pacote!= null) {
								id_pacote = Integer.parseInt(ret_id_pacote);
								nome_pacote = R.pacoteRepository.findPacoteNome(id_pacote);
							   }	
							}
						}
						
						/*    Busca os dias que faltam da o-pais  */
						long dias = 0;									
						List <List> dadosSub = null;
						dadosSub = R.subscricaoRepository.findSubscricaDexa("Paga",id_usuario);
						for(List sub : dadosSub) {	
							// data_exp - data_pag
							 dias = objFunc.DiferencaDatas(objFunc.DataActual(), sub.get(1).toString());
							 if(dias < 0 ){
									dias = 0;
								}
						 }
				/* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  */		
						
						if(nome_pacote.equals("Subscrição 1 mês")) {
							data_exp = objFunc.AddDays(data_pagamento_,30+(int)dias);
						}
						if(nome_pacote.equals("Subscrição 3 meses")) {
							data_exp = objFunc.AddDays(data_pagamento_,90+(int)dias);
						}
						if(nome_pacote.equals("Subscrição 6 meses")) {
							data_exp = objFunc.AddDays(data_pagamento_,182+(int)dias);
						}
						if(nome_pacote.equals("Subscrição 12 meses")) {
							data_exp = objFunc.AddDays(data_pagamento_,365+(int)dias);
						}
					
					   R.subscricaoRepository.updateEstadoCompraCliente("Paga",data_pagamento_,hora_pagamento,data_exp,1,referencia);
					 
					 
				
				   //===================================================================== 
				 
					} catch (Exception e) {
						// TODO: handle exception
						System.out.println(e);//151119040
					}
				        
				     //  _compraRepository.updateEstadoCompraCliente("Paga",referencia);
				        System.out.println(referencia+"oiieee "+valor+" "+data_pagamento);
						return referencia+" "+valor+" "+data_pagamento;
						
				 }
				 

			 
			 
			//===================== verifica se o pagamento fo feito ============================	 
			 @RequestMapping(value = "/ajax/VerificaPagamento", method = RequestMethod.GET)
			 public @ResponseBody int VerifcaPagamento(HttpSession session ,@RequestParam(value = "referencia", required = true) String referencia) throws IOException, ParseException{
				 System.out.println("=="+ referencia);//151119040
				
				 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
					 System.out.print(referencia);
					   if(objSessao!=null) {
							userDetails = (UserDetails) objSessao;
						    String []  dadosUser= userDetails.getUsername().split("_");
						    long id_user = Long.parseLong(dadosUser[0]);
							Usuario user = R.userRepository.findById(id_user).orElse(null);	
							
					 String data_exp = null,ret_id_pacote=null,nome_pacote ="";
					 try {					
				          String estado = null;
				          estado = R.subscricaoRepository.findEstadoSubscricao(referencia);
				        		 
				          if(estado != null) {
				        	 
						 if(estado.equals("Paga")) {
						
									 return 1;
								 }else {
									 return 0;
								 }
				          }else {
				        	  System.out.println("0"+estado);//151119040
				        	  return 0;
				          }
					 }	
						 catch (Exception e) {
							// TODO: handle exception
							System.out.println(e);//151119040
							return -1;
						}
				       
								
					
				 }else {
						return -1;
					}
			}
			 
			 
			 public void mainA() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
			        String url = "http://localhost:8080/payment/fiinikacomercial/update_status";
			       // String url = "https://biuryshine.musicash.co/payment/musicash/update_status";
			        String payload = "{\"amount\":\"10000.00\",\"custom_fields\":{\"callback_url\":\"https://7f1b-129-122-164-185.ngrok-free.app/payment/musicash/update_status\",\"telemovel\":\"930125528\",\"name\":\"Heitor\",\"token\":\"40c1ac80735b47eebc12b7f40efe43de\"},\"datetime\":\"2024-07-03T13:16:25Z\",\"entity_id\":1068,\"fee\":\"10000.00\",\"id\":783907683652,\"parameter_id\":null,\"period_end_datetime\":\"2024-06-12T10:00:00Z\",\"period_id\":7839,\"period_start_datetime\":\"2024-07-03T10:00:00Z\",\"product_id\":1,\"reference_id\":164438030,\"terminal_id\":\"0000000000\",\"terminal_location\":\"Internet\",\"terminal_period_id\":0,\"terminal_transaction_id\":0,\"terminal_type\":\"MB\",\"transaction_id\":7683652}";
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
			 
			 
				 

}
