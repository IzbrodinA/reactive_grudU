package com.griddynamics.reactive.cource.userorderservice.service;

import com.griddynamics.reactive.cource.userorderservice.entity.OrderInfo;
import com.griddynamics.reactive.cource.userorderservice.entity.Product;
import com.griddynamics.reactive.cource.userorderservice.entity.User;
import com.griddynamics.reactive.cource.userorderservice.entity.UserOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserOrderService {

    private final UserService userService;
    private final OrderService orderService;
    private final ProductService productService;

    public Flux<UserOrder> getUserInfo(String id) {
        return userService.getUser(id)
                .flatMapMany(user -> orderService.getOrderInfo(user.getPhone())
                        .flatMap(orderInfo -> productService.getOrderInfo(orderInfo.getProductCode())
                                .map(this::relevantProduct)
                                .map(product -> createUserOrder(user, orderInfo, product))));

    }

    private static UserOrder createUserOrder(User user, OrderInfo orderInfo, Optional<Product> product) {
        return UserOrder.builder()
                .userName(user.getName())
                .orderNumber(orderInfo.getOrderNumber())
                .phoneNumber(user.getPhone())
                .productCode(orderInfo.getProductCode())
                .productName(product.map(Product::getProductName).orElse(""))
                .productId(product.map(Product::getProductId).orElse(""))
                .build();
    }

    private Optional<Product> relevantProduct(List<Product> products) {
        return products.stream().max(Comparator.comparingDouble(Product::getScore));
    }

}
