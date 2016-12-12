# Adzerk Android SDK

An Android SDK and sample app for the Adzerk Native and UserDB APIs

## Usage

Obtain an instance of the AdzerkSdk class

    AdzerkSdk sdk = AdzerkSdk.getInstance();

Build a request for placements

    Request request = Builder.request()
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

## Download

Grab via gradle

    compile 'com.adzerk:sdk:0.2.+'

Snapshots of the development version are available in Sonatype's snapshots repository. (Coming Soon)

The SDK requires at minimum Java 7 or Android 2.3.

## License

Copyright 2016 Adzerk, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
