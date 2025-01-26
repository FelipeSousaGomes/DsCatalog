package br.devsuperior.dscatalog.repositories.projections;

import jakarta.persistence.criteria.CriteriaBuilder;

public interface ProductProjection {

    Long getId();
    String getName();


}
