package com.github.zesetup.vaadinspringbillet.ui;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class EmployeeEditorWindow extends Dialog {

  private static final long serialVersionUID = 1L;
  /**
   * The currently edited employee
   */
  private Employee employee;
  /* Fields to edit properties in Employee entity */
  TextField name = new TextField("First name");
  TextField surname = new TextField("Last name");
  TextField position = new TextField("Position");
  TextField login = new TextField("Login");

  /* Action buttons */
  Button save = new Button("Save");
  Button reset = new Button("Reset");
  Button cancel = new Button("Cancel");
  Button delete = new Button("Delete", VaadinIcon.DEL.create());
  HorizontalLayout actions = new HorizontalLayout(save, reset, delete, cancel);


  @Autowired
  EmployeeService employeeService;

  @Autowired
  public EmployeeEditorWindow() {
    // Configure and style components
    // wire action buttons to save, delete and reset
    save.addClickListener(e -> {
      employee.setName(name.getValue());
      employee.setSurname(surname.getValue());
      employee.setPosition(position.getValue());
      employee.setLogin(login.getValue());
      employeeService.save(employee);
    });
    delete.addClickListener(e -> employeeService.delete(employee));
    reset.addClickListener(e -> editEmployee(employee));
    cancel.addClickListener(e -> { close(); });
    VerticalLayout verticalLayout = new VerticalLayout(name, surname, position, login, actions);
    verticalLayout.setMargin(true);
    verticalLayout.setSpacing(true);
    add(verticalLayout);
  }

  public void editEmployee(Employee e) {
    final boolean persisted = e.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      employee = employeeService.findOne(e.getId());
      name.setValue(employee.getName());
      surname.setValue(employee.getSurname());
      position.setValue(employee.getPosition());
      login.setValue(employee.getLogin());
    } else {
      name.clear();
      surname.clear();
      position.clear();
      login.clear();
      employee = e;
    }
    reset.setVisible(persisted);
    name.focus();
  }

  public interface ChangeHandler {
    void onChange(Employee e, Boolean isDelete);
  }
 
  public void setChangeHandler(ChangeHandler h) {
    save.addClickListener(e -> h.onChange(employee, false));
    delete.addClickListener(e -> h.onChange(employee, true));
  }
}
