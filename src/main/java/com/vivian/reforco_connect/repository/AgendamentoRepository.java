package com.vivian.reforco_connect.repository;

import com.vivian.reforco_connect.domain.Agendamento; // Ajuste o import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}