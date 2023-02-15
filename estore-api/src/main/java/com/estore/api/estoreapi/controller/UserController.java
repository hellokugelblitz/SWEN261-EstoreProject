package com.estore.api.estoreapi.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.UserFileDAO;

@RestController
@RequestMapping("users")
public class UserController {
    private UserFileDAO userFileDAO;

    /**
     * Initializes the UserController
     * 
     * @param userFileDAO
     */
    public UserController(UserFileDAO userFileDAO) {
        this.userFileDAO = userFileDAO;
    }

    /**
     * Retrieves the {@linkplain User} based off of the given username
     * 
     * @param username
     *            The name of the String
     * @return {@linkplain User}
     *         status OK if found or if not found and created
     *         status INTERNAL_SERVER_ERROR if IOException
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        try {
            User user = this.userFileDAO.getUser(username);
            if (user != null)
                return new ResponseEntity<User>(user, HttpStatus.OK);
            else {
                user = this.userFileDAO.createUser(username);
                return new ResponseEntity<User>(user, HttpStatus.OK);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a {@linkplain Product} to the {@linkplain User}'s cart
     * 
     * @param username
     *            User to add the item to
     * @param product
     *            Product to add to the user's cart
     * @return {@linkplain User}
     *         status OK if everything works
     *         status INTERNAL_SERVER_ERROR if IOException
     */
    @PutMapping("/shoppingCart/{username}")
    public ResponseEntity<User> addToCart(@PathVariable String username, @RequestBody Product product) {
        try {
            User user = this.userFileDAO.getUser(username);
            if (user == null) {
                user = this.userFileDAO.createUser(username);
            }

            user.addToCart(product);
            user = this.userFileDAO.updateUser(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a {@linkplain Product} from the {@linkplain User}'s cart
     * 
     * @param username
     *            User to remove the item from
     * @param product
     *            Product to remove from the user's cart
     * @return {@linkplain User}
     *         status OK if everything works
     *         status INTERNAL_SERVER_ERROR if IOException
     */
    @PostMapping("/shoppingCart/{username}")
    public ResponseEntity<User> removeFromCart(@PathVariable String username, @RequestBody Product product) {
        try {
            User user = this.userFileDAO.getUser(username);
            if (user == null) {
                user = this.userFileDAO.createUser(username);
            }

            user.removeFromCart(product);
            user = this.userFileDAO.updateUser(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
