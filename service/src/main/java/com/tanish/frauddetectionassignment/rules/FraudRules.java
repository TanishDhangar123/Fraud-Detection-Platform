package com.tanish.frauddetectionassignment.rules;

import com.tanish.frauddetectionassignment.constants.FraudConstants;
import com.tanish.frauddetectionassignment.model.Transaction;
import java.util.ArrayList;
import java.util.List;

public class FraudRules {

    public static List<String> checkTransaction(Transaction t) {
        List<String> reasons = new ArrayList<>();
        checkHighAmount(t, reasons);
        checkOutsideIndia(t, reasons);
        checkSmallAmount(t, reasons);
        checkUnknownLocation(t, reasons);
        checkNearLimit(t, reasons);
        return reasons;
    }

    private static void checkHighAmount(Transaction t, List<String> reasons) {
        if (t.getAmount() > FraudConstants.HIGH_AMOUNT) {
            reasons.add(FraudConstants.HIGH_AMOUNT_REASON);
        }
    }

    private static void checkOutsideIndia(Transaction t, List<String> reasons) {
        if (!t.getLocation().equalsIgnoreCase(FraudConstants.INDIA)) {
            reasons.add(FraudConstants.OUTSIDE_INDIA_REASON);
        }
    }

    private static void checkSmallAmount(Transaction t, List<String> reasons) {
        if (t.getAmount() < FraudConstants.SMALL_AMOUNT) {
            reasons.add(FraudConstants.SMALL_AMOUNT_REASON);
        }
    }

    private static void checkUnknownLocation(Transaction t, List<String> reasons) {
        if (t.getLocation().equalsIgnoreCase(FraudConstants.UNKNOWN)) {
            reasons.add(FraudConstants.UNKNOWN_LOCATION_REASON);
        }
    }

    private static void checkNearLimit(Transaction t, List<String> reasons) {
        if (t.getAmount() >= FraudConstants.NEAR_THRESHOLD &&
                t.getAmount() < FraudConstants.HIGH_AMOUNT) {
            reasons.add(FraudConstants.NEAR_THRESHOLD_REASON);
        }
    }
}
