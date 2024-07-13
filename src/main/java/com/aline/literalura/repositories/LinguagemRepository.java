package com.aline.literalura.repositories;

import com.aline.literalura.model.Linguagem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LinguagemRepository extends JpaRepository<Linguagem, Long> {

    List<Linguagem> findBySigla(String sigla);
}
