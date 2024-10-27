package com.example.backend.controllers;

import com.example.backend.models.History;
import com.example.backend.services.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @PostMapping("/add")
    public ResponseEntity<History> recordHistory(@RequestBody History history) {
        return ResponseEntity.ok(historyService.recordHistory(history));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<History>> getHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getHistoryByUserId(userId));
    }
}