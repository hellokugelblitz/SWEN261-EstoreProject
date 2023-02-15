package com.estore.api.estoreapi.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.estore.api.estoreapi.model.Product;
import com.estore.api.estoreapi.model.User;
import com.estore.api.estoreapi.persistence.ProductDAO;
import com.estore.api.estoreapi.persistence.UserFileDAO;

@RestController
@RequestMapping("products")
public class ProductController {
    private ProductDAO productDao;
    private UserFileDAO userFileDAO;

    /*
     * Initializes the ProductController
     */
    public ProductController(ProductDAO productDAO, UserFileDAO userFileDAO) {
        this.productDao = productDAO;
        this.userFileDAO = userFileDAO;
    }

    /**
     * Retrieves {@linkplain Product Product} that matches given id
     *
     * @param id
     *            the id used to try and find a {@linkplain Product Product} with
     *            the same id
     *            number
     *
     * @return {@linkplain Product Product} object with the matching id number and
     *         status OK if found
     *         status NOT_FOUND if product does not exist
     *         status INTERNAL_SERVER_ERROR if IOException occurs
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        try {
            Product product = productDao.getProduct(id);
            if (product != null) {
                HttpHeaders headers = new HttpHeaders();
                return new ResponseEntity<Product>(product, headers, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * retrieves all {@linkplain Product Products} in array
     *
     * @return {@linkplain Product Product} array and status OK
     *         status INTERNAL_SERVER_ERROR if IOException occurs
     */
    @GetMapping("")
    public ResponseEntity<Product[]> getProducts() {
        try {
            Product[] products = productDao.getProducts();
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Used to get next id for newly created products.
     * 
     * @return Integer
     */
    @GetMapping("/nextId")
    public ResponseEntity<Integer> getNextId() {
        return new ResponseEntity<>(this.productDao.getProductId(), HttpStatus.OK);
    }

    /**
     * searches through {@linkplain Product Product} array to find one that matches
     * the name given
     *
     * @param name
     *            The string used to search through {@linkplain Product Product}
     *            array and find
     *            one that matches
     *
     * @return status NOT_FOUND if {@linkplain Product Product} cannot be found with
     *         String name
     *         product array of one or more {@linkplain Product Product} and status
     *         OK that
     *         matched String name
     *         status INTERNAL_SERVER_ERROR if IOException occurs
     */
    @GetMapping("/")
    public ResponseEntity<Product[]> searchProducts(@RequestParam String name) {
        try {
            Product[] products = productDao.findProducts(name);
            if (products.length == 0) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<Product[]>(products, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a new {@linkplain Product Product} if the same one doesn't already
     * exist
     *
     * @param Product
     *            {@linkplain Product Product}
     *            The {@linkplain Product Product} object that will be added
     *
     * @return status CONFLICT if {@linkplain Product Product} already exists in
     *         array
     *         {@linkplain Product Product} and status CREATED if product did not
     *         already
     *         exist
     *         status INTERNAL_SERVER_ERROR if IOException occurs
     */
    @PostMapping("")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            Product returnProduct = productDao.createProduct(product);
            if (returnProduct == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<Product>(returnProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static File multipartToFile(MultipartFile multipart, String fileName)
            throws IllegalStateException, IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + fileName + ".png");
        multipart.transferTo(convFile);
        return convFile;
    }

    @PostMapping("/custom/{username}")
    public ResponseEntity<User> createCustomProduct(@RequestParam("name") String name,
            @RequestParam("image") MultipartFile image, @RequestParam("price") String price,
            @PathVariable String username) {
        System.out.println("Here");
        try {
            Product prod = new Product(this.productDao.getProductId(), Integer.parseInt(price), 0, name,
                    multipartToFile(image, name), true);
            User user = userFileDAO.getUser(username);
            user.addToCart(prod);
            User returnUser = userFileDAO.updateUser(user);
            if (returnUser == null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            return new ResponseEntity<User>(returnUser, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/image/{name}", method = RequestMethod.GET, produces = "image/jpg")
    public @ResponseBody byte[] getProductImage(@PathVariable("name") String name) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + name + ".png");
        return Files.readAllBytes(file.toPath());
    }

    private Product findProduct(ArrayList<Product> cart, int id) {
        for (Product prod : cart) {
            if (prod.getId() == id) {
                return prod;
            }
        }

        return null;
    }

    @GetMapping("/custom/{username}/{id}")
    public ResponseEntity<Product> getCustomProductInformation(@PathVariable String username,
            @PathVariable int id) {
        try {
            User user = userFileDAO.getUser(username);
            ArrayList<Product> cart = user.getShoppingCart();
            if (cart == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Product product = findProduct(cart, id);
            if (product != null) {
                return new ResponseEntity<Product>(product, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates and returns {@linkplain Product Product}
     *
     * @param Product
     *            {@linkplain Product Product}
     *            The {@linkplain Product Product} object that will be updated
     *
     * @return {@linkplain Product Product} that was updated and status OK
     *         status NOT_FOUND if product was not found
     *         status INTERNAL_SERVER_ERROR if IOException occurs
     */
    @PutMapping("")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        try {
            Product foundProduct = productDao.getProduct(product.getId());
            if (foundProduct != null) {
                Product updatedProduct = productDao.updateProduct(product);
                return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks to see if user is an Admin
     * then locates product using id and deletes it if found
     *
     * @param id
     *            id number used to locate a {@linkplain Product Product}
     *
     * @param isAdmin
     *            boolean used to tell if user is Admin or not
     *
     * @return status FORBIDDEN if user is not an admin
     *         status OK if {@linkplain Product Product} is deleted
     *         status NOT_FOUND if {@linkplain Product Product} is not found with id
     *         status INTERNAL_SERVER_ERROR if IOException occurs
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable int id, @RequestHeader("admin") boolean isAdmin) {
        if (!isAdmin)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        try {
            if (productDao.deleteProduct(id)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
