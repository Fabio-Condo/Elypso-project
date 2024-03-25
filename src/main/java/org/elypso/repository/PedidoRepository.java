package org.elypso.repository;

import org.elypso.domain.Pedido;
import org.elypso.repository.query.PedidoRepositoryQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, PedidoRepositoryQuery {
    @Query("SELECT p FROM Pedido p WHERE p.nome LIKE %:name% OR p.numeroCliente LIKE %:name% OR p.numeroApolice LIKE %:name%")
    public Page<Pedido> findByAnyProperty(@Param("name") String name, Pageable pageable);

}
