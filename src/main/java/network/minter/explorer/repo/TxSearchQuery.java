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

package network.minter.explorer.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-explorer. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public final class TxSearchQuery {
    private long mBlock = -1;
    private long mPage = -1;
    private long mPerPage = -1;
    private List<MinterAddress> mAddressList = new ArrayList<>(0);
    private List<MinterHash> mHashList = new ArrayList<>(0);
    private List<MinterPublicKey> mPubKeyList = new ArrayList<>(0);

    public TxSearchQuery setBlock(long block) {
        mBlock = block;
        return this;
    }

    public TxSearchQuery addAddress(MinterAddress address) {
        mAddressList.add(address);
        return this;
    }

    public TxSearchQuery addAddress(String address) {
        mAddressList.add(new MinterAddress(address));
        return this;
    }

    public TxSearchQuery addHash(MinterHash hash) {
        mHashList.add(hash);
        return this;
    }

    public TxSearchQuery addHash(String hash) {
        mHashList.add(new MinterHash(hash));
        return this;
    }

    public TxSearchQuery addPublicKey(MinterPublicKey pubKey) {
        mPubKeyList.add(pubKey);
        return this;
    }

    public TxSearchQuery addPublicKey(String pubKey) {
        mPubKeyList.add(new MinterPublicKey(pubKey));
        return this;
    }

    public TxSearchQuery setPage(long pageNum) {
        mPage = pageNum;
        return this;
    }

    public TxSearchQuery setLimit(long limit) {
        mPerPage = limit;
        return this;
    }

    Map<String, Object> build() {
        Map<String, Object> out = new HashMap<>();
        if (mBlock >= 0) {
            out.put("block", String.valueOf(mBlock));
        }
        if (mPage >= 0) {
            out.put("page", mPage);
        }
        if (mPerPage >= 0) {
            out.put("perPage", mPerPage);
        }
        if (!mAddressList.isEmpty()) {
            if (mAddressList.size() == 1) {
                out.put("address", mAddressList.get(0).toString());
            } else {
                int i = 0;
                for (MinterAddress address : mAddressList) {
                    out.put("addresses[" + i + "]", address.toString());
                    i++;
                }
            }
        }

        if (!mHashList.isEmpty()) {
            if (mHashList.size() == 1) {
                out.put("hash", mHashList.get(0).toString());
            } else {
                int i = 0;
                for (MinterHash hash : mHashList) {
                    out.put("hashes[" + i + "]", hash.toString());
                    i++;
                }
            }
        }

        if (!mPubKeyList.isEmpty()) {
            if (mPubKeyList.size() == 1) {
                out.put("pubKey", mPubKeyList.get(0).toString());
            } else {
                int i = 0;
                for (MinterPublicKey pk : mPubKeyList) {
                    out.put("pubKeys[" + i + "]", pk.toString());
                    i++;
                }
            }
        }

        return out;
    }
}
