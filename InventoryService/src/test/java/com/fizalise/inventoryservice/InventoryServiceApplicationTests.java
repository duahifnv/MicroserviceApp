package com.fizalise.inventoryservice;

import com.fizalise.inventoryservice.entity.InventoryItem;
import com.fizalise.inventoryservice.repository.InventoryRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RequiredArgsConstructor
class InventoryServiceApplicationTests {
    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:17.2");
    @Autowired
    InventoryRepository inventoryRepository;
    @LocalServerPort
    Integer port;
    @BeforeAll
    static void init() {
        postgreSQLContainer.start();
    }
    @BeforeEach
    void setupRestAssured(@Autowired InventoryRepository inventoryRepository) {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        inventoryRepository.deleteAll();
    }
    @Test
    void getAllItems_returnsAllInventoryItems() {
        List<InventoryItem> inventoryItems = List.of(
                InventoryItem.builder().skuCode("iPhone_15").quantity(100).build(),
                InventoryItem.builder().skuCode("iPhone_16").quantity(200).build()
        );
        inventoryRepository.saveAll(inventoryItems);
        RestAssured.given().contentType(ContentType.JSON)
                .when().get("/api/inventory")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("[0].id", Matchers.notNullValue())
                .body("[0].skuCode", Matchers.equalTo("iPhone_15"))
                .body("[0].quantity", Matchers.equalTo(100))
                .body("[1].id", Matchers.notNullValue())
                .body("[1].skuCode", Matchers.equalTo("iPhone_16"))
                .body("[1].quantity", Matchers.equalTo(200));
    }
    @Test
    void isInStock_ItemInStock_returnsTrue() {
        List<InventoryItem> inventoryItems = List.of(
                InventoryItem.builder().skuCode("iPhone_15").quantity(100).build()
        );
        inventoryRepository.saveAll(inventoryItems);
        Assertions.assertTrue(
                RestAssured.given().contentType(ContentType.JSON)
                .queryParam("skuCode", "iPhone_15")
                .queryParam("quantity", 100)
                .when().get("/api/inventory/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(Boolean.class)
        );
    }
    @Test
    void isInStock_ItemNotInStock_returnsFalse() {
        List<InventoryItem> inventoryItems = List.of(
                InventoryItem.builder().skuCode("iPhone_15").quantity(100).build()
        );
        inventoryRepository.saveAll(inventoryItems);
        Assertions.assertFalse(
                RestAssured.given().contentType(ContentType.JSON)
                        .queryParam("skuCode", "iPhone_16")
                        .queryParam("quantity", 100)
                        .when().get("/api/inventory/check")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(Boolean.class)
        );
    }
    @Test
    void isInStock_QuantityOverRemained_returnsFalse() {
        List<InventoryItem> inventoryItems = List.of(
                InventoryItem.builder().skuCode("iPhone_15").quantity(100).build()
        );
        inventoryRepository.saveAll(inventoryItems);
        Assertions.assertFalse(
                RestAssured.given().contentType(ContentType.JSON)
                        .queryParam("skuCode", "iPhone_15")
                        .queryParam("quantity", 101)
                        .when().get("/api/inventory/check")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract().as(Boolean.class)
        );
    }
}
