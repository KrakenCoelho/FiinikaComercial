package com.ao.fiinikacomercial.controller.facturacao;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;

import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.model.facturacao.Empresa;
import com.ao.fiinikacomercial.model.facturacao.Factura;
import com.ao.fiinikacomercial.model.facturacao.Item;
import com.ao.fiinikacomercial.model.facturacao.ItemNotas;
import com.ao.fiinikacomercial.model.facturacao.NotaCredito;
import com.ao.fiinikacomercial.model.facturacao.Produto;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.model.facturacao.Subscricao;
import com.ao.fiinikacomercial.funcoes.*;
import com.ao.fiinikacomercial.service.Repositorio;
import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.security.AuthenticatedUser;

import antlr.Version;
import java.math.BigDecimal;

@Controller
public class FacturaController {

	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	

	    public BigDecimal addBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.add(b);
	    }

	    public BigDecimal multiplyBigDecimals(BigDecimal a, BigDecimal b) {
	        return a.multiply(b);
	    }
	    
	    
	// ----- View todos usuarios
	@RequestMapping("/fiinika/documentos/todos-documentos")
	public String TodosDocumentos(Model model, HttpSession session, HttpServletResponse response) throws IOException {

		  Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			// System.out.print(objSessao);
	    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
			
		

			model.addAttribute("titulo", "Todos documentos");
	
			switch (user.getTipo()) {
			case "Admin":
				model.addAttribute("facturas", R.facturaRepository.findAllFacturas(user.getIdEmpresaUser(),
						objFunc.AnoActual(), objFunc.MesActual()));
				model.addAttribute("recibos", R.reciboRepository.findAllRecibos(user.getIdEmpresaUser(),
						objFunc.AnoActual(), objFunc.MesActual()));
				break;

			case "Vendedor":
				model.addAttribute("facturas", R.facturaRepository.findAllFacturasV(objFunc.AnoActual(),
						objFunc.MesActual(),user.getId_usuario()));
				model.addAttribute("recibos", R.reciboRepository.findAllRecibosV(objFunc.AnoActual(),
						objFunc.MesActual(), user.getId_usuario()));

				break;

			default:
				break;
			}

			model.addAttribute("tipo_doc", new String[] { "PP", "FT", "FR", "NC", "RC" });
			model.addAttribute("link", new String[] { "/fiinika/documentos/proforma/visualizar-proforma/",
					"/fiinika/documentos/factura/visualizar-factura/", "/fiinika/documentos/factura/visualizar-factura/",
					"/fiinika/documentos/nota-de-credito/visualizar-nota-de-credito/", "--" });

			model.addAttribute("leng", new Integer[] { 1, 2, 3, 4, 5 });
			model.addAttribute("anos", R.FiltraAno(user.getIdEmpresaUser()));
			model.addAttribute("meses", R.FiltraMes(objFunc.AnoActual(),user.getIdEmpresaUser()));
			
			
			return "/fiinika/documentos/todos-documentos";
		} else {
			return "redirect:/index";
		}
	}

	// ----- View todos usuarios
	@RequestMapping("/fiinika/documentos/factura/criar-factura")
	public String NovaFcactura(Model model, HttpSession session,HttpServletResponse response) throws IOException {

	Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			// System.out.print(objSessao);
	    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
		    
		

			model.addAttribute("titulo", "Criar Factura");
			model.addAttribute("clientes", R.clienteRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("categorias", R.categoriaRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("produtos", R.produtoRepository.findProductoEmpresa(user.getIdEmpresaUser()));
			model.addAttribute("contas", R.contaRepository.findById_empresa(user.getIdEmpresaUser()));

			model.addAttribute("n_factura", "FT " + objFunc.CodigoDocumentos(R, "FT",user.getIdEmpresaUser())[1]);

			return "fiinika/documentos/factura/criar-factura";
		} else {
			return "redirect:/index";
		}
	}

// =========================== SALVA DADOS FACTURA ===================
	// -------------------------------------- Salva dados Factura
	// ---------------------------------------------------------
	@Transactional
	@RequestMapping(value = "/factura/criar-factura", method = RequestMethod.POST)
	public ResponseEntity<String> SalvaFactura(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("id_cliente") Long id_cliente,
			@RequestParam(value = "codigo_factura", required = false) String codigo,
			@RequestParam("id_contafk") long id_contafk, @RequestParam("ref_cat") String[] ref_cat,
			@RequestParam("id_categoria") Long[] id_categoria, @RequestParam("id_produto") Long[] id_produto,
			@RequestParam("qtd") int[] qtd, @RequestParam("preco") String[] preco,
			@RequestParam("subtotal") String[] subtotal, @RequestParam("imposto_retido_") String impos_retido,
			@RequestParam("qtdLinhaMain") int num_itens, @RequestParam("desconto") String desconto,
			@RequestParam(value = "imposto", required = false) Float imposto, @RequestParam("total") String total,
			@RequestParam("total_final") String total_final, @RequestParam("imposto_") String taxa,
			@RequestParam("referente") String referente, @RequestParam("taxa_prod") String[] taxa_prod,
			@RequestParam("tipo_prod") String[] tipo_prod, @RequestParam("valor_desconto") String valor_desconto,
			@RequestParam(value = "obs", required = false) String obs, RedirectAttributes attributes,
			HttpServletResponse response) throws IOException, ParseException, Exception {

			Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			// System.out.print(objSessao);
	    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
			
			
			Long increment = (long) 0, id_factura = (long) 0;
			String regime = "";
			float impostoR = 0;
			String invoiceNo = "", dados_assinar = "";
			boolean control_item = true;
			// -- criar codigo de verição de dados iguais
			
			List <Object> list = VerificaStock(num_itens, id_produto, qtd, user.getIdEmpresaUser());
			if((boolean)list.get(0) == true) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list.get(1).toString());
			}			

			RSAKeyPairGenerator pair;

			int control = -1;
			control = R.facturaRepository.countFactAnoTipo(user.getIdEmpresaUser(), objFunc.AnoActual(), "FT");
			
			// Traz o codigo e o increment da factura
			String[] increment_codigo = objFunc.CodigoDocumentos(R, "FT",user.getIdEmpresaUser());

			Factura factura = new Factura();
			Item item = new Item();
			factura.setId_clientefk(id_cliente);
			factura.setData_emissao(objFunc.DataActual());
			factura.setData_expiracao(null);
			factura.setCodigo_factura("FT " + increment_codigo[1]);
			factura.setDesconto(Float.parseFloat(objFunc.RemovePV(desconto)));
			factura.setImposto(0);
			factura.setTotal(objFunc.RemovePV(total));
			factura.setTotal_final(objFunc.RemovePV(total_final));
			factura.setId_empresafk(user.getIdEmpresaUser());
			factura.setIncrement(Long.parseLong(increment_codigo[0]));
			factura.setEstado("0%");
			factura.setData_hora(objFunc.DataTimeActual2().replace(" ", "T"));
			// factura.setData_hora("2023-01-11T14:40:13");
			factura.setTaxa(objFunc.RemovePV(taxa));
			factura.setTipo("FT");
			factura.setReferente(referente);
			factura.setVendedor(user.getUsername());
			factura.setObs(obs);
			factura.setId_contafk(id_contafk);
			factura.setId_vendedorfk(user.getId_usuario());
			factura.setValor_desconto(objFunc.RemovePV(valor_desconto));
			factura.setImpos_retido(impos_retido);
				
			
			String totalHash = "";
			if(Double.parseDouble(factura.getImpos_retido()) >0) {
				BigDecimal v1 = new BigDecimal(factura.getTotal_final());
				BigDecimal v2 = new BigDecimal(factura.getImpos_retido());
				totalHash = objFunc.formatNumericValue(addBigDecimals(v1,v2).toString());
				System.out.println("Com retencao totalHash "+totalHash);
				
			}else {
				totalHash = factura.getTotal_final();
				System.out.println("Sem retencao totalHash "+totalHash);
			}
			
			
			try {
				pair = new RSAKeyPairGenerator();
				pair.readToFile(request);
				// invoiceNo = "FT
				// "+objFunc.AnoActual()+"/"+Integer.parseInt(factura.getCodigo_factura());
				invoiceNo = factura.getCodigo_factura();
				if (control == 0) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash;
				
				} else if (control >= 1) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash + ";"
							+ R.facturaRepository.findLastHash(user.getIdEmpresaUser(), "FT");
					// dados_assinar = "2023-02-09T19:20:29;FT
					// 2022/3;178800.00;"+facturaRepository.findLastHashFT(id_empresa);
				}

				// Encrypt the message
				// Assinar msg
				String signature = RSA.sign(dados_assinar, pair.getPrivateKey());
				factura.setHash_msg(signature);
				System.out.println(dados_assinar + "   " + signature + " " + signature.length());
				// Let's check the signature
				boolean isCorrect = RSA.verify(dados_assinar, signature, pair.getPublicKey());
				System.out.println("Signature correct: " + isCorrect);

			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				// ============= AQUI VERIFICA SE INSERIO QUANTAS VEZES (DUPLICADOS)
				
				Iterable <Factura> fact_ex = R.facturaRepository.findFacturaExists(user.getIdEmpresaUser(),factura.getCodigo_factura());
				 if(fact_ex.toString().equals("[]")){
				 R.facturaRepository.save(factura);				
				 }else {
				  return ResponseEntity.ok().body("falha");
				 }
				
				id_factura = R.facturaRepository.findInsertFactura(factura.getId_empresafk());
				System.out.println(id_factura+" "+user.getIdEmpresaUser());
				R.facturaRepository.updateCode(objFunc.Codifica(id_factura), id_factura);
	
				List<Produto> produtosList = new ArrayList<>(); 			
				 for(int i = 0; i < num_itens; i++) {
				
						item.setRef_cat(ref_cat[i]);
						item.setId_categoriafk(id_categoria[i]);
						item.setId_produtofk(id_produto[i]);
						item.setQtd(qtd[i]);					
						item.setPreco(objFunc.RemovePV(preco[i]));
						item.setSubtotal(objFunc.RemovePV(subtotal[i]));
						item.setId_facturafk(id_factura);
						item.setTaxa_prod_item(taxa_prod[i]);
						item.setTipo_item(tipo_prod[i]);
						item.setIdEmpresafork(user.getIdEmpresaUser());
	
						for (List prod_ise : R.produtoRepository.findProdIsencao(id_produto[i])) {
							item.setDescricao_prod(prod_ise.get(0).toString());
							item.setCodigo_ise(prod_ise.get(1).toString());
							item.setMotivo_ise(prod_ise.get(2).toString());
						}
						Produto prod = R.produtoRepository.findProductoEmpresaId(user.getIdEmpresaUser(), id_produto[i]).orElse(null);
						if(prod.getTipo_produto().equals("Produto")) {
							prod.setQtdStock(prod.getQtdStock()-qtd[i]);
							produtosList.add(prod);
						}
						
						R.itemRepository.save(item);
						
						item = new Item();					
					control_item = false;
				}
				 int batchSize = num_itens; // Tamanho do lote
		         for (int i = 0; i < produtosList.size(); i += batchSize) {
		  	         List<Produto> batchListProdutos= produtosList.subList(i, Math.min(i + batchSize, produtosList.size())); 
			         R.produtoRepository.saveAll(batchListProdutos);
		         } 
		         
			} catch (Exception e) {
				System.out.println(e);
				R.facturaRepository.delete(factura);				
				return ResponseEntity.ok().body("falha");
			}
			
			if(control_item) {
				R.facturaRepository.delete(factura);
				return ResponseEntity.ok().body("falha");
			}
			attributes.addFlashAttribute("mensagem", "Factura criada com sucesso!");

			// ----LOg
		//	R.SaveLog("Criou factura nº " + factura.getCodigo_factura(), 2);

		
			
			return ResponseEntity.ok().body("Sucesso||" + objFunc.Codifica(id_factura));
		} else {

			// response.sendRedirect("/index");
			return ResponseEntity.ok().body("falha");
		}
	}

	// View Factura Individual
	@RequestMapping(value = "/fiinika/documentos/factura/visualizar-factura/{id}", method = RequestMethod.GET)
	public String FacturaIndividual(@PathVariable String id, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
		// System.out.print(objSessao);
    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);	
	    
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
			long id_decod = objFunc.Decodifica(id);
			System.out.println(id_decod);
			model.addAttribute("titulo", "Vizualizar Factura");
			model.addAttribute("factura", R.facturaRepository.findFacturaId(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));
			Iterable<Item> items = R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod);
			
			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			// model.addAttribute("produtos",R.produtoRepository.findProductoEmpresa(id_empresa));
			model.addAttribute("aux", id);
			model.addAttribute("back", "javascript:history.back()");

			return "/fiinika/documentos/factura/visualizar-factura";
		} else {
			return "redirect:/index";
		}
	}

	// View Factura Individual
	@RequestMapping(value = "/fiinika/documentos/factura/visualizar-factura/{id}/{cod}", method = RequestMethod.GET)
	public String FacturaIndividual2(@PathVariable String id, @PathVariable String cod, Model model,
			HttpSession session, HttpServletResponse response) throws IOException {
		
		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);	
		
			long id_decod = objFunc.Decodifica(id);
			
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);

			model.addAttribute("titulo", "Vizualizar Factura");
			model.addAttribute("factura", R.facturaRepository.findFacturaId(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));
			Iterable<Item> items = R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod);

			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			model.addAttribute("back", "/fiinika/documentos/todos-documentos");

			model.addAttribute("aux", id);

			return "/fiinika/documentos/factura/visualizar-factura";
		} else {
			return "redirect:/index";
		}
	}

	// ==================================================== PROFORMA
	// ===============================

	// ----- View todos usuarios
	@RequestMapping("/fiinika/documentos/proforma/criar-proforma")
	public String NovaFacturaProforma(Model model, HttpSession session, HttpServletResponse response) throws IOException {

		
		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);	

			model.addAttribute("titulo", "Criar Factura");
			model.addAttribute("clientes", R.clienteRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("categorias", R.categoriaRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("produtos", R.produtoRepository.findProductoEmpresa(user.getIdEmpresaUser()));
			model.addAttribute("contas", R.contaRepository.findById_empresa(user.getIdEmpresaUser()));

			model.addAttribute("n_factura", "PP " + objFunc.CodigoDocumentos(R, "PP",user.getIdEmpresaUser())[1]);

			// R.ver(objFunc.CodigoDocumentos(R,"PP")[1]);

			return "fiinika/documentos/proforma/criar-proforma";
		} else {
			return "redirect:/index";
		}
	}

	// -------------------------------------- Salva dados Factura Proforma
	// ---------------------------------------------------------
	@Transactional
	@RequestMapping(value = "/factura/criar-proforma", method = RequestMethod.POST)
	public ResponseEntity<String> SalvaFacturaProforma(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("id_cliente") Long id_cliente,
			@RequestParam(value = "codigo_factura", required = false) String codigo,
			@RequestParam("id_contafk") long id_contafk, @RequestParam("data_exp") String data_exp,
			@RequestParam("ref_cat") String[] ref_cat, @RequestParam("id_categoria") Long[] id_categoria,
			@RequestParam("id_produto") Long[] id_produto, @RequestParam("qtd") int[] qtd,
			@RequestParam("preco") String[] preco, @RequestParam("subtotal") String[] subtotal,
			@RequestParam("imposto_retido_") String impos_retido, @RequestParam("qtdLinhaMain") int num_itens,
			@RequestParam("desconto") String desconto, @RequestParam(value = "imposto", required = false) Float imposto,
			@RequestParam("total") String total, @RequestParam("taxa_prod") String[] taxa_prod,
			@RequestParam("total_final") String total_final, @RequestParam("valor_desconto") String valor_desconto,
			@RequestParam("tipo_prod") String[] tipo_prod, @RequestParam(value = "obs", required = false) String obs,
			@RequestParam("imposto_") String taxa, RedirectAttributes attributes,HttpServletResponse response)
			throws IOException, ParseException, Exception {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);	

			Long increment = (long) 0, id_factura = (long) 0;
			String regime = "";
			float impostoR = 0;
			String invoiceNo = "", dados_assinar = "";
			boolean control_item = true;
			// -- criar codigo de verição de dados iguais
			
			List <Object> list = VerificaStock(num_itens, id_produto, qtd, user.getIdEmpresaUser());
			if((boolean)list.get(0) == true) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list.get(1).toString());
			}

			RSAKeyPairGenerator pair;

			int control = -1;
			control = R.facturaRepository.countFactAnoTipo(user.getIdEmpresaUser(), objFunc.AnoActual(), "PP");

			if (regime.equals("Geral")) {
				impostoR = 14;
			}

			// Traz o codigo e o increment da factura
			String[] increment_codigo = objFunc.CodigoDocumentos(R, "PP",user.getIdEmpresaUser());

			Factura factura = new Factura();
			Item item = new Item();
			factura.setId_clientefk(id_cliente);
			factura.setData_emissao(objFunc.DataActual());
			//factura.setData_emissao("2023-02-09");
			factura.setData_expiracao(objFunc.FormataData(data_exp));
			//factura.setData_expiracao("2023-03-11");			
			factura.setCodigo_factura("PP " + increment_codigo[1]);
			factura.setDesconto(Float.parseFloat(objFunc.RemovePV(desconto)));
			factura.setImposto(impostoR);
			factura.setTotal(objFunc.RemovePV(total));
			factura.setTotal_final(objFunc.RemovePV(total_final));
			factura.setId_empresafk(user.getIdEmpresaUser());
			factura.setIncrement(Long.parseLong(increment_codigo[0]));
			factura.setEstado("0%");
			factura.setData_hora(objFunc.DataTimeActual2().replace(" ", "T"));
		    //factura.setData_hora("2023-02-09T19:20:29");
			factura.setTaxa(objFunc.RemovePV(taxa));
			factura.setTipo("PP");
			factura.setReferente("");
			factura.setVendedor(user.getUsername());
			factura.setObs(obs);
			factura.setId_contafk(id_contafk);
			factura.setId_vendedorfk(user.getId_usuario());
			factura.setValor_desconto(objFunc.RemovePV(valor_desconto));
			factura.setImpos_retido(impos_retido);
			
			
			String totalHash = "";
			if(Double.parseDouble(factura.getImpos_retido()) >0) {
				BigDecimal v1 = new BigDecimal(factura.getTotal_final());
				BigDecimal v2 = new BigDecimal(factura.getImpos_retido());
				totalHash = objFunc.formatNumericValue(addBigDecimals(v1,v2).toString());
				System.out.println("Com retencao totalHash "+totalHash);
				
			}else {
				totalHash = factura.getTotal_final();
				System.out.println("Sem retencao totalHash "+totalHash);
			}

			try {
				
				pair = new RSAKeyPairGenerator();
				pair.readToFile(request);
				// invoiceNo = "FT
				// "+objFunc.AnoActual()+"/"+Integer.parseInt(factura.getCodigo_factura());
				invoiceNo = factura.getCodigo_factura();
				if (control == 0) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash;
					System.out.println("Primeiro hash Factura");
				} else if (control >= 1) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash + ";"
							+ R.facturaRepository.findLastHash(user.getIdEmpresaUser(), "PP");
					// dados_assinar = "2023-02-09T19:20:29;FT
					// 2022/3;178800.00;"+facturaRepository.findLastHashFT(id_empresa);
				}
				

				// Encrypt the message
				// Assinar msg
				String signature = RSA.sign(dados_assinar, pair.getPrivateKey());
				factura.setHash_msg(signature);
				System.out.println(dados_assinar + "   " + signature + " " + signature.length());
				// Let's check the signature
				boolean isCorrect = RSA.verify(dados_assinar, signature, pair.getPublicKey());
				System.out.println("Signature correct: " + isCorrect);

			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

				// ============= AQUI VERIFICA SE INSERIO QUANTAS VEZES (DUPLICADOS)
				Iterable <Factura> fact_ex = R.facturaRepository.findFacturaExists(user.getIdEmpresaUser(),factura.getCodigo_factura());
				 if(fact_ex.toString().equals("[]")){
				 R.facturaRepository.save(factura);			
				 }else {
					  return ResponseEntity.ok().body("falha");
					 }
				 
				id_factura = R.facturaRepository.findInsertFactura(factura.getId_empresafk());
				R.facturaRepository.updateCode(objFunc.Codifica(id_factura), id_factura);
				// R.ver(num_itens+" "+ref_cat.length+" "+tipo_prod.length+"
				List prodEmp = R.produtoRepository.findIdProdEmp(user.getIdEmpresaUser());
				
				List<Produto> produtosList = new ArrayList<>(); 		
				for (int i = 0; i < num_itens; i++) {

					item.setRef_cat(ref_cat[i]);
					item.setId_categoriafk(id_categoria[i]);
					item.setId_produtofk(id_produto[i]);
					item.setQtd(qtd[i]);
					item.setPreco(objFunc.RemovePV(preco[i]));
					item.setSubtotal(objFunc.RemovePV(subtotal[i]));
					item.setId_facturafk(id_factura);
					item.setTaxa_prod_item(taxa_prod[i]);
					item.setTipo_item(tipo_prod[i]);
					item.setIdEmpresafork(user.getIdEmpresaUser());

					for (List prod_ise : R.produtoRepository.findProdIsencao(id_produto[i])) {
						item.setDescricao_prod(prod_ise.get(0).toString());
						item.setCodigo_ise(prod_ise.get(1).toString());
						item.setMotivo_ise(prod_ise.get(2).toString());
					}

					Produto prod = R.produtoRepository.findProductoEmpresaId(user.getIdEmpresaUser(), id_produto[i]).orElse(null);					
					if(prod.getTipo_produto().equals("Produto")) {
						prod.setQtdStock(prod.getQtdStock()-qtd[i]);
						produtosList.add(prod);
					}
						 R.itemRepository.save(item);
					 
					item = new Item();
					control_item = false;
				}
				
				 int batchSize = num_itens; // Tamanho do lote
		         for (int i = 0; i < produtosList.size(); i += batchSize) {
		  	         List<Produto> batchListProdutos= produtosList.subList(i, Math.min(i + batchSize, produtosList.size())); 
			         R.produtoRepository.saveAll(batchListProdutos);
		         } 
		         
			} catch (Exception e) {
				R.facturaRepository.delete(factura);			
				return ResponseEntity.ok().body("falha");

			}
			if(control_item) {
				R.facturaRepository.delete(factura);
				return ResponseEntity.ok().body("falha");
			}
			attributes.addFlashAttribute("mensagem", "Factura criada com sucesso!");

			// ----LOg
		//	R.SaveLog("Criou factura proforma nº " + factura.getCodigo_factura(), 2);

			return ResponseEntity.ok().body("Sucesso||" + objFunc.Codifica(id_factura));
		} else {
			return ResponseEntity.ok().body("logout");
		}

		

	}

	// ========================= VIEW PROFORMA INDIVIDUAL =======================0
	@RequestMapping(value = "/fiinika/documentos/proforma/visualizar-proforma/{id}", method = RequestMethod.GET)
	public String FacturaProformaIndividual(@PathVariable String id, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);

			long id_decod = objFunc.Decodifica(id);
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);

			model.addAttribute("titulo", "Vizualizar Proforma");

			List<List> factura = R.facturaRepository.findFacturaId(user.getIdEmpresaUser(), id_decod);
			model.addAttribute("factura", factura);
			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));
			Iterable<Item> items = R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod);

			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			// model.addAttribute("produtos",R.produtoRepository.findProductoEmpresa(id_empresa));
			model.addAttribute("aux", id);
			model.addAttribute("tornar", "yes");
			for (List fact1 : factura) {
				for (Factura fact2 : R.facturaRepository.findReferente(fact1.get(1).toString(),
						user.getIdEmpresaUser())) {
					model.addAttribute("tornar", "no");
				}
			}

			model.addAttribute("back", "javascript:history.back()");

			return "/fiinika/documentos/proforma/visualizar-proforma";
		} else {
			return "redirect:/index";
		}
	}

	@RequestMapping(value = "/fiinika/documentos/proforma/visualizar-proforma/{id}/{cod}", method = RequestMethod.GET)
	public String FacturaProformaIndividual2(@PathVariable String id, @PathVariable String cod, Model model,
			HttpSession session, HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);

			long id_decod = objFunc.Decodifica(id);
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);

			model.addAttribute("titulo", "Vizualizar Proforma");

			List<List> factura = R.facturaRepository.findFacturaId(user.getIdEmpresaUser(), id_decod);
			model.addAttribute("factura", factura);
			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));
			Iterable<Item> items = R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod);

			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			// model.addAttribute("produtos",R.produtoRepository.findProductoEmpresa(id_empresa));
			model.addAttribute("aux", id);
			model.addAttribute("tornar", "yes");
			for (List fact1 : factura) {
				for (Factura fact2 : R.facturaRepository.findReferente(fact1.get(1).toString(),user.getIdEmpresaUser())) {
					model.addAttribute("tornar", "no");
				}
			}

			model.addAttribute("back", "/fiinika/documentos/todos-documentos ");

			return "/fiinika/documentos/proforma/visualizar-proforma";
		} else {
			return "redirect:/index";
		}
	}

	// ========================= CRIAR FACTURA FROM PROFORMA
	// ==================================
	@RequestMapping(value = "/fiinika/documentos/factura/criar-factura-from-proforma/{id}", method = RequestMethod.GET)
	public String CriarFacturaFromProforma(@PathVariable String id, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);

			long id_decod = objFunc.Decodifica(id);
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);

			model.addAttribute("titulo", "Vizualizar Proforma");
			model.addAttribute("factura", R.facturaRepository.findFacturaId(user.getIdEmpresaUser(), id_decod));

			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));

			model.addAttribute("n_factura", "FT " + objFunc.CodigoDocumentos(R, "FT",user.getIdEmpresaUser())[1]);

			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			model.addAttribute("aux", id);

			model.addAttribute("clientes", R.clienteRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("categorias", R.categoriaRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("produtos", R.produtoRepository.findProductoEmpresa(user.getIdEmpresaUser()));
			model.addAttribute("contas", R.contaRepository.findById_empresa(user.getIdEmpresaUser()));

			return "fiinika/documentos/factura/criar-factura-from-proforma";
		} else {
			return "redirect:/index";
		}
	}

