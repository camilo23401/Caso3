import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.*;


public class Minado extends Thread{

	private static boolean encontrado = false;
	private static long tiempoInicial = 0;
	private static int longitudMensaje = 0;
	private static final char[] POSIBLESHEX = "0123456789ABCDEF".toCharArray();
	private static Object monitor = new Object();

	
	private int longitudCadena;
	private char[] abecedario= new char[26];
	private String mensaje;
	private double ceros;
	private String algoritmo;
	private boolean fin;
	private char letra;

	public Minado(char[] abecedario, int k, String mensaje, double ceros, String algoritmo, char letra)
	{
		this.longitudCadena = k;
		this.abecedario = abecedario;
		this.mensaje = mensaje;
		this.ceros = ceros;
		this.algoritmo = algoritmo;
		this.fin = false;
		this.letra = letra;
	}

	public void run()
	{
		mineria(abecedario, longitudCadena, mensaje, ceros, algoritmo, fin, letra);
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
			String hex = bytesAHexa(hashInicial);

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


	static void mineria(char[] abecedario, int k, String mensaje, double ceros, String algoritmo, boolean fin, char letra)
	{
		mineriaRec(abecedario, mensaje, abecedario.length, k, ceros, algoritmo, fin,letra);
	}


	static void mineriaRec(char[] abecedario, String mensaje, int n, int k, double ceros, String algoritmo, boolean fin,char letra)
	{
		synchronized(monitor)
		{
			fin = encontrado;
		}
		if(!fin)
		{
			if (k == 0)
			{
				String mensajeConV = "";
				String letraExtra = "";
				if(letra!='Z')
				{
					letraExtra = ""+letra;
					String cadenaDeSeis =mensaje.substring(longitudMensaje,mensaje.length());
					String mensajeOriginal = mensaje.substring(0,longitudMensaje);
					mensajeConV=mensajeOriginal+letra+cadenaDeSeis;
				}
				else
				{
					mensajeConV=mensaje;
				}
				boolean hashSirve = hash(mensajeConV, algoritmo,ceros);
				if(hashSirve)
				{
					long elapsedTime = System.nanoTime() - tiempoInicial;
					synchronized(monitor)
					{
						encontrado = true;	
					}
					System.out.println("El valor "+letraExtra+mensaje.substring(longitudMensaje,mensaje.length())+" permitió cumplir la condición definida.");
					System.out.println("Entonces, la cadena que cumple la condición es: "+mensajeConV);
					System.out.println("El proceso tomó " + elapsedTime/1000000 + "ms" );
				}
				return;
			}
			for (int i = 0; i < n; ++i)
			{
				mineriaRec(abecedario, mensaje + abecedario[i] , n, k - 1,ceros,algoritmo,fin, letra);
			}
		}

	}

	public static String bytesAHexa(byte[] bytes) {
		char[] hex = new char[bytes.length * 2];
		for (int i = 0; i < bytes.length; i++) 
		{	
			int aux = bytes[i] & 0xFF;
			hex[i * 2] = POSIBLESHEX[aux >>> 4];
			hex[i * 2 + 1] = POSIBLESHEX[aux & 0x0F];
		}
		return new String(hex);
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


		Minado[] threads = new Minado[34]; 
		tiempoInicial = System.nanoTime();
		for(int i=0; i<6;i++)
		{
			threads[i] = new Minado(abecedario, i+1, mensaje, ceros, algoritmo,'Z');
		}
		int aux = 6;
		for(char i='a'; i <= 'z'; i++)
		{
			threads[aux] = new Minado(abecedario, 6, mensaje, ceros, algoritmo,i);
			aux++;
		}
		for(int i=0; i<32;i++)
		{
			threads[i].start();
		}
		try
		{
			int contadorThreads = 0;
			for (Thread thread : threads) {
				thread.join();
				contadorThreads++;
				if(!encontrado && contadorThreads==32)
				{
					long elapsedTime = System.nanoTime() - tiempoInicial;
					System.out.println("No se encontró respuesta. Se demoró "+elapsedTime/1000000+"ms");
				}
			}
		}catch(Exception e)
		{
			e.getStackTrace();
		}

	}

}
