package com.cp.utils;

import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Calculator {
    private static final Logger logger = LogGenerator.getLoggerInstance(Calculator.class);

    public static float getCpSmBidInterest(String tenor, String rate,String principal,String maturityDate){
        try {
            float principalValue = getFormattedFloat(principal);
            float tenorValue = getFormattedFloat(tenor);
            float rateValue = getPercentageValue(rate);
            float interestValue;

            interestValue = (principalValue * tenorValue * rateValue / 365) * 100;
            if (Shared.isLeapYear(maturityDate))
                interestValue = interestValue + (tenorValue / 366);

            return interestValue;
        } catch (Exception e){
            logger.info("Exception occurred: "+e.getMessage());
            return 0;
        }
    }

    public static float getCpSmBidPrincipalAtMaturity(String principal, String interest,String rate, String tenor ){
        try {
            float principalValue = getFormattedFloat(principal);
            float interestValue = getFormattedFloat(interest);
            float rateValue = getPercentageValue(rate);
            float tenorValue = getFormattedFloat(tenor);

            return principalValue * (interestValue + rateValue * tenorValue);

        }
        catch(Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }

    public static float getCpTermAmountDue(String principal, String reDiscountRate,String daysToMaturity){
        try{
            float principalValue = getFormattedFloat(principal);
            float reDiscountRateValue = getPercentageValue(reDiscountRate);
            float dtm = getFormattedFloat(daysToMaturity);

            return  principalValue - (principalValue * reDiscountRateValue * (dtm / 365));
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }
    public static float getCpTermAmountDueForLeapYear(String amountDue,String principal, String reDiscountRate,String daysToMaturity){
        try{
            float principalValue = getFormattedFloat(principal);
            float reDiscountRateValue = getPercentageValue(reDiscountRate);
            float dtm = getFormattedFloat(daysToMaturity);
            float amountDueValue = getFormattedFloat(amountDue);

            return  amountDueValue + (principalValue * reDiscountRateValue * (dtm / 366));
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }
    public static float getCpPartialTermAdjustedPrincipalForLeapYear(String principal, String reDiscountRate, String daysToMaturity){
        try{
            float principalValue = getFormattedFloat(principal);
            float reDiscountRateValue = getPercentageValue(reDiscountRate);
            float dtm = getFormattedFloat(daysToMaturity);


            return (principalValue * 366 * 365 * 100 * 100) / (
                    (365 * 366 * 100 * 100) - (reDiscountRateValue * 100 * dtm * 366) + (reDiscountRateValue * 100 * dtm * 365)
                    );
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }

    public static float getCpPartialTermAmountDueForLeapYear(String adjustedPrincipal, String reDiscountRate, String daysToMaturity){
        try{
            float adjustedPrincipalValue = getFormattedFloat(adjustedPrincipal);
            float reDiscountRateValue = getPercentageValue(reDiscountRate);
            float dtm = getFormattedFloat(daysToMaturity);


            return adjustedPrincipalValue - (adjustedPrincipalValue * reDiscountRateValue * dtm / 365) + (adjustedPrincipalValue * reDiscountRateValue * dtm / 366);
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }


    public static float getCpPartialTermAdjustedPrincipal(String principal, String reDiscountRate, String daysToMaturity){
        try{
            float principalValue = getFormattedFloat(principal);
            float reDiscountRateValue = getPercentageValue(reDiscountRate);
            float dtm = getFormattedFloat(daysToMaturity);


            return (principalValue * 366 * 100) / ((366 * 100) - (dtm * reDiscountRateValue));
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }

    public static float getPartialTermAmountDue(String adjustedPrincipal, String reDiscountRate, String daysToMaturity){
        try{
            float adjustedPrincipalValue = getFormattedFloat(adjustedPrincipal);
            float reDiscountRateValue = getPercentageValue(reDiscountRate);
            float dtm = getFormattedFloat(daysToMaturity);


            return adjustedPrincipalValue - (adjustedPrincipalValue * reDiscountRateValue * dtm / 365);
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return 0;
        }
    }

    public static float getTermPenaltyChargeForLeapYear(String principal, String rate, String daysDue){
        try {
            float principalValue = getFormattedFloat(principal);
            float rateValue = getFormattedFloat(rate);
            float daysDueValue = getFormattedFloat(daysDue);

            return principalValue * rateValue * ( daysDueValue / 365 +  daysDueValue / 366);
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return  0;
        }
    }

    public static float getTermPenaltyCharge(String principal, String rate, String daysDue){
        try {
            float principalValue = getFormattedFloat(principal);
            float rateValue = getFormattedFloat(rate);
            float daysDueValue = getFormattedFloat(daysDue);

            return principalValue * rateValue * ( daysDueValue / 366);
        }
        catch (Exception e){
            logger.info("Exception: "+e.getMessage());
            return  0;
        }
    }


    public static float getModulusOfThousands(String amount){
        return getFormattedFloat(amount) % 1000;
    }

    public static float getFormattedFloat(String value){
        return !Shared.isEmpty(value) ? Float.parseFloat(value) : 0;
    }

    public static int getFormattedInteger(String value){
        return !Shared.isEmpty(value) ? Integer.parseInt(value) : 0;
    }
    public static long getFormattedLong(String value){
        return !Shared.isEmpty(value) ? Long.parseLong(value) : 0;
    }

    public static float getPercentageValue(String value){
        return getFormattedFloat(value) / 100;
    }
    public static long getRandomNumber(){
        Random random = new Random();
        return 10000000000L + ((long)random.nextInt(900000000)*100) + random.nextInt(100);
    }
    public static long getDaysBetweenTwoDates(String startDate, String endDate){
        return ChronoUnit.DAYS.between(LocalDate.parse(startDate),LocalDate.parse(endDate));
    }

}
