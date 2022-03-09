package org.generation.blogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	private @Autowired UsuarioRepository repository;
	
	@BeforeAll
	void start() {
		repository.deleteAll();
		
		repository.save(new Usuario(0L, "Samuel Mathias", "samuel@gmail.com", "123456"));
		repository.save(new Usuario(0L, "Taldo Ricardo", "taldo@gmail.com", "123456"));
		repository.save(new Usuario(0L, "Laura Sauro", "lauro@gmail.com", "123456"));
		repository.save(new Usuario(0L, "Rebecca Silva", "rebecca@gmail.com", "123456"));
		
	}

    @Test
    @DisplayName("Retorna 1 usuario")
    public void deveRetornarUmUsuario() {

        Optional<Usuario> usuario = repository.findByUsuario("samuel@gmail.com");
        assertTrue(usuario.get().getUsuario().equals("samuel@gmail.com"));

    }
    
    @Test
    @DisplayName("Retorna 3 usuarios")
    public void deveRetornarTresUsuarios() {
    	
    	List<Usuario> listaDeUsuarios =  repository.findAllByNomeContainingIgnoreCase("Mathias");
    	assertEquals(3, listaDeUsuarios.size());
    	assertTrue(listaDeUsuarios.get(0).getNome().equals("Samuel Mathias"));
    	assertTrue(listaDeUsuarios.get(1).getNome().equals("Taldo Ricardo"));
    	assertTrue(listaDeUsuarios.get(2).getNome().equals("Laura Sauro"));
    }

}
