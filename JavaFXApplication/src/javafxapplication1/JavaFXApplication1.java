/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

/**
 *
 * @author Nikita
 */
public class JavaFXApplication1 extends Application {
    
    public void resultcompressiondialog(String choicefile){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Result compression");
        dialog.setResizable(true);
        
        File file = new File("compressed.rle");
        File file1 = new File(choicefile);
        dialog.setContentText(" compressed.rle: " + file.length() + " bit");
        dialog.setHeaderText( choicefile + " :" + file1.length() + " bit");
        
        Optional<ButtonType> result = dialog.showAndWait();
         if (result.get() == ButtonType.OK){
             
            } else {
                // ... user chose CANCEL or closed the dialog
            }
    }
    
    public String getFileExtension(File file) {
        String fileName = file.getName();
        // если в имени файла есть точка и она не является первым символом в названии файла
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
        return fileName.substring(fileName.lastIndexOf(".")+1);
        // в противном случае возвращаем заглушку, то есть расширение не найдено
        else return "";
    }
    
    public void notification(){
        Alert dialog = new Alert(Alert.AlertType.INFORMATION);
        dialog.setTitle("Incorect input");
        dialog.setHeaderText("Please select jpg file");
        Optional<ButtonType> result = dialog.showAndWait();
         if (result.get() == ButtonType.OK){
             
            } else {
                // ... user chose CANCEL or closed the dialog
            }
    }
    
    public void resultdialog(String output){
         Alert dialog = new Alert(Alert.AlertType.INFORMATION);
         dialog.setTitle("Result decompression");
         dialog.setHeaderText("Test Complete");
         dialog.setResizable(true);
         BorderPane root = new BorderPane();
         
        File file = new File(output);
        Image image = new Image(file.toURI().toString(),800,500,false,true);
        ImageView imageView = new ImageView(image);
        
        root.setCenter(imageView);
        dialog.getDialogPane().setContent(root);
        
         Optional<ButtonType> result = dialog.showAndWait();
         if (result.get() == ButtonType.OK){
             
            } else {
                // ... user chose CANCEL or closed the dialog
            }
    }
    
    @Override
    public void start(Stage primaryStage) {
        Button btnComp = new Button();
        btnComp.setText("Compress");
        Button btnDecomp = new Button();
        btnDecomp.setText("Decompress");
        Button btnChangeRGB = new Button();
        btnChangeRGB.setText("convert and compress ");
        Button btnBase64 = new Button();
        btnBase64.setText("Compress with base64");
        Label label = new Label("");
        Label basemod = new Label("false");
        
        btnComp.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");    
                
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File choicefile = fileopen.getSelectedFile();
                    String fileName = choicefile.getName();
                     if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                         // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
                         label.setText(fileName.substring(fileName.lastIndexOf(".")+1));
                         // в противном случае возвращаем заглушку, то есть расширение не найдено
                     else label.setText("");
                    RLE.compress(choicefile.getPath(), "compressed.rle");
                    resultcompressiondialog(choicefile.getName());
                }
            }
        });
        btnDecomp.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                if (basemod.getText().equalsIgnoreCase("true")){
                   basemod.setText("false");
                   RLE.decompress("compressed.rle", "temp/output.txt"); 
                    try {
                        BufferedImage result = Base64.decodeToImage(Base64.readUsingFiles("temp/output.txt"));
                        ImageIO.write(result, "JPEG", new File("output.jpg"));
                        label.setText("jpg");
                    } catch (IOException ex) {
                        Logger.getLogger(JavaFXApplication1.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                File fileRLE = new File("compressed.rle");
                    if (fileRLE.exists()){
                        RLE.decompress("compressed.rle", "output." + label.getText());
                     // RLE.decompress("compressed.rle", "output.jpg");
                    }
                }
                resultdialog("output." + label.getText());
            }
        });
        btnChangeRGB.setOnAction(new EventHandler<ActionEvent>() {      //Кнопка смены 'палитры' 
            
            @Override
            public void handle(ActionEvent event) {
               JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");    
                
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File choicefile = fileopen.getSelectedFile();
                    String fileName = choicefile.getName();
                     if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                         // то вырезаем все знаки после последней точки в названии файла, то есть ХХХХХ.txt -> txt
                         label.setText(fileName.substring(fileName.lastIndexOf(".")+1));
                         // в противном случае возвращаем заглушку, то есть расширение не найдено
                     else label.setText("");
                     if (label.getText().equalsIgnoreCase("jpg")){
                             try {
                                 RLE.testCmykToRgb(choicefile.getPath());
                             } catch (Exception ex) {
                                 Logger.getLogger(JavaFXApplication1.class.getName()).log(Level.SEVERE, null, ex);
                             }
                             RLE.compress("temp/convertedImage.jpg", "compressed.rle");
                             resultcompressiondialog(choicefile.getName());
                     }
                     else notification();
                }
            }
        });
        
        btnBase64.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                JFileChooser fileopen = new JFileChooser();
                int ret = fileopen.showDialog(null, "Открыть файл");    
                
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File choicefile = fileopen.getSelectedFile();
                    String fileName = choicefile.getName();
                     if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                         label.setText(fileName.substring(fileName.lastIndexOf(".")+1));
                     else label.setText("");
                    if (label.getText().equalsIgnoreCase("jpg")){
                             try {
                                 String base64 = Base64.encodeToString(ImageIO.read(new File(choicefile.getPath())), "jpg");
                                 basemod.setText("true");
                                 PrintWriter out = new PrintWriter("temp/filename.txt");
                                 out.println(base64);
                                 out.close();
                             } catch (Exception ex) {
                                 Logger.getLogger(JavaFXApplication1.class.getName()).log(Level.SEVERE, null, ex);
                             }
                             RLE.compress("temp/filename.txt", "compressed.rle");
                             resultcompressiondialog(choicefile.getName());
                     }
                     else notification();
                }
            }
        });
        
        GridPane root = new GridPane();
        root.setHgap(10);
        root.setVgap(10);
        root.setPadding(new Insets(20, 150, 10, 10));
        root.setAlignment(Pos.CENTER);
        root.add(btnComp, 2, 3);
        root.add(btnDecomp, 2, 4);
        root.add(btnChangeRGB, 4, 3);
        root.add(btnBase64, 4, 4);
        Text text = new Text("Please, select your command");
        Text text1 = new Text("compress jpg file in another palette ");
        root.add(text, 2, 2);
        root.add(text1, 4, 2);
        
        Scene scene = new Scene(root, 700, 500);
        
        primaryStage.setTitle("RLE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
