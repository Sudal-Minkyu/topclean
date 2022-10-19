package com.broadwave.toppos.User.CashReceipt;

import com.broadwave.toppos.User.CashReceipt.CashReceiptDtos.CashReceiptDto;

/**
 * @author Minkyu
 * Date : 2022-04-27
 * Time :
 * Remark :
 */
public interface CashReceiptRepositoryCustom {
    CashReceiptDto findByCashReceipt(Long frId, String frCode);
}
