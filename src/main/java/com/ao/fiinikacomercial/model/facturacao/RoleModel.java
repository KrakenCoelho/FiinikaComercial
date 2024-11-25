package com.ao.fiinikacomercial.model.facturacao;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

import com.ao.fiinikacomercial.enums.RoleName;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class RoleModel implements GrantedAuthority, Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roleId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName roleName;
    
    
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return this.roleName.toString();
	}


	public long getRoleId() {
		return roleId;
	}


	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}


	public RoleName getRoleName() {
		return roleName;
	}


	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}
	
	


}
