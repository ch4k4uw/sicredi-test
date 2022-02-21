# Instacredi, a SICRED test
## Features
* User sign-in and sign-up
    * With safe password storing through SHA512 pass hashing implementation
* Feed of events
* Event check-in
* Event sharing

## Architecture
This app implements the `MVVM` design pattern for the presenter module (`app`) and its macro architecture is based on the `separation of concerns` (presenter, domain, infrastructure).
To achieve it the presenter implements `use-cases` pattern to deal with the domain architecture, which is based on `query and command`. That `query and command` based architecture is implemented using the `repository`, `services`, and `anemic entities` patterns. And to make all of this work as required, to manage and set up all of the dependency injection, all modules implement the `inversion of control` pattern.

## Module arrangement
1. app
    * `Presentation` - Implements every presentation patterns and feature use cases. 
2. domain
    * `Domain` - Implements the business logic required for each feature as well as the required infrastructure required to deal with each business logic requirement.
3. core
    * `Infrastructure` - Hight, middle, and low-level required features to implement the app business and presentation features.

## Package pattern
1. app
    * `Feature` - App feature
        * `interaction` - Required data structures to deal with `domain` queries and user interactions.
        * `uc` - Use cases implementations. 
        * `ioc` - Inversion of control. Required modules to manage the `dependency injection pattern`.
        * `adapter` - Required objects to implements the `adapter pattern` implementation.
        * `extensions` - `Extensions methods patterns` implementations
2. domain
    * `Domain feature` - Business requirement implementation. Its relation with the `app feature` *isn't* 1:1, but n:m since the `domain feature` works over a `query and command` architecture. Then for N `app features`, can exist 0 or M `domain features`.
        * `domain` - Domain implementation and domain infrastructure requirements. It implements required data structure, patterns and domain contracts to deal with the required business logic
            * `data` - DDD's `data` object.
            * `entity` - DDD's `entity` object (anemic).
            * `repository` - `Repository pattern` domain contracts.
        * `infra` - Domain requirements implementations. Implements every low level logic required to implement the domain business logic.
            * `ioc` - Same as the `app.feature.ioc`.
            * `repository` - Domain repository requirements implementations.
            *  `…` - Several packages, private to the infrastructure, to deal with the domain requirements.
3. core
    * `Feature` - General app required feature.
    
## Technologies
1. `Retrofit` - Http requests (`core` module).
2. `Dagger-Hilt` - Dependency injection management (`core`, `domain` and `app` modules).
3. `DataStore` - AndroidX data store to mock the user auth features (`core` and `domain` modules).
4. `Timber` - App loggin helper (`core`, `domain` and `app` modules).
5. `Glide` - Async image download (`core`).
6. `Material design` - App design identity (`core` and `app` modules).
7. `Kotlin-Coroutines` - Every async operation (`core`, `domain` and `app` modules).
8. `Kotlin parcelize` - Android parcelable objects implementation compile helper (`core` and `app` modules).
9. `Safe navigation args` - AndroidX navigation arguments helper (`app` module).
10. `AndroidX navigation` - Android fragment navigation helper (`app` module).
