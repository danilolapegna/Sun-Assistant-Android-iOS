This app provides personalized health advice on sun exposure, promoting safe habits regarding UV exposure. It is both an educational tool and a practical resource.

## Key Features
- **Kotlin Multiplatform**: the core logic is shared across platforms using Kotlin Multiplatform. Most of the business logic, including message generation, is in the shared module, while platform-specific UIs are built natively for Android and iOS.
  
- **Cross-Platform Efficiency**: this app demonstrates the power of Kotlin Multiplatform in building shared logic, making it an excellent resource for developers interested in cross-platform development.

## Project Status
This project was a key component of an ongoing startup initiative. However, given the fact that the project will definitely change, development up to this point has been made open-source to promote public utility (its github history has also been deleted to avoid exposing previously used keys and similar). Alsoo, it can be useful for learning, and community contributions. Last but not least, I decided to make available for anyone interested in learning Kotlin Multiplatform or contributing to its development.

### Needs to have
- **Some functionalities are currently disabled and require you to use your own api keys**
  - Reverse geocoding (telling what part of the world you're in, starting from latlon) is disabled. You'll find a placeholder in GeocodingApiConfig.kt but you can make it work by simply getting your own api key in https://api.bigdatacloud.net/data/reverse-geocode.
  - Same applies to fetching alternative sun data with OpenUV. You can grab your own api key on https://www.openuv.io/ and put it in CommonStrings_System.kt
  - **GP services won't work if you don't add your own google-services.json, from your Firebase Project**

### Would like to have/Potential future development
- **UI refactoring**: some parts of the user interface may need polishing to ensure a consistent and seamless experience across platforms.
- **Resource sharing**: there are opportunities for optimization in how resources (e.g., assets) are shared between Android and iOS.
- **Androic iconset**: android icon set is not set and defaulted to a common placeholder.
- **Better retrieving of sun values**: at the moment the exact UV index is based on a simple time-interpolation value, fetched from meteo sources, but I'm sure something more precise and relevant can be done here.
