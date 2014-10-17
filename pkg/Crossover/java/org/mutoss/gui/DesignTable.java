package org.mutoss.gui;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;

public class DesignTable extends JTable {

	public DesignTable() {
		super(new DesignTableModel());
		setAutoCreateRowSorter(true);
		//setModel(new DesignTableModel(this));
	}
	
	public DesignTableModel getModel() {
        return (DesignTableModel)super.getModel();
    }
	
	public void setDesigns(List<Design> designs) {
		getModel().setDesigns(designs);
	}
	
	public String getToolTipText(MouseEvent e) {
        String tooltip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int realRowIndex = convertRowIndexToModel(rowIndex);
        int realColumnIndex = convertColumnIndexToModel(colIndex);

        if (realColumnIndex == 0) {
            tooltip = getModel().getDesigns().get(realRowIndex).reference;
        } else if (realColumnIndex == 3) { //TODO Show how balanced design is.
        	//
        } else {
            tooltip = super.getToolTipText(e);
        }
        return tooltip;
    }
	
}
