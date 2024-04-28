package model;

public class Cell {
    private boolean revealed;
    private String status;
    private Boolean falseFlag = false;
    
    public Cell(boolean revealed, String status) {
        this.revealed = revealed;
        this.status = status;
    }
    
    public boolean getRevealed() {
        return revealed;
    }

    public void setRevealed(boolean r) { revealed = r; }


    public String getStatus() {
        return status;
    }

    public void setStatus(String c) {
        status = c;
    }

	public Boolean getFalseFlag() {
		return falseFlag;
	}

	public void setFalseFlag(Boolean falseFlag) {
		this.falseFlag = falseFlag;
	}
	
	public Cell copy(){
		Cell copy = new Cell(revealed, status);
		copy.setFalseFlag(falseFlag);
		return copy;
	}

}
