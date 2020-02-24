package com.geekbrains.controllers;

import com.geekbrains.entites.Order;
import com.geekbrains.entites.OrderItem;
import com.geekbrains.entites.User;
import com.geekbrains.repositories.OrderItemRepository;
import com.geekbrains.services.OrderService;
import com.geekbrains.services.UserService;
import com.geekbrains.utils.Cart;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private Cart cart;
    private UserService userService;
    private OrderService orderService;
    private OrderItemRepository orderItemRepository;
    private RabbitController rabbitController;
    private Order order;

    public OrderController(Cart cart, UserService userService, OrderService orderService, OrderItemRepository orderItemRepository, RabbitController rabbitController) {
        this.cart = cart;
        this.userService = userService;
        this.orderService = orderService;
        this.orderItemRepository = orderItemRepository;
        this.rabbitController = rabbitController;
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
        order = new Order(user, cart, address, phone);
        order = orderService.save(order);

        for (OrderItem orderItem : order.getItems()) {
            orderItemRepository.save(orderItem);
        }

        model.addAttribute("order_id_str", order.getId());
        rabbitController.sendMessage("Confirm." + order.getId());
        model.addAttribute("pdf_name", "/orders/"+createOrderPDF());
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

    public String createOrderPDF() {
        Document document = new Document();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String name = format.format(date) + "_order" + order.getId() + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream("src\\main\\resources\\orders\\" + name));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk orderFromDate = new Chunk("Your order number " + order.getId() + " from " + format.format(date), font);
        Chunk supplier = new Chunk("Supplier: SuperShop", font);
        Chunk customer = new Chunk("Customer: " + order.getUser().getFirstName() + " " + order.getUser().getLastName(), font);
        Chunk total = new Chunk("Total: " + order.getPrice(), font);
        Chunk deliveryAddress = new Chunk("Delivery address: " + order.getAddress(), font);
        Chunk contactPhone = new Chunk("Contact phone: " + order.getPhone(), font);

        List<OrderItem> orderItems = order.getItems();
        PdfPTable table = new PdfPTable(4);
        Stream.of("Number", "Title", "Quantity", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem currentOrderItem = orderItems.get(i);
            table.addCell(String.valueOf(i + 1));
            table.addCell(currentOrderItem.getProduct().getTitle());
            table.addCell(String.valueOf(currentOrderItem.getQuantity()));
            table.addCell(String.valueOf(currentOrderItem.getPrice()));
        }

        try {
            document.add(orderFromDate);
            document.add(new Paragraph("\n"));
            document.add(supplier);
            document.add(new Paragraph("\n"));
            document.add(customer);
            document.add(new Paragraph("\n"));
            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(total);
            document.add(new Paragraph("\n"));
            document.add(deliveryAddress);
            document.add(new Paragraph("\n"));
            document.add(contactPhone);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
        return name;
    }

}
