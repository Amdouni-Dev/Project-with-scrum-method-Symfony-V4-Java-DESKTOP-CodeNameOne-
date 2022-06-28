package com.google.zxing.qrcode;


/**
 *  @author satorux@google.com (Satoru Takabayashi) - creator
 *  @author dswitkin@google.com (Daniel Switkin) - ported from C++
 */
public final class QRCode {

	public static final int NUM_MASK_PATTERNS = 8;

	public QRCode() {
	}

	public Mode getMode() {
	}

	public ErrorCorrectionLevel getECLevel() {
	}

	public Version getVersion() {
	}

	public int getMaskPattern() {
	}

	public ByteMatrix getMatrix() {
	}

	@java.lang.Override
	public String toString() {
	}

	public void setMode(Mode value) {
	}

	public void setECLevel(ErrorCorrectionLevel value) {
	}

	public void setVersion(Version version) {
	}

	public void setMaskPattern(int value) {
	}

	public void setMatrix(ByteMatrix value) {
	}

	public static boolean isValidMaskPattern(int maskPattern) {
	}
}
