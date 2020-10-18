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

import com.google.gson.*
import network.minter.core.crypto.MinterPublicKey
import network.minter.explorer.models.CoinDelegation
import network.minter.explorer.models.CoinItemBase
import network.minter.explorer.models.ValidatorMeta
import java.lang.reflect.Type
import java.math.BigDecimal

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
class ExplorerCoinDelegationJsonConverter : JsonDeserializer<CoinDelegation?> {
    /**
     * YES, boilerplate, BUT manual deserializing increases performance
     * @param json
     * @param typeOfT
     * @param context
     * @return
     * @throws JsonParseException
     */
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): CoinDelegation? {
        if (json.isJsonNull) {
            return null
        }
        val data = CoinDelegation()
        val root = json.asJsonObject

        val coinRoot = root["coin"]?.asJsonObject
        data.coin = CoinItemBase()
        data.coin.id = coinRoot?.get("id")?.asBigInteger
        data.coin.symbol = coinRoot?.get("symbol")?.asString
        data.amount = root["value"]?.asBigDecimal ?: BigDecimal.ZERO
        data.bipValue = root["bip_value"]?.asBigDecimal ?: BigDecimal.ZERO
        if (root.has("is_waitlisted") && root.get("is_waitlisted").isJsonPrimitive) {
            try {
                data.isInWaitlist = root["is_waitlisted"].asBoolean
            } catch (e: Throwable) {
                data.isInWaitlist = false
            }
        }

        data.meta = ValidatorMeta()
        if (root.has("validator")) {
            val validatorRoot = root["validator"].asJsonObject
            data.publicKey = MinterPublicKey(validatorRoot["public_key"].asString)
            data.meta!!.name = validatorRoot.asStringDef("name")
            data.meta!!.description = validatorRoot.asStringDef("description")
            data.meta!!.iconUrl = validatorRoot.asStringDef("icon_url")
            data.meta!!.siteUrl = validatorRoot.asStringDef("site_url")
        }

        return data
    }

    fun JsonObject.asStringDef(key: String, defVal: String? = null): String? {
        if (!has(key) || get(key).isJsonNull || isJsonNull) {
            return defVal
        }

        return get(key).asString
    }
}