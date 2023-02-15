package com.estore.api.estoreapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;
import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Tag("UserControllerTest")
public class UserControllerTest {
    private UserController userController;
    private UserFileDAO mockFileDAO;

    @BeforeEach
    public void setupProductController() {
        mockFileDAO = mock(UserFileDAO.class);
        userController = new UserController(mockFileDAO);
    }

    @Test
    public void testGetUser() throws IOException {
        User testUser = new User("test", null);
        when(mockFileDAO.getUser("test")).thenReturn(testUser);
        ResponseEntity<User> response = userController.getUser("test");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody().getName());
        assertEquals(null, response.getBody().getShoppingCart());
    }

    @Test
    public void testAddToCart() throws IOException {
        User testUser = new User("test", null);
        Product testProduct = new Product(1, 1, 1, "test", null, false);
        when(mockFileDAO.getUser("test")).thenReturn(testUser);
        ResponseEntity<User> response = userController.addToCart("test", testProduct);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() throws IOException {
        User testUser = new User("test", null);
        Product testProduct = new Product(1, 1, 1, "test", null, false);
        when(mockFileDAO.getUser("test")).thenReturn(testUser);
        ResponseEntity<User> response = userController.addToCart("test", testProduct);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ResponseEntity<User> response2 = userController.removeFromCart("test", testProduct);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
    }
    // @Test 
    // public void testRemoveFromEmptyCart() throws IOException{
    //     User testUser = new User("test", null);
    //     // Product testProduct = new Product(1, 1, 1, "test");
    //     when(mockFileDAO.getUser("test")).thenReturn(testUser);
    //     // ResponseEntity<User> response = userController.removeFromCart("test", testProduct);
    //     assertEquals(HttpStatus.OK, response.getStatusCode());
    // }
}
