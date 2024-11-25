package com.ao.fiinikacomercial.repository.facturacao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ao.fiinikacomercial.model.facturacao.Pacote;

public interface PacoteRepository extends CrudRepository<Pacote, Long> { 
	
	@Query(value="SELECT * from pacote ", nativeQuery = true)
	Iterable <Pacote> findAllPacotes();
	
	@Query(value="SELECT * FROM pacote WHERE  id_pacote= ? ", nativeQuery = true)
	Iterable <Pacote> findPacoteById (Long id_pacote);
	
	@Query(value="SELECT tipo FROM pacote limit 1", nativeQuery = true)
	String findPacoteTipo ();
	
	@Query(value="SELECT preco FROM pacote where id_pacote = ?", nativeQuery = true)
	float findPacotePreco (long id_pacote);
	
	@Query(value="select increment,referencia  from pacote order by id_pacote desc limit 1", nativeQuery = true)
	List <List>  findLastIncrement ();
	
	@Query(value="SELECT nome_pacote FROM pacote where id_pacote = ?", nativeQuery = true)
	String findPacoteNome (int id_pacote);
}
