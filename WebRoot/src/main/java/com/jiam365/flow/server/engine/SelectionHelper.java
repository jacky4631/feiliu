// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

import com.jiam365.flow.sdk.utils.ProductIDHelper;
import java.util.HashMap;
import java.util.Map;

public class SelectionHelper
{
    private Map<String, Integer> inTurnSelectionIndex;
    
    public SelectionHelper() {
        this.inTurnSelectionIndex = new HashMap<String, Integer>();
    }
    
    public synchronized int[] inTurnIndex(final String productId, final int count) {
        final String groupId = ProductIDHelper.productGroup(productId);
        Integer idx = this.inTurnSelectionIndex.get(groupId);
        if (idx == null) {
            idx = 0;
            this.inTurnSelectionIndex.put(groupId, 0);
        }
        else {
            if (++idx >= count) {
                idx = 0;
            }
            this.inTurnSelectionIndex.put(groupId, idx);
        }
        final int[] ret = new int[count];
        for (int i = 0; i < count; ++i) {
            ret[i] = idx;
            if (++idx >= count) {
                idx = 0;
            }
        }
        return ret;
    }
    
    public int[] normalIndex(final int count) {
        final int[] ret = new int[count];
        for (int i = 0; i < count; ++i) {
            ret[i] = i;
        }
        return ret;
    }
    
    public synchronized void markSelection(final String productId, final int idx) {
        final String groupId = ProductIDHelper.productGroup(productId);
        this.inTurnSelectionIndex.put(groupId, idx);
    }
}
