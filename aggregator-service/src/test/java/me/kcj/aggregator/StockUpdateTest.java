package me.kcj.aggregator;

import lombok.extern.slf4j.Slf4j;
import me.kcj.aggregator.mockservice.StockMockService;
import me.kcj.aggregator.mockservice.UserMockService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.annotation.DirtiesContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@DirtiesContext
@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.stock-service.address=in-process:integration-test"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class StockUpdateTest {
    private static final String STOCK_UPDATES_ENDPOINT = "http://localhost:%d/stock/updates";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void stockUpdateTest() {
       var list =  restTemplate.execute(
          STOCK_UPDATES_ENDPOINT.formatted(port),
          HttpMethod.GET,
          null,
            this::getResponse
        );
        Assertions.assertEquals(5, list.size());
    }


    private List<String> getResponse(ClientHttpResponse clientHttpResponse) {
        var list = new ArrayList<String>();
        try (var reader = new BufferedReader(new InputStreamReader(clientHttpResponse.getBody()))) {
            String line;
            while(Objects.nonNull(line = reader.readLine())){
                if(!line.isEmpty()) {
                    log.info(line);
                    list.add(line);
                }
            }
        }catch (Exception e) {
            log.error("streaming error", e);
        }
        return list;
    }


    @TestConfiguration
    static class TestConfig {

        @GrpcService
        public StockMockService stockMockService() {
            return new StockMockService();
        }


    }
}
