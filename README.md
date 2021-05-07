# Adzerk Android SDK

An Android SDK for the Adzerk Native and UserDB APIs

## Download

Grab via gradle

```gradle
implementation 'com.adzerk:sdk:2.1.+'
```

The SDK uses Java 8 and supports a minimum Android 5.1 (API level 22).

To target a client application with minimum SDK <26, refer to the [Android Java 8 support documentation](https://developer.android.com/studio/write/java8-support).

## Usage

Create an instance of the AdzerkSdk for your `networkId`:

```kotlin
AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).build();
```

Create an instance of the AdzerkSdk and also provide a hostname:

```kotlin
AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).hostname("custom.host.com").build();
```

Build a request for placements

```kotlin
Request request = new Request.Builder()
      .addPlacement(new Placement("div1", <site_id>, <ad_types...>))
      .addPlacement(new Placement("div2", <site_id>, <ad_types...>))
      .addPlacement(new Placement("div3", <site_id>, <ad_types...>))
      .build();
```

Submit the request and get a callback for the response

```kotlin
sdk.requestPlacement(request, listener);
```

Get content and decisions from the response

```kotlin
@Override
public void success(Response response) {
    Decision firstDecision = response.getDecisions("div1").get(0);
    // ...
}
```

## Additional Options
Additional optional parameters supported by the API may be specified via the Builder on the Request or Placement.

Example: to specify eCPM partitions for a Placement:

```kotlin
String[] ecpmPartitions = new String[]{"main", "detail", "footer"};
Request request = new Request.Builder()
    .addPlacement(new Placement("div1", 1133898L, 163)
        .setCount(ecpmPartitions.length)
        .addAdditionalOption("ecpmPartitions", ecpmPartitions))
    .build();
```

@since SDK v2.1.0+

## Distance Targeting

Distance Targeting is not supported via the Additional Options feature.

```kotlin
val request = new Request.Builder()
    .addPlacement(Placement("div1", 1133898, (163)))
    .addAdditionalOption("intendedLatitude", 35.91868)
    .addAdditionalOption("intendedLongitude", -78.96001)
    .addAdditionalOption("radius", 50)
    .build()
```

## Multi-Winner Placements
A multi-winner placement returns multiple selections inside a single placement object.

To request multiple winners, specify a count for the Placement object. This requests the maximum number of winners (selections) that can be included in the placement.

```kotlin
Request request = new Request.Builder()
   .addPlacement(new Placement("div1", <site_id>, <ad_types...>).setCount(3))
   .build();
```

@since SDK v2.0.0+

## GDPR Consent

Consent preferences can be specified when building a request. For example, to set GDPR consent for tracking in the European Union (this defaults to false):

```kotlin
Request request = new Request.Builder(placements)
    .setUser(new User(key))
    .setConsent(new Consent(true))
    .build();
```

@since SDK v0.4.0+

## Building
Use gradlew to build library archive

```
./gradlew assemble
```
output: `sdk/build/outputs/aar`

## Testing
Run unit tests

```
./gradlew test
```
Specify `--rerun-tasks` to rerun up-to-date tests. To view test reports see:

output: `sdk/build/reports/tests`

## Documentation
Generate the SDK documentation

```
./gradlew javadoc
```

output: `sdk/build/docs/javadoc`

## Publishing
Update the version string properties in `sdk/build.gradle`

Either get a copy of the Kevel Engineering signing key or generate a new signing key using the instructions here:

https://blog.sonatype.com/2010/01/how-to-generate-pgp-signatures-with-maven/

Update `~/.gradle/gradle.properties` with your signing information.

```
./gradlew uploadArchive
```

Login in to Sonatype OSS, locate the staging package, close, and release it.
