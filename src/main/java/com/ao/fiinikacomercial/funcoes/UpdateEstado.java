//package com.ao.fiinikacomercial.funcoes;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.ao.dexaconta.model.DataHora;
//import com.ao.dexaconta.model.*;
//import com.ao.dexaconta.repository.Data_HoraRepository;
//import com.ao.dexaconta.service.Repositorio;
//
//
//import com.squareup.okhttp.Response;
//
//
//@Component @EnableScheduling 
//public class UpdateEstado {
//	
//	 @Autowired
//	 Repositorio R;
//	 Funcoes objFunc  = new Funcoes();
//	
//	 @Autowired
//	 private Data_HoraRepository _datahoraRepository;
//	
//	private final long SEGUNDO = 1000; 
//    private final long MINUTO = SEGUNDO * 60; 
//    private final long HORA = MINUTO * 60;
//    String msg = "";
//    
//    
//
//    @Scheduled(cron = "0 27 14 * * ?") 
//  //  @Scheduled(cron = "0 0/1 * * * ?",zone="UTC") // de 1 em 1 minuto
//    public void Actualiza() throws ParseException { 
//    	
//    	for(DataHora data:_datahoraRepository.findAll()) {
//	    	if(objFunc.AddDays(data.getData_hora(), 1).equals(objFunc.DataActual())) {
//				DataHora d = new DataHora();
//				d.setId_data(data.getId_data());
//				d.setData_hora(objFunc.DataActual());
//				_datahoraRepository.save(d);
//				System.out.println("OK");
//			}
//     }
//    	//_datahoraRepository.updateEstadoCron("Concluido", objFunc.DataActual());
//    
//    }
//    
//    
//    //@Scheduled(cron = "0 15 10 15 * ?") estamos programando uma tarefa para ser executada às 10:15 no dia 15 de cada mês.
//    @Scheduled(cron = "0 0 20 * * ?")  // Todos os dias as 20 horas
//   // @Scheduled(cron = "0 0/1 * * * ?",zone="UTC") // de 1 em 1 minuto
//    public void EliminaRefPendentes() { 
//      
//       R._subscricaoRepository.DeleteRefereciaExpCron();
//  
//    }
//    
//    @Scheduled(cron = "0 0 7 * * ?")  // Todos os dias as 20 horas
//    public void EnviaSMSFaltandoDias() throws ParseException, IOException { 
//     
//    	long dias = 0;
//	
//		for(Subscricao sub : R._subscricaoRepository.findSubscricaDexaCron("Paga")) {	
//			
//			 dias = objFunc.DiferencaDatas(objFunc.DataActual(), sub.getData_expiracao_pagamento());
//		
//			 if(dias == 5) {
//				 
//				 Optional <Empresa> emp = R._empresaRepository.findById(sub.getId_empresafk());
//				 Iterable <Usuario> usu = R._usuarioRepository.findIdUserEmpresa(sub.getId_empresafk());
//				
//				 msg = "Caro(a) cliente "+emp.get().getNome_empresa()+", a sua subscricao para o Sistema de facturação Dexa Contas termina em "+dias+" dias faça já  pagamento para poder continuar a facturar. ";
//				 Response r = objFunc.enviarSmsSingle(usu.iterator().next().getTelemovel(), msg);
//				 r.body().close();
//			
//				}
//		 }		
//		
//  
//    }
//    
//    
//
//}
