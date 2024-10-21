This app, in early but fully functional prototype stage, provides personalized health advice on sun exposure, promoting safe habits regarding UV exposure. It is both an educational tool and a practical resource.

## Key Features
- **Kotlin Multiplatform**: the core logic is shared across platforms using Kotlin Multiplatform. Most of the business logic, including message generation, is in the shared module, while platform-specific UIs are built natively for Android and iOS.
  
- **Cross-Platform Efficiency**: this app demonstrates the power of Kotlin Multiplatform in building shared logic, making it an excellent resource for developers interested in cross-platform development.

- **Tech stack & architecture**
   - Kotlin multiplatform
   - Ktor for network calls to OpenMeteo and OpenUV APIs (alternative)
   - Jetpack Compose (Droid)
   - Coroutines (Droid)
   - GP Location Services (Droid)
   - SwiftUI (iOS)
   - MVVM (Droid + iOS)

## Project Status
This project was a key component of an ongoing startup initiative. However, given the fact that the project will definitely change, development up to this point has been made open-source (and in fact its github history has been entirely deleted). This was done with a clear intent to:
- Promote learning on the sun exposure issue
- Promote learning on Kotlin Multiplatform development, on mobile app cross-platform and, in general, on best practices regarding the tech stack used here.

### Lacking/needs to have
- **Some functionalities are currently disabled and will require you to use your own api keys**
  - Reverse geocoding (telling what part of the world you're in, starting from latlon) is disabled. Won't break the app entirely but won't work either. You'll find a placeholder in GeocodingApiConfig.kt but you can make it work by simply getting your own api key in https://api.bigdatacloud.net/data/reverse-geocode.
  - Same applies to fetching alternative sun data with OpenUV. Even in this case, it won't break the app entirely but won't allow you to use that data as an alternative source (less important than previous one, probably). You can grab your own api key on https://www.openuv.io/ and put it in CommonStrings_System.kt
  - **GP services won't work if you don't add your own google-services.json, from your Firebase Project**. This is going to be a breaking problem if unaddressed.

### Would like to have/Potential future development
- **UI refactoring**: some parts of the user interface may need polishing to ensure a consistent and seamless experience across platforms.
- **Resource sharing**: there are opportunities for optimization in how resources (e.g., assets) are shared between Android and iOS. The current solution, based on static resources in the common module, is just a prototype and probably needs to be changed in a final/live product.
- **Better calculation of sun exposure values**: at the moment the exact UV index (in addition to temperature and air quality) are based on a simple time-based interpolation on hourly values from meteo sources, but I'm sure something more precise and relevant can be done here.

### My coding philosophy
I am a pragmatic dev. This doesn't mean that I like to write bad code for the sake of releasing early. Doesn't also mean that I like to neglect things such as performance, modularity, best practices and good code structure. But honestly: I still think that releasing what you have despite imperfect is something you've got to deal with. Too often, software engineers fall in love with the idea of perfection—clean, beautifully styled code that ticks every checkbox of "best practices." But the reality is, coding is part of a business. The goal isn't just to write pretty code for the sake of it; it's to create something functional, valuable, and delivered on time. Spending hours perfecting the indentation or over-engineering a solution that could be simpler and just as effective is not pragmatic. It wastes time that could be better spent solving real-world problems.

In short: this codebase probably isn't perfect, but it's engineered well enough to represent a first, strong iteration to solve the problem. Hopefully you'll appreciate it enough for that. 
