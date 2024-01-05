package com.testcontainers.userregister.learning;

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
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.OffsetDateTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LearningApplicationTests {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    AccountProducer accountProducer;

    public static final String RABBITMQ_IMAGE = "rabbitmq:3-management";
    public static final String POSTGRES_IMAGE = "postgres:13-alpine";
    public static final String ACCOUNT_IMAGE = "test-containers-account:1.0.6-SNAPSHOT";

//    @LocalServerPort
    private Integer port = 9011;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_IMAGE);

    static RabbitMQContainer rabbit = new RabbitMQContainer(RABBITMQ_IMAGE);

    @ClassRule
    public static GenericContainer<?> account = new GenericContainer<>(DockerImageName.parse(ACCOUNT_IMAGE))
            .withEnv("SPRING_RABBITMQ_HOST=", "rabbit-mq")
            .withExposedPorts(9011, 5672)
            .withStartupTimeout(Duration.ofSeconds(3))
            .dependsOn(rabbit);

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
// amqp://localhost:32843 - rabbit::getAmqpUrl
// b1b7cb4ea14c130ae44dcdcc75c7771bba7afb9285f1ef60316f769bc8ba9c6c - rabbit::getContainerId
// /quizzical_lehmann - rabbit::getContainerName
// localhost - rabbit::getContainerIpAddress
// "InspectContainerResponse(args=[rabbitmq-server], config=ContainerConfig(attachStderr=false, attach.... - rabbit::getContainerInfo
// localhost - getHost
// http://localhost:32867 - getHttpUrl
// localhost - getNetwork
@BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        userRepository.deleteAll();
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

//        System.out.println(account.getLogs());

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
