# MovielogueAndroid
Android movie and tv shows data update app with MVVM Architecture, SQLite, Content Provider, AsyncHttpClient (LoopJ), reminder, notification, widget.


Features:
- searching movies and TV shows from the [TMDB](https://www.themoviedb.org/?language=en-US)
- daily reminder notification
- everyday's new released movies reminder notification
- favorite movies and TV shows local storage cart 
- dual language (english and indonesian)
- up to date datas from the TMDB API
- favorite movies and TV shows stack widget
- using Content Provider so 1 app can access another app.

Implemented topics:
* MVVM architecture
* AsyncHttpClient (LoopJ).
* glide
* fragment
* recycler view
* tab layout, action bar
* parcelable interface
* constraint layout
* localization (language)
* alarm manager
* background thread
* local storage data SQLite
* notification
* stack widget


Movielogue is an Android application to discover a list of popular movies and TV shows
Users can add and remove favorites movies and TV shows.
Never miss your favorite movie's release date with this app with every new released moview reminder and daily reminder notification.

This app is built with Java, using the [TMDB](https://www.themoviedb.org/?language=en-US) API for fetching the latest and popular movies and TV Shows.

The ConsumerApp within this project is accessing the favorite movies and TV shows data from the main app.
