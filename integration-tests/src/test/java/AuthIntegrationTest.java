import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:4004";
    }
    // Format: should -> expected outcome -> expected return status
    @Test
    public void shouldReturnOkWithValidToken() {
        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "password123"
                    }
                """;
        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract()
                .response();

        System.out.println("Generated token: " + response.jsonPath().getString("token"));
    }

    @Test
    public void shouldReturnUnauthorisedOnInvalidLogin() {
        String loginPayload = """
                    {
                        "email": "testfail@test.com",
                        "password": "password123"
                    }
                """;
        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnUnauthorisedWithIncorrectPassword() {
        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": "deadbeef"
                    }
                """;
        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnUnauthorisedNoPassword() {
        String loginPayload = """
                    {
                        "email": "testuser@test.com",
                        "password": ""
                    }
                """;
        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    public void shouldReturnUnauthorisedNoEmail() {
        String loginPayload = """
                    {
                        "email": "",
                        "password": "password123"
                    }
                """;
        given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }
}
