package com.lwohvye.springcloud.springcloudlwohvyeprovider.repository;

import com.lwohvye.springcloud.springcloudlwohvyeapi.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RoleDao extends CrudRepository<Role, Long> {

    //       使用sql语句查询时，需要设置nativeQuery=true
    @Query(nativeQuery = true,
            value = "SELECT id,role_name,description,available FROM role " +
//             IF(条件，真执行，假执行)
                    "where IF(:roleName is not null,role_name LIKE CONCAT('%',:roleName,'%'),1=1 ) ",
            countQuery = "SELECT count(1) FROM role " +
                    "where IF(:roleName is not null,role_name LIKE CONCAT('%',:roleName,'%'),1=1 ) ")
//     在执行sql的同时，亦可通过Pageable织入分页和排序
    Page<Role> findRole(@Param("roleName") String roleName, Pageable pageable);
}
