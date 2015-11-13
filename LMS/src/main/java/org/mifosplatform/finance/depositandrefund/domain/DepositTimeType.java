package org.mifosplatform.finance.depositandrefund.domain;


public enum DepositTimeType {

    INVALID(0, "depositTimeType.invalid"), //
    DISBURSEMENT(1, "depositTimeType.disbursement"), //  only for loan deposit
    ONE_TIME_CHARGE(2,"depositTimeType.oneTimeCharge");  // only for loan deposit

    private final Integer value;
    private final String code;

    private DepositTimeType(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public static Object[] validLoanValues() {
        return new Integer[] { DepositTimeType.DISBURSEMENT.getValue(),DepositTimeType.ONE_TIME_CHARGE.getValue() };
    }

    public static DepositTimeType fromInt(final Integer depositTime) {
        DepositTimeType depositTimeType = DepositTimeType.INVALID;
        if (depositTime != null) {
            switch (depositTime) {
                case 1:
                	depositTimeType = DISBURSEMENT;
                break;
                case 2:
                	depositTimeType=ONE_TIME_CHARGE;
                	break;
                default:
                	depositTimeType = INVALID;
                break;
            }
        }
        return depositTimeType;
    }

    public boolean isTimeOfDisbursement() {
        return DepositTimeType.DISBURSEMENT.getValue().equals(this.value);
    }

    public boolean isOneTimeCharge(){
    	return this.value.equals(DepositTimeType.ONE_TIME_CHARGE.getValue());
    }
    public boolean isAllowedLoanDepositTime() {
        return isTimeOfDisbursement();
    }

}
