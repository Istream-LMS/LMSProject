package org.mifosplatform.portfolio.loanaccount.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.mifosplatform.finance.depositandrefund.domain.DepositCalculationType;
import org.mifosplatform.finance.depositandrefund.domain.DepositTimeType;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.organisation.feemaster.domain.FeeMaster;
import org.mifosplatform.organisation.feemaster.domain.FeeMasterRepositoryWrapper;
import org.mifosplatform.organisation.feemaster.exception.LoanFeeMasterNotFoundException;
import org.mifosplatform.portfolio.loanaccount.domain.LoanFeeMaster;
import org.mifosplatform.portfolio.loanaccount.domain.LoanFeeMasterRepository;
import org.mifosplatform.portfolio.loanaccount.exception.DepositsNotAllowMoreThanOneException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class LoanFeeMasterAssembler {

    private final FromJsonHelper fromApiJsonHelper;
    private final FeeMasterRepositoryWrapper feeMasterRepository;
    private final LoanFeeMasterRepository loanFeeMasterRepository;

    @Autowired
    public LoanFeeMasterAssembler(final FromJsonHelper fromApiJsonHelper, final FeeMasterRepositoryWrapper feeMasterRepository,
    		final LoanFeeMasterRepository loanFeeMasterRepository) {
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.feeMasterRepository = feeMasterRepository;
        this.loanFeeMasterRepository = loanFeeMasterRepository;
    }

    public Set<LoanFeeMaster> fromParsedJson(final JsonElement element) {

        final Set<LoanFeeMaster> loanDeposits = new HashSet<LoanFeeMaster>();

        final BigDecimal principal = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("principal", element);

        if (element.isJsonObject()) {
            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);
            if (topLevelJsonElement.has("depositArray") && topLevelJsonElement.get("depositArray").isJsonArray()) {
                final JsonArray array = topLevelJsonElement.get("depositArray").getAsJsonArray();
                if(array.size() > 1){
                	throw new DepositsNotAllowMoreThanOneException(array);
                }
                for (int i = 0; i < array.size(); i++) {

                    final JsonObject loanDepositElement = array.get(i).getAsJsonObject();

                    final Long id = this.fromApiJsonHelper.extractLongNamed("id", loanDepositElement);
                    final Long depositId = this.fromApiJsonHelper.extractLongNamed("depositId", loanDepositElement);
                    final BigDecimal amount = this.fromApiJsonHelper.extractBigDecimalNamed("amount", loanDepositElement, locale);
                    final Integer depositTimeType = this.fromApiJsonHelper.extractIntegerNamed("depositTimeType", loanDepositElement, locale);
                    final Integer depositCalculationType = this.fromApiJsonHelper.extractIntegerNamed("depositCalculationType",
                    		loanDepositElement, locale);
                    final Integer depositOnType = this.fromApiJsonHelper.extractIntegerNamed("depositOnType", loanDepositElement,
                            locale);
                    if (id == null) {
                        final FeeMaster depositDefinition = this.feeMasterRepository.findOneWithNotFoundDetection(depositId);
                        DepositTimeType depositTime = null;
                        if (depositTimeType != null) {
                        	depositTime = DepositTimeType.fromInt(depositTimeType);
                        }
                        DepositCalculationType depositCalculation = null;
                        if (depositCalculationType != null) {
                        	depositCalculation = DepositCalculationType.fromInt(depositCalculationType);
                        }
                        DepositTimeType depositOn = null;
                        if (depositOnType != null) {
                        	depositOn = DepositTimeType.fromInt(depositOnType);
                        }
                       
                        final LoanFeeMaster loandeposit = LoanFeeMaster.createNewWithoutLoan(depositDefinition, principal, amount, depositTime,
                        		depositCalculation, depositOn,"N");
                        loanDeposits.add(loandeposit);
                    } else {
                        final Long loanDepositId = id;
                        final LoanFeeMaster loanDeposit = this.loanFeeMasterRepository.findOne(loanDepositId);
                        if (loanDeposit == null) { throw new LoanFeeMasterNotFoundException(loanDepositId); }

                        loanDeposit.update(amount);

                        loanDeposits.add(loanDeposit);
                    }
                }
            }
        }

        return loanDeposits;
    }
    /*public Set<LoanCharge> fromParsedJsonLease(final JsonElement element) {

        final Set<LoanCharge> loanCharges = new HashSet<LoanCharge>();

         BigDecimal principal = this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("principal", element);
     //madhav
        BigDecimal resudualAmount=null;
        BigDecimal persentage=new BigDecimal(0);
        
        final Integer numberOfRepayments = this.fromApiJsonHelper.extractIntegerWithLocaleNamed("numberOfRepayments", element);

        if (element.isJsonObject()) {
            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final String dateFormat = this.fromApiJsonHelper.extractDateFormatParameter(topLevelJsonElement);
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);
            
            if (topLevelJsonElement.has("charges") && topLevelJsonElement.get("charges").isJsonArray()) {
                final JsonArray array = topLevelJsonElement.get("charges").getAsJsonArray();

                for (int i = 0; i < array.size(); i++) {

                    final JsonObject loanChargeElement = array.get(i).getAsJsonObject();

                    final Long id = this.fromApiJsonHelper.extractLongNamed("id", loanChargeElement);
                    final Long chargeId = this.fromApiJsonHelper.extractLongNamed("chargeId", loanChargeElement);
                    final BigDecimal amount = this.fromApiJsonHelper.extractBigDecimalNamed("amount", loanChargeElement, locale);
                    final Integer chargeTimeType = this.fromApiJsonHelper.extractIntegerNamed("chargeTimeType", loanChargeElement, locale);
                
                    final Integer chargeCalculationType = this.fromApiJsonHelper.extractIntegerNamed("chargeCalculationType",
                            loanChargeElement, locale);
                    final LocalDate dueDate = this.fromApiJsonHelper
                            .extractLocalDateNamed("dueDate", loanChargeElement, dateFormat, locale);
                    final Integer chargePaymentMode = this.fromApiJsonHelper.extractIntegerNamed("chargePaymentMode", loanChargeElement,
                            locale);
                    
                    if (id == null) {
                        final Charge chargeDefinition = this.feeMasterRepository.findOneWithNotFoundDetection(chargeId);
                        ChargeTimeType chargeTime = null;
                        if (chargeTimeType != null) {
                            chargeTime = ChargeTimeType.fromInt(chargeTimeType);
                        }
                        ChargeCalculationType chargeCalculation = null;
                        if (chargeCalculationType != null) {
                            chargeCalculation = ChargeCalculationType.fromInt(chargeCalculationType);
                        }
                        ChargePaymentMode chargePaymentModeEnum = null;
                        if (chargePaymentMode != null) {
                            chargePaymentModeEnum = ChargePaymentMode.fromInt(chargePaymentMode);
                        }
                        //madhav
                        System.out.println("*******"+ chargeDefinition.getChargeTime());
                         if(chargeDefinition.getChargeTime()==10){
                        	  LoanCharge loanCharge1  = LoanCharge.createNewWithoutLoan(chargeDefinition, this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("principal", element), amount, chargeTime,
                                chargeCalculation, dueDate, chargePaymentModeEnum,numberOfRepayments);
                        loanCharges.add(loanCharge1);
                        System.out.println("if block======================="+ principal);
                        System.out.println("if block======================="+ amount);
                        persentage= loanCharge1.getPercentage();
                     //   BigDecimal principalbyhun= principal.divide(new BigDecimal(100));
                    	//resudualAmount=principalbyhun.multiply(persentage);
                    	//BigDecimal b=loanCharge1.amountOutstanding();
                       // principal =principal.subtract(resudualAmount);
                        
                        }
                        else{
                        	  LoanCharge loanCharge  = LoanCharge.createNewWithoutLoan(chargeDefinition, principal, amount, chargeTime,
                                    chargeCalculation, dueDate, chargePaymentModeEnum,numberOfRepayments);      
                            loanCharges.add(loanCharge);
                            System.out.println("else block======================="+principal);
                            System.out.println("if block======================="+ amount);
                        }
                        
                    } else {
                        final Long loanChargeId = id;						
                        final LoanCharge loanCharged = this.loanFeeMasterRepository.findOne(loanChargeId);
                        if (loanCharged == null) { throw new LoanChargeNotFoundException(loanChargeId); }
                        System.out.println("last else============== block======================="+ amount);
                        loanCharged.update(amount, dueDate, numberOfRepayments);
                        loanCharges.add(loanCharged);
                    }
                }
            }
        }

        return loanCharges;
    }*/
}
