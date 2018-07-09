package org.poem.common.tools;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BaseDao<T, D> extends JpaRepository<T, D>, PagingAndSortingRepository<T, D> {
}
