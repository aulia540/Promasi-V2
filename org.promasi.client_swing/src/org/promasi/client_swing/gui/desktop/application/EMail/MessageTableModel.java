/**
 *
 */
package org.promasi.client_swing.gui.desktop.application.EMail;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.joda.time.DateTime;
import org.promasi.client_swing.gui.GuiException;

/**
 * @author alekstheod
 * 
 */
public class MessageTableModel implements TableModel{

    /**
     * Table header.
     */
    private String[] _columnHeaders;

    /**
     * All the {@link Message}s of this model.
     */
    private List<Message> _messages;

    /**
     * Constructor will initialize the object.
     */
    public MessageTableModel( List<Message> messages )throws GuiException{
        _columnHeaders = new String[] { "Title" , "Sender", "Date Send" };
        if ( messages == null ){
            throw new GuiException("Wrong argument messages == null");
        }
        
        _messages = new LinkedList<>(messages);
    }

    @Override
    public void addTableModelListener ( TableModelListener l ){
        // Disabled.
    }

    @Override
    public Class<?> getColumnClass ( int columnIndex ){
        return String.class;
    }

    @Override
    public int getColumnCount ( ){
        return _columnHeaders.length;
    }

    @Override
    public String getColumnName ( int columnIndex ){
        return _columnHeaders[columnIndex];
    }

    @Override
    public int getRowCount ( ){
        return _messages.size( );
    }

    @Override
    public Object getValueAt ( int rowIndex, int columnIndex ){
        Message message = _messages.get( rowIndex );
        if ( columnIndex == 0 ){
            return message.getTheme( );
        }else if ( columnIndex == 1 ){
            return message.getSender( ).toString( );
        }else if ( columnIndex == 2 ){
            DateTime dateSent = message.getDate();
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(dateSent.toDate());
			SimpleDateFormat dateFromat = new SimpleDateFormat("dd MMMMM kk");
			return dateFromat.format(calendar.getTime() );
        }
        
        return new String();
    }

    @Override
    public boolean isCellEditable ( int rowIndex, int columnIndex ){
        return false;
    }

    @Override
    public void removeTableModelListener ( TableModelListener l ){}

    @Override
    public void setValueAt ( Object value, int rowIndex, int columnIndex ){}

    /**
     * @return the {@link #_messages}.
     */
    public List<Message> getMessages ( ){
        return new LinkedList<>(_messages);
    }

}