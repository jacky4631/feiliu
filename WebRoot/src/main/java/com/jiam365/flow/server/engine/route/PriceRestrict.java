// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine.route;

import com.jiam365.flow.server.entity.User;
import com.jiam365.flow.server.params.ProductSelectionParam;
import com.jiam365.flow.server.utils.DoubleUtils;
import com.jiam365.flow.server.engine.ChooseRestrict;
import com.jiam365.flow.server.dto.ChoosedProduct;
import com.jiam365.flow.sdk.RechargeRequest;
import com.jiam365.flow.server.params.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.jiam365.flow.server.service.UserManager;

public class PriceRestrict implements Condition
{
    @Autowired
    private UserManager userManager;
    @Autowired
    private ParamsService paramsService;
    
    @Override
    public boolean pass(final RechargeRequest request, final ChoosedProduct choosedProduct, final ChooseRestrict... restricts) {
        final ProductSelectionParam param = this.paramsService.loadProductSelectionParam();
        boolean priceProtected = param.isEnablePriceProtect();
        double userDiscount = request.getBillDiscount();
        if (restricts.length > 0) {
            priceProtected = restricts[0].isPriceProtected();
            userDiscount = restricts[0].getUserDiscount();
        }
        final double billAmount = DoubleUtils.mul(userDiscount, request.getPrice());
        final double costDiscount = choosedProduct.getCostDiscount();
        final double costAmount = DoubleUtils.mul(costDiscount, choosedProduct.getCostPrice());
        final User user = this.userManager.loadUserByUsername(request.getUsername());
        return (user.getUserType() == 1 && param.isIgnorePriceWithOperator()) || user.isForeSkipPriceProtected() || !priceProtected || billAmount >= costAmount;
    }
}
