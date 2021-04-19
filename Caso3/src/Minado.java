import java.math.BigInteger;
import java.security.*;

public class Minado {
	
	@SuppressWarnings("finally")
	public static String hash(String mensaje, String algoritmo)
	{
		String hashFinal = "";
		try
		{
			MessageDigest tipoHash = MessageDigest.getInstance(algoritmo);
			byte[] hashInicial = tipoHash.digest(mensaje.getBytes());
			BigInteger hashComoNumero = new BigInteger(1, hashInicial);
			hashFinal = hashComoNumero.toString(16);
			while(hashFinal.length()< 32)
			{
				hashFinal = "0"+hashFinal;
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

	public static void main(String[] args) {
		System.out.println(hash("GeeksForGeeks","SHA-512"));
	}

}
