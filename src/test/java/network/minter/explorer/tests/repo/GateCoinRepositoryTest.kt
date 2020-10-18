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
import org.junit.Assert.*
import org.junit.Test
import java.math.BigInteger

/**
 * minter-android-explorer. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
class GateCoinRepositoryTest {

    companion object {
        init {
            MinterExplorerSDK.Setup()
                    .setEnableDebug(true)
                    .setLogger(StdLogger())
                    .init()
        }
    }

    @Test
    fun testGetCoin() {
        val coinRepo = MinterExplorerSDK.getInstance().coinsGate()
        val resultByName = coinRepo.getCoinInfo("MNT").blockingFirst()

        assertTrue(resultByName.isOk)
        assertEquals("MNT", resultByName.result.symbol)

        val resultById = coinRepo.getCoinInfo(BigInteger("10")).blockingFirst()

        assertTrue(resultById.isOk)
        assertEquals("LASHIN", resultById.result.symbol)
    }

    @Test
    fun testCheckCoinExists() {
        val coinRepo = MinterExplorerSDK.getInstance().coinsGate()
        val resultByName = coinRepo.coinExists("UNKNOWN123").blockingFirst()

        assertFalse(resultByName)


        val resultByUnknownId = coinRepo.coinExists(BigInteger("9999999999999")).blockingFirst()
        assertFalse(resultByUnknownId)

        val resultMNT = coinRepo.coinExists(BigInteger.ZERO).blockingFirst()
        assertTrue(resultMNT)
    }
}