package com.example.literatura.repository;

import com.example.literatura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByIdioma(String idioma);
    Optional<Livro> findByTitulo(String titulo);
    Optional<Livro> findByTituloIgnoreCase(String titulo);
}

