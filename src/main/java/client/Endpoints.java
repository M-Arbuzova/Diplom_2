package client;

public class Endpoints {
    public static final String CREATE_USER_API = "api/auth/register";
    public static final String LOGIN_API = "api/auth/login";
    public static final String DELETE_USER_API = "api/auth/user";
    public static final String PATCH_USER_API = "api/auth/user"; //оставила так, как по-другому постоянно падает тест
    public static final String CREATE_ORDER_API = "api/orders";
    public static final String ORDERS_ALL_API = "api/orders/all";
    public static final String INGREDIENT_API = "api/ingredients/";

}
