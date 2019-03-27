package com.cluborg.coreteamservice;


import com.cluborg.coreteamservice.model.Team;
import com.cluborg.coreteamservice.service.TeamService;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
properties = "server.port= 0")
@RunWith(SpringRunner.class)
@Import(CoreTeamServiceApplication.class)
@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
public abstract class BaseTest {

    static {
        System.setProperty("eureka.client.enabled", "false");
        System.setProperty("spring.cloud.config.failFast", "false");
    }


    @LocalServerPort
    private int port;

    @MockBean
    private TeamService teamService;

    @Before
    public void before(){

        RestAssured.baseURI = "http://localhost:" + this.port;

        Mockito.when(teamService.findById("id"))
                .thenReturn(Mono.just(new Team("id", "Cork")));

        Mockito.when(teamService.findById("teamWithNoPlayers"))
                .thenReturn(Mono.just(new Team("teamWithNoPlayers", "Kerry")));

        Mockito.when(teamService.findById("noTeam"))
                .thenReturn(Mono.empty());

        Mockito.doAnswer(
                invocationOnMock -> Mono.empty()
        ).when(teamService).deleteById("id");

        Mockito.doAnswer(
                invocationOnMock -> Mono.empty()
        ).when(teamService).deleteById("teamWithNoPlayers");
    }
}
