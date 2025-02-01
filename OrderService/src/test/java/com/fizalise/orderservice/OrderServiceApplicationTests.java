package com.fizalise.orderservice;

import com.fizalise.orderservice.stub.InventoryClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {
	@ServiceConnection
	static PostgreSQLContainer<?> postgreSQLContainer =
			new PostgreSQLContainer<>("postgres:17.2");
	@LocalServerPort
	Integer port;
	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	static {
		postgreSQLContainer.start();
	}
	@Test
	void shouldCreateOrder_ItemInStock() {
		String requestBody = """
				{
				    "skuCode": "samsung_note_22",
				    "price": 1200,
				    "quantity": 1,
				    "userDetails": {
						"email": "igor@mail.ru",
						"firstName": "Igor",
						"lastName": "Vihorkov"
					}
				}
				""";
		/*
		Искусственно задаем сценарий, при котором по GET запросу в inventoryClient
		будет возвращен необходимый JSON (делаем вид, что продукт есть в базе)
		 */
		InventoryClientStub.stubInventoryCall_returnsTrue("samsung_note_22", 1);

		RestAssured.given().contentType("application/json").body(requestBody)
				.when().post("/api/order")
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.contentType("application/json")
				.body("id", Matchers.notNullValue())
				.body("orderNumber", Matchers.notNullValue())
				.body("skuCode", Matchers.equalTo("samsung_note_22"))
				.body("price", Matchers.equalTo(1200))
				.body("quantity", Matchers.equalTo(1));
	}
	@Test
	void shouldCreateOrder_ItemNotInStock() {
		String requestBody = """
				{
				    "skuCode": "samsung_note_22",
				    "price": 1200,
				    "quantity": 1,
				    "userDetails": {
						"email": "igor@mail.ru",
						"firstName": "Igor",
						"lastName": "Vihorkov"
					}
				}
				""";
		InventoryClientStub.stubInventoryCall_returnsFalse("samsung_note_22", 1);

		RestAssured.given().contentType("application/json").body(requestBody)
				.when().post("/api/order")
				.then()
				.statusCode(HttpStatus.BAD_REQUEST.value());
	}
	@Test
	void shouldReturnAllOrders() {
		RestAssured.given()
				.when().get("/api/order")
				.then()
				.statusCode(HttpStatus.OK.value())
				.body("[0].id", Matchers.notNullValue())
				.body("[0].orderNumber", Matchers.notNullValue())
				.body("[0].skuCode", Matchers.equalTo("iphone_15"))
				.body("[0].price", Matchers.equalTo(1000F))
				.body("[0].quantity", Matchers.equalTo(2))
				.body("[1].id", Matchers.notNullValue())
				.body("[1].orderNumber", Matchers.notNullValue())
				.body("[1].skuCode", Matchers.equalTo("iphone_16"))
				.body("[1].price", Matchers.equalTo(1200F))
				.body("[1].quantity", Matchers.equalTo(1));
	}
}
