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
import com.google.gson.JsonParseException
import network.minter.core.crypto.MinterAddress
import network.minter.explorer.models.AddressBalance
import network.minter.explorer.models.CoinBalance
import java.lang.reflect.Type
import java.util.*

/**
 * minter-android-explorer. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
class ExplorerAddressBalanceDeserializer : JsonDeserializer<AddressBalance?> {
    companion object {
        private const val COINS_BALANCE = "balances"
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AddressBalance? {
        if (json.isJsonNull) {
            return null
        }
        val data = AddressBalance()
        val root = json.asJsonObject
        if (root.has("address") && !root["address"].isJsonNull) {
            data.address = MinterAddress(root["address"].asString)
        }
        if (root.has("total_balance_sum") && !root["total_balance_sum"].isJsonNull) {
            data.totalBalance = root["total_balance_sum"].asBigDecimal
        }
        if (root.has("total_balance_sum_usd") && !root["total_balance_sum_usd"].isJsonNull) {
            data.totalBalanceUSD = root["total_balance_sum_usd"].asBigDecimal
        }
        if (root.has("available_balance_sum") && !root["available_balance_sum"].isJsonNull) {
            data.availableBalanceBIP = root["available_balance_sum"].asBigDecimal
        }
        if (root.has("available_balance_sum_usd") && !root["available_balance_sum_usd"].isJsonNull) {
            data.availableBalanceUSD = root["available_balance_sum_usd"].asBigDecimal
        }
        if (root.has(COINS_BALANCE)) {
            val coins = root.getAsJsonArray(COINS_BALANCE)
            val out: MutableMap<String, CoinBalance> = HashMap()
            for (i in 0 until coins.size()) {
                val coinData = coins[i].asJsonObject
                val amount = coinData["amount"].asBigDecimal
                val bipAmount = coinData["bip_amount"].asBigDecimal
                val name = coinData["coin"].asString.toUpperCase(Locale.getDefault())
                out[name] = CoinBalance(name, amount, bipAmount, data.address)
            }
            data.coins = out
        }
        return data
    }


}