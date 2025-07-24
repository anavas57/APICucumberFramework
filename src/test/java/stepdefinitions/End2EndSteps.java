package stepdefinitions;

import apiEngine.Book;
import apiEngine.EndPoints;
import apiEngine.IRestResponse;
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
    private static IRestResponse<UserAccountResponse> userAccountResponse;
    private static Book book;

    @Given("^I am an authorized user$")
    public void iAmAnAuthorizedUser() {
        System.out.println("Given I am an authorized user");

        AuthorizationRequest authRequest = new AuthorizationRequest("TOOLSQA-Test", "Test@@123");
        tokenResponse = EndPoints.authenticateUser(authRequest).getBody();
    }

    @Given("^A list of books is available$")
    public void aListOfBooksIsAvailable() {
        System.out.println("Given A list of books is available");
        IRestResponse<BooksResponse> booksResponse = EndPoints.getBooks();
        book = booksResponse.getBody().books.get(0);
        /**
        System.out.println("============================================= List of Books =============================================");
        for (int i=0; booksResponse.size() > i; i++) {
            System.out.println("Title: " + booksResponse.books.get(i).title + "\nISBN: " + booksResponse.books.get(i).isbn+ "\nAuthor: " +
                    booksResponse.books.get(i).author + "\nDescription: " + booksResponse.books.get(i).description +
                    "\nPublisher: " + booksResponse.books.get(i).publisher + "\nSubTitle: " + booksResponse.books.get(i).subTitle +
                    "\n" + booksResponse.books.get(i).website + "\nNumber of Pages: " + booksResponse.books.get(i).pages + "\n");
        }
        System.out.println("=========================================================================================================");
        **/
    }

    @When("^I \"([^\"]*)\" a book to my reading list$")
    public void bookToMyReadingList(String action) {
        System.out.println("When I " + action + " a book to my reading list");
        switch (action) {
            case "add":
                ISBNRequest isbn = new ISBNRequest(book.isbn);
                AddBooksRequest addBooksRequest = new AddBooksRequest(USER_ID, isbn);
                userAccountResponse = EndPoints.addBook(addBooksRequest, tokenResponse.token);
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
        switch (actionResult) {
            case "201":
                Assert.assertTrue(userAccountResponse.isSuccessful());
                Assert.assertEquals(201, userAccountResponse.getStatusCode());
                Assert.assertEquals(USER_ID, userAccountResponse.getBody().userID);
                Assert.assertEquals(book.isbn, userAccountResponse.getBody().books.get(0).isbn);

                break;
            case "204":
                Assert.assertEquals(204, response.getStatusCode());
                userAccountResponse = EndPoints.getUserAccount(USER_ID, tokenResponse.token);
                Assert.assertEquals(200, userAccountResponse.getStatusCode());
                Assert.assertEquals(0, userAccountResponse.getBody().books.size());
                break;
            default:
                System.out.println("Invalid Response Code");
        }
    }
}
