package com.akp.External.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.akp.entities.Hotel;



@FeignClient(name = "HOTEL-SERVICE")
public interface HotelService {

    @GetMapping("/hotels/{hId}")
    public Hotel getHotel(@PathVariable("hId") String hotelId);
}



/* 
package com.akp.External.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.akp.entities.Hotel;

@FeignClient(name="HOTEL-SERVICE")
public interface  HotelService {

    @GetMapping("/hotels/{hId}")
    Hotel getHotel(@PathVariable("hId") String  hotelId);
} */

