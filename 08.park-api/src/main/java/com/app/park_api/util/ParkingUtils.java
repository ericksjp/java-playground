package com.app.park_api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {

    private static final double PRICE_15_MIN = 5.00;
    private static final double PRICE_60_MIN = 9.75;
    private static final double ADDITIONAL_CHARGE = 1.75;
    private static final double DISCOUNT = 0.30;

    public static String genReceipt() {
        return LocalDateTime.now().toString()
                .substring(0, 19).replaceAll("([-:])", "").replace("T", "-");
    }

    public static BigDecimal calculatePrice(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (checkOut == null) { throw new IllegalStateException("Cannot calculate price when check-out time is not set"); }
        if (checkIn == null) { throw new IllegalStateException("Cannot calculate price when check-in time is not set"); }

        long minutesPassed = ChronoUnit.MINUTES.between(checkIn, checkOut);

        double price;

        if (minutesPassed < 15) price = PRICE_15_MIN;
        else if (minutesPassed < 60) price = PRICE_60_MIN;
        else {
            minutesPassed = minutesPassed - 60;
            price = PRICE_60_MIN + ADDITIONAL_CHARGE * Math.ceilDivExact(minutesPassed, 15);
        }

        return new BigDecimal(price).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calculateDiscount(BigDecimal price, int visits) {
        if (price == null) throw new IllegalStateException("Cannot calculate discount when price is not set");

        BigDecimal value = BigDecimal.ZERO;

        if (visits >= 10 && visits % 10 == 0) {
            value = price.multiply(BigDecimal.valueOf(DISCOUNT));
        }

        return value.setScale(2, RoundingMode.HALF_EVEN);
    }

}
