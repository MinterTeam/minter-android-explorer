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

package network.minter.explorer.api.converters;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.utils.Base64UrlSafe;
import network.minter.core.crypto.BytesData;
import network.minter.core.internal.log.Mint;
import network.minter.explorer.MinterExplorerSDK;
import network.minter.explorer.models.HistoryTransaction;

/**
 * minter-android-explorer. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class ExplorerHistoryTransactionDeserializer implements JsonDeserializer<HistoryTransaction> {
    @Override
    public HistoryTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final Gson gson = MinterExplorerSDK.getInstance().getGsonBuilder().create();
        final HistoryTransaction tx = gson.fromJson(json, HistoryTransaction.class);

        if (tx.type != null && tx.type.getCls() != null) {
            JsonObject obj = json.getAsJsonObject().get("data").getAsJsonObject();
            tx.data = gson.fromJson(obj, tx.type.getCls());
        } else {
            JsonObject obj = json.getAsJsonObject().get("data").getAsJsonObject();
            tx.data = gson.fromJson(obj, HistoryTransaction.TxDefaultResult.class);
        }

        return tx;
    }

    public static class CheckTransactionDeserializer implements JsonDeserializer<CheckTransaction> {

        @Override
        public CheckTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            }

            String raw = json.getAsString();
            if (raw.isEmpty()) {
                return null;
            }

            CheckTransaction check;
            try {
                final BytesData checkData = Base64UrlSafe.decode(raw);
                check = CheckTransaction.fromEncoded(checkData.toHexString());
            } catch (Throwable t) {
                Mint.d("Unable to decode raw check: %s", raw);
                check = null;
            }

            return check;
        }
    }
}
