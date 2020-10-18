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

package network.minter.explorer.tests.repo;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.internal.log.StdLogger;
import network.minter.explorer.MinterExplorerSDK;
import network.minter.explorer.models.AddressBalance;
import network.minter.explorer.models.AddressListBalances;
import network.minter.explorer.models.CoinBalance;
import network.minter.explorer.models.DelegationList;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.GasValue;
import network.minter.explorer.models.GateResult;
import network.minter.explorer.models.HistoryTransaction;
import network.minter.explorer.repo.ExplorerAddressRepository;
import network.minter.explorer.repo.ExplorerTransactionRepository;
import network.minter.explorer.repo.GateGasRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("ConstantConditions")
public class AddressRepositoryTest extends BaseRepoTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
        new MinterExplorerSDK.Setup()
                .setEnableDebug(true)
                .setLogger(new StdLogger())
                .init();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMinGas() {
        GateGasRepository repo = MinterExplorerSDK.getInstance().gas();
        GateResult<GasValue> gasResult = repo.getMinGas().blockingFirst();
        assertNotNull(gasResult.result);
        assertNull(gasResult.error);
    }

    @Test
    public void getBalanceForSingleAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();

        ExpResult<AddressBalance> body = addressRepository.getAddressData(new MinterAddress("Mxeeda61bbe9929bf883af6b22f5796e4b92563ba4"), true).blockingFirst();


        AddressBalance data = body.result;
        assertEquals(2, data.coins.size());
        assertNotNull(data.address);

        assertNotNull(data.coins.get("MNT"));
        CoinBalance b2 = data.coins.get("MNT");
        assertEquals("MNT", b2.coin);
    }

    //
    @Test
    public void testFindCoin() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();

        List<MinterAddress> addresses = new ArrayList<>();
        addresses.add(new MinterAddress("Mx8d008dffe2f9144a39a2094ebdedadad335e814f"));
        ExpResult<AddressListBalances> body = addressRepository.getAddressesData(addresses).blockingFirst();

        AddressListBalances data = body.result;
    }

    @Test
    public void getBalanceForSingleObjectAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();

        ExpResult<AddressBalance> result = addressRepository.getAddressData(new MinterAddress("Mxeeda61bbe9929bf883af6b22f5796e4b92563ba4")).blockingFirst();

        checkResponseSuccess(result);
    }

    @Test
    public void getBalanceForNullAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();

        IllegalArgumentException ex = null;
        try {
            String address = null;
            ExpResult<AddressBalance> result = addressRepository.getAddressData(address).blockingFirst();
            checkResponseSuccess(result);
        } catch (IllegalArgumentException e) {
            ex = e;
        }

        assertNotNull(ex);
    }


    @Test
    public void getBalanceForInvalidAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();

        Throwable ex = null;

        try {
            ExpResult<AddressBalance> result = addressRepository.getAddressData("MxZZ1609b85ee21b9493dffbca1079c74d47b75f2a").blockingFirst();
            assertFalse(result.isOk());
            assertNotNull(result.error);
            assertNotNull(result.error.message);
        } catch (Throwable e) {
            ex = e;
        }

        assertNotNull(ex);

    }

    @Test
    public void getBalanceForMultipleAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);
        addressList.add(new MinterAddress("Mxeeda61bbe9929bf883af6b22f5796e4b92563ba4"));
        addressList.add(new MinterAddress("Mx0000000000000000000000000000000000000000"));
        ExpResult<AddressListBalances> result = addressRepository.getAddressesData(addressList).blockingFirst();

        checkResponseSuccess(result);
        AddressListBalances data = result.result;
        assertEquals(2, data.size());
    }

    @Test
    public void getBalanceForMultipleNullAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);
        addressList.add(null);
        addressList.add(null);

        IllegalArgumentException ex = null;
        try {
            ExpResult<AddressListBalances> result = addressRepository.getAddressesData(addressList).blockingFirst();
            checkResponseSuccess(result);
        } catch (IllegalArgumentException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getBalanceForMultipleOneNullAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);
        addressList.add(new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb"));
        addressList.add(null);

        ExpResult<AddressListBalances> result = addressRepository.getAddressesData(addressList).blockingFirst();
        checkResponseSuccess(result);

        AddressListBalances dataList = result.result;
        assertEquals(1, dataList.size());

        AddressBalance data = dataList.get(0);
        assertNotNull(data);

        assertNotNull(data.address);

        assertNotNull(data.coins.get("MNT"));
        CoinBalance b2 = data.coins.get("MNT");
        assertEquals("MNT", b2.coin);
        assertEquals(new BigDecimal("0").stripTrailingZeros(), b2.amount.stripTrailingZeros());


        assertEquals(new BigDecimal("0"), data.totalBalance);
    }

    @Test
    public void getBalanceForMultipleNullListAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        final List<MinterAddress> addressList = null;

        NullPointerException ex = null;
        try {
            ExpResult<AddressListBalances> result = addressRepository.getAddressesData(addressList).blockingFirst();
            checkResponseSuccess(result);
        } catch (NullPointerException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getBalanceForMultipleEmptyListAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);

        Throwable ex = null;
        try {
            ExpResult<AddressListBalances> result = addressRepository.getAddressesData(addressList).blockingFirst();
            checkResponseSuccess(result);
        } catch (IllegalArgumentException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getEmptyDelegations() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        MinterAddress address = new MinterAddress("Mx0000000000000000000000000000000000000000");
        ExpResult<DelegationList> result = addressRepository.getDelegations(address).blockingFirst();

        checkResponseSuccess(result);
        ExpResult<DelegationList> data = result;
        assertEquals(0, data.result.size());

    }

    @Test
    public void getNullAddressDelegations() {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        MinterAddress address = null;
        Throwable ex = null;

        try {
            ExpResult<DelegationList> result = addressRepository.getDelegations(address).blockingFirst();

            checkResponseSuccess(result);
            ExpResult<DelegationList> data = result;
            assertEquals(0, data.result.size());
        } catch (Throwable e) {
            ex = e;
        }

        assertNotNull(ex);

    }

    @Test
    public void getNonEmptyDelegations() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();
        MinterAddress address = new MinterAddress("Mx8d008dffe2f9144a39a2094ebdedadad335e814f");
        ExpResult<DelegationList> result = addressRepository.getDelegations(address).blockingFirst();
        Gson gson = MinterExplorerSDK.getInstance().getGsonBuilder().create();

        Throwable t = null;
        try {
            // check for gson encode/decode
            DelegationList in = result.result;
            String res = gson.toJson(in);
            DelegationList out = gson.fromJson(res, DelegationList.class);
        } catch (Throwable e) {
            t = e;
        }

        assertNull(t);


        checkResponseSuccess(result);
