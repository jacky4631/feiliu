// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.params;

public class ProductSelectionParam
{
    public static final int MODE_PRICE_AND_PRIORITY = 0;
    public static final int MODE_IN_TURN = 1;
    private int selectionMode;
    private boolean enableReplacement;
    private boolean enablePriceProtect;
    private boolean ignorePriceWithOperator;
    
    public ProductSelectionParam() {
        this.selectionMode = 0;
        this.enableReplacement = true;
        this.enablePriceProtect = true;
        this.ignorePriceWithOperator = true;
    }
    
    public int getSelectionMode() {
        return this.selectionMode;
    }
    
    public void setSelectionMode(final int selectionMode) {
        this.selectionMode = selectionMode;
    }
    
    public boolean isEnableReplacement() {
        return this.enableReplacement;
    }
    
    public void setEnableReplacement(final boolean enableReplacement) {
        this.enableReplacement = enableReplacement;
    }
    
    public boolean isEnablePriceProtect() {
        return this.enablePriceProtect;
    }
    
    public void setEnablePriceProtect(final boolean enablePriceProtect) {
        this.enablePriceProtect = enablePriceProtect;
    }
    
    public boolean isIgnorePriceWithOperator() {
        return this.ignorePriceWithOperator;
    }
    
    public void setIgnorePriceWithOperator(final boolean ignorePriceWithOperator) {
        this.ignorePriceWithOperator = ignorePriceWithOperator;
    }
}
