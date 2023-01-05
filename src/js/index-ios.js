/*!
 *
 * Author: Alex Disler (alexdisler.com)
 * github.com/alexdisler/cordova-plugin-inapppurchase
 *
 * Licensed under the MIT license. Please see README for more information.
 *
 */

const inAppPurchase = { utils };

const nativeCall = (name, args = []) => {
  return new Promise((resolve, reject) => {
    window.cordova.exec((res) => {
      resolve(res);
    }, (err) => {
      reject(err);
    }, 'PaymentsPlugin', name, args);
  });
};

inAppPurchase.getProducts = (productIds) => {
  return new Promise((resolve, reject) => {
    if(!inAppPurchase.utils.validArrayOfStrings(productIds)) {
      reject(new Error(inAppPurchase.utils.errors[101]));
    } else {
      return nativeCall('getProducts', [productIds]).then((res) => {
        if (!res || !res.products) {
          resolve([]);
        } else {
          const arr = res.products.map((val) => {
            return {
              productId   : val.productId,
              title       : val.title,
              description : val.description,
              priceAsDecimal : val.priceAsDecimal,
              priceRaw    : val.priceRaw,
              price       : val.price,
              country     : val.country,
              currency    : val.currency,
              introductoryPrice: val.introductoryPrice,
              introductoryPriceSupported: val.introductoryPriceSupported,
            };
          });
          resolve(arr);
        }
      }).catch(reject);
    }
  });
};

/**
 * @param productId - The InAppPurchase productId as defined in iTunesConnect
 * @param extraParams - Not used; for compatability with Android
 */
inAppPurchase.buy = (productId, extraParams) => {
  return new Promise((resolve, reject) => {
    if(!inAppPurchase.utils.validString(productId)) {
      reject(new Error(inAppPurchase.utils.errors[102]));
    } else {
      nativeCall('buy', [productId]).then((res) => {
        resolve({
          transactionId : res.transactionId,
          receipt       : res.receipt,
        });
      }).catch(reject);
    }
  });
};

/**
 * This function exists so that the iOS plugin API will be compatible with that of Android -
 * where this function is required.
 * See README for more details.
 */
inAppPurchase.subscribe = (productId, extraParams) => {
  return inAppPurchase.buy(productId, extraParams);
};

/**
 * This function exists so that the iOS plugin API will be compatible with that of Android -
 * where this function is required.
 * See README for more details.
 */
inAppPurchase.acknowledge = () => {
  return Promise.resolve();
};

/**
 * This function exists so that the iOS plugin API will be compatible with that of Android -
 * where this function is required.
 * See README for more details.
 */
inAppPurchase.consume = () => {
  return Promise.resolve();
};

inAppPurchase.getReceipt = () => {
  return nativeCall('getReceipt').then((res) => {
    let receipt = '';
    if (res && res.receipt) {
      receipt = res.receipt;
    }
    return receipt;
  });
};

inAppPurchase.restorePurchases = () => {
  return nativeCall('restorePurchases').then((res) => {
    let arr = [];
    if (res && res.transactions) {
      arr = res.transactions.map((val) => {
        return {
          productId     : val.productId,
          date          : val.date,
          transactionId : val.transactionId,
          state         : val.transactionState,
        };
      });
    }
    return arr;
  });
};

module.exports = inAppPurchase;
