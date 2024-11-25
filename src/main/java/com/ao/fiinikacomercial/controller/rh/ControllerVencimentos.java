package com.ao.fiinikacomercial.controller.rh;

import java.io.IOException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ao.fiinikacomercial.funcoes.Funcoes;
import com.ao.fiinikacomercial.model.facturacao.Usuario;
import com.ao.fiinikacomercial.model.rh.Vencimentos;
import com.ao.fiinikacomercial.repository.facturacao.CategoriaRepository;
import com.ao.fiinikacomercial.repository.rh.AusenciaRepository;
import com.ao.fiinikacomercial.repository.rh.DeducaoRepository;
import com.ao.fiinikacomercial.repository.rh.FuncionarioRepository;
import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.RetribuicaoRepository;
import com.ao.fiinikacomercial.repository.rh.VencimentoRepository;
import com.ao.fiinikacomercial.security.AuthenticatedUser;
import com.ao.fiinikacomercial.service.Repositorio;

@MultipartConfig
@Controller
public class ControllerVencimentos {
	
	@Autowired
	private AusenciaRepository ausenciaRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private DeducaoRepository deducaoRepository;
	@Autowired
	private FuncionarioRepository funcionarioRepository;
	@Autowired
	private ProcessamentoRepository processamentoRepository;
	@Autowired
	private RetribuicaoRepository retribuicaoRepository;
	@Autowired
    public AuthenticatedUser authenticatedUser;
	@Autowired
    public Repositorio R;

	Funcoes objFunc  = new Funcoes();
	
	UserDetails userDetails;
	@Autowired
	private VencimentoRepository vencimentoRepository;
	
	
	
	//DISPLAY PROCESSAMENTOS 
		@RequestMapping("/fiinika/salarios/recibo/{id}")
		public String displayProcessamentosPage(Model model, @PathVariable int id, HttpServletRequest request, RedirectAttributes attribute,HttpSession session,HttpServletResponse response) throws IOException {

			 Object objSessao =  authenticatedUser.getAuthenticatedUserDetails2();
			   if(objSessao!=null) {
					userDetails = (UserDetails) objSessao;
				    String []  dadosUser= userDetails.getUsername().split("_");
				    long id_user = Long.parseLong(dadosUser[0]);
					Usuario user = R.userRepository.findById(id_user).orElse(null);
					authenticatedUser.Dados(model, session, response);
										
					for(Vencimentos vencimento : vencimentoRepository.findById(id)) {
						model.addAttribute("processamento", processamentoRepository.findById(vencimento.getProcessamento_id()));
						model.addAttribute("deducoes", deducaoRepository.findAllDeducoes(user.getIdEmpresaUser()));
						model.addAttribute("retribuicoes", retribuicaoRepository.findAllRetribuicoes(user.getIdEmpresaUser()));
					}					
					
					model.addAttribute("vencimento", vencimentoRepository.findById(id));
					model.addAttribute("id_aux", id);
					
					
				
			return "/fiinika/salarios/recibo";
			   }else {
			
			attribute.addFlashAttribute("mensagem", "A sessão de login expirou!");
			return "redirect:/index";
			
			}
			

		}
	

}
