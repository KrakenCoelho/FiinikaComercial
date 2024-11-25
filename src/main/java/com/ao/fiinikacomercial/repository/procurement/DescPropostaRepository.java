package com.ao.fiinikacomercial.repository.procurement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.procurement.DescProposta;


public interface DescPropostaRepository extends JpaRepository<DescProposta,Long> {

	 @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE desc_proposta SET estadodp='Pago', selecionado='1' WHERE iddp=?",
	      nativeQuery = true
	   )
	   int actualizadp( int referencia);
	  
	 @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE desc_proposta SET qtd=?, subtotal=? WHERE iddp=?",
	      nativeQuery = true
	   )
	   int actualizqtd( String qtd,String sbt ,Long id);
	 
	 
	  @Query(
		      value = "SELECT qtd FROM desc_proposta WHERE iddp=?",
		      nativeQuery = true
		   )
		   int quantidade(int id);
	  
	 
	  
	
}
