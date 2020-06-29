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
import network.minter.explorer.models.CoinDelegation
import network.minter.explorer.models.DelegationList
import java.lang.reflect.Type

/**
 * minter-android-explorer. 2020
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
class ExplorerDelegationListJsonConverter : JsonDeserializer<DelegationList> {
    private val gson: Gson = GsonBuilder().registerTypeAdapter(CoinDelegation::class.java, ExplorerCoinDelegationJsonConverter())
            .create()

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DelegationList {
        val delegationList = DelegationList()

        if (json == null || json.isJsonNull) {
            return delegationList
        }

        val root: JsonArray = json.asJsonArray

        for (it in root) {
            val obj = it.asJsonObject
            val item = gson.fromJson(obj, CoinDelegation::class.java)
            if (!delegationList.delegations.containsKey(item.publicKey)) {
                delegationList.delegations[item.publicKey!!] = ArrayList()
            }
            delegationList.delegations[item.publicKey]!!.add(item)
        }

        for (entry in delegationList.delegations) {
            entry.value.sortByDescending { it.bipValue }
        }

        return delegationList
    }

}