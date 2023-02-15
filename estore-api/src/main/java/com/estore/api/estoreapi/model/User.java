package com.estore.api.estoreapi.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user that interacts with the E-Store
 */
public class User {
    @JsonProperty("username")
    private String username;
    @JsonProperty("shoppingCart")
    private ArrayList<Product> shoppingCart;

    /**
     * Creates a user with the given username and shopping cart
     * 
     * @param username
     *            Name of the user
     * @param shoppingCart
     *            Array of products the user currently has
     */
    public User(@JsonProperty("username") String username,
            @JsonProperty("shoppingCart") ArrayList<Product> shoppingCart) {
        this.username = username;
        this.shoppingCart = shoppingCart;
    }

    /**
     * Returns the name of the user
     * 
     * @return Name String
     */
    public String getName() {
        return this.username;
    }

    /**
     * Returns the user's shopping cart
     * 
     * @return ArrayList of Products representing the shopping cart
     */
    public ArrayList<Product> getShoppingCart() {
        return this.shoppingCart;
    }

    /**
     * Adds a product to the cart, initializes the array list if needed
     * 
     * @param product
     *            Adds the given {@linkplain Product}
     */
    public void addToCart(Product product) {
        if (this.shoppingCart == null) {
            this.shoppingCart = new ArrayList<>();
        }
        this.shoppingCart.add(product);
    }

    /**
     * Removes a product from the cart, initializes the array list if needed
     * 
     * @param product
     *            Removes the given {@linkplain Product}
     */
    public void removeFromCart(Product product) {
        if (this.shoppingCart == null) {
            this.shoppingCart = new ArrayList<>();
        }
        if (this.shoppingCart.contains(product)) {
            this.shoppingCart.remove(product);
        }
    }
}
