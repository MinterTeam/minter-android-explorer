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

import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class CoinDelegation extends BaseCoinValue {
    public MinterPublicKey publicKey = null;
    public ValidatorMeta meta = null;

    public CoinDelegation(String coin, BigDecimal amount, BigDecimal bipValue) {
        this.coin = coin;
        this.amount = amount;
        this.bipValue = bipValue;
    }

    public CoinDelegation(String coin, BigDecimal amount, BigDecimal bipValue, MinterPublicKey publicKey, ValidatorMeta meta) {
        this.coin = coin;
        this.amount = amount;
        this.bipValue = bipValue;
        this.publicKey = publicKey;
        this.meta = meta;
    }

    public CoinDelegation() {
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoinDelegation)) return false;
        if (!super.equals(o)) return false;

        CoinDelegation that = (CoinDelegation) o;

        if (!bipValue.equals(that.bipValue)) return false;
        if (publicKey != null ? !publicKey.equals(that.publicKey) : that.publicKey != null)
            return false;
        return meta != null ? meta.equals(that.meta) : that.meta == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + bipValue.hashCode();
        result = 31 * result + (publicKey != null ? publicKey.hashCode() : 0);
        result = 31 * result + (meta != null ? meta.hashCode() : 0);
        return result;
    }
}
