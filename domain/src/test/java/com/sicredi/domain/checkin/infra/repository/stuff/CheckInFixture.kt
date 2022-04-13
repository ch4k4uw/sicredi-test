package com.sicredi.domain.checkin.infra.repository.stuff

import com.sicredi.domain.checkin.infra.remote.model.CheckInRequest
import com.sicredi.domain.common.stuff.CommonFixture

object CheckInFixture {
    val CheckInRequest by lazy {
        CheckInRequest(
            eventId = "xxx",
            name = CommonFixture.User.name,
            email = CommonFixture.User.email
        )
    }
}