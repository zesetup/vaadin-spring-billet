package com.github.zesetup.vaadinspringbillet.ui;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.StringUtils;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.MTable;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
	private final EmployeeRepository employeeRepo;
	private final EmployeeEditorWindow editorWindow;
	
	final TextField filter;
	private final Button addNewBtn;
	private static final Logger logger = LoggerFactory.getLogger(VaadinUI.class);
	Grid grid;
	MTable<Employee> mTable;
	static final int PAGESIZE = 10;
	@Autowired
	public VaadinUI(EmployeeRepository repo, EmployeeEditorWindow editorWindow) {
	    this.employeeRepo = repo;
	    this.grid = new Grid();
	    mTable = new MTable<>(Employee.class)
	    		.withProperties("id", "name", "surname")
	    		.withColumnHeaders("id", "name", "surname")
	    		.setSortableProperties("id", "name", "surname");
	    		

	    
	    this.editorWindow = editorWindow;
		this.filter = new TextField();
		this.addNewBtn = new Button("New employee", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		//Mtable
		/*final List<Person> listOfEmployees=employeeRepo.findAll();*/
		mTable.setPageLength(10);
		/**/
		
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, mTable);
		//VerticalLayout mainLayout = new VerticalLayout(actions, grid);
		setContent(mainLayout);
		
		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "name", "surname");
		filter.setInputPrompt("Filter by surname");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listEmployees(e.getText()));
		
		// Connect selected employee to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			logger.info("selected!");
			if (e.getSelected().isEmpty()) {
				//editorSubWindow.setVisible(false);
			} else {
				logger.info("-> edit");
				/* Editor subwindow */
				editorWindow.editEmployee((Employee) grid.getSelectedRow());
				addWindow(editorWindow);
				
			}
		});

		// Instantiate and edit new employee the new button is clicked
		addNewBtn.addClickListener(e -> {
			editorWindow.editEmployee(new Employee("saply", "Itan", "Saply", "Enginner"));
			addWindow(editorWindow);
		});

		// Listen changes made by the editor, refresh data from backend
		editorWindow.setChangeHandler(() -> {
			editorWindow.close();
			listEmployees(filter.getValue());
		});
		
		/*editorSubWindow.addCloseListener(e ->{
			editorSubWindow.setVisible(false);
			logger.info("editorSubWindow closed");
		});*/

		// Initialize listing
		listEmployees(null);
		listEmployeesPaged("");
	}

	// tag::listEmployees[]
	private void listEmployees(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(new BeanItemContainer(Employee.class, employeeRepo.findAll()));
		} else {
			grid.setContainerDataSource(
		    		new BeanItemContainer(Employee.class, employeeRepo.findBySurnameStartsWithIgnoreCase(text)));
		}
	}
	// end::listEmployees[]
	
	private void listEmployeesPaged(String surnameFilter) {
		/*A dead simple in memory listing would be:
		list.setRows(repo.findAll());	
		But we want to support filtering, first add the % marks for SQL name query*/
        String likeFilter = "%" + surnameFilter + "%";
        
        //mTable.setRows(employeeRepo.findBySurnameStartsWithIgnoreCase(surnameFilter));

/*		Lazy binding for better optimized connection from the Vaadin Table to
		Spring Repository. This approach uses less memory and database
		resources. Use this approach if you expect you'll have lots of data 
		in your table. There are simpler APIs if you don't need sorting.
*/		
        logger.info("paged -> "+surnameFilter);
        mTable.lazyLoadFrom(
        (firstRow, asc, sortProperty) -> employeeRepo.findBySurnameStartsWithIgnoreCase(
        		surnameFilter,
                new PageRequest(
                        firstRow / PAGESIZE,
                        PAGESIZE
                )
        	),
	        //count fetching strategy
	        () -> (int) employeeRepo.countBySurnameStartsWithIgnoreCase(surnameFilter),
	        PAGESIZE
        );
        
        /*mTable.lazyLoadFrom(
		//entity fetching strategy
		        (firstRow, asc, sortProperty) -> employeeRepo.findBySurnameStartsWithIgnoreCase(
		        		surnameFilter,
		                new PageRequest(
		                        firstRow / PAGESIZE,
		                        PAGESIZE,
		                        asc ? Sort.Direction.ASC : Sort.Direction.DESC,
		                        //fall back to id as "natural order"
		                        sortProperty == null ? "id" : sortProperty
		                )
		        ),
		        //count fetching strategy
		        () -> (int) employeeRepo.countBySurnameStartsWithIgnoreCase(surnameFilter),
		        PAGESIZE
        );*/
        //adjustActionButtonState();
    }
}
