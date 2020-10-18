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

import com.google.gson.reflect.TypeToken;

import java.math.BigInteger;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.GateCoinEndpoint;
import network.minter.explorer.models.CoinItem;
import network.minter.explorer.models.GateResult;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class GateCoinRepository extends DataRepository<GateCoinEndpoint> implements DataRepository.Configurator {
    public GateCoinRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Observable<GateResult<CoinItem>> getCoinInfo(String coinName) {
        return getInstantService().getCoinInfo(coinName);
    }

    public Observable<GateResult<CoinItem>> getCoinInfo(BigInteger coinId) {
        return getInstantService().getCoinInfoById(coinId.toString());
    }

    public Observable<Boolean> coinExists(BigInteger coinId) {
        return getCoinInfo(coinId)
                .switchMap(result -> Observable.just(result.isOk()));
    }

    public Observable<Boolean> coinExists(String coinName) {
        return getCoinInfo(coinName)
                .switchMap(result -> Observable.just(result.isOk()));
    }

    @Nonnull
    @Override
    protected Class<GateCoinEndpoint> getServiceClass() {
        return GateCoinEndpoint.class;
    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(new TypeToken<GateResult<CoinItem>>() {
        }.getType(), new GateResult.Deserializer<>(CoinItem.class));
    }
}
