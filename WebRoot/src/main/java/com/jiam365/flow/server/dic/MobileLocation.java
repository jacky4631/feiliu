// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.dic;

import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Entity;
import java.io.Serializable;

@Entity(value = "mobile_location", noClassnameStored = true)
public class MobileLocation implements Serializable
{
    private static final long serialVersionUID = -6144224058510048338L;
    @Id
    private String sectionNo;
    @Indexed
    private String area;
    private String mobileType;
    private String areaCode;
    private String postcode;
    
    public String getSectionNo() {
        return this.sectionNo;
    }
    
    public void setSectionNo(final String sectionNo) {
        this.sectionNo = sectionNo;
    }
    
    public String getArea() {
        return this.area;
    }
    
    public void setArea(final String area) {
        this.area = area;
    }
    
    public String getMobileType() {
        return this.mobileType;
    }
    
    public void setMobileType(final String mobileType) {
        this.mobileType = mobileType;
    }
    
    public String getAreaCode() {
        return this.areaCode;
    }
    
    public void setAreaCode(final String areaCode) {
        this.areaCode = areaCode;
    }
    
    public String getPostcode() {
        return this.postcode;
    }
    
    public void setPostcode(final String postcode) {
        this.postcode = postcode;
    }
}
