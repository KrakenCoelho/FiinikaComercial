package com.ao.fiinikacomercial.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Empresa;

import okhttp3.*;


@Service
public class ProxyPay {
	
	 Funcoes objFunc = new Funcoes();
	   private final String API_KEY = "a95teci62t7v15d63s3om2recu4fp5v3";
	   private final String LINK = "https://api.proxypay.co.ao";
	
	 public int SentProxypay(HttpServletRequest request,String nome,String token,String email,String telemovel,String valor,String referencia,String dataExpRef) throws JSONException, ParseException, IOException { 
		 
		 JSONObject custom_fields = new JSONObject();
			custom_fields.put("name",nome)
						 .put("token", token)
						 .put("telemovel", telemovel)
						  .put("callback_url",  "https://"+request.getServerName()+"/payment/fiinikacomercial/update_status");
		

			JSONObject reference = new JSONObject();

			reference.put("amount", valor)
			  .put("end_datetime", dataExpRef)
			  .put("custom_fields", custom_fields);

		    OkHttpClient client = new OkHttpClient();

			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(reference.toString(), mediaType);
			
			
			Request requestP = new Request.Builder()	
					.url("https://api.proxypay.co.ao/references/"+referencia)
				    //.url("https://api.sandbox.proxypay.co.ao/references/"+referencia)
				    .put(body)					    //  srarbb65qj4phisdt94nr8nnpm5bem5u
				    .addHeader("Authorization",  "Token " + "a95teci62t7v15d63s3om2recu4fp5v3")
				    .addHeader("Accept", "application/vnd.proxypay.v2+json")
				    .addHeader("Content-Type", "application/json")
				    .build();

			 Response response = client.newCall(requestP).execute();
			 System.out.println("valido "+response.code());
			 
		  return response.code();
	 
	   }
	 
		public String gerarReferencia(Empresa empresa,Double  valor, String package_type,HttpServletRequest requestD) {
		      final String NF = objFunc.geraRef();
		     // final String NF = objFunc.GeraReferencia();
		      if (NF != null) {
		    	  
		         System.out.println("REFERENCE: " + NF + "\nNAME: " + empresa.getNomeEmpresa());
		     
		         JSONObject custom_fields = new JSONObject();
		         custom_fields.put("name", empresa.getNomeEmpresa())
		         			  .put("description", package_type)
		         			  .put("telephone", empresa.getTelemovel())
		         			  .put("transaction_id", Long.toString(empresa.getIdEmpresa()))
		         			// .put("callback_url", "https://"+requestD.getServerName()+"/app/payment/payments.php");
		         			 .put("callback_url",  "https://"+requestD.getServerName()+"/procurement/update_status");
		         			  
		         Calendar now = Calendar.getInstance();
		         Calendar novoCalendario = (Calendar)now.clone();
		         novoCalendario.add(Calendar.HOUR_OF_DAY, 2);
		         
		         JSONObject reference = new JSONObject();
		         
		         reference.put("amount", valor)
		         		  .put("end_datetime", (new SimpleDateFormat("yyyy-MM-dd hh:mm"))
		         		  .format(novoCalendario.getTime())).put("custom_fields", custom_fields);
		         
		         try {
		 		    OkHttpClient client = new OkHttpClient();

					MediaType mediaType = MediaType.parse("application/json");
					RequestBody body = RequestBody.create(reference.toString(), mediaType);
					
					
					Request requestP = new Request.Builder()	
							.url("https://api.proxypay.co.ao/references/"+NF)
						    //.url("https://api.sandbox.proxypay.co.ao/references/"+referencia)
						    .put(body)					    //  srarbb65qj4phisdt94nr8nnpm5bem5u
						    .addHeader("Authorization",  "Token " + "a95teci62t7v15d63s3om2recu4fp5v3")
						    .addHeader("Accept", "application/vnd.proxypay.v2+json")
						    .addHeader("Content-Type", "application/json")
						    .build();

					 Response response = client.newCall(requestP).execute();
					 System.out.println("valido "+response.code());
		             System.out.println("RESPONSE: " + response); 
		             
		            	
	            	
		             
		             if (response.code() > 199 && response.code() < 300) {
		                try {
//		                   String MSG_SMS = "Prezado(a) " + cliente.getNome_c() + ", pague o seu bilhete no ATM ou aplicativo do seu banco, Entidade: 01068" + ", Referencia: " + NF + ", Montante Akz: " + valor;
		                  	
		                	 String MSG_SMS = "Prezado(a), pague o seu bilhete no ATM ou aplicativo do seu banco, Entidade: 01068" + ", Referencia: " + NF + ", Montante Akz: " + valor;
			                  
		                	
		                 System.out.println(MSG_SMS);
		            // Response r =enviarSms(cliente.getNumero(),MSG_SMS);
		            	// r.body().close();
		            	 
//		            	 Response t=enviarMensagem("+244944272703");
//		            	 t.body().close();
		                } catch (Exception e1) {
		                	System.out.println("Erro: " + e1);
		                }
		            

		                return NF;
		             }
		          } catch (Exception e) {
		        	  System.out.println("ERROR: "+ e);
		          }
		      }

		      return null;
		   }
	 
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
		 
		  return "";		
	   }
	   
		 public void Descarta(String ref) throws IOException {
			 
			 OkHttpClient client = new OkHttpClient();

			 Request request = new Request.Builder()
			     .url("https://api.proxypay.co.ao/payments/"+ref)
			     .delete()
			     .addHeader("Authorization", "Token " + "a95teci62t7v15d63s3om2recu4fp5v3")
			     .addHeader("Accept", "application/vnd.proxypay.v2+json")
			     .build();

			 Response response = client.newCall(request).execute();
			 String s = response.body().string();
			 System.out.println("valido "+s+response.code());
			 response.body().close();
			 
		 }

}
