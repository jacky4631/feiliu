package com.jiam365.flow.plugins.xinyou;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 * Created by ken on 15/12/16.
 */
public class BaseRequestDTO {

    /**
     * 商户账号
     */
    protected String Account;
    
    /**
     * 版本号 1.1 固定值
     */
    protected String V ;
    
    /**
     * 命令
     */
    protected String Action;
    
    /**
     * apikey
     */
    private String key;
    
    
	/**
     * 签名
     */
    protected String sign;


    public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}
	


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	

	public String getV() {
		return V;
	}

	public void setV(String v) {
		V = v;
	}

	public String getAction() {
		return Action;
	}

	public void setAction(String action) {
		Action = action;
	}

	/**
     * 如果不一样可以重写,否则直接用
     */
    public void generateSignature(String key) {
        String _signature = MD5.md5(key);
        this.setSign(_signature);
    }
    
    public void generateSignature(String username,String key) {
        StringBuilder sb = new StringBuilder();
//      "account=szhh&mobile=13815261656&package=10&key=a426da9b82444c8bb7b415670c50e18f"
      sb.append("account="+this.getAccount()).append("&count=5").append("&key="+key);
      String data = sb.toString();
      System.out.println("sb:"+data);
      String _signature = MD5.md5(data);
        this.setSign(_signature);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}

