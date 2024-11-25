package com.ao.fiinikacomercial.repository.procurement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.procurement.Proposta;



public interface PropostaRepository extends JpaRepository<Proposta,Long> {
	
	 @Query(
		      value = "SELECT produto.id_produto,foto_produto,nome_produto,desc_proposta.preco,qtd, desc_proposta.subtotal,desc_proposta.taxa_prod, desc_proposta.estadodp, desc_proposta.iddp FROM produto,desc_proposta WHERE produto.id_produto=desc_proposta.id_produto AND desc_proposta.concurso_id=? AND desc_proposta.empresa_prop_id=?",
		      nativeQuery = true
		   )
		   List<List> propostafeitas(Long id_concurso,Long id_empresaQuePrpos);
	 		
	 @Query(
		      value = "SELECT estado_proposta FROM proposta WHERE empresa_proposta_id=? GROUP BY estado_proposta",
		      nativeQuery = true
		   )
		   List<List> propostaEstado(Long id_empresaQuePrpos);
	 
	 @Query(
		      value = "SELECT MONTH(data_proposta),data_proposta FROM proposta WHERE empresa_proposta_id=? GROUP BY MONTH(data_proposta)",
		      nativeQuery = true
		   )
		   List<List> propostaMes(Long id_empresaQuePrpos);
	 
	 
	 @Query(
		      value = "SELECT YEAR(data_proposta) FROM proposta WHERE empresa_proposta_id=? GROUP BY YEAR(data_proposta)",
		      nativeQuery = true
		   )
		   List<List> propostaaNO(Long id_empresaQuePrpos);
	 
	 
	 @Query(
		      value = "SELECT estado_proposta FROM proposta WHERE idconcu=? AND id_proposta=?",
		      nativeQuery = true
		   )
		   String estadoDaPorposta(Long id_concurso,Long id_proposta);
	 
	 @Query(
		      value = "SELECT produto.id_produto,foto_produto,nome_produto,desc_proposta.preco,qtd_stock, desc_proposta.subtotal,desc_proposta.taxa_prod, desc_proposta.estadodp, desc_proposta.iddp,desc_proposta.empresa_prop_id FROM produto,desc_proposta WHERE produto.id_produto=desc_proposta.id_produto AND desc_proposta.concurso_id=? ",
		      nativeQuery = true
		   )
		   List<List> propostadopedido(Long id_concurso);
	 	
	 @Query(
		      value = "SELECT produto.id_produto,foto_produto,nome_produto,desc_proposta.preco,qtd_stock, desc_proposta.subtotal,desc_proposta.taxa_prod, desc_proposta.estadodp, desc_proposta.iddp,desc_proposta.empresa_prop_id,qtd FROM produto,desc_proposta WHERE produto.id_produto=desc_proposta.id_produto AND estadodp='Pago' AND desc_proposta.concurso_id=? ",
		      nativeQuery = true
		   )
		   List<List> propostapaga(Long id_concurso);
	 
	 
	 @Query(
		      value = "SELECT id_proposta,estado_proposta,empresa.nome_empresa,empresa.id_empresa,idconcu,endereco FROM proposta,empresa WHERE empresa_proposta_id=empresa.id_empresa AND proposta.idconcu=? ",
		      nativeQuery = true
		   )
		   List<List> empresaspropostas(Long id_concurso);
	 
	 
	 
	   @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE proposta SET estado_proposta='Pago' WHERE id_proposta=?",
	      nativeQuery = true
	   )
	   int propo( Long id_proposta);
	 
	   @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE proposta SET estado_proposta='Não Entregue' WHERE id_proposta=?",
	      nativeQuery = true
	   )
	   int negarentrega( Long id_proposta);
	 
	   @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE proposta SET estado_proposta='Entregue' WHERE id_proposta=?",
	      nativeQuery = true
	   )
	   int confirmarentrega( Long id_proposta);
	   
	   
	   @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE proposta SET estado_proposta='Rejeitado' WHERE id_proposta=?",
	      nativeQuery = true
	   )
	   int rejeitarporposta( Long id_proposta);
	   
	   
	 
	   @Query(
	      value = "SELECT COUNT(*) FROM proposta WHERE proposta.idconcu =? AND proposta.empresa_proposta_id=?",
	      nativeQuery = true
	   )
	   int jafez( Long id_concurso,Long id_empresa);
	 
}
