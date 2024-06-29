package BooksAPIs;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

@SuppressWarnings("ALL")
public class Books {
    public Books() {
        RestAssured.baseURI = "https://simple-books-api.glitch.me/";
    }


    private JSONObject loginBody(String clientName , String clientEmail) {
        JSONObject login = new JSONObject();
        login.put("clientName", clientName);
        login.put("clientEmail", clientEmail);
        return login;
    }


    @Step("authentication")
    public Response authentication(String clientName , String clientEmail ) {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .body(loginBody(clientName ,clientEmail))
                .post("api-clients");
    }


    @Step("get books list")
    public Response getBooks() {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .get("books");
    }

    @Step("get Specific Book id:[{id}]")
    public Response getSpecificBook(int id) {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .get("books/" + id);
    }


    @Step("create Order Body bookId:[{bookId}] , customerName:[{customerName}]")
    private JSONObject createOrderBody(int bookId, String customerName) {
        JSONObject OrderBody = new JSONObject();
        OrderBody.put("bookId", bookId);
        OrderBody.put("customerName", customerName);
        return OrderBody;
    }


    @Step("create Order ==> bookId:[{bookId}] , customerName:[{customerName}]")
    public Response createOrder(int bookId, String customerName, String token) {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer "+token)
                .body(createOrderBody(bookId, customerName))
                .post("orders");
    }



    @Step("get orders list")
    public Response getOrders(String token) {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .header("Authorization" , "Bearer "+token)
                .get("orders");
    }




    @Step("updated Order Body updatedcustomerName:[{customerName}]")
    private JSONObject updatedBookingBody(String customerName) {
        JSONObject updatedOrderBody = new JSONObject();
        updatedOrderBody.put("customerName", customerName);
        return updatedOrderBody;
    }


    @Step("updated order ==> updatedCustomerName:[{updatedcustomerName}]")
    public Response updateOrder(String token,String orderId, String updatedcustomerName ) {

        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .header("Authorization" , "Bearer "+token)
                .body(updatedBookingBody(updatedcustomerName))
                .patch("orders/"+orderId);
    }


    @Step("delete order orderId:[{orderId}]")
    public Response deleteOrder(String orderId,String customerName,String token) {
        return RestAssured
                .given()
                .filter(new AllureRestAssured())
                .header("Authorization" , "Bearer "+token)
                .body(customerName)
                .delete("orders/" + orderId);
    }


}


