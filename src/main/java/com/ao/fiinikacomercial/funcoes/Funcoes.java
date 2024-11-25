package com.ao.fiinikacomercial.funcoes;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.ao.fiinikacomercial.service.Repositorio;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;






public class Funcoes {
	
	// final String uuid = UUID.randomUUID().toString().replace("-", "");	
	  
	@Autowired
	Repositorio R;
	
    String[] bancos = {
		    "Banco Angolano de Investimento",
		    "Banco BIC",
		    "Banco Yetu",
		    "Banco Sol",
		    "Banco de Negócios Internacional",
		    "Banco de Desenvolvimento de Angola",
		    "Banco Comercial Angolano, S.A",
		    "Banco Comercial do Huambo",
		    "Banco de Fomento Angola",
		    "Banco de Poupança e Crédito",
		    "Banco Económico",
		    "Banco Regional do Keve",
		    "Banco de Investimento Rural",
		    "Banco Millennium Atlântico",
		    "Banco de Crédito do Sul, S.A.",
		    "Banco VTB África S.A.",
		    "Finibanco Angola, S.A",
		    "Standard Bank Angola",
		    "Banco Caixa Geral Angola",
		    "Banco da China Lda - Sucursal de Luanda",
		    "Banco Valor S.A",
		    "Banco Prestígio",
		    "Banco BAI Micro Finanças S.A.",
		    "Banco de Comércio e Indústria"
		};
	
	String[] siglas = {"BAI", "BIC", "YETU", "SOL", "BNI", "BDA", "BCA", "BCH", "BFA", "BPC", "BE", "BRK", "BIR", "BMA", "BCS", "VTB", "FNB", "SBA", "BCGA", "BOCLB", "BVB", "BPG", "BMF", "BCI"};
	
	private final Pattern IBAN_PATTERN = Pattern.compile("AO\\d{2}[.]\\d{4}[.]\\d{4}[.]\\d{4}[.]\\d{4}[.]\\d{4}[.]\\d");
	private final Pattern patternIban = Pattern.compile("^AO.{23}$");
	
	 public String singleFileUpload (HttpServletRequest request,MultipartFile file, String pasta) {
	        String novoNome="sem-foto.png";
	        if(!(file.isEmpty())) {
	        try {
	             String realPathtoUploads =  request.getServletContext().getRealPath("imgs/");
	             novoNome = new Date().getTime() +file.getOriginalFilename().substring(file.getOriginalFilename().length()-5);
	             novoNome = novoNome.replace(")", "");
	                String filePath = realPathtoUploads +pasta +"/"+ novoNome;
	                 File dest = new File(filePath);
	                 file.transferTo(dest);    
	            } catch (IOException e) {
	                
	                System.out.println("ERRO DE UPLOADS: "+ e.getMessage());
	            }}
	         return novoNome;  

	    }
	 
	 public String singleFileUpload (HttpServletRequest request,MultipartFile file, String pasta,String sameName) {
	        String novoNome="sem-foto.png";
	        if(!(file.isEmpty())) {
	        try {
	             String realPathtoUploads =  request.getServletContext().getRealPath("imgs/");
//	             novoNome = new Date().getTime() +file.getOriginalFilename().substring(file.getOriginalFilename().length()-5);
//	             novoNome = novoNome.replace(")", "");
	                String filePath = realPathtoUploads +pasta +"/"+ sameName;
	                 File dest = new File(filePath);
	                 file.transferTo(dest);    
	            } catch (IOException e) {
	                
	                System.out.println("ERRO DE UPLOADS: "+ e.getMessage());
	            }}
	         return sameName;  

	    }
	 
