package com.dev.mercadinhoastro.web.repository;

import com.dev.mercadinhoastro.web.model.ClienteModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ClienteModel, Long> {
}
