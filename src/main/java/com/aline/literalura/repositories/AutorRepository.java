package com.aline.literalura.repositories;

import com.aline.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long > {

    @Query("SELECT a FROM Autor a WHERE a.deathYear > :year AND a.birthYear <= :year")
    List<Autor> findAutoreVivosByAno(Integer year);
}