	 public String BigsingleFileUpload(HttpServletRequest request,MultipartFile file, String pasta) {
	        String novoNome="sem-foto.png";
	        if(!(file.isEmpty())) {
	        try {
	             String realPathtoUploads =  request.getServletContext().getRealPath("arquivos/");
	             novoNome = new Date().getTime() +file.getOriginalFilename().substring(file.getOriginalFilename().length()-5);
	             novoNome = novoNome.replace(")", "");
	                String filePath = realPathtoUploads +pasta +"/"+ novoNome;
	                 File dest = new File(filePath);
	                 file.transferTo(dest);    
	            } catch (IOException e) {
	                
	                System.out.println("ERRO DE UPLOADS: "+ e.getMessage());
	            }}
	         return novoNome;  

	    }
	 
	 public String GeraCodigo(int len) { 
			
			char [] chart = {'0','1','2','3','4','5','6','7','8','9'};
		    char [] codigo = new char[len];
		    int charLength = chart.length;
		    Random rdm = new Random();
		    for(int x = 0; x<len; x++) {
		    	codigo[x]=chart[rdm.nextInt(charLength)];
		    }
			
			return  new String(codigo);
		}
	 
	 
	 

	 public static Response enviarSmsSingle(String telefone, String msg) throws IOException{

		 String xmlString = "<SMS> "
		         + "<authentification> <username>sservices</username> <password>smsillico</password> </authentification>"
		         + "<message> <sender>Fiinika</sender>  "
		         + "<text>"
		         + msg
		         + "</text> </message> <recipients> "
		         + "<gsm>"
		         + telefone
		         + "</gsm>"
		         + " </recipients> </SMS>";

		           
		     
		     OkHttpClient client = new OkHttpClient();

		     MediaType mediaType = MediaType.parse("application/xml");
		     RequestBody body = RequestBody.create(mediaType, xmlString);
		  
		     Request request = new Request.Builder()
		         .url("https://api.smsillico-ao.com/sendsms/xml")
		         .post(body)
		         .addHeader("Accept", "application/json")
		         .addHeader("Content-Type", "application/json")
		         .build();		  
		         
		             return client.newCall(request).execute();
		             
		          
		     }
	 
	 
	 //---- Mes Reverso
	 public String MesReverso(String valor) {
		 String mes="";
	  switch (valor) {
			case "Janeiro":	mes="01"; break;
			case "Fervereiro":mes="02"; break;
			case "Março":	mes="03"; break;
			case "Abril":	mes="04"; break;
			case "Maio":	mes="05"; break;
			case "Junho":	mes="06"; break;
			case "Julho":	mes="07"; break;
			case "Agosto":	mes="08"; break;
			case "Setembro":mes="09"; break;
			case "Outubro":	mes="10"; break;
			case "Novembro":mes="11"; break;
			case "Dezembro":mes="12"; break;
				
			default:
				break;
			}
	  return mes;
	}
	 
