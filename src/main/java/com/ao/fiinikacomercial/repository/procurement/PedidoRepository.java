package com.ao.fiinikacomercial.repository.procurement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ao.fiinikacomercial.model.procurement.Pedido;





public interface PedidoRepository extends JpaRepository<Pedido,Long> {

	
	  @Query(
		      value = "SELECT id_pedido,nome_pedido,descricao,data_inicio_concurso,data_fecho_concurso,estado_proposta,id_proposta FROM proposta,pedido WHERE pedido.id_pedido=idconcu AND empresa_proposta_id=?",
		      nativeQuery = true
		   )
		   List<List> veresp(Long emprresaidfk);
	
	
	  @Query(
		      value = "SELECT estado FROM pedido WHERE empresa_idfk=? GROUP BY estado;",
		      nativeQuery = true
		   )
		   List<List> estadoesp(Long emprresaidfk);
	  
	  
	  @Query(
		      value = "SELECT MONTH(data_criacao),data_criacao FROM pedido WHERE empresa_idfk=? GROUP BY MONTH(data_criacao)",
		      nativeQuery = true
		   )
		   List<List> mesesp(Long emprresaidfk);
	
	  @Query(
		      value = "SELECT YEAR(data_criacao) FROM pedido WHERE empresa_idfk=? GROUP BY YEAR(data_criacao)",
		      nativeQuery = true
		   )
		   List<List> anoesp(Long emprresaidfk);
	
	  
	  
