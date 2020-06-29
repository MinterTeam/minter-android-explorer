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
import network.minter.explorer.MinterExplorerSDK
import network.minter.explorer.models.AddressBalance
import network.minter.explorer.models.AddressListBalances
import java.lang.reflect.Type

class ExplorerAddressListBalancesDeserializer : JsonDeserializer<AddressListBalances?> {
    val gson = MinterExplorerSDK.getInstance().gsonBuilder
            .registerTypeAdapter(AddressBalance::class.java, ExplorerAddressBalanceDeserializer())
            .create()

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): AddressListBalances? {
        if (json.isJsonNull) {
            return null
        }

        val balances = AddressListBalances()
        val root = json.asJsonArray
        for (i in 0 until root.size()) {
            val balanceJson = root[i].asJsonObject
            val balance = gson.fromJson(balanceJson, AddressBalance::class.java)
            balances.balances.add(balance)
        }
        return balances
    }
}