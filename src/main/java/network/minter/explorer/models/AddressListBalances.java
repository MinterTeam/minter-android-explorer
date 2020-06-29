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

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.core.crypto.MinterAddress;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class AddressListBalances {
    public List<AddressBalance> balances = new ArrayList<>();

    public AddressListBalances() {
    }

    public AddressListBalances(List<MinterAddress> addresses) {
        balances = Stream.of(addresses)
                .map(AddressBalance::new)
                .toList();
    }

    public int size() {
        return balances.size();
    }

    public boolean isEmpty() {
        return balances.isEmpty();
    }


    public AddressBalance get(int index) {
        return balances.get(index);
    }

    public Optional<AddressBalance> find(MinterAddress address) {
        return Stream.of(balances)
                .filter(item -> item.address != null)
                .filter(item -> item.address.equals(address))
                .findFirst();
    }

    @Nonnull
    public AddressBalance getBalance(MinterAddress address) {
        for (AddressBalance balance : balances) {
            if (balance.address.equals(address)) {
                return balance;
            }
        }

        return new AddressBalance(address);
    }

    public BigDecimal getCoinBalance(MinterAddress address, String coin) {
        if (address == null || coin == null) {
            throw new IllegalArgumentException("Address and coin can't be null");
        }

        return getBalance(address).getCoin(coin).amount;
    }
}
