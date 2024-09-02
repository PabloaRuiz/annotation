package com.annotations.resource;

import com.annotations.repository.AnnotationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.annotations.config.TestFiles.ANNOTATION_REQUEST;
import static com.annotations.config.TestFiles.DESCRIPTION_JSON;
import static io.restassured.RestAssured.given;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
public class AnnotationResourceTest {

    public static final String PATH_BASE = "/api/v1/annotations";
    public static final String PASSWORD_KEY = "carl hung";

    @Inject
    AnnotationRepository annotationRepository;

    @Inject
    ObjectMapper objectMapper;

    @Test
    @Order(1)
    void saveAnnotationTest() {
        given()
                .contentType(APPLICATION_JSON)
                .header("password", PASSWORD_KEY)
                .body(ANNOTATION_REQUEST.load())
                .post(PATH_BASE)
                .then()
                .statusCode(204);
    }

    @Test
    @Order(2)
    void getAnnotationWhereTitleTest() {
        var id = "66d50bbb245f003e3e65d74e";

        given()
                .contentType(APPLICATION_JSON)
                .header("password", PASSWORD_KEY)
                .body(ANNOTATION_REQUEST.load())
                .get(PATH_BASE + "/id/" + id)
                .then()
                .statusCode(200)
                .body("title", equalTo("Rotina"))
                .body("date", equalTo("2024-08-25T14:45:00.000+00:00"))
                .body("description", equalTo("ler os livros Historia do Brasil e Solid"))
                .body("status", equalTo("OPEN"));

    }

    @Test
    @Order(3)
    void getListAnnotationWithStatusTest() {
        given()
                .contentType(APPLICATION_JSON)
                .header("password", PASSWORD_KEY)
                .get(PATH_BASE + "/status/OPEN")
                .then()
                .statusCode(200)
                .body("[0].title", equalTo("Rotina"))
                .body("[0].date", equalTo("2024-08-25T14:45:00.000+00:00"))
                .body("[0].description", equalTo("ler os livros Historia do Brasil e Solid"))
                .body("[0].status", equalTo("OPEN"));
    }

    @Test
    @Order(4)
    void ModifyNewDescriptionTest() {
        var id = "66d50bbb245f003e3e65d74e";

        given()
                .contentType(APPLICATION_JSON)
                .header("password", PASSWORD_KEY)
                .body(DESCRIPTION_JSON.load())
                .patch(PATH_BASE + "/modify/" + id)
                .then()
                .statusCode(200)
                .body("title", equalTo("Rotina"))
                .body("date", equalTo("2024-08-25T14:45:00.000+00:00"))
                .body("description", equalTo("Beber água"))
                .body("status", equalTo("OPEN"));
    }
}
