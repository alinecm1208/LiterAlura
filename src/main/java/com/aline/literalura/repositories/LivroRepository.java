package com.aline.literalura.repositories;

import com.aline.literalura.model.Linguagem;
import com.aline.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {


    boolean existsByTitle(String title);

    List<Livro> findByLinguagem(List<Linguagem> linguagens);
}
