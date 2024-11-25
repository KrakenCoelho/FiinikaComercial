//package com.ao.fiinikacomercial.service;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.ao.wonga.model.*;
//import com.ao.wonga.funcoes.*;
//import com.ao.wonga.service.Repositorio;
//import com.resend.Resend;
//import com.resend.core.exception.ResendException;
//import com.resend.services.batch.model.CreateBatchEmailsResponse;
//import com.resend.services.emails.model.SendEmailRequest;
//import com.resend.services.emails.model.SendEmailResponse;
//
//
//
//@Component @EnableScheduling @EnableAsync
//public class CronRegras {
//	
//
//	
//	private final long SEGUNDO = 1000; 
//    private final long MINUTO = SEGUNDO * 60; 
//    private final long HORA = MINUTO * 60;
//    
//    @Autowired
//  	Repositorio R;
//    Funcoes objFunc  = new Funcoes();
//    
//    @Async
//  //@Scheduled(cron = "0 15 10 15 * ?") estamos programando uma tarefa para ser executada às 10:15 no dia 15 de cada mês.
//  //@Scheduled(cron = "*/2 * * * * *") // de 2 a 2 segundos
////  @Scheduled(cron = "0 0/1 * * * ?",zone="UTC") // de 1 em 1 minuto
//    @Scheduled(cron = "0 0 0 * * ?") // Executar todos os dias às 20:00
//    public void DescontoAutCliente() throws InterruptedException, ParseException, ResendException { 
//    	
//   //======   Cron feito de desconto automatico na conta do cliente ===========
//    	
//    	List<Usuario> useresBloqueio = new ArrayList<>(); // Para os user a serem bloqueiados
//    	List<Usuario> usersBaixaNivel = new ArrayList<>(); // Para os users que baixaram de nivel
//    	List<Usuario> padrinhoBaixaNivel = new ArrayList<>(); // Para os users (Padrinhos) que baixaram de nivel
//    	
//    	List<Cartao> cartaoUpdateBalance = new ArrayList<>(); // Para descontar o credito
//    	List<Credito> creditoPagar = new ArrayList<>(); // Para os creditos  a pagar
//    	List<Movimento> movimentos = new ArrayList<>(); // Para o registos das transacoes  a pagar
//    	List<Bloqueio> bloqueios = new ArrayList<>(); // Para a lista de bloqueados
//    	
//    	Usuario sistema = R.userRepository.findById((long)1).orElse(null);
//    	
//    	// CREDITO DE CLIENTES COM PENDENTES NA DATA ACTUAL
//    	Iterable <Credito> creditosPendentesNoPrazo = R.creditoRepository.findCreditoNoPrazo("Pendente",objFunc.DataActual());
//    	for(Credito credito : creditosPendentesNoPrazo) {
//    		
//    		Usuario usuario = R.userRepository.findById(credito.getUsuario().getId_usuario()).orElse(null);
//    		Cliente cliente = R.clienteRepository.findByUsuario(usuario).orElse(null);
//    		Cartao cartao = R.cartaoRepository.findByIdCliente(usuario.getId_usuario()).orElse(null);    		
//    		// Verifica se o cliente tem saldo suficiente para o desconto
//    		if(Double.parseDouble(cartao.getBalance()) >= Double.parseDouble(credito.getValorPagar())) {
//    			float newSaldo =  Float.parseFloat(cartao.getBalance()) - Float.parseFloat(credito.getValorPagar());
//    			cartao.setBalance(newSaldo+"");
//    			cartaoUpdateBalance.add(cartao);
//    			
//    			credito.setEstado("Pago");
//    			credito.setDatePagamento(objFunc.DataActual(1));
//    			creditoPagar.add(credito);
//    			
//    			Movimento movimento = new Movimento();
//    		    movimento.setIdEmissor(usuario);
//    		    movimento.setIdReceiver(sistema);
//    		   
//    		    movimento.setNameReceiver("Pagamento de Crédito");	 
//    		    movimento.setNameEmissor(cliente.getClientName());		    					   
//    		   
//    		    movimento.setDateTrans(objFunc.DataActual(1));
//    		    movimento.setHora(objFunc.Hora());
//    		    movimento.setTransType("Debito/Outro");
//    		    movimento.setStatus(200);
//    		    movimento.setValor(credito.getValorPagar());	   
//    		    movimento.setChannel(1); //Channel 1 online
//    		    movimento.setObs("Desconto de Pagamento de Crédito do pelo sistema");
//    		    movimentos.add(movimento);  
//    		}		
//    	}    	
//    	 //======================== DESCONTO NA CONTA DO CLIENTE PARA PAGAR O CREDITO ==================		  
//    	 System.out.println("======================= LOTE ========================");
//  	     int batchSize = 500; // Tamanho do lote
//         for (int i = 0; i < cartaoUpdateBalance.size(); i += batchSize) {
//             List<Cartao> batchListCartao = cartaoUpdateBalance.subList(i, Math.min(i + batchSize, cartaoUpdateBalance.size())); 
//  	         R.cartaoRepository.saveAll(batchListCartao);
//  	         
//  	         List<Credito> batchListCredito= creditoPagar.subList(i, Math.min(i + batchSize, creditoPagar.size())); 
//  	         R.creditoRepository.saveAll(batchListCredito);
//  	         
//  	         List<Movimento> batchListMovimento= movimentos.subList(i, Math.min(i + batchSize, movimentos.size())); 
//	         R.movimentoRepository.saveAll(batchListMovimento);
//  	         
//             System.out.println("Pagamento de credito");
//         }   
//
//         System.out.println("Cron feito de desconto automatico na conta do cliente");    	
//  
//    }
//    
//    @Async
//    //@Scheduled(cron = "0 15 10 15 * ?") estamos programando uma tarefa para ser executada às 10:15 no dia 15 de cada mês.
//  //  @Scheduled(cron = "*/2 * * * * *") // de 2 a 2 segundos
//  // @Scheduled(cron = "0 0/1 * * * ?",zone="UTC") // de 1 em 1 minuto
//    
//    @Scheduled(cron = "0 10 0 * * ?") // Executar todos os dias às 00:10
//    public void NaoCumpridos() throws InterruptedException, ParseException, ResendException, IOException { 
//    	
//    	List<Usuario> useresBloqueio = new ArrayList<>(); // Para os user a serem bloqueiados
//    	List<Usuario> usersBaixaNivel = new ArrayList<>(); // Para os users que baixaram de nivel
//    	List<Usuario> padrinhoBaixaNivel = new ArrayList<>(); // Para os users (Padrinhos) que baixaram de nivel
//    	
//    	List<Cartao> cartaoUpdateBalance = new ArrayList<>(); // Para descontar o credito
//    	List<Credito> creditoPagar = new ArrayList<>(); // Para os creditos  a pagar
//    	List<Credito> creditoIncumprido = new ArrayList<>(); // Para os creditos  a pagar
//    	List<Movimento> movimentos = new ArrayList<>(); // Para o registos das transacoes  a pagar
//    	List<Bloqueio> bloqueios = new ArrayList<>(); // Para a lista de bloqueados
//    	
//    	// ==================================== PARA SMS PADRINHOS =========================== 	    			
//    	List<String> listT = new ArrayList<>(); // 
//    	List<String> listP = new ArrayList<>(); // 
//    	Map<String, String> map = new HashMap<>();
//    	
//    	Usuario sistema = R.userRepository.findById((long)1).orElse(null);
//    	int lo =0;
//    	// CREDITO DE CLIENTES COM PENDENTES NA DATA ACTUAL
//    	Iterable <Credito> creditosPendentesNoPrazo = R.creditoRepository.findCreditoIncumpridos("Pendente",objFunc.DataActual());
//    	for(Credito credito : creditosPendentesNoPrazo) {
//    		
//    		Usuario usuario = R.userRepository.findById(credito.getUsuario().getId_usuario()).orElse(null);
//    		Cliente cliente = R.clienteRepository.findByUsuario(usuario).orElse(null);
//    		Cartao cartao = R.cartaoRepository.findByIdCliente(usuario.getId_usuario()).orElse(null); 
//    		
//    			// Pega o dados do suer afilhado
//    			Usuario userAfilhado = usuario;
//    			// Senão vai para o padrinho do cliente em causa
//    			Convite convite = R.conviteRepository.findByTelAfilhado(usuario.getTelemovel()).orElse(null);
//    			if(convite != null) {
//    				// Aqui já são dados do padrinho
//    				usuario = R.userRepository.findById(convite.getPadrinho().getId_usuario()).orElse(null);
//    			     cliente = R.clienteRepository.findByUsuario(usuario).orElse(null);
//    	    		 cartao = R.cartaoRepository.findByIdCliente(usuario.getId_usuario()).orElse(null); 
//    	    		// Verifica se o padrinho tem fundos para descontar sem juros
//    	    		 if(Double.parseDouble(cartao.getBalance()) >= Double.parseDouble(credito.getValor())) {
//    	     			float newSaldo =  Float.parseFloat(cartao.getBalance()) - Float.parseFloat(credito.getValor());
//    	     			cartao.setBalance(newSaldo+"");
//    	     			cartaoUpdateBalance.add(cartao);
//    	     			
//    	     			credito.setEstado("Pago");
//    	     			credito.setDatePagamento(objFunc.DataActual(1));
//    	     			creditoPagar.add(credito);
//    	     			
//    	     			Movimento movimento = new Movimento();
//    	     		    movimento.setIdEmissor(usuario);
//    	     		    movimento.setIdReceiver(sistema);
//    	     		   
//    	     		    movimento.setNameReceiver("Pagamento de Crédito");	 
//    	     		    movimento.setNameEmissor(cliente.getClientName());		    					   
//    	     		   
//    	     		    movimento.setDateTrans(objFunc.DataActual(1));
//    	     		    movimento.setHora(objFunc.Hora());
//    	     		    movimento.setTransType("Debito/Outro");
//    	     		    movimento.setStatus(200);
//    	     		    movimento.setValor(credito.getValor());	   
//    	     		    movimento.setChannel(1); //Channel 1 online
//    	     		    movimento.setObs("Pagamento de Crédito do afilhado: "+convite.getTelAfilhado());
//    	     		    movimentos.add(movimento);    	     		    
//    	    		 }else {
//    	    			 credito.setEstado("Incumprido");
//    	    			 creditoIncumprido.add(credito);
//    	    		 }
//    	    		 
//    	    		// Se padrinho pagou quer dizer que o afilhado (cliente) não cumpriu
// 	     		    // Vamos verificar o nivel do afilhado para baixar de nivel ou bloquear
// 	     		    
//	    	     		if(userAfilhado.getNivel() == 2) {
//	    	       			// Bloqueia afilhado por 6 meses	    
//	    	     			userAfilhado.setStatus(0);
//	    	     			useresBloqueio.add(userAfilhado);
//	    	     			
//	    	       			// Lista de bloqueados
//	    	       			Bloqueio bloq = new Bloqueio();
//	    	       			bloq.setTelemovel(userAfilhado.getTelemovel());
//	    	       			bloq.setDataBloqueio(objFunc.DataActual(1));
//	    	       			bloq.setUsuario(userAfilhado);
//	    	       			bloq.setDataRegresso(objFunc.StringToDate(objFunc.AddDays(objFunc.DataActual(), 182)));
//	    	       			bloqueios.add(bloq);
//	    	       			
//	    	       		}
//	    	       		
//	    	       		if(userAfilhado.getNivel() > 2) {
//	    	       			// Baixa para o afilhado para nivel 2
//	    	       			userAfilhado.setNivel(2);
//	    	       			usersBaixaNivel.add(userAfilhado);
//	    	       		}    	    		 
//    	    			
//    	    			 // O padrinho cai 3 niveis
//    	    			int nivPadrinho = usuario.getNivel();
//    	    			nivPadrinho = nivPadrinho - 3;
//    	    			if(nivPadrinho < 2) {
//    	    				usuario.setNivel(2);
//    	    				padrinhoBaixaNivel.add(usuario);
//    	    			}else {
//    	    				usuario.setNivel(nivPadrinho);
//    	    				padrinhoBaixaNivel.add(usuario);
//    	    			} 
//    	    	    	
//    	    			// Lista de padrinhos para sms
//	    				listP.add(convite.getTelPadrinho());
//	    				listT.add(userAfilhado.getTelemovel());
//    	    			 	
//    			}
//    			
//    	   		
//    	}
//    	
//    	 //======================== DESCONTO NA CONTA DO PADRINHO PARA PAGAR O CREDITO ==================		  
//    	 System.out.println("======================= LOTE ========================");
//  	     int batchSize = 500; // Tamanho do lote
//         for (int i = 0; i < cartaoUpdateBalance.size(); i += batchSize) {
//             List<Cartao> batchListCartao = cartaoUpdateBalance.subList(i, Math.min(i + batchSize, cartaoUpdateBalance.size())); 
//  	         R.cartaoRepository.saveAll(batchListCartao);
//  	         
//  	         List<Credito> batchListCredito= creditoPagar.subList(i, Math.min(i + batchSize, creditoPagar.size())); 
//  	         R.creditoRepository.saveAll(batchListCredito);
//  	         
//  	         List<Movimento> batchListMovimento= movimentos.subList(i, Math.min(i + batchSize, movimentos.size())); 
//	         R.movimentoRepository.saveAll(batchListMovimento);
//  	         
//             System.out.println("Pagamento de credito");
//         }   
//         // ================================= INCUMPRIDOS ==============
//         for (int i = 0; i < creditoIncumprido.size(); i += batchSize) {           
//  	         List<Credito> batchListCredito= creditoIncumprido.subList(i, Math.min(i + batchSize, creditoIncumprido.size())); 
//  	         R.creditoRepository.saveAll(batchListCredito);  	         
//             System.out.println("Incumprido");
//         }
//         
//         // Users a bloquear
//         for (int i = 0; i < useresBloqueio.size(); i += batchSize) {
//             List<Usuario> batchListUser = useresBloqueio.subList(i, Math.min(i + batchSize, useresBloqueio.size())); 
//  	         R.userRepository.saveAll(batchListUser);
//  	         
//  	         List<Bloqueio> batchListBloq= bloqueios.subList(i, Math.min(i + batchSize, bloqueios.size())); 
//  	         R.bloqueioRepository.saveAll(batchListBloq);
//  	         
//	         System.out.println("Bloqueio de users que não pagaram os creditos");
//         }    
//         
//         // Baixar de nivel Afilhados
//         for (int i = 0; i < usersBaixaNivel.size(); i += batchSize) {
//             List<Usuario> batchListUser = usersBaixaNivel.subList(i, Math.min(i + batchSize, usersBaixaNivel.size())); 
//  	         R.userRepository.saveAll(batchListUser);  	         
//  	         
//	         System.out.println("Baixa para o nivel 2 users");
//         }    
//         
//         // Baixar de nivel Padrinho 3 niveis
//         for (int i = 0; i < padrinhoBaixaNivel.size(); i += batchSize) {
//             List<Usuario> batchListPadr = padrinhoBaixaNivel.subList(i, Math.min(i + batchSize, padrinhoBaixaNivel.size())); 
//  	         R.userRepository.saveAll(batchListPadr); 
//	       
//         } 
//         
//        // FILTRA CADA PADRINHO COM SEUS AFILHADOS
//        String smsFinalPadr = "";
//        int j = 0;
//     	for(String chave :listP) {    		 
// 			  if (map.containsKey(chave)) {
// 	              // Concatenar o valor existente com o novo valor
// 	              String valorExistente = map.get(chave);
// 	              String novoValor = valorExistente + " "+listT.get(j); // Substitua "novo_valor" pelo novo valor que deseja adicionar
// 	              map.put(chave, novoValor);
// 	          } else {
// 	              // Se a chave não existe, simplesmente adicione o valor ao Map
// 	              map.put(chave, listT.get(j));
// 	          }
// 			  j++;
//     	}
//     	
//     	 // SMS MENOS DE BAIXA DE NIVEL PARA OS PADRINHOS
//   	 for (Map.Entry<String, String> entry : map.entrySet()) {
//   		 try {
//               System.out.println("Chave: " + entry.getKey() + ", Valor: " + entry.getValue());
//	            if(entry.getValue().toString().length() > 9) {
//	         		smsFinalPadr = "Caro padrinho sofreu quedas de niveis, em função do incumprimento dos afilhados: "+entry.getValue();
//	         	}else {
//	         		smsFinalPadr = "Caro padrinho sofreu uma queda de 3 niveis, em função do incumprimento do afilhado: "+entry.getValue();
//	         	}
//             System.out.println("Entrou sms padrinhos "+smsFinalPadr);
//	         objFunc.enviarSmsSingle(entry.getKey(),smsFinalPadr);    
//	         
//	             // Simula uma chamada à API remota
//	             Thread.sleep(1000); // Atraso de 1 segundo
//	         } catch (InterruptedException e) {
//	             Thread.currentThread().interrupt();
//	         }
//        }
//         
//         System.out.println("Cron feito de incumprimento");    	
//  
//    }
//    
//    @Async
//   //@Scheduled(cron = "0 15 10 15 * ?") estamos programando uma tarefa para ser executada às 10:15 no dia 15 de cada mês.
//  // @Scheduled(cron = "*/10 * * * * *") // de 2 a 2 segundos
//// @Scheduled(cron = "0 0/1 * * * ?",zone="UTC") // de 1 em 1 minuto    
// @Scheduled(cron = "0 0 8 * * ?") // Executar todos os dias às 08:00
// public void EnvioSms() throws InterruptedException, ParseException, ResendException, IOException { 
//    	
//    	List<String>  telemAfilhados = new ArrayList<>(); // Para os user a serem bloqueiados
//    	List<String> telemAfilhados2 = new ArrayList<>();
//    	List<String> listT = new ArrayList<>(); // 
//    	List<String> listP = new ArrayList<>(); // 
//    	Map<String, String> map = new HashMap<>();
//    	
//    	// CREDITO DE CLIENTES COM PENDENTES NA DATA ACTUAL
//    	Iterable <Credito> creditosPendentesNoPrazo = R.creditoRepository.findCreditoDentroPrazo("Pendente",objFunc.DataActual());
//    	for(Credito credito : creditosPendentesNoPrazo) {
//    		
//    		Usuario usuario = R.userRepository.findById(credito.getUsuario().getId_usuario()).orElse(null);
//    		Cliente cliente = R.clienteRepository.findByUsuario(usuario).orElse(null);
//    		Cartao cartao = R.cartaoRepository.findByIdCliente(usuario.getId_usuario()).orElse(null); 
//    		
//    		// Calcula a diferença de datas
//    		long dd = objFunc.DiferencaDatas(objFunc.DataActual(), credito.getDatePrazo()+"");
//    		
//    		// Meio parazo
//    		if(dd == credito.getLending().getPrazo()/2) {
//    			// Envia sms para os clientes
//    			   // Pega os numeros de telefones    			
//    			   telemAfilhados.add("<gsm>"+usuario.getTelemovel()+"</gsm>");    			  
//    		}
//    	//	System.out.println(dd);
//    		if(dd == 1) {
//    			// Envia sms para os clientes e paddrinhos    			
//    			Convite convite = R.conviteRepository.findByTelAfilhado(usuario.getTelemovel()).orElse(null);
//    			if(convite != null) {
//    				// Aqui já são dados do padrinho 
//    				listP.add(convite.getTelPadrinho()); 
//    				telemAfilhados2.add("<gsm>"+convite.getTelAfilhado()+"</gsm>");
//    				listT.add(usuario.getTelemovel());
//    			}    			
//    		}
//    	
//    	}
//    
//    	
//    	String smsMeioPrazo = "Caro cliente já está a meio prazo do pagamento do seu credito";
//    	String smsFinal = "Caro cliente tem apenas 1 dia para o pagamento do seu credito";
//    	String smsFinalPadr = "";    	
//    	
//    	int batchSize = 100;     	
//    	// SMS MEIO PRAZO PARA OS CLIENTES
//        for (int i = 0; i < telemAfilhados.size(); i += batchSize) {
//            List<String> batchListTelem = telemAfilhados.subList(i, Math.min(i + batchSize, telemAfilhados.size())); 
// 	         String listaTelem = batchListTelem.toString().replace("[", "");
//	 	        listaTelem = listaTelem.replace("]", "");
//	 	        listaTelem = listaTelem.replace(", ", "");
//	 	        
// 	           objFunc.enviarSmsMany(listaTelem,smsMeioPrazo);
// 	           System.out.println(listaTelem+" SMS MEIO PRAZO PARA OS CLIENTES");
//          
//        }
//      	// SMS MENOS 1 DIA PARA O FINAL PARA OS CLIENTES
//        for (int i = 0; i < telemAfilhados2.size(); i += batchSize) {
//            List<String> batchListTelem = telemAfilhados2.subList(i, Math.min(i + batchSize, telemAfilhados2.size())); 
// 	         String listaTelem = batchListTelem.toString().replace("[", "");
//	 	        listaTelem = listaTelem.replace("]", "");
//	 	        listaTelem = listaTelem.replace(", ", "");
//	 	        
// 	            objFunc.enviarSmsMany(listaTelem,smsFinal); 	            
//	 	        System.out.println(listaTelem+" SMS MENOS 1 DIA PARA O FINAL PARA OS CLIENTES ");
//        }   
//        
//   
//       
//    	int j = 0;
//    	for(String chave :listP) {    		 
//			  if (map.containsKey(chave)) {
//	              // Concatenar o valor existente com o novo valor
//	              String valorExistente = map.get(chave);
//	              String novoValor = valorExistente + " "+listT.get(j); // Substitua "novo_valor" pelo novo valor que deseja adicionar
//	              map.put(chave, novoValor);
//	          } else {
//	              // Se a chave não existe, simplesmente adicione o valor ao Map
//	              map.put(chave, listT.get(j));
//	          }
//			  j++;
//    	}
//    	
//    	  // SMS MENOS 1 DIA PARA O FINAL PARA OS PADRINHOS
//    	 for (Map.Entry<String, String> entry : map.entrySet()) {
//    		 try {
//                System.out.println("Chave: " + entry.getKey() + ", Valor: " + entry.getValue());
//	            if(entry.getValue().toString().length() > 9) {
//	         		smsFinalPadr = "Caro padrinho os seus afilhados: "+entry.getValue()+", possuem apenas 1 dia para o pagamento dos seus creditos";
//	         	}else {
//	         		smsFinalPadr = "Caro padrinho o seu afilhado: "+entry.getValue()+", tem apenas 1 dia para o pagamento do seu credito";
//	         	}
//             System.out.println(entry.getKey()+"Entrou padrinhos "+smsFinalPadr);
//	         objFunc.enviarSmsSingle(entry.getKey(),smsFinalPadr);    
//	         
//	             // Simula uma chamada à API remota
//	             Thread.sleep(500); // Atraso de 500 milisegundo
//	         } catch (InterruptedException e) {
//	             Thread.currentThread().interrupt();
//	         }
//         }
//	     
//      }
//    
//
//       
//	}


