/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alexdisler.inapppurchases;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.android.billingclient.api.ProductDetails;

/**
 * Represents an in-app product's listing details.
 */
public class IabSkuDetails {
    String mItemType;
    String mSku;
    String mType;
    Double mPriceAsDecimal;
    String mPrice;
    String mPriceCurrency;
    String mPriceRaw;
    String mTitle;
    String mDescription;
    ProductDetails mProductDetails;

    String TAG = "google.payments ISD";

    // Item types
    public static final String ITEM_TYPE_INAPP = "inapp";
    public static final String ITEM_TYPE_SUBS = "subs";

    public IabSkuDetails(ProductDetails productDetails) {
        this(IabHelper.ITEM_TYPE_INAPP, productDetails);
    }

    public IabSkuDetails(String itemType, ProductDetails productDetails) {
        mProductDetails = productDetails;
        mItemType = itemType;

        mSku = productDetails.getProductId();
        mType = productDetails.getProductType();
        mTitle = productDetails.getTitle();

        mDescription = productDetails.getDescription();
//         mPriceAsDecimal = Double.valueOf(1000);
        if(ITEM_TYPE_INAPP.equals(itemType)) {
            ProductDetails.OneTimePurchaseOfferDetails otp = productDetails.getOneTimePurchaseOfferDetails();

            mPrice = otp.getFormattedPrice();
            mPriceCurrency = otp.getPriceCurrencyCode();
            mPriceAsDecimal = otp.getPriceAmountMicros()/Double.valueOf(1000000);
        } else {
            ProductDetails.SubscriptionOfferDetails so = productDetails.getSubscriptionOfferDetails().get(0);

            ProductDetails.PricingPhase pp = so.getPricingPhases().getPricingPhaseList().get(0);

            mPrice = pp.getFormattedPrice();
            mPriceCurrency = pp.getPriceCurrencyCode();
            mPriceAsDecimal = pp.getPriceAmountMicros()/Double.valueOf(1000000);
        }

//         long priceMicros = productDetails.getPriceAmountMicros();
        DecimalFormat formatter = new DecimalFormat("#.00####");
        formatter.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        mPriceRaw = formatter.format(mPriceAsDecimal);
    }

    public String getSku() { return mSku; }
    public String getType() { return mType; }
    public String getPrice() { return mPrice; }
    public String getPriceCurrency() { return mPriceCurrency; }
    public Double getPriceAsDecimal() { return mPriceAsDecimal; }
    public String getPriceRaw() { return mPriceRaw; }
    public String getTitle() { return mTitle; }
    public String getDescription() { return mDescription; }

    @Override
    public String toString() {
        return "IabSkuDetails mSku: " + mSku
          + " mType: " + mType
          + " mPrice: " + mPrice
          + " mPriceCurrency: " + mPriceCurrency
          + " mPriceAsDecimal: " + mPriceAsDecimal
          + " mPriceRaw: " + mPriceRaw
          + " mTitle: " + mTitle
          + " mDescription: " + mDescription + "\n";
//         if(mSkuDetails != null) {
//             return "IabSkuDetails:" + mSkuDetails;
//         } else {
//             return "IabSkuDetails Product:" + mProductDetails;
//         }
    }
}
