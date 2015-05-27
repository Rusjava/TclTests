/*
 * General class for reading/writing Shadow files
 */
package shadowfileconverter;

import java.io.File;
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
import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.util.Formatter;
import java.util.Scanner;
import javax.swing.SwingUtilities;

/**
 *
 * @author Ruslan Feshchenko
 * @version 1.1
 */
public class ShadowFiles implements Closeable {

    protected final boolean write;
    protected final boolean binary;
    protected File file = null;
    protected Object stream = null;
    protected int ncol;
    protected byte rLength;
    protected int nrays;
    protected int rayCounter;

    /**
     * Main constructor
     *
     * @param write false - open for reading, true - open for writing
     * @param binary false - open for text I/O, true - open for binary I/O
     * @param ncol number of columns
     * @param nrays number of rays
     * @param pFile default file
     * @throws java.io.FileNotFoundException
     * @throws shadowfileconverter.ShadowFiles.EndOfLineException thrown when
     * end of line is reached
     * @throws shadowfileconverter.ShadowFiles.FileIsCorruptedException thrown
     * when the integer column or ray number can not be interpreted
     * @throws shadowfileconverter.ShadowFiles.FileNotOpenedException thrown
     * when the user cancels file opening
     * @throws java.lang.InterruptedException
     * @throws java.lang.reflect.InvocationTargetException
     */
    public ShadowFiles(boolean write, boolean binary, int ncol, int nrays, File pFile) throws IOException,
            EndOfLineException, FileIsCorruptedException, FileNotOpenedException, InterruptedException, InvocationTargetException {
        this.write = write;
        this.binary = binary;
        this.ncol = ncol;
        this.rLength = (byte) (ncol * 8 - 256);
        this.nrays = nrays;
        this.rayCounter = 0;
        this.file = pFile;
        if (write) {
            if (binary) {
                if (openWrite("Choose a binary file to save a ray set in")) {
                    stream = new DataOutputStream(new FileOutputStream(file, false));
                    ((DataOutputStream) stream).write(new byte[]{12, 0, 0, 0});
                    ((DataOutputStream) stream).writeInt(Integer.reverseBytes(ncol));
                    ((DataOutputStream) stream).writeInt(Integer.reverseBytes(nrays));
                    ((DataOutputStream) stream).writeInt(0);
                    ((DataOutputStream) stream).write(new byte[]{12, 0, 0, 0});
                } else {
                    throw new FileNotOpenedException();
                }
            } else {
                if (openWrite("Choose a text file to save a ray set in")) {
                    stream = new PrintWriter(new FileWriter(file, false));
                    Formatter fm = new Formatter();
                    fm.format("%d %d", ncol, nrays);
                    ((PrintWriter) stream).println(fm);
                } else {
                    throw new FileNotOpenedException();
                }
            }
        } else {
            if (binary) {
                byte[] tmp = new byte[4];
                if (openRead("Choose a binary file with ray data")) {
                    stream = new DataInputStream(new FileInputStream(file));
                    if (((DataInputStream) stream).read(tmp, 0, 4) < 4 || tmp[0] != 12) {
                        throw new FileIsCorruptedException(0);
                    }
                    this.ncol = Math.min(Integer.reverseBytes(((DataInputStream) stream).readInt()), ncol);
                    this.nrays = Math.min(Integer.reverseBytes(((DataInputStream) stream).readInt()), nrays);
                    if (((DataInputStream) stream).read(tmp, 0, 4) < 4 || tmp[0] != 0) {
                        throw new FileIsCorruptedException(0);
                    }
                    if (((DataInputStream) stream).read(tmp, 0, 4) < 4 || tmp[0] != 12) {
                        throw new FileIsCorruptedException(0);
                    }
                } else {
                    throw new FileNotOpenedException();
                }
            } else {
                if (openRead("Choose a text file with ray data")) {
                    Scanner header;
                    stream = new BufferedReader(new FileReader(file));
                    String line = ((BufferedReader) stream).readLine();
                    if (line == null) {
                        throw new EOFException();
                    }
                    header = new Scanner(line);
                    try {
                        this.ncol = Math.min(header.nextInt(), ncol);
                        this.nrays = Math.min(header.nextInt(), nrays);
                    } catch (InputMismatchException e) {
                        throw new FileIsCorruptedException(rayCounter);
                    } catch (NoSuchElementException e) {
                        throw new EndOfLineException(rayCounter);
                    }
                } else {
                    throw new FileNotOpenedException();
                }
            }
        }
    }

    /**
     * Constructor without default path
     *
     * @param write
     * @param binary
     * @param ncol
     * @param nrays
     * @throws IOException
     * @throws EndOfLineException
     * @throws FileIsCorruptedException
     * @throws FileNotOpenedException
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public ShadowFiles(boolean write, boolean binary, int ncol, int nrays) throws IOException,
            EndOfLineException, FileIsCorruptedException, FileNotOpenedException, InterruptedException, InvocationTargetException {
        this(write, binary, ncol, nrays, null);
    }

    /**
     * Closes I/O stream
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (stream != null) {
            if (write) {
                if (binary) {
                    ((DataOutputStream) stream).close();
                } else {
                    ((PrintWriter) stream).close();
                }
            } else {
                if (binary) {
                    ((DataInputStream) stream).close();
                } else {
                    ((BufferedReader) stream).close();
                }
            }
        }
    }

    /**
     * Writes binary data for one ray or the file heading
     *
     * @param rayData double array of 18 numbers representing 18 columns of ray
     * data
     * @throws java.io.IOException
     */
    public void write(double[] rayData) throws IOException {
        int nread = Math.min(rayData.length, ncol);
        rayCounter++;
        if (binary) {
            ((DataOutputStream) stream).write(new byte[]{rLength, 0, 0, 0});
            for (int i = 0; i < nread; i++) {
                ((DataOutputStream) stream).
                        writeLong(Long.reverseBytes(Double.doubleToLongBits(rayData[i])));
            }
            ((DataOutputStream) stream).write(new byte[]{rLength, 0, 0, 0});
        } else {
            Formatter fm = new Formatter();
            for (int i = 0; i < nread; i++) {
                fm.format("%f ", rayData[i]);
            }
            ((PrintWriter) stream).println(fm);
        }
    }

