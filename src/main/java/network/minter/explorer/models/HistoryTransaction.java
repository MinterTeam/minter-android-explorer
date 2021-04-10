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

import com.annimon.stream.Objects;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.utils.Base64UrlSafe;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.log.Mint;

/**
 * minter-android-explorer. 2018
 *
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 * <p>
 * Common data model for explorer transaction
 * @link <a href="https://testnet.explorer.minter.network/help/index.html">Swagger API doc</a>
 */
@Parcel
public class HistoryTransaction implements Serializable, Comparable<HistoryTransaction> {
    public BigInteger txn;
    public MinterHash hash;
    public BigInteger nonce;
    @SerializedName("height")
    public BigInteger block;
    public Date timestamp;
    public BigDecimal fee;
    @SerializedName("gas_coin")
    public CoinItemBase gasCoin;
    public Type type;
    public MinterAddress from;
    @Transient
    public Object data;
    public String payload;

    public Type getType() {
        return type;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public BigInteger getBlock() {
        return block;
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public BigInteger getTxNumber() {
        return txn;
    }

    public String getPayload() {
        return payload;
    }

    public MinterHash getHash() {
        return hash;
    }

    public MinterAddress getFrom() {
        return from;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public boolean isIncoming(List<MinterAddress> addressList) {
        if (type == Type.Send) {
            return addressList.contains(this.<TxSendCoinResult>getData().to);
        } else if (type == Type.MultiSend) {
            final TxMultisendResult res = getData();
            if (res.items == null || res.items.isEmpty()) {
                return false;
            }

            for (TxSendCoinResult sendData : res.items) {
                if (addressList.contains(sendData.to)) {
                    return true;
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryTransaction that = (HistoryTransaction) o;
        return block.equals(that.block) &&
                Objects.equals(fee, that.fee) &&
                Objects.equals(hash, that.hash) &&
                Objects.equals(nonce, that.nonce) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(type, that.type) &&
                Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, nonce, block, timestamp, fee, type, data);
    }

    @Override
    public int compareTo(@Nonnull HistoryTransaction o) {
        return o.timestamp.compareTo(timestamp);
    }

    public enum Status {
        @SerializedName("success")
        Success,
        @SerializedName("error")
        Error,
    }

    public enum Type {
        @SerializedName("1")
        Send(TxSendCoinResult.class),
        @SerializedName("2")
        SellCoin(TxConvertCoinResult.class),
        @SerializedName("3")
        SellAllCoins(TxConvertCoinResult.class),
        @SerializedName("4")
        BuyCoin(TxConvertCoinResult.class),
        @SerializedName("5")
        CreateCoin(TxCreateCoinResult.class),
        @SerializedName("6")
        DeclareCandidacy(TxDeclareCandidacyResult.class),
        @SerializedName("7")
        Delegate(TxDelegateUnbondResult.class),
        @SerializedName("8")
        Unbond(TxDelegateUnbondResult.class),
        @SerializedName("9")
        RedeemCheck(TxRedeemCheckResult.class),
        @SerializedName("10")
        SetCandidateOnline(TxSetCandidateOnlineOfflineResult.class),
        @SerializedName("11")
        SetCandidateOffline(TxSetCandidateOnlineOfflineResult.class),
        @SerializedName("12")
        CreateMultisigAddress(TxCreateMultisigResult.class),
        @SerializedName("13")
        MultiSend(TxMultisendResult.class),
        @SerializedName("14")
        EditCandidate(TxEditCandidateResult.class),
        /**
         * @since minter 1.2
         **/
        @SerializedName("15")
        SetHaltBlock(TxSetHaltBlockResult.class),
        @SerializedName("16")
        RecreateCoin(TxRecreateCoinResult.class),
        @SerializedName("17")
        EditCoinOwner(TxChangeCoinOwnerResult.class),
        @SerializedName("18")
        EditMultisig(TxEditMultisigResult.class),
        @SerializedName("19")
        PriceVote(TxPriceVoteResult.class),
        @SerializedName("20")
        EditCandidatePublicKey(TxEditCandidatePublicKeyResult.class),

        /**
         * @since minter 2.0
         */
        @SerializedName("21")
        AddLiquidity(TxAddLiquidityResult.class),
        @SerializedName("22")
        RemoveLiquidity(TxRemoveLiquidityResult.class),
        @SerializedName("23")
        SellSwapPool(TxSellSwapPoolResult.class),
        @SerializedName("24")
        BuySwapPool(TxBuySwapPoolResult.class),
        @SerializedName("25")
        SellAllSwapPool(TxSellAllSwapPoolResult.class),
        @SerializedName("26")
        EditCandidateCommission(TxEditCandidateCommissionResult.class),
        @SerializedName("27")
        MoveStake(TxMoveStakeResult.class),
        @SerializedName("28")
        MintToken(TxMintTokenResult.class),
        @SerializedName("29")
        BurnToken(TxBurnTokenResult.class),
        @SerializedName("30")
        CreateToken(TxCreateTokenResult.class),
        @SerializedName("31")
        RecreateToken(TxRecreateTokenResult.class),
        @SerializedName("32")
        VoteCommission(TxVoteCommissionResult.class),
        @SerializedName("33")
        VoteUpdate(TxVoteUpdateResult.class),
        @SerializedName("34")
        CreateSwapPool(TxCreateSwapPoolResult.class),
        ;

        Class<?> mCls;

        Type(Class<?> cls) {
            mCls = cls;
        }

        public Class<?> getCls() {
            return mCls;
        }
    }

    /**
     * Fallback data class
     */
    public static class TxDefaultResult {
    }

    public static class TxEditCandidatePublicKeyResult {
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        @SerializedName("new_pub_key")
        public MinterPublicKey newPublicKey;
    }


    public static class TxSetHaltBlockResult {
        public BigInteger height;
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
    }


    public static class TxEditMultisigResult extends TxCreateMultisigResult {

    }


    public static class TxPriceVoteResult {
        public BigInteger price;
    }

    /**
     * Data model for sending transaction
     */

    public static class TxSendCoinResult {
        public MinterAddress to;
        public CoinItemBase coin;
        @SerializedName("value")
        public BigDecimal amount;

        public MinterAddress getTo() {
            return to;
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }
            return coin.symbol;
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }


    /**
     * Data model for creating coin transaction
     */

    public static class TxCreateCoinResult {
        public String name;
        public String symbol;
        @SerializedName("initial_amount")
        public BigDecimal initialAmount;
        @SerializedName("initial_reserve")
        public BigDecimal initialReserve;
        @SerializedName("constant_reserve_ratio")
        public int constantReserveRatio;
        @SerializedName("max_supply")
        public BigDecimal maxSupply = BigDecimal.ZERO;

        public String getName() {
            return name;
        }

        public String getSymbol() {
            if (symbol == null) {
                return null;
            }

            return symbol.toUpperCase();
        }

        public BigDecimal getInitialAmount() {
            return initialAmount;
        }

        public BigDecimal getInitialReserve() {
            return initialReserve;
        }

        /**
         * @return Percent
         */
        public int getConstantReserveRatio() {
            return constantReserveRatio;
        }
    }


    public static class TxRecreateCoinResult extends TxCreateCoinResult {
    }


    public static class TxChangeCoinOwnerResult {
        public String symbol;
        @SerializedName("new_owner")
        public MinterAddress newOwner;
    }

    /**
     * Data model for exchanging coins transaction
     */

    public static class TxConvertCoinResult {
        @SerializedName("coin_to_sell")
        public CoinItemBase coinToSell;
        @SerializedName("coin_to_buy")
        public CoinItemBase coinToBuy;
        @SerializedName("value_to_buy")
        public BigDecimal valueToBuy;
        @SerializedName("value_to_sell")
        public BigDecimal valueToSell;
        @SerializedName("maximum_value_to_sell")
        public BigDecimal maxValueToSell;
        @SerializedName("minimum_value_to_buy")
        public BigDecimal minValueToBuy;

        public BigDecimal getMinValueToBuy() {
            return minValueToBuy;
        }

        public BigDecimal getMaxValueToSell() {
            return maxValueToSell;
        }

        public String getCoinToSell() {
            if (coinToSell == null) {
                return null;
            }
            return coinToSell.symbol;
        }

        public String getCoinToBuy() {
            if (coinToBuy == null) {
                return null;
            }
            return coinToBuy.symbol;
        }

        public BigDecimal getValueToBuy() {
            if (valueToBuy == null) {
                valueToBuy = BigDecimal.ZERO;
            }
            return valueToBuy;
        }

        public BigDecimal getValueToSell() {
            if (valueToSell == null) {
                valueToSell = BigDecimal.ZERO;
            }
            return valueToSell;
        }
    }

    /**
     * Data model for declaring validator candidacy
     */

    public static class TxDeclareCandidacyResult {
        public MinterAddress address;
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public int commission;
        public CoinItemBase coin;
        public BigDecimal stake;

        public MinterAddress getAddress() {
            return address;
        }

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }

        public int getCommission() {
            return commission;
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }
            return coin.symbol;
        }

        public BigDecimal getStake() {
            if (stake == null) {
                stake = BigDecimal.ZERO;
            }
            return stake;
        }
    }

    /**
     * Data model for enabling validator transaction
     */

    public static class TxSetCandidateOnlineOfflineResult {
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }
    }

    /**
     * Data model for delegating and unbonding transactions
     */

    public static class TxDelegateUnbondResult {
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public CoinItemBase coin;
        public BigDecimal value = BigDecimal.ZERO;

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }

        /**
         * Return delegated/unbonded stake
         *
         * @return human decimal value
         */
        public BigDecimal getValue() {
            return value;
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }

            return coin.symbol;
        }
    }

