# Release notes

## 0.4.0
 - BREAKING CHANGES:
    - This version is FULLY incompatible with previously
    - New explorer api, much methods moved to Gate repositories. 
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