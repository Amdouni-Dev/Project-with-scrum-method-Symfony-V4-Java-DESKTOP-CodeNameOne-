package edu.esprit.tests.factory;

import com.github.javafaker.Faker;
import edu.esprit.entities.User;

public class UserFactory {
    public static User createUser(){
        Faker faker = new Faker();
        User user = new User();
        user.setFirstName(faker.name().firstName());
        user.setLastName(faker.name().lastName());
        user.setEmail(faker.internet().emailAddress());
        user.setAbout(faker.lorem().paragraph());
        user.setPlainPassword("12345");
        user.setIsVerified(true);
        return user;
    }
}
