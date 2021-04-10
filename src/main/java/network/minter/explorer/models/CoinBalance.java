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


import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.core.crypto.MinterAddress;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class CoinBalance extends BaseCoinValue {
    public MinterAddress address;

    public CoinBalance() {
    }

    public CoinBalance(CoinItemBase coin, BigDecimal amount, BigDecimal bipValue, MinterAddress address) {
        this.coin = coin;
        this.amount = amount;
        this.bipValue = bipValue;
        this.address = address;
    }

    public CoinBalance(BigInteger coinId, String coin, BigDecimal amount, BigDecimal bipValue, MinterAddress address) {
        this.coin = new CoinItemBase(coinId, coin);
        this.amount = amount;
        this.bipValue = bipValue;
        this.address = address;
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoinBalance)) return false;
        if (!super.equals(o)) return false;

        CoinBalance that = (CoinBalance) o;

        return address != null ? address.equals(that.address) : that.address == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
