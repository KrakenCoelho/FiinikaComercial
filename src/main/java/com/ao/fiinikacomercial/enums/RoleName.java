package com.ao.fiinikacomercial.enums;

public enum RoleName {
	
	 ROLE_SUPER,
	 ROLE_ADMIN,	
	 ROLE_USER,
	 ROLE_MERCH;
	
	public static  Object [] getRoleByName(String name) {
		int i = 1;
        for (RoleName roleName : RoleName.values()) {
            if (roleName.name().equalsIgnoreCase(name)) {
                return new Object [] {roleName,i};
            }
            i++;
        }
        throw new IllegalArgumentException("RoleName não encontrado: " + name);
    }
	

}
