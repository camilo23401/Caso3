import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
			while(hashFinal.length()< 64)
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
	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Ingrese el algoritmo de generación de código de Hash (SHA-256 o SHA-512):");
		// Reading data using readLine
		String algoritmo = reader.readLine();
		
		System.out.println("Ingrese la cadena que representa los datos de una transacción");
		String mensaje = reader.readLine();
		
		System.out.println("Ingrese el número de 0's esperado");
		int ceros = Integer.parseInt(reader.readLine());

		for(char valor = 'a'; valor <='z'; valor++ )
		{
			String mensajeConV = mensaje + valor;
			String hashResultante = hash(mensajeConV, algoritmo);
			int contadorCeros = 0;
			int i = 0;
			char revision = hashResultante.charAt(i);
			while(revision=='0')
			{
				contadorCeros++;
				i++;
				revision = hashResultante.charAt(i);
			}
			if(contadorCeros == ceros)
			{
				System.out.println("El valor "+valor+" permitió cumplir la condición definida");
				System.out.println(hashResultante);
			}
		}
	}

}
