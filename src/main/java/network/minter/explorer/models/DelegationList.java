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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class DelegationList {
    public Map<MinterPublicKey, List<CoinDelegation>> delegations = new HashMap<>();

    public int size() {
        return delegations.size();
    }

    public boolean isEmpty() {
        return delegations.isEmpty();
    }

    public List<CoinDelegation> getDelegatedByPublicKey(MinterPublicKey publicKey) {
        if (!hasDelegations(publicKey)) {
            return Collections.emptyList();
        }
        return delegations.get(publicKey);
    }

    public boolean hasDelegations(MinterPublicKey publicKey) {
        return publicKey != null && delegations.containsKey(publicKey) && delegations.get(publicKey) != null;
    }
}
