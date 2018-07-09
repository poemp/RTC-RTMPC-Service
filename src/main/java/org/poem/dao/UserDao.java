package org.poem.dao;


import org.poem.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserDao extends JpaRepository<UserEntity,String> , PagingAndSortingRepository<UserEntity,String> {
}
