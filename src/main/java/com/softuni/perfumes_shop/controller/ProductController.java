package com.softuni.perfumes_shop.controller;

import com.softuni.perfumes_shop.model.dto.inbound.AddProductDTO;
import com.softuni.perfumes_shop.model.dto.outbound.ViewProductDTO;
import com.softuni.perfumes_shop.model.enums.Gender;
import com.softuni.perfumes_shop.model.enums.ProductType;
import com.softuni.perfumes_shop.service.CartService;
import com.softuni.perfumes_shop.service.ProductService;
import com.softuni.perfumes_shop.service.exception.AuthorizationCheckException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import jakarta.persistence.NonUniqueResultException;
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
    private final CartService cartService;
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

        model.addAttribute("genderTypes",
                Arrays.stream(Gender.values())
                .map(Gender::getGender)
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

        return "products";
    }

    @GetMapping("/male-fragrances")
    public String viewMaleFragrances(Model model) {
        List<ViewProductDTO> maleFragrances = productService.getMaleProducts();
        model.addAttribute("products", maleFragrances);

        return "products";
    }

    @GetMapping("/female-fragrances")
    public String viewFemaleFragrances(Model model) {
        List<ViewProductDTO> femaleFragrances = productService.getFemaleProducts();
        model.addAttribute("products", femaleFragrances);

        return "products";
    }

    @GetMapping("/unisex-fragrances")
    public String viewUnisexFragrances(Model model) {
        List<ViewProductDTO> unisexFragrances = productService.getUnisexProducts();
        model.addAttribute("products", unisexFragrances);

        return "products";
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

        if (cartService.containsProductWithId(id)) {
            redirectAttributes.addFlashAttribute("isMarkedForBuying", true);

            return "redirect:/products/product/{id}";
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
