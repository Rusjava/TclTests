/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shadowfileconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.InputMismatchException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.EOFException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.Formatter;
import java.util.Scanner;

/**
 *
 * @author Ruslan Feshchenko
 * @version 0.1
 */
public class ShadowFiles {
    
    private final boolean write;
    private final boolean binary;
    private File file=null;
    private Object stream=null;
    private int ncol;
    private int nrays;
    private int rayCounter;
    //private Counter rayCounter;

    /**
     * Main constructor
     * @param write false - open for reading, true - open for writing
     * @param binary false - open for text I/O, true - open for binary I/O
     * @param ncol number of columns
     * @param nrays number of rays
     * @throws java.io.FileNotFoundException
     * @throws shadowfileconverter.ShadowFiles.EndOfLineException thrown when end of line is reached
     * @throws shadowfileconverter.ShadowFiles.FileIsCorruptedException thrown when the integer column or ray number can not be interpreted
     * @throws shadowfileconverter.ShadowFiles.FileNotOpenedException thrown when the user cancels file opening
     */
    public ShadowFiles(boolean write, boolean binary, int ncol, int nrays) throws FileNotFoundException, 
            IOException, EndOfLineException, FileIsCorruptedException, FileNotOpenedException {
        this.write=write;
        this.binary=binary;
        this.ncol=ncol;
        this.nrays=nrays;
        this.rayCounter=0;
        if (write) {
            if (binary) {
                if (openWrite("Choose a binary file to save a ray set in")) {
                    stream=new DataOutputStream(new FileOutputStream(file, false));
                    ((DataOutputStream)stream).write(new byte [] {12,0,0,0});
                    ((DataOutputStream)stream).writeInt(Integer.reverseBytes(ncol));
                    ((DataOutputStream)stream).writeInt(Integer.reverseBytes(nrays));
                    ((DataOutputStream)stream).writeInt(0);
                    ((DataOutputStream)stream).write(new byte [] {12,0,0,0});
                }  else {
                    throw new FileNotOpenedException();
                }    
            } else {
                 if (openWrite("Choose a text file to save a ray set in")) {
                     stream=new PrintWriter(new FileWriter(file, false));
                     Formatter fm=new Formatter();
                     fm.format("%d %d", ncol, nrays);
                     ((PrintWriter)stream).println(fm);
                 }  else {
                    throw new FileNotOpenedException();
                }        
            }
 
        } else {
            if (binary) {
                int tmp;
                if (openRead("Choose a binary file with ray data")) {
                    stream=new DataInputStream(new FileInputStream(file));
                    tmp=((DataInputStream)stream).readInt();
                    this.ncol=Math.min(Integer.reverseBytes(((DataInputStream)stream).readInt()), ncol);
                    this.nrays=Math.min(Integer.reverseBytes(((DataInputStream)stream).readInt()), nrays);
                    tmp=((DataInputStream)stream).readInt();
                    tmp=((DataInputStream)stream).readInt();
                }  else {
                    throw new FileNotOpenedException();
                }   
            } else {
                if (openRead("Choose a text file with ray data")) {
                    Scanner header;
                    stream=new BufferedReader(new FileReader(file));
                    String line=((BufferedReader)stream).readLine();
                    if (line==null) {
                        throw new EOFException();
                    }
                    header=new Scanner(line);
                    try {
                        this.ncol=Math.min(header.nextInt(), ncol);
                        this.nrays=Math.min(header.nextInt(), nrays);
                    } catch (InputMismatchException e) {
                        throw new FileIsCorruptedException(rayCounter);
                    } catch (NoSuchElementException e) {
                        throw new EndOfLineException(rayCounter);
                    } 
                }   else {
                    throw new FileNotOpenedException();
                }   
            }
        }        
    }

    /**
     * Closes I/O stream
     * @throws IOException
     */
    public void close() throws IOException {
        if (stream!=null) {
            if (write) {
                if (binary) {
                    ((DataOutputStream)stream).close();
                } else {
                    ((PrintWriter)stream).close();
                }
            } else {
                if (binary) {
                    ((DataInputStream)stream).close();
                } else {
                    ((BufferedReader)stream).close();
                }
            } 
        }
    }
    
