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

package network.minter.explorer.models;

import com.annimon.stream.Objects;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;

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
    public BigInteger block;
    public Date timestamp;
    public BigDecimal fee;
    public Type type;
    public MinterAddress from;
    @Transient
    public Object data;
    public String payload;
    @Deprecated
    public transient String username;
    @Deprecated
    public transient String avatarUrl;

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

    @Deprecated
    public String getAvatarUrl() {
        return avatarUrl;
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
        if (type != Type.Send) {
            return false;
        }

        return addressList.contains(this.<TxSendCoinResult>getData().to);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }

    @Deprecated
    public String getAvatar() {
        return avatarUrl;
    }

    @Deprecated
    public HistoryTransaction setAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    @Deprecated
    public HistoryTransaction setUsername(String username) {
        this.username = username;
        return this;
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
        CreateCoin(TxCreateResult.class),
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

    /**
     * Data model for sending transaction
     */
    @Parcel
    public static class TxSendCoinResult {
        public MinterAddress to;
        public String coin;
        @SerializedName("value")
        public BigDecimal amount;

        public MinterAddress getTo() {
            return to;
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }
            return coin.toUpperCase();
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }

    /**
     * Data model for creating coin transaction
     */
    @Parcel
    public static class TxCreateResult {
        public String name;
        public String symbol;
        @SerializedName("initial_amount")
        public BigDecimal initialAmount;
        @SerializedName("initial_reserve")
        public BigDecimal initialReserve;
        @SerializedName("constant_reserve_ratio")
        public int constantReserveRatio;

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

    /**
     * Data model for exchanging coins transaction
     */
    @Parcel
    public static class TxConvertCoinResult {
        @SerializedName("coin_to_sell")
        public String coinToSell;
        @SerializedName("coin_to_buy")
        public String coinToBuy;
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
            return coinToSell.toUpperCase();
        }

        public String getCoinToBuy() {
            if (coinToBuy == null) {
                return null;
            }
            return coinToBuy.toUpperCase();
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
    @Parcel
    public static class TxDeclareCandidacyResult {
        public MinterAddress address;
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public int commission;
        public String coin;
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
            return coin.toUpperCase();
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
    @Parcel
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
    @Parcel
    public static class TxDelegateUnbondResult {
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public String coin;
        public String stake;
        public String value;

        public MinterPublicKey getPublicKey() {
            return publicKey;
        }

        public BigDecimal getStake() {
            if (stake == null || stake.isEmpty()) {
                stake = "0";
            }

            return new BigDecimal(stake);
        }

        public BigDecimal getValue() {
            if (value == null || value.isEmpty()) {
                value = "0";
            }

            return new BigDecimal(value);
        }

        public String getCoin() {
            if (coin == null) {
                return null;
            }

            return coin.toUpperCase();
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
    @Parcel
    public static class TxRedeemCheckResult {
        @SerializedName("raw_check")
        public String rawCheck;
        public String proof;
        @Deprecated
        public CheckData check;

        public String getRawProof() {
            return proof;
        }

        @Deprecated
        public CheckData getCheck() {
            return check;
        }

        public String getRawCheck() {
            return rawCheck;
        }
    }

    @Parcel
    public static class CheckData {
        public String coin;
        public String nonce;
        public BigDecimal value;
        public MinterAddress sender;
        @SerializedName("due_block")
        public BigInteger dueBlock;

        public String getCoin() {
            return coin;
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

    @Parcel
    public static class TxCreateMultisigResult {
        public BigInteger threshold;
        public List<BigInteger> weights = new ArrayList<>();
        public List<MinterAddress> addresses = new ArrayList<>();
    }

    @Parcel
    public static class TxMultisendResult {
        @SerializedName("list")
        public List<TxSendCoinResult> items;
    }

    @Parcel
    public static class TxEditCandidateResult {
        @SerializedName("reward_address")
        public MinterAddress rewardAddress;
        @SerializedName("owner_address")
        public MinterAddress ownerAddress;
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;

        public MinterPublicKey getPublicKey() {
            return pubKey;
        }

        public MinterAddress getRewardAddress() {
            return rewardAddress;
        }

        public MinterAddress getOwnerAddress() {
            return ownerAddress;
        }
    }

    //@TODO standard! write to Lashin to fix this
    @Deprecated
    @Parcel
    public static class CandidateEditResult {
        @SerializedName("reward_address")
        public MinterAddress rewardAddress;
        @SerializedName("owner_address")
        public MinterAddress ownerAddress;
        @SerializedName("pub_key")
        public MinterPublicKey pubKey;
    }
}
