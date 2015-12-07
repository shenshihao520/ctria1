package com.hsm.barcode;

import java.util.Arrays;

public class DecodeResult {
	public String barcodeData;
	public byte codeId;
	public byte aimId;
	public byte aimModifier;
	public int length;
	public byte[] byteBarcodeData;
	
	public DecodeResult(){
		
	}

	@Override
	public String toString() {
		return "DecodeResult [barcodeData=" + barcodeData + ", codeId="
				+ codeId + ", aimId=" + aimId + ", aimModifier=" + aimModifier
				+ ", length=" + length + ", byteBarcodeData="
				+ Arrays.toString(byteBarcodeData) + "]";
	}
	
}
