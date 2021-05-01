import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.*;
import java.util.ArrayList;
import java.util.Vector;

public class Minado extends Thread{

	private static boolean encontrado = false;
	private static long tiempoInicial = 0;
	private static int longitudMensaje = 0;
	private static MessageDigest tipoHash;
	private static boolean entero;
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	private static Object monitor = new Object();

	private int longitudCadena;
	private char[] abecedario= new char[26];
	private String mensaje;
	private double ceros;
	private String algoritmo;
	private volatile boolean fin;

	public Minado(char[] abecedario, int k, String mensaje, double ceros, String algoritmo)
	{
		this.longitudCadena = k;
		this.abecedario = abecedario;
		this.mensaje = mensaje;
		this.ceros = ceros;
		this.algoritmo = algoritmo;
		this.fin = false;
	}

	public void run()
	{
		mineria(abecedario, longitudCadena, mensaje, ceros, algoritmo, fin);
		System.out.println("Termino el thread que revisaba cadenas de longitud " + longitudCadena);
	}

	@SuppressWarnings("finally")
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


	static void mineria(char[] abecedario, int k, String mensaje, double ceros, String algoritmo, boolean fin)
	{
		int n = abecedario.length;
		mineriaRec(abecedario, mensaje, n, k, ceros, algoritmo, fin);
	}


	static void mineriaRec(char[] abecedario, String mensaje, int n, int k, double ceros, String algoritmo, boolean fin)
	{
		synchronized(monitor)
		{
			fin = encontrado;
		}
		if(fin)
		{

		}
		else
		{
			if (k == 0)
			{
				String mensajeConV = mensaje;
				boolean hashSirve = hash(mensajeConV, algoritmo,ceros);
				if(hashSirve)
				{
					long elapsedTime = System.nanoTime() - tiempoInicial;
					synchronized(monitor)
					{
						encontrado = true;	
					}
					System.out.println("El valor "+mensaje.substring(longitudMensaje,mensaje.length())+" permitió cumplir la condición definida");
					System.out.println("El proceso tomó " + elapsedTime/1000000 + "ms" );
				}
				return;
			}
			for (int i = 0; i < n; ++i)
			{
				String nuevoMensaje = mensaje + abecedario[i];
				mineriaRec(abecedario, nuevoMensaje, n, k - 1,ceros,algoritmo,fin);
			}
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


	public static void main(String[] args) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Ingrese el algoritmo de generación de código de Hash (SHA-256 o SHA-512):");

		String algoritmo = reader.readLine();

		System.out.println("Ingrese la cadena que representa los datos de una transacción");
		String mensaje = reader.readLine();

		System.out.println("Ingrese el número de 0's esperado");
		int ceros = Integer.parseInt(reader.readLine());

		longitudMensaje = mensaje.length();
		char[] abecedario = {'a', 'b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
		Minado[] threads = new Minado[7]; 
		tiempoInicial = System.nanoTime();
		for(int i =0; i<7;i++)
		{
			threads[i] = new Minado(abecedario, i+1, mensaje, ceros, algoritmo);
		}

		for(int i =0; i<7;i++)
		{
			threads[i].start();
		}
	}

}
