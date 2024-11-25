package com.ao.fiinikacomercial.repository.procurement;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.procurement.Pagamento;



public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	  
	 @Query(
		      value = "SELECT id_pag,entidade,referencia,valor,pedido_id,proposta_id FROM pagamento WHERE referencia=? ",
		      nativeQuery = true
		   )
		   List<List> referenciainfo(String referencia);

	  @Query(
		      value = "SELECT pagamento.estatuto FROM `pagamento` WHERE pagamento.referencia=?",
		      nativeQuery = true
		   )
		   String re(String id);
	  
	  @Query(
		      value = "SELECT pagamento.proposta_id FROM `pagamento` WHERE pagamento.referencia=?",
		      nativeQuery = true
		   )
		   Long proposta(String id);
	  
	  @Query(
		      value = "SELECT pedido_id FROM pagamento WHERE pagamento.referencia=?",
		      nativeQuery = true
		   )
		   Long pedidoId(String id);
	  
	  @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE pagamento SET estatuto='Pago' WHERE referencia=?",
	      nativeQuery = true
	   )
	   int actuapag( String referencia);
	  
	  
	  @Query(
		      value = "SELECT produtos_ids FROM `pagamento` WHERE pagamento.referencia=?",
		      nativeQuery = true
		   )
		   String arra(String id);
	  
	  @Query(
		      value = "SELECT id_pedido, data_criacao,valor_sem_taxa, taxa_servico,estado_pagamento,id_pag FROM pedido,pagamento WHERE id_pedido=pedido_id AND estatuto='Pago' AND pagamento.empresa_id=? ",
		      nativeQuery = true
		   )
		   List<List> Facturacaoesp(Long idEmpresa);
	  
	  
}