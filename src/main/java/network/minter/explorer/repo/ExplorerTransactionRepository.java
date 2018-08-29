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

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerTransactionEndpoint;
import network.minter.explorer.api.converters.ExplorerHistoryTransactionDeserializer;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;
import retrofit2.Call;

/**
 * minter-android-explorer. 2018
 * Transactions information api repository
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * @link <a href="https://testnet.explorer.minter.network/help/index.html#operations-Transactions-get_api_v1_transactions">swagger</a>
 */
public class ExplorerTransactionRepository extends DataRepository<ExplorerTransactionEndpoint> implements DataRepository.Configurator {
    public ExplorerTransactionRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     * @param address minter address
     * @return
     * @TODO query builder
     * @link https://explorer.beta.minter.network/help/index.html#operations-Transactions-get_api_v1_transactions
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address) {
        Map<String, String> query = new HashMap<>();
        query.put("address", address.toString());
        return getInstantService().getTransactions(query);
    }

    /**
     * Get transactions list for multiple minter addresses
     * @param addresses list of minter addresses
     * @return
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses) {
        return getTransactions(addresses, 1);
    }

    /**
     * Get transactions list for multiple minter addresses with given page number
     * @param addresses list of minter addresses
     * @param page page number
     * @return
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, long page) {
        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getInstantService().getTransactions(out, page);
    }

    /**
     * Get transactions list for multiple minter addresses with given page number
     * @param addresses list of minter addresses
     * @param page page number
     * @return
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, long page, int limit) {
        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getInstantService().getTransactions(out, page, limit);
    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(HistoryTransaction.class, new ExplorerHistoryTransactionDeserializer());
    }

    @NonNull
    @Override
    protected Class<ExplorerTransactionEndpoint> getServiceClass() {
        return ExplorerTransactionEndpoint.class;
    }
}
