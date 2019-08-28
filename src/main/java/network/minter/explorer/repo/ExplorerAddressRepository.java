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

package network.minter.explorer.repo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerAddressEndpoint;
import network.minter.explorer.api.converters.ExplorerAddressDataDeserializer;
import network.minter.explorer.models.AddressData;
import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.DelegationInfo;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.RewardData;
import retrofit2.Call;

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
     * @param addresses list of minter addresses
     * @return Retrofit call
     */
    public Call<BCExplorerResult<List<AddressData>>> getAddressesData(List<MinterAddress> addresses) {
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
     * @param address minter address
     * @return Retrofit call
     */
    public Call<BCExplorerResult<AddressData>> getAddressData(MinterAddress address) {
        return getAddressData(address.toString());
    }

    /**
     * Get full information about given address
     * @param address minter address
     * @return Retrofit call
     */
    public Call<BCExplorerResult<AddressData>> getAddressData(MinterAddress address, boolean withSum) {
        return getAddressData(address.toString(), withSum);
    }

    /**
     * Get full information about given address
     * @param address string minter address WITH prefix "Mx"
     * @return Retrofit call
     */
    public Call<BCExplorerResult<AddressData>> getAddressData(String address) {
        return getInstantService().balance(address);
    }

    /**
     * Get full information about given address
     * @param address string minter address WITH prefix "Mx"
     * @param withSum Show total balance
     * @return Retrofit call
     */
    public Call<BCExplorerResult<AddressData>> getAddressData(String address, boolean withSum) {
        return getInstantService().balance(address, withSum ? 1 : 0);
    }

    /**
     * Get list of delegated coins to validators
     * @param address
     * @return
     */
    public Call<ExpResult<List<DelegationInfo>>> getDelegations(MinterAddress address) {
        checkNotNull(address, "Address can't be null");

        return getInstantService().getDelegationsForAddress(address.toString(), 1);
    }

    /**
     * Get list of delegated coins to validators
     * @param address
     * @return
     */
    public Call<ExpResult<List<DelegationInfo>>> getDelegations(MinterAddress address, long page) {
        checkNotNull(address, "Address can't be null");

        return getInstantService().getDelegationsForAddress(address.toString(), page);
    }

    /**
     * Get list of reward events
     * @param address
     * @param page
     * @return
     */
    public Call<ExpResult<List<RewardData>>> getRewards(MinterAddress address, long page) {
        checkNotNull(address, "Address can't be null");

        return getInstantService().getRewards(address.toString(), page);
    }

    public Call<ExpResult<List<RewardData>>> getRewards(MinterAddress address) {
        return getRewards(address, 1);
    }

    @Nonnull
    @Override
    protected Class<ExplorerAddressEndpoint> getServiceClass() {
        return ExplorerAddressEndpoint.class;
    }

	@Override
	public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(AddressData.class, new ExplorerAddressDataDeserializer());
	}

}
