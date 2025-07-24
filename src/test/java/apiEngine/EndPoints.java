package apiEngine;

import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.RemoveBooksRequest;
import apiEngine.model.responses.BooksResponse;
import apiEngine.model.responses.TokenResponse;
import apiEngine.model.responses.UserAccountResponse;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndPoints {
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    public static IRestResponse<TokenResponse> authenticateUser(AuthorizationRequest authRequest) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request.body(authRequest).post(Route.generateToken());
        return new RestResponse<>(TokenResponse.class, response);
    }

    public static IRestResponse<BooksResponse> getBooks() {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        Response response = request.get(Route.books());
        return new RestResponse<>(BooksResponse.class, response);
    }

    public static IRestResponse<UserAccountResponse> addBook(AddBooksRequest addBooksRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        Response response = request.body(addBooksRequest).post(Route.books());
        return new RestResponse<>(UserAccountResponse.class, response);
    }

    public static Response removeBook(RemoveBooksRequest removeBookRequest, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        return request.body(removeBookRequest).delete(Route.book());
    }

    public static IRestResponse<UserAccountResponse> getUserAccount(String userId, String token) {
        RestAssured.baseURI = BASE_URL;
        RequestSpecification request = RestAssured.given();
        request.header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
        Response response = request.get(Route.userAccount(userId));
        return new RestResponse<>(UserAccountResponse.class, response);
    }
}
