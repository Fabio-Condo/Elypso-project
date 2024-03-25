package org.elypso.repository.impl;

import org.elypso.domain.Pedido;
import org.elypso.repository.filter.PedidoFilter;
import org.elypso.repository.query.PedidoRepositoryQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PedidoRepositoryImpl implements PedidoRepositoryQuery {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager manager;

    @Override
    public Page<Pedido> filter(PedidoFilter pedidoFilter, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Pedido> criteria = builder.createQuery(Pedido.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        getSortOrder(pedidoFilter, builder, criteria, root);

        Predicate[] predicates = createRestrictions(pedidoFilter, builder, root);
        criteria.where(predicates);

        TypedQuery<Pedido> query = manager.createQuery(criteria);
        addRestrictionsPagination(query, pageable);

        return new PageImpl<>(query.getResultList(), pageable, total(pedidoFilter));
    }

    private void addRestrictionsPagination(TypedQuery<?> query, Pageable pageable) {
        int currentPage = pageable.getPageNumber();
        int totalRecordsByPage = pageable.getPageSize();
        int firstPageRecord = currentPage * totalRecordsByPage;

        query.setFirstResult(firstPageRecord);
        query.setMaxResults(totalRecordsByPage);

        LOGGER.info("Current page: " + currentPage + " Total records by page: " + totalRecordsByPage + " First record page " + firstPageRecord);
    }

    private Long total(PedidoFilter pedidoFilter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Pedido> root = criteria.from(Pedido.class);

        Predicate[] predicates = createRestrictions(pedidoFilter, builder, root);
        criteria.where(predicates);

        criteria.select(builder.count(root));
        return manager.createQuery(criteria).getSingleResult();
    }

    private Predicate[] createRestrictions(PedidoFilter pedidoFilter, CriteriaBuilder builder,Root<Pedido> root) {
        List<Predicate> predicates = new ArrayList<>();

        restrictions( pedidoFilter, predicates, builder, root);

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    public void restrictions(PedidoFilter pedidoFilter, List<Predicate> predicates, CriteriaBuilder builder, Root<Pedido> root){

        if(!ObjectUtils.isEmpty(pedidoFilter.getGlobal())) {
            Predicate nome = builder.like(
                    builder.lower(root.get("nome")), "%" + pedidoFilter.getGlobal().toLowerCase() + "%");
            Predicate numeroCliente = builder.like(
                    builder.lower(root.get("numeroCliente")), "%" + pedidoFilter.getGlobal().toLowerCase() + "%");
            Predicate numeroApolice = builder.like(
                    builder.lower(root.get("numeroApolice")), "%" + pedidoFilter.getGlobal().toLowerCase() + "%");
            predicates.add(builder.or(nome, numeroCliente, numeroApolice));
        }

        if(!ObjectUtils.isEmpty(pedidoFilter.getNome())) {
            predicates.add(builder.like(
                    builder.lower(root.get("nome")), "%" + pedidoFilter.getNome().toLowerCase() + "%"));
        }
        if(!ObjectUtils.isEmpty(pedidoFilter.getNumeroApolice())) {
            predicates.add(builder.like(
                    builder.lower(root.get("numeroApolice")), "%" + pedidoFilter.getNumeroApolice().toLowerCase() + "%"));
        }
        if(!ObjectUtils.isEmpty(pedidoFilter.getNumeroCliente())) {
            predicates.add(builder.like(
                    builder.lower(root.get("numeroCliente")), "%" + pedidoFilter.getNumeroCliente().toLowerCase() + "%"));
        }
        if (pedidoFilter.getLado() != null) {
            predicates.add(
                    builder.equal(root.get("lado"), pedidoFilter.getLado()));
        }
        if (pedidoFilter.getBeginDate() != null) {
            predicates.add(
                    builder.greaterThanOrEqualTo(root.get("date"), pedidoFilter.getBeginDate()));
        }
        if (pedidoFilter.getEndDate() != null) {
            predicates.add(
                    builder.lessThanOrEqualTo(root.get("date"), pedidoFilter.getEndDate()));
        }
    }

    public void getSortOrder(PedidoFilter pedidoFilter, CriteriaBuilder builder, CriteriaQuery<Pedido> criteria, Root<Pedido> root){
        if(Objects.equals(pedidoFilter.getPedidoOrderBy(), "id,asc")){
            criteria.orderBy(builder.asc(root.get("id")));
        }
        if(Objects.equals(pedidoFilter.getPedidoOrderBy(), "id,desc")){
            criteria.orderBy(builder.desc(root.get("id")));
        }
        if(Objects.equals(pedidoFilter.getPedidoOrderBy(), "nome,asc")){
            criteria.orderBy(builder.asc(root.get("nome")));
        }
        if(Objects.equals(pedidoFilter.getPedidoOrderBy(), "nome,desc")){
            criteria.orderBy(builder.desc(root.get("nome")));
        }
        if(Objects.equals(pedidoFilter.getPedidoOrderBy(), "date,asc")){
            criteria.orderBy(builder.asc(root.get("date")));
        }
        if(Objects.equals(pedidoFilter.getPedidoOrderBy(), "date,desc")){
            criteria.orderBy(builder.desc(root.get("date")));
        }
    }
}
