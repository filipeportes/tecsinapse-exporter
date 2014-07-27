package br.com.tecsinapse.exporter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.common.base.Strings;

public class CSVUtil {

	public static final String SEPARATOR = ";";
	public static final char DEFAULT_SEPARATOR_CHAR = ';';

    public static List<String> processInputCSV(InputStream inputStream, Charset charset) throws IOException {
        return processInputCSV(inputStream, true, charset);
    }

    public static List<String> processCSV(InputStream inputStream, Charset charset) throws IOException {
        return processInputCSV(inputStream, false, charset);
    }

    private static List<String> processInputCSV(InputStream inputStream, boolean ignoreFirstLine, Charset charset) throws IOException {
        List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset)) ){

		    String line = null;
            if (ignoreFirstLine) {
                line = br.readLine();
            }

            while ((line = br.readLine()) != null) {

				if (!line.isEmpty() && line.split(SEPARATOR).length > 0) {
					lines.add(line);
				}
			}
		} catch (IOException e) {
			throw e;
		}

		return lines;
	}

	public static String getColumnValue(String[] columns, int index) {
		if (index >= columns.length || Strings.isNullOrEmpty(columns[index])) {
			return null;
		}

		return columns[index].trim();

	}

	public static List<String> simpleLine(Object[] fields) throws Exception {
		List<String> list = new ArrayList<>();
		if (fields != null && fields.length > 0) {
			for (Object o : fields) {
				if (o == null) {
					list.add("");
				}
				else {
					list.add(o.toString());
				}
			}
		}
		return list;
	}

	public static <T> File writeCsv(List<List<T>> csv, String fileName, String chartsetName)
			throws IOException {
		File file = new File(fileName + ".csv");
		OutputStream o = new FileOutputStream(file);
		write(csv, o, chartsetName);
		return file;
	}

	public static <T> void write(List<List<T>> csv, OutputStream o, String chartsetName, char separator)
			throws UnsupportedEncodingException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(o, chartsetName)) {
			for (List<T> row : csv) {
				StringBuilder line = new StringBuilder();
				for (Iterator<T> iter = row.iterator(); iter.hasNext();) {
					String field = String.valueOf(iter.next())
							.replace("\"", "\"\"");
					if (field.indexOf(separator) > -1 || field.indexOf('"') > -1) {
						field = '"' + field + '"';
					}
					line.append(field);
					if (iter.hasNext()) {
						line.append(separator);
					}
				}
				line.append("\n");
				writer.write(line.toString());
			}
			writer.flush();
		}
	}

	public static <T> void write(List<List<T>> csv, OutputStream o, String chartsetName)
			throws UnsupportedEncodingException, IOException {
		write(csv, o, chartsetName, DEFAULT_SEPARATOR_CHAR); 
	}
}