package vn.com.shop;

import org.springframework.boot.SpringApplication;

public class TestShoppingApplication {

    public static void main(String[] args) {
        SpringApplication.from(ShoppingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
