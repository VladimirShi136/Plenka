package com.app.plenka.data.repository

import com.app.plenka.domain.model.FreeMovie
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FreeMovieRepository @Inject constructor() {
    fun getFreeMovies(): List<FreeMovie> {
        return listOf(
            FreeMovie(
                id = "big_buck_bunny",
                title = "Big Buck Bunny",
                description = "Большой кролик с большим сердцем. Когда три грызуна начинают издеваться над беззащитными лесными обитателями, кролик объявляет им войну.",
                posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_Buck_Bunny_Poster_1080p.jpg/800px-Big_Buck_Bunny_Poster_1080p.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                license = "Creative Commons Attribution 3.0",
                author = "Blender Foundation"
            ),
            FreeMovie(
                id = "sintel",
                title = "Sintel",
                description = "История одинокой девушки по имени Синтел, которая отправляется на поиски маленького дракончика, ставшего её верным другом.",
                posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8f/Sintel_poster.jpg/800px-Sintel_poster.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4",
                license = "Creative Commons Attribution 3.0",
                author = "Blender Foundation"
            ),
            FreeMovie(
                id = "tears_of_steel",
                title = "Tears of Steel",
                description = "В мрачном будущем группа воинов и ученых собирается в старом амстердамском баре, чтобы попытаться спасти человечество от роботов.",
                posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Tears_of_Steel_poster.jpg/800px-Tears_of_Steel_poster.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                license = "Creative Commons Attribution 3.0",
                author = "Blender Foundation"
            ),
            FreeMovie(
                id = "elephants_dream",
                title = "Elephants Dream",
                description = "Два персонажа путешествуют по сюрреалистичному механическому миру, созданному их собственным воображением.",
                posterUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/Elephants_Dream_poster.jpg/800px-Elephants_Dream_poster.jpg",
                videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                license = "Creative Commons Attribution 3.0",
                author = "Blender Foundation"
            )
        )
    }
}
