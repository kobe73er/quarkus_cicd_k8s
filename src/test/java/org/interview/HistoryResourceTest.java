package org.interview;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.interview.dto.DomainHistoryDTO;
import org.interview.resource.HistoryResource;
import org.interview.service.DomainLookupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class HistoryResourceTest {

  private HistoryResource historyResource;
  private DomainLookupService domainLookupService;

  @BeforeEach
  public void setUp() {
    domainLookupService = mock(DomainLookupService.class);
    historyResource = new HistoryResource();
    historyResource.domainLookupService = domainLookupService;
  }

  @Test
  void testGetHistoryEndpoint() {
    given().when().get("/v1/history").then().statusCode(200).body(is(not(empty())));
  }

  @Test
  public void testGetHistory_Success() {
    List<DomainHistoryDTO> historyList = new ArrayList<>();
    DomainHistoryDTO history1 = new DomainHistoryDTO();
    history1.setDomain("example.com");
    history1.setClient_ip("192.168.1.1");

    DomainHistoryDTO history2 = new DomainHistoryDTO();
    history2.setDomain("test.com");
    history2.setClient_ip("192.168.1.2");

    historyList.add(history1);
    historyList.add(history2);

    when(domainLookupService.getDomainHistory()).thenReturn(historyList);

    Response response = historyResource.getHistory();

    assertEquals(200, response.getStatus(), "Response status should be 200 OK");
    assertEquals(historyList, response.getEntity(), "Response entity should match historyList");
  }
}
