package com.geekbrains.controllers;

import com.geekbrains.entites.Category;
import com.geekbrains.entites.Order;
import com.geekbrains.entites.Product;
import com.geekbrains.entites.User;
import com.geekbrains.services.CategoryService;
import com.geekbrains.services.OrderService;
import com.geekbrains.services.ProductService;
import com.geekbrains.services.UserService;
import com.geekbrains.utils.Cart;
import com.geekbrains.utils.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class MarketController {
    private ProductService productService;
    private CategoryService categoryService;
    private UserService userService;
    private OrderService orderService;
    private Cart cart;



    public MarketController(ProductService productService, CategoryService categoryService, UserService userService, OrderService orderService, Cart cart) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.orderService = orderService;
        this.cart = cart;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login_page";
    }

    @GetMapping("/profile")
    public String profilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/";
        }
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/")
    public String index(Model model, @RequestParam Map<String, String> params) {
        int pageIndex = 0;
        if (params.containsKey("p")) {
            pageIndex = Integer.parseInt(params.get("p")) - 1;
        }
        Pageable pageRequest = PageRequest.of(pageIndex, 10);
        ProductFilter productFilter = new ProductFilter(params);
        Page<Product> page = productService.findAll(productFilter.getSpec(), pageRequest);

        List<Category> categories = categoryService.getAll();
        model.addAttribute("filtersDef", productFilter.getFilterDefinition());
        model.addAttribute("categories", categories);
        model.addAttribute("page", page);
        return "index";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(Model model, @PathVariable Long id) {
        Product product = productService.findById(id);
        List<Category> categories = categoryService.getAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "edit_product";
    }

    @PostMapping("/edit")
    public String saveProduct(@ModelAttribute(name = "product") Product product) {
        productService.save(product);
        return "redirect:/";
    }


    @GetMapping("/registration")
    public String showForm(){
        return "registration_form";
    }

    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute(name = "phone") String phone,
                             @ModelAttribute(name = "password") String password,
                             @ModelAttribute(name = "first_name") String first_name,
                             @ModelAttribute(name = "last_name") String last_name,
                             @ModelAttribute(name = "email") String email) {
        userService.addNewUser(phone, password, first_name, last_name, email);
        return "redirect:/";
    }
}