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

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import network.minter.blockchain.models.NodeResult;
import network.minter.explorer.MinterExplorerSDK;

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class GateResult<T> extends NodeResult {
    public T result;

    public static class Deserializer<T> implements JsonDeserializer<GateResult<T>> {
        private final Class<T> clazz;
        private final GsonBuilder gson;

        public Deserializer(Class<T> clazz) {
            this.clazz = clazz;
            gson = MinterExplorerSDK.getInstance().getGsonBuilder();
        }

        public Deserializer<T> registerTypeAdapter(Type type, Object typeAdapter) {
            gson.registerTypeAdapter(type, typeAdapter);
            return this;
        }

        @Override
        public GateResult<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            GateResult<T> out = new GateResult<>();
            if (json.isJsonNull()) {
                out.error = new NodeResult.Error();
                out.error.code = -1;
                out.error.message = "Empty response";
                return out;
            }

            JsonObject root = json.getAsJsonObject();
            if (root.has("error")) {
                out.error = gson.create().fromJson(root.get("error"), NodeResult.Error.class);
                return out;
            }

            out.result = gson.create().fromJson(root, clazz);

            return out;
        }
    }
}
