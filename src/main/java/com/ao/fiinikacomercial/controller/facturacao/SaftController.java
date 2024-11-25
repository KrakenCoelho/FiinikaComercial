package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;


@Controller
public class SaftController {
	
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	 @RequestMapping(value="/fiinika/configuracoes/saft", method=RequestMethod.GET)
	   public String PromocoesGerais(Model model, HttpSession session,HttpServletRequest httpServletRequest,HttpServletResponse response) throws IOException{
		 
				 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();				
				   if(objSessao!=null) {
						userDetails = (UserDetails) objSessao;
					    String []  dadosUser= userDetails.getUsername().split("_");
					    long id_user = Long.parseLong(dadosUser[0]);
						Usuario user = R.userRepository.findById(id_user).orElse(null);
					    authenticatedUser.Dados(model, session, response);				    

//					     model.addAttribute("saft_mes",R.saftRepository.findSaftAnos(user.getIdEmpresaUser()));	
//						 model.addAttribute("data_actual",objFunc.DataActual());
//						 model.addAttribute("menor_data_registo",objFunc.AnoMesDia(R._facturaRepository.MenorDataFactura()));
//						 model.addAttribute("maior_data_registo",objFunc.AnoMesDia(R._facturaRepository.MaiorDataFactura()));
			
			return "fiinika/configuracoes/saft";
			
			}else {
				return  "redirect:/login";
			}
	    }

}
