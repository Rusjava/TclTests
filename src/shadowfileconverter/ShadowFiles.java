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
    private File file;
    private Object stream;

    /**
     * Main constructor
     * @param write false - open for reading, true - open for writing
     * @param binary false - open for text I/O, true - open for binary I/O
     */
    public ShadowFiles(boolean wr, boolean bi) throws FileNotFoundException {
        this.write=wr;
        this.binary=bi;
        if (write) {
            if (binary) {
                JFileChooser fo=new JFileChooser ();
                fo.setDialogTitle("Choose a binary file to save a ray set in");
                int ans=fo.showOpenDialog(null);   
                if (ans==JFileChooser.APPROVE_OPTION) {
                    file=fo.getSelectedFile();
                    if (file.exists ()) {
                        int n=JOptionPane.showConfirmDialog(null, "The file already exists. Overwrite?", "Warning",
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if (n==JOptionPane.NO_OPTION) {
                            file=null;
                            return;
                        }
                    }
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
     * Writes data for one ray
     * @param raydata - double array of 18 numbers representing 18 columns of ray data
     */
    public void write(double [] raydata) throws IOException {
        if (binary) {
            for (int i=0; i<raydata.length; i++) {
                ((DataOutputStream)stream).writeDouble(raydata[i]);
            }
        }
    }
}
