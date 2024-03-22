package org.elypso.repository;

import org.elypso.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElypsoRepository extends JpaRepository<Pedido, Long> {
}
