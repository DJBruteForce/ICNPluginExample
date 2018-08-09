package com.ibm.gil.business;

public class FormSelect  implements Comparable<FormSelect> {

	protected String value;
	
	protected String label;
	
	private boolean selected = false;
	
    @Override
    public int compareTo(FormSelect otherObject) {
        return this.label.compareTo(otherObject.label);
    }

	public String getValue() {
		return value;
	}

	public void setValue(String item) {
		this.value = item;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String description) {
		this.label = description;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public String toString() {
		
		return getValue() + " - " + getLabel() + " - Selected:" + isSelected();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormSelect other = (FormSelect) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	

}
