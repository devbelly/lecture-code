package com.devbelly.controller;

import com.devbelly.entity.Notification;
import com.devbelly.entity.Product;
import com.devbelly.repository.NotificationRepository;
import com.devbelly.repository.ProductRepository;
import com.devbelly.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ProductService productService;

    @Test
    void hi() {
        Product product = Product.of("맥북", 1000L);
        productRepository.save(product);

        Product findProduct = productRepository.findById(product.getId()).get();
        assertThat(findProduct).isEqualTo(product);

        productService.update(product.getId());

        List<Notification> findNotiList = notificationRepository.findAll();
        assertThat(findNotiList.size()).isEqualTo(1);

    }
}