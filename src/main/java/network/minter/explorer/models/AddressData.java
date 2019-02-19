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

package network.minter.explorer.models;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;

import static com.google.common.base.MoreObjects.firstNonNull;
import static java.math.BigDecimal.ZERO;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class AddressData {

    public Map<String, CoinBalance> coins;
    public long txCount;
    // not null only if get list of balances by addresses
    public MinterAddress address;

    public AddressData() {
        coins = new HashMap<>();
    }

    public Map<String, CoinBalance> getCoins() {
        if (coins == null) {
            coins = new HashMap<>();
        }

        return coins;
    }

    public void fillDefaultsOnEmpty() {
        if (getCoins().isEmpty()) {
            coins.put(MinterSDK.DEFAULT_COIN, new CoinBalance(MinterSDK.DEFAULT_COIN, ZERO, ZERO));
        }
    }

    public BigDecimal getTotalBalance() {
        if (getCoins().isEmpty()) {
            return ZERO;
        }

        // @TODO this is not so valid data for now, explorer doesn't know real value in base coin
        BigDecimal totalOut = ZERO;
        for (Map.Entry<String, CoinBalance> entry : getCoins().entrySet()) {
            totalOut = totalOut.add(entry.getValue().getBaseCoinAmount());
        }

        return totalOut;
    }

    @Parcel
    public static class CoinBalance {
        public String coin;
        public BigDecimal amount;
        public BigDecimal usdAmount;
        public BigDecimal baseCoinAmount;

        public CoinBalance() {
        }

        public CoinBalance(String coin, BigDecimal value, BigDecimal valueUsd) {
            this.coin = coin;
            this.amount = value;
            this.usdAmount = valueUsd;
        }

        public BigDecimal getBaseCoinAmount() {
            return firstNonNull(baseCoinAmount, ZERO);
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }
            return coin.toUpperCase();
        }

        public BigDecimal getAmount() {
            return firstNonNull(amount, ZERO);
        }

        public BigDecimal getUsdAmount() {
            return firstNonNull(usdAmount, ZERO);
        }
    }
}
