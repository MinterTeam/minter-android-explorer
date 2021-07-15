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

package network.minter.explorer.tests.repo

import network.minter.core.internal.log.StdLogger
import network.minter.explorer.MinterExplorerSDK
import network.minter.explorer.models.PoolRoute
import org.junit.Assert.*
import org.junit.Test
import java.math.BigDecimal

/**
 * minter-android-explorer. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
class ExplorerPoolsRepositoryTest {

    companion object {
        init {
            MinterExplorerSDK.Setup()
                .setEnableDebug(true)
                .setLogger(StdLogger())
                .init()
        }
    }

    @Test
    fun testGetRouteBIPtoLASHIN() {
        val poolsRepo = MinterExplorerSDK.getInstance().pools()
        val res = poolsRepo.getRoute("BIP", "LASHIN", BigDecimal("10"), PoolRoute.SwapType.Buy)
            .blockingFirst()
        assertNotNull(res)
        assertNull(res.error)
        assertNotNull(res.result)

        assertEquals(2, res.result.coins.size)
        assertEquals("BIP", res.result.coins[0].symbol)
        assertEquals("LASHIN", res.result.coins[1].symbol)
        assertNotNull(res.result.amountIn)
        assertNotNull(res.result.amountOut)
    }
}