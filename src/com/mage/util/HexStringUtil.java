package com.mage.util;

public class HexStringUtil {

    public static HexStringUtil getInstance() {
        return new HexStringUtil();
    }

    public String bytesToHexString(byte[] bArray) {
        StringBuffer stringBuffer = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                stringBuffer.append(0);
            stringBuffer.append(sTemp.toUpperCase());
        }
        return stringBuffer.toString();
    }

    public byte[] hexStringToByte(String hex) {
        int len = 0;
        //�ж��ַ����ĳ����Ƿ�����λ
        if (hex.length() >= 2) {
            //�ж��ַ�ϲ���Ƿ���ż��
            len = (hex.length() / 2);
            if (hex.length() % 2 == 1) {
                hex = "0" + hex;
                len = len + 1;
            }
        } else {
            hex = "0" + hex;
            len = 1;
        }
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        if (c >= 'a')
            return (c - 'a' + 10) & 0x0f;
        if (c >= 'A')
            return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }
}