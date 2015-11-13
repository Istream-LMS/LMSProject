package org.mifosplatform.finance.depositandrefund.domain;


public enum DepositCalculationType {

    INVALID(0, "depositCalculationType.invalid"), //
    FLAT(1, "depositCalculationType.flat"), //
    PERCENT_OF_AMOUNT(2, "depositCalculationType.percent.of.amount"); //

    private final Integer value;
    private final String code;

    private DepositCalculationType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public static Object[] validValuesForLoan() {
        return new Integer[] { DepositCalculationType.FLAT.getValue(), DepositCalculationType.PERCENT_OF_AMOUNT.getValue() };
    }
    
    public static DepositCalculationType fromInt(final Integer depositCalculation) {
        DepositCalculationType depositCalculationType = DepositCalculationType.INVALID;
        switch (depositCalculation) {
            case 1:
            	depositCalculationType = FLAT;
            break;
            case 2:
            	depositCalculationType = PERCENT_OF_AMOUNT;
            break;
        }
        return depositCalculationType;
    }

    public boolean isPercentageOfAmount() {
        return this.value.equals(DepositCalculationType.PERCENT_OF_AMOUNT.getValue());
    }

    public boolean isFlat() {
        return this.value.equals(DepositCalculationType.FLAT.getValue());
    }

    public boolean isPercentageBased(){
        return isPercentageOfAmount();
    }
}
