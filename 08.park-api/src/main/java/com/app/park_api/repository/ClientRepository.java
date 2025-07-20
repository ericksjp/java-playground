package com.app.park_api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.park_api.entity.Client;
import com.app.park_api.repository.projection.ClientProjection;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c from clients c")
    Page<ClientProjection> findAllPageable(Pageable pageable);
    Client findByUserId(Long id);
    Optional<Client> findByCpf(String cpf);
}