    /**
     * Writes binary data for one ray or the file heading
     * @param rayData double array of 18 numbers representing 18 columns of ray data
     * @throws java.io.IOException
     */
    public void write(double [] rayData) throws IOException {
        int nread=Math.min(rayData.length, ncol);
        rayCounter++;
        if (binary) {
            ((DataOutputStream)stream).write(new byte [] {12,0,0,0});
            for (int i=0; i<nread; i++) {
                ((DataOutputStream)stream).
                        writeLong(Long.reverseBytes(Double.doubleToLongBits(rayData[i])));
            }
            ((DataOutputStream)stream).write(new byte [] {12,0,0,0});
        } else {
            Formatter fm=new Formatter();
            for (int i=0; i<nread; i++) {
                fm.format("%f ", rayData[i]);
            }
            ((PrintWriter)stream).println(fm);
        }
    }
    
    /**
     * Reads binary data of one ray or of the file heading
     * @param rayData double array of 18 numbers representing 18 columns of ray data
     * @throws java.io.IOException
     * @throws shadowfileconverter.ShadowFiles.EndOfLineException thrown when end of line is reached
     * @throws shadowfileconverter.ShadowFiles.FileIsCorruptedException thrown when the integer column or ray number can not be interpreted
     */
    public void read(double [] rayData) throws IOException, EndOfLineException, FileIsCorruptedException {
        int tmp, nread=Math.min(rayData.length, ncol);
        rayCounter++;
        if (binary) {
            tmp=((DataInputStream)stream).readInt();
            if (tmp==0) {
                throw new EOFException();
            }
            for (int i=0; i<nread; i++) {
                rayData[i]=Double.longBitsToDouble(Long.reverseBytes(((DataInputStream)stream).readLong()));
            }
            tmp=((DataInputStream)stream).readInt();
        } else {
            Scanner header;
            String line=((BufferedReader)stream).readLine();
            if (line==null) {
                throw new EOFException ();
            }
            header=new Scanner(line);
            for (int i=0; i<nread; i++) {
                try {
                    rayData[i]=header.nextDouble();
                } catch (InputMismatchException e) {
                    throw new FileIsCorruptedException(rayCounter);
                } catch (NoSuchElementException e) {
                    throw new EndOfLineException(rayCounter);
                }
            }
        }
    }
    
    public int getNcol() {
        return ncol;
    }
    
    public int getNrays() {
        return nrays;
    }
    
    private boolean openWrite(String title) {
        JFileChooser fo=new JFileChooser ();
        fo.setDialogTitle(title);
        int ans=fo.showOpenDialog(null);   
        if (ans==JFileChooser.APPROVE_OPTION) {
            file=fo.getSelectedFile();
            if (file.exists()) {
                int n=JOptionPane.showConfirmDialog(null, "The file already exists. Overwrite?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (n==JOptionPane.NO_OPTION) {
                    file=null;
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean openRead(String title) {
        JFileChooser fo=new JFileChooser ();
        fo.setDialogTitle(title);
        int ans=fo.showOpenDialog(null);   
        if (ans==JFileChooser.APPROVE_OPTION) {
            file=fo.getSelectedFile();
            return true;
        }
        return false;
    }
    
    /**
     * Class for exception when the number of columns is less than specified
     */
    public static class EndOfLineException extends Exception {

        /**
         * The current ray number
         */
        public int rayNumber;

        /**
         * 
         * @param rayNumber current ray number
         */
        public EndOfLineException (int rayNumber) {
            super();
            this.rayNumber=rayNumber;
        }
    }
    
    /**
     * Class for exception when the text file can not be read due to data corruption
     */
    public static class FileIsCorruptedException extends Exception {

        /**
         * The current ray number
         */
        public int rayNumber;

        /**
         *
         * @param rayNumber current ray number
         */
        public FileIsCorruptedException (int rayNumber) {
            super();
            this.rayNumber=rayNumber;
        }
    }
    
    /**
     * Class for exception thrown when the user cancels file opening
     */
    public static class FileNotOpenedException extends Exception {

        /**
         * Empty constructor
         */
        public FileNotOpenedException () {
            super();
        }
    }
}
