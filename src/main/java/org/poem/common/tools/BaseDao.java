package org.poem.common.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseDao<T, D> extends JpaRepository<T, D>, PagingAndSortingRepository<T, D>,JpaSpecificationExecutor<T> {
}
