package me.darkboy.upload;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.MultiFileReceiver;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Route("")
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public MainView() {

        addClassName("centered-content");

        H1 title = new H1("EasyUpload");

        Upload upload = new Upload((MultiFileReceiver) (filename, mimeType) -> {
            String currentFolder = "uploaded-files/" + UUID.randomUUID();

            try {
                Files.createDirectory(Paths.get(currentFolder));
            } catch (IOException e) {
                e.printStackTrace();
            }

            File file = new File(new File(currentFolder), filename);

            Button downloadButton = new Button("Click to download");

            Anchor anchor = new Anchor(getStreamResource(file.getName(), file), file.getName());
            anchor.getElement().setAttribute("download", true);
            anchor.setHref(getStreamResource(file.getName(), file));
            anchor.removeAll();
            anchor.add(downloadButton);

            Label fileLabel = new Label(filename);

            add(fileLabel, anchor);

            try {

                return new FileOutputStream(file);

            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
                return null;
            }
        });

        add(title, upload);
    }

    public StreamResource getStreamResource(String filename, File content) {
        return new StreamResource(filename,
                () -> {
                    try {
                        return new BufferedInputStream(new FileInputStream(content));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }
}
