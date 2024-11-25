//package com.ao.wonga.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.logout.LogoutHandler;
//
//import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//public class CustomLogoutHandler implements LogoutHandler {
//
//    @Override
//    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
//        // Aqui você pode adicionar a lógica personalizada para o logout
//        // Por exemplo, limpar cookies, revogar tokens, etc.
//    	  System.out.println("Auth: " + SecurityContextHolder.getContext().getAuthentication());  
//    	 
//    	  try {
//    		for(GrantedAuthority auth: authentication.getAuthorities()) {
//    		  if (auth.getAuthority().equals("ROLE_ADMIN") || auth.getAuthority().equals("ROLE_SUPER")) {        	 
//    	            // Se for um administrador, redirecione para a página de admin  login
//    			  System.out.println("Consegui, agora é só redirecionar para a página desejada... backoffice");
//    	          httpServletResponse.sendRedirect("/backoffice/login");
//    	       } 
//    		  if (auth.getAuthority().equals("ROLE_USER")) {        	 
//  	            // Se for um administrador, redirecione para a página de inicio da loja
//    			  System.out.println("Consegui, agora é só redirecionar para a página desejada... cliente");
//  	             httpServletResponse.sendRedirect("/autenticacao/login");
//  	          } 
//			
//    	  }
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
//    }
//    
//    private String[] extractDomainAndLastString(String url) {	        
//
//    	  String[] domainAndLastString = new String[2];
//    	  try {
//              URI parsedUri = new URI(url);
//              String domain = parsedUri.getHost();
//              String path = parsedUri.getPath();
//              String[] pathComponents = path.split("/");
//
//              // Se o esquema estiver presente na URL, adicionamos ao domínio
//              String scheme = parsedUri.getScheme();
//              if (scheme != null) {
//                  domain = scheme + "://" + domain;
//              }
//
//              // Se a porta estiver presente na URL, adicionamos ao domínio
//              int port = parsedUri.getPort();
//              if (port != -1) {
//                  domain = domain + ":" + port;
//              }
//
//              // O domínio retornado inclui www., se presente
//              domain = domain.startsWith("www.") ? domain.substring(4) : domain;
//
//              // A última string será a última parte do caminho da URL
//              String lastString = pathComponents[pathComponents.length - 1];
//
//              domainAndLastString[0] = domain;
//              domainAndLastString[1] = lastString;
//          } catch (Exception e) {
//              e.printStackTrace();
//          }
//
//         return domainAndLastString;
//     
//    }
//
//}




