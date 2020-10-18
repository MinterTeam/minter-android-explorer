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

package network.minter.explorer.api;

import java.util.List;

import io.reactivex.Observable;
import network.minter.explorer.models.AddressBalance;
import network.minter.explorer.models.AddressListBalances;
import network.minter.explorer.models.DelegationList;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.RewardStatistics;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * minter-android-explorer. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public interface ExplorerAddressEndpoint {
    /**
     * Resolve balance by address
     *
     * @param address
     * @return Retrofit call with [ExpResult]
     */
    @GET("addresses/{address}")
    Observable<ExpResult<AddressBalance>> balance(@Path("address") String address);

    /**
     * Resolve balance by address
     *
     * @param address
     * @return Retrofit call with [ExpResult]
     */
    @GET("addresses/{address}")
    Observable<ExpResult<AddressBalance>> balance(@Path("address") String address, @Query("with_sum") Integer withSum);

    /**
     * Resolve balance by multiple addresses
     *
     * @param addresses
     * @return Retrofit call with [ExpResult]
     */
    @GET("addresses")
    Observable<ExpResult<AddressListBalances>> balanceMultiple(@Query(value = "addresses[]", encoded = true) List<String> addresses);

    /**
     * List of delegated validators
     *
     * @param address
     * @return
     */
    @GET("addresses/{address}/delegations")
    Observable<ExpResult<DelegationList>> getDelegationsForAddress(
            @Path("address") String address,
            @Query("page") Integer page
    );

    /**
     * Aggregated reward statistics with just sum and date
     *
     * @param address
     * @return
     */
    @GET("addresses/{address}/statistics/rewards")
    Observable<ExpResult<List<RewardStatistics>>> getRewardStatistics(@Path("address") String address);

    @GET("addresses/{address}/statistics/rewards")
    Observable<ExpResult<List<RewardStatistics>>> getRewardStatistics(
            @Path("address") String address,
            @Query("start_time") String startTime,
            @Query("end_time") String endTime
    );
}
