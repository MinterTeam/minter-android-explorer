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
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

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
    /**
     * Used more memory, but find method has big O(1)
     */
    public Map<BigInteger, CoinBalance> coinsById = new HashMap<>();
    // not null only if get list of balances by addresses
    public MinterAddress address = null;
    public BigDecimal totalBalance = BigDecimal.ZERO;
    public BigDecimal totalBalanceUSD = BigDecimal.ZERO;
    public BigDecimal stakeBalanceBIP = BigDecimal.ZERO;
    public BigDecimal stakeBalanceUSD = BigDecimal.ZERO;

    public AddressBalance() {
        fillDefaultsOnEmpty();
    }

    public AddressBalance(MinterAddress address) {
        this.address = address;
        fillDefaultsOnEmpty();
    }

    public void fillDefaultsOnEmpty() {
        if (coins.isEmpty()) {
            coins.put(MinterSDK.DEFAULT_COIN, new CoinBalance(
                    MinterSDK.DEFAULT_COIN_ID,
                    MinterSDK.DEFAULT_COIN,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    address
            ));
            coinsById.put(MinterSDK.DEFAULT_COIN_ID, new CoinBalance(
                    MinterSDK.DEFAULT_COIN_ID,
                    MinterSDK.DEFAULT_COIN,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    address
            ));
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

    /**
     * Find coin balance by coin name
     *
     * @param name coin name
     * @return optional value, empty if not found
     * @deprecated Use {@link #findCoin(String name)}
     */
    @Deprecated
    public Optional<CoinBalance> findCoinByName(String name) {
        return findCoin(name);
    }

    /**
     * Find coin balance by coin id
     *
     * @param id coin id
     * @return optional value, empty if not found
     * @deprecated Use {@link #findCoin(BigInteger id)}
     */
    @Deprecated
    public Optional<CoinBalance> findCoinById(BigInteger id) {
        return findCoin(id);
    }

    /**
     * Find coin balance by coin name
     *
     * @param name coin name
     * @return optional value, empty if not found
     */
    public Optional<CoinBalance> findCoin(String name) {
        if (name == null) {
            return Optional.empty();
        }

        return Stream.of(getCoinsList())
                .filter(v -> name.toUpperCase().equals(v.coin.symbol.toUpperCase()))
                .findFirst();
    }

    /**
     * Find coin balance by coin id
     *
     * @param id coin id
     * @return optional value, empty if not found
     */
    public Optional<CoinBalance> findCoin(BigInteger id) {
        if (id == null) {
            return Optional.empty();
        }

        if (coinsById.containsKey(id)) {
            return Optional.of(coinsById.get(id));
        }

        return Optional.empty();
    }

    public boolean hasCoin(String name) {
        if (name == null) return false;

        return coins.containsKey(name) && coins.get(name) != null;
    }

    public boolean hasCoin(BigInteger id) {
        return coinsById.containsKey(id);
    }

    /**
     * Return coin in non-null format. If coin not found, returns coin with name specified in argument and null coin id)
     *
     * @param name coin name
     * @return
     */
    public CoinBalance getCoin(String name) {
        if (!hasCoin(name)) {
            return new CoinBalance(null, name, BigDecimal.ZERO, BigDecimal.ZERO, address);
        }
        return coins.get(name);
    }

    /**
     * Return coin in non-null format. If coin not found, returns coin with name <UNKNOWN> and id specified in argument
     *
     * @param id coin id
     * @return
     */
    @Nonnull
    public CoinBalance getCoin(BigInteger id) {
        if (!hasCoin(id)) {
            return new CoinBalance(id, "<UNKNOWN>", BigDecimal.ZERO, BigDecimal.ZERO, address);
        }

        return findCoin(id).get();
    }


    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressBalance)) return false;

        AddressBalance that = (AddressBalance) o;

        if (!coins.equals(that.coins)) return false;
        if (!coinsById.equals(that.coinsById)) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (!totalBalance.equals(that.totalBalance)) return false;
        if (!totalBalanceUSD.equals(that.totalBalanceUSD)) return false;
        if (!stakeBalanceBIP.equals(that.stakeBalanceBIP)) return false;
        return stakeBalanceUSD.equals(that.stakeBalanceUSD);
    }

    @Override
    public int hashCode() {
        int result = coins.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + totalBalance.hashCode();
        result = 31 * result + totalBalanceUSD.hashCode();
        result = 31 * result + stakeBalanceBIP.hashCode();
        result = 31 * result + stakeBalanceUSD.hashCode();
        return result;
    }
}
