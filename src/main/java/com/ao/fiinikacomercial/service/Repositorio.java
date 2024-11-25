package com.ao.fiinikacomercial.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Factura;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.facturacao.ClienteRepository;
import com.ao.fiinikacomercial.repository.facturacao.ContaRepository;
import com.ao.fiinikacomercial.repository.facturacao.DespesaRepository;
import com.ao.fiinikacomercial.repository.facturacao.EmpresaRepository;
import com.ao.fiinikacomercial.repository.facturacao.EmpresaSectorRepository;
import com.ao.fiinikacomercial.repository.facturacao.FacturaRepository;
import com.ao.fiinikacomercial.repository.facturacao.FornecedorRepository;
import com.ao.fiinikacomercial.repository.facturacao.IsencaoRepository;
import com.ao.fiinikacomercial.repository.facturacao.ItemNotaRepository;
import com.ao.fiinikacomercial.repository.facturacao.ItemRepository;
import com.ao.fiinikacomercial.repository.facturacao.NotaCreditoRepository;
import com.ao.fiinikacomercial.repository.facturacao.PacoteRepository;
import com.ao.fiinikacomercial.repository.facturacao.PasswordRestRepository;
import com.ao.fiinikacomercial.repository.facturacao.ProdutoRepository;
import com.ao.fiinikacomercial.repository.facturacao.ReciboRepository;
import com.ao.fiinikacomercial.repository.facturacao.SectoresRepository;
import com.ao.fiinikacomercial.repository.facturacao.SubscricaoRepository;
import com.ao.fiinikacomercial.repository.facturacao.userRepository;



@Service
public class Repositorio {
	
	@Autowired
    public userRepository userRepository;
	@Autowired
    public ContaRepository contaRepository;
	@Autowired
    public EmpresaRepository empresaRepository;
	@Autowired
    public SubscricaoRepository subscricaoRepository;
	@Autowired
    public PacoteRepository pacoteRepository;
	@Autowired
    public IsencaoRepository isencaoRepository;
	@Autowired
    public ClienteRepository clienteRepository;
	@Autowired
    public CategoriaRepository categoriaRepository;
	@Autowired
    public ProdutoRepository produtoRepository;
	@Autowired
    public FornecedorRepository fornecedorRepository;
	@Autowired
    public DespesaRepository despesaRepository;
	@Autowired
    public FacturaRepository facturaRepository;
	@Autowired
    public ItemRepository itemRepository;
	@Autowired
    public ReciboRepository reciboRepository;
	@Autowired
    public NotaCreditoRepository nota_creditoRepository;
	@Autowired
    public ItemNotaRepository  itemNotaRepository;
	@Autowired
    public SectoresRepository sectorRepository;
	@Autowired
    public EmpresaSectorRepository empresasectorRepository;
	@Autowired
    public PasswordRestRepository passwordRepository;
//	@Autowired
  //  public SaftRepository saftRepository;
//	@Autowired
//    public FundoRegistoRepository fundoRegistoRepository;
	
	private  Funcoes objFunc = new Funcoes();
	
	public void Acesso(HttpServletResponse response,int status) throws IOException {
		
		if(status == 0) {
			response.sendRedirect("/401");
		}
		
	}
	
	 public Object[]  FiltraMes(String ano,long id_empresa) {
		 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
	 	 List<Object> Listmes_aux= new ArrayList<>(); 
	 	 String controle="0";
	 
			 for(Factura fact: facturaRepository.findFacturasEmpresa(id_empresa,ano)) {							
				 Listmeses.add(fact.getData_emissao().substring(5,7));
				} 
			 Object[] meses = Listmeses.toArray();
				 for(int i=0; i<meses.length;i++) {	
					 if(!controle.equals((String)meses[i])) {
						 Listmes_aux.add((String) meses[i]);
				          controle= (String) meses[i];
				          
				         
					 }
				          
			} 
			Object[] mesFinal = Listmes_aux.toArray();
				return mesFinal;
		 }
	 
	 public Object[]  FiltraAno(long id_empresa) {
		 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
	 	 List<Object> Listmes_aux= new ArrayList<>(); 
	 	 String controle="0";
	 
			 for(Factura fact: facturaRepository.findAll(id_empresa)) {							
				 Listmeses.add(fact.getData_emissao().substring(0,4));
				} 
			 Object[] meses = Listmeses.toArray();
				 for(int i=0; i<meses.length;i++) {	
					 if(!controle.equals((String)meses[i])) {
						 Listmes_aux.add((String) meses[i]);
				          controle= (String) meses[i];
				         
				         
					 }
				          
			} 
			Object[] mesFinal = Listmes_aux.toArray();
				return mesFinal;
		 }
	
	// Retorna lista_docs e lista de isd codificados
			 public List<List> Documemtos(List <List> lista,List<List> ListMain) {
					
				 boolean b = false;
				 List Ids_codificado = new ArrayList<> ();
				 for(List doc: lista) {
						try {						
							Ids_codificado.add(objFunc.Codifica(Long.parseLong(doc.get(0).toString())));
							b = true;							
						} catch (Exception e) {
							
					  }							
					}
				 if(b) {
					 ListMain.add(lista);	
				     ListMain.add(Ids_codificado);					
					
					}
				
				 return ListMain;
				 }
	
	
} 

