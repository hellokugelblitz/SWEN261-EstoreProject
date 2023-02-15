package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
public class ProductTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_name = "TV";
        int expected_price = 100;
        int expected_quantity = 1;

        // Invoke
        Product product = new Product(expected_id, expected_price, expected_quantity, expected_name, null, false);

        // Analyze
        assertEquals(expected_id, product.getId());
        assertEquals(expected_name, product.getName());
        assertEquals(expected_price, product.getPrice());
        assertEquals(expected_quantity, product.getQuantity());
    }
}