//================================================================== FACTURA RECIBO ============================================================	
	// ----- View todos usuarios
	@RequestMapping("/fiinika/documentos/factura/criar-factura-recibo")
	public String NovaFacturaRecibo(Model model, HttpSession session, HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);

			model.addAttribute("titulo", "Criar Factura/Recibo");
			model.addAttribute("clientes", R.clienteRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("categorias", R.categoriaRepository.findById_empresa(user.getIdEmpresaUser()));
			model.addAttribute("produtos", R.produtoRepository.findProductoEmpresa(user.getIdEmpresaUser()));
			model.addAttribute("contas", R.contaRepository.findById_empresa(user.getIdEmpresaUser()));

			model.addAttribute("n_factura", "FR " + objFunc.CodigoDocumentos(R, "FR",user.getIdEmpresaUser())[1]);

			return "fiinika/documentos/factura/criar-factura-recibo";
		} else {
			return "redirect:/index";
		}
	}

	// ============================================= SALVA FACTURA RECIBO
	// ===============================================
	@Transactional
	@RequestMapping(value = "/factura/criar-factura-recibo", method = RequestMethod.POST)
	public ResponseEntity<String> SalvaFacturaRecibo(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("id_cliente") Long id_cliente, @RequestParam("id_contafk") long id_contafk,
			@RequestParam("ref_cat") String[] ref_cat, @RequestParam("id_categoria") Long[] id_categoria,
			@RequestParam("id_produto") Long[] id_produto, @RequestParam("qtd") int[] qtd,
			@RequestParam("preco") String[] preco, @RequestParam("subtotal") String[] subtotal,
			@RequestParam("qtdLinhaMain") int num_itens, @RequestParam("desconto") String desconto,
			@RequestParam(value = "imposto", required = false) Float imposto,
			@RequestParam("tipo_prod") String[] tipo_prod, @RequestParam("total") String total,
			@RequestParam("total_final") String total_final, @RequestParam("imposto_") String taxa,
			@RequestParam("imposto_retido_") String impos_retido, @RequestParam("referente") String referente,
			@RequestParam(value = "obs", required = false) String obs,
			@RequestParam("valor_desconto") String valor_desconto, @RequestParam("taxa_prod") String[] taxa_prod,
			RedirectAttributes attributes,HttpServletResponse response) throws IOException, ParseException, Exception {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			
			Long increment = (long) 0, id_factura = (long) 0;
			String regime = "";
			float impostoR = 0;
			String invoiceNo = "", dados_assinar = "";
			boolean control_item = true;
			
			List <Object> list = VerificaStock(num_itens, id_produto, qtd, user.getIdEmpresaUser());
			System.out.println(list);
			if((boolean)list.get(0) == true) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list.get(1).toString());
			}	

			RSAKeyPairGenerator pair;

			int control = -1;
			control = R.facturaRepository.countFactAnoTipo(user.getIdEmpresaUser(), objFunc.AnoActual(), "FR");

			if (regime.equals("Geral")) {
				impostoR = 14;
			}

			// Traz o codigo e o increment da factura
			String[] increment_codigo = objFunc.CodigoDocumentos(R, "FR",user.getIdEmpresaUser());

			Factura factura = new Factura();
			Item item = new Item();
			factura.setId_clientefk(id_cliente);
			factura.setData_emissao(objFunc.DataActual());
			factura.setData_expiracao(null);
			factura.setCodigo_factura("FR " + increment_codigo[1]);
			factura.setDesconto(Float.parseFloat(objFunc.RemovePV(desconto)));
			factura.setImposto(impostoR);
			factura.setTotal(objFunc.RemovePV(total));
			factura.setTotal_final(objFunc.RemovePV(total_final));
			factura.setId_empresafk(user.getIdEmpresaUser());
			factura.setIncrement(Long.parseLong(increment_codigo[0]));
			factura.setEstado("100%");
			factura.setData_hora(objFunc.DataTimeActual2().replace(" ", "T"));
			factura.setTaxa(objFunc.RemovePV(taxa));
			factura.setTipo("FR");
			factura.setReferente("");
			factura.setVendedor(user.getUsername());
			factura.setObs(obs);
			factura.setId_contafk(id_contafk);
			factura.setId_vendedorfk(user.getId_usuario());
			factura.setValor_desconto(objFunc.RemovePV(valor_desconto));
			factura.setImpos_retido(impos_retido);
			
			String totalHash = "";
			if(Double.parseDouble(factura.getImpos_retido()) >0) {
				BigDecimal v1 = new BigDecimal(factura.getTotal_final());
				BigDecimal v2 = new BigDecimal(factura.getImpos_retido());
				totalHash = objFunc.formatNumericValue(addBigDecimals(v1,v2).toString());
				System.out.println("Com retencao totalHash "+totalHash);
				
			}else {
				totalHash = factura.getTotal_final();
				System.out.println("Sem retencao totalHash "+totalHash);
			}

			try {
				pair = new RSAKeyPairGenerator();
				pair.readToFile(request);
				// invoiceNo = "FR
				// "+objFunc.AnoActual()+"/"+Integer.parseInt(factura.getCodigo_factura());
				invoiceNo = factura.getCodigo_factura();
				if (control == 0) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash;
					
				} else if (control >= 1) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash + ";"
							+ R.facturaRepository.findLastHash(user.getIdEmpresaUser(), "FR");
					// dados_assinar = "2022-01-10;2022-01-10T14:03:31;FT
					// 2022/3;178800.00;"+facturaRepository.findLastHashFT(id_empresa);
				}

				// Encrypt the message
				// Assinar msg
				String signature = RSA.sign(dados_assinar, pair.getPrivateKey());
				factura.setHash_msg(signature);
				System.out.println(dados_assinar + "   " + signature + " " + signature.length());
				// Let's check the signature
				boolean isCorrect = RSA.verify(dados_assinar, signature, pair.getPublicKey());
				System.out.println("Signature correct: " + isCorrect);

			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				// ============= AQUI VERIFICA SE INSERIO QUANTAS VEZES (DUPLICADOS)
				 Iterable <Factura> fact_ex = R.facturaRepository.findFacturaExists(user.getIdEmpresaUser(),factura.getCodigo_factura());
				 if(fact_ex.toString().equals("[]")){
					 R.facturaRepository.save(factura);
				 }else {
					  return ResponseEntity.ok().body("falha");
					 }
				 
				id_factura = R.facturaRepository.findInsertFactura(factura.getId_empresafk());
				R.facturaRepository.updateCode(objFunc.Codifica(id_factura), id_factura);
				
				List prodEmp = R.produtoRepository.findIdProdEmp(user.getIdEmpresaUser());
				
				List<Produto> produtosList = new ArrayList<>(); 		
				for (int i = 0; i < num_itens; i++) {
					item.setRef_cat(ref_cat[i]);
					item.setId_categoriafk(id_categoria[i]);
					item.setId_produtofk(id_produto[i]);
					item.setQtd(qtd[i]);
					item.setPreco(objFunc.RemovePV(preco[i]));
					item.setSubtotal(objFunc.RemovePV(subtotal[i]));
					item.setId_facturafk(id_factura);
					item.setTaxa_prod_item(taxa_prod[i]);
					item.setTipo_item(tipo_prod[i]);
					item.setIdEmpresafork(user.getIdEmpresaUser());

					for (List prod_ise : R.produtoRepository.findProdIsencao(id_produto[i])) {
						item.setDescricao_prod(prod_ise.get(0).toString());
						item.setCodigo_ise(prod_ise.get(1).toString());
						item.setMotivo_ise(prod_ise.get(2).toString());
					}

					Produto prod = R.produtoRepository.findProductoEmpresaId(user.getIdEmpresaUser(), id_produto[i]).orElse(null);
					if(prod.getTipo_produto().equals("Produto")) {
						prod.setQtdStock(prod.getQtdStock()-qtd[i]);
						produtosList.add(prod);
					}
					
					
						 R.itemRepository.save(item);
					 
					item = new Item();
					control_item = false;
				}
				 int batchSize = num_itens; // Tamanho do lote
		         for (int i = 0; i < produtosList.size(); i += batchSize) {
		  	         List<Produto> batchListProdutos= produtosList.subList(i, Math.min(i + batchSize, produtosList.size())); 
			         R.produtoRepository.saveAll(batchListProdutos);
		         } 
			} catch (Exception e) {
				System.out.println(e);
				R.facturaRepository.delete(factura);
				return ResponseEntity.ok().body("falha");
			}
			if(control_item) {
				R.facturaRepository.delete(factura);
				return ResponseEntity.ok().body("falha");
			}

			attributes.addFlashAttribute("mensagem", "Factura/Recibo criada com sucesso!");
			// ----LOg
			//R.SaveLog("Criou factura/recibo nº " + factura.getCodigo_factura(), 2);

			return ResponseEntity.ok().body("Sucesso||" + objFunc.Codifica(id_factura));
		} else {
			return ResponseEntity.ok().body("logout");
		}
	}

