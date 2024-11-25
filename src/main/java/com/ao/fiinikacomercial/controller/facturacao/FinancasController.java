package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ao.fiinikacomercial.model.facturacao.Despesa;
import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@Controller
public class FinancasController {

	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	

	    public BigDecimal addBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.add(b);
	    }

	    public BigDecimal multiplyBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.multiply(b);
	    }
	    
		 @RequestMapping("/fiinika/financas/ver-relatorios")
		   public String Despesas(Model model, HttpSession session,HttpServletResponse response) throws ParseException, IOException{
			
		   		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);			   
			    
			     model.addAttribute("titulo","Ver relatorios");
			    
			     List ListmesProveito= new ArrayList<>(); 
				 List ListmesDespesa= new ArrayList<>(); 
				 List ListmeReceber= new ArrayList<>(); 
				 
			     double proveitoMes = 0; String despesaMes="0";
			     for(Object mes:objFunc.ListaMeses()) {
			    	 if(Integer.parseInt(mes.toString()) <= Integer.parseInt(objFunc.MesActual())) {
					     double total_recibos = 0;
						 String t_recibo = R.reciboRepository.TotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual(),mes.toString());
						 String t_factRec = R.facturaRepository.TotalFacturadoFR(user.getIdEmpresaUser(),objFunc.AnoActual(),mes.toString());
						    if(t_recibo!=null) {
						    	total_recibos = Double.parseDouble(t_recibo);
						    }
							if(t_factRec!=null) {
								total_recibos = total_recibos + Double.parseDouble(t_factRec);
						    }			    
							ListmesProveito.add(total_recibos);
							ListmesDespesa.add(R.despesaRepository.TotalDespesas(user.getIdEmpresaUser(),objFunc.AnoActual(),mes.toString()));
							ListmeReceber.add(R.facturaRepository.TotalPagar(user.getIdEmpresaUser(),objFunc.AnoActual(),mes.toString()));
							if(Integer.parseInt(mes.toString()) == Integer.parseInt(objFunc.MesActual())) {
								proveitoMes = total_recibos;
								despesaMes = R.despesaRepository.TotalDespesas(user.getIdEmpresaUser(),objFunc.AnoActual(),mes.toString());
							}
							
			    	 }	
						
						
			    	
			    }
		
			    model.addAttribute("proveitos",ListmesProveito);
			    model.addAttribute("despesas",ListmesDespesa);
			    model.addAttribute("contasreceber",ListmeReceber);
			    model.addAttribute("meses",objFunc.Meses());
			    model.addAttribute("m",objFunc.Mes());
			    model.addAttribute("mesInt",Integer.parseInt(objFunc.MesActual()));		
			    
			     List Listmes1= new ArrayList<>(); 
				 List Listmes2= new ArrayList<>(); 
				 List Listmes3= new ArrayList<>(); 
				 List List_num_venda_mes= new ArrayList<>(); 
				 List List_num_despesa_mes= new ArrayList<>(); 
				 
				 String t_recibo = R.reciboRepository.TotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual());
				 String t_factRec = R.facturaRepository.TotalFacturadoFR(user.getIdEmpresaUser(),objFunc.AnoActual());
				 String medR = R.reciboRepository.AvgTotalRecibo(user.getIdEmpresaUser(),objFunc.AnoActual());
				 String medFR = R.facturaRepository.AvgTotalFacturadoFR(user.getIdEmpresaUser(),objFunc.AnoActual());

				 double total_recibos = 0;
				 double medTotal = 0;
				 boolean c = false;
					    if(t_recibo!=null) {
					    	total_recibos = Double.parseDouble(t_recibo);					    
					    }
						if(t_factRec!=null) {
							total_recibos = total_recibos + Double.parseDouble(t_factRec);							
					    }
				// VERIFICAR O CALCULO DA MEDIA	
						if(medR!=null) {					    	
					    	medTotal = Double.parseDouble(medR);					    	
					    }
						if(medFR!=null) {							
							medTotal = medTotal + Double.parseDouble(medFR);							
					    }
				 
						
				   int n = 0,num=0;
				   for(Object obj_mes:FiltraMesEntradas2(objFunc.AnoActual(),user.getIdEmpresaUser())) {	
					  
						   n = R.facturaRepository.ProveCountVendasMesFR(user.getIdEmpresaUser(),obj_mes.toString());
						   n = n + R.reciboRepository.CountVendasMes(user.getIdEmpresaUser(),obj_mes.toString());
						   Listmes1.add(objFunc.Mes(obj_mes.toString()));
						   List_num_venda_mes.add(n);
						   n=0;
						   num++;
					 }
				   
				   for(Object obj_mes:FiltraMesDespesas(objFunc.AnoActual(),user.getIdEmpresaUser())) {			
						 Listmes2.add(objFunc.Mes(obj_mes.toString()));
						 List_num_despesa_mes.add(R.despesaRepository.CountDespesasMes(user.getIdEmpresaUser(),obj_mes.toString()));
					 }

				   model.addAttribute("totalProveitos", total_recibos);
				   model.addAttribute("mediaMensal", total_recibos/num);
				   model.addAttribute("proveitoMes", proveitoMes);
				   
				   model.addAttribute("despesaMes", despesaMes);
				   model.addAttribute("mediaMensalD", R.despesaRepository.AvgTotalDespesa(user.getIdEmpresaUser(),objFunc.AnoActual()));
				   model.addAttribute("despesaTotal", R.despesaRepository.TotalDespesa(user.getIdEmpresaUser(),objFunc.AnoActual()));
				
				   
				   model.addAttribute("meses_venda",Listmes1);
				   model.addAttribute("num_vendas",List_num_venda_mes);
				   model.addAttribute("meses_despesas",Listmes2);
				   model.addAttribute("num_despesas",List_num_despesa_mes);
	
				return "fiinika/financas/ver-relatorios";
				
				}else {
					return "redirect:/index";
				}
		    }
		 
		 //Entradas para FR
		 public Object[]  FiltraMesEntradas2(String ano,long id_empresaLogada) {
			 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
		 	 List<Object> Listmes_aux= new ArrayList<>(); 
		 	 String controle="0";
		 
				 for(List rec: R.reciboRepository.findIdReciboEmpresa(id_empresaLogada,ano,id_empresaLogada,ano)) {							
					 Listmeses.add(rec.get(0).toString().substring(5,7));
					} 
				 Object[] meses = Listmeses.toArray();
					 for(int i=0; i<meses.length;i++) {	
						 if(!controle.equals((String)meses[i])) {
							 Listmes_aux.add((String) meses[i]);
					          controle= (String) meses[i];
					          
					         
						 }
					          
				} 
				Object[] mesFinal = Listmes_aux.toArray();
					return mesFinal;
			 }
		 
			 public Object[]  FiltraMesDespesas(String ano,long id_empresaLogada) {
				 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
			 	 List<Object> Listmes_aux= new ArrayList<>(); 
			 	 String controle="0";
			 
					 for(Despesa des: R.despesaRepository.findDespesa(id_empresaLogada,ano)) {							
						 Listmeses.add(des.getData_despesa().substring(5,7));
						} 
					 Object[] meses = Listmeses.toArray();
						 for(int i=0; i<meses.length;i++) {	
							 if(!controle.equals((String)meses[i])) {
								 Listmes_aux.add((String) meses[i]);
						          controle= (String) meses[i];
						          
						         
							 }
						          
					} 
					Object[] mesFinal = Listmes_aux.toArray();
						return mesFinal;
				 }

}
