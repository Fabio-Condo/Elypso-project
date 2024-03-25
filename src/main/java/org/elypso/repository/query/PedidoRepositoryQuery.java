package org.elypso.repository.query;

import org.elypso.domain.Pedido;
import org.elypso.repository.filter.PedidoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PedidoRepositoryQuery {

    public Page<Pedido> filter(PedidoFilter pedidoFilter, Pageable pageable);

    public List<Pedido> filterForExcel(PedidoFilter pedidoFilter, Pageable pageable);


}
