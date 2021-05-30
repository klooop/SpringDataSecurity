package com.example.springdatasecurity.controllers;

import com.example.springdatasecurity.entities.Product;
import com.example.springdatasecurity.repositories.specifications.ProductsSpecs;
import com.example.springdatasecurity.services.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@Controller
@RequestMapping("/products")
public class ProductsController {

    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public String showItems(Model model,
                            @RequestParam(value="page", required = false) Integer page,
                            @RequestParam(value="word",required = false)String word,
                            @RequestParam(value="minPrice",required = false)BigDecimal minPrice,
                            @RequestParam(value="maxPrice",required = false)BigDecimal maxPrice ) {
        if (page==null) page=1;
        Specification<Product> filter =Specification.where(null);
        if (word!=null){
            filter= filter.and(ProductsSpecs.titleContains(word));
        }
        if (minPrice!=null){
            filter= filter.and(ProductsSpecs.priceGreaterThanOrEq(minPrice));
        }
        if (maxPrice!=null){
            filter= filter.and(ProductsSpecs.priceLesserThanOrEq(maxPrice));
        }


        List<Product> resultList =productsService.
                getProductsWithPagingAndFiltering(filter, PageRequest.of(page-1,5))
                .getContent();
        model.addAttribute("products", resultList);

        model.addAttribute("word", word);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        return "products";
    }
    @GetMapping("/show/{id}")
    public String show(Model model, @PathVariable (value="id") Long id) {
        Product product = productsService.findById(id);
        model.addAttribute("product", product);
        return "product-page";
    }
    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable(value="id") Long id){
        productsService.deleteById(id);
        return "redirect:/products";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        Product product= new Product();
        model.addAttribute("product", product);
        return "product-edit";
    }

    @PostMapping("/edit")
    public String addProduct(@ModelAttribute(value= "product") Product product) {
        productsService.saveOrUpdate(product);
        return "redirect:/products";
    }
}
