package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Despesa;

public interface DespesaRepository extends CrudRepository<Despesa, Long> { 
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM fornecedor f,despesa d WHERE d.id_empresafk = ? and id_fornecedor=id_fornecedorfk and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? order by id_despesa desc", nativeQuery = true)
	List <List> findAll (long id_empresa,String ano, String mes );
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM fornecedor f,despesa d WHERE id_fornecedor=id_fornecedorfk and id_contafk = ? and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? ", nativeQuery = true)
	List <List> findByIdConta (long id_contafk,String ano, String mes );
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM fornecedor f,despesa d WHERE id_fornecedor=id_fornecedorfk and d.id_empresafk = ? and id_contafk = ? ", nativeQuery = true)
	List <List> findByIdConta (long id_empresa,long id_contafk);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_despesa,banco,tipo_despesa FROM fornecedor f,despesa d,conta WHERE id_fornecedor=id_fornecedorfk and id_conta = id_contafk and id_despesa = ? ", nativeQuery = true)
	List <List> findById (long id_despesa);
	
	@Query(value="select *  from despesa,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_despesa) = ? order by id_despesa desc", nativeQuery = true)
	Iterable <Despesa> findIdDespesaEmpresa (Long id_empresafk,String ano );
	
	@Query(value="select *  from despesa,empresa where id_empresa = id_empresafk and id_empresafk = ?", nativeQuery = true)
	Iterable <Despesa> findAll (Long id_empresafk);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa FROM fornecedor f,despesa d,empresa e WHERE e.id_empresa=d.id_empresafk and e.id_empresa=f.id_empresafk and d.id_empresafk = ? and id_fornecedor=id_fornecedorfk and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? ", nativeQuery = true)
	List <List> DespesasMesAno (Long id_empresafk, String ano, String mes );
	//id_despesa,nome_fornecedor,nome_despesa,custo_despesa
	
	@Query(value="SELECT Year(data_despesa) as ano, Month(data_despesa) as mes FROM despesa  WHERE id_empresafk = ? and id_despesa = ? ", nativeQuery = true)
	List <List> AnoMes (long id_empresa,Long id_despesa);
	
	@Query(value="select increment,codigo_despesa  from despesa,empresa where id_empresa = id_empresafk and id_empresafk = ? order by id_despesa desc limit 1", nativeQuery = true)
	List <List>  findLastIncrement (Long id_empresafk);
	
	@Query(value="select increment,codigo_despesa,YEAR(data_despesa)  from despesa where id_empresafk = ? and YEAR(data_despesa) = ? order by id_despesa desc limit 1", nativeQuery = true)
	List <List>  findLastIncrement(long id_empresa,String ano);
	
	@Query(value="select  COALESCE(sum(custo_despesa),0) from despesa,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? ", nativeQuery = true)
	String  TotalDespesas (Long id_empresafk,String ano, String mes);
	
	@Query(value="select sum(custo_despesa) from despesa,empresa where id_empresa = id_empresafk and id_empresafk = ? and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? and id_criadorfk = ?", nativeQuery = true)
	String  TotalDespesasV (Long id_empresafk,String ano, String mes,long id_criadorfk);
	
	@Query(value="SELECT data_despesa AS date,COUNT(id_despesa) AS despesas FROM despesa where id_empresafk = ? and YEAR(data_despesa) = ? and MONTH(data_despesa) = ?  GROUP BY MONTH(data_despesa)", nativeQuery = true)
	List <List> GraficoDespesas (long id_empresa,String ano,String mes);
	
	@Query(value="SELECT SUM(custo_despesa) FROM despesa where id_empresafk = ? and YEAR(data_despesa) = ? and id_fornecedorfk = ?", nativeQuery = true)
	String TotalDespesaForenecedor(long id_empresa,String ano,long id);
	
	@Query(value="SELECT SUM(custo_despesa) FROM despesa where id_empresafk = ? and id_fornecedorfk = ?", nativeQuery = true)
	String TotalDespesaForenecedor(long id_empresa,long id);
	
	@Query(value="SELECT * FROM despesa where despesa.id_empresafk = ? and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? and id_fornecedorfk = ?", nativeQuery = true)
	Iterable <Despesa> DespesasForenecedor(long id_empresa,String ano,String mes,long id);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? and id_fornecedorfk = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasForenecedorSearch(long id_empresa,String ano,String mes,long id);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and YEAR(data_despesa) = ? and MONTH(data_despesa) = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasAnoMes(long id_empresa,String ano,String mes);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and YEAR(data_despesa) = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasAno(long id_empresa,String ano);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and  MONTH(data_despesa) = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasMes(long id_empresa,String mes);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and YEAR(data_despesa) = ? and id_fornecedorfk = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasAnoForenecedor(long id_empresa, String ano,long id);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and MONTH(data_despesa) = ? and id_fornecedorfk = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasMesForenecedor(long id_empresa,String mes,long id);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and id_fornecedorfk = ? order by id_despesa desc", nativeQuery = true)
	List <List> DespesasForenecedor(long id_empresa,long id);
	
	@Query(value="SELECT id_despesa,nome_fornecedor,custo_despesa,nome_despesa,codigo_despesa,date_format(data_despesa,'%d/%m/%Y'),criado_por,foto_logotipo FROM  despesa,fornecedor where despesa.id_empresafk = ? and id_fornecedor=id_fornecedorfk and codigo_despesa  LIKE %?%  order by id_despesa desc", nativeQuery = true)
	List <List> findPesquisaDinamica (long id_empresa,String codigo_despesa);
	
	@Query(value="select *  from despesa where id_empresafk = ? and  YEAR(data_despesa) = ?", nativeQuery = true)
	Iterable <Despesa> findDespesa (long id_empresa,String ano);
	
	@Query(value="select count(id_despesa) from despesa where id_empresafk = ? and  MONTH(data_despesa) = ?", nativeQuery = true)
	int CountDespesasMes(long id_empresa,String mes);
	
	@Query(value="SELECT COALESCE(SUM(custo_despesa),0) FROM despesa where id_empresafk = ? and YEAR(data_despesa) = ?", nativeQuery = true)
	String TotalDespesa(long id_empresa,String ano);
	
	@Query(value="SELECT AVG(mensal) AS media_mensal FROM ( SELECT COALESCE(SUM(custo_despesa),0) as mensal FROM despesa where id_empresafk = ? and YEAR(data_despesa) = ? GROUP BY MONTH(data_despesa)) as Fmensal", nativeQuery = true)
	String AvgTotalDespesa(long id_empresa,String ano);
	
	@Query(value="select data_despesa  from despesa where id_empresafk = ? and  YEAR(data_despesa) = ?", nativeQuery = true)
	List <List> findData(long id_empresa,String ano);
	
	
	
	

	
	
	

}