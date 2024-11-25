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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.Factura;
import com.ao.fiinikacomercial.model.facturacao.Item;
import com.ao.fiinikacomercial.model.facturacao.Recibo;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.service.Repositorio;
import com.ao.fiinikacomercial.security.AuthenticatedUser;

@Controller
public class ReciboController {
	
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	
	
	//View  Todos produtos
 @RequestMapping(value="/fiinika/documentos/recibo/criar-recibo/{id}", method=RequestMethod.GET)
   public String FacturaIndividual(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
	
	  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
   if(objSessao!=null) {
		userDetails = (UserDetails) objSessao;
	    String []  dadosUser= userDetails.getUsername().split("_");
	    long id_user = Long.parseLong(dadosUser[0]);
		Usuario user = R.userRepository.findById(id_user).orElse(null);
	    authenticatedUser.Dados(model, session, response);	
        long id_decod = objFunc.Decodifica(id);
        Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
		
		model.addAttribute("titulo","Criar Recibo");
		model.addAttribute("factura", R.facturaRepository.findFacturaId(user.getIdEmpresaUser(),id_decod));
	    model.addAttribute("contas",R.contaRepository.findById_empresa(user.getIdEmpresaUser()));
		model.addAttribute("dados",empresa);
		model.addAttribute("aux",id);
	
		return "fiinika/documentos/recibo/criar-recibo";
		}else {
			return "redirect:/index";
		}
    }
 
 //------------------------------------ Salva dados Recibo -----------------------------
 @RequestMapping(value="/recibo/criar", method=RequestMethod.POST)
 public ResponseEntity<String> SalvaRecibo(HttpServletRequest request,@Valid Recibo recibo,HttpSession session,Model model,
		 @RequestParam(value="img_recibo",required=false) MultipartFile file_recibo,
		 @RequestParam(value="percentagem",required=false) String perc, @RequestParam("aux") String idFac,RedirectAttributes attributes,HttpServletResponse response) throws IOException, ParseException{
  
	  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
  if(objSessao!=null) {
		userDetails = (UserDetails) objSessao;
	    String []  dadosUser= userDetails.getUsername().split("_");
	    long id_user = Long.parseLong(dadosUser[0]);
		Usuario user = R.userRepository.findById(id_user).orElse(null);
	    authenticatedUser.Dados(model, session, response);
	 
        Optional <Factura> oldFactura = R.facturaRepository.findById(objFunc.Decodifica(idFac));
        Factura factura = oldFactura.get();
        
        String [] increment_codigo = objFunc.CodigoDocumentos(R,"RC",user.getIdEmpresaUser());
        
        recibo.setCodigo_recibo("RC "+increment_codigo[1]);
        recibo.setData_recibo(objFunc.DataActual());
        recibo.setData_pagamento(objFunc.AnoMesDia(recibo.getData_pagamento()));
        recibo.setData_hora_recibo(objFunc.DataTimeActual2().replace(" ", "T"));
        recibo.setIncrement_rec(Long.parseLong(increment_codigo[0]));
        recibo.setTaxa_recibo(factura.getTaxa());
        recibo.setTotal_recibo(factura.getTotal());
        recibo.setVendedor_recibo(user.getUsername());  
        recibo.setId_facturafk(objFunc.Decodifica(idFac));  
        recibo.setPercentagem("100%");  
        recibo.setId_vendedorfk(user.getId_usuario());
        recibo.setId_empresa_fkR(user.getIdEmpresaUser());
     
       
        if(recibo.getRetencao().equals("Sim")) {
            double r = 0.065* Double.parseDouble(recibo.getValor_pago());
            recibo.setRetencao(r+"");
        }else {
            recibo.setRetencao("00.00");
        }
    
    
       if (file_recibo!=null) {
        recibo.setFoto_recibo(objFunc.singleFileUpload(request,file_recibo,"recibos"));
        }
      
            R.reciboRepository.save(recibo);
            R.reciboRepository.updateCode(objFunc.Codifica(recibo.getId_recibo()), recibo.getId_recibo());
        
            int perc_old = Integer.parseInt(factura.getEstado().split("%")[0]);
           // int perc_new = Integer.parseInt(perc.trim().split("%")[0]);
           // int perc_act=perc_old+perc_new;
            factura.setEstado("100"+"%");
    
            R.facturaRepository.save(factura);
            
        attributes.addFlashAttribute("mensagem", "Recibo adicionado com sucesso!");
        
         //----LOg
		// R.SaveLog("Criou recibo nº "+recibo.getCodigo_recibo(),2);
        
        return ResponseEntity.ok().body("Sucesso||"+objFunc.Codifica(recibo.getId_recibo()));
        
	 }else {
			//response.sendRedirect("/index");
			return ResponseEntity.ok().body("logout");
		}
    }
 
	//View  Todos produtos
@RequestMapping(value="/fiinika/documentos/recibo/visualizar-recibo/{id}", method=RequestMethod.GET)
public String ReciboIndividual(@PathVariable String id,Model model, HttpSession session,HttpServletResponse response) throws IOException{
	
	  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
   if(objSessao!=null) {
		userDetails = (UserDetails) objSessao;
	    String []  dadosUser= userDetails.getUsername().split("_");
	    long id_user = Long.parseLong(dadosUser[0]);
		Usuario user = R.userRepository.findById(id_user).orElse(null);
	    authenticatedUser.Dados(model, session, response);
	    
        long id_decod = objFunc.Decodifica(id);
        Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
		
		model.addAttribute("titulo","Visualizar recibo");
	    model.addAttribute("recibo",R.reciboRepository.findReciboId(id_decod));
	    model.addAttribute("cliente",R.reciboRepository.findReciboIdCliente(id_decod));
	    model.addAttribute("contas",R.contaRepository.findById_empresa(user.getIdEmpresaUser()));
		model.addAttribute("dados",empresa);
		model.addAttribute("aux",id);
	
		return "fiinika/documentos/recibo/visualizar-recibo";
		}else {
			return "redirect:/index";
		}
 }

}