    /**
     * Data model for redeeming checks transactions
     * <p>
     * "check": {
     * "coin": "MNT",
     * "nonce": 1,
     * "value": "0.27442917",
     * "sender": "Mx7633980c000139dd3bd24a3f54e06474fa941e16",
     * "due_block": 999999999
     * },
     * "raw_check": "f8a001843b9ac9ff8a4d4e54000000000000008803cef7e04c59c51cb841654259796e1a58adfe2e98db69c24cafdf7e98e68df069194a591d0ad0c12970499d144385c10bf55f6dc350639f7db00e33d4959796f27c22084d4b21b2135b011ba074c550975b9c7af76d423621d0ad0357145c41026fefb79a4a6efbcf16d9a919a066b0e6ec56ef3d9ea9ef0bbf30fe01d5bf9316df33fddbc97d43b6120b14ebc4",
     * "from": "Mx7633980c000139dd3bd24a3f54e06474fa941e16"
     */

    public static class TxRedeemCheckResult {
        /**
         * Base64-encoded check data
         */
        @SerializedName("raw_check")
        public String rawCheck;
        /**
         * Base64-encoded check proof
         */
        public String proof;
        public CheckData check;

        public String getRawProof() {
            return proof;
        }

        public CheckData getCheck() {
            return check;
        }

