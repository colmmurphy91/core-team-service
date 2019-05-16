package com.cluborg.coreteamservice.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TeamRestConfiguration {

    @Bean
    RouterFunction<ServerResponse> routes(TeamHandler teamHandler) {
        return route(GET("/teams"), teamHandler::all)
                .andRoute(POST("/team"), teamHandler::create)
                .andRoute(GET("/team/{id}"), teamHandler::getOneTeam)
                .andRoute(PUT("/team/{id}"), teamHandler::updateTeam)
                .andRoute(DELETE("/team/{id}"), teamHandler::deleteTeamById);
    }


}
