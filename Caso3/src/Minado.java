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

	private static Object monitor = new Object();

	private int longitudCadena;
	private char[] abecedario= new char[26];
	private String mensaje;
	private int ceros;
	private String algoritmo;

	public Minado(char[] abecedario, int k, String mensaje, int ceros, String algoritmo)
	{
		this.longitudCadena = k;
		this.abecedario = abecedario;
		this.mensaje = mensaje;
		this.ceros = ceros;
		this.algoritmo = algoritmo;
	}

	public void run()
	{
		mineria(abecedario, longitudCadena, mensaje, ceros, algoritmo);
		System.out.println("Termino el thread que revisaba cadenas de longitud " + longitudCadena);
	}

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
			while(hashFinal.length()< 128)
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


	static void mineria(char[] abecedario, int k, String mensaje, int ceros, String algoritmo)
	{
		int n = abecedario.length;
		mineriaRec(abecedario, mensaje, n, k, ceros, algoritmo);
	}


	static void mineriaRec(char[] abecedario, String mensaje, int n, int k, int ceros, String algoritmo)
	{
		boolean fin;
		synchronized(monitor)
		{
			fin = encontrado;
		}
		if(encontrado)
		{

		}
		else
		{
			if (k == 0)
			{
				String mensajeConV = mensaje;
				String hashResultante = hash(mensajeConV, algoritmo);
				int contadorCeros = 0;
				int aux = 0;
				char revision = hashResultante.charAt(aux);
				while(revision=='0')
				{
					contadorCeros++;
					aux++;
					revision = hashResultante.charAt(aux);
				}
				if(contadorCeros == ceros)
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
				mineriaRec(abecedario, nuevoMensaje, n, k - 1,ceros,algoritmo);
			}
		}

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
