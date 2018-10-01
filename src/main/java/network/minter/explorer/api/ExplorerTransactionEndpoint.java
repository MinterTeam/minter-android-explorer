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

package network.minter.explorer.api;

import java.util.List;
import java.util.Map;

import network.minter.blockchain.models.CountableData;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.blockchain.models.TransactionSendResult;
import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface ExplorerTransactionEndpoint {
    /**
     * @param query
     * @return
     */
    @GET("v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@QueryMap Map<String, String> query);

    @GET("v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@Query(value = "addresses[]", encoded = true) List<String> addresses);

    @GET("v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(@Query(value = "addresses[]", encoded = true) List<String> addresses, @Query("page") long page);

    @GET("v1/transactions")
    Call<ExpResult<List<HistoryTransaction>>> getTransactions(
            @Query(value = "addresses[]", encoded = true) List<String> addresses,
            @Query("page") long page,
            @Query("perPage") int limit
    );

    @GET("v1/transaction/{hash}")
    Call<ExpResult<HistoryTransaction>> findTransactionByHash(@Path("hash") String hash);

    @GET("v1/transaction/get-count/{address}")
    Call<BCExplorerResult<CountableData>> getTransactionsCount(@Path("address") String address);

    @POST("v1/transaction/push")
    Call<BCExplorerResult<TransactionSendResult>> sendTransaction(@Body Map<String, String> data);

    /**
     * Calculates signed transaction commission
     * @param signedTx Valid transaction, signed with private key
     * @return
     */
    @GET("v1/estimate/tx-commission")
    Call<BCExplorerResult<TransactionCommissionValue>> getTxCommission(@Query("transaction") String signedTx);


}
