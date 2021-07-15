package network.minter.explorer.models;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.util.List;

import network.minter.blockchain.api.EstimateSwapFrom;

/**
 * minter-android-explorer. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class PoolRoute {
    @SerializedName("swap_type")
    public EstimateSwapFrom swapIn;
    @SerializedName("amount_in")
    public BigDecimal amountIn;
    @SerializedName("amount_out")
    public BigDecimal amountOut;
    public List<CoinItemBase> coins;
    public int extraCoinsCount() {
        return coins.size() < 3 ? 0 : coins.size() - 2;
    }

    public enum SwapType {
        // pass amount how many you want to get
        Buy("output"),
        // pass amount how many you want to sell
        Sell("input");

        String val;

        SwapType(String v) {
            val = v;
        }

        public String getValue() {
            return val;
        }
    }
}
