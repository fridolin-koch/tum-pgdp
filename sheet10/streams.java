package sheet10;

import java.io.*;

public class streams {

	final static String name = "FileDemo.dat";

	// Eigene Exception definieren:
	static class MyIOException extends Exception
	{
		public String info;

		MyIOException(String inpo)
		{
			this.info=info;
		}
	}

	public static void main(String args[]) throws MyIOException {
		File textFile;   // Textdatei
		// Es gibt diverse Stream Typen um diverse Datentypen zu verarbeiten, mit und ohne Formatierung
		DataInputStream in = null;   // ungetypter Dateieingabe-Stream
		FileWriter out = null;  // Dateiausgabe-Stream
		// Standardeingabe in Reader umwandeln
		BufferedReader stdin;
		PrintWriter stdout;

		try {
			stdin = new BufferedReader(
					new InputStreamReader(System.in));
			stdout = new PrintWriter(System.out, true);
		} catch (OutOfMemoryError oome) {
			return; // Hier kann man nicht viel tun (evtl. grosses caches frei geben o.ä. und es nochmal versuchen)
		}

		// Erzeugen eines Ausgabe-Streams, der mit der Datei
		// 'FileDemo.dat' verknüpft ist
		try {
			out = new FileWriter(name);
			int key = 0;
			// Einlesen von Text von der Standardeingabe
			// bis zur Eingabe eines x und
			// gleichzeitiges Abspeichern des Textes
			do {
				try {
					key = stdin.read();
					out.write(key);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} while ((char) key != 'x');
			out.close();        // Schliessen der Datei
			// Erzeugen eines Eingabe-Streams, der mit
			// der Datei verknüpft ist
			textFile = new File(name);
			try {
				//DataInputStream liest unsrukturierte Daten (keine Header Info/Typsicherheit)
				in = new DataInputStream(
					 new BufferedInputStream(new FileInputStream(textFile)));
			} catch (FileNotFoundException ex) {
				System.out.println(textFile + " existiert nicht im aktuellen Verzeichnis!");
			}
			int size = (int) textFile.length(); // Dateilänge
			int read = 0;    // Anzahl der gelesenen Zeichen
			byte data[] = new byte[size];      // Lesepuffer
			// Auslesen der Datei
			while (read < size) {
				float f=in.readFloat(); // Interpretiere 4 bytes als float
				stdout.println(f); //was muss man tippen damit man "-365.592" bekommt?? :)
				read+=4; //1 Float = 4 Bytes
				int r = in.read(data, read, size - read);
				// Gibt Anzahl der gelesenen Bytes (<= size - read) oder -1 bei EOF zurück
				if (r > 0) {
					read += r;
				} else {
					break; // EOF
				}
			}
			in.close();
			// Ausgabe des Lesepuffers
			stdout.println(new String(data));
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new MyIOException("io exception occured");
		} finally {
			//try closing streams that might still be open
			try {
				out.close();
			} catch (Exception e) {//closing might fail due to out==null or various other reasons
			}
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}
}

