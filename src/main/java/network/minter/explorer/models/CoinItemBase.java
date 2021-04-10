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

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigInteger;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class CoinItemBase implements Comparable<CoinItemBase> {
    public BigInteger id;
    public String symbol;
    public CoinType type = CoinType.Coin;

    public CoinItemBase(BigInteger id, String symbol, CoinType type) {
        this.id = id;
        this.symbol = symbol;
        this.type = type;
    }

    public CoinItemBase() {
    }

    public CoinItemBase(BigInteger id, String symbol) {
        this.id = id;
        this.symbol = symbol;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CoinItemBase)) return false;
        CoinItemBase that = (CoinItemBase) o;
        return Objects.equal(id, that.id) &&
                Objects.equal(symbol, that.symbol) &&
                type == that.type;
    }

    @Override
    public String toString() {
        return symbol;
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(id, symbol, type);
    }

    public enum CoinType {
        @SerializedName("coin")
        Coin("coin"),
        @SerializedName("token")
        Token("token"),
        @SerializedName("pool_token")
        PoolToken("pool_token"),
        ;

        private final String value;

        CoinType(String sname) {
            value = sname;
        }

        public static CoinType findByName(String name) {
            for (CoinType c : CoinType.values()) {
                if (c.name().equals(name) || c.value.equals(name)) {
                    return c;
                }
            }

            return Coin;
        }
    }

    @Override
    public int compareTo(CoinItemBase o) {
        return id.compareTo(o.id);
    }
}
