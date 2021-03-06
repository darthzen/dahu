package io.dahuapp.editor.kernel.module;

import io.dahuapp.driver.MediaDriver;
import io.dahuapp.driver.MediaDriver.Capture;
import io.dahuapp.common.kernel.Module;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialog.Actions;
import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import java.util.Optional;


/**
 * Media kernel module.
 */
public class Media implements Module {

    private MediaDriver.CaptureContext captureContext;

    @Override
    public void load(){
        captureContext = MediaDriver.loadCaptureContext();
    }

    public Capture takeCapture(String projectDir, String imageId) {
        if (MediaDriver.hasChanged(captureContext)){
            captureContext = MediaDriver.loadCaptureContext();
        }

        return MediaDriver.takeCapture(captureContext, projectDir, imageId);
    }

    /**
     * Prompts a popup to the user containing a text area.
     * @param title : title of the window
     * @param defaultInput : default text of the text area.
     * @return : the user's input
     */
    public String getInputPopup(String title, String defaultInput) {
        final TextArea inputText = new TextArea();
        inputText.setText(defaultInput);

        // we create a new dialog, with no owner, a title,
        // not limited to the application's limits and with a
        // native style.
        Dialog dlg = new Dialog(null, title, false, DialogStyle.NATIVE);

        // layout a custom GridPane containing the input field and label
        final GridPane content = new GridPane();
        content.setHgap(2);
        content.setVgap(1);
        content.add(new Label("Text"), 0, 0);
        content.add(inputText, 1, 0);
        GridPane.setHgrow(inputText, Priority.ALWAYS);

        // create the dialog with a custom graphic and the gridpane above as the
        // main content region
        dlg.setResizable(true);
        dlg.setIconifiable(false);
        dlg.setContent(content);
        dlg.getActions().addAll(Dialog.Actions.OK);

        // request focus on the username field by default (so the user can
        // type immediately without having to click first)
        Platform.runLater(new Runnable() {
            public void run() {
                inputText.requestFocus();
            }
        });

        dlg.show();
        return inputText.getText();
    }

}
