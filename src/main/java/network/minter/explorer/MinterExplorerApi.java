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

package network.minter.explorer;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.api.converters.BigIntegerDeserializer;
import network.minter.core.internal.api.converters.BytesDataDeserializer;
import network.minter.core.internal.api.converters.MinterAddressDeserializer;
import network.minter.core.internal.api.converters.MinterCheckDeserializer;
import network.minter.core.internal.api.converters.MinterHashDeserializer;
import network.minter.core.internal.api.converters.MinterPublicKeyDeserializer;
import network.minter.core.internal.log.Mint;
import network.minter.core.internal.log.StdLogger;
import network.minter.core.internal.log.TimberLogger;
import network.minter.explorer.repo.ExplorerAddressRepository;
import network.minter.explorer.repo.ExplorerCoinsRepository;
import network.minter.explorer.repo.ExplorerTransactionRepository;
import network.minter.explorer.repo.GateGasRepository;
import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class MinterExplorerApi {
    public static final String FRONT_URL = BuildConfig.BASE_FRONT_URL;
    public final static String NET_ID_TESTNET = "odin";
    public final static String NET_ID_TESTNET_WITH_MULTISIG = "dva";
    private final static String BASE_API_URL = BuildConfig.BASE_API_URL;
    private final static String BASE_GATE_URL = "https://gate.minter.network/api/";
    private final static String DATE_FORMAT;

    private static MinterExplorerApi INSTANCE;


    static {
        String format = "yyyy-MM-dd HH:mm:ssX";
        try {
            Class.forName("android.os.Build");
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.N) {
                format = "yyyy-MM-dd HH:mm:ssZ";
            }
        } catch (ClassNotFoundException ignore) {
        } finally {
            DATE_FORMAT = format;
        }
    }

    private ApiService.Builder mApiService;
    private ApiService.Builder mGateApiService;
    private ExplorerTransactionRepository mTransactionRepository;
    private ExplorerAddressRepository mAddressRepository;
    private ExplorerCoinsRepository mCoinsRepository;
    private GateGasRepository mGasRepository;

    private MinterExplorerApi() {
        this(BASE_API_URL);
    }

    private MinterExplorerApi(String baseApiUrl) {
        mApiService = new ApiService.Builder(baseApiUrl, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid (explorer)");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
        mApiService.setDateFormat(DATE_FORMAT);

        mGateApiService = new ApiService.Builder(BASE_GATE_URL, getGsonBuilder());
        mGateApiService.addHeader("Content-Type", "application/json");
        mGateApiService.addHeader("X-Minter-Client-Name", "MinterAndroid (gate)");
        mGateApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
        mGateApiService.setDateFormat(DATE_FORMAT);
    }

    /**
     * Init method
     */
    public static void initialize() {
        initialize(false);
    }

    /**
     * Init method
     */
    public static void initialize(String baseExplorerApiUrl) {
        initialize(baseExplorerApiUrl, false);
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(String baseExplorerApiUrl, boolean debug, Mint.Leaf logger) {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterExplorerApi(baseExplorerApiUrl);
        INSTANCE.mApiService.setDebug(debug);
        INSTANCE.mGateApiService.setDebug(debug);

        // we should run this at any teapot
        if (debug) {
            boolean isAndroid = true;
            try {
                Class.forName("android.util.Log");

                try {
                    // in case mockable android classes
                    android.util.Log.d("test", "test");
                } catch (RuntimeException e) {
                    isAndroid = false;
                }
            } catch (ClassNotFoundException e) {
                isAndroid = false;
            }

            if (!isAndroid && logger instanceof TimberLogger) {
                Mint.brew(new StdLogger());
            } else {
                Mint.brew(logger);
            }

            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
            INSTANCE.mGateApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(String baseExplorerApiUrl, boolean debug) {
        initialize(baseExplorerApiUrl, debug, new TimberLogger());
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(boolean debug) {
        initialize(BASE_API_URL, debug, new TimberLogger());
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(boolean debug, Mint.Leaf logger) {
        initialize(BASE_API_URL, debug, logger);
    }

    /**
     * Create new front url using HttpUrl.Builder
     * @return HttpUrl.Builder
     * @see HttpUrl.Builder
     */
    public static HttpUrl.Builder newFrontUrl() {
        return HttpUrl.parse(FRONT_URL).newBuilder();
    }

    /**
     * @return Singleton instance
     */
    public static MinterExplorerApi getInstance() {
        return INSTANCE;
    }

    public void setNetworkId(String id) {
        mApiService.addHeader("X-Minter-Chain-Id", id);
    }

    /**
     * Gate gas repository
     *
     * @return
     */
    public GateGasRepository gas() {
        if (mGasRepository == null) {
            mGasRepository = new GateGasRepository(mGateApiService);
        }

        return mGasRepository;
    }

    /**
     * @return Transactions api repository
     */
    public ExplorerTransactionRepository transactions() {
        if (mTransactionRepository == null) {
            mTransactionRepository = new ExplorerTransactionRepository(mApiService);
        }

        return mTransactionRepository;
    }

    /**
     * @return Coins api repository
     */
    public ExplorerCoinsRepository coins() {
        if (mCoinsRepository == null) {
            mCoinsRepository = new ExplorerCoinsRepository(mApiService);
        }

        return mCoinsRepository;
    }

    public ApiService.Builder getApiService() {
        return mApiService;
    }

    public ApiService.Builder getGateApiService() {
        return mGateApiService;
    }

    /**
     * @return Addresses api repository
     */
    public ExplorerAddressRepository address() {
        if (mAddressRepository == null) {
            mAddressRepository = new ExplorerAddressRepository(mApiService);
        }

        return mAddressRepository;
    }

    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.setDateFormat(DATE_FORMAT);
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(MinterPublicKey.class, new MinterPublicKeyDeserializer());
        out.registerTypeAdapter(MinterHash.class, new MinterHashDeserializer());
        out.registerTypeAdapter(MinterCheck.class, new MinterCheckDeserializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

        return out;
    }
}
