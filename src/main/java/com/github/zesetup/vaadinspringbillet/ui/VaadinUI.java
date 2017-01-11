package com.github.zesetup.vaadinspringbillet.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
	private final EmployeeRepository repo;
	private final EmployeeEditorWindow editorWindow;
	final Grid grid;
	final TextField filter;
	private final Button addNewBtn;
	private static final Logger logger = LoggerFactory.getLogger(VaadinUI.class);
	
	@Autowired
	public VaadinUI(EmployeeRepository repo, EmployeeEditorWindow editorWindow) {
	    this.repo = repo;
	    this.grid = new Grid();
	    this.editorWindow = editorWindow;
		this.filter = new TextField();
		this.addNewBtn = new Button("New employee", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid);
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
	}

	// tag::listEmployees[]
	private void listEmployees(String text) {
		logger.info(repo.findAll().size()+"<size");
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(new BeanItemContainer(Employee.class, repo.findAll()));
		} else {
			grid.setContainerDataSource(
		    		new BeanItemContainer(Employee.class, repo.findBySurnameStartsWithIgnoreCase(text)));
		}
	}
	// end::listEmployees[]
}
