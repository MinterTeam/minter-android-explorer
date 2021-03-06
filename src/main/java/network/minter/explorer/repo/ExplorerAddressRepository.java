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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerAddressEndpoint;
import network.minter.explorer.api.converters.ExplorerAddressBalanceDeserializer;
import network.minter.explorer.api.converters.ExplorerAddressListBalancesDeserializer;
import network.minter.explorer.api.converters.ExplorerCoinDelegationJsonConverter;
import network.minter.explorer.api.converters.ExplorerDelegationListJsonConverter;
import network.minter.explorer.models.AddressBalance;
import network.minter.explorer.models.AddressListBalances;
import network.minter.explorer.models.CoinDelegation;
import network.minter.explorer.models.DelegationList;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.RewardStatistics;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;


/**
 * minter-android-explorer. 2018
 * Address information api repository
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ExplorerAddressRepository extends DataRepository<ExplorerAddressEndpoint> implements DataRepository.Configurator {
    public ExplorerAddressRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get full information about given addresses
     *
     * @param addresses list of minter addresses
     * @return Retrofit call
     */
    public Observable<ExpResult<AddressListBalances>> getAddressesData(List<MinterAddress> addresses) {
        checkNotNull(addresses, "List can't be null");
        checkArgument(addresses.size() > 0, "List can't be empty");

        final List<String> sAddresses = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            if (address == null) {
                continue;
            }
            sAddresses.add(address.toString());
        }

        checkArgument(sAddresses.size() > 0, "List contains all null items");

        return getInstantService(this).balanceMultiple(sAddresses);
    }

    /**
     * Get full information about given address
     *
     * @param address minter address
     * @return Retrofit call
     */
    public Observable<ExpResult<AddressBalance>> getAddressData(MinterAddress address) {
        return getAddressData(address.toString());
    }

    /**
     * Get full information about given address
     *
     * @param address minter address
     * @return Retrofit call
     */
    public Observable<ExpResult<AddressBalance>> getAddressData(MinterAddress address, boolean withSum) {
        return getAddressData(address.toString(), withSum);
    }

    /**
     * Get full information about given address
     *
     * @param address string minter address WITH prefix "Mx"
     * @return Retrofit call
     */
    public Observable<ExpResult<AddressBalance>> getAddressData(String address) {
        checkArgument(MinterAddress.testString(address), "Invalid address");
        return getInstantService().balance(address);
    }

    /**
     * Get full information about given address
     *
     * @param address string minter address WITH prefix "Mx"
     * @param withSum Show total balance
     * @return Retrofit call
     */
    public Observable<ExpResult<AddressBalance>> getAddressData(String address, boolean withSum) {
        return getInstantService().balance(address, withSum ? 1 : 0);
    }

    /**
     * Get list of delegated coins to validators
     *
     * @param address
     * @return
     */
    public Observable<ExpResult<DelegationList>> getDelegations(MinterAddress address) {
        checkNotNull(address, "Address can't be null");

        return getInstantService().getDelegationsForAddress(address.toString(), 1);
    }

    /**
     * Get list of delegated coins to validators
     *
     * @param address
     * @return
     */
    public Observable<ExpResult<DelegationList>> getDelegations(MinterAddress address, Integer page) {
        checkNotNull(address, "Address can't be null");

        return getInstantService().getDelegationsForAddress(address.toString(), page);
    }

    /**
     * Get as much reward statistics as explorer gives. No pagination available.
     *
     * @param address
     * @return
     */
    public Observable<ExpResult<List<RewardStatistics>>> getRewardStatistics(String address) {
        checkNotNull(address, "Address can't be null");
        return getInstantService().getRewardStatistics(address);
    }

    /**
     * Get as much reward statistics as explorer gives. No pagination available.
     *
     * @param address
     * @return
     */
    public Observable<ExpResult<List<RewardStatistics>>> getRewardStatistics(MinterAddress address) {
        checkNotNull(address, "Address can't be null");
        return getInstantService().getRewardStatistics(address.toString());
    }

    /**
     * Get reward for specified dates. No pagination available.
     *
     * @param address   Fully qualified minter address
     * @param startTime Formats: YYYY-MM-DD | YYYY-MM-DD HH:MM:SS
     * @param endTime   Formats: YYYY-MM-DD | YYYY-MM-DD HH:MM:SS
     * @return
     */
    public Observable<ExpResult<List<RewardStatistics>>> getRewardStatistics(String address, String startTime, String endTime) {
        checkNotNull(address, "Address can't be null");
        return getInstantService().getRewardStatistics(address, startTime, endTime);
    }

    /**
     * Get reward for specified dates. No pagination available.
     *
     * @param address   Fully qualified minter address
     * @param startTime Formats: YYYY-MM-DD | YYYY-MM-DD HH:MM:SS
     * @param endTime   Formats: YYYY-MM-DD | YYYY-MM-DD HH:MM:SS
     * @return
     */
    public Observable<ExpResult<List<RewardStatistics>>> getRewardStatistics(MinterAddress address, String startTime, String endTime) {
        checkNotNull(address, "Address can't be null");
        return getInstantService().getRewardStatistics(address.toString(), startTime, endTime);
    }

    @Nonnull
    @Override
    protected Class<ExplorerAddressEndpoint> getServiceClass() {
        return ExplorerAddressEndpoint.class;
    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(AddressBalance.class, new ExplorerAddressBalanceDeserializer());
        api.registerTypeAdapter(AddressListBalances.class, new ExplorerAddressListBalancesDeserializer());
        api.registerTypeAdapter(CoinDelegation.class, new ExplorerCoinDelegationJsonConverter());
        api.registerTypeAdapter(DelegationList.class, new ExplorerDelegationListJsonConverter());
    }

}
