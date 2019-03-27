package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request{
        method( GET())
        url("/team/teamWithNoPlayers")
    }

    response{
        status(200)
        headers {
            contentType(applicationJsonUtf8())
        }
        body("""
        {
            "id" : "teamWithNoPlayers", 
            "teamName" : "Kerry"
        }
        """)
    }

}