# Adzerk Android SDK

An Android SDK for the Adzerk Native and UserDB APIs

## Download

Grab via gradle

    implementation 'com.adzerk:sdk:1.0.+'


The SDK requires a minimum Android 8.0 (API level 26) and Java 8

## Usage

Create an instance of the AdzerkSdk for your `networkId`:

    AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).build();

Create an instance of the AdzerkSdk and also provide a hostname:

    AdzerkSdk sdk = new AdzerkSdk.Builder().networkId(23L).hostname("custom.host.com").build();

Build a request for placements

    Request request = new Request.Builder()
          .addPlacement(new Placement("div1", <site_id>, <ad_types...>))
          .addPlacement(new Placement("div2", <site_id>, <ad_types...>))
          .addPlacement(new Placement("div3", <site_id>, <ad_types...>))
          .build();

Submit the request and get a callback for the response

    sdk.requestPlacement(request, listener);

Get content and decisions from the response

    @Override
    public void success(Response response) {
        Decision decision = response.getDecision("div1");
        ...
    }

## GDPR Consent

Consent preferences can be specified when building a request. For example, to set GDPR consent for tracking in the European Union (this defaults to false):

    Request request = new Request.Builder(placements)
        .setUser(new User(key))
        .setConsent(new Consent(true))
        .build();

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

Update `gradle.properties` with your Bintray account information
```
./gradlew bintrayUpload
```
Login in to your Bintray account to publish the uploaded build.