package stepdefinitions;

import apiEngine.Book;
import apiEngine.model.EndPoints;
import apiEngine.model.requests.AddBooksRequest;
import apiEngine.model.requests.AuthorizationRequest;
import apiEngine.model.requests.ISBNRequest;
import apiEngine.model.requests.RemoveBooksRequest;
import apiEngine.model.responses.BooksResponse;
import apiEngine.model.responses.TokenResponse;
import apiEngine.model.responses.UserAccountResponse;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.junit.Assert;

public class End2EndSteps {
    private static final String USER_ID = "392ad8a0-840d-4793-a7c3-89da98e23f47";
    private static final String USERNAME = "TOOLS-QA-Test";
    private static final String PASSWORD = "Test@@123";
    private static final String BASE_URL = "https://bookstore.toolsqa.com";

    private static Response response;
    private static TokenResponse tokenResponse;
    private static Book book;

    @Given("^I am an authorized user$")
    public void iAmAnAuthorizedUser() {
        System.out.println("Given I am an authorized user");

        AuthorizationRequest authRequest = new AuthorizationRequest("TOOLSQA-Test", "Test@@123");
        response = EndPoints.authenticateUser(authRequest);
        // Deserializing the Response body into tokenResponse
        tokenResponse = response.getBody().as(TokenResponse.class);
    }

    @Given("^A list of books is available$")
    public void aListOfBooksIsAvailable() {
        System.out.println("Given A list of books is available");
        response = EndPoints.getBooks();// Deserializing the Response body into Books class
        BooksResponse booksResponse = response.getBody().as(BooksResponse.class);
        book = booksResponse.books.get(0);

        System.out.println("============================================= List of Books =============================================");
        for (int i=0; booksResponse.books.size() > i; i++) {
            System.out.println("Title: " + booksResponse.books.get(i).title + "\nISBN: " + booksResponse.books.get(i).isbn+ "\nAuthor: " +
                    booksResponse.books.get(i).author + "\nDescription: " + booksResponse.books.get(i).description +
                    "\nPublisher: " + booksResponse.books.get(i).publisher + "\nSubTitle: " + booksResponse.books.get(i).subTitle +
                    "\n" + booksResponse.books.get(i).website + "\nNumber of Pages: " + booksResponse.books.get(i).pages + "\n");
        }
        System.out.println("=========================================================================================================");
    }

    @When("^I \"([^\"]*)\" a book to my reading list$")
    public void bookToMyReadingList(String action) {
        System.out.println("When I " + action + " a book to my reading list");
        ISBNRequest isbn = new ISBNRequest(book.isbn);
        switch (action) {
            case "add":
                AddBooksRequest addBooksRequest = new AddBooksRequest(USER_ID, isbn);
                response = EndPoints.addBook(addBooksRequest, tokenResponse.token);
                break;
            case "remove":
                RemoveBooksRequest removeBookRequest = new RemoveBooksRequest(USER_ID, book.isbn);
                response = EndPoints.removeBook(removeBookRequest, tokenResponse.token);
                break;
            default:
                System.out.println(action + " is an invalid action");
        }
    }

    @Then("^The books is \"([^\"]*)\"$")
    public void theBooksIs(String actionResult) {
        System.out.println("Then the book is " + actionResult);
        UserAccountResponse userAccountResponse;
        switch (actionResult) {
            case "201":
                Assert.assertEquals(201, response.getStatusCode());
                userAccountResponse = response.getBody().as(UserAccountResponse.class);
                Assert.assertEquals(USER_ID, userAccountResponse.userID);
                Assert.assertEquals(book.isbn, userAccountResponse.books.get(0).isbn);
                break;
            case "204":
                Assert.assertEquals(204, response.getStatusCode());
                response = EndPoints.getUserAccount(USER_ID, tokenResponse.token);
                Assert.assertEquals(200, response.getStatusCode());

                UserAccountResponse userAccount = response.getBody().as(UserAccountResponse.class);
                Assert.assertEquals(0, userAccount.books.size());
                break;
            default:
                System.out.println("Invalid Response Code");
        }
    }
}
