package com.controle.monitoria_api.repository;

import com.controle.monitoria_api.model.IES;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IESRepository extends JpaRepository<IES, Long> {

    boolean existsByNome(String nome);
}
