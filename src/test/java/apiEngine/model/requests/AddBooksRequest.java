package apiEngine.model.requests;

import java.util.ArrayList;
import java.util.List;

public class AddBooksRequest {
    public String userId;
    public List<ISBNRequest> collectionOfIsbns;

    //As of now this is for adding a single book, later we will add another constructor.
    //That will take a collection of ISBN to add multiple books
    public AddBooksRequest(String userId, ISBNRequest isbn){
        this.userId = userId;
        collectionOfIsbns = new ArrayList<ISBNRequest>();
        collectionOfIsbns.add(isbn);
    }
}
