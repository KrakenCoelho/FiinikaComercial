package com.ao.fiinikacomercial.model.facturacao;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;



@Entity
public class Usuario implements UserDetails, Serializable {
	
    private static final long serialVersionUID = 1L;
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idUsuario;

	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = true)
	private String telemovel;

	@Column(nullable = false)
	private String password;
	
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Column(nullable = false)
	private Date dataCriacao;
    
	@Column
	private String tipo;
    
	@Column
	private long idEmpresaUser;
	
	private String role ;
	@Column
	private String codigo ;
	
	@Column
	private String foto_perfil ;

	@ManyToMany
    @JoinTable(name = "TB_USERS_ROLES",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleModel> roles =  new ArrayList<>();
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return this.roles;
	}

	public List<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleModel> roles) {
		this.roles = roles;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.password;
	}  

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public long getId_usuario() {
		return idUsuario;
	}

	public void setId_usuario(long id_usuario) {
		this.idUsuario = id_usuario;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelemovel() {
		return telemovel;
	}

	public void setTelemovel(String telemovel) {
		this.telemovel = telemovel;
	}

	public long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getIdEmpresaUser() {
		return idEmpresaUser;
	}

	public void setIdEmpresaUser(long idEmpresaUser) {
		this.idEmpresaUser = idEmpresaUser;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getFoto_perfil() {
		return foto_perfil;
	}

	public void setFoto_perfil(String foto_perfil) {
		this.foto_perfil = foto_perfil;
	}

	
}
