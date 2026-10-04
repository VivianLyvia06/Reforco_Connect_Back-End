package com.vivian.reforco_connect.repository;

import com.vivian.reforco_connect.domain.Instituicao; // Ajuste o import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituicaoRepository extends JpaRepository<Instituicao, Long> {
}