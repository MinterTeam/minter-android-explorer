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

package network.minter.explorer.tests.repo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.explorer.MinterExplorerApi;
import network.minter.explorer.models.AddressData;
import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.DelegationInfo;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.repo.ExplorerAddressRepository;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@SuppressWarnings("ConstantConditions")

public class AddressRepositoryTest extends BaseRepoTest {

    static {
        MinterExplorerApi.initialize(true);
//        MinterExplorerApi.getInstance().getApiService().addHttpInterceptor(new ApiMockInterceptor());
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getBalanceForSingleAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();

        Response<BCExplorerResult<AddressData>> result = addressRepository.getAddressData("Mx601609b85ee21b9493dffbca1079c74d47b75f2a").execute();

        checkResponseSuccess(result);

        AddressData data = result.body().result;
        assertEquals(2, data.coins.size());
        assertNotNull(data.address);

        assertNotNull(data.coins.get("PROBLKCH02"));
        AddressData.CoinBalance b1 = data.coins.get("PROBLKCH02");
        assertEquals("PROBLKCH02", b1.getCoin());
        assertEquals(new BigDecimal("165695681186885.069475041566967905"), b1.getAmount());

        assertNotNull(data.coins.get("MNT"));
        AddressData.CoinBalance b2 = data.coins.get("MNT");
        assertEquals("MNT", b2.getCoin());
        assertEquals(new BigDecimal("0e-18"), b2.getAmount());


        assertEquals(new BigDecimal("165695681186885.069475041566967905"), data.getTotalBalance());
    }

    @Test
    public void getBalanceForSingleObjectAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();

        Response<BCExplorerResult<AddressData>> result = addressRepository.getAddressData(new MinterAddress("Mx601609b85ee21b9493dffbca1079c74d47b75f2a")).execute();

        checkResponseSuccess(result);
    }

    @Test
    public void getBalanceForNullAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();

        IllegalArgumentException ex = null;
        try {
            String address = null;
            Response<BCExplorerResult<AddressData>> result = addressRepository.getAddressData(address).execute();
            checkResponseSuccess(result);
        } catch (IllegalArgumentException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getBalanceForMultipleAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);
        addressList.add(new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb"));
        addressList.add(new MinterAddress("Mx0000000000000000000000000000000000000000"));
        Response<BCExplorerResult<List<AddressData>>> result = addressRepository.getAddressesData(addressList).execute();

        checkResponseSuccess(result);
        BCExplorerResult<List<AddressData>> data = result.body();
        assertEquals(2, data.result.size());
    }

    @Test
    public void getBalanceForMultipleNullAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);
        addressList.add(null);
        addressList.add(null);

        IllegalArgumentException ex = null;
        try {
            Response<BCExplorerResult<List<AddressData>>> result = addressRepository.getAddressesData(addressList).execute();
            checkResponseSuccess(result);
        } catch (IllegalArgumentException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getBalanceForMultipleOneNullAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);
        addressList.add(new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb"));
        addressList.add(null);

        Response<BCExplorerResult<List<AddressData>>> result = addressRepository.getAddressesData(addressList).execute();
        checkResponseSuccess(result);

        List<AddressData> dataList = result.body().result;
        assertEquals(1, dataList.size());

        AddressData data = dataList.get(0);
        assertNotNull(data);

        assertNotNull(data.address);


        assertNotNull(data.coins.get("BTCSECURE"));
        AddressData.CoinBalance b1 = data.coins.get("BTCSECURE");
        assertEquals("BTCSECURE", b1.getCoin());
        assertEquals(new BigDecimal("1.970000000000000000"), b1.getAmount());

        assertNotNull(data.coins.get("MNT"));
        AddressData.CoinBalance b2 = data.coins.get("MNT");
        assertEquals("MNT", b2.getCoin());
        assertEquals(new BigDecimal("899.590579976503661317"), b2.getAmount());


        assertEquals(new BigDecimal("901.560579976503661317"), data.getTotalBalance());
    }

    @Test
    public void getBalanceForMultipleNullListAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        final List<MinterAddress> addressList = null;

        NullPointerException ex = null;
        try {
            Response<BCExplorerResult<List<AddressData>>> result = addressRepository.getAddressesData(addressList).execute();
            checkResponseSuccess(result);
        } catch (NullPointerException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getBalanceForMultipleEmptyListAddresses() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        final List<MinterAddress> addressList = new ArrayList<>(2);

        Throwable ex = null;
        try {
            Response<BCExplorerResult<List<AddressData>>> result = addressRepository.getAddressesData(addressList).execute();
            checkResponseSuccess(result);
        } catch (IllegalArgumentException e) {
            ex = e;
        }

        assertNotNull(ex);
    }

    @Test
    public void getEmptyDelegations() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        MinterAddress address = new MinterAddress("Mx0000000000000000000000000000000000000000");
        Response<ExpResult<List<DelegationInfo>>> result = addressRepository.getDelegations(address).execute();

        checkResponseSuccess(result);
        ExpResult<List<DelegationInfo>> data = result.body();
        assertEquals(0, data.result.size());

    }

    @Test
    public void getNullAddressDelegations() {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        MinterAddress address = null;
        Throwable ex = null;

        try {
            Response<ExpResult<List<DelegationInfo>>> result = addressRepository.getDelegations(address).execute();

            checkResponseSuccess(result);
            ExpResult<List<DelegationInfo>> data = result.body();
            assertEquals(0, data.result.size());
        } catch (Throwable e) {
            ex = e;
        }

        assertNotNull(ex);

    }

    @Test
    public void getNonEmptyDelegations() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();
        MinterAddress address = new MinterAddress("Mx601609b85ee21b9493dffbca1079c74d47b75f2a");
        Response<ExpResult<List<DelegationInfo>>> result = addressRepository.getDelegations(address).execute();

        checkResponseSuccess(result);
        ExpResult<List<DelegationInfo>> data = result.body();
        assertEquals(2, data.result.size());


        DelegationInfo info1 = data.result.get(0);
        assertEquals("PROBLKCH02", info1.coin);
        assertEquals(new MinterPublicKey("Mpeb60e7864833a9fc1537795175bb2c4044518f96e68291cdb3c50a81a68d3a1c"), info1.pubKey);
        assertEquals(new BigDecimal("150000000000000.000000000000000000"), info1.value);

        DelegationInfo info2 = data.result.get(1);
        assertEquals("PROBLKCH01", info2.coin);
        assertEquals(new MinterPublicKey("Mpeb60e7864833a9fc1537795175bb2c4044518f96e68291cdb3c50a81a68d3a1c"), info2.pubKey);
        assertEquals(new BigDecimal("11000.000000000000000000"), info2.value);
    }

    @Test
    public void getBalanceForUnknownAddress() throws IOException {
        ExplorerAddressRepository addressRepository = MinterExplorerApi.getInstance().address();

        Response<BCExplorerResult<AddressData>> result = addressRepository.getAddressData("Mx0000000000000000000000000000000000000000").execute();

        checkResponseSuccess(result);
    }

}