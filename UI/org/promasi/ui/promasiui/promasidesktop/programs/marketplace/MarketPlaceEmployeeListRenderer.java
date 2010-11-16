package org.promasi.ui.promasiui.promasidesktop.programs.marketplace;


import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.JXPanel;
import org.promasi.game.IGame;
import org.promasi.game.company.SerializableEmployee;


/**
 * 
 * A {@link ListCellRenderer} responsible for rendering an employee for the
 * {@link MarketPlaceFrame}.
 * 
 * @author eddiefullmetal
 * 
 */
public class MarketPlaceEmployeeListRenderer implements ListCellRenderer
{

    /**
     * Default logger for this class.
     */
    private static final Logger LOGGER = Logger.getLogger( MarketPlaceEmployeeListRenderer.class );

    /**
     * The {@link JXPanel} that contains the data for the {@link Employee}.
     */
    private JXPanel _panel;

    /**
     * The {@link JEditorPane} that contains the curriculum vitae of the
     * employee.
     */
    private JEditorPane _curriculumVitaeText;

    /**
     * The label that shows the salary of the employee.
     */
    private JLabel _salaryLabel;

    /**
     * 
     */
    private IGame _game;
    
    /**
     * A label that shows the company that the employee is working for if he is
     * hired.
     */
    private JLabel _workingForLabel;

    public MarketPlaceEmployeeListRenderer(IGame game)throws NullArgumentException
    {
    	if(game==null)
    	{
    		throw new NullArgumentException("Wrong argument game==null");
    	}
    	
    	_game=game;
    }
    
    /**
     * Creates the {@link #_panel}. It always creates a new object, sharing the
     * object does not provide the good results.
     */
    private void buildPanel ( )
    {
        // Setup the panel.
        _panel = new JXPanel( );
        _panel.setLayout( new MigLayout( new LC( ).fill( ) ) );
        _panel.setBorder( BorderFactory.createEtchedBorder( ) );
        // Setup curriculumVitaeText
        _curriculumVitaeText = new JEditorPane( );
        _curriculumVitaeText.setContentType( "text/html" );
        _curriculumVitaeText.setEditable( false );
        // Setup salaryLabel
        _salaryLabel = new JLabel( );
        // Setup workingForLabel
        _workingForLabel = new JLabel( );
        // Configure the panel layout.
        _panel.add( _curriculumVitaeText, new CC( ).spanX( ).grow( ).gapX( "10px", "0px" ) );
        _panel.add( _salaryLabel, new CC( ).skip( 1 ).alignX( "trailing" ).wrap( ) );
        // SpanX in case the company name is too large.
        _panel.add( _workingForLabel, new CC( ).spanX( ).alignX( "trailing" ).wrap( ) );
    }

    @Override
    public Component getListCellRendererComponent ( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus )
    {
        if ( value instanceof SerializableEmployee )
        {
            buildPanel( );
            // Assign the values for the specific employee.
            SerializableEmployee employee = (SerializableEmployee) value;
            _curriculumVitaeText.setText( employee.getCurriculumVitae( ) );
            _salaryLabel.setText( "Salary " + String.valueOf( employee.getSalary( ) ) );
            if ( !_game.isEmployeeAvailable(employee) )
            {
                _workingForLabel.setText( "Working For Your Team" );
            }
            else
            {
                _workingForLabel.setText( "Free" );
            }
            // Set the colors depending if the value is selected.
            if ( isSelected )
            {
                _workingForLabel.setForeground( list.getSelectionForeground( ) );
                _salaryLabel.setForeground( list.getSelectionForeground( ) );
                _panel.setBackground( list.getSelectionBackground( ) );
            }
            else
            {
                _workingForLabel.setForeground( list.getForeground( ) );
                _salaryLabel.setForeground( list.getForeground( ) );
                _panel.setBackground( list.getBackground( ) );
            }
            
            return _panel;
        }
        else
        {
            // If the component is not an Employee use the
            // DefaultListCellRenderer to render it.
            LOGGER.warn( "MarketPlaceEmployeeListRenderer used to render objects other than Employees." );
            return new DefaultListCellRenderer( ).getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
        }
    }
}
