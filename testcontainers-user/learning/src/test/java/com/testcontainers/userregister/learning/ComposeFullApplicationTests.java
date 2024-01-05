package com.testcontainers.userregister.learning;

import com.testcontainers.userregister.learning.model.User;
import com.testcontainers.userregister.learning.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.ClassRule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.*;

import java.io.File;
import java.time.OffsetDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ComposeFullApplicationTests {

    @Autowired
    UserRepository userRepository;

    @LocalServerPort
    private static Integer port;

    public static final String RABBIT_MQ_SERVICE = "rabbit-mq";
    public static final int RABBIT_MQ_SERVICE_PORT = 5432;
    public static final String DB = "testcontainers-db";
    public static final int DB_PORT = 5672;
    public static final String ACCOUNT_SERVICE = "account-service";
    public static final int ACCOUNT_PORT = 9011;

    @ClassRule
    public static DockerComposeContainer<?> environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withExposedService(RABBIT_MQ_SERVICE, RABBIT_MQ_SERVICE_PORT)
                    .withExposedService(DB, DB_PORT)
                    .withExposedService(ACCOUNT_SERVICE, ACCOUNT_PORT);

    @BeforeAll
    static void startUp() {
        environment.start();

    }
    @BeforeEach
    void setUp() {
        Testcontainers.exposeHostPorts(port);
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @AfterAll
    static void end() {
        environment.stop();
    }


    static String getRBHost(){
        return environment.getServiceHost(RABBIT_MQ_SERVICE, RABBIT_MQ_SERVICE_PORT);
    }

    static String getDBUrl(){
        //jdbc:postgresql://localhost/testcontainers
        return String.format("jdbc:postgresql://%s:%d/testcontainers",
                environment.getServiceHost(DB, DB_PORT),
                environment.getServicePort(DB, DB_PORT));
    }

    static Integer getRBPort(){
        return environment.getServicePort(RABBIT_MQ_SERVICE, RABBIT_MQ_SERVICE_PORT);
    }

    @DynamicPropertySource
     static void configProperties(DynamicPropertyRegistry registry) {
        String rabbitHost = environment.getServiceHost(RABBIT_MQ_SERVICE, RABBIT_MQ_SERVICE_PORT);
        Integer rabbitPort = environment.getServicePort(RABBIT_MQ_SERVICE, RABBIT_MQ_SERVICE_PORT);

        String dbHost = environment.getServiceHost(DB, DB_PORT);
        Integer dbPort = environment.getServicePort(DB, DB_PORT);

        registry.add("spring.datasource.url", ComposeFullApplicationTests::getDBUrl);
//
//        registry.add("spring.rabbitmq.host", ComposeLearningApplicationTests::getRBHost);
//        registry.add("spring.rabbitmq.port", ComposeLearningApplicationTests::getRBPort);
    }



    @Test
    void userShouldHaveOneAccount() throws InterruptedException {


        User user = new User();
        user.setBirthday(OffsetDateTime.now());
        user.setName("Bob");
        user.setSurname("Green");
        user.setAccount(null);


        given()
                .contentType(ContentType.JSON)
                .when()
                .body(user)
                .post("api/user/create")
                .then()
                .statusCode(200)
                .body("name", equalTo("Bob"))
                .and()
                .body("surname", equalTo("Green"));

        Thread.sleep(2);

        User dbUser = userRepository.findAll().stream().findFirst().orElseThrow();

        assertNotNull(dbUser);
        assertEquals(1, dbUser.getId());

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/user/" + dbUser.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("Bob"))
                .and()
                .body("surname", equalTo("Green"))
                .and()
                .body("account", notNullValue());
    }

//    @Test
    void shouldGetOneUser() {
        User user = new User();
        user.setBirthday(OffsetDateTime.now());
        user.setName("Bob");
        user.setSurname("Green");
        user.setAccount(null);
        userRepository.save(user);

        User dbUser = userRepository.findAll().stream().findFirst().orElseThrow();

        assertNotNull(dbUser);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/user/" + dbUser.getId())
                .then()
                .statusCode(200)
                .body("name", equalTo("Bob"))
                .and()
                .body("surname", equalTo("Green"));

    }
}
