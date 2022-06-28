package com.google.zxing.qrcode;


/**
 *  @author satorux@google.com (Satoru Takabayashi) - creator
 *  @author dswitkin@google.com (Daniel Switkin) - ported from C++
 */
public final class QREncoder {

	/**
	 *  @param content text to encode
	 *  @param ecLevel error correction level to use
	 *  @return {@link QRCode} representing the encoded QR code
	 *  @throws IllegalArgumentException if encoding can't succeed, because of
	 *  for example invalid content or configuration
	 */
	public static QRCode encode(String content, ErrorCorrectionLevel ecLevel) {
	}

	public static QRCode encode(String content, ErrorCorrectionLevel ecLevel, java.util.Map hints) {
	}

	public static Mode chooseMode(String content) {
	}
}
