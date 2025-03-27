package com.akp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.akp.entities.User;
import com.akp.services.Impl.UserServiceImpl;
import com.akp.services.UserService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = (Logger) LoggerFactory.getLogger(UserServiceImpl.class);

    @RequestMapping("/home")
    public String Home()
    {
        //System.out.println("Home");
        return "Home home home Home";
    }

    //create
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user)
    {
        User user1=userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }

    int retryCount=1;
    //get single user
    @GetMapping("/{userId}")
   //@CircuitBreaker(name="RATING_HOTEL_CB",fallbackMethod="ratingHotelFallback")
   //@Retry(name="RATING_HOTEL_RETRYSERVICE",fallbackMethod="ratingHotelFallback")
   @RateLimiter(name="userRateLimiter",fallbackMethod="ratingHotelFallback")
    public ResponseEntity<User> getSingleUser(@PathVariable String userId)
    {
        logger.info("Retry count : {} ",retryCount);
        retryCount++;
        User user1=userService.getUser(userId);
        return ResponseEntity.ok(user1);
    }


    public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex)
    {
       // logger.info("Fallback is executed because service is down.",ex.getMessage());
        User user = User.builder()
                    .email("abcsd@gmail.com")
                    .name("Dummy")
                    .about("Created because services is down")
                    .userId("12345")
                    .build();
        return new ResponseEntity<>(user,HttpStatus.BAD_REQUEST);
    }
   //get all user
   @GetMapping
   public ResponseEntity<List<User>> getAllUser()
   {
        List<User> allUser = userService.getAllUsers();
            return ResponseEntity.ok(allUser);   
   }

}
