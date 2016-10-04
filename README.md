# Popular Movies

## The Movie DB API Key
This application uses data from The Movie DB API. Every API call requires a unique API key, which is taken out for legal reasons. To have the application run properly, you will need to create an account at https://www.themoviedb.org/account/signup and request a free API key for non-commercial use. 

There are multiple ways to set your API key within the application. Here are 2 ways:

### Method 1: Edit `build.gradle` directly
Within the build.gradle file, simply replace the `MovieDbApiKey` variable with your API key string. For example, if your API key is `asdf1234`:

```java
buildTypes.each {
        it.buildConfigField 'String', 'MOVIE_DB_API_KEY', MovieDbApiKey
}
```
should be changed to 

```java
buildTypes.each {
        it.buildConfigField 'String', 'MOVIE_DB_API_KEY', '"asdf1234"'
}
```

### Method 2: Edit `[USER_HOME]/.gradle/gradle.properties`
Add the following line to `[USER_HOME]/.gradle/gradle.properties`

    MovieDbApiKey="asdf1234"






