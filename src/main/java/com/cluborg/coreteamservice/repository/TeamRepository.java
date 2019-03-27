package com.cluborg.coreteamservice.repository;

import com.cluborg.coreteamservice.model.Team;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface TeamRepository extends ReactiveMongoRepository<Team, String> {
    Mono<Team> findByTeamName(String teamNaome);
}
