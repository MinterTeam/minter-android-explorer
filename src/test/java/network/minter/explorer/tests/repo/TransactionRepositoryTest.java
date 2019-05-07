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


import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.log.StdLogger;
import network.minter.explorer.MinterExplorerApi;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;
import network.minter.explorer.repo.ExplorerTransactionRepository;
import network.minter.explorer.repo.TxSearchQuery;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-explorer. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TransactionRepositoryTest extends BaseRepoTest {

    static {
        MinterExplorerApi.initialize(true, new StdLogger());
        MinterExplorerApi.getInstance().getApiService().addHttpInterceptor(new ApiMockInterceptor());
    }

    @Test
    public void transactionsData() throws IOException {
        ExplorerTransactionRepository repo = MinterExplorerApi.getInstance().transactions();
        MinterAddress address = new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb");
        Response<ExpResult<List<HistoryTransaction>>> response = repo.getTransactions(address).execute();

        checkResponseSuccess(response);

        /*
        index | type
        see address=Mx601609b85ee21b9493dffbca1079c74d47b75f2a.json
         0 Send(TxSendCoinResult.class),
         1 SellCoin(TxConvertCoinResult.class),
         2 SellAllCoins(TxConvertCoinResult.class),
         3 BuyCoin(TxConvertCoinResult.class),
         4 CreateCoin(TxCreateResult.class),
         5 DeclareCandidacy(TxDeclareCandidacyResult.class),
         6 Delegate(TxDelegateUnbondResult.class),
         7 Unbond(TxDelegateUnbondResult.class),
         8 RedeemCheck(TxRedeemCheckResult.class),
         9 SetCandidateOnline(TxSetCandidateOnlineOfflineResult.class),
        10 SetCandidateOffline(TxSetCandidateOnlineOfflineResult.class),
        -- CreateMultisigAddress(TxCreateMultisigResult.class),
        11 EditCandidate(TxEditCandidateResult.class),
        -- MultiSend(TxMultisendResult.class), no example
         */
        ExpResult.Meta meta = response.body().meta;
        List<HistoryTransaction> transactions = response.body().result;

        assertNotNull(meta);
        assertNotNull(transactions);

        assertEquals(1, meta.currentPage);
        assertEquals(0, meta.from);
        assertEquals(0, meta.to);
        assertEquals(1, meta.lastPage);
        assertEquals(50, meta.perPage);
        assertEquals(12, meta.total);
        // @TODO fix it on backend!
        //assertEquals(BuildConfig.BASE_API_URL + "v1/transactions", meta.path);
        assertNotNull(meta.path);
        assertTrue(meta.path.contains("v1/transactions"));

        assertEquals(meta.total, transactions.size());

        // SEND COIN
        testSend(transactions.get(0));

        // SELL COIN
        testSell(transactions.get(1));

        // SELL ALL COIN
        testSellAll(transactions.get(2));

        // BUY
        testBuy(transactions.get(3));

        // CREATE COIN
        testCreateCoin(transactions.get(4));

        // DECLARE CANDIDACY
        testDeclareCandidacy(transactions.get(5));

        // DELEGATE
        testDelegate(transactions.get(6));

        // UNBOND
        testUnbond(transactions.get(7));

        // REDEEM CHECK
        testRedeemCheck(transactions.get(8));

        // SET CANDIDATE ONLINE
        testSetCandidateOnline(transactions.get(9));

        // SET CANDIDATE OFFLINE
        testSetCandidateOffline(transactions.get(10));

        // CREATE MULTISIG ADDRESS
//        testCreateMultisigAddress(transactions.get(11));

        // EDIT CANDIDATE
        testEditCandidate(transactions.get(11));


        assertNotEquals(transactions.get(0), transactions.get(1));
        assertEquals(transactions.get(2), transactions.get(2));
        assertEquals(transactions.get(2).hashCode(), transactions.get(2).hashCode());

        // DESC ordering
        assertEquals(1, transactions.get(0).compareTo(transactions.get(1)));
        assertEquals(-1, transactions.get(1).compareTo(transactions.get(0)));
    }

    @Test
    public void getTransactionsByMultipleAddresses() {
//        ExplorerTransactionRepository repo = MinterExplorerApi.getInstance().transactions();
//        MinterAddress address1 = new MinterAddress("Mx0acbd5df9bc4bdc9fcf2f87e8393907739401a27");
//        MinterAddress address2 = new MinterAddress("Mx91d56aa8bbcc796ed0183838d56600e7780e16f9");
//        TxSearchQuery query = new TxSearchQuery()
//                .addAddress(address1)
//                .addAddress(address2);
//
//        Response<ExpResult<List<HistoryTransaction>>> response = repo.getTransactions(query).execute();
//        checkResponseSuccess(response);
    }

    //    @Test
    public void getTransactionsCount() throws IOException {
        ExplorerTransactionRepository repo = MinterExplorerApi.getInstance().transactions();
        TxSearchQuery query = new TxSearchQuery();

        /*
        index | type
        see
         0 Send(TxSendCoinResult.class),
         1  SellCoin(TxConvertCoinResult.class),
         2 SellAllCoins(TxConvertCoinResult.class),
         3 BuyCoin(TxConvertCoinResult.class),
         4 CreateCoin(TxCreateResult.class),
         5 DeclareCandidacy(TxDeclareCandidacyResult.class),
         6 Delegate(TxDelegateUnbondResult.class),
         7 Unbond(TxDelegateUnbondResult.class),
         8 RedeemCheck(TxRedeemCheckResult.class),
         9 SetCandidateOnline(TxSetCandidateOnlineOfflineResult.class),
        10 SetCandidateOffline(TxSetCandidateOnlineOfflineResult.class),
        11 CreateMultisigAddress(TxCreateMultisigResult.class),
        12 MultiSend(TxMultisendResult.class),
        13 EditCandidate(TxEditCandidateResult.class),
         */

        Map<String, HistoryTransaction.Type> required = new HashMap<String, HistoryTransaction.Type>() {{
            put("send", HistoryTransaction.Type.Send);
            put("sell", HistoryTransaction.Type.SellCoin);
            put("sellAll", HistoryTransaction.Type.SellAllCoins);
            put("buyCoin", HistoryTransaction.Type.BuyCoin);
            put("createCoin", HistoryTransaction.Type.CreateCoin);
            put("declareCandidacy", HistoryTransaction.Type.DeclareCandidacy);
            put("delegate", HistoryTransaction.Type.Delegate);
            put("unbond", HistoryTransaction.Type.Unbond);
            put("redeemCheckData", HistoryTransaction.Type.RedeemCheck);
            put("setCandidateOnData", HistoryTransaction.Type.SetCandidateOnline);
            put("setCandidateOffData", HistoryTransaction.Type.SetCandidateOffline);
            put("multiSig", HistoryTransaction.Type.CreateMultisigAddress);
            put("multiSend", HistoryTransaction.Type.MultiSend);
            put("editCandidate", HistoryTransaction.Type.EditCandidate);
        }};
        Map<String, String> typeMap = new HashMap<>();

        int page = 1;
        int lastPage = 0;
        while (page == 1 || page < lastPage) {
            System.err.println("Search in page: " + page);
            System.err.println("Left to find types: " + (required.size() - typeMap.size()));
            query.setPage(page);
            Response<ExpResult<List<HistoryTransaction>>> txResp = repo.getTransactions(query).execute();

            for (HistoryTransaction tx : txResp.body().result) {
                for (Map.Entry<String, HistoryTransaction.Type> entry : required.entrySet()) {
                    if (tx.getType() == entry.getValue() && !typeMap.containsKey(entry.getKey())) {
                        typeMap.put(entry.getKey(), tx.getHash().toString());
                        System.err.println("Found required TX: " + entry.getKey());
                    }
                }
            }

            if (typeMap.size() == required.size()) {
                break;
            }

            if (lastPage == 0) {
                lastPage = txResp.body().meta.lastPage;
            }
            page++;
        }

        for (Map.Entry<String, String> entry : typeMap.entrySet()) {
            System.out.println(entry.getKey() + ": https://explorer-api.testnet.minter.network/api/v1/transactions/" + entry.getValue());
        }
    }

    @Test
    public void sendTransaction() {

    }

    @Test
    public void getTransactionCommission() {

    }

    /*
    {
      "txn": 1988,
      "hash": "Mtc3217b9534f677a96bd360fde73e8684f9546efbcb57a6d2bf5879c20fca7166",
      "nonce": 2,
      "block": 2046,
      "timestamp": "2019-02-18T15:03:31+03:00",
      "fee": "0.010000000000000000",
      "type": 1,
      "payload": "",
      "from": "Mx06431236daf96979aa6cdf470a7df26430ad8efb",
      "data": {
        "coin": "BTCSECURE",
        "to": "Mx4c74fb299a1abc37c35e272c76484e0542790f4c",
        "value": "4721.908815059578837926"
      }
    }
     */
    private void testSend(HistoryTransaction tx) {
        assertEquals(new BigInteger("1988"), tx.getTxNumber());
        assertEquals(new MinterHash("Mtc3217b9534f677a96bd360fde73e8684f9546efbcb57a6d2bf5879c20fca7166"), tx.getHash());
        assertEquals(new BigInteger("2"), tx.getNonce());
        assertEquals(new BigInteger("2046"), tx.getBlock());

        List<String> knownPatterns = new ArrayList<>(Arrays.asList(
                "yyyy-MM-dd'T'HH:mm:ssZ",
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
                "yyyy-MM-dd' 'HH:mm:ss",
                "yyyy-MM-dd' 'HH:mm:ssX",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ssX"
        ));


        Date parsed = null;

        for (String sdfFormat : knownPatterns) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(sdfFormat);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date tmp = sdf.parse("2019-02-18T15:03:31+03:00");
                parsed = tmp;
            } catch (Throwable t) {
            }
        }
        assertNotNull(parsed);

        assertEquals(parsed, tx.getTimestamp());

        assertEquals(new BigDecimal("0.010000000000000000"), tx.getFee());
        assertEquals(HistoryTransaction.Type.Send, tx.getType());
