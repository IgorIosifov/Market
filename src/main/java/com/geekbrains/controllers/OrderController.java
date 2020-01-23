package com.geekbrains.controllers;

import com.geekbrains.entites.Order;
import com.geekbrains.entites.OrderItem;
import com.geekbrains.entites.User;
import com.geekbrains.repositories.OrderItemRepository;
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
    private OrderItemRepository orderItemRepository;

    public OrderController(Cart cart, UserService userService, OrderService orderService, OrderItemRepository orderItemRepository) {
        this.cart = cart;
        this.userService = userService;
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
    }


    @GetMapping("/info")
    public String confirmOrder(Principal principal, Model model) {
        if (principal != null) {
            User user = userService.findByPhone(principal.getName());
            model.addAttribute("def_phone", user.getPhone());
        } else {
            model.addAttribute("def_phone", "");
        }
        model.addAttribute("cart", cart);
        return "order_info_before_confirmation";
    }

    @PostMapping("/create")
    public String createOrder(Principal principal, Model model, @RequestParam(name = "address") String address, @RequestParam(name = "phone_number") String phone) {
        User user = new User();
        if (principal != null) {
            user = userService.findByPhone(principal.getName());
        } else {
            user.setPhone(phone);
            userService.addNewUser(user);
        }
        Order order = new Order(user, cart, address, phone);
        order = orderService.save(order);

        for (OrderItem orderItem : order.getItems()) {
            orderItemRepository.save(orderItem);
        }

        model.addAttribute("order_id_str", order.getId());
        return "order_confirmation";
    }

    @GetMapping("/history")
    public String showOrdersHistory(Principal principal, Model model) {
        if (principal != null) {
            model.addAttribute("orders_history_list", orderService.findOrdersByPhone(principal.getName()));
            return "orders_history";
        } else {
            return "registration_form";
        }
    }

}
