package com.devbelly.service;

import com.devbelly.entity.Product;
import com.devbelly.event.PriceDownEvent;
import com.devbelly.repository.ProductRepository;
import com.google.common.eventbus.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final EventBus eventBus;

    public void update(Long id){
        Product product = productRepository.findById(id).get();
        Long currentPrice = product.getPrice();
        product.setPrice(currentPrice-10);
        productRepository.save(product);

        PriceDownEvent event = new PriceDownEvent();
        event.setId(product.getId());
        event.setTitie(product.getTitle());

        eventBus.post(event);
    }
}
