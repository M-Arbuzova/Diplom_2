package orders;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserApi;
import user.UserGenerateData;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    User user;
    Order order;
    UserApi userApi;
    OrderApi orderApi;
    private String bearerToken;

    @Before
    public void setUp() {
        userApi = new UserApi();
        user = new UserGenerateData().getRandomUser();
        orderApi = new OrderApi();
        order = new Order();
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userApi.deleteUser(bearerToken);
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Проверка, что можно создать заказ, если пользователь авторизован")
    public void createOrderTest() {
        fillIngredientList();
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        UserApi.userLogin(user);
        ValidatableResponse responseCreateOrder = OrderApi.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(SC_OK).body("success", is(true));
    }

    //следующий тест баг? на сайте нет возможности создать заказ без логина, да и тут если получится создать заказ
//    то за кем он закрепляется?
    @Test
    @DisplayName("Создание заказа неавторизованным пользователем")
    @Description("Проверка, что можно создать заказ, если пользователь не авторизован")
    public void createOrderWithoutAuthTest() {
        fillIngredientList();
        ValidatableResponse responseCreateOrder = OrderApi.createOrderWithoutAuth(order);
        responseCreateOrder.assertThat().statusCode(SC_OK).body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без добавления ингредиентов авторизованным пользователем")
    @Description("Проверка, что нельзя создать заказ, если не добавить ингредиенты")
    public void createOrderWithoutIngredientTest() {
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderApi.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(SC_BAD_REQUEST).body("success", is(false))
                .and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа c неверным хешем ингредиентов авторизованным пользователем")
    @Description("Проверка, что нельзя создать заказ, если указать неверный хеш ингредиентов")
    public void createOrderWithWrongHashIngredientTest() {
        fillWrongHashIngredientList();
        ValidatableResponse responseReg = userApi.userReg(user);
        bearerToken = responseReg.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = OrderApi.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }


    private void fillIngredientList() {
        ValidatableResponse validatableResponse = OrderApi.getAllIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(4));
        ingredients.add(list.get(2));
        ingredients.add(list.get(0));
    }

    private void fillWrongHashIngredientList() {
        ValidatableResponse validatableResponse = OrderApi.getAllIngredients();
        List<String> list = validatableResponse.extract().path("data._id");
        List<String> ingredients = order.getIngredients();
        ingredients.add(list.get(0));
        ingredients.add(list.get(4).repeat(1));
        ingredients.add(list.get(2).repeat(2));
        ingredients.add(list.get(0));
    }
}
