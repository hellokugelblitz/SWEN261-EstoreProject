package com.estore.api.estoreapi.model;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a product
 */
public class Product {
    static final String STRING_FORMAT = "Product [id=%d, quantity=%d, price=%d, name=%s]";

    @JsonProperty("id")
    private int id;
    @JsonProperty("price")
    private int price;
    @JsonProperty("quantity")
    private int quantity;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image")
    private File image;
    @JsonProperty("custom")
    private boolean custom;

    /**
     * Create a hero with the given id and name
     * 
     * @param id
     *            The id of the hero
     * @param name
     *            The name of the hero
     * @param price
     *            The price of the product
     * @param quantity
     *            The quantity of the product
     * 
     *            {@literal @}JsonProperty is used in serialization and
     *            deserialization
     *            of the JSON object to the Java object in mapping the fields.
     *            If a
     *            field
     *            is not provided in the JSON object, the Java field gets the
     *            default Java
     *            value, i.e. 0 for int
     */
    public Product(@JsonProperty("id") int id, @JsonProperty("price") int price,
            @JsonProperty("quantity") int quantity, @JsonProperty("name") String name,
            @JsonProperty("image") File image, @JsonProperty("custom") boolean custom) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.image = image;
        this.custom = custom;
    }

    /**
     * Retrieves the id of the product
     * 
     * @return The id of the product
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retrieves the price of the product
     * 
     * @return The price of the product
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Retrieves the quantity of the product
     * 
     * @return The quantity of the product
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Retrieves the name of the product
     * 
     * @return The name of the product
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the product
     * 
     * @return The name of the product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT, id, price, quantity, name);
    }

    @Override
    public boolean equals(Object other) {
        Product otherProduct = (Product) other;
        return otherProduct.getId() == this.id;
    }
}
