package com.example.backend.controllers;

import com.example.backend.models.Hotel;
import com.example.backend.models.HotelRoom;
import com.example.backend.repositories.HotelRepository;
import com.example.backend.repositories.HotelRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")  // 允许跨域请求
@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    // 上传图片保存路径
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/image/hotel/";

    // 创建酒店，包含图片上传功能
    @PostMapping(consumes = "multipart/form-data")
    public Hotel createHotel(@RequestParam("name") String name,
                             @RequestParam("location") String location,
                             @RequestParam("city") String city,
                             @RequestParam("type") String type,
                             @RequestParam("image") MultipartFile imageFile) throws IOException {

        Hotel hotel = new Hotel();
        hotel.setName(name);
        hotel.setLocation(location);
        hotel.setCity(city);
        hotel.setType(type);

        // 处理图片上传
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();  // 防止文件名冲突
            String imagePath = UPLOAD_DIR + fileName;
            Path path = Paths.get(imagePath);
            Files.createDirectories(path.getParent());
            Files.write(path, imageFile.getBytes());
            hotel.setImageUrl("/image/hotel/" + fileName);  // 设置相对路径用于前端访问
        }

        return hotelRepository.save(hotel);
    }

    // 根据ID获取单个酒店
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable int id) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        return hotel.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    // 获取酒店列表，带可选的查询参数
    @GetMapping
    public List<Hotel> getHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String type) {

        // 检查查询参数是否存在，并根据这些条件进行筛选
        if (name != null || city != null || type != null) {
            // 这里可以实现自定义查询逻辑
            return hotelRepository.findByQueryParams(name, city, type);
        } else {
            return hotelRepository.findAll();
        }
    }
    // 更新酒店信息，包括图片更新功能
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable int id,
                                             @RequestParam("name") String name,
                                             @RequestParam("location") String location,
                                             @RequestParam("city") String city,
                                             @RequestParam("type") String type,
                                             @RequestParam(value = "image", required = false) MultipartFile imageFile) throws IOException {
        Optional<Hotel> existingHotel = hotelRepository.findById(id);
        if (existingHotel.isPresent()) {
            Hotel hotel = existingHotel.get();
            hotel.setName(name);
            hotel.setLocation(location);
            hotel.setCity(city);
            hotel.setType(type);

            // 处理图片更新
            if (imageFile != null && !imageFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                String imagePath = UPLOAD_DIR + fileName;
                Path path = Paths.get(imagePath);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());
                hotel.setImageUrl("/image/hotel/" + fileName);  // 更新相对路径用于前端访问
            }

            hotelRepository.save(hotel);
            return ResponseEntity.ok(hotel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除酒店
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable int id) {
        if (hotelRepository.existsById(id)) {
            hotelRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据酒店ID获取所有房型
    @GetMapping("/{id}/rooms")
    public List<HotelRoom> getRoomsByHotelId(@PathVariable Long id) {
        return hotelRoomRepository.findByHotel_HotelId(id);
    }

    // 为酒店添加房型
    @PostMapping("/{id}/rooms")
    public HotelRoom addRoomToHotel(@PathVariable int id, @RequestBody HotelRoom room) {
        Optional<Hotel> hotel = hotelRepository.findById(id);
        if (hotel.isPresent()) {
            room.setHotel(hotel.get());
            return hotelRoomRepository.save(room);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "酒店不存在");
        }
    }

    // 更新房型信息
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<HotelRoom> updateRoom(@PathVariable Long roomId, @RequestBody HotelRoom updatedRoom) {
        Optional<HotelRoom> existingRoom = hotelRoomRepository.findById(roomId);
        if (existingRoom.isPresent()) {
            HotelRoom room = existingRoom.get();
            room.setRoomType(updatedRoom.getRoomType());
            room.setPrice(updatedRoom.getPrice());
            room.setDescription(updatedRoom.getDescription());
            hotelRoomRepository.save(room);
            return ResponseEntity.ok(room);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除房型
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        if (hotelRoomRepository.existsById(roomId)) {
            hotelRoomRepository.deleteById(roomId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
