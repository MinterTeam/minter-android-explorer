/*
 * Copyright (C) by MinterTeam. 2019
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

package network.minter.explorer.tests.repo;

import network.minter.explorer.models.BCExplorerResult;
import network.minter.explorer.models.ExpResult;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * minter-android-explorer. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public abstract class BaseRepoTest {

    protected void checkResponseSuccess(Response<?> result) {
        assertTrue(result.isSuccessful());
        if (result.body() instanceof ExpResult) {
            final ExpResult<?> body = ((ExpResult) result.body());
            assertNull(bodyError(body), body.error);
            assertTrue(body.isOk());
        } else if (result.body() instanceof BCExplorerResult) {
            final BCExplorerResult<?> body = ((BCExplorerResult) result.body());
            assertNull(bodyError(body), body.error);
            assertTrue(body.isOk());
        }
    }

    protected void checkResponseError(Response<?> result) {
        assertTrue(result.isSuccessful());
        if (result.body() instanceof ExpResult) {
            final ExpResult<?> body = ((ExpResult) result.body());
            assertNotNull("Body has no error!", body.error);
            assertTrue(!body.isOk());
        } else if (result.body() instanceof BCExplorerResult) {
            final BCExplorerResult<?> body = ((BCExplorerResult) result.body());
            assertNotNull("Body has no error!", body.error);
            assertTrue(!body.isOk());
        }
    }

    protected String bodyError(ExpResult<?> res) {
        if (res.error != null) {
            return res.error.getMessage();
        }

        return "No error";
    }

    protected String bodyError(BCExplorerResult<?> res) {
        if (res.error != null) {
            return res.error.getMessage();
        }

        return "No error";
    }
}
