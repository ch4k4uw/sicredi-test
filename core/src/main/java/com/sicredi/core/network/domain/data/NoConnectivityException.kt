package com.sicredi.core.network.domain.data

import java.io.IOException

class NoConnectivityException(message: String? = "No connectivity exception") : IOException(message)