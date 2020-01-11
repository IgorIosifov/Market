package com.geekbrains.controllers;

import com.geekbrains.entites.Order;
import com.geekbrains.entites.User;
import com.geekbrains.services.OrderService;
import com.geekbrains.services.UserService;
import com.geekbrains.utils.Cart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private Cart cart;
    private UserService userService;
    private OrderService orderService;

    public OrderController(Cart cart, UserService userService, OrderService orderService) {
        this.cart = cart;
        this.userService = userService;
        this.orderService = orderService;
    }


    @GetMapping("/info")
    public String confirmOrder(Principal principal, Model model) {
        User user = userService.findByPhone(principal.getName());
        model.addAttribute("cart", cart);
        model.addAttribute("def_phone", user.getPhone());
        return "order_info_before_confirmation";
    }

    @PostMapping("/create")
    public String createOrder(Principal principal, Model model, @RequestParam(name = "address") String address, @RequestParam(name = "phone_number") String phone) {
        User user = userService.findByPhone(principal.getName());
        Order order = new Order(user, cart, address, phone);
        order = orderService.save(order);
        model.addAttribute("order_id_str", String.format("%05d", order.getId()));
        model.addAttribute("user", principal);
        return "order_confirmation";
    }
}
