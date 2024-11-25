package com.ao.fiinikacomercial.repository.facturacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ao.fiinikacomercial.model.facturacao.PasswordResetToken;
import com.ao.fiinikacomercial.model.facturacao.Usuario;

public interface PasswordRestRepository extends JpaRepository<PasswordResetToken,Long>{
	
	 Optional<PasswordResetToken> findByToken(String username);	 
	 Optional <PasswordResetToken> findFirstByUsuarioOrderByIdDesc(Usuario user);

}
