package com.example.hcbar_project.repository;

import java.time.LocalDate;
import java.util.*;
import com.example.hcbar_project.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    // 特定の日付の販売実績一覧
    List<Sale> findBySaleDate(java.time.LocalDate date);

    List<Sale> findBySaleDateBetween(LocalDate start, LocalDate end);
}