/*
 * Copyright (C) by MinterTeam. 2018
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerCoinsEndpoint;
import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.CoinItem;
import network.minter.explorer.models.ExpResult;
import retrofit2.Call;

import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich [edward.vstock[at]gmail.com]
 */
public class ExplorerCoinsRepository extends DataRepository<ExplorerCoinsEndpoint> {
    public ExplorerCoinsRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get list of all known coins in network
     * @return Retrofit call
     */
    public Call<ExpResult<List<CoinItem>>> getAll() {
        return getInstantService().getAll();
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *         1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy Buying coin coin
     * @return Exchange calculation
     */
    public Call<BCExplorerResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(@Nonnull String coinToSell, BigDecimal valueToSell, @Nonnull String coinToBuy) {
        return getCoinExchangeCurrencyToSell(coinToSell, valueToSell.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy);
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToSell Selling amount of exchange (big integer amount like: 1 BIP equals
     *         1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy Buying coin coin
     * @return Exchange calculation
     */
    public Call<BCExplorerResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(@Nonnull String coinToSell, BigInteger valueToSell, @Nonnull String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToSell(
                checkNotNull(coinToSell, "Source coin required").toUpperCase(),
                valueToSell.toString(), checkNotNull(coinToBuy, "Target coin required").toUpperCase()
        );
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToBuy Buying amount of exchange (human readable amount like: 1 BIP equals 1.0 in
     *         float equivalent)
     * @param coinToBuy Buying coin
     * @return Exchange calculation
     */
    public Call<BCExplorerResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(@Nonnull String coinToSell, BigDecimal valueToBuy, @Nonnull String coinToBuy) {
        return getCoinExchangeCurrencyToBuy(coinToSell, valueToBuy.multiply(Transaction.VALUE_MUL_DEC).toBigInteger(), coinToBuy);
    }

    /**
     * @param coinToSell Selling coin
     * @param valueToBuy Buying amount of exchange (big integer amount like: 1 BIP equals
     *         1000000000000000000 (18 zeroes) in big integer equivalent)
     * @param coinToBuy Buying coin
     * @return Exchange calculation
     */
    public Call<BCExplorerResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(@Nonnull String coinToSell, BigInteger valueToBuy, @Nonnull String coinToBuy) {
        return getInstantService().getCoinExchangeCurrencyToBuy(
                checkNotNull(coinToSell, "Source coin required").toUpperCase(),
                valueToBuy.toString(), checkNotNull(coinToBuy, "Target coin required").toUpperCase()
        );
    }

    /**
     * Search coin by it symbol
     * @return Retrofit call
     */
    public Call<ExpResult<List<CoinItem>>> search(String symbol) {
        return getInstantService().search(symbol);
    }


    @Nonnull
    @Override
    protected Class<ExplorerCoinsEndpoint> getServiceClass() {
        return ExplorerCoinsEndpoint.class;
    }
}
