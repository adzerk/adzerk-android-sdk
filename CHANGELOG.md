# CHANGELOG

## 2.3.0 (09/02/2022)
- update: Android SDK update for API 32 (resolves: #98)

## 2.2.1 (11/10/2021)
- new: add support for gross merchandise value in pixel fire events

## 2.2.0 (08/05/2021)
- new: add general pixel firing support #91
- new: support advertiserId in response #93

## 2.1.0 (11/22/2020)
- new: add support to specify additional optional parameters at the Request and Placement levels #75
- new: adds `matchedPoints` to DecisionResponse to support GeoDistance Targeting  #78

## 2.0.0 (10/28/2020)
- new: add support for requesting multi-winner Placements by specifying a count on Placement for ad request #74
   - breaking: `DecisionResponse getDecision(name)` has been replaced by `getDecisions(name)` which returns `List<Decision>`
   - breaking: `DecisionResponse getDecisions()` now returns a map from Placement name to decision list `Map<String, List<Decision>>`
- new: add support for `botFiltering` option #76
- fix: remote outdated `time` field from ad Request #77

## 1.0.1 (08/27/2020)
- enable minSDK 22

## 1.0.0 (08/12/2020)

* add support for Adzerk e-dash domains #68
* add Adzerk SDK version header to requests #67
* add support of custom hostname
* default placements to top-level networkId

## 0.5.0 (03/22/2020)

* Android SDK updates for API 29
* update project documentation
* add console test logging

## 0.4.0 (05/19/18)

* Support GDPR params & endpoints #64

## 0.3.0 (10/22/17)

* network library updated: Retrofit2 (v2.3.0)
* updated for SDK 26

## 0.2.0 (12/12/16)

* Fixed [#51](https://github.com/adzerk/adzerk-android-sdk/issues/51): properly parse event IDs and URLs from Native Ads API responses.

## 0.1.1 (8/7/15)

...

## 0.1.0 (8/4/15)
