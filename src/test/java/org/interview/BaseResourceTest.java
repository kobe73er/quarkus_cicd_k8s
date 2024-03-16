package org.interview;

import static io.restassured.RestAssured.given;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
class BaseResourceTest {

  @Test
  void testHelloEndpoint() {
    given().when().get("/").then().statusCode(200); // 只验证响应状态码为 200
  }
}
