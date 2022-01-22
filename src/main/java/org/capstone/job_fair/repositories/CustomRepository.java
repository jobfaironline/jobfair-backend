package org.capstone.job_fair.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

public interface CustomRepository<T, ID extends Serializable>{
    <S extends T> S customSave(S entity, boolean updateFlag);
}