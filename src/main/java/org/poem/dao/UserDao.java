package org.poem.dao;


import org.poem.common.tools.BaseDao;
import org.poem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends BaseDao<UserEntity, String> {
}
