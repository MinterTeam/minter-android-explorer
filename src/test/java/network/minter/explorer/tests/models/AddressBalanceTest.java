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

package network.minter.explorer.tests.models;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.log.StdLogger;
import network.minter.explorer.MinterExplorerSDK;
import network.minter.explorer.models.AddressBalance;
import network.minter.explorer.models.CoinBalance;
import network.minter.explorer.models.CoinDelegation;

import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-explorer. 2019
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressBalanceTest {

    @Test
    public void encodeGsonCoinDelegation() {
        new MinterExplorerSDK.Setup()
                .setExplorerApiUrl("https://explorer-api.chilinet.minter.network/api/")
                .setGateApiUrl("https://gate-api.chilinet.minter.network/api/")
                .setEnableDebug(true)
                .setLogger(new StdLogger())
                .init();

        Map<MinterPublicKey, List<CoinDelegation>> delegations = new HashMap<>();
        MinterPublicKey pk1 = new MinterPublicKey("Mp47f5c19c3ac5d66c960f36aa1a2d74ec8127f96308b67054332c988ee9eafaf0");
        MinterPublicKey pk2 = new MinterPublicKey("Mp57f5c19c3ac5d66c960f36aa1a2d74ec8127f96308b67054332c988ee9eafaf0");
        delegations.put(pk1, new ArrayList<>());
        delegations.put(pk2, new ArrayList<>());

        CoinDelegation v1 = new CoinDelegation(BigInteger.ZERO, "BIP", ZERO, ZERO);
        CoinDelegation v2 = new CoinDelegation(BigInteger.ZERO, "MNT", new BigDecimal("1"), new BigDecimal("1"));
        CoinDelegation v3 = new CoinDelegation(new BigInteger("1"), "ZERO", new BigDecimal("2"), new BigDecimal("2"));
        delegations.get(pk1).add(v1);
        delegations.get(pk2).add(v1);
        delegations.get(pk1).add(v2);
        delegations.get(pk2).add(v2);
        delegations.get(pk1).add(v3);
        delegations.get(pk2).add(v3);

        Gson gson = MinterExplorerSDK.getInstance().getGsonBuilder().create();
        String res = gson.toJson(delegations);


        CoinDelegation res2 = gson.fromJson(res, CoinDelegation.class);

        System.out.println(res);
    }

    @Test
    public void fillEmptyWithDefaultValue() {
        AddressBalance data = new AddressBalance();
        assertNotNull(data.coins); //empty default
        assertNull(data.address);
        Assert.assertEquals(new BigDecimal("0"), data.totalBalance);


        assertTrue(data.coins.isEmpty());
        data.fillDefaultsOnEmpty();

        assertNotNull(data.coins);
        assertNull(data.address);
        Assert.assertEquals(1, data.coins.size());

        CoinBalance def = data.coins.get(MinterSDK.DEFAULT_COIN);
        assertNotNull(def);
        Assert.assertEquals(MinterSDK.DEFAULT_COIN_ID, def.coin.id);
        Assert.assertEquals(def.coin, def.coin);

        def.coin = null;
        assertNull(def.coin);

        def.coin.symbol = (MinterSDK.DEFAULT_COIN.toLowerCase());
        Assert.assertEquals(MinterSDK.DEFAULT_COIN_ID, def.coin.id);

        assertEquals(ZERO, data.totalBalance);
        data.fillDefaultsOnEmpty();
        assertNotNull(data.coins);

        CoinBalance nitem = data.coins.get(MinterSDK.DEFAULT_COIN);
        assertNotNull(nitem);
        nitem.amount = (ZERO);
        Assert.assertEquals(ZERO, nitem.amount);

        data.coins.clear();
        assertEquals(ZERO, data.totalBalance);
        data.fillDefaultsOnEmpty();


        assertNotNull(data.coins);
        data.fillDefaultsOnEmpty();
        data.coins.put("a", new CoinBalance(null, null, ZERO, ZERO, null));
        data.fillDefaultsOnEmpty();
        assertEquals(2, data.coins.size());
    }

    @Test
    public void parcelAndUnparcel() {
//        AddressBalance ad = new AddressBalance();
//        Parcelable ads = Parcels.wrap(ad);
//
//        Parcel parcel = Mockito.spy(Parcel.class);
//
//        AddressBalance$$Parcelable p = new AddressBalance$$Parcelable(ad);
//        p.getParcel();
//        p.writeToParcel(parcel, 0);
//        p.describeContents();


//        Bundle bundle = mock(Bundle.class);

//        when(bundle.putParcelable("test", ads))
    }
}
