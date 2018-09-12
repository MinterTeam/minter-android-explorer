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
import java.util.List;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import network.minter.explorer.api.ExplorerSettingsEndpoint;
import network.minter.explorer.models.BalanceChannel;
import network.minter.explorer.models.ExpResult;
import retrofit2.Call;

import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich [edward.vstock[at]gmail.com]
 */
public class ExplorerSettingsRepository extends DataRepository<ExplorerSettingsEndpoint> {
    public ExplorerSettingsRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get websocket balance update notification
     * @param addresses minter address to notify about
     * @param userId optional unique user id
     * @return Retrofit call
     */
    public Call<ExpResult<BalanceChannel>> getBalanceChannel(@NonNull List<MinterAddress> addresses, String userId) {
        checkNotNull(addresses, "Addresses can't be null");
        final List<String> addressStrings = new ArrayList<>(addresses.size());
        for (MinterAddress address : addresses) {
            addressStrings.add(address.toString());
        }
        if (userId == null) {
            return getInstantService().getBalanceChannel(addressStrings);
        }

        return getInstantService().getBalanceChannel(addressStrings, userId);
    }

    @NonNull
    @Override
    protected Class<ExplorerSettingsEndpoint> getServiceClass() {
        return ExplorerSettingsEndpoint.class;
    }
}
