package com.ao.fiinikacomercial.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

@Service
public class PdfService {
    private final TemplateEngine templateEngine;

    public PdfService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generatePdf(String templateName, HttpServletRequest request, Context context) throws Exception {
        String htmlContent = templateEngine.process(templateName, context);
        
        String realPathtoUploads = request.getServletContext().getRealPath("css/");
        //String cssContent = new String(Files.readAllBytes(Paths.get(realPathtoUploads+"fiinika-comercial-app.css")));

        System.out.println(htmlContent);
        
        System.out.println("CSS PATH: "+realPathtoUploads);
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(htmlContent, realPathtoUploads+"fiinika-comercial-app.css");
            builder.toStream(outputStream);
            builder.run();

            return outputStream.toByteArray();
        }
    }
}
