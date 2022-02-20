package ru.voronkov.client.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.input.ReversedLinesFileReader;

public class ClientHistory implements AutoCloseable {

    private static final String HISTORY_CHAT_S_TXT = "./chatHistory/chat_%s.txt";
    private final String clientName;
    private PrintWriter printWriter;
    private File historyFile;

    public ClientHistory(String clientName) {
        this.clientName = clientName;
    }

    public void init() {
        try {
            historyFile = createHistoryFile();
            this.printWriter = new PrintWriter(new BufferedWriter(new FileWriter(historyFile, StandardCharsets.UTF_8, true)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createHistoryFile() throws IOException {
        String filePath = String.format(HISTORY_CHAT_S_TXT, clientName);
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    public void appendText(String text) {
        printWriter.print(text);
        printWriter.flush();
    }

    public String loadLastRows2(int rowsNumber) {
        List<String> result = new ArrayList<>(rowsNumber);
        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(historyFile, 4096, StandardCharsets.UTF_8)) {
            for (int i = 0; i < rowsNumber; i++) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.reverse(result);

        return String.join(System.lineSeparator(), result);
    }

    public String loadLastRows(int rowsNumber) {
        try(RandomAccessFile raf = new RandomAccessFile(historyFile, "r")) {
            long pointer;
            int count = 0;

            for (pointer = raf.length() - 1; pointer > 0; pointer--) {
                raf.seek(pointer);
                if (raf.read() == '\n') {
                    count++;
                }

                if (count == rowsNumber) {
                    break;
                }
            }

            if (pointer >= 0) {
                raf.seek(pointer);
            }

            byte[] resultData = new byte[(int) (raf.length() - raf.getFilePointer())];
            raf.read(resultData);

            return new String(resultData, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public void close() {
        if (printWriter != null) {
            printWriter.close();
        }
    }
}
