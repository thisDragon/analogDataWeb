package com.analog.data.util;

import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
* @ClassName: CfgKeyUtils
* @Description: 生成cfgKey的工具类
* @author yangjianlong
* @date 2019年9月9日
*
 */
public class CfgKeyUtils {

	private static Logger logger = LoggerFactory.getLogger(CfgKeyUtils.class);
	
	public static String getCfgKey(String userNanem){
		 String name = "";
		 String num = "";
		 String a = "";
		 String b = "";
		 Random rand=new Random();
		 int rn = rand.nextInt(6)+ 1;
		try {
			long times = System.currentTimeMillis();
			if (1558596076172L > times) {
				return "-1";
			}
			
			String s = zipNumber(times + "");
			name = encode(userNanem.getBytes("utf-8"));
			int l = name.length();
			
			if (l < 10){
				num = "0" + l;
			}else{
				num = l + "";
			}
		
			 a = s.substring(0, rn);
			 b = s.substring(rn, s.length());
		} catch (Exception e) {
			logger.error("获取CgfKey异常", e);
			return "";
		}
		logger.info("userNanem===" + userNanem + "  " + (rn + a + num + name + b) );
		return (rn + a + num + name + b);
	}



	
//	//校验cfgkey
//	private boolean checkCfgKey(String cfgkey, String username) throws Exception {
//			try{
//				cfgkey = cfgkey.replace("%3d", "=");
//				cfgkey = cfgkey.replace("%20", " ");
//				cfgkey = cfgkey.replace("%26", "&");
//				cfgkey = cfgkey.replace("%23", "#");
//				int rn = Integer.valueOf(cfgkey.substring(0, 1)).intValue();
//				int ul = Integer.valueOf(cfgkey.substring(0+1+rn, 2+1+rn)).intValue();
//				String name64 = cfgkey.substring(0+1+rn+2, ul+1+rn+2);
//				if(!new String(decode(name64)).equals(username))
//					return false;
//				String time36 = cfgkey.substring(1, 1+rn)+cfgkey.substring(1+2+rn+ul);
//				Date keyTime = new Date(Long.valueOf(rezipNumber(time36)).longValue());
//				if(!DateUtils.timeInRange(keyTime, new Date(), 5))
//					return false;
//				return true;
//			}catch (Exception e) {
//				logger.error("cfgkey解析异常，cfgkey:" + cfgkey + "，调度员：" + username);
//				throw e;
//				}
//		}



	/**
		 * 以三十六进制压缩字符串,主要用于地图图片名称的命名
		 * @param sText
		 * @return
		 */
		public static String zipNumber(String sText){
			long nNum = Long.parseLong(sText);
			String sResult = "";	//定义返回字符
			while(nNum > 35){
				long nMod = nNum % 36;
				if(nMod < 10){		//前10个用数字
					sResult = (char)(nMod + 48) + sResult;
				}else{//后26用大写字符代替
					sResult = (char)(nMod + 65 - 10) + sResult;
				}
				nNum = (long)(nNum / 36);
			}
			if(nNum < 10){
				sResult = (char)(nNum + 48) + sResult;
			}else{
				sResult = (char)(nNum + 65 - 10) + sResult;
			}
			return sResult;
		}
		
		/**
		 * 解压缩三十六进制的字符中,还原文件名
		 * @param sText
		 * @return
		 */
		public static String rezipNumber(String sText){
			long  nReturn = 0;
			for(int i=0; i<sText.length(); i++){
				long tempNum = (long)(sText.charAt(sText.length() - i - 1));
				if(tempNum >= 65){
					tempNum = (long)((tempNum - 65 + 10) * Math.pow(36,i));
				}else{
					tempNum = (long)((tempNum - 48) * Math.pow(36,i));
				}
				nReturn += tempNum;
			}
			
			return String.valueOf(nReturn);

		}
		
		 private static char[] base64EncodeChars = new char[]{
	         'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
	         'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
	         'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
	         'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
	         'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
	         'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
	         'w', 'x', 'y', 'z', '0', '1', '2', '3',
	         '4', '5', '6', '7', '8', '9', '+', '/'};
		
		private static byte[] base64DecodeChars = new byte[]{
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
	        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
	        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
	        -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
	        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
	        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
	        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1};
		/**
		 * 加密
		 *
		 * @param data 明文的字节数组
		 * @return 密文字符串
		 */
		public static String encode(byte[] data) {
		    StringBuffer sb = new StringBuffer();
		    int len = data.length;
		    int i = 0;
		    int b1, b2, b3;
		    while (i < len) {
		        b1 = data[i++] & 0xff;
		        if (i == len) {
		            sb.append(base64EncodeChars[b1 >>> 2]);
		            sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
		            sb.append("==");
		        break;
		    }
		    b2 = data[i++] & 0xff;
		    if (i == len) {
		        sb.append(base64EncodeChars[b1 >>> 2]);
		        sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
		        sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
		        sb.append("=");
		            break;
		        }
		        b3 = data[i++] & 0xff;
		        sb.append(base64EncodeChars[b1 >>> 2]);
		        sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
		        sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
		        sb.append(base64EncodeChars[b3 & 0x3f]);
		    }
		    return sb.toString();
		}
		/**
		 * 解密
		 *
		 * @param str 密文
		 * @return 明文的字节数组
		 * @throws UnsupportedEncodingException
		 */
		public static byte[] decode(String str) throws Exception {
		    StringBuffer sb = new StringBuffer();
		    byte[] data = str.getBytes("US-ASCII");
		int len = data.length;
		int i = 0;
		int b1, b2, b3, b4;
		while (i < len) {
		    /* b1 */
		    do {
		        b1 = base64DecodeChars[data[i++]];
		    } while (i < len && b1 == -1);
		    if (b1 == -1) break;
		    /* b2 */
		    do {
		        b2 = base64DecodeChars
		                [data[i++]];
		    } while (i < len && b2 == -1);
		    if (b2 == -1) break;
		    sb.append((char) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
		    /* b3 */
		    do {
		        b3 = data[i++];
		        if (b3 == 61) return sb.toString().getBytes("iso8859-1");
		        b3 = base64DecodeChars[b3];
		    } while (i < len && b3 == -1);
		    if (b3 == -1) break;
		    sb.append((char) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
		    /* b4 */
		    do {
		        b4 = data[i++];
		        if (b4 == 61) return sb.toString().getBytes("iso8859-1");
		        b4 = base64DecodeChars[b4];
		    } while (i < len && b4 == -1);
		    if (b4 == -1) break;
		    sb.append((char) (((b3 & 0x03) << 6) | b4));
		}
		return sb.toString().getBytes("iso8859-1");
		}
	
	
}
