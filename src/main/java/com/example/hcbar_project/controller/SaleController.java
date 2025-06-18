package com.example.hcbar_project.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hcbar_project.model.Sale;
import com.example.hcbar_project.service.SaleService;
import com.example.hcbar_project.service.ProductService;

@Controller
@SessionAttributes({"sale", "sales", "saleDate"})
public class SaleController {

    @Autowired private SaleService saleService;
    @Autowired private ProductService productService;

    /** ① 入力画面表示 */
    @GetMapping("/admin_sale_input")
    public String showInput(Model model,
                            @RequestParam(required = false) Boolean completed) {
        // フォーム用の空オブジェクト
        model.addAttribute("sale", new Sale());
        // 入力中の行リストを初期化
        if (!model.containsAttribute("sales")) {
            model.addAttribute("sales", new ArrayList<Sale>());
        }
        // 日付もセッション保持
        model.addAttribute("saleDate", LocalDate.now());
        // 商品マスタ一覧
        model.addAttribute("products", productService.listAll());
        model.addAttribute("completed", completed == Boolean.TRUE);
        return "admin_sale_input";
    }

    /** ② 「追加」→ 入力行をセッションリストに積む */
    @PostMapping("/add")
    public String addRow(@ModelAttribute("sale") Sale sale,
                         @ModelAttribute("sales") List<Sale> sales,
                         @ModelAttribute("saleDate") LocalDate date,
                         Model model) {
        // セッションの日付をフォームのSaleにも設定
        sale.setSaleDate(date);
        // 新しいSaleインスタンスをコピーしてリストへ
        Sale copy = new Sale();
        copy.setProduct(sale.getProduct());
        copy.setQuantity(sale.getQuantity());
        copy.setSaleDate(date);
        sales.add(copy);

        // フォームをクリアして再表示
        model.addAttribute("sale", new Sale());
        model.addAttribute("products", productService.listAll());
        return "sale_input";
    }

    /** ③ 確認画面表示 */
    @GetMapping("/confirm")
    public String showConfirm(@ModelAttribute("sales") List<Sale> sales,
                              @ModelAttribute("saleDate") LocalDate date,
                              Model model) {
        model.addAttribute("sales", sales);
        model.addAttribute("saleDate", date);
        return "sale_confirm";
    }

    /** ④ 登録実行（新規 or 更新） */
    @PostMapping("/save")
    public String save(@ModelAttribute("sales") List<Sale> sales,
                       @ModelAttribute("saleDate") LocalDate date,
                       Model model) {
        // 既存を日付で取得
        List<Sale> existing = saleService.findByDate(date);
        if (!existing.isEmpty()) {
            saleService.updateAll(date, sales);  // TODO: implement in service
        } else {
            saleService.registerAll(sales);       // TODO: implement in service
        }
        // セッションをクリア
        model.addAttribute("sales", new ArrayList<Sale>());
        // 完了フラグを付けて入力画面へリダイレクト
        return "redirect:/sales/input?completed=true";
    }

    /** ⑤ 編集画面表示（日付指定で既存レコードをセッションにセット） */
    @GetMapping("/edit/{date}")
    public String showEdit(
        @PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate date,
        Model model) {

        List<Sale> existing = saleService.findByDate(date);
        model.addAttribute("sales", new ArrayList<>(existing));
        model.addAttribute("saleDate", date);
        model.addAttribute("sale", new Sale());
        model.addAttribute("products", productService.listAll());
        return "sale_input";
    }
}

