package com.cluborg.coreteamservice.service;

import com.cluborg.coreteamservice.model.Team;
import com.cluborg.coreteamservice.repository.TeamRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    public Flux<Team> all() {
        return teamRepository
                .findAll();
    }

    public Mono<Team> create(String teamName) {
        return teamRepository
                .save(new Team(null, teamName));
    }

    public Mono<Team> update(String id, String teamName) {
        return teamRepository.findById(id)
                .map(t -> new Team(t.getId(), teamName))
                .flatMap(this.teamRepository::save);
    }

    public Mono<Team> findById(String id) {
        return teamRepository
                .findById(id);
    }

    public Mono<Void> deleteById(String teamId) {
        return teamRepository.findById(teamId)
                .flatMap(teamRepository::delete)
                .onErrorResume(e -> Mono.error(new RuntimeException("Team Does Not Exist")));
    }
}
