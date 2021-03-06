package org.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.repository.UsuarioRepository;
import org.generation.blogPessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {
	
	private @Autowired TestRestTemplate testRestTemplate;
	private @Autowired UsuarioService usuarioService;
	private @Autowired UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
	}
	
	@Test
	@Order(1)
	@DisplayName("Cadastrar Um Usuário")
	public void deveCriarUmUsuario() {
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Rebecca Silva","foto", "rebecca@gmail.com", "123456", "adm", "biografia"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());	
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação de Usuários")
	public void naoDeveDuplicarUsuarios() {
		
		usuarioService.cadastrarUsuario(new Usuario(0L, "Rebecca Silva","foto", "rebecca@gmail.com", "123456", "normal", "biografia"));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<>(new Usuario(0L, "Rebecca Silva","foto", "rebecca@gmail.com", "123456", "adm", "biografia"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Alterar um usuario")
	public void deveAtualizarUmUsuario() {
		Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario (0L, "Juliana Andrews", "foto", "juliana@email.com.br", "juliana123", "adm", "biografia"));
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
				"Juliana Andrews Ramos", "foto", "juliana_ramos@email.com", "juliana123", "adm", "biografia");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario()); 
		
	} 
	
	@Test
	@Order(4)
	@DisplayName("Listar todos as Postagens")
	public void deveMostrarTodasPostagens() {
		usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "foto", "sabrina@email.com", 
				"sabrina123", "normal", "biografia"));			
		
		usuarioService.cadastrarUsuario(new Usuario (0L, "Ricardo Marques", "foto", "ricardo@email.com","ricardo123", "adm", "biografia"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("sabrina@email.com", "sabrina123")
				.exchange("/postagens", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

}
