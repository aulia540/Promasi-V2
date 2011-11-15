package org.promasi.client.gui.marketplace;

import java.util.Map;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.layout.grouplayout.LayoutStyle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.promasi.game.GameException;
import org.promasi.game.IGame;
import org.promasi.game.company.Company;
import org.promasi.game.company.MarketPlace;
import org.promasi.game.company.SerializableEmployee;
import org.promasi.utilities.exceptions.NullArgumentException;
import org.promasi.utilities.serialization.SerializationException;


public class MarketPlaceComposite extends Composite {

	/**
	 * 
	 */
	protected Object result;
	
	/**
	 * 
	 */
	private Map<String, SerializableEmployee> _availableEmployees;
	
	/**
	 * 
	 */
	private Map<String, SerializableEmployee> _hiredEmployees;
	
	/**
	 * 
	 */
	private Browser _hiredEmployeeInfo;
	
	/**
	 * 
	 */
	private IGame _game;
	
	/**
	 * 
	 */
	private Browser _availableEmployeeInfo;
	
	/**
	 * 
	 */
	private Table _availableEmployeesTable;
	
	/**
	 * 
	 */
	private Table _hiredEmployeesTable;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MarketPlaceComposite(Composite parent, int style, Company company, MarketPlace marketPlace , IGame game)throws NullArgumentException {
		super(parent, style);

		if(company==null){
			throw new NullArgumentException("Wrong argument company==null");
		}
		
		if(marketPlace==null){
			throw new NullArgumentException("Wrong argument marketPlace==null");
		}
		
		if(game==null){
			throw new NullArgumentException("Wrong argument game==null");
		}
		
		
		_game=game;
		try{
			_availableEmployees=new TreeMap<String, SerializableEmployee>(marketPlace.getAvailableEmployees());
			_hiredEmployees=new TreeMap<String, SerializableEmployee>(company.getEmployees());
		}catch(SerializationException e){
			e.printStackTrace();
			//TODO logging.
			_availableEmployees=new TreeMap<String, SerializableEmployee>();
			_hiredEmployees=new TreeMap<String, SerializableEmployee>();
		}
		
		TabFolder tabFolder = new TabFolder(this, SWT.NONE);
		
		TabItem availableTab = new TabItem(tabFolder, SWT.NONE);
		availableTab.setText("Available");
		
		Composite availableComposite = new Composite(tabFolder, SWT.NONE);
		availableTab.setControl(availableComposite);
		
		Button hireButton = new Button(availableComposite, SWT.NONE);
		hireButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int selectedIndex=_availableEmployeesTable.getSelectionIndex();
				if(selectedIndex>=0){
					TableItem tableItem=_availableEmployeesTable.getItem(selectedIndex);
					if(_availableEmployees.containsKey(tableItem.getText(0))){
						try {
							_game.hireEmployee( tableItem.getText(0) );
						} catch (GameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
					}
				}
			}
		});
		
		hireButton.setText("Hire");
		
		_availableEmployeesTable = new Table(availableComposite, SWT.BORDER | SWT.FULL_SELECTION);
		_availableEmployeesTable.setHeaderVisible(true);
		_availableEmployeeInfo = new Browser(availableComposite, SWT.NONE);
		_availableEmployeesTable.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int selectedIndex=_availableEmployeesTable.getSelectionIndex();
				if(selectedIndex>=0){
					TableItem tableItem=_availableEmployeesTable.getItem(selectedIndex);
					if(_availableEmployees.containsKey(tableItem.getText(0))){
						try {
							_availableEmployeeInfo.setText(_availableEmployees.get(tableItem.getText(0)).getCurriculumVitae());
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		TableColumn tblclmnId = new TableColumn(_availableEmployeesTable, SWT.NONE);
		tblclmnId.setWidth(40);
		tblclmnId.setText("ID");
		
		TableColumn tblclmnName = new TableColumn(_availableEmployeesTable, SWT.NONE);
		tblclmnName.setWidth(65);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnLastname = new TableColumn(_availableEmployeesTable, SWT.NONE);
		tblclmnLastname.setWidth(81);
		tblclmnLastname.setText("Lastname");
		GroupLayout gl_availableComposite = new GroupLayout(availableComposite);
		gl_availableComposite.setHorizontalGroup(
			gl_availableComposite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_availableComposite.createSequentialGroup()
					.add(11)
					.add(_availableEmployeesTable, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
					.add(7)
					.add(gl_availableComposite.createParallelGroup(GroupLayout.TRAILING)
						.add(_availableEmployeeInfo, GroupLayout.PREFERRED_SIZE, 397, GroupLayout.PREFERRED_SIZE)
						.add(hireButton, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_availableComposite.setVerticalGroup(
			gl_availableComposite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_availableComposite.createSequentialGroup()
					.add(13)
					.add(gl_availableComposite.createParallelGroup(GroupLayout.TRAILING)
						.add(_availableEmployeesTable, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
						.add(gl_availableComposite.createSequentialGroup()
							.add(_availableEmployeeInfo, GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.RELATED)
							.add(hireButton)))
					.add(13))
		);
		availableComposite.setLayout(gl_availableComposite);
		
		TabItem hiredTab = new TabItem(tabFolder, SWT.NONE);
		hiredTab.setText("Hired");
		
		Composite hiredComposite = new Composite(tabFolder, SWT.NONE);
		hiredTab.setControl(hiredComposite);
		_hiredEmployeeInfo = new Browser(hiredComposite, SWT.NONE);
		
		Button dischargeButton = new Button(hiredComposite, SWT.NONE);
		dischargeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int selectedIndex=_hiredEmployeesTable.getSelectionIndex();
				if(selectedIndex>=0){
					TableItem tableItem=_hiredEmployeesTable.getItem(selectedIndex);
					if(_hiredEmployees.containsKey(tableItem.getText(0))){
						try {
							_game.dischargeEmployee( tableItem.getText(0) );
						} catch (GameException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		
		dischargeButton.setText("Discharge");
		
		_hiredEmployeesTable = new Table(hiredComposite, SWT.BORDER | SWT.FULL_SELECTION);
		_hiredEmployeesTable.setHeaderVisible(true);
		_hiredEmployeesTable.setLinesVisible(true);
		
		_hiredEmployeesTable.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int selectedIndex=_hiredEmployeesTable.getSelectionIndex();
				if(selectedIndex>=0){
					TableItem tableItem=_hiredEmployeesTable.getItem(selectedIndex);
					if(_hiredEmployees.containsKey(tableItem.getText(0))){
						try {
							_hiredEmployeeInfo.setText(_hiredEmployees.get(tableItem.getText(0)).getCurriculumVitae());
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		TableColumn tblclmnId_1 = new TableColumn(_hiredEmployeesTable, SWT.NONE);
		tblclmnId_1.setWidth(40);
		tblclmnId_1.setText("ID");
		
		TableColumn tblclmnName_1 = new TableColumn(_hiredEmployeesTable, SWT.NONE);
		tblclmnName_1.setWidth(65);
		tblclmnName_1.setText("Name");
		
		TableColumn tblclmnLastname_1 = new TableColumn(_hiredEmployeesTable, SWT.NONE);
		tblclmnLastname_1.setWidth(81);
		tblclmnLastname_1.setText("Lastname");
		GroupLayout gl_hiredComposite = new GroupLayout(hiredComposite);
		gl_hiredComposite.setHorizontalGroup(
			gl_hiredComposite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_hiredComposite.createSequentialGroup()
					.add(11)
					.add(_hiredEmployeesTable, GroupLayout.PREFERRED_SIZE, 210, GroupLayout.PREFERRED_SIZE)
					.add(gl_hiredComposite.createParallelGroup(GroupLayout.TRAILING)
						.add(gl_hiredComposite.createSequentialGroup()
							.add(6)
							.add(_hiredEmployeeInfo, GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE))
						.add(gl_hiredComposite.createSequentialGroup()
							.addPreferredGap(LayoutStyle.RELATED)
							.add(dischargeButton, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_hiredComposite.setVerticalGroup(
			gl_hiredComposite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_hiredComposite.createSequentialGroup()
					.add(13)
					.add(gl_hiredComposite.createParallelGroup(GroupLayout.TRAILING)
						.add(_hiredEmployeesTable, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
						.add(gl_hiredComposite.createSequentialGroup()
							.add(_hiredEmployeeInfo, GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
							.addPreferredGap(LayoutStyle.RELATED)
							.add(dischargeButton)))
					.add(13))
		);
		hiredComposite.setLayout(gl_hiredComposite);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(groupLayout.createSequentialGroup()
					.add(tabFolder, GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
					.add(5))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(groupLayout.createSequentialGroup()
					.add(tabFolder, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
					.add(4))
		);
		setLayout(groupLayout);
		
		for(Map.Entry<String, SerializableEmployee> entry : _availableEmployees.entrySet()){
			TableItem item=new TableItem(_availableEmployeesTable, SWT.NONE);
			item.setText(0, entry.getValue().getEmployeeId());
			item.setText(1,entry.getValue().getFirstName());
			item.setText(2,entry.getValue().getLastName());
		}
		
		for(Map.Entry<String, SerializableEmployee> entry : _hiredEmployees.entrySet()){
			TableItem item=new TableItem(_hiredEmployeesTable, SWT.NONE);
			
			item.setText(0, entry.getValue().getEmployeeId());
			item.setText(1,entry.getValue().getFirstName());
			item.setText(2,entry.getValue().getLastName());
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * 
	 * @param company
	 * @param marketPlace
	 * @throws NullArgumentException
	 * @throws SerializationException 
	 */
	public synchronized void updateMarketPlaceDialog(Company company, MarketPlace marketPlace)throws NullArgumentException, SerializationException{
		if(company==null){
			throw new NullArgumentException("Wrong argument company==null");
		}
		
		if(marketPlace==null){
			throw new NullArgumentException("Wrong argument marketPlace==null");
		}
		
		_availableEmployees=new TreeMap<String, SerializableEmployee>(marketPlace.getAvailableEmployees());
		_hiredEmployees=new TreeMap<String, SerializableEmployee>(company.getEmployees());
		
		this.getDisplay().syncExec(new Runnable() {
			
			@Override
			public void run() {
				if(!getParent().getShell().isDisposed() && !getParent().getShell().getDisplay().isDisposed()){
					_hiredEmployeeInfo.setText("");
					_availableEmployeeInfo.setText("");
					_availableEmployeesTable.removeAll();
					for(Map.Entry<String, SerializableEmployee> entry : _availableEmployees.entrySet()){
						TableItem item=new TableItem(_availableEmployeesTable, SWT.NONE);
						item.setText(0, entry.getValue().getEmployeeId());
						item.setText(1,entry.getValue().getFirstName());
						item.setText(2,entry.getValue().getLastName());
					}
					
					_hiredEmployeesTable.removeAll();
					for(Map.Entry<String, SerializableEmployee> entry : _hiredEmployees.entrySet()){
						TableItem item=new TableItem(_hiredEmployeesTable, SWT.NONE);
						item.setText(0, entry.getValue().getEmployeeId());
						item.setText(1,entry.getValue().getFirstName());
						item.setText(2,entry.getValue().getLastName());
					}
					
					if(_hiredEmployeesTable.getItemCount()!=0){
						_hiredEmployeesTable.setSelection(0);
						String employeeId=_hiredEmployeesTable.getItem(0).getText(0);
						_hiredEmployeeInfo.setText(_hiredEmployees.get(employeeId).getCurriculumVitae());
					}
					
					if(_availableEmployeesTable.getItemCount()!=0){
						_availableEmployeesTable.setSelection(0);
						String employeeId=_availableEmployeesTable.getItem(0).getText(0);
						_availableEmployeeInfo.setText(_availableEmployees.get(employeeId).getCurriculumVitae());
					}
				}			
			}
		});
	}
}
