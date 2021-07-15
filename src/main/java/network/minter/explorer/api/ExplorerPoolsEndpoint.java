/*
 * Copyright (C) by MinterTeam. 2021
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

import io.reactivex.Observable;
import network.minter.explorer.models.CoinItemBase;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.GateResult;
import network.minter.explorer.models.HistoryTransaction;
import network.minter.explorer.models.Pool;
import network.minter.explorer.models.PoolProvider;
import network.minter.explorer.models.PoolRoute;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * minter-android-explorer. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public interface ExplorerPoolsEndpoint {

    @GET("pools")
    Observable<ExpResult<List<Pool>>> getPools();

    @GET("pools/list/coins")
    Observable<ExpResult<List<CoinItemBase>>> getCoins();

    @GET("pools/list/coins/{coin}")
    Observable<ExpResult<List<CoinItemBase>>> getPossibleSwaps(@Path("coin") String coin);

    @GET("pools")
    Observable<ExpResult<List<Pool>>> getPools(@Query("page") Integer page);

    @GET("pools/token/{token}")
    Observable<ExpResult<Pool>> getPoolByToken(@Path("token") String token);

    @GET("pools/coins/{coin0}/{coin1}")
    Observable<ExpResult<Pool>> getPoolByPair(@Path("coin0") String coin0, @Path("coin1") String coin1);

    @GET("pools/coins/{coin0}/{coin1}/transactions")
    Observable<ExpResult<List<HistoryTransaction>>> getTransactionsByPair(@Path("coin0") String coin0, @Path("coin1") String coin1);

    @GET("pools/coins/{coin0}/{coin1}/providers")
    Observable<ExpResult<List<PoolProvider>>> getProvidersByPair(@Path("coin0") String coin0, @Path("coin1") String coin1);

    @GET("pools/coins/{coin0}/{coin1}/route")
    Observable<GateResult<PoolRoute>> getRoute(
            @Path("coin0") String coin0,
            @Path("coin1") String coin1,
            @Query("amount") String amount,
            @Query("type") String routeType
    );

    @GET("pools/coins/{coin0}/{coin1}/estimate")
    Observable<GateResult<PoolRoute>> getEstimate(
            @Path("coin0") String coin0,
            @Path("coin1") String coin1,
            @Query("amount") String amount,
            @Query("type") String routeType
    );

    @GET("pools/token/{coin0}/{coin1}/providers/{address}")
    Observable<ExpResult<PoolProvider>> getProviderByPair(@Path("coin0") String coin0, @Path("coin1") String coin1, @Path("address") String address);

    @GET("pools/providers/{address}")
    Observable<ExpResult<List<PoolProvider>>> getProvidersByAddress(@Path("address") String address);

    @GET("pools/token/{token}/providers")
    Observable<ExpResult<List<PoolProvider>>> getProvidersByToken(@Path("token") String token);

    @GET("pools/token/{token}/providers/{address}")
    Observable<ExpResult<PoolProvider>> getProviderByToken(@Path("token") String token, @Path("address") String address);

    @GET("pools/token/{token}/transactions")
    Observable<ExpResult<List<HistoryTransaction>>> getTransactionsByToken(@Path("token") String token, @Query("page") Integer page);
}
