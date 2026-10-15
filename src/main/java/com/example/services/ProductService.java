package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.Product;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);
    List<Product> findAll(Sort sort);
    Product findById(int id);
    Product save(Product product);
    void delete(Product product);
    List<Product> findAll();
    
    
    // Metodo que recupera todos los productos de una presentacion concreta,
    // cuyo nombre se recibe como parametro METODO 4
     
    List<Product> findByPresentationName(String presentation);
}
