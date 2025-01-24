# MediaTracker
MediaTracker is an Android app that fetches movie data from TheMovieDB.org, allowing you to keep track of movies you've watched or plan to watch. This app is still very much a work in progress, as more features are currently planned.

In order to run this app, you'll need to provide your own API key from TMDB and put the following in your local.properties file:

TMDB_API_KEY=_INSERT_YOUR_API_KEY_HERE_

## Architecture
MediaTracker implements the MVVM architecture with interactors (UseCases). The app uses Jetpack Compose, Coroutines and Flows, Material3, Room, Retrofit2, and Dagger Hilt.
