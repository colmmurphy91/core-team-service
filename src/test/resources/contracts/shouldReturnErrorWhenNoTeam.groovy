package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description("should return one team")

    request{
        method( GET())
        url("/team/noTeam")
    }

    response{
        status(404)
    }
}