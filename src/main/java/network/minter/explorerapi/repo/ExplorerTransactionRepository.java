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

package network.minter.explorerapi.repo;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.explorerapi.api.ExplorerTransactionEndpoint;
import network.minter.explorerapi.api.converters.ExplorerHistoryTransactionDeserializer;
import network.minter.explorerapi.models.ExpResult;
import network.minter.explorerapi.models.HistoryTransaction;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.data.DataRepository;
import retrofit2.Call;

/**
 * minter-android-explorer. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ExplorerTransactionRepository extends DataRepository<ExplorerTransactionEndpoint> {
    public ExplorerTransactionRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address) {
        Map<String, String> query = new HashMap<>();
        query.put("address", address.toString());

        return getService().getTransactions(query);
    }

    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses) {
        return getTransactions(addresses, 1);
    }

    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, long page) {
        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getService().getTransactions(out, page);
    }

    @Override
    protected void configureService(ApiService.Builder apiBuilder) {
        super.configureService(apiBuilder);
        apiBuilder.registerTypeAdapter(HistoryTransaction.class, new ExplorerHistoryTransactionDeserializer());
    }

    @NonNull
    @Override
    protected Class<ExplorerTransactionEndpoint> getServiceClass() {
        return ExplorerTransactionEndpoint.class;
    }
}