package ru.yandex.praktikum.Users;

import org.apache.commons.lang3.RandomStringUtils;

public class User {

    public String email;
    public String password;
    public String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public User(){}

    public static User getAllRandom() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, name);
    }

    public static User getRandomNoEmail() {
        String password = RandomStringUtils.randomAlphabetic(10);
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(null, password, name);
    }

    public static User getRandomNoPassword() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
        String name = RandomStringUtils.randomAlphabetic(10);
        return new User(email, null, name);
    }

    public static User getRandomNoName() {
        String email = RandomStringUtils.randomAlphabetic(10) + "@test.ru";
        String password = RandomStringUtils.randomAlphabetic(10);
        return new User(email, password, null);
    }

    @Override
    public String toString() {
        return "Courier {" + "email:" + email + "," + "password:" + password + "," + "name:" + name + "}";
    }

}
