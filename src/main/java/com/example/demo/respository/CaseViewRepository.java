package com.example.demo.respository;

import com.example.demo.entity.CaseView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseViewRepository extends JpaRepository<CaseView,Long> {
}
