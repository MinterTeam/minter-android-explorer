Minter Android Explorer API SDK
===============================
[![Download](https://api.bintray.com/packages/minterteam/android/minter-android-explorer/images/download.svg)](https://bintray.com/minterteam/android/minter-android-explorer-testnet/_latestVersion)
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
    minterExplorerSDK = "1.0.0"
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
class MyProject {
    public static void main(String[] args) {
        MinterExplorerSDK.Setup explorerSdk = new MinterExplorerSDK.Setup().setEnableDebug(true /*set true or false to see logs*/);

        // Optional: you can set your own explorer api url
        explorerSdk.setExplorerApiUrl(BuildConfig.EXPLORER_API_URL);
        // Optional: you can set your own gate api url
        explorerSdk.setGateApiUrl(BuildConfig.GATE_API_URL);
        
        // Optional: also you can set your own logger by interface {@link Mint.Leaf}
        explorerSdk.setLogger(new TimberLogger());
        // Finally, initialize singletone instance and get it
        explorerSdk.init();    
        
        // Now you can use SDK, see example below
    }
    
}
```

### Usage
SDK uses retrofit http client with RxJava2


Example: how to get transaction list by minter address

```java
import android.util.Log;
import java.util.List;
import io.reactivex.schedulers.Schedulers;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;
import network.minter.explorer.repo.ExplorerTransactionRepository;

class MyProject {
    
    public void someFunc() {
        ExplorerTransactionRepository txRepo = MinterExplorerSDK.getInstance().transactions();

        // get list of transactions by given address
        MinterAddress address = new MinterAddress("Mx01c8af77721c9666c672de62a4deadda0dafb03a");
        txRepo.getTransactions(address)
            .subscribeOn(Schedulers.io())
            .subscribe((ExpResult<List<HistoryTransaction>> response) -> {
                if(response.isOk()) {
                    List<HistoryTransaction> transactions = response.result;    
                } else {
                    // all errors with "content-type: application/json" will be put in the ExpResult, so you can handle it without exceptions
                    Log.d("Explorer", String.format("Error: [%d] %s", response.getCode(), response.getMessage()));
                }
            }, throwable -> {
                // there is some unknown error
            });
    }
}
```
For more examples, see our [wallet app](https://github.com/MinterTeam/minter-android-wallet)

## Docs
Javadocs available with package on bintray. Usage guide: soon

## Build
TODO

## Tests
TODO

## Changelog
See [Release notes](RELEASE.md)


## License

This software is released under the [MIT](LICENSE.txt) License.

© 2018 MinterTeam <edward.vstock@gmail.com>, All rights reserved.