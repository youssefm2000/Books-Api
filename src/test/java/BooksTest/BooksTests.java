package BooksTest;
import BooksAPIs.Books;
import Utils.CurrentDate;
import Utils.JsonFileManager;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class BooksTests {

    Books books;
    JsonFileManager updateBookJson;
    JsonFileManager loginJson;
    JsonFileManager createOrderJson;

    @BeforeClass
    public void beforeClass(){
        books = new Books();
        createOrderJson = new JsonFileManager("src/test/resources/TestData/BooksTestData/CreateOrderTestData.json");
        updateBookJson = new JsonFileManager("src/test/resources/TestData/BooksTestData/UpdateBook.json");
        loginJson = new JsonFileManager("src/test/resources/TestData/BooksTestData/loginBook.json");
    }

    @Test(description = "authentication")
    @Story("post Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description : Verify authentication token")
    public void login() {
        String token = books.authentication
                        (
                                loginJson.getTestData("clientName") + CurrentDate.GenerateCurrentDateAndTime(),
                                loginJson.getTestData("clientEmail") + CurrentDate.GenerateCurrentDateAndTime()
                        )
                .jsonPath().get("accessToken");
        System.out.println(token);
    }


    @Test(description = "get all Books")
    @Story("GET Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description : Verify status Code equal:200")
    public void getAllBooks() {
        books.getBooks()
                .then()
                .statusCode(200)
                .log()
                .all();
    }


    @Test(description = "get Book By ID")
    @Story("GET Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description :  Verify status Code equal:200 ")
    public void getBook() {
        books.getSpecificBook(2)
                .then()
                .statusCode(200)
                .log()
                .all();
    }

    @Test(description = "create New Order")
    @Story("post Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description :  Verify status Code equal:201 & Verify that order created successfully")
    public void createOrder() {

        String clientName = loginJson.getTestData("clientName") + CurrentDate.GenerateCurrentDateAndTime();

        String Token = books.authentication(
                        clientName,
                        loginJson.getTestData("clientEmail") + CurrentDate.GenerateCurrentDateAndTime())
                .jsonPath().get("accessToken");

        books.createOrder(
                        1,
                        clientName,
                        Token)
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .body("created", equalTo(true))
                .log()
                .all();


    }

    @Test(description = "get all Orders")
    @Story("GET Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description : Verify status Code equal:200")
    public void getAllOrders() {
        String Token = books.authentication(loginJson.getTestData("clientName") + CurrentDate.GenerateCurrentDateAndTime(),
                        loginJson.getTestData("clientEmail") + CurrentDate.GenerateCurrentDateAndTime()
                )
                .jsonPath().get("accessToken");
        books.getOrders(Token)
                .then()
                .statusCode(200)
                .log()
                .all();
    }


    @Test(description = "update Order")
    @Story("put Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description :  update Order")
    public void updateOrder() {

        String clientName = loginJson.getTestData("clientName") + CurrentDate.GenerateCurrentDateAndTime();

        String Token = books.authentication(
                        clientName,
                        loginJson.getTestData("clientEmail") + CurrentDate.GenerateCurrentDateAndTime())
                .jsonPath().get("accessToken");

        String orderId = books.createOrder(
                1,
                clientName,
                Token).jsonPath().get("orderId");

        books.updateOrder(Token, orderId, updateBookJson.getTestData("customerName"))
                .then()
                .log()
                .all();

    }

    @Test(description = "delete order")
    @Story("delete Request")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test Description :  delete order & Verify status Code equal:204")
    public void deleteOrder() {
        String clientName = loginJson.getTestData("clientName") + CurrentDate.GenerateCurrentDateAndTime();

        String Token = books.authentication(
                        clientName,
                        loginJson.getTestData("clientEmail") + CurrentDate.GenerateCurrentDateAndTime())
                .jsonPath().get("accessToken");

        String orderId = books.createOrder(
                        1,
                        clientName,
                        Token)
                .jsonPath()
                .get("orderId");


        books.deleteOrder(orderId, clientName, Token)
                .then()
                .statusCode(204)
                .log()
                .all();
    }

}


