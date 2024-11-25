package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Despesa;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.repository.*;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;


@Controller
public class DespesaController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
 


//-----------------------------------------------------------------------	 
	// View Despesas
	 @RequestMapping("/fiinika/despesas/todas-despesas")
	   public String Despesas(Model model, HttpSession session,HttpServletResponse response) throws ParseException, IOException{
		
	   		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
		   
		    
		    model.addAttribute("titulo","Todas despesas");
		    model.addAttribute("despesas",R.despesaRepository.findAll(user.getIdEmpresaUser(),objFunc.AnoActual(),objFunc.MesActual()));
		    model.addAttribute("fornecedores",R.fornecedorRepository.findById_empresa(user.getIdEmpresaUser()));
		    model.addAttribute("anos",FiltraAno(user.getIdEmpresaUser()));
		    model.addAttribute("meses",FiltraMes(objFunc.AnoActual(),user.getIdEmpresaUser()));		   
		    
		  //  R.Role(model,session,response);
			return "fiinika/despesas/todas-despesas";
			
			}else {
				return "redirect:/index";
			}
	    }
	 
	 
	 // View Criar Despesa
	 @RequestMapping("/fiinika/despesas/criar-despesa")
	   public String CriarDespesa(Model model, HttpSession session, HttpServletResponse response) throws ParseException, IOException{
			 
		  		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
			 
			 model.addAttribute("titulo","Criar despesa | fiinikaConta");
			 model.addAttribute("activo4","activo");
			 model.addAttribute("logado",session.getAttribute("userLogado"));
			 model.addAttribute("fornecedores",R.fornecedorRepository.findById_empresa(user.getIdEmpresaUser()));
			 model.addAttribute("contas",R.contaRepository.findById_empresa(user.getIdEmpresaUser()));
			 
			//R.ver(objFunc.CodigoDocumentos(R,"DP")[1]);
			//R.Role(model,session,response);
			return "fiinika/despesas/criar-despesa";
			}else {
				return "redirect:/index";
			}
	    }
	 
		// View Despesa Individual
	  @RequestMapping(value="/fiinika/despesas/despesa-page/{id}", method=RequestMethod.GET)
	   public String DespesaINdividual(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		  		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
		
			long id_decod = objFunc.Decodifica(id);
			model.addAttribute("titulo","Despesa page");
			model.addAttribute("despesa",R.despesaRepository.findById(id_decod));
			model.addAttribute("id",id);
			
			//R.Role(model,session,response);
			return "fiinika/despesas/despesa-page";
			}else {
				return "redirect:/index";
			}
	    }
	 
	//=================================== Salva dados Despesa ============================
	 @RequestMapping(value="/despesas/criar", method=RequestMethod.POST)
	 public ResponseEntity<String> SalvaDespesa(HttpServletRequest request,@Valid Despesa despesa,HttpSession session,
			 Model model,@RequestParam("custo") String custo,			
			 @RequestParam(value="img_despesa",required=false) MultipartFile file_despesa,
			 RedirectAttributes attributes,HttpServletResponse response) throws IOException, ParseException{
		 
			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
		 
		    despesa.setCusto_despesa(objFunc.RemovePV(custo));
		    despesa.setData_despesa(objFunc.DataActual());
		    despesa.setIncrement(Integer.parseInt(objFunc.CodigoDocumentos(R,"DP",user.getIdEmpresaUser())[0])); // incrementa a variavel
		    despesa.setCodigo_despesa("DP "+objFunc.CodigoDocumentos(R,"DP",user.getIdEmpresaUser())[1]);// Gera o codigo
		    despesa.setCriado_por(user.getUsername());
		    despesa.setIdEmpresafk(user.getIdEmpresaUser());
		    despesa.setIdCriadorfk(user.getId_usuario());		    
		   
		   if (file_despesa  != null ) {
		    despesa.setFoto_despesa(objFunc.singleFileUpload(request,file_despesa,"despesas"));
		    }else {
		    	despesa.setFoto_despesa("doc_default_despesa.png");
		    }
		   
			 R.despesaRepository.save(despesa);
			 attributes.addFlashAttribute("mensagem", "Despesa adicionada com sucesso!");
			//----LOg
			// R.SaveLog("Criou despesa nº "+despesa.getCodigo_despesa(),2);
			 return ResponseEntity.ok().body("Sucesso");
			 
	 	}else {
			//response.sendRedirect("/index");
			return ResponseEntity.ok().body("logout");
		}
	 
		}
	 
	 
	 public Object[]  FiltraMes(String ano,long id_empresa) {
		 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
	 	 List<Object> Listmes_aux= new ArrayList<>(); 
	 	 String controle="0";
	 
			 for(Despesa desp: R.despesaRepository.findIdDespesaEmpresa(id_empresa,ano)) {							
				 Listmeses.add(desp.getData_despesa().substring(5,7));
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
	 
			 for(Despesa desp: R.despesaRepository.findAll(id_empresa)) {							
				 Listmeses.add(desp.getData_despesa().substring(0,4));
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
	 
	 
	//---------------------------------- AJAX FILTRO Despesas
	 
	 @RequestMapping(value = "/ajax/docs_despesas", method = RequestMethod.GET)
	 public @ResponseBody List<List> findAll(@RequestParam(value = "ano", required = false) String ano,
			 								 @RequestParam(value = "mes", required = false) String mes,
			 								 @RequestParam(value = "tipo", required = false) String tipo, HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
		 
		 List<List> documentos = new ArrayList<>();
		 List Ids_codificado = new ArrayList<> ();
		// List documentos = new ArrayList<> ();
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
// System.out.print(objSessao);
   if(objSessao!=null) {
		userDetails = (UserDetails) objSessao;
	    String []  dadosUser= userDetails.getUsername().split("_");
	    long id_user = Long.parseLong(dadosUser[0]);
		Usuario user = R.userRepository.findById(id_user).orElse(null);
		authenticatedUser.Dados(model, session, response);
		
		 long id_fornecedor = 0;
		 
		 if(!tipo.equals(""))
			 id_fornecedor = Long.parseLong(tipo);
		
	 //Pesquisa os tres
			if(!ano.equals("") && !mes.equals("") && !tipo.equals("")) {
			//R._facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
				return Documemtos(R.despesaRepository.DespesasForenecedorSearch(user.getIdEmpresaUser(),ano,mes,id_fornecedor),documentos);	 
			 }
			
			// Pesquisa ano e mes
			 if(!ano.equals("") && !mes.equals("") && tipo.equals("")) {
				 return Documemtos(R.despesaRepository.DespesasAnoMes(user.getIdEmpresaUser(),ano,mes),documentos);	 
			 }
			 // pesquisa ano e tipo
			 if(!ano.equals("") && mes.equals("") && !tipo.equals("")) {
				 return Documemtos(R.despesaRepository.DespesasAnoForenecedor(user.getIdEmpresaUser(),ano,id_fornecedor),documentos);	 
			 }
			 // pesquisa ano
			 if(!ano.equals("") && mes.equals("") && tipo.equals("")) {
				 return Documemtos(R.despesaRepository.DespesasAno(user.getIdEmpresaUser(),ano),documentos);
			 }
			 // pesquisa mes e tipo
			 if(ano.equals("") && !mes.equals("") && !tipo.equals("")) {
				 return Documemtos(R.despesaRepository.DespesasMesForenecedor(user.getIdEmpresaUser(),mes,id_fornecedor),documentos);
			 }
			 // pesquisa mes
			 if(ano.equals("") && !mes.equals("") && tipo.equals("")) {
				 return Documemtos(R.despesaRepository.DespesasMes(user.getIdEmpresaUser(),mes),documentos);
			 }
			 // pesquisa tipo
			 if(ano.equals("") && mes.equals("") && !tipo.equals("")) {
				 return Documemtos(R.despesaRepository.DespesasForenecedor(user.getIdEmpresaUser(),id_fornecedor),documentos);
			 }
				// List produto = (List) R._produtoRepository.findProductoforCategoria2(id_categoria);
				 
			     return null;
   }else {
	   return null;
   }
			 }	
			 
			 
			 //========= Dinamica nome
			 @RequestMapping(value = "/ajax/docs_despesas_pesquisa", method = RequestMethod.GET)
			 public @ResponseBody List<List> findAllDinamic(@RequestParam(value = "nome", required = false) String codigo_despesa,
					 HttpSession session,Model model,HttpServletResponse response) throws IOException {				 
			
				 List<List> documentos = new ArrayList<>();
				 List Ids_codificado = new ArrayList<> ();
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
		   if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);
				 
				 if(!codigo_despesa.equals("")) {			
				       return Documemtos(R.despesaRepository.findPesquisaDinamica(user.getIdEmpresaUser(),codigo_despesa),documentos);	 
				 }
			
				 
			     return null;
		   }else { return null; }
	 }	
	
	
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
		// R.ver("d"+ListMain);
		 return ListMain;
		 }


}
