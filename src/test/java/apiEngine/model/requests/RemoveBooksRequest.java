package apiEngine.model.requests;

public class RemoveBooksRequest {
    public String isbn;
    public String userId;

    public RemoveBooksRequest(String userId, String isbn) {
        this.userId = userId;
        this.isbn = isbn;
    }
}
