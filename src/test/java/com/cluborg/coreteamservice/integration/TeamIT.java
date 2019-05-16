package com.cluborg.coreteamservice.integration;

import com.cluborg.coreteamservice.CoreTeamServiceApplication;
import com.cluborg.coreteamservice.model.Team;
import com.cluborg.coreteamservice.repository.TeamRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes= CoreTeamServiceApplication.class,
        webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TeamIT {

    @LocalServerPort
    int port;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void test1_shouldGetAStatusIsCreated() {
        WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build()
                .post()
                .uri("/team")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new Team(null, "Naomh Aban")), Team.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void test3_shouldGetOneTeam(){
        Mono<Team> team = teamRepository.save(new Team(null, "Cork"));
        String id = team.block().getId();
        WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build()
                .get()
                .uri("team/"+ id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("@.teamName")
                .isEqualTo("Cork");
    }

    @Test
    public void test4_shouldGetUpdateOneTeam(){
        Mono<Team> team = teamRepository.save(new Team(null, "Kery"));
        String id = team.block().getId();
        WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build()
                .put()
                .uri("team/"+ id)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(new Team( "Kerry")), Team.class)
                .exchange()
                .expectStatus()
                .isAccepted();
    }


    @Test
    public void test5_shouldGetAllTeams(){
        WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build()
                .get()
                .uri("/teams")
                .exchange()
        .expectStatus()
        .isOk();
    }



}
