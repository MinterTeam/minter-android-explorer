# Release notes

## 2.0.1
- Explorer coins estimate calculation
- Added pools repository to sdk instance

## 2.0.0
- **BREAKING FOR ANDROID RELEASE**
- blockchain sdk version 2.0.0
- Added new transactions data types to `HistoryTransaction`
- Added `ExplorerPoolsRepository` which represents pools information
- Added `Pool` and `PoolProvider` models

## 1.0.0
- **BREAKING RELEASE**
- removed `GateResult` - use `NodeResult` from blockchain sdk
- Simple retrofit calls replaced with RxJava2 calls
- Some methods changed it's signatures as minter 1.2 has new api and data sets
- Added `GateCoinRepository` to get coin information from Team's gate directly
- `CoinItem` and `CoinDelegations` now extends `CoinItemBase` which has coin id
- `AddressBalance`: `availableBalanceBIP` and `availableBalanceUSD` renamed to `stakeBalanceBIP` and `stakeBalanceBIP` accordingly
- `CoinDelegation`: now has field `isInWaitlist` that represents wallet delegated stake now in waitlist
- Added new transactions data types to `HistoryTransaction`

## 0.8.1
 - Added `minStake` and `commission` value for validator data

## 0.8.0
 - Renamed AddressData to AddressBalance
 - Added AddressListBalance instead of List<AddressData> for multiple addresses
 - Added a few models to represent delegated stakes and like
 - BREAKING: Renamed `MinterExplorerApi` base class to `MinterExplorerSDK` and marked as deprecated `initialize` methods. 
 Use `new MinterExplorerSDK.Setup()` to configure SDK
 - BREAKING: `GateTransactionRepository.sendTransaction` not returns `GateResult<PushResult>` instead of `GateResult<TransactionSendResult>`

## 0.7.1
 - Added "latest_block_time" to base explorer result model
 - Updated blockchain sdk to 0.12.2

## 0.7.0
 - Updated blockchain sdk to to 0.12.0

## 0.6.3
 - Added base Gate api url to initialize(...) methods
 - Cleanup

## 0.6.1
 - Updated core and blockchain sdk

## 0.6.0
 - Removed `available_balance_*` from address balance result
 - Added more info about validators
 - Also added repository to get all validators list

## 0.5.0
 - Migrated to androidx
 - Additional fields for balance: total balance, usd balance, etcr

## 0.4.5
 - Added query param to get balance with all coins sum and in USD
 - Updated SDK
 - Fixed getting transactions with limit

## 0.4.4
 - Fixed getting list by single address: new api method
 - Added couple of methods to get transactions by block numbers

## 0.4.3
 - Added pagination when retrieving delegations for address
 - Added additional field to the response Meta
 
## 0.4.2
 - Main Net base urls

## 0.4.1
 - Updated core library
 - Fixed incorrect deserialization of the Redeem Check transaction
    
## 0.4.0
 - BREAKING CHANGES:
    - This version is FULLY incompatible with previously
    - New explorer api, much methods moved to Gate repositories
    - Updated tests

## 0.3.1
 - Added temporary "Gate" repository to get min gas price directly from gate. Soon it will be separated sdk.

## 0.3.0
 - Repositories tests
 - Android-less code

## 0.2.9
 - BREAKING:
    - BCExplorerResult instead of ExpResult for those methods what are getting balance  

## 0.2.8
 - BREAKING:
   - new code type in error result, was enum, now it's a integer
 - New transaction types
 - Updated SDK versions
 - Added additional fields to coin info result

## 0.2.7
 - Updated blockchain SDK dependency with new fields

## 0.2.6
 - Added error and code to base response object

## 0.2.5
 - Updated core and blockchain libraries

## 0.2.4
 - added network id config
 - Reduced android dependencies usage
 - added ability to set custom explorer api url

## 0.2.3
 - added proxy methods for blockchain node through the Explorer:
    - send transaction
    - calculate transaction fee by transaction hash
    - coin exchange currency (sell/buy)
    - counting transactions by address to get **nonce**

## 0.2.2
 - package dependencies fix

## 0.2.1
 - updated core sdk to creating private key from mnemonic phrase directly

## 0.2.0
 - **breaking changes**:
   - all **"unbound"** transaction strings, objects and constants renamed to **"unbond"**
   - `TxDelegateResult` and `TxUnboundResult` now combined to single class: `TxDelegateUnbondResult` as they have single data model in explorer.
   - removed fields `from` from `Tx[*]Result`, use `HistoryTransaction#from`
   - renamed fields `pubKey` to `publicKey`
   - removed `amount` from `TxConvertCoinResult`, use `TxConvertCoinResult#getValueToBuy` and `TxConvertCoinResult#getValueToSell`
 - added more getters to models
 - deprecated methods `ExplorerAddressEndpoint#getBalanceChannel` and `ExplorerAddressRepository#getBalanceChannel`,
 use instead `ExplorerSettingsEndpoint#getBalanceChannel` and `ExplorerSettingsRepository#getBalanceChannel`.

## 0.1.3
 - added unspecified transactions data for type: unbound, redeem check and delegate

## 0.1.2
 - added list and searching coin method and repository. [Ref](https://testnet.explorer.minter.network/help/index.html#operations-Coins-get_api_v1_coins)
 - sdk versions now no more synchronized

## 0.1.1
 - change base frontend url for testnet environment
 - more javadocs
 - readme additions
 - `TxConvertCoinResult` now have 2 additional fields: **value_to_buy** and **value_to_sell**
 - limit items per page in `ExplorerTransactionsRepository` repository
