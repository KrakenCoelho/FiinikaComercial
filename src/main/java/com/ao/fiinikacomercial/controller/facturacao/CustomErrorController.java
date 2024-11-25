package com.ao.fiinikacomercial.controller.facturacao;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
 
    @RequestMapping("/error")
    public String handleError(HttpServletResponse response) {
        int status = response.getStatus();
        if (status == HttpServletResponse.SC_NOT_FOUND) {
            // Trata o erro 404 e redireciona para a página de erro 404 personalizada
            return "404"; // Nome da sua página de erro 404 personalizada
        } else {
            // Outros erros podem ser tratados aqui
            return "error"; // Página de erro genérica
        }
    }
 
    
    public String getErrorPath() {
        return "/error";
    }
    
    @RequestMapping("/403")
	   public String Not403(){
		 
	    return "403";
	    }
    
    
}

