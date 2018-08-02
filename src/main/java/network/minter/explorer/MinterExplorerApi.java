/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.explorer;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.api.converters.BigIntegerDeserializer;
import network.minter.core.internal.api.converters.BytesDataDeserializer;
import network.minter.core.internal.api.converters.MinterAddressDeserializer;
import network.minter.core.internal.api.converters.MinterHashDeserializer;
import network.minter.core.internal.api.converters.MinterPublicKeyDeserializer;
import network.minter.explorer.repo.ExplorerAddressRepository;
import network.minter.explorer.repo.ExplorerTransactionRepository;
import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * minter-android-explorer. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterExplorerApi {
	public static final String FRONT_URL = BuildConfig.BASE_FRONT_URL;
	private final static String BASE_API_URL = BuildConfig.BASE_API_URL;
	private static MinterExplorerApi INSTANCE;
	private ApiService.Builder mApiService;
	private ExplorerTransactionRepository mTransactionRepository;
	private ExplorerAddressRepository mAddressRepository;

	private MinterExplorerApi() {
		mApiService = new ApiService.Builder(BASE_API_URL, getGsonBuilder());
		mApiService.addHeader("Content-Type", "application/json");
		mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid (explorer)");
		mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
		mApiService.setDateFormat("yyyy-MM-dd HH:mm:ssZ");
	}

	public static void initialize(boolean debug) {
		if (INSTANCE != null) {
			return;
		}

		INSTANCE = new MinterExplorerApi();
		INSTANCE.mApiService.setDebug(debug);

		if (debug) {
			INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
		}
	}

	public static HttpUrl.Builder newFrontUrl() {
		return HttpUrl.parse(FRONT_URL).newBuilder();
	}

	public static MinterExplorerApi getInstance() {
		return INSTANCE;
	}

	public ExplorerTransactionRepository transactions() {
		if (mTransactionRepository == null) {
			mTransactionRepository = new ExplorerTransactionRepository(mApiService);
		}

		return mTransactionRepository;
	}

	public ApiService.Builder getApiService() {
		return mApiService;
	}

	public ExplorerAddressRepository address() {
		if (mAddressRepository == null) {
			mAddressRepository = new ExplorerAddressRepository(mApiService);
		}

		return mAddressRepository;
	}


	public GsonBuilder getGsonBuilder() {
		GsonBuilder out = new GsonBuilder();
        out.setDateFormat("yyyy-MM-dd HH:mm:ssZ");
		out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
		out.registerTypeAdapter(MinterPublicKey.class, new MinterPublicKeyDeserializer());
		out.registerTypeAdapter(MinterHash.class, new MinterHashDeserializer());
		out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
		out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

		return out;
	}
}
