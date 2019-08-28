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

import java.util.List;

import network.minter.explorer.models.AddressData;
import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.DelegationInfo;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.RewardData;
import retrofit2.Call;
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
     * @return Retrofit call with {@link ExpResult}
     */
    @GET("v1/addresses/{address}")
    Call<BCExplorerResult<AddressData>> balance(@Path("address") String address);

    /**
     * Resolve balance by address
     * @param address
     * @return Retrofit call with {@link ExpResult}
     */
    @GET("v1/addresses/{address}")
    Call<BCExplorerResult<AddressData>> balance(@Path("address") String address, @Query("withSum") Integer withSum);

    /**
     * Resolve balance by multiple addresses
     *
     * @param addresses
     * @return Retrofit call with {@link ExpResult}
     */
    @GET("v1/addresses")
    Call<BCExplorerResult<List<AddressData>>> balanceMultiple(@Query(value = "addresses[]", encoded = true) List<String> addresses);

    /**
     * List of delegated validators
     * @param address
     * @return
     */
    @GET("v1/addresses/{address}/delegations")
    Call<ExpResult<List<DelegationInfo>>> getDelegationsForAddress(@Path("address") String address,
                                                                   @Query("page") long page);

    @GET("v1/addresses/{address}/events/rewards")
    Call<ExpResult<List<RewardData>>> getRewards(@Path("address") String address, @Query("page") long page);
}
