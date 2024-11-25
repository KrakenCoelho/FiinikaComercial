package com.ao.fiinikacomercial.security;

import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.facturacao.userRepository;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
    public userRepository _usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String telemovel) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
	//	Usuario usuario = _usuarioRepository.findByUsername(username)
		Usuario usuario = _usuarioRepository.findByTelemovel(telemovel)
				 .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + telemovel));
		boolean Enabled =true;
		boolean AccountNonLock =true;

		
		 return new User(usuario.getId_usuario()+"_"+usuario.getUsername(), usuario.getPassword(), Enabled, true, true,AccountNonLock, usuario.getAuthorities());
	}

}
