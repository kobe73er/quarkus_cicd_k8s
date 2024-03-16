package org.interview;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class ToolsResourceTest {

  @Test
  public void testLookupDomainSuccess() {
    given()
        .queryParam("domain", "example.com") // 这应该是一个有效的域名
        .when()
        .get("/v1/tools/lookup")
        .then()
        .statusCode(200) // 成功的请求应该返回 200 状态码
        .body(containsString("addresses")); // 简单地检查响应体中是否包含关键字"addresses"
  }
}
