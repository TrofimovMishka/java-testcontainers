package com.testcontainers.userregister.learning;

import com.testcontainers.userregister.learning.model.Message;
import com.testcontainers.userregister.learning.model.User;
import com.testcontainers.userregister.learning.rabbit.producer.AccountProducer;
import com.testcontainers.userregister.learning.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.ClassRule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;

import java.time.OffsetDateTime;

import static com.testcontainers.userregister.learning.rabbit.config.RabbitMqConfig.CREATE_ACCOUNT_QUEUE;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DbRabitUserContainerTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AccountProducer accountProducer;

    public static final String RABBITMQ_IMAGE = "rabbitmq:3-management";
    public static final String POSTGRES_IMAGE = "postgres:13-alpine";

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE);

    static RabbitMQContainer rabbit = new RabbitMQContainer(RABBITMQ_IMAGE)
            .withExposedPorts(5672, 15672);

    @BeforeAll
    static void beforeAll() {
        rabbit.start();
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        rabbit.stop();
        postgres.stop();
    }

    @DynamicPropertySource
    static void configProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.rabbitmq.host", rabbit::getHost);
        registry.add("spring.rabbitmq.port", rabbit::getFirstMappedPort);
        registry.add("spring.rabbitmq.username", rabbit::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbit::getAdminPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
    }

    @Test
    void userShouldHaveOneAccount() {
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
                .body("id", equalTo(1));
    }

    @RabbitListener(queues = {CREATE_ACCOUNT_QUEUE})
    public void consume(Message message) {
        System.out.println("Message from RabbitMQ: " + message);
    }
}
