package com.ao.fiinikacomercial.service;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ao.fiinikacomercial.service.PasswordResetService;
import com.ao.fiinikacomercial.model.facturacao.PasswordResetToken;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.funcoes.*;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;

@Service
public class PasswordResetService {

    @Autowired
    private Repositorio R;
    Funcoes objFunc = new Funcoes();

    public void requestPasswordReset(String email,HttpServletRequest request) throws ResendException {
        Usuario user = R.userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            passwordResetToken.setToken(token);
            passwordResetToken.setUsuario(user);
            R.passwordRepository.save(passwordResetToken);
          
            String resetUrl = objFunc.currentUrlServer(request) + "/fiinika/seguranca/criar-nova-senha/" + token;
            resetUrl = "<a href=\"" + resetUrl + "\">Link</a>";
            String message = "Para redefinir sua senha, clique no link a seguir:\n" + resetUrl;
            
            Resend resend = new Resend("re_PdWcAqGc_LNykrApEYiKwwt6wY8Du7jAP");
            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
	                .from("Fiinika <geral@fiinika-comercial.com>")
	                .to(user.getEmail())
	                .subject("Redefinição de senha")
	                .html(message)
	                .build();

	        SendEmailResponse data = resend.emails().send(sendEmailRequest);
        }else {System.out.println("Não enviado");}
    }

    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken passwordResetToken = R.passwordRepository.findByToken(token).orElse(null);
        if (passwordResetToken != null && !passwordResetToken.isExpired()) {
            Usuario user = passwordResetToken.getUsuario();            
            
            user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
            R.userRepository.save(user);
            R.passwordRepository.delete(passwordResetToken);
            return true;
        }
        return false;
    }
}



