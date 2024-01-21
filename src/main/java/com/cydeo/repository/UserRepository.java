package com.cydeo.repository;

import com.cydeo.entity.Company;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByIdAndIsDeleted(Long id,Boolean isDeleted);
    @Query("SELECT u FROM User u WHERE u.company=?1 AND u.isDeleted=?2 ORDER BY u.role.description asc ")
    List<User> findAllUserWithCompanyAndIsDeleted(Company company,Boolean isDeleted);

    @Query("SELECT u FROM User u WHERE u.role.description=?1")
    List<User>findAllAdminRole(String role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.company = ?1 AND u.role.id = 2")
    Integer isUserOnlyAdmin(Company company, Role role);
    List<User> findAllByIsDeleted(Boolean deleted);

}