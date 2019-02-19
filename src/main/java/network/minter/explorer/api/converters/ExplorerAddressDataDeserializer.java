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

package network.minter.explorer.api.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import network.minter.core.crypto.MinterAddress;
import network.minter.explorer.models.AddressData;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */ /*
{"txCount":1,"coins":[{"coin":"mnt","amount":39.999999,"baseCoinAmount":39.999999,"usdAmount":0.002999999925}]}
 */
public class ExplorerAddressDataDeserializer implements JsonDeserializer<AddressData> {
    private final static String COINS_BALANCE = "coins";

    @Override
    public AddressData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonNull()) {
            return null;
        }

        AddressData data = new AddressData();

        JsonObject root = json.getAsJsonObject();

        if (root.has("address") && !root.get("address").isJsonNull()) {
            data.address = new MinterAddress(root.get("address").getAsString());
        }

        if (root.has(COINS_BALANCE)) {
            JsonArray coins = root.getAsJsonArray(COINS_BALANCE);
            final Map<String, AddressData.CoinBalance> out = new HashMap<>();
            for (int i = 0; i < coins.size(); i++) {
                final JsonObject coin = coins.get(i).getAsJsonObject();
                final AddressData.CoinBalance b = new AddressData.CoinBalance();
                b.amount = coin.get("amount").getAsBigDecimal();
                b.usdAmount = coin.get("usdAmount").getAsBigDecimal();
                b.baseCoinAmount = coin.get("baseCoinAmount").getAsBigDecimal();
                b.coin = coin.get("coin").getAsString();
                out.put(b.coin, b);
            }

            data.coins = out;
        }

        if (root.has("txCount")) {
            data.txCount = root.get("txCount").getAsLong();
        } else {
            data.txCount = 0L;
        }

        return data;
    }
}
