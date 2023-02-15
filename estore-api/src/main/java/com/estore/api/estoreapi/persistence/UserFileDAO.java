package com.estore.api.estoreapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.estore.api.estoreapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserFileDAO {
    private ObjectMapper objectMapper;
    private String filename;
    private Map<String, User> users;

    /**
     * Creates a User File Data Access Object
     * 
     * @param filename
     *            Filename to read from and write to
     * @param objectMapper
     *            Provides JSON Object to/from Java Object serialization
     *            and deserialization
     * 
     * @throws IOException
     *             when file cannot be accessed or read from
     */
    public UserFileDAO(@Value("${users.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        this.load();
    }

    /**
     * Loads {@linkplain User products} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException
     *             when file cannot be accessed or read from
     */
    private void load() throws IOException {
        this.users = new HashMap<>();
        User[] userArray = objectMapper.readValue(new File(this.filename), User[].class);

        for (User user : userArray) {
            this.users.put(user.getName(), user);
        }
    }

    /**
     * Saves the user array to the provided file
     * 
     * @throws IOException
     */
    private void save() throws IOException {
        Object[] userArray = this.users.values().toArray();
        objectMapper.writeValue(new File(filename), userArray);
    }

    /**
     * Creates and saves a {@linkplain User user}
     * 
     * @param User
     *            {@linkplain User user} object to be created and saved
     *
     * @return new {@link User User} if successful, false otherwise
     * 
     * @throws IOException
     *             if an issue with underlying storage
     */
    public User createUser(String username) throws IOException {
        synchronized(this.users) {
            if (this.users.containsKey(username)) {
                return this.users.get(username);
            } else {
                User user = new User(username, null);
                this.users.put(username, user);
                this.save();
                return user;
            }
        }
    }

    /**
     * Updates and saves a {@linkplain User User}
     * 
     * @param {@link
     *            User User} object to be updated and saved
     * 
     * @return updated {@link User User} if successful, null if
     *         {@link User User} could not be found
     * 
     * @throws IOException
     *             if underlying storage cannot be accessed
     */
    public User updateUser(User user) throws IOException {
        synchronized(this.users) {
            if (this.users.containsKey(user.getName())) {
                this.users.put(user.getName(), user);
                this.save();
                return user;
            }

            return null;
        }
    }

    /**
     * Retrieves a {@linkplain User User} with the given id
     * 
     * @param username
     *            The username of the {@link User User} to get
     * 
     * @return a {@link User User} object with the matching username
     *         <br>
     *         null if no {@link User User} with a matching username is found
     * 
     * @throws IOException
     *             if an issue with underlying storage
     */
    public User getUser(String username) throws IOException {
        synchronized(this.users) {
            return this.users.get(username);
        }
    }
}
