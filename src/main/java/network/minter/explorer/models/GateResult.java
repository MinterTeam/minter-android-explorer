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

import network.minter.blockchain.models.BCResult;

/**
 * minter-android-explorer. 2019
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class GateResult<Result> {

    @SerializedName("data")
    public Result result;
    public ErrorResult error;
    public int statusCode;

    public static <T> GateResult<T> copyError(GateResult<?> another) {
        GateResult<T> out = new GateResult<>();
        out.statusCode = another.statusCode;
        out.error = another.error;

        return out;
    }

    public boolean isOk() {
        return error == null;
    }

    public String getMessage() {
        if (error == null) {
            return null;
        }

        return error.message;
    }

    public static class ErrorResult {
        public int code;

        @SerializedName("log")
        public String message;
        public String value;
        public String coin;

        public String getMessage() {
            return message;
        }

        public BCResult.ResultCode getResultCode() {
            return BCResult.ResultCode.findByCode(code);
        }
    }
}
