package user;

import client.BurgerSpec;
import client.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.hamcrest.CoreMatchers.is;

public class UserApi extends Endpoints {
    public ValidatableResponse userReg(User user) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .and()
                .body(user)
                .when()
                .post(Endpoints.CREATE_USER_API)
                .then();
    }

    public static ValidatableResponse userLogin(User user) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .and()
                .body(user)
                .when()
                .post(Endpoints.LOGIN_API)
                .then();
    }

    public ValidatableResponse deleteUser(String bearerToken) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .headers("Authorization", bearerToken)
                .delete(Endpoints.DELETE_USER_API)
                .then()
                .statusCode(SC_ACCEPTED)
                .and().body("message", is("User successfully removed"));
    }

    public ValidatableResponse updateDataUserWithAuth(UserNewData userNewData, String bearerToken) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .header("Authorization", bearerToken)
                .contentType(ContentType.JSON)
                .and()
                .body(userNewData)
                .when()
                .patch(Endpoints.PATCH_USER_API)
                .then();
    }

    public ValidatableResponse updateDataUserWithoutAuth(UserNewData userNewData) {
        return given()
                .spec(BurgerSpec.requestSpecification())
                .and()
                .body(userNewData)
                .patch(Endpoints.PATCH_USER_API)
                .then();
    }
}
