/*
 * Copyright (C) by MinterTeam. 2019
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

package network.minter.explorer.api;

import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.explorer.models.GateResult;
import network.minter.explorer.models.TxCount;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * minter-android-explorer. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public interface GateEstimateEndpoint {

    @GET("v1/estimate/tx-commission")
    Call<GateResult<TransactionCommissionValue>> getTxCommission();

    /**
     * Give an estimation about coin exchange (selling)
     * @param coinToSell coin to convert from
     * @param valueToSell BigInteger string value
     * @param coinToBuy coin to convert to
     * @return
     */
    @GET("v1/estimate/coin-sell")
    Call<GateResult<ExchangeSellValue>> getCoinExchangeCurrencyToSell(
            @Query("coinToSell") String coinToSell,
            @Query("valueToSell") String valueToSell,
            @Query("coinToBuy") String coinToBuy
    );

    /**
     * Give an estimation about coin exchange (buying)
     * @param coinToSell coin to convert from
     * @param valueToBuy BigInteger string value
     * @param coinToBuy coin to convert to
     * @return
     */
    @GET("v1/estimate/coin-buy")
    Call<GateResult<ExchangeBuyValue>> getCoinExchangeCurrencyToBuy(
            @Query("coinToSell") String coinToSell,
            @Query("valueToBuy") String valueToBuy,
            @Query("coinToBuy") String coinToBuy
    );

    /**
     * Calculates signed transaction commission
     * @param signedTx Valid transaction, signed with private key
     * @return
     */
    @GET("v1/estimate/tx-commission")
    Call<GateResult<TransactionCommissionValue>> getTxCommission(@Query("transaction") String signedTx);


    @GET("v1/nonce/{address}")
    Call<GateResult<TxCount>> getTransactionsCount(@Path("address") String address);
}
