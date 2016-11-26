package com.jiam365.flow.plugins.wuyika;

public class QueryRequestDTO {
	
	String nonce;
	String signature;
	String value;
	
	public void setnonce(String nonce)
	{
		this.nonce=nonce;
		
	}
	public String getnonce()
	{
		return nonce;
		}
	
	public void setsignature(String signature)
	{
		this.signature=signature;
		}
	public String getsignature()
	{
		return signature;
		}
	
	public void setvalue(String value)
	{
		this.value=value;
		}
	public String getvalue()
	{
		return value;
		}
	
	
	

}
