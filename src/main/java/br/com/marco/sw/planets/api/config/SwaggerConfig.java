package br.com.marco.sw.planets.api.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Header;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private final ResponseMessage MSG_POST_201 = customMessage201();
	private final ResponseMessage MSG_PUT_204 = simpleMessage(204, "Autorização ok");
	private final ResponseMessage MSG_DEL_204 = simpleMessage(204, "Deleção ok");
	private final ResponseMessage MSG_400 = simpleMessage(400, "Erro de validação");
	private final ResponseMessage MSG_403 = simpleMessage(403, "Não autorizado");
	private final ResponseMessage MSG_404 = simpleMessage(404, "Não encontrado");
	private final ResponseMessage MSG_500 = simpleMessage(404, "Erro inesperado");
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, Arrays.asList(MSG_403, MSG_404, MSG_500))
				.globalResponseMessage(RequestMethod.POST, Arrays.asList(MSG_POST_201, MSG_400, MSG_403, MSG_500))
				.globalResponseMessage(RequestMethod.PUT, Arrays.asList(MSG_PUT_204, MSG_400, MSG_403, MSG_404, MSG_500))
				.globalResponseMessage(RequestMethod.DELETE, Arrays.asList(MSG_DEL_204, MSG_403, MSG_404, MSG_500))
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.marco.sw.planets.api.controllers"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo());
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfo(
		"API de Planetas de Star Wars",
		"Esta API é utilizada para cadastro de Planetas que aparecem nos filmes de Star Wars",
		"Versão 1.0",
		"http://localhost:8080/api/planets",
		new Contact("Marco Aurélio Carneiro Rosa", "http://localhost:8080/api/planets", "marcocarneirorosa@gmail.com"),
		"Permitido uso para todos",
		"http://localhost:8080/api/planets",
		Collections.emptyList()
		);
	}
	
	private ResponseMessage simpleMessage(int code, String msg) {
		return new ResponseMessageBuilder().code(code).message(msg).build();
	}
	
	private ResponseMessage customMessage201() {
		Map<String, Header> map = new HashMap<>();
		map.put("location", new Header("location", "URI do novo recurso", new ModelRef("string")));
		
		return new ResponseMessageBuilder()
		.code(201)
		.message("Recurso criado")
		.headersWithDescription(map)
		.build();
	}
}
