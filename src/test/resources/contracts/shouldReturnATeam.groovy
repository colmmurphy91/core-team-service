package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {

    description("should return one team")

    request{
        method( GET())
        url("/team/id")
    }

    response{
        status(200)
        headers {
            contentType(applicationJsonUtf8())
        }
        body("""
        {
            "id" : "id", 
            "teamName" : "Cork"
        }
        """)
    }
}