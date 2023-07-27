package com.example.affablebeanui.service;

import com.example.affablebeanui.entity.Product;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CardService {
    private Set<Product> cart=new HashSet<>();

    public int cartSize(){
        return cart.size();
    }

    public void addToCart(Product product){
        cart.add(product);
    }

    public Set<Product>getAllProducts(){
        return cart;
    }
    public void clearCart(){
        cart.clear();
    }
}
