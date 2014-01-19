
package net.sehales.secon.utils;

import java.util.ArrayList;
import java.util.List;

public class PageBuilder<T> {
    
    private List<T>       rawList;
    private List<List<T>> pages          = new ArrayList<List<T>>();
    private int           entriesPerPage = 10;
    
    public PageBuilder(List<T> list) {
        this.rawList = list;
    }
    
    public PageBuilder(List<T> list, int entriesPerPage) {
        this.rawList = list;
        this.entriesPerPage = entriesPerPage;
    }
    
    public void build() {
        pages.clear();
        
        int i = 0;
        int listCounter = 0;
        List<T> currentList = null;
        
        for (T element : rawList) { // iterate through all elements
            if (i == 0) { // shall we start with a new list?
                pages.add(new ArrayList<T>()); // create a new list
                currentList = pages.get(listCounter); // set the list we are
                                                      // using to that list
                listCounter++; // next list will be another one
            }
            if (i < entriesPerPage) { // shall we add more entrys?
                currentList.add(element); // add the current element
                i++; // next round, next element
            } else {
                i = 0; // we have reached the end of the page, lets inidcate the
                       // next page
            }
        }
    }
    
    public int getEntrysPerPage() {
        return this.entriesPerPage;
    }
    
    public List<T> getPage(int nr) {
        if (nr >= size()) {
            return null;
        }
        return this.pages.get(nr);
    }
    
    public List<T> getRawList() {
        return this.rawList;
    }
    
    public void setEntrysPerPage(int entrys) {
        this.entriesPerPage = entrys;
    }
    
    public int size() {
        return this.pages.size();
    }
}
