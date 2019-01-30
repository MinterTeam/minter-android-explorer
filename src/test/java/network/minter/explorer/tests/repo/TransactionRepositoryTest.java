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
import java.util.List;
import java.util.TimeZone;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.log.StdLogger;
import network.minter.explorer.MinterExplorerApi;
import network.minter.explorer.models.ExpResult;
import network.minter.explorer.models.HistoryTransaction;
import network.minter.explorer.repo.ExplorerTransactionRepository;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
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
        see address=Mx06431236daf96979aa6cdf470a7df26430ad8efb.json
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
        11 CreateMultisigAddress(TxCreateMultisigResult.class),
        12 EditCandidate(TxEditCandidateResult.class),
        -- MultiSend(TxMultisendResult.class), no example
         */
        ExpResult.Meta meta = response.body().meta;
        List<HistoryTransaction> transactions = response.body().result;

        assertNotNull(meta);
        assertNotNull(transactions);

        assertEquals(1, meta.currentPage);
        assertEquals(1, meta.from);
        assertEquals(4, meta.to);
        assertEquals(1, meta.lastPage);
        assertEquals(50, meta.perPage);
        assertEquals(13, meta.total);
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
        testCreateMultisigAddress(transactions.get(11));

        // EDIT CANDIDATE
        testEditCandidate(transactions.get(12));


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

    @Test
    public void getTransactionsCount() {

    }

    @Test
    public void sendTransaction() {

    }

    @Test
    public void getTransactionCommission() {

    }

    private void testSend(HistoryTransaction tx) {
        assertEquals(new BigInteger("212"), tx.getTxNumber());
        assertEquals(new MinterHash("Mta4f6524a6c65fa95ba4c92343554995b932235ca5974c2beb6c74b66416b76b0"), tx.getHash());
        assertEquals(new BigInteger("105"), tx.getNonce());
        assertEquals(new BigInteger("684"), tx.getBlock());

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
                Date tmp = sdf.parse("2019-01-28T16:35:33.000+00:00");
                parsed = tmp;
            } catch (Throwable t) {
            }
        }
        assertNotNull(parsed);

        assertEquals(parsed, tx.getTimestamp());

        assertEquals(new BigDecimal("0.010000000000000000"), tx.getFee());
        assertEquals(HistoryTransaction.Type.Send, tx.getType());
        assertEquals(HistoryTransaction.Status.Success, tx.getStatus());
        assertEquals("", tx.getPayload());
        assertNotNull(tx.getData());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxSendCoinResult);

        assertEquals(new MinterAddress("Mx184ac726059e43643e67290666f7b3195093f870"), tx.getFrom());

        HistoryTransaction.TxSendCoinResult data = tx.getData();
        assertEquals(new MinterAddress("Mx06431236daf96979aa6cdf470a7df26430ad8efb"), data.getTo());
        assertEquals(new BigDecimal("100.000000000000000000"), data.getAmount());
        assertEquals("MNT", data.getCoin());
        assertNotEquals("mnt", data.getCoin());
    }

    private void testSell(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.SellCoin, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertTrue(tx.getData() instanceof HistoryTransaction.TxConvertCoinResult);
        HistoryTransaction.TxConvertCoinResult data = tx.getData();

        assertEquals("MNT", data.getCoinToSell());
        assertEquals("PROBLKCH01", data.getCoinToBuy());
        assertEquals(new BigDecimal("7440.111883339234899552"), data.getValueToBuy());
        assertEquals(new BigDecimal("1.000000000000000000"), data.getValueToSell());
    }

    private void testSellAll(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.SellAllCoins, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertTrue(tx.getData() instanceof HistoryTransaction.TxConvertCoinResult);
        HistoryTransaction.TxConvertCoinResult data = tx.getData();

        assertEquals("MNT", data.getCoinToSell());
        assertEquals("PROBLKCH01", data.getCoinToBuy());
        assertEquals(new BigDecimal("1650.801469331639573424"), data.getValueToBuy());
        assertEquals(new BigDecimal("212.501285841088945766"), data.getValueToSell());
    }

    private void testBuy(HistoryTransaction tx) {
        /*
        {
      "txn": 158224,
      "hash": "Mtc0743a44fd569b6cddae4eb15acf2bf5b15fcb623bb7361c212a8d2fa95c6875",
      "nonce": 20,
      "block": 34110,
      "timestamp": "2019-01-30T15:52:55.000+00:00",
      "fee": "0.100000000000000000",
      "type": "buyCoin",
      "status": "success",
      "payload": "",
      "from": "Mx9f1431b9781aafa2b348979b5cdfc6bab4db7829",
      "data": {
        "coin_to_sell": "MNT",
        "coin_to_buy": "EVSCAPITAL",
        "value": "107.920000000000000000",
        "value_to_buy": "107.920000000000000000",
        "value_to_sell": "7118.908775317248217994",
        "from": "Mx9f1431b9781aafa2b348979b5cdfc6bab4db7829"
      }
         */
        assertEquals(HistoryTransaction.Type.BuyCoin, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx9f1431b9781aafa2b348979b5cdfc6bab4db7829"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxConvertCoinResult);
        HistoryTransaction.TxConvertCoinResult data = tx.getData();

        assertEquals("MNT", data.getCoinToSell());
        assertEquals("EVSCAPITAL", data.getCoinToBuy());
        assertEquals(new BigDecimal("107.920000000000000000"), data.getValueToBuy());
        assertEquals(new BigDecimal("7118.908775317248217994"), data.getValueToSell());
    }

    private void testCreateCoin(HistoryTransaction tx) {
        /*
        {
      "txn": 155043,
      "hash": "Mtae3fe193a3958cbcfa5cf79380fe2c05935f4a59fa372e05f12fa642a96b3c4f",
      "nonce": 1311,
      "block": 29006,
      "timestamp": "2019-01-30T08:43:33.000+00:00",
      "fee": "1.000000000000000000",
      "type": "createCoin",
      "status": "success",
      "payload": "",
      "from": "Mx601609b85ee21b9493dffbca1079c74d47b75f2a",
      "data": {
        "name": "problkch03",
        "symbol": "PROBLKCH03",
        "initial_amount": "100000000.000000000000000000",
        "initial_reserve": "100.000000000000000000",
        "constant_reserve_ratio": "10",
        "from": "Mx601609b85ee21b9493dffbca1079c74d47b75f2a"
      }
    },
         */

        assertEquals(HistoryTransaction.Type.CreateCoin, tx.getType());
        assertEquals(new BigDecimal("1.000000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx601609b85ee21b9493dffbca1079c74d47b75f2a"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxCreateResult);
        HistoryTransaction.TxCreateResult data = tx.getData();

        assertEquals("problkch03", data.getName());
        assertEquals("PROBLKCH03", data.getSymbol());
        assertEquals(new BigDecimal("100000000.000000000000000000"), data.getInitialAmount());
        assertEquals(new BigDecimal("100.000000000000000000"), data.getInitialReserve());
        assertEquals(10, data.getConstantReserveRatio());

    }

    private void testDeclareCandidacy(HistoryTransaction tx) {
        /*
        {
      "txn": 155814,
      "hash": "Mt3a4a0de6310df6819163997d476deea3938be2e2a7373789171519831371df48",
      "nonce": 32,
      "block": 30160,
      "timestamp": "2019-01-30T10:20:15.000+00:00",
      "fee": "10.028000000000000000",
      "type": "declareCandidacy",
      "status": "success",
      "payload": "custom message",
      "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
      "data": {
        "address": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
        "pub_key": "Mp3bbccaaf3caed847b41d15e5f8c511278a9ddb0067b11e09d609de963f809d8d",
        "commission": "50",
        "coin": "MNT",
        "stake": "1.000000000000000000",
        "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16"
      }
    },
         */

        assertEquals(HistoryTransaction.Type.DeclareCandidacy, tx.getType());
        assertEquals(new BigDecimal("10.028000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxDeclareCandidacyResult);
        HistoryTransaction.TxDeclareCandidacyResult data = tx.getData();

        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), data.getAddress());
        assertEquals(new MinterPublicKey("Mp3bbccaaf3caed847b41d15e5f8c511278a9ddb0067b11e09d609de963f809d8d"), data.getPublicKey());
        assertEquals(50, data.getCommission());
        assertEquals("MNT", data.getCoin());
        assertEquals(new BigDecimal("1.000000000000000000"), data.getStake());
    }

    private void testDelegate(HistoryTransaction tx) {
        assertEquals(HistoryTransaction.Type.Delegate, tx.getType());
        assertEquals(new BigDecimal("0.268000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertTrue(tx.getData() instanceof HistoryTransaction.TxDelegateUnbondResult);
        assertEquals("Validator.Center Autodelegate #0.8", tx.getPayload());
        HistoryTransaction.TxDelegateUnbondResult data = tx.getData();

        assertEquals(
                new MinterPublicKey("Mp4de057441c02bc6d18ddf335a7b65dc8d09b59e14e0811a884a621e7c70e05ae"),
                data.getPublicKey()
        );
        assertEquals("MNT", data.getCoin());
        assertEquals(new BigDecimal("385.656000000000000000"), data.getStake());
    }

    private void testUnbond(HistoryTransaction tx) {
        /*
        {
      "txn": 157122,
      "hash": "Mte306859c5d59617a66bc3dd13d057bc4a3b40e3be85c14637504a987ab0ab8a8",
      "nonce": 24,
      "block": 32327,
      "timestamp": "2019-01-30T13:22:42.000+00:00",
      "fee": "0.200000000000000000",
      "type": "unbond",
      "status": "success",
      "payload": "",
      "from": "Mxd9db5a5d74856d155cd7ef0fed98c8b759a37bb9",
      "data": {
        "pub_key": "Mpd7a5ea77e1072dc722d212764943895441681483747a05e62835bd12532c4ee6",
        "coin": "MNT",
        "stake": "4397.000000000000000000",
        "from": "Mxd9db5a5d74856d155cd7ef0fed98c8b759a37bb9"
      }
    },
         */

        assertEquals(HistoryTransaction.Type.Unbond, tx.getType());
        assertEquals(new BigDecimal("0.200000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mxd9db5a5d74856d155cd7ef0fed98c8b759a37bb9"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxDelegateUnbondResult);
        HistoryTransaction.TxDelegateUnbondResult data = tx.getData();

        assertEquals(new MinterPublicKey("Mpd7a5ea77e1072dc722d212764943895441681483747a05e62835bd12532c4ee6"), data.getPublicKey());
        assertEquals("MNT", data.getCoin());
        assertEquals(new BigDecimal("4397.000000000000000000"), data.getStake());
    }

    private void testRedeemCheck(HistoryTransaction tx) {
        /*
        {
      "txn": 155838,
      "hash": "Mt0cf97f7f2c8ddad52dd6e952a4c0fa759090f31062e89fddcce5171f74ba3d08",
      "nonce": 44,
      "block": 30190,
      "timestamp": "2019-01-30T10:22:47.000+00:00",
      "fee": "0.030000000000000000",
      "type": "redeemCheckData",
      "status": "success",
      "payload": "",
      "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
      "data": {
        "check": {
          "coin": "MNT",
          "nonce": 1,
          "value": "0.27442917",
          "sender": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
          "due_block": 999999999
        },
        "raw_check": "f8a001843b9ac9ff8a4d4e54000000000000008803cef7e04c59c51cb841654259796e1a58adfe2e98db69c24cafdf7e98e68df069194a591d0ad0c12970499d144385c10bf55f6dc350639f7db00e33d4959796f27c22084d4b21b2135b011ba074c550975b9c7af76d423621d0ad0357145c41026fefb79a4a6efbcf16d9a919a066b0e6ec56ef3d9ea9ef0bbf30fe01d5bf9316df33fddbc97d43b6120b14ebc4",
        "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16"
      }
    },
         */

        assertEquals(HistoryTransaction.Type.RedeemCheck, tx.getType());
        assertEquals(new BigDecimal("0.030000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxRedeemCheckResult);
        HistoryTransaction.TxRedeemCheckResult data = tx.getData();

        assertNotNull(data.getCheck());
        assertEquals(
                new MinterCheck("f8a001843b9ac9ff8a4d4e54000000000000008803cef7e04c59c51cb841654259796e1a58adfe2e98db69c24cafdf7e98e68df069194a591d0ad0c12970499d144385c10bf55f6dc350639f7db00e33d4959796f27c22084d4b21b2135b011ba074c550975b9c7af76d423621d0ad0357145c41026fefb79a4a6efbcf16d9a919a066b0e6ec56ef3d9ea9ef0bbf30fe01d5bf9316df33fddbc97d43b6120b14ebc4"),
                data.getRawCheck()
        );

        HistoryTransaction.CheckData checkData = data.getCheck();
        assertEquals("MNT", checkData.getCoin());
        assertEquals(new BigInteger("1"), checkData.getNonce());
        assertEquals(new BigDecimal("0.27442917"), checkData.getValue());
        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), checkData.getSender());
        assertEquals(new BigInteger("999999999"), checkData.getDueBlock());
    }

    private void testSetCandidateOnline(HistoryTransaction tx) {
        /*
        {
      "txn": 156865,
      "hash": "Mtdc5e7e83a28fe48d1a8f8928e9e040d5828d14e2e38c87b3c7016c289e12e034",
      "nonce": 3,
      "block": 31928,
      "timestamp": "2019-01-30T12:49:07.000+00:00",
      "fee": "0.100000000000000000",
      "type": "setCandidateOnData",
      "status": "success",
      "payload": "",
      "from": "Mx3bdee0d64fa9ac892720f48724ef6a4e2919a6ba",
      "data": {
        "pub_key": "Mp1523aba429a126c2d51c3fc85c9f589970f4b25a7afab9787a3bc8f76306cf4b",
        "from": "Mx3bdee0d64fa9ac892720f48724ef6a4e2919a6ba"
      }
    },
         */

        assertEquals(HistoryTransaction.Type.SetCandidateOnline, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx3bdee0d64fa9ac892720f48724ef6a4e2919a6ba"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxSetCandidateOnlineOfflineResult);
        HistoryTransaction.TxSetCandidateOnlineOfflineResult data = tx.getData();

        assertEquals(new MinterPublicKey("Mp1523aba429a126c2d51c3fc85c9f589970f4b25a7afab9787a3bc8f76306cf4b"), data.getPublicKey());
    }

    private void testSetCandidateOffline(HistoryTransaction tx) {
        /*
        {
      "txn": 156382,
      "hash": "Mt56c0806aa6c60ac738b877874ad10dc013d3e55ddcc1a7267dc236b5ce00ea2b",
      "nonce": 1176,
      "block": 31104,
      "timestamp": "2019-01-30T11:39:58.000+00:00",
      "fee": "0.100000000000000000",
      "type": "setCandidateOffData",
      "status": "success",
      "payload": "",
      "from": "Mxfc7281e9e1429c57d6cf02f193e9082e68dc052d",
      "data": {
        "pub_key": "Mpeadea542b99de3b414806b362910cc518a177f8217b8452a8385a18d1687a80b",
        "from": "Mxfc7281e9e1429c57d6cf02f193e9082e68dc052d"
      }
    },
         */
        assertEquals(HistoryTransaction.Type.SetCandidateOffline, tx.getType());
        assertEquals(new BigDecimal("0.100000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mxfc7281e9e1429c57d6cf02f193e9082e68dc052d"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxSetCandidateOnlineOfflineResult);
        HistoryTransaction.TxSetCandidateOnlineOfflineResult data = tx.getData();

        assertEquals(new MinterPublicKey("Mpeadea542b99de3b414806b362910cc518a177f8217b8452a8385a18d1687a80b"), data.getPublicKey());
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

    private void testMultisend(HistoryTransaction tx) {
        // @TODO
    }

    private void testEditCandidate(HistoryTransaction tx) {
        /*
        {
      "txn": 155819,
      "hash": "Mte26ee3a9a08d95c2afcf7d462cb2294edcab745bd41bbd8f79548825aa9a7768",
      "nonce": 34,
      "block": 30165,
      "timestamp": "2019-01-30T10:20:41.000+00:00",
      "fee": "10.028000000000000000",
      "type": "editCandidate",
      "status": "success",
      "payload": "custom message",
      "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
      "data": {
        "pub_key": "Mp3bbccaaf3caed847b41d15e5f8c511278a9ddb0067b11e09d609de963f809d8d",
        "reward_address": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
        "owner_address": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
        "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16"
      }
    }
         */

        assertEquals(HistoryTransaction.Type.EditCandidate, tx.getType());
        assertEquals(new BigDecimal("10.028000000000000000"), tx.getFee());
        assertNotNull(tx);
        assertEquals(new MinterAddress("Mx7633980c000139dd3bd24a3f54e06474fa941e16"), tx.getFrom());
        assertTrue(tx.getData() instanceof HistoryTransaction.TxEditCandidateResult);
        HistoryTransaction.TxEditCandidateResult data = tx.getData();
        //@TODO!
        // miss again
        //assertEquals(new MinterPublicKey("Mp3bbccaaf3caed847b41d15e5f8c511278a9ddb0067b11e09d609de963f809d8d"), data.getPublicKey());
    }
}
