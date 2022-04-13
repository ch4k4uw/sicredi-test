# Instacredi
- [Features](#features)
- [Architecture](#architecture)
- [Module arrangement](#module-arrangement)
- [Package pattern](#package-pattern)
- [Technologies](#technologies)
- [Remarks](#remarks)
## Features
* User sign-in and sign-up
    * With safe password storing through SHA512 pass hashing implementation
* Feed of events
* Event check-in
* Event sharing

## Architecture
This app implements the `MVVM` design pattern for the presenter module (`app`) and its macro architecture is based on the `separation of concerns` (view, view-model, domain and infrastructure).
To implement it the view implements the user interactions and screen flows through Android view patterns mirroring the features implemented in the presenter module, which implements `use-cases` pattern to deal with the domain architecture, which is based on `query and command`. That `query and command` based architecture is implemented using the `repository`, `services`, and `anemic entities` patterns.</br> 
To make all of this work as required, to manage and set up all of the dependency injection, all modules implement the `inversion of control` pattern.

## Module arrangement
1. app
    * `View` - Implements the user interactions and the screen flows.
2. presenter
    * `View-Model` - Implements every view-models and the features use cases.
3. domain
    * `Domain` - Implements the business logic required for each feature as well as the required infrastructure required to deal with each business logic requirement.
4. core
    * `Infrastructure` - High, middle, and low-level required features to implement the app business and presentation features.

## Package pattern
1. app
    * `Feature` - App feature
        * `adapter` - Required objects to implements the `adapter pattern` implementation.
        * `extensions` - `Extensions methods patterns` implementations
2. presenter
    * `Feature` - App feature
        * `interaction` - Required data structures to deal with `domain` queries and user interactions.
        * `uc` - Use cases implementations.
        * `ioc` - Inversion of control. Required modules to manage the `dependency injection pattern`.
   
3. domain
    * `Domain feature` - Business requirement implementation. Its relation with the `app feature` *isn't* 1:1, but n:m since the `domain feature` works over a `query and command` architecture. Then for N `app features`, can exist 0 or M `domain features`.
        * `domain` - Domain implementation and domain infrastructure requirements. It implements required data structure, patterns and domain contracts to deal with the required business logic
            * `data` - DDD's `data` object.
            * `entity` - DDD's `entity` object (anemic).
            * `repository` - `Repository pattern` domain contracts.
        * `infra` - Domain requirements implementations. Implements every low level logic required to implement the domain business logic.
            * `ioc` - Same as the `app.feature.ioc`.
            * `repository` - Domain repository requirements implementations.
            *  `…` - Several packages, private to the infrastructure, to deal with the domain requirements.
4. core
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

## Remarks
Click [here](bin/app-release.apk) to download the generated bin file. 