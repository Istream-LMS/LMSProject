package org.mifosplatform.portfolio.loanaccount.command;

import java.math.BigDecimal;

public class LoanFeeMasterCommand implements Comparable<LoanFeeMasterCommand> {

    @SuppressWarnings("unused")
    private final Long id;
    private final Long depositId;
    private final BigDecimal amount;
    @SuppressWarnings("unused")
    private final Integer depositTimeType;
    @SuppressWarnings("unused")
    private final Integer depositCalculationType;
    @SuppressWarnings("unused")
    private final Integer depositOnType;

    public LoanFeeMasterCommand(final Long id, final Long depositId, final BigDecimal amount, final Integer depositTimeType,
            final Integer depositCalculationType, final Integer depositOnType) {
        this.id = id;
        this.depositId = depositId;
        this.amount = amount;
        this.depositTimeType = depositTimeType;
        this.depositCalculationType = depositCalculationType;
        this.depositOnType = depositOnType;
    }

    @Override
    public int compareTo(final LoanFeeMasterCommand o) {
        int comparison = this.depositId.compareTo(o.depositId);
        if (comparison == 0) {
            comparison = this.amount.compareTo(o.amount);
        }
        return comparison;
    }
}