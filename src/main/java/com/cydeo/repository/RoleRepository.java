package com.cydeo.repository;

import com.cydeo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("SELECT r FROM Role r WHERE r.id between 2 and 4")
    List<Role> getAllRoleForAdmin(String role);
    @Query("SELECT r FROM Role r WHERE r.id=2")
    Role getAllRoleForRoot(String role);
}