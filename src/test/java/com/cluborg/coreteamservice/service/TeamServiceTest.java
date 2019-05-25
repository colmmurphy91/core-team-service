package com.cluborg.coreteamservice.service;

import com.cluborg.coreteamservice.model.Team;
import com.cluborg.coreteamservice.repository.TeamRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

@Log4j2
@Import(TeamService.class)
@RunWith(SpringRunner.class)
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;
    @MockBean
    private TeamRepository teamRepository;

    @Test
    public void getAll() {
        Flux<Team> saved = Flux.just(new Team(null, "Colm"), new Team(null, "Oonagh"));

        Mockito.when(teamRepository.findAll()).thenReturn(saved);

        Flux<Team> composite = teamService.getAll().thenMany(saved);

        Predicate<Team> match = team -> saved.any(saveItem -> saveItem.equals(team)).block();

        StepVerifier
                .create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();

    }

    @Test
    public void save() {
        Mono <Team> actual = Mono.just(new Team("test","Naomh Aban"));
        Mockito.when(teamRepository.save(new Team(null,"Naomh Aban"))).thenReturn(actual);

        Mono<Team> teamMono = this.teamService.create("Naomh Aban");

        StepVerifier
                .create(teamMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void Update(){
        Team team = new Team("id", "test");

        Mockito.when(teamRepository.findById("id")).thenReturn(Mono.just(team));
        Mockito.when(teamRepository.save(new Team("id", "New Name"))).thenReturn(Mono.just(new Team("id", "New Name")));

        Mono<Team> teamMono = teamService.update("id", "New Name");

        StepVerifier.create(teamMono)
                .expectNextMatches(team2 -> team2.getTeamName().equals("New Name"))
                .verifyComplete();
    }

    @Test
    public void findById(){
        Team team = new Team("id", "TestTeam");

        Mockito.when(teamRepository.findById("id")).thenReturn(Mono.just(team));

        Mono<Team> actual = teamService.findById(team.getId());

        StepVerifier.create(actual)
                .expectNextMatches(team2 -> team2.getTeamName().equals("TestTeam"))
                .verifyComplete();
    }

    @Test
    public void shouldDeleteSuccessfully(){

        Team team = new Team("teamId", "Naomh Aban");

        Mockito.when(teamService.findById("teamId"))
                .thenReturn(Mono.just(team));

        Mockito.doAnswer((i) -> Mono.empty())
                .when(teamRepository).delete(team);

        Mono<Void> t = teamService.deleteById("teamId");

        StepVerifier
                .create(t)
                .expectNext()
                .verifyComplete();
    }

    @Test
    public void shouldReturnErrorWhenTeamDoesNotExist(){
        Mockito.when(teamService.findById("id"))
                .thenReturn(Mono.error(new RuntimeException("Team Does Not Exist")));

        Mono<Void> t = teamService.deleteById("id");

        Mockito.verify(teamRepository, Mockito.times(0)).deleteById("teamId");

        StepVerifier
                .create(t)
                .expectError()
                .verify();
    }

}