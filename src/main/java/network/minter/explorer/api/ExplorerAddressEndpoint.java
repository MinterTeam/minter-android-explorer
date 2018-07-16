/*
 * Copyright (C) by MinterTeam. 2018
 * @link https://github.com/MinterTeam
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
import network.minter.explorer.models.BalanceChannel;
import network.minter.explorer.models.ExpResult;
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
	@GET("v1/address/{address}")
    Call<ExpResult<AddressData>> balance(@Path("address") String address);

	/**
	 * Resolve balance by multiple addresses
	 *
	 * @param addresses
	 * @return Retrofit call with {@link ExpResult}
	 */
	@GET("v1/address")
    Call<ExpResult<List<AddressData>>> balanceMultiple(@Query(value = "addresses[]", encoded = true) List<String> addresses);

	/**
	 * Get WebSocket connection data
	 *
	 * @param addresses
	 * @return Retrofit call with {@link ExpResult}
	 */
	@GET("v1/address/get-balance-channel")
	Call<ExpResult<BalanceChannel>> getBalanceChannel(@Query(value = "addresses[]", encoded = true) List<String> addresses);

	/**
	 * Get WebSocket connection data
	 *
	 * @param addresses
	 * @param user      For statistics some user id
	 * @return Retrofit call with {@link ExpResult}
	 */
	@GET("v1/address/get-balance-channel")
	Call<ExpResult<BalanceChannel>> getBalanceChannel(@Query(value = "addresses[]", encoded = true) List<String> addresses, @Query("user") String user);
}
