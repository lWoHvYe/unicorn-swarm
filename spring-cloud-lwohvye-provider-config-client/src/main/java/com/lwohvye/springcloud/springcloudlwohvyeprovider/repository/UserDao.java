package com.lwohvye.springcloud.springcloudlwohvyeprovider.repository;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);


    //     使用sql语句查询时，需要设置nativeQuery=true
    //     IF(条件，真执行，假执行)
    @Query(nativeQuery = true,
            value = "SELECT uid,name,password,salt,state,username,role_id,phone_number FROM user " +
                    "where IF(:username is not null,username LIKE CONCAT('%',:username,'%'),1 = 1 ) ",
            countQuery = "select count(1) from user " +
                         " where IF(:username is not null,username LIKE CONCAT('%',:username,'%'),1 = 1 ) ")
//     在执行sql的同时，亦可通过Pageable织入分页和排序
    Page<User> findUser(@Param("username") String username, Pageable pageable);
}
