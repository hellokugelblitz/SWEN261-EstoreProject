package com.estore.api.estoreapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-Tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_cart_size = 2;
        String expected_user_name = "user";
        String expected_first_item_name = "banana";
        String expected_second_item_name = "apple";

        Product product1 = new Product(1, 0, 1, "banana", null, false);
        Product product2 = new Product(1, 0, 1, "apple", null, false);
        ArrayList<Product> user_cart = new ArrayList<Product>();
        user_cart.add(product1);
        user_cart.add(product2);

        // Invoke
        User user = new User("user", user_cart);

        // Analyze
        assertEquals(expected_cart_size, user.getShoppingCart().size());
        assertEquals(expected_user_name, user.getName());
        assertEquals(expected_first_item_name, user.getShoppingCart().get(0).getName());
        assertEquals(expected_second_item_name, user.getShoppingCart().get(1).getName());
    }

    @Test
    public void testRemove() {

        // Setup
        int expected_cart_size = 2;
        int expected_cart_size_after_removal = 1;

        Product product1 = new Product(1, 0, 1, "banana", null, false);
        Product product2 = new Product(1, 0, 1, "apple", null, false);
        ArrayList<Product> user_cart = new ArrayList<Product>();
        user_cart.add(product1);
        user_cart.add(product2);

        User user = new User("user", user_cart);

        // Analyze
        assertEquals(expected_cart_size, user.getShoppingCart().size());

        user.removeFromCart(product2);

        assertEquals(expected_cart_size_after_removal, user.getShoppingCart().size());
    }

    @Test
    public void testEquals() {
        Product product1 = new Product(1, 0, 1, "banana", null, false);
        Product product2 = new Product(1, 1000, 2, "apple", null, false);

        assertTrue(product1.equals(product2));
    }

    @Test
    public void testNotEquals() {
        Product product1 = new Product(1, 0, 1, "banana", null, false);
        Product product2 = new Product(2, 1000, 2, "apple", null, false);

        assertFalse(product1.equals(product2));
    }
}
