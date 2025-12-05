package org.cttelsamicsterrassa.data.core.domain.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeasonRangeValidator {
    private static final Pattern YEAR_RANGE_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})$");

    public static boolean isValidYearRange(String input) {
        Matcher matcher = YEAR_RANGE_PATTERN.matcher(input);

        if (!matcher.matches()) {
            return false; // doesn't match pattern like 2023-2024
        }

        int startYear = Integer.parseInt(matcher.group(1));
        int endYear = Integer.parseInt(matcher.group(2));

        // check that end year is exactly next year
        return endYear == startYear + 1;
    }
}
