package com.cluborg.coreteamservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Team {

    public Team(@NotNull String teamName) {
        this.teamName = teamName;
    }

    @Id
    String id;

    @NotNull
    private String teamName;

}
