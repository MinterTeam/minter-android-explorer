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

import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import network.minter.blockchain.models.ExchangeBuyValue;
import network.minter.blockchain.models.ExchangeSellValue;
import network.minter.core.internal.log.StdLogger;
import network.minter.explorer.MinterExplorerSDK;
import network.minter.explorer.models.CoinItem;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.GateResult;
import network.minter.explorer.repo.ExplorerCoinsRepository;
import network.minter.explorer.repo.GateEstimateRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-explorer. 2019
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class CoinsRepositoryTest extends BaseRepoTest {

    static {
        new MinterExplorerSDK.Setup()
                .setEnableDebug(true)
                .setLogger(new StdLogger())
                .init();
//        MinterExplorerSDK.getInstance().getApiService().addHttpInterceptor(new ApiMockInterceptor());
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void getAll() throws IOException {
        ExplorerCoinsRepository repo = MinterExplorerSDK.getInstance().coins();

        ExpResult<List<CoinItem>> result = repo.getAll().blockingFirst();
        checkResponseSuccess(result);
    }

//    @Test
//    public void getCinemacoin() throws IOException {
//        ExplorerCoinsRepository repo = MinterExplorerSDK.getInstance().coins();
//
//        ExpResult<List<CoinItem>> result = repo.search("cinemacoin").blockingFirst();
//        checkResponseSuccess(result);
//
//        assertNotNull(result.result);
//        List<CoinItem> items = result.result;
//        assertNotNull(items);
//        assertEquals(1, items.size());
//
//
//        CoinItem item0 = items.get(0);
//        assertNotNull(item0);
//
//        assertEquals("CINEMACOIN", item0.symbol);
//        assertEquals("CINEMACOIN", item0.name);
//        assertEquals(10, item0.crr);
//        assertEquals(new BigDecimal("100.000000000000000000"), item0.reserveBalance);
//        assertEquals(new BigDecimal("100000.000000000000000000"), item0.volume);
//    }

    @Test
    public void getUnknownCoin() throws IOException {
        ExplorerCoinsRepository repo = MinterExplorerSDK.getInstance().coins();

        ExpResult<List<CoinItem>> result = repo.search("unknown").blockingFirst();
        checkResponseSuccess(result);

        assertNotNull(result.result);
        List<CoinItem> items = result.result;
        assertNotNull(items);
        assertEquals(0, items.size());
    }

//    @Test
//    public void buyCoinCurrency() throws IOException {
//        GateEstimateRepository repo = MinterExplorerSDK.getInstance().estimate();
//        ExchangeBuyValue data =
//                repo.getCoinExchangeCurrencyToBuy("MNT", new BigDecimal("1"), "CINEMACOIN").blockingFirst();
//
//        assertTrue(data.isOk());
//
//        assertEquals(new BigInteger("25561747758752620"), data.willPay);
//        assertEquals(new BigInteger("100000000000000000"), data.commission);
//        assertEquals(new BigDecimal("100000000000000000").divide(Transaction.VALUE_MUL_DEC), data.getCommission());
//        assertEquals(new BigDecimal("25561747758752620").divide(Transaction.VALUE_MUL_DEC), data.getAmount());
//        assertEquals(
//                new BigDecimal("100000000000000000").divide(Transaction.VALUE_MUL_DEC)
//                        .add(new BigDecimal("25561747758752620").divide(Transaction.VALUE_MUL_DEC))
//                ,
//                data.getAmountWithCommission()
//        );
//    }

    @Test
    public void buyCoinWrongName() throws IOException {
        GateEstimateRepository repo = MinterExplorerSDK.getInstance().estimate();
        GateResult<ExchangeBuyValue> result =
                repo.getCoinExchangeCurrencyToBuy("MNT", new BigDecimal("1"), "MNT").blockingFirst();

        assertFalse(result.isOk());
        assertEquals(400, result.getCode());
        assertEquals("\"From\" coin equals to \"to\" coin", result.getMessage());

        // hmm...
//        assertNotEquals("\"From\" coin equals to \"to\" coin", error.getMessage());
//        assertEquals(BCResult.ResultCode.UnknownError, error.getResultCode());
    }

    @Test
    public void sellCoinCurrency() throws IOException {
        GateEstimateRepository repo = MinterExplorerSDK.getInstance().estimate();
        GateResult<ExchangeSellValue> data =
                repo.getCoinExchangeCurrencyToSell("MNT", new BigDecimal("1"), "BANANATEST").blockingFirst();


        assertEquals(new BigInteger("15000000000000000"), data.result.willGet);
        assertEquals(new BigInteger("100000000000000000"), data.result.commission);
        assertEquals(new BigDecimal("0.1"), data.result.getCommission());
        assertEquals(new BigDecimal("0.015"), data.result.getAmount());
        assertEquals(new BigDecimal("0.015").add(new BigDecimal("0.1")), data.result.getAmountWithCommission());
    }

    //    curl "http://node.chilinet.minter.network:28843/estimate_coin_sell?coin_id_to_sell=0&coin_id_to_buy=3&value_to_sell=1000000000000000000"
//    curl http://node.chilinet.minter.network:28843/estimate_coin_buy?coin_id_to_sell=0&coin_id_to_buy=3&value_to_buy=1000000000000000000
    @Test
    public void sellCoinWrongName() throws IOException {
        GateEstimateRepository repo = MinterExplorerSDK.getInstance().estimate();
        GateResult<ExchangeSellValue> result =
                repo.getCoinExchangeCurrencyToSell("MNT", new BigDecimal("1"), "MNT").blockingFirst();

        assertFalse(result.isOk());
        assertNotNull(result.error);
        assertEquals(400, result.getCode());
        assertEquals("\"From\" coin equals to \"to\" coin", result.getMessage());
    }
}