	 public String DataActual() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		
		 return formatter.format(date);
	}
	 
	 public Date DataActual(int d) {
		 
		 Date date = new Date();
		
		 return date;
	}
	 
	 public String AnoMesDia(String data) {
		
		String [] dataR = data.split("-");
		 return dataR[2]+"-"+dataR[1]+"-"+dataR[0];
	}
	 
	 public String DataTimeActual() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		 return formatter.format(date);
	}
	 public String DataTimeActual2() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		 return formatter.format(date);
	}
	 //--------------------------------------------
	 public String Data() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
		
		 return formatter.format(date).substring(6, 10);
	}
	 
	 public String AnoActual() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
		
		 return formatter.format(date).substring(6, 10);
	}
	 
	 public String HoraActual() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		 return formatter.format(date).substring(11);
	}
	 
	 public String AnoAnterior() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
		//Create calendar instance
		    Calendar instance = Calendar.getInstance();
		//Set your date to it
		    instance.setTime(date);
		//Substract one year from it
		    instance.add(Calendar.YEAR, -1);
		  
		 return formatter.format(instance.getTime()).substring(6, 10);
	}
	 
	 public String MesActual() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
		
		 return formatter.format(date).substring(3, 5);
	}
	 
	 public String MesAnterior() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy");
		 
		    Calendar cal = Calendar.getInstance();  
		    cal.setTime(date);  
		    cal.set(Calendar.DAY_OF_MONTH, 1);  
		    cal.add(Calendar.DATE, -1);

		    Date preMonthDate = cal.getTime();  
		 
		
		 return formatter.format(preMonthDate).substring(3, 5);
	}
	 
	 
	 public String AddDays(String data_entrada,int num_dias) throws ParseException{
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");	    
		  
		    
		    Calendar calendar = Calendar.getInstance();
		    if(data_entrada!=null) { 
		       Date d1 = dateFormat.parse(data_entrada);
		       calendar.setTime(d1);
		    }
		    else {
		       calendar.setTime(new Date());
		    }
		   // System.out.println(sdf1.format(calendar.getTime()));
		    calendar.add(Calendar.DATE,num_dias);
		   // System.out.println(sdf2.format(calendar.getTime()));
		    
		    return sdf2.format(calendar.getTime());
		    
		}
	 
	 public String SubtraiDias(String data,int dias) throws ParseException{
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
		 cal.setTime(sdf.parse(data));
			cal.add(Calendar.DATE, -dias);
			//System.out.println(sdf.format(cal.getTime()));
			return sdf.format(cal.getTime());

		 }
	 
	 public boolean VerifData(String dateString1,String dateString2) throws ParseException {
	       

	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        Date date1 = sdf.parse(dateString1);
	        Date date2 = sdf.parse(dateString2);

	        if (date1.compareTo(date2) > 0) {
	            return false;
	        } else if (date1.compareTo(date2) < 0) {
	        	return true;
	        } else {
	        	return true;
	        }
	    }
	 
	 public String Mes(String valor) {
		 String mes="";
	  switch (valor) {
			case "01":	mes="Janeiro"; break;
			case "02":	mes="Fevereiro"; break;
			case "03":	mes="Março"; break;
			case "04":	mes="Abril"; break;
			case "05":	mes="Maio"; break;
			case "06":	mes="Junho"; break;
			case "07":	mes="Julho"; break;
			case "08":	mes="Agosto"; break;
			case "09":	mes="Setembro"; break;
			case "10":	mes="Outubro"; break;
			case "11":	mes="Novembro"; break;
			case "12":	mes="Dezembro"; break;
				
			default:
				break;
			}
	  return mes;
 }
	 
	 public String GeraReferencia() {
		 Date date = new Date();
		 SimpleDateFormat formatter= new SimpleDateFormat("HHmmssddMM");
		
		 return formatter.format(date);
	}
	 
	 public String geraRef() {
	       String R = new SimpleDateFormat("HHmsddMMyy").format(new Date()).substring(0, 9);

			return R;
	}
	 
	 public String MesAbreviado(String valor) {
		 String mes="";
	  switch (valor) {
			case "01":	mes="Jan"; break;
			case "02":	mes="Fev"; break;
			case "03":	mes="Mar"; break;
			case "04":	mes="Abr"; break;
			case "05":	mes="Mai"; break;
			case "06":	mes="Jun"; break;
			case "07":	mes="Jul"; break;
			case "08":	mes="Ago"; break;
			case "09":	mes="Set"; break;
			case "10":	mes="Out"; break;
			case "11":	mes="Nov"; break;
			case "12":	mes="Dez"; break;
				
			default:
				break;
			}
	  return mes;
 }
	 
	 public  static String MesStatic(String valor) {
		 String mes="";
	  switch (Integer.parseInt(valor)+"") {
			case "1":	mes="Janeiro"; break;
			case "2":	mes="Fevereiro"; break;
			case "3":	mes="Março"; break;
			case "4":	mes="Abril"; break;
			case "5":	mes="Maio"; break;
			case "6":	mes="Junho"; break;
			case "7":	mes="Julho"; break;
			case "8":	mes="Agosto"; break;
			case "9":	mes="Setembro"; break;
			case "10":	mes="Outubro"; break;
			case "11":	mes="Novembro"; break;
			case "12":	mes="Dezembro"; break;
				
			default:
				break;
			}
	  return mes;
 }
	 
	 public Object[]  Meses() {
	    	
	   	 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
	   
			 for(int i=1; i <=12; i++ ) {	
				 Listmeses.add(MesStatic(i+""));
				} 
			 Object[] meses = Listmeses.toArray();			 
		
				 return meses;
			
		 }
	 public Object[]  ListaMeses() {
	    	
	   	 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
	   
			 for(int i=1; i <=12; i++ ) {	
				 String numeroMes = (i < 10) ? "0" + i : String.valueOf(i);
				 Listmeses.add(numeroMes);
				} 
			 Object[] meses = Listmeses.toArray();			 
		
				 return meses;
			
		 }
	 public Object[]  Mes() {
	    	
	   	 List<Object> Listmeses = new ArrayList<>(); // q vai pegar todos os meses das despesas
	   
			 for(int i=1; i <=12; i++ ) {	
				 Listmeses.add(i);
				} 
			 Object[] meses = Listmeses.toArray();			 
		
				 return meses;
			
		 }
	 
	   public static String FormatDate(String data) {
		   if(!data.equals("")) {
			   String [] str = data.split("-");
			   return str[2]+"/"+str[1]+"/"+str[0];
		   } 
			 return null;
		}
		 
	   public static String Codifica(Long id) {
		 
		 String str = DatatypeConverter.printBase64Binary(id.toString().getBytes());			
		 return str;
		 }
		 
	 
	public static Long Decodifica(String id) {
			 
		 String res = new String(DatatypeConverter.parseBase64Binary(id));
		
		 return Long.parseLong(res);
		 
		 }
	
	   public static String Calclula(String taxa,Float preco,int qtd) {
		   
		   double valor = (Double.parseDouble(taxa)/100)*(preco*qtd);
		  
			 return valor+"";
		}
	
	
	public String [] CodigoDocumentos(Repositorio R,String tipo,long id_empresa) {					 
        
		 
		 Long increment = (long)1;			 
		 String Newcodigo=AnoActual()+"/"+1;
		 String [] ret = new String[2];
		 ret[0] = increment+"";
		 ret[1] = Newcodigo;
		 
         List <List> increment_cod = null;
		 
		 if(tipo.equals("DP")) {
			 increment_cod = R.despesaRepository.findLastIncrement(id_empresa,AnoActual());
		 }else if(tipo.equals("RC")) {
			 increment_cod = R.reciboRepository.findLastIncrementRecibo(id_empresa,AnoActual());
		 }else {
			 increment_cod = R.facturaRepository.findLastIncrement(id_empresa,AnoActual(),tipo);
		 }
		
		 
		 for(List l: increment_cod) {			
				increment=Long.parseLong(l.get(0).toString());	
				if(!AnoActual().equals(l.get(2).toString())) {
				 Newcodigo=AnoActual()+"/"+1;
				 increment=(long)1;
				}
			 }
		
		
		 if(!increment_cod.toString().equals("[]")) {		   			       
			 increment=increment+1;
			 Newcodigo =AnoActual()+"/"+increment;
			 ret[0] = increment+"";
			 ret[1] = Newcodigo;
			 
			 return ret;
			 
		 }else {				  
			 return ret;
			 }
		 
	 }

	 
	 
	 public long DiferencaDatas(String data1,String data2) throws ParseException {
		 
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		 Date d1 = dateFormat.parse(data1);
		 Date d2 = dateFormat.parse(data2);
		 long dif_mil_seg = d2.getTime() - d1.getTime();
		  
		 long dias = TimeUnit.DAYS.convert(dif_mil_seg, TimeUnit.MILLISECONDS);
		 return dias;
		
		 }
	 
	public 	List <List> AnosFrequencia(){
		
		 List<List> ListMain = new ArrayList<>();
		 
		List ano1 = new ArrayList<> ();
		List ano2 = new ArrayList<> ();
		List ano3 = new ArrayList<> ();
		List ano4 = new ArrayList<> ();
		List ano5 = new ArrayList<> ();
		List ano6 = new ArrayList<> ();
		
		ano1.add("1º Ano");
		ano2.add("2º Ano");
		ano3.add("3º Ano");
		ano4.add("4º Ano");
		ano5.add("5º Ano");
		ano6.add("6º Ano");
		
		 ListMain.add(ano1);
		 ListMain.add(ano2);
		 ListMain.add(ano3);
		 ListMain.add(ano4);
		 ListMain.add(ano5);
		 ListMain.add(ano6);
			
			return ListMain;
			
			 }

	public Object[] Classes(){
		
		 List<Object>ListMain = new ArrayList<>();
		
		 ListMain.add("10º Classe");
		 ListMain.add("11º Classe");
		 ListMain.add("12º Classe");
		 ListMain.add("13º Classe");
	
		 Object[] classes = ListMain.toArray();
			return classes;
			
			 }
	
	public 	List <List> Periodos(){
		
		 List<List> ListMain = new ArrayList<>();
		 
		List p1 = new ArrayList<> ();
		List p2 = new ArrayList<> ();
		List p3 = new ArrayList<> ();
		
		
		p1.add("Manhã");
		p2.add("Tarde");
		p3.add("Noite");
		
		
		 ListMain.add(p1);
		 ListMain.add(p2);
		 ListMain.add(p3);
			
			return ListMain;
			
			 }
	
	 public  String formatNumericValue(String input) {
	        // Verificar se a string é numérica
	        try {
	            Double.parseDouble(input);
	        } catch (NumberFormatException e) {
	            // Tratar erro se a string não for numérica
	            return "Valor não numérico";
	        }

	        // Configurar o formato para usar ponto como separador decimal
	        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
	        symbols.setDecimalSeparator('.');
	        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);

	        // Formatar o valor com duas casas decimais
	        double numericValue = Double.parseDouble(input);
	        String formattedValue = decimalFormat.format(numericValue);

	        return formattedValue;
	    }
	
	public String formatarFloat(float numero){
		System.out.println(numero);
		  String retorno = "";
		  DecimalFormat formatter = new DecimalFormat("#.00");
		  try{
		    retorno = formatter.format(numero);
		  }catch(Exception ex){
		    System.err.println("Erro ao formatar numero: " + ex);
		  }
		  if(numero == 0.0)
			  return "0.00";
		  
		  return retorno.replace(",", ".");
		}
	
	public  String formatNumber(String number) {
		if(number!=null){
			NumberFormat nf = new DecimalFormat("##.##");
		    return nf.format(Float.parseFloat(number));
		}
		return null;
	}
	
	public static String Pos_Neg(String number) {
	    NumberFormat nf = new DecimalFormat("##.###");
	    if(number!=null){
	    if(Double.parseDouble(number) >= 10) {
	    	return "media";
	    }
	    }
	    return "media negativa";
	}
	 


	public static String A_R(String number) {
	    NumberFormat nf = new DecimalFormat("##.###");
	    if(number!=null){
	    if(Double.parseDouble(number) >= 10) {
	    	return null;
	    }
	    }
	    return "reprovado";
	}
	
	public String RemovePV(String str_valor){
		 String imp =  str_valor.replace(".", "");
	    return  imp.replace(",",".");       
	}
	
	public String FormataData(String data) {
		 String [] s=data.split("-");
		 String newData =s[2]+"-"+s[1]+"-"+s[0];
		
		 return newData;
	}
	
	public boolean isDigit(String s) {
	    return s.matches("[0-9]*");
	}
	
	public  boolean isValidEmailAddressRegex(String email) {
	    boolean isEmailIdValid = false;
	    if (email != null && email.length() > 0) {
	        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	        Matcher matcher = pattern.matcher(email);
	        if (matcher.matches()) {
	            isEmailIdValid = true;
	        }
	    }
	    return isEmailIdValid;
	}
	
	public boolean validarNome(String nome) {
		   // Verifica se o nome é nulo ou vazio
		   if (nome == null || nome.trim().isEmpty()) {
		       return false;
		   }
		   
		   // Verifica se o nome tem pelo menos 3 caracteres
		   if (nome.length() < 3) {
		       return false;
		   }
		   
		   // Define uma expressão regular para verificar se o nome começa com letra ou número
		   String regex = "^[a-zA-Z0-9].*$";
		   
		   // Verifica se o nome corresponde à expressão regular
		   if (!nome.matches(regex)) {
		       return false;
		   }
		   
		   // Se passar por todas as validações, retorna verdadeiro
		   return true;
		}
	
	public boolean isNullEmpty(String nome) {
		   // Verifica se o nome é nulo ou vazio
		   if (nome == null || nome.trim().isEmpty()) {
		       return false;
		   }
	 
		   return true;
	}
	
	public boolean isCurrencyFormat(String value) {
	    String currencyRegex = "\\d{1,3}(\\.\\d{3})*,\\d{2}";
	    return value.matches(currencyRegex);
	}
	
	public boolean validateIban(String iban) {
	    return iban != null && IBAN_PATTERN.matcher(iban).matches();
	}
	
	public boolean validateIban2(String iban) {
		
	  Matcher matcher = patternIban.matcher(iban);
      if (matcher.matches()) {
    	  return true;
         // System.out.println("A string começa com 'AO' e tem 25 caracteres.");
      } else {
         // System.out.println("A string não atende aos critérios.");
    	  return false;
      }
      
	}
	
	public boolean validateSiglaBanco(String sigla) {
		for(int i=0; i< siglas.length; i++) {
			if(siglas[i].equals(sigla)) {
			   return true;
			}
		}
	    return false;
	}
	
	public  boolean isValidDate(String data) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    
	    try {
	      LocalDate.parse(data, formatter);
	      return true;
	    } catch (DateTimeParseException e) {
	      return false;
	    }
	  }
	
	  public String geraUUID() {		
		  String d = UUID.randomUUID().toString().replace("-", "");		        
		  return d;
	}
	  
	  public static boolean verificaNumero(String numero) {
	        // Verifica se o número tem 9 dígitos e começa com 9
	        if (numero.length() == 9 && numero.charAt(0) == '9' && numero.matches("[0-9]+")) {
	            return true;
	        } else {
	            return false;
	        }
	  }
	  
	  public  String extrairCaracteres(String entrada) {
	        // Verifica se a string tem pelo menos 31 caracteres
	        if (entrada.length() >= 31) {
	            // Extrai os caracteres especificados
	            char caractere1 = entrada.charAt(0);      // Primeiro caractere
	            char caractere11 = entrada.charAt(10);    // Décimo primeiro caractere
	            char caractere21 = entrada.charAt(20);    // Vigésimo primeiro caractere
	            char caractere31 = entrada.charAt(30);    // Trigésimo primeiro caractere

	            // Forma a nova string com os caracteres extraídos
	            String novaString = "" + caractere1 + caractere11 + caractere21 + caractere31;

	            return novaString;
	        } else {
	            // A string não tem caracteres suficientes para realizar as extrações
	            return "";
	        }
	    }
	  
	  public String currentUrlServer (HttpServletRequest request) {
			 String serverName = request.getServerName();            
		     // Obtém o esquema (protocolo) usado (http ou https)
		     String scheme = request.getScheme();            
		     // Obtém a porta do servidor
		     int serverPort = request.getServerPort();            
		     // Monta a URL principal com o protocolo HTTP e a porta, se diferente da porta padrão (80 para HTTP, 443 para HTTPS)
		     String url;
		     if (("http".equals(scheme) && serverPort == 80) || ("https".equals(scheme) && serverPort == 443)) {
		         url = scheme + "://" + serverName;
		     } else {
		         url = scheme + "://" + serverName + ":" + serverPort;
		     }
		   
		   return url;
		 }
}