        public String getRawCheck() {
            return rawCheck;
        }

        public CheckTransaction getCheckTransaction() {
            if (rawCheck.isEmpty()) {
                return null;
            }

            CheckTransaction check;
            try {
                final BytesData checkData = Base64UrlSafe.decode(rawCheck);
                check = CheckTransaction.fromEncoded(checkData.toHexString());
            } catch (Throwable t) {
                Mint.d("Unable to decode raw check: %s", rawCheck);
                check = null;
            }

            return check;
        }
    }


    public static class CheckData {
        public CoinItemBase coin;
        @SerializedName("gas_coin")
        public CoinItemBase gasCoin;
        /**
         * Base64-encoded nonce string
         */
        public String nonce;
        public BigDecimal value;
        public MinterAddress sender;
        @SerializedName("due_block")
        public BigInteger dueBlock;

        public String getCoin() {
            return coin.toString();
        }

        public String getNonce() {
            return nonce;
        }

        public MinterAddress getSender() {
            return sender;
        }

        public BigInteger getDueBlock() {
            return dueBlock;
        }

        public BigDecimal getValue() {
            return value;
        }
    }


    public static class TxCreateMultisigResult {
        public BigInteger threshold;
        public List<BigInteger> weights = new ArrayList<>();
        public List<MinterAddress> addresses = new ArrayList<>();
        @SerializedName("multisig_address")
        public MinterAddress multisigAddress;
    }


