package com.github.zesetup.vaadinspringbillet.ui;

import org.springframework.beans.factory.annotation.Autowired;
import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


@SpringComponent
@UIScope
public class EmployeeEditorWindow extends Window {

		//private final EmployeeRepository repository;
		/**
		 * The currently edited employee
		 */
		private Employee employee;
		/* Fields to edit properties in Employee entity */
		TextField name = new TextField("First name");
		TextField surname= new TextField("Last name");

		/* Action buttons */
		Button save = new Button("Save", FontAwesome.SAVE);
		Button cancel = new Button("Reset");
		Button delete = new Button("Delete", FontAwesome.TRASH_O);
		CssLayout actions = new CssLayout(save, cancel, delete);
		

		@Autowired
		EmployeeService employeeService;
		
		@Autowired
		public EmployeeEditorWindow() {
			//this.repository = repository;
			// Configure and style components
			actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
			save.setStyleName(ValoTheme.BUTTON_PRIMARY);
			save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
			// wire action buttons to save, delete and reset
			save.addClickListener(e -> employeeService.save(employee));
			delete.addClickListener(e -> employeeService.delete(employee));
			cancel.addClickListener(e -> editEmployee(employee));
			VerticalLayout verticalLayout = new VerticalLayout(name, surname, actions);
			verticalLayout.setMargin(true);
			verticalLayout.setSpacing(true);
			setPosition(100, 100);
			setCaption("Employee editor");
			setContent(verticalLayout);
		}

		public interface ChangeHandler {

			void onChange();
		}

		public void editEmployee(Employee e) {
			final boolean persisted = e.getId() != null;
			if (persisted) {
				// Find fresh entity for editing
				employee = employeeService.findOne(e.getId());
			}
			else {
				employee = e;
			}
			cancel.setVisible(persisted);

			// Bind employee properties to similarly named fields
			// Could also use annotation or "manual binding" or programmatically
			// moving values from fields to entities before saving
			BeanFieldGroup.bindFieldsUnbuffered(employee, this);

			//setVisible(true);

			// A hack to ensure the whole form is visible
			save.focus();
			// Select all text in firstName field automatically
			name.selectAll();
		}

		public void setChangeHandler(ChangeHandler h) {
			// ChangeHandler is notified when either save or delete
			// is clicked
			save.addClickListener(e -> h.onChange());
			delete.addClickListener(e -> h.onChange());
		}
}
