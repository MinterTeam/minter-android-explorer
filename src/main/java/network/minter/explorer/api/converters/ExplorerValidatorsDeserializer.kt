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

package network.minter.explorer.api.converters

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import network.minter.core.crypto.MinterPublicKey
import network.minter.explorer.models.ValidatorItem
import network.minter.explorer.models.ValidatorMeta
import java.lang.reflect.Type

class ExplorerValidatorsDeserializer : JsonDeserializer<ValidatorItem?> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): ValidatorItem? {
        if (json == null || json.isJsonNull) {
            return null
        }

        val validator = ValidatorItem()
        val meta = ValidatorMeta()

        val root = json.asJsonObject
        validator.pubKey = MinterPublicKey(root["public_key"].asString)
        validator.part = root["part"].asBigDecimal
        validator.stake = root["stake"].asBigDecimal
        validator.status = root["status"].asInt
        if (root.has("delegator_count") && !root["delegator_count"].isJsonNull) {
            validator.delegatorCount = root["delegator_count"].asBigInteger
        }

        validator.meta = meta

        meta.description = root["description"].asStringDef()
        meta.iconUrl = root["icon_url"].asStringDef()
        meta.siteUrl = root["site_url"].asStringDef()
        meta.name = root["name"].asStringDef()

        return validator

    }

    fun JsonElement.asStringDef(defValue: String? = null): String? {
        if (isJsonNull) {
            return defValue
        }

        return asString
    }

    fun JsonObject.asStringDef(key: String, defVal: String? = null): String? {
        if (!has(key) || get(key).isJsonNull || isJsonNull) {
            return defVal
        }

        return get(key).asString
    }
}