package org.meteorminer.hash;


public class HexUtil {
    public static String encode(int[] data) {
        StringBuilder sb = new StringBuilder(data.length * 8);

        for (int aData : data) {
            sb.append(encode((aData >>> 4) & 0xf));
            sb.append(encode((aData) & 0xf));
            sb.append(encode((aData >>> 12) & 0xf));
            sb.append(encode((aData >>> 8) & 0xf));
            sb.append(encode((aData >>> 20) & 0xf));
            sb.append(encode((aData >>> 16) & 0xf));
            sb.append(encode((aData >>> 28) & 0xf));
            sb.append(encode((aData >>> 24) & 0xf));
        }
        return sb.toString();
    }

    private static char encode(int data) {
        char[] hex = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
                'b', 'c', 'd', 'e', 'f'};
        return hex[data];
    }

    public static long[] decode(long[] data, String hex) {
        int size = Math.min(data.length, (hex.length() / 8));

        for (int i = 0; i < size; i++) {
            String parse = hex.substring(i * 8, (i * 8) + 8);
            data[i] = (Long.reverseBytes(Long.parseLong(parse, 16) << 16)) >>> 16;
        }
        return data;
    }

    public static int[] decode(int[] data, String hex) {
        int len = hex.length() / 8;
        for (int i = 0; i < len; i++) {
            int at = i * 8;
            data[i] = decode(hex.charAt(at)) << 4
                    ^ decode(hex.charAt(at + 1))
                    ^ decode(hex.charAt(at + 2)) << 12
                    ^ decode(hex.charAt(at + 3)) << 8
                    ^ decode(hex.charAt(at + 4)) << 20
                    ^ decode(hex.charAt(at + 5)) << 16
                    ^ decode(hex.charAt(at + 6)) << 28
                    ^ decode(hex.charAt(at + 7)) << 24;
        }
        return data;
    }

    private static int decode(char hex) {
        switch (hex) {
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
                return 10;
            case 'b':
                return 11;
            case 'c':
                return 12;
            case 'd':
                return 13;
            case 'e':
                return 14;
            case 'f':
                return 15;
        }
        throw new IllegalArgumentException();
    }
}
