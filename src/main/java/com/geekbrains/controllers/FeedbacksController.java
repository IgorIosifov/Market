package com.geekbrains.controllers;

import com.geekbrains.entites.Feedback;
import com.geekbrains.entites.Product;
import com.geekbrains.entites.User;
import com.geekbrains.repositories.FeedbackRepository;
import com.geekbrains.services.ProductService;
import com.geekbrains.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/feedbacks")
public class FeedbacksController {

    private ProductService productService;

    private UserService userService;

    private FeedbackRepository feedbackRepository;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
    @Autowired
    public void setFeedbackRepository(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{id}")
    public String viewProductFeedbacks(Model model, @PathVariable Long id, Principal principal) {
        Product product = productService.findById(id);
        boolean allowed=false;
        if (principal!=null){
            User user = userService.findByPhone(principal.getName());
            if (feedbackRepository.findByProductIdAndUserId(product.getId(),user.getId())==null){
                allowed = true;
            }
        }
        List<Feedback> feedbacks = product.getFeedbacks();
        model.addAttribute("allowed", allowed);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("avgRating", productService.averageRating(id));
        model.addAttribute("product", product);
        return "product_info";
    }

    @GetMapping("/write/{id}")
    public String writeProductFeedbackForm(Model model, Principal principal, @PathVariable Long id) {
        if (principal==null){
            return "registration_form";
        }
        if (productService.isUserBuyProduct(principal, productService.findById(id))){
            User user = userService.findByPhone(principal.getName());
            Product product = productService.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("user", user);
            return "write_feedback";
        }
        return "didnt_buy_page";
    }

    @PostMapping
    public String saveFeedback(@ModelAttribute(name = "feedback") Feedback feedback) {
        productService.addNewFeedback(feedback);
        return "redirect:/";
    }
}
