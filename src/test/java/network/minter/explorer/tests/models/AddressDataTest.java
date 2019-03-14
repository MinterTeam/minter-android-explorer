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

package network.minter.explorer.tests.models;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import network.minter.core.MinterSDK;
import network.minter.explorer.models.AddressData;

import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-explorer. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressDataTest {

    @Test
    public void fillEmptyWithDefaultValue() {
        AddressData data = new AddressData();
        assertNotNull(data.coins); //empty default
        assertNull(data.address);
        Assert.assertEquals(new BigDecimal("0"), data.getTotalBalance());


        assertTrue(data.coins.isEmpty());
        data.fillDefaultsOnEmpty();

        assertNotNull(data.coins);
        assertNull(data.address);
        Assert.assertEquals(1, data.coins.size());

        AddressData.CoinBalance def = data.coins.get(MinterSDK.DEFAULT_COIN);
        assertNotNull(def);
        Assert.assertEquals(MinterSDK.DEFAULT_COIN, def.getCoin());
        Assert.assertEquals(def.getCoin(), def.coin);

        def.coin = null;
        assertNull(def.getCoin());

        def.coin = MinterSDK.DEFAULT_COIN.toLowerCase();
        Assert.assertEquals(MinterSDK.DEFAULT_COIN.toUpperCase(), def.getCoin());

        data.coins = null;
        assertEquals(ZERO, data.getTotalBalance());
        data.fillDefaultsOnEmpty();
        assertNotNull(data.coins);

        AddressData.CoinBalance nitem = data.coins.get(MinterSDK.DEFAULT_COIN);
        assertNotNull(nitem);
        nitem.amount = null;
        Assert.assertEquals(ZERO, nitem.getAmount());

        data.coins.clear();
        assertEquals(ZERO, data.getTotalBalance());
        data.fillDefaultsOnEmpty();

        data.coins = null;
        assertNotNull(data.getCoins());
        data.fillDefaultsOnEmpty();
        data.coins.put("a", new AddressData.CoinBalance(null, null));
        data.fillDefaultsOnEmpty();
        assertEquals(2, data.getCoins().size());
    }

    @Test
    public void parcelAndUnparcel() {
//        AddressData ad = new AddressData();
//        Parcelable ads = Parcels.wrap(ad);
//
//        Parcel parcel = Mockito.spy(Parcel.class);
//
//        AddressData$$Parcelable p = new AddressData$$Parcelable(ad);
//        p.getParcel();
//        p.writeToParcel(parcel, 0);
//        p.describeContents();


//        Bundle bundle = mock(Bundle.class);

//        when(bundle.putParcelable("test", ads))
    }
}
