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

import java.math.BigDecimal;

import javax.annotation.Nullable;

import network.minter.blockchain.models.BCResult;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class BCExplorerResult<Result> {
    public int statusCode = 200;
    @SerializedName("data")
    public Result result;
    public ErrorResult error;

    public static <T> BCExplorerResult<T> copyError(BCExplorerResult<?> another) {
        BCExplorerResult<T> out = new BCExplorerResult<>();
        out.statusCode = another.statusCode;
        out.error = another.error;

        return out;
    }

    public String getMessage() {
        if (error == null) {
            return null;
        }

        return error.message;
    }

    public BCResult.ResultCode getErrorCode() {
        if (error == null) {
            return BCResult.ResultCode.Success;
        }

        return BCResult.ResultCode.findByCode(error.code);
    }

    @Deprecated
    public boolean isSuccess() {
        return isOk();
    }

    public boolean isOk() {
        return statusCode == 200 && getErrorCode() == BCResult.ResultCode.Success;
    }

    public static class ErrorResult {
        @SerializedName("log")
        public String message;
        public int code;
        public String data;

        public BCResult.ResultCode getResultCode() {
            return BCResult.ResultCode.findByCode(code);
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        /**
         * Not null only on send transaction error caused by insufficient funds
         */
        @Nullable
        public String coin;
        /**
         * The same
         */
        @Nullable
        public BigDecimal value;
    }

}
