package com.example.minesweeper;

import java.io.*;

public class FileController {

    private static String FILE_NAME = "tileMap.txt";

    public void write(Tile[][] tileMap){
        clearFile();
        SerializedTile serTileMap[][] = new SerializedTile[30][16];

        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 16; j++){
                serTileMap[i][j] = tileMap[i][j].convert();
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(serTileMap);

            objectOutputStream.close();
            fileOutputStream.close();
        }
        catch (FileNotFoundException e){
            System.out.println("File not found");
        }
        catch (IOException e){
            System.out.println("Output failure");
        }
    }

    public Tile[][] read() throws IOException, ClassNotFoundException {

        Tile tileMap[][] = new Tile[30][16];
        SerializedTile readTileMap[][];

        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        readTileMap = (SerializedTile[][]) objectInputStream.readObject();

        fileInputStream.close();
        objectInputStream.close();

        for (int i = 0; i < 30; i++){
            for (int j = 0; j < 16; j++){
                tileMap[i][j] = readTileMap[i][j].convert();
            }
        }

        clearFile();
        return tileMap;
    }

    private static void clearFile() {
        try{
            FileWriter fw = new FileWriter(FILE_NAME, false);
            PrintWriter pw = new PrintWriter(fw, false);

            pw.flush();
            pw.close();
            fw.close();
        }catch(Exception exception){
            System.out.println("Exception have been caught");
        }

    }
}
