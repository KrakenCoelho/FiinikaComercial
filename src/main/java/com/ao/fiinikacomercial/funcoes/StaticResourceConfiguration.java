package com.ao.fiinikacomercial.funcoes;
/*package com.ao.dexaconta.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration 
public class StaticResourceConfiguration implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		String caminho="imgs/";
		WebMvcConfigurer.super.addResourceHandlers(registry); 
		
		registry.addResourceHandler("/imgs/**").addResourceLocations("file:"+System.getProperty("user.dir")+"/" +caminho);
		registry.addResourceHandler("../images/**").addResourceLocations("classpath:/static/");
		
	}

}
*/