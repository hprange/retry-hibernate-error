package org.acme;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
@Transactional
@QuarkusTestResource(PostgresResource.class)
public class CarResourceTest {
    @Test
    void getCarNameConcurrently() throws ExecutionException, InterruptedException {

        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> callCarResource());
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> callCarResource());

        CompletableFuture.allOf(future1, future2);

        assertThat(future1.get(), is(200));
        assertThat(future2.get(), is(200));
    }

    Integer callCarResource() {
        return given()
                .queryParam("name", "Mustang")
                .when()
                .get("/cars")
                .then()
                .extract()
                .statusCode();
    }
}