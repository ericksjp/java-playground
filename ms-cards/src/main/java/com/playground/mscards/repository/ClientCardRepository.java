package com.playground.mscards.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.playground.mscards.entity.ClientCard;

public interface ClientCardRepository extends JpaRepository<ClientCard, Long> {
    List<ClientCard> findByCpf(String cpf);
}

