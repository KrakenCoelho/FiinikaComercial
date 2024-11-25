package com.ao.fiinikacomercial.funcoes;
/*
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.ao.dexaconta.controller.IndexController;
import com.ao.fiinikacomercial.model.*;
import com.ao.fiinikacomercial.repository.*;

import antlr.StringUtils;

public class SaftDexa {
	
	 private Funcoes objFunc = new Funcoes();	

	    private String nome_file_saft ="";
	 
	
	
	public  String EscreverXml(HttpServletRequest request,long IDuserlogado,EmpresaRepository _empresa,ClienteRepository _cliente,FacturaRepository _factura,ReciboRepository _recibo,ProdutoRepository _produto,ItemRepository _item,ItemNotaRepository _itemNota,SaftRepository _saft,NotaCreditoRepository _nota_creditoRepository, String data_de,String data_ate) {
		
	      try {
	    	  String ano = data_de.split("-")[0];
	    	  String mes = data_de.split("-")[1];
	    	  String mes2 = data_ate.split("-")[1];
	    	  
	    	  String nome_saft = "SAFT-AO";
	    	  
	    	  if(mes.equals(mes2)) {
	    		  nome_saft=nome_saft+"-"+ano+"_"+objFunc.MesAbreviado(mes)+"_"+ data_de.split("-")[2]+" a "+data_ate.split("-")[2];
	    	  }else {
	    		  nome_saft=nome_saft+"-"+ano+"_"+objFunc.MesAbreviado(mes)+"_"+ data_de.split("-")[2]+" a "+objFunc.MesAbreviado(mes2)+"_"+data_ate.split("-")[2];
	    	  }	    	  
	    	 
	    	  		
	    	  String invoiceNo = "";
	          String xmlString = "<AuditFile xmlns=\"urn:OECD:StandardAuditFile-Tax:AO_1.01_01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
	    	  Iterable <Empresa> empresa = _empresa.findIdUser(IDuserlogado);
	    	 // Iterable <Loja> lojas = _lojaRepository.findAll(); 
	    	  List <Long> List_id_loja =  new ArrayList<>();
	    	  
	    	  String realPathtoUploads =  request.getServletContext().getRealPath("saft/"); 
	    	 	    	 
	    	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder builder = null;	        
	         builder = factory.newDocumentBuilder();
	         
	      
	       // int num_loja = (int) _lojaRepository.count(), contLoja = 0;
       for(Empresa emp:empresa) {    
    	   nome_saft = emp.getNif()+"_"+nome_saft;
      	 //contLoja = 0;
      xmlString +="<Header><AuditFileVersion>1.01_01</AuditFileVersion>"
      	 		+ "<CompanyID>"+emp.getNome_empresa()+"</CompanyID>"
      	 		+ "<TaxRegistrationNumber>"+emp.getNif()+"</TaxRegistrationNumber>"
      	 		+ "<TaxAccountingBasis>F</TaxAccountingBasis>"
      	 		+ "<CompanyName>"+emp.getNome_empresa()+"</CompanyName>\n"
      	 		+ "<CompanyAddress>\n"
      	 		+ "<AddressDetail>"+emp.getEndereco()+"</AddressDetail>\n"
      	 		+ "<City>LUANDA</City>\n"
      	 		+ "<Province>LUANDA</Province>\n"
      	 		+ "<Country>AO</Country>\n"
      	 		+ "</CompanyAddress>\n"
      	 		+ "<FiscalYear>"+objFunc.AnoActual()+"</FiscalYear>\n"
      	 		+ "<StartDate>"+data_de+"</StartDate>\n"
      	 		+ "<EndDate>"+data_ate+"</EndDate>\n"
      	 		+ "<CurrencyCode>AOA</CurrencyCode>\n"
      	 		+ "<DateCreated>"+objFunc.DataActual()+"</DateCreated>\n"
      	 		+ "<TaxEntity>GLOBAL</TaxEntity>\n"
      	 		+ "<ProductCompanyTaxID>"+emp.getNif()+"</ProductCompanyTaxID>\n"
      	 		+ "<SoftwareValidationNumber>371/AGT/2022</SoftwareValidationNumber>\n" // Trocar depois
      	 		+ "<ProductID>DexaContas/SELECTE MIDIA -SERVICESLDA</ProductID>\n"					// Trocar depois
      	 		+ "<ProductVersion>1.0.0</ProductVersion>\n"
      	 		+ "<Telephone>+"+emp.getTelemovel()+"</Telephone>\n"
      	 		+ "<Email>"+emp.getEmail()+"</Email>"
      	 		+ "</Header>"
      	 		+ "<MasterFiles>\n";
      //"<SoftwareValidationNumber>83/AGT/2019</SoftwareValidationNumber>\n" 
      //	+ "<ProductID>KwanzaGest/GLOBOSOFT-TECN. DE INF. E TELECOMUNICACOES, LDA</ProductID>\n"
      					for(Cliente cli: _cliente.findById_empresa(emp.getId_empresa())) {
      						
      xmlString +="<Customer>\n"
      	 		+ "<CustomerID>"+cli.getId_cliente()+"</CustomerID>\n"
      	 		+ "<AccountID>Desconhecido</AccountID>\n"
      	 		+ "<CustomerTaxID>"+cli.getNif()+"</CustomerTaxID>\n"
      	 		+ "<CompanyName>"+cli.getNome_cliente()+"</CompanyName>\n"
      	 		+ "<BillingAddress>\n"
      	 		+ "<AddressDetail>"+cli.getEndereco()+"</AddressDetail>\n"
      	 		+ "<City>LUANDA</City>\n"
      	 		+ "<PostalCode>Desconhecido</PostalCode>\n"
      	 		+ "<Province>LUANDA</Province>\n"
      	 		+ "<Country>AO</Country>\n"
      	 		+ "</BillingAddress>\n"
      	 		+ "<Telephone>"+cli.getTelemovel()+"</Telephone>\n"
      	 		+ "<Email>"+cli.getEmail()+"</Email>\n"
      	 		+ "<SelfBillingIndicator>0</SelfBillingIndicator>\n"
      	 		+ "</Customer>";
      					}     
      					
      	      for(List prod: _produto.findProdudByCatEmp(emp.getId_empresa())) {	
      	    	
      xmlString +="<Product>\n"
      			+ "<ProductType>P</ProductType>\n"
      			+ "<ProductCode>"+prod.get(0)+"</ProductCode>\n"
      			+ "<ProductGroup>SERVIÇOS</ProductGroup>\n"
      			+ "<ProductDescription>"+prod.get(1)+"</ProductDescription>\n"
      			+ "<ProductNumberCode>000000000"+prod.get(0)+"</ProductNumberCode>\n"
      			+ "</Product>";
      	      }
     xmlString  +="<TaxTable>\n"
    		    + "<TaxTableEntry>\n"
				+ "<TaxType>IVA</TaxType>\n"
				+ "<TaxCode>NOR</TaxCode>\n"
				+ "<Description>Normal</Description>\n"
				+ "<TaxPercentage>14.0000</TaxPercentage>\n"
				+ "</TaxTableEntry>\n"
				+ "<TaxTableEntry>\n"
				+ "<TaxType>IVA</TaxType>\n"
				+ "<TaxCode>OUT</TaxCode>\n"
				+ "<Description>IVA - Regime de Exclusão</Description>\n"
				+ "<TaxPercentage>0.0000</TaxPercentage>\n"
				+ "</TaxTableEntry>\n"
				+ "<TaxTableEntry>\n"
				+ "<TaxType>IVA</TaxType>\n"
				+ "<TaxCode>ISE</TaxCode>\n"
				+ "<Description>Transmissão de bens e serviços não sujeita</Description>\n"
				+ "<TaxPercentage>0</TaxPercentage>\n"
				+ "</TaxTableEntry>\n"
				+ "</TaxTable>"
				+ "</MasterFiles>"
				+ "<SourceDocuments><SalesInvoices>";
     
                String total_credit= "0",total_debit= "0";
     			if(_factura.TotalCreditoMesF_FR(emp.getId_empresa(), data_de,data_ate)!=null) {
     				total_credit = _factura.TotalCreditoMesF_FR(emp.getId_empresa(), data_de,data_ate);
     			}
     			if(_factura.TotalDebitoMesNC(emp.getId_empresa(), data_de,data_ate)!=null) {
     				total_debit = _factura.TotalDebitoMesNC(emp.getId_empresa(), data_de,data_ate);
     			}
     xmlString  +="<NumberOfEntries>"+_factura.countEntradasMesF_FR_NC(emp.getId_empresa(), data_de,data_ate)+"</NumberOfEntries>\n"
                + "<TotalDebit>"+objFunc.formatarFloat(Float.parseFloat(total_debit))+"</TotalDebit>\n"
                + "<TotalCredit>"+objFunc.formatarFloat(Float.parseFloat(total_credit))+"</TotalCredit>\n";
     
     String tax_str ="";
     
     for(Factura fact: _factura.findIdFacturaEmpresaF_FR_NC(emp.getId_empresa(), data_de,data_ate)) {	
    	 String InvoiceStatus ="N";
         if(fact.getEstado().equals("A"))
        	 InvoiceStatus = "A";
         
    	 invoiceNo = fact.getCodigo_factura();
    	 xmlString   +="<Invoice>\n"
		        + "<InvoiceNo>"+invoiceNo+"</InvoiceNo>\n"
		        + "<DocumentStatus>\n"
		        + "<InvoiceStatus>"+InvoiceStatus+"</InvoiceStatus>\n"
		        + "<InvoiceStatusDate>"+fact.getData_hora()+"</InvoiceStatusDate>\n"
		        + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
		        + "<SourceBilling>P</SourceBilling>\n"
		        + "</DocumentStatus>\n"
		        + "<Hash>"+fact.getHash_msg()+"</Hash>\n"
		        + "<HashControl>1</HashControl>\n"
		        + "<Period>"+Integer.parseInt(fact.getData_emissao().split("-")[1])+"</Period>\n"
		        + "<InvoiceDate>"+fact.getData_emissao()+"</InvoiceDate>\n"
		        + "<InvoiceType>"+fact.getTipo()+"</InvoiceType>\n"
		        + "<SpecialRegimes>\n"
		        + "<SelfBillingIndicator>0</SelfBillingIndicator>\n"
		        + "<CashVATSchemeIndicator>0</CashVATSchemeIndicator>\n"
		        + "<ThirdPartiesBillingIndicator>0</ThirdPartiesBillingIndicator>\n"
		        + "</SpecialRegimes>\n"
		        + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
		        + "<SystemEntryDate>"+fact.getData_hora()+"</SystemEntryDate>\n"
		        + "<CustomerID>"+fact.getId_clientefk()+"</CustomerID>\n";
    int line = 1;
   
    
    if(fact.getTipo().equals("NC")) {
		System.out.println("NC "+fact.getId_factura());
	   for(ItemNotas item: _itemNota.findItemFactura(emp.getId_empresa(),fact.getId_factura())) {
		String origem =  _nota_creditoRepository.findNotacreditoIdFact(fact.getId_factura()).iterator().next().getOrigem();
		System.out.println("Origem "+ origem);
		  if(!item.getTaxa_prod_nota().equals("00.00")) {
			   tax_str =  "<Tax>\n"
				   		+ "<TaxType>IVA</TaxType>\n"
				   		+ "<TaxCountryRegion>AO</TaxCountryRegion>\n"
				   		+ "<TaxCode>NOR</TaxCode>\n"
				   		+ "<TaxPercentage>"+Double.parseDouble(item.getTaxa_prod_nota())+"</TaxPercentage>\n"
				   		+ "</Tax>\n"
				   		+ "<SettlementAmount>0</SettlementAmount>";
		   }else {
			   tax_str =  "<Tax>\n"
				        + "<TaxType>IVA</TaxType>\n"
				        + "<TaxCountryRegion>AO</TaxCountryRegion>\n"
				        + "<TaxCode>ISE</TaxCode>\n"
				        + "<TaxPercentage>0.0000</TaxPercentage>\n"
				        + "</Tax>\n"
				        + "<TaxExemptionReason>"+item.getMotivo_ise()+"</TaxExemptionReason>\n"
				        + "<TaxExemptionCode>"+item.getCodigo_ise()+"</TaxExemptionCode>\n"
				        + "<SettlementAmount>0.0000</SettlementAmount>\n";
				}
	
	  xmlString   +=  "<Line>\n"
			        + "<LineNumber>"+line+"</LineNumber>\n"
			        + "<ProductCode>"+item.getId_produtofk()+"</ProductCode>\n"
			        + "<ProductDescription>"+item.getDescricao_prod()+"</ProductDescription>\n"
			        + "<Quantity>"+item.getQtd()+"</Quantity>\n"
			        + "<UnitOfMeasure>UN</UnitOfMeasure>\n"
			        + "<UnitPrice>"+item.getPreco()+"</UnitPrice>\n"
			        + "<TaxPointDate>"+fact.getData_emissao()+"</TaxPointDate>\n"
			        +"<References>"
	                +"<Reference>"+origem+"</Reference>"
	                +"</References>"
			        + "<Description>"+item.getDescricao_prod()+"</Description>\n"			        
			        + "<DebitAmount>"+item.getPreco()*item.getQtd()+"</DebitAmount>\n"
			        + tax_str
			        + "</Line>\n";
			       line++;
			       
			       }
		   }else {
			   //---- PARA OUTROS TIPOS COMO FT,FT OU PP
			   for(Item item: _item.findItemFactura(emp.getId_empresa(),fact.getId_factura())) {
				   
				   if(!item.getTaxa_prod_item().equals("00.00")) {
					   tax_str =  "<Tax>\n"
						   		+ "<TaxType>IVA</TaxType>\n"
						   		+ "<TaxCountryRegion>AO</TaxCountryRegion>\n"
						   		+ "<TaxCode>NOR</TaxCode>\n"
						   		+ "<TaxPercentage>"+Double.parseDouble(item.getTaxa_prod_item())+"</TaxPercentage>\n"
						   		+ "</Tax>\n"
						   		+ "<SettlementAmount>0</SettlementAmount>";
				   }else {
					   tax_str =  "<Tax>\n"
						        + "<TaxType>IVA</TaxType>\n"
						        + "<TaxCountryRegion>AO</TaxCountryRegion>\n"
						        + "<TaxCode>ISE</TaxCode>\n"
						        + "<TaxPercentage>0.0000</TaxPercentage>\n"
						        + "</Tax>\n"
						        + "<TaxExemptionReason>"+item.getMotivo_ise()+"</TaxExemptionReason>\n"
						        + "<TaxExemptionCode>"+item.getCodigo_ise()+"</TaxExemptionCode>\n"
						        + "<SettlementAmount>0.0000</SettlementAmount>\n";
						}
				  
				  xmlString   +=  "<Line>\n"
						        + "<LineNumber>"+line+"</LineNumber>\n"
						        + "<ProductCode>"+item.getId_produtofk()+"</ProductCode>\n"
						        + "<ProductDescription>"+item.getDescricao_prod()+"</ProductDescription>\n"
						        + "<Quantity>"+item.getQtd()+"</Quantity>\n"
						        + "<UnitOfMeasure>UN</UnitOfMeasure>\n"
						        + "<UnitPrice>"+item.getPreco()+"</UnitPrice>\n"
						        + "<TaxPointDate>"+fact.getData_emissao()+"</TaxPointDate>\n"
						        + "<Description>"+item.getDescricao_prod()+"</Description>\n"			        
						        + "<CreditAmount>"+item.getPreco()*item.getQtd()+"</CreditAmount>\n"
						        + tax_str
						        + "</Line>\n";
						       line++;
			   }
			   
		   }

   
    xmlString   +="<DocumentTotals>\n"
		        + "<TaxPayable>"+fact.getTaxa()+"</TaxPayable>\n"
		        + "<NetTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal())-Double.parseDouble(fact.getImpos_retido())-Double.parseDouble(fact.getValor_desconto()))+"")+"</NetTotal>\n"
		        + "<GrossTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal_final()))+"")+"</GrossTotal>\n"
		        + "</DocumentTotals>\n"
		        + "</Invoice>\n";
              
     }
     
     
     //------- Para WORKINGDOCUMENTAS (PROFORMAS,ORCAMENTOS ENTRE OUTROS)
     total_credit= "0"; total_debit= "0";
   		if(_factura.TotalCreditoMesWORK(emp.getId_empresa(), data_de,data_ate)!=null) {
   			total_credit = _factura.TotalCreditoMesWORK(emp.getId_empresa(), data_de,data_ate);
   		}
   		if(_factura.TotalDebitoMesWORK_NC(emp.getId_empresa(), data_de,data_ate)!=null) {
   			// REVER TOTALDEBIT PARA WORKDOCUMENTS
			//	total_debit = _factura.TotalDebitoMesWORK_NC(emp.getId_empresa(), ano,mes);
		}
   		
     xmlString+=  "</SalesInvoices>\n"
    		    + "<WorkingDocuments>\n"
  	        	+ "<NumberOfEntries>"+_factura.countEntradasMesWORK(emp.getId_empresa(), data_de, data_ate)+"</NumberOfEntries>\n"
   		        + "<TotalDebit>"+objFunc.formatarFloat(Float.parseFloat(total_debit))+"</TotalDebit>\n"
   		        + "<TotalCredit>"+objFunc.formatarFloat(Float.parseFloat(total_credit))+"</TotalCredit>\n";
     
     for(Factura fact: _factura.findIdFacturaEmpresaWORK(emp.getId_empresa(), data_de,data_ate)) {	
    	 
    	 String InvoiceStatus ="N";
         if(fact.getEstado().equals("A"))
        	 InvoiceStatus = "A";
         
         invoiceNo = fact.getCodigo_factura();
         xmlString+=  "<WorkDocument>"
				    + "<DocumentNumber>"+invoiceNo+"</DocumentNumber>\n"
				    + "<DocumentStatus>\n"
				    +    "<WorkStatus>"+InvoiceStatus+"</WorkStatus>\n"
				    +    "<WorkStatusDate>"+fact.getData_hora()+"</WorkStatusDate>\n"
				    +    "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
				    +    "<SourceBilling>P</SourceBilling>\n"
				    + "</DocumentStatus>\n"
				    + "<Hash>"+fact.getHash_msg()+"</Hash>\n"
				    + "<HashControl>1</HashControl>\n"
				    + "<Period>"+Integer.parseInt(mes)+"</Period>\n"
				    + "<WorkDate>"+fact.getData_emissao()+"</WorkDate>\n"
				    + "<WorkType>"+fact.getTipo()+"</WorkType>\n"
				    + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
				    + "<SystemEntryDate>"+fact.getData_hora()+"</SystemEntryDate>\n"
				    + "<CustomerID>"+fact.getId_clientefk()+"</CustomerID>\n";
         
         int line = 1;
         
         for(Item item: _item.findItemFactura(emp.getId_empresa(),fact.getId_factura())) {
			   
			   if(!item.getTaxa_prod_item().equals("00.00")) {
				   tax_str =  "<Tax>\n"
					   		+ "<TaxType>IVA</TaxType>\n"
					   		+ "<TaxCountryRegion>AO</TaxCountryRegion>\n"
					   		+ "<TaxCode>NOR</TaxCode>\n"
					   		+ "<TaxPercentage>"+Double.parseDouble(item.getTaxa_prod_item())+"</TaxPercentage>\n"
					   		+ "</Tax>\n"
					   		+ "<SettlementAmount>0</SettlementAmount>";
			   }else {
				   tax_str =  "<Tax>\n"
					        + "<TaxType>IVA</TaxType>\n"
					        + "<TaxCountryRegion>AO</TaxCountryRegion>\n"
					        + "<TaxCode>ISE</TaxCode>\n"
					        + "<TaxPercentage>0.0000</TaxPercentage>\n"
					        + "</Tax>\n"
					        + "<TaxExemptionReason>"+item.getMotivo_ise()+"</TaxExemptionReason>\n"
					        + "<TaxExemptionCode>"+item.getCodigo_ise()+"</TaxExemptionCode>\n"
					        + "<SettlementAmount>0.0000</SettlementAmount>\n";
					}
			  
			  xmlString   +=  "<Line>\n"
					        + "<LineNumber>"+line+"</LineNumber>\n"
					        + "<ProductCode>"+item.getId_produtofk()+"</ProductCode>\n"
					        + "<ProductDescription>"+item.getDescricao_prod()+"</ProductDescription>\n"
					        + "<Quantity>"+item.getQtd()+"</Quantity>\n"
					        + "<UnitOfMeasure>UN</UnitOfMeasure>\n"
					        + "<UnitPrice>"+item.getPreco()+"</UnitPrice>\n"
					        + "<TaxPointDate>"+fact.getData_emissao()+"</TaxPointDate>\n"
					        + "<Description>"+item.getDescricao_prod()+"</Description>\n"			        
					        + "<CreditAmount>"+item.getPreco()*item.getQtd()+"</CreditAmount>\n"
					        + tax_str
					        + "</Line>\n";
					       line++;
		   }
             xmlString      +="<DocumentTotals>\n"
			 		        + "<TaxPayable>"+fact.getTaxa()+"</TaxPayable>\n"
			 		        + "<NetTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal())-Double.parseDouble(fact.getImpos_retido())-Double.parseDouble(fact.getValor_desconto()))+"")+"</NetTotal>\n"
			 		        + "<GrossTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal_final()))+"")+"</GrossTotal>\n"
			 		        + "</DocumentTotals>\n"
			 		        + "</WorkDocument>\n";         
     
       }    
     
     //------ PARA OS PAGAMENTOS
        total_credit= "0";
		if(_recibo.TotalCreditoDeAte(emp.getId_empresa(), data_de,data_ate)!=null) {
			total_credit = _recibo.TotalCreditoDeAte(emp.getId_empresa(), data_de,data_ate);
		}
   xmlString+=  "</WorkingDocuments>"
   		      + "<Payments>"
   	       	  + "<NumberOfEntries>"+_recibo.countEntradasDeAte(emp.getId_empresa(), data_de, data_ate)+"</NumberOfEntries>\n"
    		  + "<TotalDebit>0.00</TotalDebit>\n"
    		  + "<TotalCredit>"+objFunc.formatarFloat(Float.parseFloat(total_credit))+"</TotalCredit>\n";
   
    String PaymentRefNo = "";  
   for(List recibo: _recibo.findIdReciboEmpresaAuxDeAte(emp.getId_empresa(), data_de,data_ate)) {	 
	   
	   PaymentRefNo =recibo.get(16)+"";
	 float taxaf =  Float.parseFloat(recibo.get(6).toString())*(Float.parseFloat(recibo.get(10).toString())/100);
   xmlString+=	"<Payment>\n"
    		  + "<PaymentRefNo>"+PaymentRefNo+"</PaymentRefNo>\n"
    		  + "<Period>"+Integer.parseInt(mes)+"</Period>\n"
    		  + "<TransactionDate>"+recibo.get(4)+"</TransactionDate>\n"
    		  + "<PaymentType>RC</PaymentType>\n"
    		  + "<SystemID>"+recibo.get(0)+"</SystemID>\n"
    		  + "<DocumentStatus>\n"
    		  + "<PaymentStatus>N</PaymentStatus>\n"
    		  + "<PaymentStatusDate>"+recibo.get(11)+"</PaymentStatusDate>\n"
    		  + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
    		  + "<SourcePayment>P</SourcePayment>\n"
    		  + "</DocumentStatus>\n"
    		  + "<PaymentMethod>\n"
    		  + "<PaymentAmount>"+recibo.get(6)+"</PaymentAmount>\n"
    		  + "<PaymentDate>"+recibo.get(12)+"</PaymentDate>\n"
    		  + "</PaymentMethod>\n"
    		  + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
    		  + "<SystemEntryDate>"+recibo.get(11)+"</SystemEntryDate>\n"
    		  + "<CustomerID>"+recibo.get(9)+"</CustomerID>\n"
    		  
    		  + "<Line>\n"
    		  + "<LineNumber>1</LineNumber>\n"
    		  + "<SourceDocumentID>\n"
    		  + "<OriginatingON>"+recibo.get(17)+"</OriginatingON>\n"
    		  + "<InvoiceDate>"+recibo.get(8)+"</InvoiceDate>\n"
    		  + "</SourceDocumentID>\n"
    		  + "<SettlementAmount>0</SettlementAmount>\n"
    		  + "<CreditAmount>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(14).toString()))+"</CreditAmount>\n"
    		  + "</Line>\n"
    		  
    		  + "<DocumentTotals>\n"
    		  + "<TaxPayable>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(13).toString()))+"</TaxPayable>\n" // valor soma das taxas 
    		  + "<NetTotal>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(14).toString()))+"</NetTotal>\n" // valor sem sem taxas e descontos
    		  + "<GrossTotal>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(6).toString()))+"</GrossTotal>\n" // Valor pago
    		  + "</DocumentTotals>\n"
    		  + "</Payment>";
   }
       
   xmlString +="</Payments>"
   		     + "</SourceDocuments>"      		
      	 	 + "</AuditFile>";
      
		       List_id_loja = new ArrayList<>();
		  
       }
         // xmlString += "</Produtos>";
          xmlString = xmlString.replace("&", "");      
          xmlString = xmlString.replaceAll("\n","");
         // System.out.println(xmlString);
         Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
	         // write the content into xml file
	         Transformer transformer = TransformerFactory.newInstance().newTransformer();
	         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	         // initialize StreamResult with File object to save to file
	         //StreamResult result = new StreamResult(new StringWriter());
	         DOMSource source = new DOMSource(doc);	    
	         nome_file_saft = nome_saft+".xml";
	         StreamResult result = new StreamResult(new File(realPathtoUploads+nome_file_saft));
	         transformer.transform(source, result);
	       	  
	        // Output to console for testing
	         StreamResult consoleResult = new StreamResult(System.out);
	         transformer.transform(source, consoleResult);
	         
	     return nome_file_saft;
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	         return "";
	      }
	   }
	
	

	    
	public  String EscreverXmlAux(HttpServletRequest request,long IDuserlogado,EmpresaRepository _empresa,ClienteRepository _cliente,FacturaAuxRepository _factura,ReciboRepository _recibo,ProdutoRepository _produto,ItemAuxRepository _item,ItemNotaAuxRepository _itemNota,SaftRepository _saft,NotaCreditoRepository _nota_creditoRepository, String data_de,String data_ate) {
		
	      try {
	    	  String ano = data_de.split("-")[0];
	    	  String mes = data_de.split("-")[1];
	    	  String mes2 = data_ate.split("-")[1];
	    	  
	    	  String nome_saft = "SAFT-AO";
	    	  
	    	  if(mes.equals(mes2)) {
	    		  nome_saft=nome_saft+"-"+ano+"_"+objFunc.MesAbreviado(mes)+"_"+ data_de.split("-")[2]+" a "+data_ate.split("-")[2];
	    	  }else {
	    		  nome_saft=nome_saft+"-"+ano+"_"+objFunc.MesAbreviado(mes)+"_"+ data_de.split("-")[2]+" a "+objFunc.MesAbreviado(mes2)+"_"+data_ate.split("-")[2];
	    	  }	    	  
	    	 
	    	  		
	    	  String invoiceNo = "";
	          String xmlString = "<AuditFile xmlns=\"urn:OECD:StandardAuditFile-Tax:AO_1.01_01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
	    	  Iterable <Empresa> empresa = _empresa.findIdUser(IDuserlogado);
	    	 // Iterable <Loja> lojas = _lojaRepository.findAll(); 
	    	  List <Long> List_id_loja =  new ArrayList<>();
	    	  
	    	  String realPathtoUploads =  request.getServletContext().getRealPath("saft/"); 
	    	 	    	 
	    	 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder builder = null;	        
	         builder = factory.newDocumentBuilder();
	         
	      
	       // int num_loja = (int) _lojaRepository.count(), contLoja = 0;
     for(Empresa emp:empresa) {    
  	   nome_saft = emp.getNif()+"_"+nome_saft;
    	 //contLoja = 0;
    xmlString +="<Header><AuditFileVersion>1.01_01</AuditFileVersion>"
    	 		+ "<CompanyID>"+emp.getNome_empresa()+"</CompanyID>"
    	 		+ "<TaxRegistrationNumber>"+emp.getNif()+"</TaxRegistrationNumber>"
    	 		+ "<TaxAccountingBasis>F</TaxAccountingBasis>"
    	 		+ "<CompanyName>"+emp.getNome_empresa()+"</CompanyName>\n"
    	 		+ "<CompanyAddress>\n"
    	 		+ "<AddressDetail>"+emp.getEndereco()+"</AddressDetail>\n"
    	 		+ "<City>LUANDA</City>\n"
    	 		+ "<Province>LUANDA</Province>\n"
    	 		+ "<Country>AO</Country>\n"
    	 		+ "</CompanyAddress>\n"
    	 		+ "<FiscalYear>"+objFunc.AnoActual()+"</FiscalYear>\n"
    	 		+ "<StartDate>"+data_de+"</StartDate>\n"
    	 		+ "<EndDate>"+data_ate+"</EndDate>\n"
    	 		+ "<CurrencyCode>AOA</CurrencyCode>\n"
    	 		+ "<DateCreated>"+objFunc.DataActual()+"</DateCreated>\n"
    	 		+ "<TaxEntity>GLOBAL</TaxEntity>\n"
    	 		+ "<ProductCompanyTaxID>"+emp.getNif()+"</ProductCompanyTaxID>\n"
    	 		+ "<SoftwareValidationNumber>371/AGT/2022</SoftwareValidationNumber>\n" // Trocar depois
    	 		+ "<ProductID>DexaContas/SELECTE MIDIA -SERVICESLDA</ProductID>\n"					// Trocar depois
    	 		+ "<ProductVersion>1.0.0</ProductVersion>\n"
    	 		+ "<Telephone>+"+emp.getTelemovel()+"</Telephone>\n"
    	 		+ "<Email>"+emp.getEmail()+"</Email>"
    	 		+ "</Header>"
    	 		+ "<MasterFiles>\n";
    //"<SoftwareValidationNumber>83/AGT/2019</SoftwareValidationNumber>\n" 
    //	+ "<ProductID>KwanzaGest/GLOBOSOFT-TECN. DE INF. E TELECOMUNICACOES, LDA</ProductID>\n"
    					for(Cliente cli: _cliente.findById_empresa(emp.getId_empresa())) {
    						
    xmlString +="<Customer>\n"
    	 		+ "<CustomerID>"+cli.getId_cliente()+"</CustomerID>\n"
    	 		+ "<AccountID>Desconhecido</AccountID>\n"
    	 		+ "<CustomerTaxID>"+cli.getNif()+"</CustomerTaxID>\n"
    	 		+ "<CompanyName>"+cli.getNome_cliente()+"</CompanyName>\n"
    	 		+ "<BillingAddress>\n"
    	 		+ "<AddressDetail>"+cli.getEndereco()+"</AddressDetail>\n"
    	 		+ "<City>LUANDA</City>\n"
    	 		+ "<PostalCode>Desconhecido</PostalCode>\n"
    	 		+ "<Province>LUANDA</Province>\n"
    	 		+ "<Country>AO</Country>\n"
    	 		+ "</BillingAddress>\n"
    	 		+ "<Telephone>"+cli.getTelemovel()+"</Telephone>\n"
    	 		+ "<Email>"+cli.getEmail()+"</Email>\n"
    	 		+ "<SelfBillingIndicator>0</SelfBillingIndicator>\n"
    	 		+ "</Customer>";
    					}     
    					
    	      for(List prod: _produto.findProdudByCatEmp(emp.getId_empresa())) {	
    	    	
    xmlString +="<Product>\n"
    			+ "<ProductType>P</ProductType>\n"
    			+ "<ProductCode>"+prod.get(0)+"</ProductCode>\n"
    			+ "<ProductGroup>SERVIÇOS</ProductGroup>\n"
    			+ "<ProductDescription>"+prod.get(1)+"</ProductDescription>\n"
    			+ "<ProductNumberCode>000000000"+prod.get(0)+"</ProductNumberCode>\n"
    			+ "</Product>";
    	      }
   xmlString  +="<TaxTable>\n"
  		    + "<TaxTableEntry>\n"
				+ "<TaxType>IVA</TaxType>\n"
				+ "<TaxCode>NOR</TaxCode>\n"
				+ "<Description>Normal</Description>\n"
				+ "<TaxPercentage>14.0000</TaxPercentage>\n"
				+ "</TaxTableEntry>\n"
				+ "<TaxTableEntry>\n"
				+ "<TaxType>IVA</TaxType>\n"
				+ "<TaxCode>OUT</TaxCode>\n"
				+ "<Description>IVA - Regime de Exclusão</Description>\n"
				+ "<TaxPercentage>0.0000</TaxPercentage>\n"
				+ "</TaxTableEntry>\n"
				+ "<TaxTableEntry>\n"
				+ "<TaxType>IVA</TaxType>\n"
				+ "<TaxCode>ISE</TaxCode>\n"
				+ "<Description>Transmissão de bens e serviços não sujeita</Description>\n"
				+ "<TaxPercentage>0</TaxPercentage>\n"
				+ "</TaxTableEntry>\n"
				+ "</TaxTable>"
				+ "</MasterFiles>"
				+ "<SourceDocuments><SalesInvoices>";
   
              String total_credit= "0",total_debit= "0";
   			if(_factura.TotalCreditoMesF_FR(emp.getId_empresa(), data_de,data_ate)!=null) {
   				total_credit = _factura.TotalCreditoMesF_FR(emp.getId_empresa(), data_de,data_ate);
   			}
   			if(_factura.TotalDebitoMesNC(emp.getId_empresa(), data_de,data_ate)!=null) {
   				total_debit = _factura.TotalDebitoMesNC(emp.getId_empresa(), data_de,data_ate);
   			}
   xmlString  +="<NumberOfEntries>"+_factura.countEntradasMesF_FR_NC(emp.getId_empresa(), data_de,data_ate)+"</NumberOfEntries>\n"
              + "<TotalDebit>"+objFunc.formatarFloat(Float.parseFloat(total_debit))+"</TotalDebit>\n"
              + "<TotalCredit>"+objFunc.formatarFloat(Float.parseFloat(total_credit))+"</TotalCredit>\n";
   
   String tax_str ="";
   
   for(FacturaAux fact: _factura.findIdFacturaEmpresaF_FR_NC(emp.getId_empresa(), data_de,data_ate)) {	
  	 String InvoiceStatus ="N";
       if(fact.getEstado().equals("A"))
      	 InvoiceStatus = "A";
       
  	 invoiceNo = fact.getCodigo_factura();
  	 xmlString   +="<Invoice>\n"
		        + "<InvoiceNo>"+invoiceNo+"</InvoiceNo>\n"
		        + "<DocumentStatus>\n"
		        + "<InvoiceStatus>"+InvoiceStatus+"</InvoiceStatus>\n"
		        + "<InvoiceStatusDate>"+fact.getData_hora()+"</InvoiceStatusDate>\n"
		        + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
		        + "<SourceBilling>P</SourceBilling>\n"
		        + "</DocumentStatus>\n"
		        + "<Hash>"+fact.getHash_msg()+"</Hash>\n"
		        + "<HashControl>1</HashControl>\n"
		        + "<Period>"+Integer.parseInt(fact.getData_emissao().split("-")[1])+"</Period>\n"
		        + "<InvoiceDate>"+fact.getData_emissao()+"</InvoiceDate>\n"
		        + "<InvoiceType>"+fact.getTipo()+"</InvoiceType>\n"
		        + "<SpecialRegimes>\n"
		        + "<SelfBillingIndicator>0</SelfBillingIndicator>\n"
		        + "<CashVATSchemeIndicator>0</CashVATSchemeIndicator>\n"
		        + "<ThirdPartiesBillingIndicator>0</ThirdPartiesBillingIndicator>\n"
		        + "</SpecialRegimes>\n"
		        + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
		        + "<SystemEntryDate>"+fact.getData_hora()+"</SystemEntryDate>\n"
		        + "<CustomerID>"+fact.getId_clientefk()+"</CustomerID>\n";
  int line = 1;
 
  
  if(fact.getTipo().equals("NC")) {
		System.out.println("NC "+fact.getId_factura());
	   for(ItemNotasAux item: _itemNota.findItemFactura(emp.getId_empresa(),fact.getId_factura())) {
		String origem =  _nota_creditoRepository.findNotacreditoIdFact(fact.getId_factura()).iterator().next().getOrigem();
		System.out.println("Origem "+ origem);
		  if(!item.getTaxa_prod_nota().equals("00.00")) {
			   tax_str =  "<Tax>\n"
				   		+ "<TaxType>IVA</TaxType>\n"
				   		+ "<TaxCountryRegion>AO</TaxCountryRegion>\n"
				   		+ "<TaxCode>NOR</TaxCode>\n"
				   		+ "<TaxPercentage>"+Double.parseDouble(item.getTaxa_prod_nota())+"</TaxPercentage>\n"
				   		+ "</Tax>\n"
				   		+ "<SettlementAmount>0</SettlementAmount>";
		   }else {
			   tax_str =  "<Tax>\n"
				        + "<TaxType>IVA</TaxType>\n"
				        + "<TaxCountryRegion>AO</TaxCountryRegion>\n"
				        + "<TaxCode>ISE</TaxCode>\n"
				        + "<TaxPercentage>0.0000</TaxPercentage>\n"
				        + "</Tax>\n"
				        + "<TaxExemptionReason>"+item.getMotivo_ise()+"</TaxExemptionReason>\n"
				        + "<TaxExemptionCode>"+item.getCodigo_ise()+"</TaxExemptionCode>\n"
				        + "<SettlementAmount>0.0000</SettlementAmount>\n";
				}
	
	  xmlString   +=  "<Line>\n"
			        + "<LineNumber>"+line+"</LineNumber>\n"
			        + "<ProductCode>"+item.getId_produtofk()+"</ProductCode>\n"
			        + "<ProductDescription>"+item.getDescricao_prod()+"</ProductDescription>\n"
			        + "<Quantity>"+item.getQtd()+"</Quantity>\n"
			        + "<UnitOfMeasure>UN</UnitOfMeasure>\n"
			        + "<UnitPrice>"+item.getPreco()+"</UnitPrice>\n"
			        + "<TaxPointDate>"+fact.getData_emissao()+"</TaxPointDate>\n"
			        +"<References>"
	                +"<Reference>"+origem+"</Reference>"
	                +"</References>"
			        + "<Description>"+item.getDescricao_prod()+"</Description>\n"			        
			        + "<DebitAmount>"+item.getPreco()*item.getQtd()+"</DebitAmount>\n"
			        + tax_str
			        + "</Line>\n";
			       line++;
			       
			       }
		   }else {
			   //---- PARA OUTROS TIPOS COMO FT,FT OU PP
			   for(ItemAux item: _item.findItemFactura(emp.getId_empresa(),fact.getId_factura())) {
				   
				   if(!item.getTaxa_prod_item().equals("00.00")) {
					   tax_str =  "<Tax>\n"
						   		+ "<TaxType>IVA</TaxType>\n"
						   		+ "<TaxCountryRegion>AO</TaxCountryRegion>\n"
						   		+ "<TaxCode>NOR</TaxCode>\n"
						   		+ "<TaxPercentage>"+Double.parseDouble(item.getTaxa_prod_item())+"</TaxPercentage>\n"
						   		+ "</Tax>\n"
						   		+ "<SettlementAmount>0</SettlementAmount>";
				   }else {
					   tax_str =  "<Tax>\n"
						        + "<TaxType>IVA</TaxType>\n"
						        + "<TaxCountryRegion>AO</TaxCountryRegion>\n"
						        + "<TaxCode>ISE</TaxCode>\n"
						        + "<TaxPercentage>0.0000</TaxPercentage>\n"
						        + "</Tax>\n"
						        + "<TaxExemptionReason>"+item.getMotivo_ise()+"</TaxExemptionReason>\n"
						        + "<TaxExemptionCode>"+item.getCodigo_ise()+"</TaxExemptionCode>\n"
						        + "<SettlementAmount>0.0000</SettlementAmount>\n";
						}
				  
				  xmlString   +=  "<Line>\n"
						        + "<LineNumber>"+line+"</LineNumber>\n"
						        + "<ProductCode>"+item.getId_produtofk()+"</ProductCode>\n"
						        + "<ProductDescription>"+item.getDescricao_prod()+"</ProductDescription>\n"
						        + "<Quantity>"+item.getQtd()+"</Quantity>\n"
						        + "<UnitOfMeasure>UN</UnitOfMeasure>\n"
						        + "<UnitPrice>"+item.getPreco()+"</UnitPrice>\n"
						        + "<TaxPointDate>"+fact.getData_emissao()+"</TaxPointDate>\n"
						        + "<Description>"+item.getDescricao_prod()+"</Description>\n"			        
						        + "<CreditAmount>"+item.getPreco()*item.getQtd()+"</CreditAmount>\n"
						        + tax_str
						        + "</Line>\n";
						       line++;
			   }
			   
		   }

 
  xmlString   +="<DocumentTotals>\n"
		        + "<TaxPayable>"+objFunc.formatNumericValue(fact.getTaxa())+"</TaxPayable>\n"
		        + "<NetTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal())-Double.parseDouble(fact.getImpos_retido())-Double.parseDouble(fact.getValor_desconto()))+"")+"</NetTotal>\n"
		        + "<GrossTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal_final()))+"")+"</GrossTotal>\n"
		        + "</DocumentTotals>\n"
		        + "</Invoice>\n";
            
   }
   
   
   //------- Para WORKINGDOCUMENTAS (PROFORMAS,ORCAMENTOS ENTRE OUTROS)
   total_credit= "0"; total_debit= "0";
 		if(_factura.TotalCreditoMesWORK(emp.getId_empresa(), data_de,data_ate)!=null) {
 			total_credit = _factura.TotalCreditoMesWORK(emp.getId_empresa(), data_de,data_ate);
 		}
 		if(_factura.TotalDebitoMesWORK_NC(emp.getId_empresa(), data_de,data_ate)!=null) {
 			// REVER TOTALDEBIT PARA WORKDOCUMENTS
			//	total_debit = _factura.TotalDebitoMesWORK_NC(emp.getId_empresa(), ano,mes);
		}
 		
   xmlString+=  "</SalesInvoices>\n"
  		    + "<WorkingDocuments>\n"
	        	+ "<NumberOfEntries>"+_factura.countEntradasMesWORK(emp.getId_empresa(), data_de, data_ate)+"</NumberOfEntries>\n"
 		        + "<TotalDebit>"+objFunc.formatarFloat(Float.parseFloat(total_debit))+"</TotalDebit>\n"
 		        + "<TotalCredit>"+objFunc.formatarFloat(Float.parseFloat(total_credit))+"</TotalCredit>\n";
   
   for(FacturaAux fact: _factura.findIdFacturaEmpresaWORK(emp.getId_empresa(), data_de,data_ate)) {	
  	 
  	 String InvoiceStatus ="N";
       if(fact.getEstado().equals("A"))
      	 InvoiceStatus = "A";
       
       invoiceNo = fact.getCodigo_factura();
       xmlString+=  "<WorkDocument>"
				    + "<DocumentNumber>"+invoiceNo+"</DocumentNumber>\n"
				    + "<DocumentStatus>\n"
				    +    "<WorkStatus>"+InvoiceStatus+"</WorkStatus>\n"
				    +    "<WorkStatusDate>"+fact.getData_hora()+"</WorkStatusDate>\n"
				    +    "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
				    +    "<SourceBilling>P</SourceBilling>\n"
				    + "</DocumentStatus>\n"
				    + "<Hash>"+fact.getHash_msg()+"</Hash>\n"
				    + "<HashControl>1</HashControl>\n"
				    + "<Period>"+Integer.parseInt(mes)+"</Period>\n"
				    + "<WorkDate>"+fact.getData_emissao()+"</WorkDate>\n"
				    + "<WorkType>"+fact.getTipo()+"</WorkType>\n"
				    + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
				    + "<SystemEntryDate>"+fact.getData_hora()+"</SystemEntryDate>\n"
				    + "<CustomerID>"+fact.getId_clientefk()+"</CustomerID>\n";
       
       int line = 1;
       
       for(ItemAux item: _item.findItemFactura(emp.getId_empresa(),fact.getId_factura())) {
			   
			   if(!item.getTaxa_prod_item().equals("00.00")) {
				   tax_str =  "<Tax>\n"
					   		+ "<TaxType>IVA</TaxType>\n"
					   		+ "<TaxCountryRegion>AO</TaxCountryRegion>\n"
					   		+ "<TaxCode>NOR</TaxCode>\n"
					   		+ "<TaxPercentage>"+Double.parseDouble(item.getTaxa_prod_item())+"</TaxPercentage>\n"
					   		+ "</Tax>\n"
					   		+ "<SettlementAmount>0</SettlementAmount>";
			   }else {
				   tax_str =  "<Tax>\n"
					        + "<TaxType>IVA</TaxType>\n"
					        + "<TaxCountryRegion>AO</TaxCountryRegion>\n"
					        + "<TaxCode>ISE</TaxCode>\n"
					        + "<TaxPercentage>0.0000</TaxPercentage>\n"
					        + "</Tax>\n"
					        + "<TaxExemptionReason>"+item.getMotivo_ise()+"</TaxExemptionReason>\n"
					        + "<TaxExemptionCode>"+item.getCodigo_ise()+"</TaxExemptionCode>\n"
					        + "<SettlementAmount>0.0000</SettlementAmount>\n";
					}
			  
			  xmlString   +=  "<Line>\n"
					        + "<LineNumber>"+line+"</LineNumber>\n"
					        + "<ProductCode>"+item.getId_produtofk()+"</ProductCode>\n"
					        + "<ProductDescription>"+item.getDescricao_prod()+"</ProductDescription>\n"
					        + "<Quantity>"+item.getQtd()+"</Quantity>\n"
					        + "<UnitOfMeasure>UN</UnitOfMeasure>\n"
					        + "<UnitPrice>"+item.getPreco()+"</UnitPrice>\n"
					        + "<TaxPointDate>"+fact.getData_emissao()+"</TaxPointDate>\n"
					        + "<Description>"+item.getDescricao_prod()+"</Description>\n"			        
					        + "<CreditAmount>"+item.getPreco()*item.getQtd()+"</CreditAmount>\n"
					        + tax_str
					        + "</Line>\n";
					       line++;
		   }
           xmlString      +="<DocumentTotals>\n"
			 		        + "<TaxPayable>"+objFunc.formatNumericValue(fact.getTaxa())+"</TaxPayable>\n"
			 		        + "<NetTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal())-Double.parseDouble(fact.getImpos_retido())-Double.parseDouble(fact.getValor_desconto()))+"")+"</NetTotal>\n"
			 		        + "<GrossTotal>"+objFunc.formatNumericValue((Double.parseDouble(fact.getTotal_final()))+"")+"</GrossTotal>\n"
			 		        + "</DocumentTotals>\n"
			 		        + "</WorkDocument>\n";         
   
     }    
   
   //------ PARA OS PAGAMENTOS
      total_credit= "0";
		if(_recibo.TotalCreditoDeAte(emp.getId_empresa(), data_de,data_ate)!=null) {
			total_credit = _recibo.TotalCreditoDeAte(emp.getId_empresa(), data_de,data_ate);
		}
 xmlString+=  "</WorkingDocuments>"
 		      + "<Payments>"
 	       	  + "<NumberOfEntries>"+_recibo.countEntradasDeAte(emp.getId_empresa(), data_de, data_ate)+"</NumberOfEntries>\n"
  		  + "<TotalDebit>0.00</TotalDebit>\n"
  		  + "<TotalCredit>"+objFunc.formatarFloat(Float.parseFloat(total_credit))+"</TotalCredit>\n";
 
  String PaymentRefNo = "";  
 for(List recibo: _recibo.findIdReciboEmpresaAuxDeAte(emp.getId_empresa(), data_de,data_ate)) {	 
	   
	   PaymentRefNo =recibo.get(16)+"";
	 float taxaf =  Float.parseFloat(recibo.get(6).toString())*(Float.parseFloat(recibo.get(10).toString())/100);
 xmlString+=	"<Payment>\n"
  		  + "<PaymentRefNo>"+PaymentRefNo+"</PaymentRefNo>\n"
  		  + "<Period>"+Integer.parseInt(mes)+"</Period>\n"
  		  + "<TransactionDate>"+recibo.get(4)+"</TransactionDate>\n"
  		  + "<PaymentType>RC</PaymentType>\n"
  		  + "<SystemID>"+recibo.get(0)+"</SystemID>\n"
  		  + "<DocumentStatus>\n"
  		  + "<PaymentStatus>N</PaymentStatus>\n"
  		  + "<PaymentStatusDate>"+recibo.get(11)+"</PaymentStatusDate>\n"
  		  + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
  		  + "<SourcePayment>P</SourcePayment>\n"
  		  + "</DocumentStatus>\n"
  		  + "<PaymentMethod>\n"
  		  + "<PaymentAmount>"+recibo.get(6)+"</PaymentAmount>\n"
  		  + "<PaymentDate>"+recibo.get(12)+"</PaymentDate>\n"
  		  + "</PaymentMethod>\n"
  		  + "<SourceID>"+_empresa.findNomeUser(IDuserlogado)+"</SourceID>\n"
  		  + "<SystemEntryDate>"+recibo.get(11)+"</SystemEntryDate>\n"
  		  + "<CustomerID>"+recibo.get(9)+"</CustomerID>\n"
  		  
  		  + "<Line>\n"
  		  + "<LineNumber>1</LineNumber>\n"
  		  + "<SourceDocumentID>\n"
  		  + "<OriginatingON>"+recibo.get(17)+"</OriginatingON>\n"
  		  + "<InvoiceDate>"+recibo.get(8)+"</InvoiceDate>\n"
  		  + "</SourceDocumentID>\n"
  		  + "<SettlementAmount>0</SettlementAmount>\n"
  		  + "<CreditAmount>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(14).toString()))+"</CreditAmount>\n"
  		  + "</Line>\n"
  		  
  		  + "<DocumentTotals>\n"
  		  + "<TaxPayable>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(13).toString()))+"</TaxPayable>\n" // valor soma das taxas 
  		  + "<NetTotal>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(14).toString()))+"</NetTotal>\n" // valor sem sem taxas e descontos
  		  + "<GrossTotal>"+objFunc.formatarFloat(Float.parseFloat(recibo.get(6).toString()))+"</GrossTotal>\n" // Valor pago
  		  + "</DocumentTotals>\n"
  		  + "</Payment>";
 }
     
 xmlString +="</Payments>"
 		     + "</SourceDocuments>"      		
    	 	 + "</AuditFile>";
    
		       List_id_loja = new ArrayList<>();
		  
     }
       // xmlString += "</Produtos>";
        xmlString = xmlString.replace("&", "");      
        xmlString = xmlString.replaceAll("\n","");
       // System.out.println(xmlString);
       Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
	         // write the content into xml file
	         Transformer transformer = TransformerFactory.newInstance().newTransformer();
	         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	         // initialize StreamResult with File object to save to file
	         //StreamResult result = new StreamResult(new StringWriter());
	         DOMSource source = new DOMSource(doc);	    
	         nome_file_saft = nome_saft+".xml";
	         StreamResult result = new StreamResult(new File(realPathtoUploads+nome_file_saft));
	         transformer.transform(source, result);
	       	  
	        // Output to console for testing
	         StreamResult consoleResult = new StreamResult(System.out);
	         transformer.transform(source, consoleResult);
	         
	     return nome_file_saft;
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	         return "";
	      }
	   }
	
	 public String RemovePV(String str_valor){
		 String imp =  str_valor.replace(".", "");
	     return  imp.replace(",",".");       
	}


}
*/