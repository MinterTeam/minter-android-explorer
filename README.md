Minter Android Explorer API SDK
===============================
[![Download](https://api.bintray.com/packages/minterteam/android/minter-android-explorer-testnet/images/download.svg) ](https://bintray.com/minterteam/android/minter-android-explorer-testnet/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


Minter Explorer API SDK library for Android
---------------------------

## Setup

Gradle
root build.gradle
```groovy
allprojects {
    repositories {
       // ... some repos
        maven { url "https://dl.bintray.com/minterteam/android" }
    }
}
```

project build.gradle
```groovy

ext {
    minterExplorerSDK = "0.6.3"
}

dependencies {
    // for testnet use suffix "-testnet"
    implementation "network.minter.android:minter-android-explorer-testnet:${minterExplorerSDK}"

    // for main net
    implementation "network.minter.android:minter-android-explorer:${minterExplorerSDK}"
}
```

## Basic Usage
### Initialize it
```java

MinterExplorerApi.initialize();
```

### Usage
SDK uses retrofit http client [see](https://square.github.io/retrofit/)
```java
// get transactions (or other) repository
ExplorerTransactionsRepository txRepo = MinterExplorerApi.getInstance().transactions();

// get list of transactions by given address
MinterAddress address = new MinterAddress("Mx01c8af77721c9666c672de62a4deadda0dafb03a");
txRepo.getTransactions(address).enqueue(new Callback<ExpResult<List<HistoryTransaction>>>() {
    @Override
    public void onResponse(@NonNull Call<ExpResult<List<HistoryTransaction>>> call, @NonNull Response<ExpResult<List<HistoryTransaction>>> response) {
        // handle response
    }

    @Override
    public void onFailure(@NonNull Call<ExpResult<List<HistoryTransaction>>> call, @NonNull Throwable t) {
        // handle error
    }
});
```
For more examples, see our [wallet app](https://github.com/MinterTeam/minter-android-wallet)

## Docs
TODO (javadocs available for now)

## Build
TODO

## Tests
TODO

## Changelog

See [Release notes](RELEASE.md)


## License

This software is released under the [MIT](LICENSE.txt) License.

© 2018 MinterTeam <edward.vstock@gmail.com>, All rights reserved.