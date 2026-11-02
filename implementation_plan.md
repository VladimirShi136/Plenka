# План реализации Этапа 4: Трейлеры и «Смотреть бесплатно» (Media3 ExoPlayer)

Реализация раздела «Смотреть бесплатно» и просмотр трейлеров фильмов с помощью Jetpack Media3 ExoPlayer и каталога легальных видео (CC/общественное достояние или встроенные трейлеры).

## Proposed Changes

### [Dependencies & Gradle]
#### [MODIFY] [libs.versions.toml](file:///C:/Users/kalba/AndroidStudioProjects/Plenka/gradle/libs.versions.toml)
- Добавить зависимости androidx.media3 (media3-exoplayer, media3-ui).

#### [MODIFY] [app/build.gradle.kts](file:///C:/Users/kalba/AndroidStudioProjects/Plenka/app/build.gradle.kts)
- Подключить библиотеки Media3 ExoPlayer.

### [Data Layer]
#### [NEW] [FreeMovie.kt](file:///C:/Users/kalba/AndroidStudioProjects/Plenka/app/src/main/java/com/app/plenka/domain/model/FreeMovie.kt)
- Модель данных для бесплатных фильмов (id, title, description, posterUrl, videoUrl, license, author).

#### [NEW] [FreeMovieRepository.kt](file:///C:/Users/kalba/AndroidStudioProjects/Plenka/app/src/main/java/com/app/plenka/data/repository/FreeMovieRepository.kt)
- Репозиторий подборки качественных легальных фильмов (Big Buck Bunny, Sintel, Tears of Steel и др.).

### [UI Layer & Player]
#### [NEW] [VideoPlayerScreen.kt](file:///C:/Users/kalba/AndroidStudioProjects/Plenka/app/src/main/java/com/app/plenka/ui/screens/VideoPlayerScreen.kt)
- Полноэкранный плеер на базе Media3 ExoPlayer.

#### [MODIFY] [MainScreens.kt](file:///C:/Users/kalba/AndroidStudioProjects/Plenka/app/src/main/java/com/app/plenka/ui/screens/MainScreens.kt)
- Обновить `FreeMoviesScreen` для отображения списка фильмов и запуска плеера.

## Verification Plan
- Сборка проекта (`gradle_build`) и проверка работы плеера.
