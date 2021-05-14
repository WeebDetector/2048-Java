package pkg2048;


public class Tile {
    private int value;
    
    public Tile(){
        this(0);
    }
    
    public Tile(int value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
    
    public void mergeTiles(Tile tile) {
        this.setValue(this.value + tile.getValue());
    }
    
    public void clear(){
        this.setValue(0);
    }
    
    public String toString(){
        return(Integer.toString(this.getValue()));
    }
}
