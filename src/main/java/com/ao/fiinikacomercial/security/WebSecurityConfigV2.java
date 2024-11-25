package com.ao.fiinikacomercial.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfigV2 {
	
	
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	
		    /*	
		   	 http
		        .sessionManagement()
		            .maximumSessions(1) // Configura o número máximo de sessões simultâneas permitidas (1 no exemplo)
		            .maxSessionsPreventsLogin(true) // Define se um novo login deve impedir o login de outra sessão ativa. Com o valor true não entra no login com false entra e a outra sessão é mandada para expiredUrl
		            .expiredUrl("/session-expired"); // Página para redirecionar quando uma sessão expirar
		   	*/
    	/*
		    http
		    	.sessionManagement()
		        .invalidSessionUrl("/login?invalid");*/
		    	   //@formatter:off
        http
    	.sessionManagement()
        .invalidSessionUrl("/login?invalid");
		    http.authorizeRequests()          
		    .antMatchers("/static/**").permitAll()   
		    .antMatchers("/fragments/scriptLogin").permitAll()
		    .antMatchers("/login").permitAll()
		    .antMatchers("/admin/**").hasRole("ADMIN")
		    .antMatchers("/fiinika/financas/**").hasAnyRole("SUPER", "ADMIN")
		    .antMatchers("/fiinika/stock/**").hasAnyRole("SUPER", "ADMIN")
		    .antMatchers("/fiinika/salarios/**").hasAnyRole("SUPER", "ADMIN")
		    .antMatchers("/fiinika/procurement/**").hasAnyRole("SUPER", "ADMIN")
		    .antMatchers("/fragments/**").hasAnyRole("USER", "ADMIN","SUPER")
		    .antMatchers("/cliente/dashboard").hasRole("USER")
		    .antMatchers("/cliente/informacao/**").hasRole("USER")
		    .antMatchers("/cliente/conta-custodia/**").hasRole("USER")
		    //.anyRequest().authenticated()            
		    .and()
		      .formLogin()
		      .loginPage("/login")
		      .usernameParameter("telemovel")
		      .loginProcessingUrl("/process-login")
		      .successHandler(new CustomAuthenticationSuccessHandler()) // Use o manipulador personalizado
		    //  .defaultSuccessUrl("/madz/cliente/dashboard")
		      .failureUrl("/login?error=true")
		      .failureHandler((request,response,exception)->{
		    	  response.setStatus(HttpStatus.UNAUTHORIZED.value());
		    	  response.getWriter().write("Credenciais incorretas");
		      })
		      .permitAll()	           
		    .and()
		      .logout()
		      .logoutUrl("/logout")
		    //  .addLogoutHandler(customLogoutHandler())
		      .logoutSuccessUrl("/login?logout=true")
		      .invalidateHttpSession(true)
		      .clearAuthentication(true)
		      .deleteCookies("JSESSIONID")
		      .permitAll()
		     .and()
		     .exceptionHandling()
		       .accessDeniedPage("/403")
		     .and()
		       .exceptionHandling()
		       .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.NOT_FOUND)) // Trata páginas não encontradas
		     .and()
		      .csrf()
		      .disable();
		return http.build(); 
      
    }
/*
    @Bean
    public LogoutHandler customLogoutHandler() {
        return new CustomLogoutHandler();
    } */
    

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } 
} 