//================================================================ NOTA DE CREDITO ====================================================0

	@RequestMapping(value = "/fiinika/documentos/nota-de-credito/criar-nota-de-credito/{id}", method = RequestMethod.GET)
	public String CriarNotaCredito(@PathVariable String id, Model model, HttpSession session,
			HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);

			long id_decod = objFunc.Decodifica(id);
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
			model.addAttribute("titulo", "Criar Nota de crédito");

			model.addAttribute("factura", R.facturaRepository.findFacturaId(user.getIdEmpresaUser(), id_decod));

			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));

			model.addAttribute("n_factura", "NC " + objFunc.CodigoDocumentos(R, "NC",user.getIdEmpresaUser())[1]);

			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			model.addAttribute("aux", id);

			model.addAttribute("clientes", R.clienteRepository.findAll());
			model.addAttribute("categorias", R.categoriaRepository.findAll());
			model.addAttribute("produtos", R.produtoRepository.findAll());
			model.addAttribute("contas", R.contaRepository.findAll());

			return "fiinika/documentos/nota-de-credito/criar-nota-de-credito";
		} else {
			return "redirect:/index";
		}
	}

	// =============================================== SALVAR NOTA DE CREDITO
	// =============================================
	@Transactional
	@RequestMapping(value = "/factura/criar-nota-credito", method = RequestMethod.POST)
	public ResponseEntity<String> SalvaNotaCredito(HttpServletRequest request, HttpSession session,
			@Valid NotaCredito notaCredito, Model model, @RequestParam("id_cliente") Long id_cliente,
			@RequestParam("ref_cat") String[] ref_cat, @RequestParam("id_categoria") Long[] id_categoria,
			@RequestParam("id_contafk") long id_contafk, @RequestParam("id_produto") Long[] id_produto,
			@RequestParam("valor_desconto") String valor_desconto, @RequestParam("qtd") int[] qtd,
			@RequestParam("preco") String[] preco, @RequestParam("subtotal") String[] subtotal,
			@RequestParam("tipo_prod") String[] tipo_prod, @RequestParam("qtdLinhaMain") int num_itens,
			@RequestParam("desconto") String desconto, @RequestParam(value = "imposto", required = false) Float imposto,
			@RequestParam("total") String total, @RequestParam("total_final") String total_final,
			@RequestParam("imposto_") String taxa, @RequestParam("taxa_prod_nota") String[] taxa_prod_nota,
			@RequestParam("id_item") Long[] id_itens_a_eliminar, @RequestParam("motivo") String motivo,
			@RequestParam("imposto_retido_") String impos_retido,
			@RequestParam("id_factura_origem") long id_factura_origem, RedirectAttributes attributes,HttpServletResponse response)
			throws IOException, ParseException, Exception {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			// ==== Caso o motivo for a Anulação ======
			if (motivo.equals("Anulação")) {
				AnulaFactura(id_factura_origem);
			}

			Long increment = (long) 0, id_factura = (long) 0;
			String regime = "";
			float impostoR = 0;
			String invoiceNo = "", dados_assinar = "";

			RSAKeyPairGenerator pair;

			int control = -1;
			control = R.facturaRepository.countFactAnoTipo(user.getIdEmpresaUser(), objFunc.AnoActual(), "NC");

			if (regime.equals("Geral")) {
				impostoR = 14;
			}

			// Traz o codigo e o increment da factura
			String[] increment_codigo = objFunc.CodigoDocumentos(R, "NC",user.getIdEmpresaUser());

			// Nota credito
			Factura factura = new Factura();
			ItemNotas itemNota = new ItemNotas();
			factura.setId_clientefk(id_cliente);
			factura.setData_emissao(objFunc.DataActual());
			factura.setData_expiracao(null);
			factura.setCodigo_factura("NC " + increment_codigo[1]);
			factura.setDesconto(Float.parseFloat(objFunc.RemovePV(desconto)));
			factura.setImposto(impostoR);
			factura.setTotal(objFunc.RemovePV(total));
			factura.setTotal_final(objFunc.RemovePV(total_final));
			factura.setId_empresafk(user.getIdEmpresaUser());
			factura.setIncrement(Long.parseLong(increment_codigo[0]));
			factura.setEstado("0%");
			factura.setData_hora(objFunc.DataTimeActual2().replace(" ", "T"));
			factura.setTaxa(objFunc.RemovePV(taxa));
			factura.setTipo("NC");
			factura.setReferente("");
			factura.setVendedor(user.getUsername());
			factura.setObs("");
			factura.setId_contafk(id_contafk);
			factura.setValor_desconto(objFunc.RemovePV(valor_desconto));
			factura.setId_vendedorfk(user.getId_usuario());
			factura.setImpos_retido(impos_retido);

			
			String totalHash = "";
			if(Double.parseDouble(factura.getImpos_retido()) >0) {
				BigDecimal v1 = new BigDecimal(factura.getTotal_final());
				BigDecimal v2 = new BigDecimal(factura.getImpos_retido());
				totalHash = objFunc.formatNumericValue(addBigDecimals(v1,v2).toString());
				System.out.println("Com retencao totalHash "+totalHash);
				
			}else {
				totalHash = factura.getTotal_final();
				System.out.println("Sem retencao totalHash "+totalHash);
			}
			
			try {
				pair = new RSAKeyPairGenerator();
				pair.readToFile(request);
				// invoiceNo = "NC
				// "+objFunc.AnoActual()+"/"+Integer.parseInt(factura.getCodigo_factura());
				invoiceNo = factura.getCodigo_factura();
				if (control == 0) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash;
					System.out.println("Primeiro hash NotaCretito");
				} else if (control >= 1) {
					dados_assinar = factura.getData_emissao() + ";" + factura.getData_hora() + ";" + invoiceNo + ";"
							+ totalHash + ";"
							+ R.facturaRepository.findLastHash(user.getIdEmpresaUser(), "NC");
					// dados_assinar = "2022-01-10;2022-01-10T14:03:31;FT
					// 2022/3;178800.00;"+facturaRepository.findLastHashFT(id_empresa);
				}

				// Encrypt the message
				// Assinar msg
				String signature = RSA.sign(dados_assinar, pair.getPrivateKey());
				factura.setHash_msg(signature);
				System.out.println(dados_assinar + "   " + signature + " " + signature.length());
				// Let's check the signature
				boolean isCorrect = RSA.verify(dados_assinar, signature, pair.getPublicKey());
				System.out.println("Signature correct: " + isCorrect);

			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				// ============= AQUI VERIFICA SE INSERIO QUANTAS VEZES (DUPLICADOS)
				Iterable <Factura> fact_ex = R.facturaRepository.findFacturaExists(user.getIdEmpresaUser(),factura.getCodigo_factura());
				 if(fact_ex.toString().equals("[]")){
				 R.facturaRepository.save(factura);
				 }else {
					  return ResponseEntity.ok().body("falha");
					 }
				 
				id_factura = R.facturaRepository.findInsertFactura(factura.getId_empresafk());
				R.facturaRepository.updateCode(objFunc.Codifica(id_factura), id_factura);

				// ---- Nota credito
				notaCredito.setId_clientefk(id_cliente);
				notaCredito.setId_facturafk(id_factura);
				notaCredito.setData_emissao(objFunc.DataActual());
				R.nota_creditoRepository.save(notaCredito);
				// ------ Nota credito
				
				List prodEmp = R.produtoRepository.findIdProdEmp(user.getIdEmpresaUser());
				for (int i = 0; i < num_itens; i++) {
					itemNota.setRef_cat(ref_cat[i]);
					itemNota.setId_categoriafk(id_categoria[i]);
					itemNota.setId_produtofk(id_produto[i]);
					itemNota.setQtd(qtd[i]);
					itemNota.setPreco(Float.parseFloat(objFunc.RemovePV(preco[i])));
					itemNota.setSubtotal(Float.parseFloat(objFunc.RemovePV(subtotal[i])));
					itemNota.setId_notafk(id_factura);
					itemNota.setTaxa_prod_nota(taxa_prod_nota[i]);
					itemNota.setTipo_item_nota(tipo_prod[i]);
					itemNota.setIdEmpresafork(user.getIdEmpresaUser());

					for (List prod_ise : R.produtoRepository.findProdIsencao(id_produto[i])) {
						itemNota.setDescricao_prod(prod_ise.get(0).toString());
						itemNota.setCodigo_ise(prod_ise.get(1).toString());
						itemNota.setMotivo_ise(prod_ise.get(2).toString());
					}

					
						 R.itemNotaRepository.save(itemNota);
					 
						 
					R.itemNotaRepository.save(itemNota);
					itemNota = new ItemNotas();
					
				}
				/*
				 * //====== CHAMA FUNCAO QUE MODIFICA OS ITENS DA FACTURA ORIGINAL
				 * ============== if(motivo.equals("Rectificação"))
				 * SalvadosNovosFactura(id_factura_origem,id_itens_a_eliminar,request);
				 */
			} catch (Exception e) {
				System.out.println(e);
				R.facturaRepository.delete(factura);
				return ResponseEntity.ok().body("falha");
			}

			attributes.addFlashAttribute("mensagem", "Nota de Crédito criada com sucesso!");

			// ----LOg
			//R.SaveLog("Criou nota de crédito nº " + factura.getCodigo_factura(), 2);

			return ResponseEntity.ok().body("Sucesso||" + objFunc.Codifica(id_factura));
		} else {
			return ResponseEntity.ok().body("logout");
		}

	}

	// ========================= VIEW NOTA DE CREDITO INDIVIDUAL
	// =======================0
	@RequestMapping(value = "/fiinika/documentos/nota-de-credito/visualizar-nota-de-credito/{id}", method = RequestMethod.GET)
	public String NotaCredito(@PathVariable String id, Model model, HttpSession session, HttpServletResponse response) throws IOException {

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);

			long id_decod = objFunc.Decodifica(id);
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);

			model.addAttribute("titulo", "Vizualizar Nota de crédito");
			model.addAttribute("factura", R.facturaRepository.findFacturaIdNC(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("itens", R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod));
			Iterable<Item> items = R.itemRepository.findItemFactura(user.getIdEmpresaUser(), id_decod);

			model.addAttribute("qtdItem", R.itemRepository.findqtdItem(user.getIdEmpresaUser(), id_decod));
			model.addAttribute("dados", empresa);
			// model.addAttribute("produtos",R.produtoRepository.findProductoEmpresa(id_empresa));
			model.addAttribute("aux", id);

			return "fiinika/documentos/nota-de-credito/visualizar-nota-de-credito";
		} else {
			return "redirect:/index";
		}
	}
	
	
	 @RequestMapping(value="/fiinika/documentos/pdf/factura-termica/{code}", method=RequestMethod.GET)
	   public String ViewTermica( Context context,Model model, HttpSession session,HttpServletResponse response,HttpServletRequest resquest,
			   @PathVariable("code") String code) throws IOException{
		
	Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
    if(objSessao!=null) {
			userDetails = (UserDetails) objSessao;
		    String []  dadosUser= userDetails.getUsername().split("_");
		    long id_user = Long.parseLong(dadosUser[0]);
			Usuario user = R.userRepository.findById(id_user).orElse(null);
		    authenticatedUser.Dados(model, session, response);
				
			Empresa empresa = R.empresaRepository.findById(user.getIdEmpresaUser()).orElse(null);
		    long id_factura = objFunc.Decodifica(code);
			model.addAttribute("titulo","Factura Termica");	
			
			 // Empresa empresa = R._empresaRepository.findById(id_empresa).orElse(null);	
			  Factura factura = R.facturaRepository.findFacturaAndEmpresa(user.getIdEmpresaUser(),id_factura).orElse(null);	
			   List  valor_imposto = new ArrayList<>();
		     
		       double total_inc_ret = 0;
		      
				
		       
		       model.addAttribute("emp",R.empresaRepository.findById(factura.getId_empresafk()).orElse(null));
		       model.addAttribute("fact",factura);
		       model.addAttribute("cl",R.clienteRepository.findById(factura.getId_clientefk()).orElse(null));
		       Iterable <Item> items = R.itemRepository.findItem(factura.getId_factura());
		       model.addAttribute("itens",items);
		       model.addAttribute("coord",R.contaRepository.findById(factura.getId_contafk()).orElse(null));
		       model.addAttribute("obs_txt",0);
		       model.addAttribute("server", objFunc.currentUrlServer(resquest));
		      
		      
			      for(Subscricao sub :R.subscricaoRepository.findSubscricaDexa(factura.getId_empresafk())) {
			    	  model.addAttribute("obs_txt",1);
			    	
			      }
		       
			      Map<String, List<Item>> gruposItens = new HashMap<>();
			        gruposItens.put("14.00", new ArrayList<>());
			        gruposItens.put("7.00", new ArrayList<>());
			        gruposItens.put("5.00", new ArrayList<>());
			        gruposItens.put("0.00", new ArrayList<>());
			        
			        // Mapa para armazenar as somas de preço e imposto
			        Map<String, Map<String, Double>> somas = new HashMap<>();
			        somas.put("14.00", new HashMap<>());
			        somas.get("14.00").put("preco", 0.0);
			        somas.get("14.00").put("imposto", 0.0);

			        somas.put("7.00", new HashMap<>());
			        somas.get("7.00").put("preco", 0.0);
			        somas.get("7.00").put("imposto", 0.0);

			        somas.put("5.00", new HashMap<>());
			        somas.get("5.00").put("preco", 0.0);
			        somas.get("5.00").put("imposto", 0.0);

			        somas.put("0.00", new HashMap<>());
			        somas.get("0.00").put("preco", 0.0);
			        somas.get("0.00").put("imposto", 0.0);
			        
			        for (Item item : items) {
			            // Formata a taxa para duas casas decimais			        	
			            String taxa = item.getTaxa_prod_item();
			            if(taxa.equals("00.00")) {
			        		taxa = "0.00";
			        	}
			          
			            // Adiciona o item ao grupo correspondente
			            gruposItens.get(taxa).add(item);

			            // Calcula o preço total e o imposto para a taxa
			            double precoTotal = Double.parseDouble(item.getPreco()) * item.getQtd();
			            double imposto = (Double.parseDouble(item.getTaxa_prod_item()) / 100) * precoTotal;

			            // Atualiza as somas
			            Map<String, Double> somaTaxa = somas.get(taxa);
			         
			            somaTaxa.put("preco", somaTaxa.get("preco") + precoTotal);
			            somaTaxa.put("imposto", somaTaxa.get("imposto") + imposto);
			         
			            valor_imposto.add((Double.parseDouble(item.getTaxa_prod_item())/100)*Double.parseDouble(item.getPreco()));
			    	    if(item.getTipo_item().equals("Serviço 6.5")) {
			    		   total_inc_ret = total_inc_ret + (Double.parseDouble(item.getPreco())*item.getQtd());
			    	    }
			        }
			        
//			        System.out.println("Somas por taxa:");
//			        somas.forEach((taxa, soma) -> {
//			            System.out.println("Taxa: " + taxa + " | Preço: " + soma.get("preco") + " | Imposto: " + soma.get("imposto"));
//			        });

		     
		       
		       model.addAttribute("v_imp",valor_imposto);
		       model.addAttribute("total_inc_ret",total_inc_ret);
		       model.addAttribute("somas",somas);
		       model.addAttribute("hash_msg",objFunc.extrairCaracteres(factura.getHash_msg()));
		       
			
			
		  
			//R.Role(model,session,response);	
			
			return "fiinika/documentos/pdf/factura-termica";
			
			}else {
				return  "redirect:/index";
			}
	    }

	public String AnulaFactura(long id_factura_origem) {

		Factura factura = R.facturaRepository.findById(id_factura_origem).get();
		factura.setEstado("A");
		R.facturaRepository.save(factura);

		return "";

	}

