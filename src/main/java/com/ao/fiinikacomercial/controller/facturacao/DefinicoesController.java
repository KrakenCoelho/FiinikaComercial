package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.text.ParseException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.EmpresaSector;
import com.ao.fiinikacomercial.model.facturacao.Subscricao;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@Controller
public class DefinicoesController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	 //----- View dados empresa para edidtar
	 @RequestMapping("/fiinika/configuracoes/todas-configuracoes")
	   public String Definicoes(Model model, HttpSession session,HttpServletResponse response)throws ParseException, IOException{
		 
		  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			// System.out.print(objSessao);
	    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
			  
			   model.addAttribute("titulo","Todas configurações");	
			   
			   /*    Busca os dias que faltam da o-pais  */
				long dias = 0;									
				List <List> dadosSub = null;
				dadosSub = R.subscricaoRepository.findSubscricaDexa("Paga",user.getIdEmpresaUser());
				for(List sub : dadosSub) {	
					// data_exp - data_pag
					 dias = objFunc.DiferencaDatas(objFunc.DataActual(), sub.get(1).toString());
					 if(dias < 0 ){
							dias = 0;
						}
				 }
				
				  model.addAttribute("dias",dias);	
				
			
		  //  R.Role(model,session,response);	
			return "fiinika/configuracoes/todas-configuracoes";
			}else {
				return "redirect:/index";
			}
	    }
	 
	
	
	 //----- View dados empresa para edidtar
	 @RequestMapping("/fiinika/configuracoes/configuracoes-gerais")
	   public String DefinicoesGerais(Model model, HttpSession session,HttpServletResponse response) throws IOException{
		 
		 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	// System.out.print(objSessao);
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
				authenticatedUser.Dados(model, session, response);	
				Optional<Empresa> oldEmpresa = R.empresaRepository.findById(user.getIdEmpresaUser());// busca dados antigos
			  
			   model.addAttribute("titulo","Configurações Gerais");	
			   model.addAttribute("empresa", R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null));
			   model.addAttribute("todos", R.sectorRepository.findAll() );
			   model.addAttribute("sectoresEmpresa", R.empresasectorRepository.findByEmpresa(oldEmpresa.get()) );
			 
			//R.Role(model,session,response);	
			return "fiinika/configuracoes/configuracoes-gerais";
			}else {
				return "redirect:/index";
			}
	    }

	     // Salva dados editados da empresa
@RequestMapping(value="/admin/edit_salvar", method=RequestMethod.POST)
public ResponseEntity<String> SalvaDadosEmpresa(HttpServletRequest request,@Valid Empresa empresa,@RequestParam("nome_foto_perfil") String nome_foto_perfil,HttpSession session,
		@RequestParam(value="img_logo",required=false) MultipartFile file_logo,
		@RequestParam(name="password",required=false) String password,
		@RequestParam(name="sector",required=false) long [] sectores,
		Model model,HttpServletResponse response) throws IOException{
	 
	
	 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
// System.out.print(objSessao);
  if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
			authenticatedUser.Dados(model, session, response);	
	   
			 Optional<Empresa> oldEmpresa = R.empresaRepository.findById(user.getIdEmpresaUser());// busca dados antigos
			 if(oldEmpresa.isPresent()){
		           Empresa emp = oldEmpresa.get();
			 
			 if (file_logo != null) {
				 if(emp.getControl_nif() == 0) {
					 empresa.setFoto_logotipo(objFunc.singleFileUpload(request,file_logo,"empresas"));		
				 }else {
					 empresa.setFoto_logotipo(objFunc.singleFileUpload(request,file_logo,"empresas",emp.getFoto_logotipo()));		
				 }
				   			   
			    }else {
			    	empresa.setFoto_logotipo(emp.getFoto_logotipo());
			    }
			    empresa.setCambioEuro(objFunc.RemovePV(empresa.getCambioEuro()));
			    empresa.setCambioDollar(objFunc.RemovePV(empresa.getCambioDollar()));
			 	empresa.setIdEmpresa(emp.getIdEmpresa());
			    Subscricao subsc = R.subscricaoRepository.findSubscricaDexaOpt("Paga",user.getIdEmpresaUser()).orElse(null);
			    int controlNif = 0;
			    if(subsc.getTipo() == 0) {
			    	controlNif = 0;			    	
			    }
			    // Mantem o nif 
			    if(subsc.getTipo() == 1  && emp.getControl_nif() == 0) {
			    	controlNif = 1;
			    	
			    }		    	
			    
			    if(subsc.getTipo() == 1 && emp.getControl_nif() == 1) {
			    	controlNif = 1;
			    	empresa.setNif(emp.getNif());	    	
			    }
			    empresa.setControl_nif(controlNif);
				System.out.println(controlNif);
			    EmpresaSector empSector = new EmpresaSector();
			    int i=0;
			    Iterable <EmpresaSector> empSec = R.empresasectorRepository.findByEmpresa(empresa);
			    if(empSec.toString().equals("[]")) {
				    if(sectores != null) {
					    for(long setor:sectores) {			    	
					    	empSector.setEmpresa(empresa);
					    	empSector.setSectorIdfk(R.sectorRepository.findById(sectores[i]).orElse(null));
					    	R.empresasectorRepository.save(empSector);			    
					    	i++;
					    	empSector = new EmpresaSector();
					    }
				    }else {
				    	 return ResponseEntity.ok().body("Sem sector");
				    }
			    }
				R.empresaRepository.save(empresa);
	

		 return ResponseEntity.ok().body("Sucesso_"+empresa.getFoto_logotipo());
	}else {
		return ResponseEntity.ok().body("false");
	}
	 
}else {
	   // response.sendRedirect("/index");
	    return ResponseEntity.ok().body("logout");
}
	 
	 
}
	

}
