package user;

import client.BurgerSpec;
import client.Endpoints;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

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
    public ValidatableResponse userLogin(User user){
        return given()
                .spec(BurgerSpec.requestSpecification())
                .and()
                .body(user)
                .when()
                .post(Endpoints.LOGIN_API)
                .then();
    }
    public ValidatableResponse deleteUser(String bearerToken){
        return given()
                .spec(BurgerSpec.requestSpecification())
                .headers("Authorization", bearerToken)
                .delete(Endpoints.DELETE_USER_API)
                .then();
    }

    public ValidatableResponse updateDataUserWithAuth(User user, String bearerToken){
        return given()
                .spec(BurgerSpec.requestSpecification())
                .header("Authorization", bearerToken)
                .body(user)
                .and()
                .patch(Endpoints.USER_PATH_API + "user")
                .then();
    }
    public ValidatableResponse updateDataUserWithoutAuth(String bearerToken){
        return given()
                .spec(BurgerSpec.requestSpecification())
                .auth().oauth2(bearerToken)
                .and()
                .patch(Endpoints.USER_PATH_API + "user")
                .then();
    }
}