    /**
     * Reads binary data of one ray or of the file heading
     *
     * @param rayData double array of 18 numbers representing 18 columns of ray
     * data
     * @throws java.io.EOFException when there are no more rays
     * @throws java.io.IOException
     * @throws shadowfileconverter.ShadowFiles.EndOfLineException thrown when
     * the number of columns is less then specified
     * @throws shadowfileconverter.ShadowFiles.FileIsCorruptedException thrown
     * when the record can not be interpreted
     */
    public void read(double[] rayData) throws EOFException, IOException, EndOfLineException, FileIsCorruptedException {
        int nread = Math.min(rayData.length, ncol);
        rayCounter++;
        if (binary) {
            byte[] tmp = new byte[4];
            int bNumber;
            /* 
             * Reading record length of Fortran77 binary format and checking if it is 144
             */
            bNumber = ((DataInputStream) stream).read(tmp, 0, 4);
            if (bNumber == 0) {
                throw new EOFException();
            }
            if (bNumber < 4 || tmp[0] != rLength) {
                throw new FileIsCorruptedException(rayCounter);
            }
            /* 
             * Reading columns and throwing an exception if the tend of file is reached
             */
            try {
                for (int i = 0; i < nread; i++) {
                    rayData[i] = Double.longBitsToDouble(Long.reverseBytes(((DataInputStream) stream).readLong()));
                }
            } catch (EOFException ex) {
                throw new EndOfLineException(rayCounter);
            }
            /* 
             * Reading record length of Fortran77 binary format and checking if it is 144
             */
            bNumber = ((DataInputStream) stream).read(tmp, 0, 4);
            if (bNumber < 4 || tmp[0] != rLength) {
                throw new FileIsCorruptedException(rayCounter);
            }
        } else {
            Scanner lineScanner;
            String line = ((BufferedReader) stream).readLine();
            if (line == null) {
                throw new EOFException();
            }
            lineScanner = new Scanner(line);
            for (int i = 0; i < nread; i++) {
                try {
                    rayData[i] = lineScanner.nextDouble();
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

    /**
     * Opening file for writing
     *
     * @param title
     * @return
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    protected boolean openWrite(String title) throws InterruptedException, InvocationTargetException {
        final Answer ans = new Answer();
        final JFileChooser fo = new JFileChooser(file);
        fo.setDialogTitle(title);
        if (SwingUtilities.isEventDispatchThread()) {
            ans.ans = fo.showSaveDialog(null);
        } else {
            SwingUtilities.invokeAndWait(() -> ans.ans = fo.showSaveDialog(null));
        }
        if (ans.ans == JFileChooser.APPROVE_OPTION) {
            file = fo.getSelectedFile();
            if (file.exists()) {
                if (SwingUtilities.isEventDispatchThread()) {
                    ans.ans = JOptionPane.showConfirmDialog(null, "The file already exists. Overwrite?", "Warning",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                } else {
                    SwingUtilities.invokeAndWait(() -> ans.ans = JOptionPane.showConfirmDialog(null, "The file already exists. Overwrite?", "Warning",
                            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE));
                }
                if (ans.ans == JOptionPane.NO_OPTION) {
                    file = null;
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Opening file for reading
     *
     * @param title
     * @return
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    protected boolean openRead(String title) throws InterruptedException, InvocationTargetException {
        final Answer ans = new Answer();
        final JFileChooser fo = new JFileChooser(file);
        fo.setDialogTitle(title);
        if (SwingUtilities.isEventDispatchThread()) {
            ans.ans = fo.showOpenDialog(null);
        } else {
            SwingUtilities.invokeAndWait(() -> ans.ans = fo.showOpenDialog(null));
        }
        if (ans.ans == JFileChooser.APPROVE_OPTION) {
            file = fo.getSelectedFile();
            return true;
        }
        return false;
    }

    /**
     * Returning the file
     *
     * @return
     */
    public File getFile() {
        return file;
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
        public EndOfLineException(int rayNumber) {
            super();
            this.rayNumber = rayNumber;
        }
    }

    /**
     * Class for exception when the text/binary file can not be read due to data
     * corruption
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
        public FileIsCorruptedException(int rayNumber) {
            super();
            this.rayNumber = rayNumber;
        }
    }

    /**
     * Class for exception thrown when the user cancels file opening
     */
    public static class FileNotOpenedException extends Exception {

        /**
         * Empty constructor
         */
        public FileNotOpenedException() {
            super();
        }
    }

    /**
     * Class for passing integer numbers from inner classes
     */
    protected class Answer {

        public int ans;
    }
}
