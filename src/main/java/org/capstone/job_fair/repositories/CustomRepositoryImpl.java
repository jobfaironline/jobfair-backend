package org.capstone.job_fair.repositories;

import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

public class CustomRepositoryImpl<T, ID extends Serializable> implements CustomRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public <S extends T> S customSave(S entity, boolean updateFlag) {
        if (updateFlag) {
            return entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
            return entity;
        }
    }
}
