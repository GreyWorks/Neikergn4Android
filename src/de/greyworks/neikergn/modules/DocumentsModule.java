package de.greyworks.neikergn.modules;

import java.util.ArrayList;

import de.greyworks.neikergn.containers.DocumentItem;

public class DocumentsModule {
	private ArrayList<DocumentItem> docItems = new ArrayList<DocumentItem>();

	public DocumentsModule() {
	}


	public void updateContent() {
		docItems.add(new DocumentItem("Abfallkalender 2014", "pdf/2014_abfallkalender.pdf"));
	}

	public boolean hasDocuments() {
		return docItems.size() > 0;
	}

	public ArrayList<DocumentItem> getItems() {
		return docItems;
	}

}
