package com.ao.fiinikacomercial.controller.facturacao;




import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FragmentsController {
	

	//---''''''''''''''''''''''''''---------------- Fragmentos -------------''''''''''''''''''''''
	@RequestMapping("fragments/script")
	   public String Script(){
		 
	    return "/fragments/script";
	    }
	
	@RequestMapping("fragments/scriptLogin")
	   public String ScriptLogin(){
		 
	    return "/fragments/scriptLogin";
	    }
	
	@RequestMapping("fragments/header")
		   public String Header(){
			 
		    return "/fragments/header";
		    
		    }
	
	@RequestMapping("fragments/headerAdm")
	   public String HeaderAdmin(){
		 
	    return "/fragments/headerAdm";
	    
	    }
	
	@RequestMapping("fragments/menuAdmin")
	   public String MenuAdmin(){
		 
	    return "/fragments/menuAdmin";
	    
	    }
	
	@RequestMapping("fragments/menuCliente")
	   public String MenuUser(){		
		
	    return "/fragments/menuCliente";
	    
	    }
	
	@RequestMapping("fragments/menuInstituicao")
	   public String TopNav(){
		 
	    return "/fragments/menuInstituicao";
	    
	    }
	
	@RequestMapping("fragments/divMobile")
	   public String DivMobile(){
		 
	    return "/fragments/divMobile";
	    
	    }
	
	
	@RequestMapping("fragments/footer")
	   public String Footer(){
		 
	    return "/fragments/footer";
	    
	    }	
	
	
	

}
