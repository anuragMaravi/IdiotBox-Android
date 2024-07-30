# IdiotBox-Android

[![License](https://img.shields.io/badge/license-Custom-blue.svg)](LICENSE)

IdiotBox is an Android application that allows users to browse and explore popular movies, upcoming movies, top-rated movies, and movies currently playing in theaters. The app fetches data from the TMDB (The Movie Database) API and displays movie details, trailers, and casting information.

## Features

- Browse popular, upcoming, top-rated, and now-playing movies
- View detailed information about movies
- Watch movie trailers
- View casting information for movies
- Search for movies

## Screenshots

| Home Screen | Movie Details                              | Movie Details                              | Actor Details                           |
| ----------- |--------------------------------------------|--------------------------------------------|-----------------------------------------|
| ![Home Screen](screenshots/home.png) | ![Movie Details](screenshots/detail_1.png) | ![Movie Details](screenshots/detail_2.png) | ![Actor Details](screenshots/actor.png) |

## Prerequisites

- Android Studio
- An API key from [The Movie Database (TMDB)](https://www.themoviedb.org/)

## Getting Started

### Setup

1. **Clone the repository:**
   ```bash
   git clone https://github.com/anuragmaravi/idiotbox.git
   cd idiotbox
   ```
2. **Open the project in Android Studio.**

3. **Create a file named `apikey.properties` in the root directory of the project and add your TMDB API key:**

   ```plaintext
   TMDB_API_KEY=your_api_key_here
   ```
4. **Build and run the project:**
    - Open Android Studio.
    - Click on `File > Open` and select the cloned project.
    - Click on `Build > Make Project` to build the project.
    - Run the app on an emulator or a physical device.

## Dependencies

- [AndroidX](https://developer.android.com/jetpack/androidx)
- [Volley](https://github.com/google/volley)
- [Glide](https://github.com/bumptech/glide)

## License

This project is licensed under the terms described in the [LICENSE](LICENSE) file. Permission is granted for educational purposes only. Commercial use is prohibited without explicit permission from the author. Any use of this software must include attribution to the original author(s).
