package com.ao.fiinikacomercial.repository.facturacao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.EmpresaSector;

public interface EmpresaSectorRepository extends JpaRepository<EmpresaSector,Long>{
	
	Iterable<EmpresaSector> findByEmpresa(Empresa empresa);
	
	@Transactional(readOnly = false)
	@Modifying
	@Query(value="delete from empresa_sector where empresa_idfk =:empresa_idfk ", nativeQuery = true)
	void deleByIdEmpresa(long empresa_idfk);
	
	boolean deleteByEmpresa(Empresa empresa);

}
