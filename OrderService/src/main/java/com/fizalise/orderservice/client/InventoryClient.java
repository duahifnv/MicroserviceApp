package com.fizalise.orderservice.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

//@FeignClient(value = "inventory", url = "${inventory-service.url}")
public interface InventoryClient {
    @GetExchange("/api/inventory/check")
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);
}
