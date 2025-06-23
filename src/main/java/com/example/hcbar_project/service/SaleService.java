package com.example.hcbar_project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.hcbar_project.model.Sale;
import com.example.hcbar_project.repository.*;
import com.example.hcbar_project.model.User;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class SaleService {

    @Autowired
    private SaleRepository repo;
    @Autowired
    private UserRepository userRepository;

    /* 特定日付の既存レコード取得 */
    public List<Sale> findByDate(LocalDate date) {
        return null;
    }

    /* 新規登録：List<Sale> を一括保存 */
    public void registerAll(List<Sale> sales) {
        // ログイン中のユーザーの email を取得
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // email から User を取得（存在しなければ例外）
        User user = userRepository.findByEmail(email).orElseThrow();

        for (Sale sale : sales) {
            sale.setCreatedDate(LocalDateTime.now());
            sale.setUpdatedDate(LocalDateTime.now());
            sale.setUser(user); // 管理者固定 idが1の人だけ
        }
        repo.saveAll(sales);
    }

    /** 更新：古いレコードを削除して新しいリストを保存 */
    // updateAll()の使用場所確認
    public void updateAll(LocalDate date, List<Sale> sales) {
        List<Sale> old = repo.findBySaleDate(date);
        repo.deleteAll(old);
        repo.saveAll(sales);
    }
}