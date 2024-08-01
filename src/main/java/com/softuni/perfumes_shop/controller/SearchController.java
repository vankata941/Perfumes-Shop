package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ProductService productService;

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<ViewProductDTO> searchedProducts = productService.searchProducts(keyword);

        model.addAttribute("searchedProducts", searchedProducts);
        model.addAttribute("keyword", keyword);

        return "search";
    }
}
