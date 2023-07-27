package com.example.affablebeanui.service;

import com.example.affablebeanui.entity.Product;
import com.example.affablebeanui.entity.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class ProductService {
    public static final int DELIVERY_CHARGE = 3;
    //    @Value("${backend.url}")
//    private String baseUrl;

    private final CardService cardService;

    record TransferData(String to_email,String from_email,double amount){

    }



    private List<Product>products;
    private RestTemplate restTemplate=new RestTemplate();

    public List<Product>findProductByCategory(int categoryId){
        return products.stream()
                .filter(p -> p.getCategory().getId() == categoryId)
                .collect(Collectors.toList());
    }
    public ProductService(final CardService cardService){
        this.cardService=cardService;
        var productResponseEntity = restTemplate.getForEntity(  "http://localhost:8090/backend/products",Products.class);
        if (productResponseEntity.getStatusCode().is2xxSuccessful()){
            products=productResponseEntity.getBody().getProducts();
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    public List<Product>showAllProduct(){
        return products;
    }
    public Product purchaseProduct(int id){
        Product product=findProduct(id);

        cardService.addToCart(product);
        return product;
    }

    private Product findProduct(int id){
        return products.stream().filter(p -> p.getId()==id)
                .findAny()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity transfer(String to_email,
                                   String from_email,
                                   double amount){
        var data = new TransferData(to_email,from_email,amount+ DELIVERY_CHARGE);
      return restTemplate.postForEntity("http://localhost:8095/account/transfer",data,String.class);

    }

    public ResponseEntity saveCartItem(){
        return restTemplate.getForEntity("http://localhost:9000/transport/cart/save",String.class);
    }
}
