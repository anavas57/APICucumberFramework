package apiEngine.model;

import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.RemoveBooksRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndPoints {
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    public static Response authenticateUser(AuthorizationRequest authRequest) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        Response response = request.body(authRequest).post("/Account/v1/GenerateToken");
        return response;
    }

    public static Response getBooks() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Content-Type", "application/json");
        Response response = request.get("/BookStore/v1/Books");
        return response;
    }

    public static Response addBook(AddBooksRequest addBooksRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        Response response = request.body(addBooksRequest).post("/BookStore/v1/Books");
        return response;
    }

    public static Response removeBook(RemoveBooksRequest removeBookRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        Response response = request.body(removeBookRequest).delete("/BookStore/v1/Book");
        return response;
    }

    public static Response getUserAccount(String userId, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();

        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");

        Response response = request.get("/Account/v1/User/" + userId);
        return response;
    }
}
