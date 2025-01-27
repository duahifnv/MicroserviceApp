package com.fizalise.orderservice.stub;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class InventoryClientStub {
    /*
    "GET-запрос" в InventoryService (находится в WireMock сервере)
     */
    public static void stubInventoryCall_returnsTrue(String skuCode, Integer quantity) {
        stubFor(
                get(urlEqualTo("/api/inventory/check?skuCode=" + skuCode
                        + "&quantity=" + quantity))
                .willReturn(okJson("true"))
        );
    }
    public static void stubInventoryCall_returnsFalse(String skuCode, Integer quantity) {
        stubFor(
                get(urlEqualTo("/api/inventory/check?skuCode=" + skuCode
                        + "&quantity=" + quantity))
                        .willReturn(okJson("false"))
        );
    }
}
