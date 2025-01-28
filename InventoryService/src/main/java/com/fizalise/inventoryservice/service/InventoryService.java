package com.fizalise.inventoryservice.service;

import com.fizalise.inventoryservice.entity.InventoryItem;
import com.fizalise.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record InventoryService(InventoryRepository inventoryRepository) {
    public boolean isInInventory(String skuCode, Integer quantity) {
        return inventoryRepository
                .existsBySkuCodeAndQuantityGreaterThanEqual(skuCode, quantity);
    }
    public List<InventoryItem> findAllItems() {
        return inventoryRepository.findAll();
    }
}
