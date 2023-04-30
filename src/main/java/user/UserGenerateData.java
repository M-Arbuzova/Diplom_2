package user;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerateData extends User{
    public User getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(9) + "@bk.ru";
        String password = RandomStringUtils.randomAlphabetic(9);
        String name = RandomStringUtils.randomAlphabetic(9);
        return new User(email, password, name);
    }
    public User getGenericUser(){
        return new User("ma.arbuzova@mail.ru", "12345678", "Мария");
    }
}
