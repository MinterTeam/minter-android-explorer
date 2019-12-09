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
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.api.converters.BigIntegerJsonConverter;
import network.minter.core.internal.api.converters.BytesDataJsonConverter;
import network.minter.core.internal.api.converters.MinterAddressJsonConverter;
import network.minter.core.internal.api.converters.MinterCheckJsonConverter;
import network.minter.core.internal.api.converters.MinterHashJsonConverter;
import network.minter.core.internal.api.converters.MinterPublicKeyJsonConverter;
import network.minter.core.internal.log.Mint;
import network.minter.core.internal.log.StdLogger;
import network.minter.core.internal.log.TimberLogger;
import network.minter.explorer.repo.ExplorerAddressRepository;
import network.minter.explorer.repo.ExplorerCoinsRepository;
import network.minter.explorer.repo.ExplorerTransactionRepository;
import network.minter.explorer.repo.ExplorerValidatorsRepository;
import network.minter.explorer.repo.GateEstimateRepository;
import network.minter.explorer.repo.GateGasRepository;
import network.minter.explorer.repo.GateTransactionRepository;
import okhttp3.HttpUrl;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * minter-android-explorer. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class MinterExplorerApi {
    public static final String FRONT_URL = BuildConfig.BASE_FRONT_URL;
    private final static String BASE_API_URL = BuildConfig.BASE_API_URL;
    private final static String BASE_GATE_URL = BuildConfig.GATE_API_URL;
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
    private ExplorerValidatorsRepository mValidatorsRepository;
    private GateGasRepository mGasRepository;
    private GateEstimateRepository mGateEstimateRepo;
    private GateTransactionRepository mGateTxRepo;

    private MinterExplorerApi() {
        this(BASE_API_URL, BASE_GATE_URL);
    }

    private MinterExplorerApi(String baseApiUrl, String baseGateUrl) {
        mApiService = new ApiService.Builder(baseApiUrl, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid (explorer)");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
        mApiService.setDateFormat(DATE_FORMAT);

        mGateApiService = new ApiService.Builder(baseGateUrl, getGsonBuilder());
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
    public static void initialize(String baseExplorerApiUrl, String baseGateUrl) {
        initialize(baseExplorerApiUrl, baseGateUrl, false);
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(String baseExplorerApiUrl, String baseGateUrl, boolean debug, Mint.Leaf logger) {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterExplorerApi(baseExplorerApiUrl, baseGateUrl);
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
    public static void initialize(String baseExplorerApiUrl, String baseGateUrl, boolean debug) {
        initialize(baseExplorerApiUrl, baseGateUrl, debug, new TimberLogger());
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(boolean debug) {
        initialize(BASE_API_URL, BASE_GATE_URL, debug, new TimberLogger());
    }

    /**
     * Init method with debug logs flag
     * @param debug enable debug logs
     */
    public static void initialize(boolean debug, Mint.Leaf logger) {
        initialize(BASE_API_URL, BASE_GATE_URL, debug, logger);
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
     * @return
     */
    public GateGasRepository gas() {
        if (mGasRepository == null) {
            mGasRepository = new GateGasRepository(mGateApiService);
        }

        return mGasRepository;
    }

    public GateTransactionRepository transactionsGate() {
        if (mGateTxRepo == null) {
            mGateTxRepo = new GateTransactionRepository(mGateApiService);
        }

        return mGateTxRepo;
    }

    public GateEstimateRepository estimate() {
        if (mGateEstimateRepo == null) {
            mGateEstimateRepo = new GateEstimateRepository(mGateApiService);
        }

        return mGateEstimateRepo;
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

    /**
     * @return Validators api repository
     */
    public ExplorerValidatorsRepository validators() {
        if (mValidatorsRepository == null) {
            mValidatorsRepository = new ExplorerValidatorsRepository(mApiService);
        }

        return mValidatorsRepository;
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
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressJsonConverter());
        out.registerTypeAdapter(MinterPublicKey.class, new MinterPublicKeyJsonConverter());
        out.registerTypeAdapter(MinterHash.class, new MinterHashJsonConverter());
        out.registerTypeAdapter(MinterCheck.class, new MinterCheckJsonConverter());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerJsonConverter());
        out.registerTypeAdapter(BigDecimal.class, new BigDecimalJsonConverter());
        out.registerTypeAdapter(BytesData.class, new BytesDataJsonConverter());

        return out;
    }

    private final static class BigDecimalJsonConverter implements JsonDeserializer<BigDecimal>, JsonSerializer<BigDecimal> {

        @Override
        public BigDecimal deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (json.isJsonNull() || !json.isJsonPrimitive()) {
                return new BigDecimal("0");
            }

            if (json.getAsString().isEmpty()) {
                return new BigDecimal("0").setScale(Transaction.VALUE_MUL_DEC.scale());
            }

            return new BigDecimal(json.getAsString());
        }

        @Override
        public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toPlainString());
        }
    }
}
