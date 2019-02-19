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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import network.minter.blockchain.models.CountableData;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.blockchain.models.TransactionSendResult;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerTransactionEndpoint;
import network.minter.explorer.api.converters.ExplorerHistoryTransactionDeserializer;
import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;
import retrofit2.Call;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.CollectionsHelper.asMap;

/**
 * minter-android-explorer. 2018
 * Transactions information api repository
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * @link <a href="https://testnet.explorer.minter.network/help/index.html#operations-Transactions-get_api_v1_transactions">swagger</a>
 */
@SuppressWarnings("ConstantConditions")
public class ExplorerTransactionRepository extends DataRepository<ExplorerTransactionEndpoint> implements DataRepository.Configurator {
    public ExplorerTransactionRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get transactions list with filter query
     * @param builder
     * @return
     * @see TxSearchQuery
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(TxSearchQuery builder) {
        return getInstantService().getTransactions(builder.build());
    }

    /**
     * Get transactions list for given minter address
     * Method not finished
     * see link below
     * @param address minter address
     * @return Retrofit call
     * @TODO query builder
     * @link https://explorer.beta.minter.network/help/index.html#operations-Transactions-get_api_v1_transactions
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(MinterAddress address) {
        checkArgument(address != null, "Address can't be null");
        Map<String, Object> query = new HashMap<>();
        query.put("address", address.toString());
        return getInstantService().getTransactions(query);
    }

    /**
     * Get transactions list for multiple minter addresses
     * @param addresses list of minter addresses
     * @return Retrofit call
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses) {
        return getTransactions(addresses, 1);
    }

    /**
     * Get transactions list for multiple minter addresses with given page number
     * @param addresses list of minter addresses
     * @param page page number
     * @return Retrofit call
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, long page) {
        checkArgument(addresses != null, "Address list can't be null");
        checkArgument(addresses.size() > 0, "Address list can't be empty");

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
     * @return Retrofit call
     */
    public Call<ExpResult<List<HistoryTransaction>>> getTransactions(List<MinterAddress> addresses, long page, int limit) {
        checkArgument(addresses != null, "Address list can't be null");
        checkArgument(addresses.size() > 0, "Address list can't be empty");

        List<String> out = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            out.add(address.toString());
        }

        return getInstantService().getTransactions(out, page, limit);
    }

    public Call<BCExplorerResult<CountableData>> getTransactionCount(@Nonnull MinterAddress address) {
        checkArgument(address != null, "Address can't be null");
        return getTransactionCount(address.toString());
    }

    /**
     * Returns the number of transactions sent from an address
     * @param address fq address
     * @return Prepared request with transaction count result
     */
    public Call<BCExplorerResult<CountableData>> getTransactionCount(@Nonnull String address) {
        return getInstantService().getTransactionsCount(checkNotNull(address, "Address required!"));
    }

    /**
     * SendCoin transaction
     * @param transactionSign Raw signed TX
     * @return Prepared request
     * @see TransactionSendResult
     */
    public Call<BCExplorerResult<TransactionSendResult>> sendTransaction(@Nonnull TransactionSign transactionSign) {
        checkArgument(transactionSign != null && transactionSign.getTxSign() != null, "Transaction signature required!");
        return getInstantService().sendTransaction(
                asMap("transaction", transactionSign.getTxSign())
        );
    }

    /**
     * Resolve transaction commission before sending it
     * @param sign Transaction sign is NOT A TRANSACTION HASH, it's a valid transaction and valid to send
     * @return
     * @see network.minter.blockchain.models.operational.Transaction#sign(PrivateKey)
     */
    public Call<BCExplorerResult<TransactionCommissionValue>> getTransactionCommission(TransactionSign sign) {
        checkArgument(sign != null, "Transaction signature required!");
        return getTransactionCommission(sign.getTxSign());
    }

    /**
     * Resolve transaction commission before sending it
     * @param sign
     * @return
     */
    public Call<BCExplorerResult<TransactionCommissionValue>> getTransactionCommission(String sign) {
        checkArgument(sign != null, "Transaction signature required!");
        return getInstantService().getTxCommission(sign);
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
