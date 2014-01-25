
package net.sehales.secon.utils;

import java.util.ArrayList;
import java.util.List;

// YES I know, that this implementation of a priority list is very primitive,
// but it should be a simple list so it is a simple implementation
public class SimplePriorityList<T> {
    
    public enum Priority {
        LOWEST, LOW, NORMAL, HIGH, HIGHEST
    }
    
    private List<T> lowestList  = new ArrayList<>();
    private List<T> lowList     = new ArrayList<>();
    private List<T> normalList  = new ArrayList<>();
    private List<T> highList    = new ArrayList<>();
    private List<T> highestList = new ArrayList<>();
    
    public void add(T object, Priority priority) {
        switch (priority) {
            case LOWEST: {
                if (!lowestList.contains(object)) {
                    lowestList.add(object);
                }
                break;
            }
            case LOW: {
                if (!lowList.contains(object)) {
                    lowList.add(object);
                }
                break;
            }
            case NORMAL: {
                if (!normalList.contains(object)) {
                    normalList.add(object);
                }
                break;
            }
            case HIGH: {
                if (!highList.contains(object)) {
                    highList.add(object);
                }
                break;
            }
            case HIGHEST: {
                if (!highestList.contains(object)) {
                    highestList.add(object);
                }
                break;
            }
        }
    }
    
    public void clear() {
        lowestList.clear();
        lowList.clear();
        normalList.clear();
        highList.clear();
        highestList.clear();
    }
    
    public List<T> getHighElements() {
        return highList;
    }
    
    public List<T> getHighestElements() {
        return highestList;
    }
    
    public List<T> getLowElements() {
        return lowList;
    }
    
    public List<T> getLowestElements() {
        return lowestList;
    }
    
    public List<T> getNormalElements() {
        return normalList;
    }
    
    public void remove(T object) {
        lowestList.remove(object);
        lowList.remove(object);
        normalList.remove(object);
        highList.remove(object);
        highestList.remove(object);
    }
    
}
