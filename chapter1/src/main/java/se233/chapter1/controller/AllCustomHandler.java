package se233.chapter1.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import se233.chapter1.Launcher;
import se233.chapter1.model.item.BasedEquipment;
import se233.chapter1.model.character.BasedCharacter;
import se233.chapter1.model.item.Weapon;
import se233.chapter1.model.item.Armor;
import java.util.ArrayList;

public class AllCustomHandler {
    public static class GenCharacterHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Launcher.setMainCharacter(GenCharacter.setUpCharacter());
            Launcher.refreshPane();
        }
    }

    public static void onDragDetected(MouseEvent event, BasedEquipment equipment, ImageView imgView) {
        Dragboard db = imgView.startDragAndDrop(TransferMode.ANY);
        db.setDragView(imgView.getImage());
        ClipboardContent content = new ClipboardContent();
        content.put(BasedEquipment.DATA_FORMAT, equipment);
        db.setContent(content);
        event.consume();
    }

    public static void onDragOver(DragEvent event, String type) {
        Dragboard dragboard = event.getDragboard();
        BasedEquipment retrieveEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);
        if(dragboard.hasContent(BasedEquipment.DATA_FORMAT) && retrieveEquipment.getClass().getSimpleName().equals(type)) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    public static void onDragDropped(DragEvent event, Label lbl, StackPane imgGroup) {
        boolean dragCompleted = false;
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();

        if(dragboard.hasContent(BasedEquipment.DATA_FORMAT)) {
            BasedEquipment retrieveEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);
            BasedCharacter character = Launcher.getMainCharacter();

            if(retrieveEquipment.getClass().getSimpleName().equals("Weapon")) {
                if (Launcher.getEquippedWeapon() != null) {
                    allEquipments.add(Launcher.getEquippedWeapon());
                }
                Launcher.setEquippedWeapon((Weapon) retrieveEquipment);
                character.equipWeapon((Weapon) retrieveEquipment);
            } else {
                if (Launcher.getEquippedArmor() != null) {
                    allEquipments.add(Launcher.getEquippedArmor());
                }
                Launcher.setEquippedArmor((Armor) retrieveEquipment);
                character.equipArmor((Armor) retrieveEquipment);
            }

            Launcher.setMainCharacter(character);
            Launcher.setAllEquipments(allEquipments);
            Launcher.refreshPane();

            ImageView imgView = new ImageView();
            if (imgGroup.getChildren().size() != 1) {
                imgGroup.getChildren().remove(1);
            }

            lbl.setText(retrieveEquipment.getClass().getSimpleName() + ":\n" + retrieveEquipment.getName());
            imgView.setImage(new Image(Launcher.class.getResource(retrieveEquipment.getImagepath()).toString()));
            imgGroup.getChildren().add(imgView);
            dragCompleted = true;
        }
        event.setDropCompleted(dragCompleted);
    }

    public static void onEquipDone(DragEvent event) {
        Dragboard dragboard = event.getDragboard();
        ArrayList<BasedEquipment> allEquipments = Launcher.getAllEquipments();
        BasedEquipment retrieveEquipment = (BasedEquipment)dragboard.getContent(BasedEquipment.DATA_FORMAT);

        int pos = -1;
        for(int i=0; i<allEquipments.size(); i++) {
            if (allEquipments.get(i).getName().equals(retrieveEquipment.getName())) {
                pos = i;
            }
        }

        if (pos != -1) {
            allEquipments.remove(pos);
        }

        Launcher.setAllEquipments(allEquipments);
        Launcher.refreshPane();
    }
}