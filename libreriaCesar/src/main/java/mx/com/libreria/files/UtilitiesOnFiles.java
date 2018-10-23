/*
 * COPYRIGHT. HSBC HOLDINGS PLC 2007. ALL RIGHTS RESERVED.
 *
 * This software is only to be used for the purpose for which it has been
 * provided. No part of it is to be reproduced, disassembled, transmitted,
 * stored in a retrieval system nor translated in any human or computer
 * language in any way or for any other purposes whatsoever without the
 * prior written consent of HSBC Holdings plc.
 */
package mx.com.libreria.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utilerias para los archivos.
 */
public class UtilitiesOnFiles {
    /**
     * Metodo para modificar la fecha de modificación a un archivo.  
     * @param  filename   Archivo a modificar.
     * @return boolean
     */
    public boolean updateModificationTime(final String filename) { 

        File file = new File(filename);

        // Get the last modified time
        // long modifiedTime = file.lastModified();
        // 0L is returned if the file does not exist
    
        // Set the last modified time
        long newModifiedTime = System.currentTimeMillis();
        boolean success = file.setLastModified(newModifiedTime);        
        
        return success;
    }
    /**
     * Metodo para eliminar un archivo.
     * @param  filename   Archivo a ser eliminado.
     * @return boolean
     */
    public boolean deleteFile(final String filename) {
        boolean success = (new File(filename)).delete();
        return success;
    }
    /**
     * Metodo para determinar si existe un archivo o directorio.
     * @param  filename   Archivo o directorio a buscar
     * @return boolean
     */
    public boolean existsFileOrDirectory(final String filename) { 
        boolean exists = (new File(filename)).exists();
        return exists;
    }
    /**
     * Metodo para renombrar un archivo.
     * @param  oldname    Nombre anterior del archivo
     * @param  newname    Nombre nuevo del archivo
     * @return boolean
     */
    public boolean renamingFile(final String oldname, final String newname) { 
        // File (or directory) with old name
        File file = new File(oldname);
        
        // File (or directory) with new name
        File file2 = new File(newname);
        
        // Rename file (or directory)
        boolean success = file.renameTo(file2);        
        return success;
    }
    /**
     * Metodo para crear un archivo.
     * @param  filename   Nombre del archivo
     * @return boolean
     */
    public boolean creatingAFile(final String filename) {
        boolean success = false;
        try {
            File file = new File(filename);        
            // Create file if it does not exist
            success = file.createNewFile();            
        } catch (IOException e) { }
        return success;
    }
    /**
     * Metodo para checar si un path es directorio.
     * @param  path        Path a checar
     * @return boolean
     */
    public boolean isDirectory(final String path) { 
        File dir = new File(path);
        boolean isDir = dir.isDirectory();
        return isDir;
    }    
    /**
     * Metodo para listar archivos.
     * @param  directoryName   Nombre del directorio
     * @return File[]
     */
    public File[] listFiles(final String directoryName) {
        File dir = new File(directoryName);
    
        // This filter only returns directories
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(final File file) {
                return file.isFile();
            }
        };
        
        File[] files = dir.listFiles(fileFilter);
        return files;
    }
    /**
     * Metodo para escribir informacion en un archivo, creando uno nuevo
     * siempre.
     * @param outfilename   Archivo de salida
     * @param msg           Mensaje a enviar al archivo
     */
    public void writingToFile(final String outfilename, final String msg) { 
        try {
            BufferedWriter out = 
                new BufferedWriter(new FileWriter(outfilename));
            out.write(msg);
            out.close();
        } catch (IOException e) {
        }
    }
    /**
     * Metodo para anexar nueva información a un archivo existente, si no existe
     * lo crea.
     * @param filename   Archivo a escribir información.
     * @param msg        Mensaje a escribir
     */
    public void appendingToFile(final String filename, final String msg) { 
        try {
            BufferedWriter out = 
                new BufferedWriter(new FileWriter(filename, true));
            out.write(msg);
            out.close();
        } catch (IOException e) {
        }
    }
}
