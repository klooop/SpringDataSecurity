package com.example.springdatasecurity.services;

import com.example.springdatasecurity.entities.Product;
import com.example.springdatasecurity.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProductsService {
    private ProductRepository productRepository;
    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Transactional
    public void doSomething() {
        // \то значит что в одном методе все должно быть сделан вместе с транзакцикей
        // если ранзакция завершается то и метод тоже
    }

    public Product findById(Long id) {
//        if  (productRepository.existsById(id)){
//            return productRepository.findById(id).get();
//        }
        productRepository.findById(id).orElse(null);
        return null;
    }

    public Product findAll() {
       return (Product) productRepository.findAll();
    }

    public Product findByTitle(String title){
        return productRepository.findByTitle(title);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Page<Product> getProductsWithPagingAndFiltering(Specification<Product> specification, Pageable pageable) {
        return productRepository.findAll(specification, pageable);
    }


    public void saveOrUpdate(Product product) {
        // repo will save or update
        productRepository.save(product);
    }
}
