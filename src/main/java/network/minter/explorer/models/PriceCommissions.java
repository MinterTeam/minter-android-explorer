package network.minter.explorer.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.OperationType;

@Parcel
public class PriceCommissions {

    public CoinItemBase coin;

    @SerializedName("payload_byte")
    public BigInteger payloadByte = new BigInteger("2000000000000000");
    @SerializedName("send")
    public BigInteger send = new BigInteger("10000000000000000");
    @SerializedName("buy_bancor")
    public BigInteger buyBancor = new BigInteger("30000000000000000");
    @SerializedName("sell_bancor")
    public BigInteger sellBancor = new BigInteger("30000000000000000");
    @SerializedName("sell_all_bancor")
    public BigInteger sellAllBancor = new BigInteger("30000000000000000");
    @SerializedName("buy_pool_base")
    public BigInteger buyPoolBase = new BigInteger("30000000000000000");
    @SerializedName("buy_pool_delta")
    public BigInteger buyPoolDelta = new BigInteger("5000000000000000");
    @SerializedName("sell_pool_base")
    public BigInteger sellPoolBase = new BigInteger("30000000000000000");
    @SerializedName("sell_pool_delta")
    public BigInteger sellPoolDelta = new BigInteger("5000000000000000");
    @SerializedName("sell_all_pool_base")
    public BigInteger sellAllPoolBase = new BigInteger("30000000000000000");
    @SerializedName("sell_all_pool_delta")
    public BigInteger sellAllPoolDelta = new BigInteger("5000000000000000");
    @SerializedName("create_ticker3")
    public BigInteger createTicker3 = new BigInteger("100000000000000000000000");
    @SerializedName("create_ticker4")
    public BigInteger createTicker4 = new BigInteger("10000000000000000000000");
    @SerializedName("create_ticker5")
    public BigInteger createTicker5 = new BigInteger("1000000000000000000000");
    @SerializedName("create_ticker6")
    public BigInteger createTicker6 = new BigInteger("100000000000000000000");
    @SerializedName("create_ticker7_10")
    public BigInteger createTicker7To10 = new BigInteger("10000000000000000000");
    @SerializedName("create_coin")
    public BigInteger createCoin = new BigInteger("0");
    @SerializedName("create_token")
    public BigInteger createToken = new BigInteger("0");
    @SerializedName("recreate_coin")
    public BigInteger recreateCoin = new BigInteger("100000000000000000000");
    @SerializedName("recreate_token")
    public BigInteger recreateToken = new BigInteger("100000000000000000000");
    @SerializedName("declare_candidacy")
    public BigInteger declareCandidacy = new BigInteger("100000000000000000000");
    @SerializedName("delegate")
    public BigInteger delegate = new BigInteger("100000000000000000");
    @SerializedName("unbond")
    public BigInteger unbond = new BigInteger("100000000000000000");
    @SerializedName("redeem_check")
    public BigInteger redeemCheck = new BigInteger("30000000000000000");
    @SerializedName("set_candidate_on")
    public BigInteger setCandidateOn = new BigInteger("10000000000000000000");
    @SerializedName("set_candidate_off")
    public BigInteger setCandidateOff = new BigInteger("10000000000000000000");
    @SerializedName("create_multisig")
    public BigInteger createMultisig = new BigInteger("100000000000000000");
    @SerializedName("multisend_base")
    public BigInteger multisendBase = new BigInteger("10000000000000000");
    @SerializedName("multisend_delta")
    public BigInteger multisendDelta = new BigInteger("5000000000000000");
    @SerializedName("edit_candidate")
    public BigInteger editCandidate = new BigInteger("100000000000000000000");
    @SerializedName("set_halt_block")
    public BigInteger setHaltBlock = new BigInteger("10000000000000000");
    @SerializedName("edit_ticker_owner")
    public BigInteger editTickerOwner = new BigInteger("100000000000000000000");
    @SerializedName("edit_multisig")
    public BigInteger editMultisig = new BigInteger("10000000000000000");
    @SerializedName("edit_candidate_public_key")
    public BigInteger editCandidatePublicKey = new BigInteger("10000000000000000000000");
    @SerializedName("create_swap_pool")
    public BigInteger createSwapPool = new BigInteger("100000000000000000");
    @SerializedName("add_liquidity")
    public BigInteger addLiquidity = new BigInteger("30000000000000000");
    @SerializedName("remove_liquidity")
    public BigInteger removeLiquidity = new BigInteger("30000000000000000");
    @SerializedName("edit_candidate_commission")
    public BigInteger editCandidateCommission = new BigInteger("100000000000000000000");
    @SerializedName("mint_token")
    public BigInteger mintToken = new BigInteger("10000000000000000");
    @SerializedName("burn_token")
    public BigInteger burnToken = new BigInteger("10000000000000000");
    @SerializedName("vote_commission")
    public BigInteger voteCommission = new BigInteger("1000000000000000000");
    @SerializedName("vote_update")
    public BigInteger voteUpdate = new BigInteger("1000000000000000000");

