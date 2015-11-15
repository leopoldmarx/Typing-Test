package com.leopoldmarx.typingtest.view;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ViewResults 
{
	private String words;
	private String chars;
	
	private Stage window = new Stage();
	private HBox  hBox = new HBox();
	private ArrayList<VBox> vBoxArray = new ArrayList<>();
	
	public ViewResults(String words, String chars)
	{
		this.words = words;
		this.chars = chars;
	}
	
	public void display()
	{
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Results");
		
		Font labelFont = Font.font("Devanagari MT", FontWeight.BOLD, 25);
		Font valueFont = new  Font("Devanagari MT", 25);
		
		Label wpm      = new Label("WPM:");
		Label wpmValue = new Label(words);
		
		Label cpm      = new Label("CPM:");
		Label cpmValue = new Label(chars);
		
		wpm     .setFont(labelFont);
		cpm     .setFont(labelFont);
		wpmValue.setFont(valueFont);
		cpmValue.setFont(valueFont);
		
		vBoxArray.add(new VBox(
				wpm, 
				wpmValue));
		
		vBoxArray.add(new VBox(
				cpm,
				cpmValue));
		
		hBox.getChildren().addAll(vBoxArray);
		
		hBox.setPadding(new Insets(20));
		hBox.setSpacing(20);
		
		Scene scene = new Scene(hBox, 210, 100);
		window.setScene(scene);
		window.show();
	}
}