//        assertEquals(HistoryTransaction.Status.Success, tx.getStatus());
        assertEquals("", tx.getPayload());
        assertNotNull(tx.getData());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxSendCoinResult);

        assertEquals(new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb"), tx.getFrom());

        HistoryTransaction.TxSendCoinResult data = tx.getData();
        assertEquals(new MinterAddress("Mx4c74fb299a1abc37c35e272c76484e0542790f4c"), data.getTo());
        assertEquals(new BigDecimal("4721.908815059578837926"), data.getAmount());
        assertEquals("BTCSECURE", data.getCoin());
        assertNotEquals("btcsecure", data.getCoin());
    }

    /*
    {
      "txn": 185395,
      "hash": "Mtaf65de532a143474eb89dafb7ed0f6edfa6d73aa2eb28c1681d2e8ec9e6867aa",
      "nonce": 3090,
      "block": 301407,
      "timestamp": "2019-03-07T16:10:57+03:00",
      "fee": "0.100000000000000000",
      "type": 2,
      "payload": "",
      "from": "Mx190fd0ad9fce4878ff7a6776a2692114e9838a34",
      "data": {
        "coin_to_sell": "MNT",
        "coin_to_buy": "MNTCOIN",
        "value_to_sell": "213.000000000000000000",
        "value_to_buy": "93.084382613165501195",
        "minimum_value_to_buy": "1.000000000000000000"
      }
    }
     */
    private void testSell(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.SellCoin, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertTrue(tx.getData() instanceof HistoryTransaction.TxConvertCoinResult);
        HistoryTransaction.TxConvertCoinResult data = tx.getData();

        assertEquals("MNT", data.getCoinToSell());
        assertEquals("MNTCOIN", data.getCoinToBuy());
        assertEquals(new BigDecimal("213.000000000000000000"), data.getValueToSell());
        assertEquals(new BigDecimal("93.084382613165501195"), data.getValueToBuy());
        assertEquals(new BigDecimal("1.000000000000000000"), data.getMinValueToBuy());
    }

    /*
    {
      "txn": 185447,
      "hash": "Mt8a015333abd6880b0c6bbf58cef2ef622df71cbb977a8c74307da929b1a58e8c",
      "nonce": 10754,
      "block": 301456,
      "timestamp": "2019-03-07T16:15:00+03:00",
      "fee": "0.100000000000000000",
      "type": 3,
      "payload": "",
      "from": "Mx601609b85ee21b9493dffbca1079c74d47b75f2a",
      "data": {
        "coin_to_sell": "MNT",
        "coin_to_buy": "PROBLKCH02",
        "value_to_sell": "34.495029769548421170",
        "value_to_buy": "1101001051.653216257156531170",
        "minimum_value_to_buy": "1.000000000000000000"
      }
    },
     */
    private void testSellAll(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.SellAllCoins, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertTrue(tx.getData() instanceof HistoryTransaction.TxConvertCoinResult);
        HistoryTransaction.TxConvertCoinResult data = tx.getData();

        assertEquals("MNT", data.getCoinToSell());
        assertEquals("PROBLKCH02", data.getCoinToBuy());
        assertEquals(new BigDecimal("34.495029769548421170"), data.getValueToSell());
        assertEquals(new BigDecimal("1101001051.653216257156531170"), data.getValueToBuy());
    }

    /*
    {
      "txn": 185027,
      "hash": "Mtae23edff662a4b05deb1149bfd1134b0799f5fb52e3ddcc821fe422e8b251b49",
      "nonce": 115,
      "block": 300687,
      "timestamp": "2019-03-07T15:11:35+03:00",
      "fee": "0.100000000000000000",
      "type": 4,
      "payload": "BUY COIN",
      "from": "Mxcc7715345914256d83cb078a55275a2c25d94083",
      "data": {
        "coin_to_buy": "BTCSECURE",
        "coin_to_sell": "MNT",
        "value_to_buy": "10005.000000000000000000",
        "value_to_sell": "1616.833656925202514515",
        "maximum_value_to_sell": "1778.517022617722765966"
      }
    }
     */
    private void testBuy(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.BuyCoin, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mxcc7715345914256d83cb078a55275a2c25d94083"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxConvertCoinResult);
        HistoryTransaction.TxConvertCoinResult data = tx.getData();

        assertEquals("BTCSECURE", data.getCoinToBuy());
        assertEquals("MNT", data.getCoinToSell());
        assertEquals(new BigDecimal("10005.000000000000000000"), data.getValueToBuy());
        assertEquals(new BigDecimal("1616.833656925202514515"), data.getValueToSell());
        assertEquals(new BigDecimal("1778.517022617722765966"), data.getMaxValueToSell());
    }

    /*
    {
      "txn": 173925,
      "hash": "Mt2200a6e5a59857ea07e307343cc792242315a5b4eb417e193c6c4efe06578179",
      "nonce": 218,
      "block": 279611,
      "timestamp": "2019-03-06T10:14:50+03:00",
      "fee": "1.000000000000000000",
      "type": 5,
      "payload": "",
      "from": "Mx3c57a889ec01714f26477f3758ee3b5c08bcabd3",
      "data": {
        "name": "CINEMACOIN",
        "symbol": "CINEMACOIN",
        "initial_amount": "100000.000000000000000000",
        "initial_reserve": "100.000000000000000000",
        "constant_reserve_ratio": "10"
      }
    }
     */
    private void testCreateCoin(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.CreateCoin, tx.getType());
        assertEquals(new BigDecimal("1.000000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx3c57a889ec01714f26477f3758ee3b5c08bcabd3"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxCreateResult);
        HistoryTransaction.TxCreateResult data = tx.getData();

        assertEquals("CINEMACOIN", data.getName());
        assertEquals("CINEMACOIN", data.getSymbol());
        assertEquals(new BigDecimal("100000.000000000000000000"), data.getInitialAmount());
        assertEquals(new BigDecimal("100.000000000000000000"), data.getInitialReserve());
        assertEquals(10, data.getConstantReserveRatio());

    }

    /*
    {
      "txn": 166833,
      "hash": "Mtd19ff45caf9646b5a623a61f54f9a1f9c532c0b530609889b556af9184de3a83",
      "nonce": 1,
      "block": 267744,
      "timestamp": "2019-03-05T17:53:16+03:00",
      "fee": "10.000000000000000000",
      "type": 6,
      "payload": "",
      "from": "Mx448ac01d947a753077d13267f5218850b37b6acc",
      "data": {
        "address": "Mx448ac01d947a753077d13267f5218850b37b6acc",
        "pub_key": "Mpc8903b84b3d6e967e902cfcf18764f853469c89dcf7f8ddf5739d9915d9d4678",
        "commission": "100",
        "coin": "MNT",
        "stake": "699990.000000000000000000"
      }
    }
     */
    private void testDeclareCandidacy(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.DeclareCandidacy, tx.getType());
        assertEquals(new BigDecimal("10.000000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx448ac01d947a753077d13267f5218850b37b6acc"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxDeclareCandidacyResult);
        HistoryTransaction.TxDeclareCandidacyResult data = tx.getData();

        assertEquals(new MinterAddress("Mx448ac01d947a753077d13267f5218850b37b6acc"), data.getAddress());
        assertEquals(new MinterPublicKey("Mpc8903b84b3d6e967e902cfcf18764f853469c89dcf7f8ddf5739d9915d9d4678"), data.getPublicKey());
        assertEquals(100, data.getCommission());
        assertEquals("MNT", data.getCoin());
        assertEquals(new BigDecimal("699990.000000000000000000"), data.getStake());
    }

    /*
    {
      "txn": 185446,
      "hash": "Mtb6adda32da97d1a546f716ab20bd16db3741ecd3c678823407da96df4a539fb5",
      "nonce": 2427,
      "block": 301456,
      "timestamp": "2019-03-07T16:15:00+03:00",
      "fee": "0.286000000000000000",
      "type": 7,
      "payload": "VHJhbnNhY3Rpb24gZnJvbSBNaW50ZXIuU3RvcmUgQXV0b0RlbGVnYXRvcg==",
      "from": "Mx46bbcdb16ca1e7fc3b87a232ba064a2c634e1a72",
      "data": {
        "pub_key": "Mpc8c6834da8ba2b0b24f7e5ab67049509278e709cde925f14184586f74dcc9d0b",
        "coin": "MNT",
        "stake": "14.753081260787814000"
      }
    }
     */
    private void testDelegate(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.Delegate, tx.getType());
        assertEquals(new BigDecimal("0.286000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertTrue(tx.getData() instanceof HistoryTransaction.TxDelegateUnbondResult);
        assertEquals("VHJhbnNhY3Rpb24gZnJvbSBNaW50ZXIuU3RvcmUgQXV0b0RlbGVnYXRvcg==", tx.getPayload());
        assertEquals(new MinterAddress("Mx46bbcdb16ca1e7fc3b87a232ba064a2c634e1a72"), tx.getFrom());
        HistoryTransaction.TxDelegateUnbondResult data = tx.getData();

        assertEquals(
                new MinterPublicKey("Mpc8c6834da8ba2b0b24f7e5ab67049509278e709cde925f14184586f74dcc9d0b"),
                data.getPublicKey()
        );
        assertEquals("MNT", data.getCoin());
        assertEquals(new BigDecimal("14.753081260787814000"), data.getStake());
    }

    /*
    {
      "txn": 185291,
      "hash": "Mt27015c2f205654fb1e0f40b956c668842d975ecd71bceff499163ec937af95f9",
      "nonce": 5,
      "block": 301137,
      "timestamp": "2019-03-07T15:48:42+03:00",
      "fee": "0.200000000000000000",
      "type": 8,
      "payload": "",
      "from": "Mx94b7f5ed30e93cde7048de4cb01ba2ea359402be",
      "data": {
        "pub_key": "Mp2f4d5478540c2ecd03d8c584f59f1e552353af043d981f27f39ac9de6b0f2c78",
        "coin": "BTCSECURE",
        "value": "316347.504000000000000000"
      }
    }
     */
    private void testUnbond(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.Unbond, tx.getType());
        assertEquals(new BigDecimal("0.200000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx94b7f5ed30e93cde7048de4cb01ba2ea359402be"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxDelegateUnbondResult);
        HistoryTransaction.TxDelegateUnbondResult data = tx.getData();

        assertEquals(new MinterPublicKey("Mp2f4d5478540c2ecd03d8c584f59f1e552353af043d981f27f39ac9de6b0f2c78"), data.getPublicKey());
        assertEquals("BTCSECURE", data.getCoin());
        assertEquals(new BigDecimal("0"), data.getStake());
        assertEquals(new BigDecimal("316347.504000000000000000"), data.getValue());
    }

    /*
    {
      "txn": 81313,
      "hash": "Mt90b35e9bd8c9c608904d1dac951fafbfe5deef68c2f436b04129897a1c133965",
      "nonce": 3,
      "block": 125546,
      "timestamp": "2019-02-25T14:40:54+03:00",
      "fee": "0.030000000000000000",
      "type": 9,
      "payload": "",
      "from": "Mx571f342776a83d28d07cf570035173aa2d3ae4f1",
      "data": {
        "raw_check": "+KQKiIrHIwSJ6AAAik1OVAAAAAAAAACIDeC2s6dkAAC4QRXA65+OFAZ93jgvLqCsbfaZ5U4BMzNyroQxtDXe6mwfD/xTCw/tL/3dMtVHAHaPlLwiK+w9pA4132uqaRb3XygBG6ArWUKZAToyuNKFvGuxke41oilwPFFrtMqlbyg+1gD9waBF4AcXdFyed0YeLFKwTpSgTw/f0e5M+mU10u3eSsMrlQ==",
        "proof": "TUi0VPX1V7jFllIyGVWSgCNpbFMBqldbT+/h2HSN88t0WBwUv46iKww0jKsmATqQA0T/gI9HqYImq/3sY3A4/gE="
      }
    }
     */
    private void testRedeemCheck(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.RedeemCheck, tx.getType());
        assertEquals(new BigDecimal("0.030000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx571f342776a83d28d07cf570035173aa2d3ae4f1"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxRedeemCheckResult);
        HistoryTransaction.TxRedeemCheckResult data = tx.getData();

        assertEquals("+KQKiIrHIwSJ6AAAik1OVAAAAAAAAACIDeC2s6dkAAC4QRXA65+OFAZ93jgvLqCsbfaZ5U4BMzNyroQxtDXe6mwfD/xTCw/tL/3dMtVHAHaPlLwiK+w9pA4132uqaRb3XygBG6ArWUKZAToyuNKFvGuxke41oilwPFFrtMqlbyg+1gD9waBF4AcXdFyed0YeLFKwTpSgTw/f0e5M+mU10u3eSsMrlQ==", data.getRawCheck());
        assertNull(data.getCheck());
        assertEquals("TUi0VPX1V7jFllIyGVWSgCNpbFMBqldbT+/h2HSN88t0WBwUv46iKww0jKsmATqQA0T/gI9HqYImq/3sY3A4/gE=", data.getRawProof());

//        HistoryTransaction.CheckData checkData = data.getCheck();
//        assertEquals("MNT", checkData.getCoin());
//        assertEquals(new BigInteger("1"), checkData.getNonce());
//        assertEquals(new BigDecimal("0.27442917"), checkData.getValue());
//        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), checkData.getSender());
//        assertEquals(new BigInteger("999999999"), checkData.getDueBlock());
    }

    /*
    {
      "txn": 173581,
      "hash": "Mte1d14abe75986bae6c092824622f34d66fe9f92e93ccd5a6802feacecbb48a83",
      "nonce": 2806,
      "block": 279089,
      "timestamp": "2019-03-06T09:31:47+03:00",
      "fee": "0.100000000000000000",
      "type": 10,
      "payload": "",
      "from": "Mxdce154b6e1d06b46e95881b900eeb164e247c180",
      "data": {
        "pub_key": "Mp04a63aebb316449169fabc79cff0d92efd890e42d55e432c2cc1a081436ac84b"
      }
    }
     */
    private void testSetCandidateOnline(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.SetCandidateOnline, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mxdce154b6e1d06b46e95881b900eeb164e247c180"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxSetCandidateOnlineOfflineResult);
        HistoryTransaction.TxSetCandidateOnlineOfflineResult data = tx.getData();

        assertEquals(new MinterPublicKey("Mp04a63aebb316449169fabc79cff0d92efd890e42d55e432c2cc1a081436ac84b"), data.getPublicKey());
    }

    /*
    {
      "txn": 173574,
      "hash": "Mte07c210f80ef43d6fafa56b6656f06db8db41a8db6f8381741ee4b12c262f813",
      "nonce": 2805,
      "block": 279081,
      "timestamp": "2019-03-06T09:31:08+03:00",
      "fee": "0.100000000000000000",
      "type": 11,
      "payload": "",
      "from": "Mxdce154b6e1d06b46e95881b900eeb164e247c180",
      "data": {
        "pub_key": "Mp04a63aebb316449169fabc79cff0d92efd890e42d55e432c2cc1a081436ac84b"
      }
    }
     */
    private void testSetCandidateOffline(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.SetCandidateOffline, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mxdce154b6e1d06b46e95881b900eeb164e247c180"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxSetCandidateOnlineOfflineResult);
        HistoryTransaction.TxSetCandidateOnlineOfflineResult data = tx.getData();

        assertEquals(new MinterPublicKey("Mp04a63aebb316449169fabc79cff0d92efd890e42d55e432c2cc1a081436ac84b"), data.getPublicKey());
    }

    private void testCreateMultisigAddress(HistoryTransaction tx) {
        /*
        {
      "txn": 155841,
      "hash": "Mt1abac0c24974de6a57d9423c6ece686b084ead1a87617a6a0e29e6173033f41e",
      "nonce": 46,
      "block": 30195,
      "timestamp": "2019-01-30T10:23:12.000+00:00",
      "fee": "0.100000000000000000",
      "type": "multiSig",
      "status": "success",
      "payload": "",
      "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
      "data": {
        "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16"
      }
    },
         */
        assertEquals(HistoryTransaction.Type.CreateMultisigAddress, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxCreateMultisigResult);
        HistoryTransaction.TxCreateMultisigResult data = tx.getData();

        // @TODO! explorer doesn't return:
        // public BigInteger threshold;
        //        public List<BigInteger> weights = new ArrayList<>();
        //        public List<MinterAddress> addresses = new ArrayList<>();
    }

    /*
    {
      "txn": 121723,
      "hash": "Mt9c56e26297062323d0a044e405060fcf37e3015f7a3fcd853941832d8d616f0f",
      "nonce": 2296,
      "block": 201988,
      "timestamp": "2019-03-01T23:27:28+03:00",
      "fee": "10.000000000000000000",
      "type": 14,
      "payload": "",
      "from": "Mx190fd0ad9fce4878ff7a6776a2692114e9838a34",
      "data": {
        "pub_key": "Mpa3fc6e42e7237dd8a4bb2d53e497896775d288c4d2ec1b05aa4b999c59a10d96",
        "reward_address": "Mx9cbad5974f9705ca013dedd367d837f1e46107a3",
        "owner_address": "Mx9cbad5974f9705ca013dedd367d837f1e46107a3"
      }
    }
     */
    private void testEditCandidate(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.EditCandidate, tx.getType());
        assertEquals(new BigDecimal("10.000000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx190fd0ad9fce4878ff7a6776a2692114e9838a34"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxEditCandidateResult);
        HistoryTransaction.TxEditCandidateResult data = tx.getData();
        assertEquals(new MinterPublicKey("Mpa3fc6e42e7237dd8a4bb2d53e497896775d288c4d2ec1b05aa4b999c59a10d96"), data.getPublicKey());
        assertEquals(new MinterAddress("Mx9cbad5974f9705ca013dedd367d837f1e46107a3"), data.getRewardAddress());
        assertEquals(new MinterAddress("Mx9cbad5974f9705ca013dedd367d837f1e46107a3"), data.getOwnerAddress());
    }
}
