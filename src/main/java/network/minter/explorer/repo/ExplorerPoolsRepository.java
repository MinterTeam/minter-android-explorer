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

package network.minter.explorer.repo;

import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerPoolsEndpoint;
import network.minter.explorer.models.CoinItemBase;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.GateResult;
import network.minter.explorer.models.HistoryTransaction;
import network.minter.explorer.models.Pool;
import network.minter.explorer.models.PoolProvider;
import network.minter.explorer.models.PoolRoute;

/**
 * minter-android-explorer. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class ExplorerPoolsRepository extends DataRepository<ExplorerPoolsEndpoint> implements DataRepository.Configurator {
    public ExplorerPoolsRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Observable<ExpResult<List<Pool>>> getPools() {
        return getInstantService().getPools();
    }

    public Observable<ExpResult<List<Pool>>> getPools(int page) {
        return getInstantService().getPools(page);
    }

    public Observable<ExpResult<Pool>> getPoolByToken(String token) {
        return getInstantService().getPoolByToken(token);
    }

    public Observable<ExpResult<Pool>> getPoolByToken(BigInteger coinId) {
        return getInstantService().getPoolByToken(coinId.toString());
    }

    public Observable<ExpResult<Pool>> getPoolByPair(String coin0, String coin1) {
        return getInstantService().getPoolByPair(coin0, coin1);
    }

    public Observable<ExpResult<Pool>> getPoolByPair(BigInteger coin0, BigInteger coin1) {
        return getInstantService().getPoolByPair(coin0.toString(), coin1.toString());
    }


    public Observable<ExpResult<List<HistoryTransaction>>> getTransactionsByToken(String token, int page) {
        return getInstantService().getTransactionsByToken(token, page);
    }

    public Observable<ExpResult<List<HistoryTransaction>>> getTransactionsByToken(BigInteger tokenId, int page) {
        return getInstantService().getTransactionsByToken(tokenId.toString(), page);
    }

    public Observable<ExpResult<List<HistoryTransaction>>> getTransactionsByPair(String coin0, String coin1) {
        return getInstantService().getTransactionsByPair(coin0, coin1);
    }

    public Observable<ExpResult<List<HistoryTransaction>>> getTransactionsByPair(BigInteger coin0, BigInteger coin1) {
        return getInstantService().getTransactionsByPair(coin0.toString(), coin1.toString());
    }

    public Observable<ExpResult<List<CoinItemBase>>> getPossibleSwaps(String coinOrToken) {
        return getInstantService().getPossibleSwaps(coinOrToken);
    }

    public Observable<ExpResult<List<CoinItemBase>>> getPossibleSwaps(BigInteger coinOrTokenId) {
        return getInstantService().getPossibleSwaps(coinOrTokenId.toString());
    }

    public Observable<ExpResult<List<PoolProvider>>> getProvidersByToken(String token) {
        return getInstantService().getProvidersByToken(token);
    }

    public Observable<ExpResult<List<PoolProvider>>> getProvidersByToken(BigInteger tokenId) {
        return getInstantService().getProvidersByToken(tokenId.toString());
    }

    public Observable<ExpResult<List<PoolProvider>>> getProvidersByPair(String coin0, String coin1) {
        return getInstantService().getProvidersByPair(coin0, coin1);
    }

    public Observable<ExpResult<List<PoolProvider>>> getProvidersByPair(BigInteger coin0, BigInteger coin1) {
        return getInstantService().getProvidersByPair(coin0.toString(), coin1.toString());
    }

    public Observable<ExpResult<PoolProvider>> getProviderByToken(String token, MinterAddress address) {
        return getInstantService().getProviderByToken(token, address.toString());
    }

    public Observable<ExpResult<PoolProvider>> getProviderByToken(BigInteger tokenId, MinterAddress address) {
        return getInstantService().getProviderByToken(tokenId.toString(), address.toString());
    }

    public Observable<ExpResult<PoolProvider>> getProviderByToken(String token, String address) {
        return getInstantService().getProviderByToken(token, address);
    }

    public Observable<ExpResult<PoolProvider>> getProviderByToken(BigInteger tokenId, String address) {
        return getInstantService().getProviderByToken(tokenId.toString(), address);
    }

    public Observable<ExpResult<PoolProvider>> getProviderByPair(BigInteger coin0, BigInteger coin1, MinterAddress address) {
        return getInstantService().getProviderByPair(coin0.toString(), coin1.toString(), address.toString());
    }

    public Observable<ExpResult<PoolProvider>> getProviderByPair(String coin0, String coin1, String address) {
        return getInstantService().getProviderByPair(coin0, coin1, address);
    }

    public Observable<ExpResult<PoolProvider>> getProviderByPair(BigInteger coin0, BigInteger coin1, String address) {
        return getInstantService().getProviderByPair(coin0.toString(), coin1.toString(), address);
    }

    public Observable<ExpResult<List<PoolProvider>>> getProvidersByAddress(String address) {
        return getInstantService().getProvidersByAddress(address);
    }

    public Observable<ExpResult<List<PoolProvider>>> getProvidersByAddress(MinterAddress address) {
        return getProvidersByAddress(address.toString());
    }

    public Observable<GateResult<PoolRoute>> getRoute(String coin0, String coin1, BigDecimal amount, PoolRoute.SwapType swapType) {
        return getInstantService()
                .getRoute(coin0, coin1, Transaction.normalizeValue(amount).toString(), swapType.getValue());
    }

    public Observable<GateResult<PoolRoute>> getRoute(BigInteger coin0, BigInteger coin1, BigDecimal amount, PoolRoute.SwapType swapType) {
        return getInstantService()
                .getRoute(coin0.toString(), coin1.toString(), Transaction.normalizeValue(amount).toString(), swapType.getValue());
    }

    public Observable<GateResult<PoolRoute>> getRoute(CoinItemBase coin0, CoinItemBase coin1, BigDecimal amount, PoolRoute.SwapType swapType) {
        return getRoute(coin0.id, coin1.id, amount, swapType);
    }

    public Observable<GateResult<PoolRoute>> getRoute(BigInteger coin0, BigInteger coin1, BigInteger amount, PoolRoute.SwapType swapType) {
        return getInstantService()
                .getRoute(coin0.toString(), coin1.toString(), amount.toString(), swapType.getValue());
    }

    /**
     * getEstimate works almost like getRoute but accepts not only tokens but coins too
     *
     * @param coin0
     * @param coin1
     * @param amount
     * @param swapType
     * @return
     */
    public Observable<GateResult<PoolRoute>> getEstimate(String coin0, String coin1, BigDecimal amount, PoolRoute.SwapType swapType) {
        return getInstantService()
                .getEstimate(coin0, coin1, Transaction.normalizeValue(amount).toString(), swapType.getValue());
    }

    public Observable<GateResult<PoolRoute>> getEstimate(BigInteger coin0, BigInteger coin1, BigDecimal amount, PoolRoute.SwapType swapType) {
        return getInstantService()
                .getEstimate(coin0.toString(), coin1.toString(), Transaction.normalizeValue(amount).toString(), swapType.getValue());
    }

    public Observable<GateResult<PoolRoute>> getEstimate(CoinItemBase coin0, CoinItemBase coin1, BigDecimal amount, PoolRoute.SwapType swapType) {
        return getEstimate(coin0.id, coin1.id, amount, swapType);
    }

    public Observable<GateResult<PoolRoute>> getEstimate(BigInteger coin0, BigInteger coin1, BigInteger amount, PoolRoute.SwapType swapType) {
        return getInstantService()
                .getEstimate(coin0.toString(), coin1.toString(), amount.toString(), swapType.getValue());
    }


    @Nonnull
    @Override
    protected Class<ExplorerPoolsEndpoint> getServiceClass() {
        return ExplorerPoolsEndpoint.class;
    }
    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(new TypeToken<GateResult<PoolRoute>>() {
        }.getType(), new GateResult.Deserializer<>(PoolRoute.class));
    }
}
