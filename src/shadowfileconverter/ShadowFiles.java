/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package shadowfileconverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Ruslan Feshchenko
 * @version
 */
public class ShadowFiles {
    
    private boolean write;
    private boolean binary;
    private File file=null;
    private Object stream=null;

    /**
     * Main constructor
     * @param wr false - open for reading, true - open for writing
     * @param bi false - open for text I/O, true - open for binary I/O
     * @throws java.io.FileNotFoundException
     */
    public ShadowFiles(boolean wr, boolean bi) throws FileNotFoundException {
        this.write=wr;
        this.binary=bi;
        if (write) {
            if (binary) {
                if (openWrite("Choose a binary file to save a ray set in")) {
                    stream=new DataOutputStream(new FileOutputStream(file, false));
                }      
            } else {
                
            }
 
        } else {
            if (binary) {
                
            } else {
                
            }
        }        
    }

    /**
     * Closes I/O stream
     * @throws IOException
     */
    public void close() throws IOException {
        if (write) {
            if (binary) {
                ((DataOutputStream)stream).close();
            } else {
                
            }
        } else {
            if (binary) {
                
            } else {
                
            }
        }          
    }
    
    /**
     * Writes binary data for one ray or the file heading
     * @param rayData double array of 18 numbers representing 18 columns of ray data
     * @throws java.io.IOException
     */
    public void write(double [] rayData) throws IOException {
        if (binary) {
            ((DataOutputStream)stream).write(new byte [] {18,0,0,0});
            for (int i=0; i<rayData.length; i++) {
                ((DataOutputStream)stream).writeDouble(rayData[i]);
            }
            ((DataOutputStream)stream).write(new byte [] {18,0,0,0});
            ((DataOutputStream)stream).writeInt(0);
        }
    }
    
    /**
     * Reads binary data of one ray or of the file heading
     * @param rayData double array of 18 numbers representing 18 columns of ray data
     */
    public void read(double [] rayData) {
        
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
}
