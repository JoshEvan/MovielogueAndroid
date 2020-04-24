# MovielogueAndroid
Android movie and tv show data update app with ViewModel Architecture Component, SQLite, Content Provider, AsyncHttpClient (LoopJ), reminder, notification, widget.


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
* ViewModel Architecture Component
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


movie list          |favorite movie list           |search movies        | detail
:-------------------------|:-------------------------|:-------------------------|:-------------------------
<img align="left" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/movies.png">  |<img align="center" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/favmovies.png">  |<img align="center" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/searchmovie.png">  |<img align="center" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/detailpage.png">

favorite widget         |daily reminder          |release today reminder      | dual language
:-------------------------|:-------------------------|:-------------------------|:-------------------------
<img align="left" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/widget.png">  |<img align="center" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/dailyreminder.png">  |<img align="center" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/releasereminder.png">  |<img align="center" height="350" src="https://raw.githubusercontent.com/JoshEvan/MovielogueAndroid/master/demo/setting.png">
