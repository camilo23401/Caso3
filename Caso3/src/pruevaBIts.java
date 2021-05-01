import java.security.MessageDigest;



public class pruevaBIts {
	
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
	public static boolean hash(String mensaje, String algoritmo, double ceros)
	{
		boolean hashFinal = false;
		try
		{
			MessageDigest tipoHash = MessageDigest.getInstance(algoritmo);
			byte[] hashInicial = tipoHash.digest(mensaje.getBytes());
			int numCeros = 0;
			String hex = bytesToHex(hashInicial);
			
			for(int i = 0; i<hex.length();i++)
			{
				if(hex.charAt(i)=='0')
				{
					numCeros+=4;
				}
				else {
					i = hex.length();
				}
			}
			if(numCeros == ceros)
			{
				hashFinal = true;
			}
		}
		catch(Exception e)
		{
			e.getStackTrace();
		}
		finally 
		{
			return hashFinal;
		}
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for (int j = 0; j < bytes.length; j++) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
	        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
	    }
	    return new String(hexChars);
	}



	public static void main(String[] args) {
		
		System.out.println(hash("usepullrequesttomergeuabtjva", "SHA-512", 20));
		// TODO Auto-generated method stub

	}

}