    public BigInteger calculateCoinCost(String symbol) {
        if (symbol == null) {
            return BigInteger.ZERO;
        }

        BigInteger out;
        switch (symbol.length()) {
            case 3:
                out = createTicker3;
                break;
            case 4:
                out = createTicker4;
                break;
            case 5:
                out = createTicker5;
                break;
            case 6:
                out = createTicker6;
                break;
            default:
                out = createTicker7To10;
                break;
        }

        return out;
    }

    public BigInteger getByType(OperationType type) {
        BigInteger out;
        switch (type) {
            case SendCoin:
                out = send;
                break;
            case SellCoin:
                out = sellBancor;
                break;
            case SellAllCoins:
                out = sellAllBancor;
                break;
            case BuyCoin:
                out = buyBancor;
                break;
            case CreateCoin:
                out = createCoin;
                break;
            case DeclareCandidacy:
                out = declareCandidacy;
                break;
            case Delegate:
                out = delegate;
                break;
            case Unbound:
                out = unbond;
                break;
            case RedeemCheck:
                out = redeemCheck;
                break;
            case SetCandidateOnline:
                out = setCandidateOn;
                break;
            case SetCandidateOffline:
                out = setCandidateOff;
                break;
            case CreateMultisigAddress:
                out = createMultisig;
                break;
            case Multisend:
                out = multisendBase;
                break;
            case EditCandidate:
                out = editCandidate;
                break;

            case SetHaltBlock:
                out = setHaltBlock;
                break;
            case RecreateCoin:
                out = recreateCoin;
                break;
            case EditCoinOwner:
                out = editTickerOwner;
                break;
            case EditMultisig:
                out = editMultisig;
                break;
            case EditCandidatePublicKey:
                out = editCandidatePublicKey;
                break;

            case AddLiquidity:
                out = addLiquidity;
                break;
            case RemoveLiquidity:
                out = removeLiquidity;
                break;
            case SellSwapPool:
                out = sellPoolBase;
                break;
            case SellAllSwapPool:
                out = sellAllPoolBase;
                break;
            case BuySwapPool:
                out = buyPoolBase;
                break;
            case EditCandidateCommission:
                out = editCandidateCommission;
                break;
            case MintToken:
                out = mintToken;
                break;
            case BurnToken:
                out = burnToken;
                break;
            case CreateToken:
                out = createToken;
                break;
            case RecreateToken:
                out = recreateToken;
                break;
            case VoteCommission:
                out = voteCommission;
                break;
            case VoteUpdate:
                out = voteUpdate;
                break;
            case CreateSwapPool:
                out = createSwapPool;
                break;
            default:
                // one instead of zero - to avoid accidental divisions by zero, be carefully
                out = BigInteger.ONE;
                break;
        }
        return out;
    }
}
