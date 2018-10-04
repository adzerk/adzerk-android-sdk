# Adzerk Android SDK

An Android SDK and sample app for the Adzerk Native and UserDB APIs

## Download

Grab via gradle

    implementation 'com.adzerk:sdk:0.4.+'


The SDK requires at minimum Java 7 or Android 2.3.

## Usage

Obtain an instance of the AdzerkSdk class

    AdzerkSdk sdk = AdzerkSdk.getInstance();

Build a request for placements

    Request request = new Request.Builder()
          .addPlacement(new Placement("div1", <network_id>, <site_id>, <ad_types...>))
          .addPlacement(new Placement("div2", <network_id>, <site_id>, <ad_types...>))
          .addPlacement(new Placement("div3", <network_id>, <site_id>, <ad_types...>))
          .build();

Submit the request and get a callback for the response

    sdk.request(request, listener);

Get content and decisions from the response

    @Override
    public void success(Response response) {
        Decision decision = response.getDecision("div1");
        ...
    }

See the sample app for detailed examples

## GDPR Consent

Consent preferences can be specified when building a request. For example, to set GDPR consent for tracking in the European Union (this defaults to false):

    Request request = new Request.Builder(placements)
        .setUser(new User(key))
        .setConsent(new Consent(true))
        .build();

@since SDK v0.4.0+

## License

Copyright 2017 Adzerk, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
