package org.generation.blogPessoal.repository;



import org.generation.blogPessoal.model.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

}