    public static class TxMultisendResult {
        @SerializedName("list")
        public List<TxSendCoinResult> items;

        @Nonnull
        public List<TxSendCoinResult> findByRecipient(@Nonnull MinterAddress address) {
            if (items == null || items.isEmpty()) {
                return Collections.emptyList();
            }
            List<TxSendCoinResult> out = new ArrayList<>(1);
            for (TxSendCoinResult item : items) {
                if (item.to.equals(address)) {
                    out.add(item);
                }
            }

            return out;
        }
    }


    public static class TxEditCandidateResult {
        @SerializedName("reward_address")
        public MinterAddress rewardAddress;
        @SerializedName("owner_address")
        public MinterAddress ownerAddress;
        @SerializedName("control_address")
        public MinterAddress controlAddress;
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }

        public MinterAddress getRewardAddress() {
            return rewardAddress;
        }

        public MinterAddress getOwnerAddress() {
            return ownerAddress;
        }

        public MinterAddress getControlAddress() {
            return controlAddress;
        }
    }

    public static class TxAddLiquidityResult {
        public CoinItemBase coin0;
        public CoinItemBase coin1;
        @SerializedName("pool_token")
        public CoinItemBase poolToken;
        public BigDecimal volume0;
        public BigDecimal volume1;
        @SerializedName("maximum_volume1")
        public BigDecimal maxVolume1;
        public BigDecimal liquidity;
    }

    public static class TxRemoveLiquidityResult {
        public CoinItemBase coin0;
        public CoinItemBase coin1;
        @SerializedName("pool_token")
        public CoinItemBase poolToken;
        public BigDecimal volume0;
        public BigDecimal volume1;
        @SerializedName("minimum_volume0")
        public BigDecimal minVolume0;
        @SerializedName("minimum_volume1")
        public BigDecimal minVolume1;
        public BigDecimal liquidity;
    }

    public static class TxConvertSwapPoolResult {
        public HistoryTransaction.Type type;
        // common fields, used in sell/sell all/buy results
        public List<CoinItemBase> coins;
        @SerializedName("value_to_sell")
        public BigDecimal valueToSell;
        @SerializedName("value_to_buy")
        public BigDecimal valueToBuy;

        // sell/sell all field
        @SerializedName("minimum_value_to_buy")
        public BigDecimal minValueToBuy;
        // buy field
        @SerializedName("maximum_value_to_buy")
        public BigDecimal maxValueToBuy;

        public CoinItemBase getCoinFirst() {
            if (coins.size() == 0) {
                return null;
            }
            return coins.get(0);
        }

        public CoinItemBase getCoinLast() {
            if (coins.size() == 0) {
                return null;
            }

            return coins.get(coins.size() - 1);
        }
    }

    public static class TxSellSwapPoolResult extends TxConvertSwapPoolResult {
    }

    public static class TxSellAllSwapPoolResult extends TxSellSwapPoolResult {
    }

    public static class TxBuySwapPoolResult extends TxConvertSwapPoolResult {
    }

    public static class TxCreateSwapPoolResult {
        public CoinItemBase coin0, coin1;
        @SerializedName("pool_token")
        public CoinItemBase poolToken;
        public BigDecimal volume0, volume1;
        public BigDecimal liquidity;
    }

    public static class TxEditCandidateCommissionResult {
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
        public Integer commission;
    }

    public static class TxMoveStakeResult {
        public MinterPublicKey from, to;
        public CoinItemBase coin;
        public BigDecimal stake;
    }

    public static class TxMintTokenResult {
        public CoinItemBase coin;
        public BigDecimal value;
    }

    public static class TxBurnTokenResult extends TxMintTokenResult {

    }

    public static class TxCreateTokenResult {
        public String name;
        public String symbol;
        @SerializedName("initial_amount")
        public BigDecimal initialAmount;
        @SerializedName("max_supply")
        public BigDecimal maxSupply;
        public Boolean mintable;
        public Boolean burnable;
    }

    public static class TxRecreateTokenResult extends TxCreateTokenResult {
    }

    public static class TxVoteCommissionResult {
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
        public BigInteger height;
        public CoinItemBase coin;

        @SerializedName("payload_byte")
        public BigInteger payloadByte;
        public BigInteger send;
        @SerializedName("buy_bancor")
        public BigInteger buyBancor;
        @SerializedName("sell_bancor")
        public BigInteger sellBancor;
        @SerializedName("sell_all_bancor")
        public BigInteger sellAllBancor;
        @SerializedName("buy_pool_base")
        public BigInteger buyPoolBase;
        @SerializedName("sell_pool_base")
        public BigInteger sellPoolBase;
        @SerializedName("sell_all_pool_base")
        public BigInteger sellAllPoolBase;
        @SerializedName("buy_pool_delta")
        public BigInteger buyPoolDelta;
        @SerializedName("sell_pool_delta")
        public BigInteger sellPoolDelta;
        @SerializedName("sell_all_pool_delta")
        public BigInteger sellAllPoolDelta;
        @SerializedName("create_ticker3")
        public BigInteger createTicker3;
        @SerializedName("create_ticker4")
        public BigInteger createTicker4;
        @SerializedName("create_ticker5")
        public BigInteger createTicker5;
        @SerializedName("create_ticker6")
        public BigInteger createTicker6;
        @SerializedName("create_ticker7_10")
        public BigInteger createTicker7to10;
        @SerializedName("create_coin")
        public BigInteger createCoin;
        @SerializedName("create_token")
        public BigInteger createToken;
        @SerializedName("recreate_coin")
        public BigInteger recreateCoin;
        @SerializedName("recreate_token")
        public BigInteger recreateToken;
        @SerializedName("declare_candidacy")
        public BigInteger declareCandidacy;
        @SerializedName("delegate")
        public BigInteger delegate;
        @SerializedName("unbond")
        public BigInteger unbond;
        @SerializedName("redeem_check")
        public BigInteger redeemCheck;
        @SerializedName("set_candidate_on")
        public BigInteger setCandidateOn;
        @SerializedName("set_candidate_off")
        public BigInteger setCandidateOff;
        @SerializedName("create_multisig")
        public BigInteger createMultisig;
        @SerializedName("multisend_base")
        public BigInteger multisendBase;
        @SerializedName("multisend_delta")
        public BigInteger multisendDelta;
        @SerializedName("edit_candidate")
        public BigInteger editCandidate;
        @SerializedName("set_halt_block")
        public BigInteger setHaltBlock;
        @SerializedName("edit_ticker_owner")
        public BigInteger editTickerOwner;
        @SerializedName("edit_multisig")
        public BigInteger editMultisig;
        @SerializedName("price_vote")
        public BigInteger priceVote;
        @SerializedName("edit_candidate_public_key")
        public BigInteger editCandidatePubKey;
        @SerializedName("create_swap_pool")
        public BigInteger createSwapPool;
        @SerializedName("add_liquidity")
        public BigInteger addLiquidity;
        @SerializedName("remove_liquidity")
        public BigInteger removeLiquidity;
        @SerializedName("edit_candidate_commission")
        public BigInteger editCandidateCommission;
        @SerializedName("move_stake")
        public BigInteger moveStake;
        @SerializedName("mint_token")
        public BigInteger mintToken;
        @SerializedName("burn_token")
        public BigInteger burnToken;
        @SerializedName("vote_commission")
        public BigInteger voteCommission;
        @SerializedName("vote_update")
        public BigInteger voteUpdate;
    }

    public class TxVoteUpdateResult {
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
        public BigInteger height;
        public String version;
    }
}
