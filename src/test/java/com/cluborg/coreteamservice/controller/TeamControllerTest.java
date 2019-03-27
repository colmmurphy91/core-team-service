package com.cluborg.coreteamservice.controller;

import com.cluborg.coreteamservice.model.Team;
import com.cluborg.coreteamservice.service.TeamService;
import com.cluborg.coreteamservice.web.TeamHandler;
import com.cluborg.coreteamservice.web.TeamRestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest
@Import({TeamRestConfiguration.class, TeamHandler.class})
public class TeamControllerTest {

    @MockBean
    private TeamService teamService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void shouldGetAllTeams() {

        Mockito.when(teamService.all())
                .thenReturn(Flux.just(new Team(null, "Cork"), new Team(null, "Kerry")));

        this.webTestClient
                .get()
                .uri("/all")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().jsonPath("@.[0].teamName").isEqualTo("Cork")
                .jsonPath("@.[1].teamName").isEqualTo("Kerry");
    }

    @Test
    public void shouldSaveATeam() {

        Mockito.when(teamService.create("Naomh Aban"))
                .thenReturn(Mono.just(new Team("id", "Cork")));

        this.webTestClient
                .post()
                .uri("/team")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new Team(null, "Naomh Aban")), Team.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void shouldGetOneTeam() {

        Mockito.when(teamService.findById("id"))
                .thenReturn(Mono.just(new Team("id", "Cork")));

        this.webTestClient
                .get()
                .uri("/team/id")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody().jsonPath("@.teamName").isEqualTo("Cork");
    }

    @Test
    public void shouldGet404() {

        Mockito.when(teamService.findById("id"))
                .thenReturn(Mono.empty());

        this.webTestClient
                .get()
                .uri("/team/id")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void shouldUpdateATeam(){
        Mockito.when(teamService.update("id", "Kerry"))
                .thenReturn(Mono.just(new Team("id", "kerry")));

        this.webTestClient
                .put()
                .uri("/team/id")
                .body(Mono.just(new Team("Kerry")), Team.class)
                .exchange()
                .expectStatus()
                .isAccepted();
    }

    @Test
    public void shouldDeleteATeam(){
        Mockito.doAnswer(
                invocationOnMock -> Mono.empty()
        ).when(teamService).deleteById("id");

        this.webTestClient
                .delete()
                .uri("/team/id")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void shouldGetAnErrorDeleteATeam(){
        Mockito.doAnswer(
                invocationOnMock -> Mono.error(new RuntimeException())
        ).when(teamService).deleteById("id");

        this.webTestClient
                .delete()
                .uri("/team/id")
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
