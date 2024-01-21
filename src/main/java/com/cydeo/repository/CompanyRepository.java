package com.cydeo.repository;

import com.cydeo.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c WHERE c.id=?1")
    Company getCompanyForCurrent(Long id);
    @Query("SELECT c FROM Company c where c.id!=?1")
    List<Company>getAllCompanyForRoot(Long id);

//    Companies should be sorted by their status, "Active" companies should be on top then by title of company
    @Query("SELECT c FROM Company c WHERE c.id != 1 " +
            "ORDER BY CASE WHEN c.companyStatus = 'ACTIVE' THEN 0 ELSE 1 END, c.title")
    List<Company> findAllCompanyIdNot1();

    boolean existsByTitle(String title);

    boolean existsByTitleAndIdNot(String title, Long id);

    Company findByTitle(String companyTitle);
}