	  @Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND YEAR(data_criacao) = :ano AND MONTH(data_criacao) = :mes AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodos(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("mes") String mes, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search%",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricao(@Param("empresa_id") Long empresa_id, @Param("search") String search);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND YEAR(data_criacao) = :ano",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricaoAno1(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND YEAR(data_criacao) = :ano AND MONTH(data_criacao) = :mes",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricaoAnoMes(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("mes") String mes);
			
			@Query(
				    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND  MONTH(data_criacao) = :mes",
				    nativeQuery = true
				)
				List<List> procuratodosEmpresaDescricaoMes(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("mes") String mes);

			
			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricaoEstado(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND YEAR(data_criacao) = :ano AND MONTH(data_criacao) = :mes",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaAnoMes(@Param("empresa_id") Long empresa_id, @Param("ano") String ano, @Param("mes") String mes);
			
			@Query(
				    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND YEAR(data_criacao) = :ano ",
				    nativeQuery = true
				)
				List<List> procuratodosEmpresaAno(@Param("empresa_id") Long empresa_id, @Param("ano") String ano);

			
			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND YEAR(data_criacao) = :ano AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaAnoEstado(@Param("empresa_id") Long empresa_id, @Param("ano") String ano, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND MONTH(data_criacao) = :mes AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaMesEstado(@Param("empresa_id") Long empresa_id, @Param("mes") String mes, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND YEAR(data_criacao) = :ano AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricaoAnoEstado(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND MONTH(data_criacao) = :mes AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricaoMesEstado(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("mes") String mes, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND YEAR(data_criacao) = :ano AND MONTH(data_criacao) = :mes AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaAnoMesEstado(@Param("empresa_id") Long empresa_id, @Param("ano") String ano, @Param("mes") String mes, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND nome_pedido LIKE %:search% AND YEAR(data_criacao) = :ano",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaDescricaoAno(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND estado_proposta = :estado_proposta",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaEstado(@Param("empresa_id") Long empresa_id, @Param("estado_proposta") String estado_proposta);

			@Query(
			    value = "SELECT id_pedido, nome_pedido, descricao, data_inicio_concurso, data_fecho_concurso, estado_proposta, data_criacao FROM proposta, pedido WHERE pedido.id_pedido = idconcu AND empresa_proposta_id = :empresa_id AND MONTH(data_criacao) = :mes",
			    nativeQuery = true
			)
			List<List> procuratodosEmpresaMes(@Param("empresa_id") Long empresa_id, @Param("mes") String mes);

	  
	  
	  
	  
	  
	  
	  
	  @Query(
		      value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu ),estado,nome_pedido FROM pedido WHERE empresa_idfk=?",
		      nativeQuery = true
		   )
		   List<List> meusPedidos(Long emprresaidfk);
	  
	  
	  @Query(
		      value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu ),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search%  AND (YEAR(data_criacao)=:ano OR :ano IS NULL ) AND (MONTH(data_criacao)=:mes OR :mes IS NULL ) AND (estado=:estado OR :estado IS NULL ) ",
		      nativeQuery = true
		   )
		   List<Pedido> proctodos1(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("mes") String mes, @Param("estado") String estado);
	  
	
	  
	  @Query(
		      value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu ),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND YEAR(data_criacao)=:ano AND MONTH(data_criacao)=:mes AND estado=:estado ",
		      nativeQuery = true
		   )
		   List<List> proctodos(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("mes") String mes, @Param("estado") String estado);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricaoEstado(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("estado") String estado);
	  
	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND YEAR(data_criacao)=:ano",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricaoAno(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano);

	  
	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND MONTH(data_criacao)=:mes",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricaoMes(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("mes") String mes);

	  
	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search%",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricao(@Param("empresa_id") Long empresa_id, @Param("search") String search);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND YEAR(data_criacao)=:ano AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaAnoEstado(@Param("empresa_id") Long empresa_id, @Param("ano") String ano, @Param("estado") String estado);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND MONTH(data_criacao)=:mes AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaMesEstado(@Param("empresa_id") Long empresa_id, @Param("mes") String mes, @Param("estado") String estado);

	  
	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaEstado(@Param("empresa_id") Long empresa_id, @Param("estado") String estado);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND YEAR(data_criacao)=:ano",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaAno(@Param("empresa_id") Long empresa_id, @Param("ano") String ano);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado,nome_pedido FROM pedido WHERE empresa_idfk=:empresa_id AND MONTH(data_criacao)=:mes",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaMes(@Param("empresa_id") Long empresa_id, @Param("mes") String mes);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu) AS proposta_count,estado FROM pedido WHERE empresa_idfk=:empresa_id AND (SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu) > 0",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaComPropostas(@Param("empresa_id") Long empresa_id);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado FROM pedido WHERE empresa_idfk=:empresa_id AND YEAR(data_criacao)=:ano AND MONTH(data_criacao)=:mes",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaAnoMes(@Param("empresa_id") Long empresa_id, @Param("ano") String ano, @Param("mes") String mes);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND YEAR(data_criacao)=:ano AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricaoAnoEstado(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("estado") String estado);
	  
	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND MONTH(data_criacao)=:mes",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaMesDescricao(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("mes") String mes);
	  
	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND YEAR(data_criacao)=:ano AND MONTH(data_criacao)=:mes",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricaoAnoMes(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("ano") String ano, @Param("mes") String mes);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado FROM pedido WHERE empresa_idfk=:empresa_id AND YEAR(data_criacao)=:ano AND MONTH(data_criacao)=:mes AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaAnoMesEstado(@Param("empresa_id") Long empresa_id, @Param("ano") String ano, @Param("mes") String mes, @Param("estado") String estado);

	  @Query(
			    value = "SELECT id_pedido,descricao,data_criacao,(SELECT COUNT(*) FROM proposta WHERE pedido.id_pedido=proposta.idconcu),estado FROM pedido WHERE empresa_idfk=:empresa_id AND descricao LIKE %:search% AND MONTH(data_criacao)=:mes AND estado=:estado",
			    nativeQuery = true
			)
			List<List> proctodosEmpresaDescricaoMesEstado(@Param("empresa_id") Long empresa_id, @Param("search") String search, @Param("mes") String mes, @Param("estado") String estado);

	  
	  @Query(
		      value = "SELECT id_sector,sector FROM sectores,pedido_sector WHERE id_sector=pedido_sector.sector_idfk AND pedido_sector.pedido_idfk=?",
		      nativeQuery = true
		   )
		   List<List> sectoresdopedido(Long pedidoId);
	  
	  @Query(
		      value = "SELECT id_pedido,descricao,data_criacao,data_inicio_concurso,data_fecho_concurso,estado, empresa.id_empresa,empresa.nome_empresa,proposta.estado_proposta,id_proposta,endereco FROM pedido, empresa,proposta WHERE empresa_idfk=id_empresa AND proposta.idconcu=pedido.id_pedido AND id_pedido=? AND id_proposta=?",
		      nativeQuery = true
		   )
		   List<List> pedidoresultado(Long pedidoId, Long idproposta);
	  	
	  
	  @Query(
		      value = "SELECT id_pedido,estado,data_criacao,data_inicio_concurso,data_fecho_concurso,descricao,empresa.nome_empresa,empresa.endereco FROM pedido,empresa WHERE empresa_idfk=id_empresa AND id_pedido= ?;",
		      nativeQuery = true
		   )
		   List<List> pedidoesp(Long pedidoId);
	  
	  
//	  @Query(
//		      value = "SELECT id_pedido,data_criacao,descricao,data_inicio_concurso,data_fecho_concurso FROM pedido WHERE empresa_idfk!=?",
//		      nativeQuery = true
//		   )
//		   List<List> concursosabertos(Long empresaId);
	  
	  
	 
	  
//			  @Query(
//				      value = " SELECT p.id_pedido, p.data_criacao, p.descricao, p.data_inicio_concurso, p.data_fecho_concurso, GROUP_CONCAT(DISTINCT es.empresa_idfk SEPARATOR ', ') AS empresa_ids FROM pedido p LEFT JOIN pedido_sector ps ON p.id_pedido = ps.pedido_idfk LEFT JOIN empresa_sector es ON ps.sector_idfk = es.sector_idfk GROUP BY p.id_pedido, p.data_criacao, p.descricao, p.data_inicio_concurso, p.data_fecho_concurso HAVING FIND_IN_SET(?, empresa_ids) > 0",
//				      nativeQuery = true
//				   )
//				   List<List> concursosabertos(Long empresaId);
			  
			  
	  @Query(
		      value = "SELECT DISTINCT(id_empresa), p.id_pedido, p.data_criacao, p.descricao, p.data_inicio_concurso, p.data_fecho_concurso,(SELECT COUNT(*) FROM `proposta` WHERE empresa_proposta_id=id_empresa AND idconcu=id_pedido) AS f,nome_pedido FROM empresa,empresa_sector es,pedido p,pedido_sector ps WHERE es.empresa_idfk = id_empresa and p.empresa_idfk != id_empresa and pedido_idfk = id_pedido and ps.sector_idfk = es.sector_idfk and id_empresa = ?  HAVING f < 1",
		      nativeQuery = true
		   )
		   List<List> concursosabertos(Long empresaId);
	  
	  
	  @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE pedido SET estado='Propostas'  WHERE estado='Pendente' AND id_pedido=?",
	      nativeQuery = true
	   )
	   int actualizaEstado( Long id);
	  
	  @Transactional
	   @Modifying
	   @Query(
	      value = "UPDATE pedido SET estado='Pago'  WHERE estado='Propostas' AND id_pedido=?",
	      nativeQuery = true
	   )
	   int actualizaPago ( Long id);
	  
	  
//	  @Modifying
//	   @Query(
//	      value = "UPDATE pedido SET estado=? WHERE id_pedido=?",
//	      nativeQuery = true
//	   )
//	   int actuapag(String estatuto, Long referencia);
	  
	  
	  
	  @Query(
		      value = "SELECT DISTINCT(id_empresa), email FROM empresa,empresa_sector es,pedido p,pedido_sector ps WHERE es.empresa_idfk = id_empresa and p.empresa_idfk != id_empresa and pedido_idfk = id_pedido and ps.sector_idfk = es.sector_idfk and id_pedido = ?",
		      nativeQuery = true
		   )
		   List<List> EmpresasAfectesAoPedoido(Long empresaId);
}
