package com.cluborg.coreteamservice.model;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ModelTest {

    @Test
    public void shouldConstructTeam(){
        Team team = new Team("mongoId", "Naomh Aban");
        Assert.assertNotNull(team);

        Assert.assertEquals("mongoId", team.getId());
        Assert.assertThat(team.getTeamName(), Matchers.equalToIgnoringCase("Naomh Aban"));
    }


}
