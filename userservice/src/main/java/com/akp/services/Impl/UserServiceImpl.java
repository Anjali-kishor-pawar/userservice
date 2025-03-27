package com.akp.services.Impl;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.akp.entities.Hotel;
import com.akp.entities.Rating;
import com.akp.entities.User;
import com.akp.exception.ResourceNotFoundException;
import com.akp.repositories.UserRepository;
import com.akp.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestTemplate rTemplate;
/* 
    @Autowired
    private HotelService hotelService; */

    private final Logger logger = (Logger) LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //throw new UnsupportedOperationException("Not supported yet.");
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        //throw new UnsupportedOperationException("Not supported yet.");
        return userRepository.findAll();
    }

    // @SuppressWarnings("unchecked")
    @Override
    public User getUser(String userId) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        User user = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found on server !!"+ userId));
        //http://localhost:8083/ratings/user/352ead71-9e17-4252-900d-42e634cbbaae
        //http://RATING-SERVICE/ratings/user/
        Rating[] ratingOfUser = rTemplate.getForObject("http://localhost:8083/ratings/user/"+user.getUserId(),Rating[].class);
        
        logger.info("{}", (Object[]) ratingOfUser);
        List<Rating> ratings=Arrays.stream(ratingOfUser).toList();
        logger.info("{}", (Object) ratings);
        List<Rating> ratingList =ratings.stream().map(rating -> {
           ResponseEntity<Hotel> forEntity = rTemplate.getForEntity("http://localhost:8082/hotels/"+rating.getHotelId(), Hotel.class);
           Hotel hotel = forEntity.getBody();
           logger.info("{Response status code }",forEntity.getStatusCode());
           rating.setHotel(hotel);
            return rating; 
           /*  Hotel hotel=hotelService.getHotel(rating.getHotelId());
            logger.info("{}",hotelService);
            rating.setHotel(hotel);   
            return rating; */
        }).collect(Collectors.toList());
       user.setRating(ratingList);
        return user;
   
    }

}
