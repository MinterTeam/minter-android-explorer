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

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigDecimal;

/**
 * minter-android-explorer. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ExpResult<Result> {
    @SerializedName("data")
    public Result result;
    public Object links;
    public Meta meta;
    public int code;
    public BCExplorerResult.ErrorResult error;

    public Meta getMeta() {
        if (meta == null) {
            meta = new Meta();
        }

        return meta;
    }

    public boolean isOk() {
        return error == null;
    }

    /*
    "current_page":1,
    "last_page":3714,
    "path":"explorer-api.testnet.minter.network/api/v1/transactions",
    "per_page":50,
    "total":185690
     */
    @Parcel
    public static class Meta {
        @SerializedName("current_page")
        public int currentPage;
        @SerializedName("last_page")
        public int lastPage;
        public String path;
        @SerializedName("per_page")
        public int perPage;
        public int total;
        public Additional additional;

        @Deprecated
        public int from;
        @Deprecated
        public int to;

        @Parcel
        public static class Additional {
            @SerializedName("total_delegated_bip_value")
            public BigDecimal delegatedAmount;
        }

    }

}
