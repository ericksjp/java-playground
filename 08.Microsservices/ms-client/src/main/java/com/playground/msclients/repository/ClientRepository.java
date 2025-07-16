package com.playground.msclients.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.playground.msclients.domain.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByCpf(String cpf);
}
