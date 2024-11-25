package com.ao.fiinikacomercial.repository.facturacao;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Sectores;

public interface SectoresRepository extends JpaRepository<Sectores,Long> {
	
	boolean existsBySector(String sector);
	
	
	

}
