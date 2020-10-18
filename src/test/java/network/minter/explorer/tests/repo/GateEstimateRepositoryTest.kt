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
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * minter-android-explorer. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
class GateEstimateRepositoryTest {

    companion object {
        init {
            MinterExplorerSDK.Setup()
                    .setEnableDebug(true)
                    .setLogger(StdLogger())
                    .init()
        }
    }

    @Test
    fun testGetTxCommission() {
        val tx = "f8700102013301a0df33948d008dffe2f9144a39a2094ebdedadad335e814f880650db641e49eaf3808001b845f8431ca0719ee56aa18ad4ed495d1362264d4457fbc378064137ddbb68e6ed11996c2b6da009e2ec5b6b3eb21c7cec23d037a47aed2ae24402e305821c46aa41963679bb49"
        val estimateRepo = MinterExplorerSDK.getInstance().estimate()

        val gasResult = estimateRepo.getTransactionCommission(tx).blockingFirst()

        assertNotNull(gasResult.result.value)
    }
}