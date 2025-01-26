package com.fizalise.inventoryservice.controller;

import com.fizalise.inventoryservice.entity.InventoryItem;
import com.fizalise.inventoryservice.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public record InventoryController(InventoryService inventoryService) {
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryItem> getAllItems() {
        return inventoryService.findAllItems();
    }
    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@RequestParam String skuCode,
                             @RequestParam Integer quantity) {
        return inventoryService.isInInventory(skuCode, quantity);
    }
}