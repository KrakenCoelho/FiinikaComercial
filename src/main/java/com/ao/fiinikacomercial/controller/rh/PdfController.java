package com.ao.fiinikacomercial.controller.rh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.Context;

import com.ao.fiinikacomercial.repository.rh.ProcessamentoRepository;
import com.ao.fiinikacomercial.repository.rh.VencimentoRepository;
import com.ao.fiinikacomercial.service.PdfService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;

@Controller
public class PdfController {
    @Autowired
    private PdfService pdfService;
	@Autowired
	private ProcessamentoRepository processamentoRepository;
	@Autowired
	private VencimentoRepository vencimentoRepository;

    @GetMapping("/download-pdf/{id}")
    @ResponseBody
    public void downloadPdf(HttpServletResponse response, HttpServletRequest request, @PathVariable int id) throws IOException {
        Context context = new Context();
        // Add attributes to the context
        context.setVariable("processamento", processamentoRepository.findById(id));
        context.setVariable("vencimentos", vencimentoRepository.findByProcessamentoId(id));

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=doc-banco.pdf");

        try {
            byte[] pdfBytes = pdfService.generatePdf("/fiinika/salarios/doc-banco", request, context);
            response.getOutputStream().write(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/pdf-page")
    public String pdfPage() {
        return "pdf-page";
    }
}
