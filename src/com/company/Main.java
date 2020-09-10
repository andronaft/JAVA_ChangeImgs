package com.company;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Main {

    private static final int IMG__WIDTH__CHANGE = 2; // how many pixels will decrease
    private static final int IMG__HEIGHT__CHANGE = 2;

    private static final String PATH_TO_DIRECTORY = "/Users/andreyzhukovskiy/Desktop/unity/SlotMachineRockClimber-Unity--master/Slot Machine Rock Climber";

    private static final Boolean FLAG_CHANGE_RESOLUTION = false;

    private static final Boolean FLAG_CHANGE_PALETTE = false;

    private static final Boolean FLAG_CHANGE_BRIGHTNESS = true;

    private static final float brightness = 0.75f;




    public static void main(String[]args){

        processFilesFromFolder(new File( PATH_TO_DIRECTORY));
        System.out.println("✅✅✅ DONE");

    }

    private static  boolean resizeFileMyImg(File file,String formatName){

        try{
            String name = file.getName();
            BufferedImage originalImage = ImageIO.read(file);

            int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();


                if(FLAG_CHANGE_RESOLUTION){
                    BufferedImage resizeImageHintPng = resizeImageWithHint(originalImage, type);
                    ImageIO.write(resizeImageHintPng, formatName, new File(file.getAbsolutePath()));
                    System.out.println(file.getName() + " : has been resize \uD83D\uDD0D");
                }
                if(FLAG_CHANGE_PALETTE){
                   if(changePallet(file, formatName)){
                       System.out.println(file.getName() + " : changed pallet \uD83C\uDFA8");
                   }
                }
                if(FLAG_CHANGE_BRIGHTNESS){
                    if(changeBrightness(file,formatName)){
                        System.out.println(file.getName() + " : changed brightness \uD83C\uDF1E");
                    }
                }



                return true;
        }catch(IOException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type){
        int width = originalImage.getWidth()-IMG__WIDTH__CHANGE;
        int height = originalImage.getHeight()-IMG__HEIGHT__CHANGE;

        if(width<1){
            width = 1;
        }

        if(height<1){
           height=1;
        }


        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();

        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);

        return resizedImage;
    }

    public static void processFilesFromFolder(File folder)
    {

        File[] folderEntries = folder.listFiles();
        if(folder.isDirectory()){
            System.out.println("\uD83D\uDD3D DIR: " + folder.getName() +" \uD83D\uDD3D Path: " + folder.getAbsolutePath());
            for (File entry : folderEntries)
            {
                try{
                    if (entry.isDirectory())
                    {
                        processFilesFromFolder(entry);
                        continue;
                    }

                    if(getFileExtension(entry).equals("png")||getFileExtension(entry).equals("jpg")){
                        resizeFileMyImg(new File(entry.getAbsolutePath()),getFileExtension(entry));
                    }

                    if(getFileExtension(entry).equals("meta")){
                        System.out.println(entry.getName() + " : file was deleted ❌");
                        entry.delete();

                    }
                }catch (Exception e){
                    System.out.println(e);
                }
            }
            System.out.println("\uD83D\uDD3C end DIR: " + folder.getName() + " \uD83D\uDD3C");

        }else {
            System.out.println("‼️ Check path in PATH_TO_DIRECTORY, it must be folder ‼️");
        }


    }

    private static String getFileExtension(File file) {
        String fileName = file.getName();

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);

        else return "";
    }

    private static boolean changePallet(File file,String fileformate){
        BufferedImage src = null;
        try {
            src = ImageIO.read(new File(file.getAbsolutePath()));



        IndexColorModel cm = new IndexColorModel(
                3,
                6,
                //          RED  GREEN1 GREEN2  BLUE  WHITE BLACK
                new byte[]{ 100,     0,     0,    0,    -1,     0},
                new byte[]{   0,  100,    60,    0,    -1,     0},
                new byte[]{   0,     0,     0, 100,    -1,     0});


        BufferedImage img = new BufferedImage(
                src.getWidth(), src.getHeight(),
                BufferedImage.TYPE_BYTE_INDEXED,
                cm);
        Graphics2D g2 = img.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();


        ImageIO.write(img, fileformate, new File(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean changeBrightness(File file,String fileformate){
        BufferedImage src = null;
        try {
            src = ImageIO.read(new File(file.getAbsolutePath()));


            RescaleOp bright = new RescaleOp(brightness, 0, null);

            src = bright.filter(src,null);


            ImageIO.write(src, fileformate, new File(file.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }
}