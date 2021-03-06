/*
 * Copyright (C) by MinterTeam. 2020
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.explorer.repo;

import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.GateEstimateEndpoint;
import network.minter.explorer.models.CoinItemBase;
import network.minter.explorer.models.GateResult;
import network.minter.explorer.models.TxCount;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-explorer. 2019
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class GateEstimateRepository extends DataRepository<GateEstimateEndpoint> implements DataRepository.Configurator {
    public GateEstimateRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * @param coinIdToSell Selling coin base structure
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin base structure
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(CoinItemBase coinToSell, BigDecimal valueToSell, CoinItemBase coinIdToBuy) {
        return getCoinExchangeCurrencyToSell(coinToSell.id, valueToSell.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinIdToBuy.id);
    }

    /**
     * @param coinIdToSell Selling coin
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(BigInteger coinIdToSell, BigDecimal valueToSell, BigInteger coinIdToBuy) {
        return getCoinExchangeCurrencyToSell(coinIdToSell, valueToSell.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinIdToBuy);
    }

    /**
     * @param coinIdToSell Selling coin
     * @param valueToSell  Selling amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(BigInteger coinIdToSell, BigInteger valueToSell, BigInteger coinIdToBuy) {
        return getInstantService().getCoinExchangeCurrencyToSellById(
                checkNotNull(coinIdToSell, "Source coin ID required").toString(),
                valueToSell.toString(),
                checkNotNull(coinIdToBuy, "Target coin ID required").toString()
        );
    }

    /**
     * @param coinToSell  Selling coin
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *                    1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(String coinToSell, BigDecimal valueToSell, String coinToBuy) {
        return getCoinExchangeCurrencyToSell(coinToSell, valueToSell.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy);
    }

    /**
     * @param coinToSell  Selling coin
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *                    1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy   Buying coin coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(String coinToSell, BigInteger valueToSell, String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToSell(
                checkNotNull(coinToSell, "Source coin required").toUpperCase(),
                valueToSell.toString(),
                checkNotNull(coinToBuy, "Target coin required").toUpperCase()
        );
    }

    /**
     * @param coinToSell Selling coin base structure
     * @param valueToBuy Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                   float equivalent)
     * @param coinToBuy  Buying coin base structure
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(CoinItemBase coinToSell, BigDecimal valueToBuy, CoinItemBase coinToBuy) {
        return getCoinExchangeCurrencyToBuy(coinToSell.id, valueToBuy.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy.id);
    }

    /**
     * @param coinIdToSell Selling coin
     * @param valueToBuy   Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                     float equivalent)
     * @param coinIdToBuy  Buying coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(BigInteger coinIdToSell, BigDecimal valueToBuy, BigInteger coinIdToBuy) {
        return getCoinExchangeCurrencyToBuy(coinIdToSell, valueToBuy.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinIdToBuy);
    }

    /**
     * @param coinIdToSell Selling coin
     * @param valueToBuy   Buying amount of exchange (big integer amount like: 1 BIP equals
     *                     1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinIdToBuy  Buying coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(BigInteger coinIdToSell, BigInteger valueToBuy, BigInteger coinIdToBuy) {
        return getInstantService().getCoinExchangeCurrencyToBuyById(
                checkNotNull(coinIdToSell, "Source coin ID required").toString(),
                valueToBuy.toString(), checkNotNull(coinIdToBuy, "Target coin ID required").toString()
        );
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToBuy Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *                   float equivalent)
     * @param coinToBuy  Buying coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(@Nonnull String coinToSell, BigDecimal valueToBuy, @Nonnull String coinToBuy) {
        return getCoinExchangeCurrencyToBuy(coinToSell, valueToBuy.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy);
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToBuy Buying amount of exchange (big integer amount like: 1 BIP equals
     *                   1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy  Buying coin
     * @return Exchange calculation
     */
    public Observable<GateResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(@Nonnull String coinToSell, BigInteger valueToBuy, @Nonnull String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToBuy(
                checkNotNull(coinToSell, "Source coin required").toUpperCase(),
                valueToBuy.toString(), checkNotNull(coinToBuy, "Target coin required").toUpperCase()
        );
    }

    /**
     * Resolve transaction commission before sending it
     *
     * @param sign Transaction sign is NOT A TRANSACTION HASH, it's a valid transaction and valid to send
     * @return
     * @see network.minter.blockchain.models.operational.Transaction#signSingle(PrivateKey)
     */
    public Observable<GateResult<TransactionCommissionValue>> getTransactionCommission(TransactionSign sign) {
        checkArgument(sign != null, "Transaction signature required!");
        return getTransactionCommission(sign.getTxSign());
    }

    /**
     * Resolve transaction commission before sending it
     *
     * @param sign
     * @return
     */
    public Observable<GateResult<TransactionCommissionValue>> getTransactionCommission(String sign) {
        checkArgument(sign != null, "Transaction signature required!");
        return getInstantService().getTxCommission(sign);
    }

    public Observable<GateResult<TxCount>> getTransactionCount(@Nonnull MinterAddress address) {
        checkArgument(address != null, "Address can't be null");
        return getTransactionCount(address.toString());
    }

    /**
     * Returns the number of transactions sent from an address
     *
     * @param address fq address
     * @return Prepared request with transaction count result
     */
    public Observable<GateResult<TxCount>> getTransactionCount(@Nonnull String address) {
        return getInstantService().getTransactionsCount(checkNotNull(address, "Address required!"));
    }

    @Nonnull
    @Override
    protected Class<GateEstimateEndpoint> getServiceClass() {
        return GateEstimateEndpoint.class;
    }


    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(new TypeToken<GateResult<TransactionCommissionValue>>() {
        }.getType(), new GateResult.Deserializer<>(TransactionCommissionValue.class));

        api.registerTypeAdapter(new TypeToken<GateResult<ExchangeSellValue>>() {
        }.getType(), new GateResult.Deserializer<>(ExchangeSellValue.class));

        api.registerTypeAdapter(new TypeToken<GateResult<ExchangeBuyValue>>() {
        }.getType(), new GateResult.Deserializer<>(ExchangeBuyValue.class));

        api.registerTypeAdapter(new TypeToken<GateResult<TxCount>>() {
        }.getType(), new GateResult.Deserializer<>(TxCount.class));
    }
}
