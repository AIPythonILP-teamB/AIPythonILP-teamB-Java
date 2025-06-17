package com.handcbar.myproject.repository;

import java.util.*;
import com.handcbar.myproject.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    // 例：特定の日付の販売実績一覧
    List<Sale> findBySaleDate(java.time.LocalDate date);
}