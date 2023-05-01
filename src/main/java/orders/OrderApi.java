package orders;

import client.BurgerSpec;
import client.Endpoints;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class OrderApi extends Endpoints {
    public static ValidatableResponse createOrder(Order order, String bearerToken) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .headers("Authorization", bearerToken)
                .body(order)
                .post(Endpoints.CREATE_ORDER_API)
                .then();
    }
    public static ValidatableResponse createOrder1(Order order) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .body(order)
                .post(Endpoints.CREATE_ORDER_API)
                .then();
    }


    public static ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .body(order)
                .post(Endpoints.CREATE_ORDER_API)
                .then();
    }

    public ValidatableResponse getUserOrdersWithAuth(String bearerToken) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .headers("Authorization", bearerToken)
                .get(Endpoints.USER_ORDERS_API)
                .then();
    }

    public ValidatableResponse getUserOrdersWithoutAuth() {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .get(Endpoints.USER_ORDERS_API)
                .then();
    }

    public static ValidatableResponse getAllIngredients() {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .get(Endpoints.INGREDIENT_API)
                .then();
    }
}