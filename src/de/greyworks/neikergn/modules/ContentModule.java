package de.greyworks.neikergn.modules;

import java.util.ArrayList;


/**
 * module blueprint
 * @author michael.grau
 *
 * @param <T>	content provided by the module
 */
public interface ContentModule<T> {
	/**
	 * get content from file and update if too old
	 */
	public void updateContent();
	
	/**
	 * force item refresh from web source
	 */
	public void forceUpdateWeb();
	
	/**
	 * module has items
	 * @return	boolean		has items
	 */
	public boolean hasItems();
	
	/**
	 * get the contents of the module
	 * @return	list of items
	 */
	public ArrayList<T> getItems();
	
	/**
	 * get single item by id
	 * @param id
	 * @return	item
	 */
	public T getById(int id);
	
	/**
	 * remove old items and sort
	 */
	public void cleanUp();

}
