package com.app.park_api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.park_api.entity.ClientSpot;
import com.app.park_api.repository.projection.ClientSpotProjection;

public interface ClientSpotRepository extends JpaRepository<ClientSpot, Long> {
    @Query("""
       SELECT c, cl.cpf AS clientCpf, s.code AS spotCode
       FROM client_spot c JOIN c.client cl JOIN c.spot s
       """)
    Page<ClientSpotProjection> findAllPageable(Pageable pageable);
    Optional<ClientSpot> findByReceiptAndCheckOutIsNull(String receipt);
    Page<ClientSpotProjection> findAllByClientCpf(String cpf, Pageable pageable);
    Page<ClientSpotProjection> findAllByClientId(Long id, Pageable pageable);
}
