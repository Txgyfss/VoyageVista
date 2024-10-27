package com.example.backend.controllers;

import com.example.backend.models.Hotel;
import com.example.backend.models.HotelBooking;
import com.example.backend.models.HotelRoom;
import com.example.backend.models.User;
import com.example.backend.repositories.HotelBookingRepository;
import com.example.backend.repositories.HotelRepository;
import com.example.backend.repositories.HotelRoomRepository;
import com.example.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class HotelBookingController {

    private final HotelBookingRepository hotelBookingRepository;
    private final HotelRepository hotelRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(HotelBookingController.class);

    public HotelBookingController(HotelBookingRepository hotelBookingRepository,
                                  HotelRepository hotelRepository,
                                  HotelRoomRepository hotelRoomRepository,
                                  UserRepository userRepository) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelRepository = hotelRepository;
        this.hotelRoomRepository = hotelRoomRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        logger.info("Received booking request: {}", bookingRequest);

        // Find the user, hotel, and room from the database
        Optional<User> userOptional = userRepository.findById(bookingRequest.getUserId());
        Optional<HotelRoom> roomOptional = hotelRoomRepository.findById(bookingRequest.getRoomId());
        Optional<Hotel> hotelOptional = hotelRepository.findById(bookingRequest.getHotelId());

        // Log if any of the necessary entities are missing
        if (!userOptional.isPresent()) {
            logger.error("User not found with ID: {}", bookingRequest.getUserId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not found.");
        }
        if (!roomOptional.isPresent()) {
            logger.error("Room not found with ID: {}", bookingRequest.getRoomId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Room not found.");
        }
        if (!hotelOptional.isPresent()) {
            logger.error("Hotel not found with ID: {}", bookingRequest.getHotelId());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Hotel not found.");
        }

        // Log found entities
        User user = userOptional.get();
        HotelRoom room = roomOptional.get();
        Hotel hotel = hotelOptional.get();

        logger.info("User: {}, Room: {}, Hotel: {}", user.getUserId(), room.getRoomId(), hotel.getHotelId());

        // Calculate nights and total price
        long nights = ChronoUnit.DAYS.between(bookingRequest.getCheckIn(), bookingRequest.getCheckOut());
        if (nights <= 0) {
            logger.error("Check-in date must be before check-out date.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid check-in/check-out dates.");
        }

        BigDecimal totalPrice = room.getPrice().multiply(BigDecimal.valueOf(nights));
        logger.info("Calculated nights: {}, Total price: {}", nights, totalPrice);

        // Create new booking
        HotelBooking booking = new HotelBooking();
        booking.setUser(user);
        booking.setRoom(room);
        booking.setHotel(hotel);
        booking.setCheckIn(bookingRequest.getCheckIn());
        booking.setCheckOut(bookingRequest.getCheckOut());
        booking.setNights((int) nights);
        booking.setTotalPrice(totalPrice);

        // Save booking and log
        hotelBookingRepository.save(booking);
        logger.info("Booking successfully saved: {}", booking.getBookingId());

        return ResponseEntity.ok(booking);
    }
}
