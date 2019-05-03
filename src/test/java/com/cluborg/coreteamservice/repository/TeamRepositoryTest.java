package com.cluborg.coreteamservice.repository;

import com.cluborg.coreteamservice.model.Team;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@DataMongoTest
@ActiveProfiles("test")
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void shouldSaveAndFindByTeamName(){
        Team team = new Team(null, "Naomh Aban");
        Mono<Team> teamMono  = teamRepository.save(team);

        Publisher<Team> foundTeam = teamMono.then(teamRepository.findByTeamName("Naomh Aban"));

        StepVerifier.create(foundTeam)
                .expectNextMatches(team1 -> team1.getTeamName().equals("Naomh Aban"))
                .verifyComplete();

    }

}
