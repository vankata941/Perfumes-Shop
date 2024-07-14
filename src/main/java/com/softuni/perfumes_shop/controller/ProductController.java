package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.AddProductDTO;
import com.softuni.perfumes_shop.model.enums.PerfumeType;
import com.softuni.perfumes_shop.service.ProductService;
import jakarta.persistence.NonUniqueResultException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @ModelAttribute("productData")
    private AddProductDTO productData() {
        return new AddProductDTO();
    }

    @GetMapping("/add")
    public String viewAddProduct(Model model) {
        model.addAttribute("perfumeTypes", PerfumeType.values());
        return "add-product";
    }

    @PostMapping("/add")
    public String doAddProduct(@Valid AddProductDTO productData,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productData", productData);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productData", bindingResult);

            return "redirect:/products/add";
        }
        try {

            productService.addProduct(productData);

        } catch (NonUniqueResultException | IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("productData", productData);
            redirectAttributes.addFlashAttribute("hasError", true);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/products/add";
        }

        return "index";
    }

    @GetMapping("/eau-de-toilette")
    public String viewEauDeToilette() {
        return "eau-de-toilette";
    }


}
