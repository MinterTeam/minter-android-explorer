# Release notes

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