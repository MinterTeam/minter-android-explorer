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

package network.minter.explorer.models;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class AddressBalance {
    public Map<String, CoinBalance> coins = new HashMap<>();
    // not null only if get list of balances by addresses
    public MinterAddress address = null;
    public BigDecimal totalBalance = BigDecimal.ZERO;
    public BigDecimal totalBalanceUSD = BigDecimal.ZERO;
    public BigDecimal availableBalanceBIP = BigDecimal.ZERO;
    public BigDecimal availableBalanceUSD = BigDecimal.ZERO;

    public AddressBalance() {
        fillDefaultsOnEmpty();
    }

    public AddressBalance(MinterAddress address) {
        this.address = address;
        fillDefaultsOnEmpty();
    }

    public void fillDefaultsOnEmpty() {
        if (coins.isEmpty()) {
            coins.put(MinterSDK.DEFAULT_COIN, new CoinBalance(MinterSDK.DEFAULT_COIN, BigDecimal.ZERO, BigDecimal.ZERO, address));
        }
    }

    public boolean isEmpty() {
        return coins.isEmpty();
    }

    public int size() {
        return coins.size();
    }

    public List<CoinBalance> getCoinsList() {
        return Stream.of(coins.values()).toList();
    }

    public Optional<CoinBalance> findCoinByName(String name) {
        if (name == null) {
            return Optional.empty();
        }

        return Stream.of(getCoinsList())
                .filter(v -> name.toUpperCase().equals(v.coin.toUpperCase()))
                .findFirst();
    }

    public boolean hasCoin(String name) {
        if (name == null) return false;

        return coins.containsKey(name) && coins.get(name) != null;
    }

    public CoinBalance getCoin(String name) {
        if (!hasCoin(name)) {
            return new CoinBalance(name, BigDecimal.ZERO, BigDecimal.ZERO, address);
        }
        return coins.get(name);
    }


    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressBalance)) return false;

        AddressBalance that = (AddressBalance) o;

        if (!coins.equals(that.coins)) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (!totalBalance.equals(that.totalBalance)) return false;
        if (!totalBalanceUSD.equals(that.totalBalanceUSD)) return false;
        if (!availableBalanceBIP.equals(that.availableBalanceBIP)) return false;
        return availableBalanceUSD.equals(that.availableBalanceUSD);
    }

    @Override
    public int hashCode() {
        int result = coins.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + totalBalance.hashCode();
        result = 31 * result + totalBalanceUSD.hashCode();
        result = 31 * result + availableBalanceBIP.hashCode();
        result = 31 * result + availableBalanceUSD.hashCode();
        return result;
    }
}
