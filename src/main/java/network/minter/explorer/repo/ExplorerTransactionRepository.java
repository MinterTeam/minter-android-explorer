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
import network.minter.explorer.api.ExplorerTransactionEndpoint;
import network.minter.explorer.api.converters.ExplorerHistoryTransactionDeserializer;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;

import static network.minter.core.internal.common.Preconditions.checkArgument;

/**
 * minter-android-explorer. 2018
 * Transactions information api repository
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * @link <a href="https://testnet.explorer.minter.network/help/index.html#operations-Transactions-get_api_v1_transactions">swagger</a>
 */
@SuppressWarnings("ConstantConditions")
public class ExplorerTransactionRepository extends DataRepository<ExplorerTransactionEndpoint> implements DataRepository.Configurator {

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     *
     * @param address minter address
     * @param page    Pagination
     * @return Retrofit call
     * @TODO query builder
     * @link https://app.swaggerhub.com/apis/GrKamil/minter-explorer_api/1.2.0#/Addresses/getAddressTransactions
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address, int page) {
        checkArgument(address != null, "Address can't be null");
        return getInstantService().getTransactionsByAddress(address.toString(), page);
    }

    public ExplorerTransactionRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get transactions list with filter query
     *
     * @param builder
     * @return
     * @see TxSearchQuery
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(TxSearchQuery builder) {
        return getInstantService().getTransactions(builder.build());
    }

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     *
     * @param address minter address
     * @return Retrofit call
     * @TODO query builder
     * @link https://app.swaggerhub.com/apis/GrKamil/minter-explorer_api/1.2.0#/Addresses/getAddressTransactions
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address) {
        checkArgument(address != null, "Address can't be null");
        return getInstantService().getTransactionsByAddress(address.toString());
    }

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     *
     * @param address minter address
     * @param page    Pagination
     * @return Retrofit call
     * @TODO query builder
     * @link https://app.swaggerhub.com/apis/GrKamil/minter-explorer_api/1.2.0#/Addresses/getAddressTransactions
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address, int page, TxFilter filter) {
        checkArgument(address != null, "Address can't be null");
        if (filter == null || filter == TxFilter.None) {
            return getInstantService().getTransactionsByAddress(address.toString(), page);
        }
        return getInstantService().getTransactionsByAddress(address.toString(), page, filter.value);
    }

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     *
     * @param address   minter address
     * @param fromBlock get list started from specified block
     * @param toBlock   get list ended on specified block
     * @return Retrofit call
     * @TODO query builder
     * @link https://app.swaggerhub.com/apis/GrKamil/minter-explorer_api/1.2.0#/Addresses/getAddressTransactions
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address, long fromBlock, long toBlock, TxFilter filter) {
        if (filter == null || filter == TxFilter.None) {
            return getTransactions(address, fromBlock, toBlock);
        }

        checkArgument(address != null, "Address can't be null");
        checkArgument(fromBlock >= 0 && toBlock >= 0, "Start and End block must be greater or equals to zero");
        return getInstantService().getTransactionsByAddress(address.toString(), fromBlock, toBlock, filter.value);
    }

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     *
     * @param address   minter address
     * @param fromBlock get list started from specified block
     * @param toBlock   get list ended on specified block
     * @return Retrofit call
     * @TODO query builder
     * @link https://app.swaggerhub.com/apis/GrKamil/minter-explorer_api/1.2.0#/Addresses/getAddressTransactions
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address, long fromBlock, long toBlock) {
        checkArgument(address != null, "Address can't be null");
        checkArgument(fromBlock >= 0 && toBlock >= 0, "Start and End block must be greater or equals to zero");
        return getInstantService().getTransactionsByAddress(address.toString(), fromBlock, toBlock);
    }

    /**
     * Get transactions list for multiple minter addresses with given page number
     *
     * @param addresses list of minter addresses
     * @param page      page number
     * @return Retrofit call
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, int page) {
        checkArgument(addresses != null, "Address list can't be null");
        checkArgument(addresses.size() > 0, "Address list can't be empty");

        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getInstantService().getTransactions(out, page);
    }

    /**
     * Get transactions list for multiple minter addresses
     *
     * @param addresses list of minter addresses
     * @return Retrofit call
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses) {
        return getTransactions(addresses, 1);
    }

    /**
     * Get transactions list for multiple minter addresses with given page number
     *
     * @param addresses list of minter addresses
     * @param page      page number
     * @return Retrofit call
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, int page, TxFilter filter) {
        checkArgument(addresses != null, "Address list can't be null");
        checkArgument(addresses.size() > 0, "Address list can't be empty");

        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        if (filter == null || filter == TxFilter.None) {
            return getInstantService().getTransactions(out, page);
        } else {
            return getInstantService().getTransactions(out, page, filter.value);
        }
    }

    /**
     * Get transactions list for multiple minter addresses with given page number
     *
     * @param addresses list of minter addresses
     * @param page      page number
     * @return Retrofit call
     */
    public Observable<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, int page, int limit) {
        checkArgument(addresses != null, "Address list can't be null");
        checkArgument(addresses.size() > 0, "Address list can't be empty");

        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getInstantService().getTransactions(out, page, limit);
    }

    public Observable<ExpResult<HistoryTransaction>> getTransaction(@Nonnull String txHash) {
        checkArgument(txHash != null, "Tx hash can't be null");
        return getInstantService().findTransactionByHash(txHash);
    }

    public enum TxFilter {
        None(null),
        Incoming("incoming"),
        Outgoing("outcoming");

        String value;

        TxFilter(String value) {
            this.value = value;
        }

        public static TxFilter find(String val) {
            if (val == null) {
                return None;
            }
            for (TxFilter f : TxFilter.values()) {
                if (val.equals(f.value)) {
                    return f;
                }
            }
            return null;
        }

    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(HistoryTransaction.class, new ExplorerHistoryTransactionDeserializer());
    }

    @Nonnull
    @Override
    protected Class<ExplorerTransactionEndpoint> getServiceClass() {
        return ExplorerTransactionEndpoint.class;
    }

}
