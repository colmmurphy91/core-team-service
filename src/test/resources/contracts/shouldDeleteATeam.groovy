package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description("should delete one team")

    request{
        method( DELETE())
        url("/team/id")
    }

    response{
        status(204)
    }
}