//        ExpResult<List<CoinDelegation>> data = result.body();
//        assertEquals(2, data.result.size());
//
//
//        CoinDelegation info1 = data.result.get(0);
//        assertEquals("PROBLKCH02", info1.coin);
//        assertEquals(new MinterPublicKey("Mpeb60e7864833a9fc1537795175bb2c4044518f96e68291cdb3c50a81a68d3a1c"), info1.publicKey);
//        assertEquals(new BigDecimal("150000000000000.000000000000000000"), info1.amount);
//
//        CoinDelegation info2 = data.result.get(1);
//        assertEquals("PROBLKCH01", info2.coin);
//        assertEquals(new MinterPublicKey("Mpeb60e7864833a9fc1537795175bb2c4044518f96e68291cdb3c50a81a68d3a1c"), info2.publicKey);
//        assertEquals(new BigDecimal("11000.000000000000000000"), info2.value);
    }

    @Test
    public void getBalanceForUnknownAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerSDK.getInstance().address();

        ExpResult<AddressBalance> result = addressRepository.getAddressData("Mx0000000000000000000000000000000000000000").blockingFirst();

        checkResponseSuccess(result);
    }

    @Test
    public void getTransactions() throws IOException {
        ExplorerTransactionRepository txRepo = MinterExplorerSDK.getInstance().transactions();
        ExpResult<List<HistoryTransaction>> result = txRepo.getTransactions(new MinterAddress("Mxeeda61bbe9929bf883af6b22f5796e4b92563ba4")).blockingFirst();

        checkResponseSuccess(result);
        System.out.println();

    }

}