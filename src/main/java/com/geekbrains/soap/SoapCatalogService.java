package com.geekbrains.soap;

import com.geekbrains.entites.Product;
import com.geekbrains.services.ProductService;
import com.geekbrains.soap.catalog.ProductDTO;
import com.geekbrains.soap.catalog.ProductsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class SoapCatalogService {
    private List<ProductDTO> productDTOs;
    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @PostConstruct
    public void init() {
        productDTOs = new ArrayList<>();

        List<Product> products = productService.findall();
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setPrice(product.getPrice());
            productDTO.setTitle(product.getTitle());
            productDTOs.add(productDTO);
        }
    }

    public ProductsList getAllProductsList() {
        ProductsList productsList = new ProductsList();
        productsList.getProduct().addAll(productDTOs);
        return productsList;
    }
}