//----------------------------------------- AJAX ----------------------------------------------------			 

	@RequestMapping(value = "/ajax/produtos", method = RequestMethod.GET)
	public @ResponseBody List<Produto> findAllProdutos(
			@RequestParam(value = "id_categoria", required = true) Long id_categoria, HttpSession session) {

		List produto = (List) R.produtoRepository.findProductoforCategoria2(id_categoria);
		return produto;
	}

	@RequestMapping(value = "/ajax/produtos_data", method = RequestMethod.GET)
	public @ResponseBody Optional<Produto> findDataProduto(
			@RequestParam(value = "id_produto", required = true) Long id_produto, HttpSession session) {

		Optional<Produto> produto = R.produtoRepository.findById(id_produto);
		return produto;
	}

	// ----------------------------- Pega o codigo ---------

	@RequestMapping(value = "/ajax/cod_factura", method = RequestMethod.GET)
	public @ResponseBody List<List> findCodFactura(@RequestParam(value = "cod_factura", required = true) String cod,
			HttpSession session) {

		List factura = (List) R.facturaRepository.findCodFactura(cod);

		return factura;

	}

	// ---------------------------------- AJAX FILTRO FACTURAS

	@RequestMapping(value = "/ajax/docs_dashboard", method = RequestMethod.GET)
	public @ResponseBody List<List> findAll(@RequestParam(value = "ano", required = false) String ano,
			@RequestParam(value = "mes", required = false) String mes,
			@RequestParam(value = "tipo", required = false) String tipo, HttpSession session, Model model,HttpServletResponse response) throws IOException {
		

		List<List> documentos = new ArrayList<>();
		List Ids_codificado = new ArrayList<>();
		// List documentos = new ArrayList<> ();

		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			    
			if (tipo.equals("RC")) {
				if (!ano.equals("") && !mes.equals("")) {
					return Documemtos(R.reciboRepository.findAllRecibos(user.getIdEmpresaUser(), ano, mes),
							documentos,user);
				}
				if (!ano.equals("") && mes.equals("")) {
					return Documemtos(R.reciboRepository.findAllRecibosAno(user.getIdEmpresaUser(), ano),
							documentos,user);
				}
				if (ano.equals("") && !mes.equals("")) {
					return Documemtos(R.reciboRepository.findAllRecibosMes(user.getIdEmpresaUser(), mes),
							documentos,user);
				}
				if (ano.equals("") && mes.equals("")) {
					return Documemtos(R.reciboRepository.findAllRecibos(user.getIdEmpresaUser()), documentos,user);
				}
			} else {
				// Pesquisa os tres
				if (!ano.equals("") && !mes.equals("") && !tipo.equals("")) {
					// R.facturaRepository.FacturasLastsDocumentosSearch(ano,mes,tipo)
					return Documemtos(R.facturaRepository.FacturasLastsDocumentosSearch(user.getIdEmpresaUser(),
							ano, mes, tipo), documentos,user);
				}
				// Pesquisa ano e mes
				if (!ano.equals("") && !mes.equals("") && tipo.equals("")) {
					return Documemtos(R.facturaRepository.findAllFacturas(user.getIdEmpresaUser(), ano, mes),
							documentos,user);
				}
				// pesquisa ano e tipo
				if (!ano.equals("") && mes.equals("") && !tipo.equals("")) {
					return Documemtos(
							R.facturaRepository.findAllFacturasAnoTipo(user.getIdEmpresaUser(), ano, tipo),
							documentos,user);
				}
				// pesquisa ano
				if (!ano.equals("") && mes.equals("") && tipo.equals("")) {
					return Documemtos(R.facturaRepository.findAllFacturasAno(user.getIdEmpresaUser(), ano),
							documentos,user);
				}
				// pesquisa mes e tipo
				if (ano.equals("") && !mes.equals("") && !tipo.equals("")) {
					return Documemtos(
							R.facturaRepository.findAllFacturasMesTipo(user.getIdEmpresaUser(), mes, tipo),
							documentos,user);
				}
				// pesquisa mes
				if (ano.equals("") && !mes.equals("") && tipo.equals("")) {
					return Documemtos(R.facturaRepository.findAllFacturasMes(user.getIdEmpresaUser(), mes),
							documentos,user);
				}
				// pesquisa tipo
				if (ano.equals("") && mes.equals("") && !tipo.equals("")) {
					return Documemtos(R.facturaRepository.findAllFacturasTipo(user.getIdEmpresaUser(), tipo),
							documentos,user);
				}
				// List produto = (List)
				// R.produtoRepository.findProductoforCategoria2(id_categoria);
			}
			return null;
		} else {
			return null;
		}
	}

	public List<List> Documemtos(List<List> lista, List<List> ListMain,Usuario user) {

		List lista_aux = new ArrayList<>();
		boolean b = false, c = false;
		for (List doc : lista) {
			try {
				// Ids_codificado.add(objFunc.Codifica(Long.parseLong(doc.get(0).toString())));
				
				// R.ver(doc.get(10).toString()+"ffff"+R.getUserLogado().getId()+"
				// "+user.getTipo());
				if (doc.get(1).toString().contains("RC")) {
					if (Long.parseLong(doc.get(8).toString()) == user.getId_usuario()
							&& user.getTipo().equals("Vendedor")) {
						ListMain.add(doc);
						
					}
					if (user.getTipo().equals("Admin")) {
						b = true;
					}

				} else {
			
					if (Long.parseLong(doc.get(10).toString()) == user.getId_usuario()
							&& user.getTipo().equals("Vendedor")) {
						lista_aux.add(doc);
						
						c = true;
					}

					if (user.getTipo().equals("Admin")) {
						b = true;
					}

				}

			} catch (Exception e) {

			}
		}
		if (b) {
			ListMain.add(lista);
		}
		if (c) {
			ListMain.add(lista_aux);
		}
		
		return ListMain;
	}

	// --- Ajax pesquisa cliente por nome
	@RequestMapping(value = "/ajax/documento_pesquisa", method = RequestMethod.GET)
	public @ResponseBody List<List> PesquisaDinamicaNome(
			@RequestParam(value = "nome", required = true) String valor_pesquisa,
			@RequestParam(value = "tipo", required = true) String tipo, HttpSession session, Model model,HttpServletResponse response) throws IOException {

		List<List> ListMain = new ArrayList<>();
		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			    
			if (!valor_pesquisa.equals("")) {

				try {
					ListMain.add(R.facturaRepository.findPesquisaDinamica(user.getIdEmpresaUser(), tipo,
							valor_pesquisa));

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			return ListMain;
		} else {
			return ListMain;
		}
	}

	// --- Ajax pesquisa cliente por nome
	@RequestMapping(value = "/ajax/documentos_dashboard", method = RequestMethod.GET)
	public @ResponseBody List<List> PesquisaDinamicaNomeDashboard(
			@RequestParam(value = "nome", required = true) String valor_pesquisa, HttpSession session, Model model,HttpServletResponse response) throws IOException {

		List<List> ListMain = new ArrayList<>();
		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			if (!valor_pesquisa.equals("")) {

				try {
					ListMain.add(
							R.facturaRepository.findPesquisaDinamica(user.getIdEmpresaUser(), valor_pesquisa));

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			return ListMain;
		} else {
			return ListMain;
		}
	}

	// --- Ajax pesquisa cliente por nome
	@RequestMapping(value = "/ajax/documentos_dashboardRC", method = RequestMethod.GET)
	public @ResponseBody List<List> PesquisaDinamicaNomeDashboardRC(
			@RequestParam(value = "nome", required = true) String valor_pesquisa, HttpSession session, Model model,HttpServletResponse response) throws IOException {

		List<List> ListMain = new ArrayList<>();
		Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
	    if(objSessao!=null) {
				userDetails = (UserDetails) objSessao;
			    String []  dadosUser= userDetails.getUsername().split("_");
			    long id_user = Long.parseLong(dadosUser[0]);
				Usuario user = R.userRepository.findById(id_user).orElse(null);
			    authenticatedUser.Dados(model, session, response);
			if (!valor_pesquisa.equals("")) {

				try {
					ListMain.add(
							R.reciboRepository.findAllReciboDinamica(valor_pesquisa, user.getIdEmpresaUser()));

				} catch (Exception e) {
					// TODO: handle exception
				}
			}

			return ListMain;
		} else {
			return ListMain;
		}
	}
	
	
	public List VerificaStock(long num_itens,Long [] id_produto,int[] qtd,long idEmpresa) {
		List <Object> list = new ArrayList<>();
		list.add(false);
		list.add("");
		for(int i = 0; i < num_itens; i++) {	
			System.out.println(num_itens+" "+id_produto.length);
				Produto produto = R.produtoRepository.findProductoEmpresaId(idEmpresa, id_produto[i]).orElse(null);
				System.out.println(produto.getNomeProduto()+" "+produto.getQtdStock());
				if(produto.getQtdStock()< qtd[i]) {
					list.set(0,true);
					list.set(1,"Sem stock para o produto "+produto.getNomeProduto());					
				}			
		}
		return list;
	}

}
