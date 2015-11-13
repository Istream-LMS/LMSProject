package org.mifosplatform.finance.depositandrefund.service;

import org.mifosplatform.finance.depositandrefund.domain.DepositCalculationType;
import org.mifosplatform.finance.depositandrefund.domain.DepositTimeType;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class DepositEnumerations {

    public static EnumOptionData depositTimeType(final int id) {
        return depositTimeType(DepositTimeType.fromInt(id));
    }

    public static EnumOptionData depositTimeType(final DepositTimeType type) {
        EnumOptionData optionData = null;
        switch (type) {
            case DISBURSEMENT:
                optionData = new EnumOptionData(DepositTimeType.DISBURSEMENT.getValue().longValue(), DepositTimeType.DISBURSEMENT.getCode(),
                        "Disbursement");
            break;
            case ONE_TIME_CHARGE:
            	optionData= new EnumOptionData(DepositTimeType.ONE_TIME_CHARGE.getValue().longValue(),DepositTimeType.ONE_TIME_CHARGE.getCode(),"One Time Charge");
                  break;
            default:
                optionData = new EnumOptionData(DepositTimeType.INVALID.getValue().longValue(), DepositTimeType.INVALID.getCode(), "Invalid");
            break;
        }
        return optionData;
    }

    public static EnumOptionData depositCalculationType(final int id) {
        return depositCalculationType(DepositCalculationType.fromInt(id));
    }

    public static EnumOptionData depositCalculationType(final DepositCalculationType type) {
        EnumOptionData optionData = null;
        switch (type) {
            case FLAT:
                optionData = new EnumOptionData(DepositCalculationType.FLAT.getValue().longValue(), DepositCalculationType.FLAT.getCode(),
                        "Flat");
            break;
            case PERCENT_OF_AMOUNT:
                optionData = new EnumOptionData(DepositCalculationType.PERCENT_OF_AMOUNT.getValue().longValue(),
                        DepositCalculationType.PERCENT_OF_AMOUNT.getCode(), "% Amount");
            break;
            default:
                optionData = new EnumOptionData(DepositCalculationType.INVALID.getValue().longValue(),
                        DepositCalculationType.INVALID.getCode(), "Invalid");
            break;
        }
        return optionData;
    }

}
