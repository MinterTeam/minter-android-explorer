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

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class ExpResult<Result> {
    @SerializedName("data")
    public Result result;
    public Object links;
    public Meta meta;
    @SerializedName("latest_block_time")
    public Date latestBlockTime;
    public ErrorResult error;

    public static <T> ExpResult<T> copyError(ExpResult<?> another) {
        ExpResult<T> out = new ExpResult<>();
        out.error = another.error;
        return out;
    }

    public Meta getMeta() {
        if (meta == null) {
            meta = new Meta();
        }

        return meta;
    }

    public String getMessage() {
        if (error == null) return null;
        return error.message;
    }

    public int getCode() {
        if (error == null) return 0;

        return error.code;
    }

    public boolean isOk() {
        return error == null;
    }


    public static class ErrorResult {
        public int code;
        public String message;
        public Map<String, String> fields;

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }
    }


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


        public static class Additional {
            @SerializedName("total_delegated_bip_value")
            public BigDecimal delegatedAmount;
        }

    }

}
