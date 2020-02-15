package com.geekbrains.services;

import com.geekbrains.entites.*;
import com.geekbrains.repositories.FeedbackRepository;
import com.geekbrains.repositories.OrderItemRepository;
import com.geekbrains.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private FeedbackRepository feedbackRepository;
    private OrderItemRepository orderItemRepository;
    private UserService userService;
    private OrderService orderService;

    @Autowired
    public void setOrderItemRepository(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setFeedbackRepository(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public Page<Product> findAll(Specification<Product> spec, Pageable pageable) {
        return productRepository.findAll(spec, pageable);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).get();
    }


    public Product save(Product Product) {
        return productRepository.save(Product);
    }

    public Double averageRating(Long id) {
        double avgRating;
        if (findById(id) != null) {
            Product product = findById(id);
            List<Feedback> feedbacks = product.getFeedbacks();
            double sum = 0.0;
            for (Feedback feedback : feedbacks) {
                sum += feedback.getRating();
            }
            avgRating = sum / feedbacks.size();
        } else {
            avgRating = 0.0;
        }
        return avgRating;
    }

    public void addNewFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
        Product product = findById(feedback.getProduct().getId());
        List<Feedback> feedbacks = product.getFeedbacks();
        feedbacks.add(feedback);
        product.setFeedbacks(feedbacks);
    }

    public boolean isUserBuyProduct(Principal principal, Product product) {
        if (principal!= null) {
            User user = userService.findByPhone(principal.getName());
            return orderItemRepository.findOrderItemByProductIdAAndUser(product.getId(), user) != null;
        }
        return false;
    }

    public List<Product> findall(){
        return productRepository.findAll();
    }
}

