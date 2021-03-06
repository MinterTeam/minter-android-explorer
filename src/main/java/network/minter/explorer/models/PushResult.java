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

import org.parceler.Parcel;

import java.math.BigInteger;
import java.util.Map;

import network.minter.blockchain.models.NodeResult;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class PushResult extends NodeResult {

    public TxData transaction;
    public MinterHash hash;

    @Parcel
    public static class TxData {
        public BigInteger nonce;
        public MinterHash hash;
        @SerializedName("raw_tx")
        public String rawTx;
        public BigInteger height;
        public MinterAddress from;
        @SerializedName("gas_coin")
        public BigInteger gasCoinId;
        public Map<String, String> tags;

        public Transaction decodeRawTx() {
            return Transaction.fromEncoded(rawTx);
        }
    }

}
