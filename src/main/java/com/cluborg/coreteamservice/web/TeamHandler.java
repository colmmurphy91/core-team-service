package com.cluborg.coreteamservice.web;

import com.cluborg.coreteamservice.model.Team;
import com.cluborg.coreteamservice.service.TeamService;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class TeamHandler {

    private final TeamService teamService;

    public TeamHandler(TeamService teamService) {
        this.teamService = teamService;
    }

    public Mono<ServerResponse> all(ServerRequest serverRequest){
        return defaultReadManyResponse(teamService.getAll());

    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Flux flux = serverRequest.bodyToFlux(Team.class)
                .flatMap(toWrite -> teamService.create(toWrite.getTeamName()));
        return defaultWriteResponse(flux);
    }

    public Mono<ServerResponse> getOneTeam(ServerRequest serverRequest) {
        return defaultReadResponse(teamService.findById(serverRequest.pathVariable("id")));
    }

    public Mono<ServerResponse> deleteTeamById(ServerRequest serverRequest){
        return teamService
                .deleteById(serverRequest.pathVariable("id"))
                .then(ServerResponse
                        .noContent()
                        .build())
                .onErrorResume(t -> ServerResponse.badRequest().build());
    }

    private Mono<ServerResponse> defaultReadResponse(Publisher<Team> team) {
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return Mono
                .from(team)
                .flatMap(t -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(t))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> updateTeam(ServerRequest serverRequest){
        Flux flux = serverRequest.bodyToFlux(Team.class)
                .flatMap(toUpdate -> teamService.update(serverRequest.pathVariable("id"), toUpdate.getTeamName()));
        return defaultUpdateResponse(flux);
    }

    private static Mono<ServerResponse> defaultReadManyResponse(Publisher<Team> team) {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(team, Team.class);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Team> profiles) {
        return Mono
                .from(profiles)
                .flatMap(p -> ServerResponse
                        .created(URI.create("/team/" + p.getId()))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build()
                );
    }

    private static Mono<ServerResponse> defaultUpdateResponse(Publisher<Team> profiles) {
        return Mono
                .from(profiles)
                .flatMap(p -> ServerResponse
                        .accepted()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .build()
                );
    }
}
