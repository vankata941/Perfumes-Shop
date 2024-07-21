package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.model.enums.ProductType;
import com.softuni.perfumes_shop.service.ProductService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CurrentUserDetails currentUserDetails;

    @ModelAttribute("productData")
    private AddProductDTO productData() {
        return new AddProductDTO();
    }

    @GetMapping("/add")
    public String viewAddProduct(Model model) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

        model.addAttribute("productTypes",
                Arrays.stream(ProductType.values())
                        .map(ProductType::getName)
                        .toArray());

        return "add-product";
    }

    @PostMapping("/add")
    public String doAddProduct(@Valid AddProductDTO productData,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes
    ) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }

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

    @GetMapping("/all")
    public String viewAllProducts(Model model) {
        List<ViewProductDTO> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);
        return "all-products";
    }

    @GetMapping("/eau-de-toilette")
    public String viewEauDeToilette() {
        return "eau-de-toilette";
    }

    @GetMapping("/product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        ViewProductDTO productDTO = productService.getViewProductById(id);
        model.addAttribute("product", productDTO);
        return "view-product";
    }

    @DeleteMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!currentUserDetails.hasRole("ADMIN")) {
            throw new AuthorizationCheckException();
        }
        String name = productService.findNameOfProduct(id);
        productService.deleteProductById(id);

        if (name != null) {
            redirectAttributes.addFlashAttribute("successfullyDeleted", true);
            redirectAttributes.addFlashAttribute("name", name);
        }

        return "redirect:/products/all";
    }
}
