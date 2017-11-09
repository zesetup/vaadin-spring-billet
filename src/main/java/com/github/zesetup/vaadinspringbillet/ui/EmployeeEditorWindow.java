package com.github.zesetup.vaadinspringbillet.ui;

import com.github.zesetup.vaadinspringbillet.dao.EmployeeRepository;
import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class EmployeeEditorWindow extends Window {

  private static final long serialVersionUID = 1L;
  @Autowired
  private EmployeeRepository repository;
  /**
   * The currently edited employee
   */
  private Employee employee;
  /* Fields to edit properties in Employee entity */
  TextField name = new TextField("First name");
  TextField surname = new TextField("Last name");

  /* Action buttons */
  Button save = new Button("Save", VaadinIcons.UPLOAD);
  Button reset = new Button("Reset");
  Button cancel = new Button("Cancel");
  Button delete = new Button("Delete", VaadinIcons.RECYCLE);
  CssLayout actions = new CssLayout(save, reset, delete, cancel);


  @Autowired
  EmployeeService employeeService;

  @Autowired
  public EmployeeEditorWindow() {
    setClosable(false);
    setResizable(false);
    // Configure and style components
    actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    // wire action buttons to save, delete and reset
    save.addClickListener(e -> {
      employee.setName(name.getValue());
      employee.setSurname(surname.getValue());
      employeeService.save(employee);
    });
    delete.addClickListener(e -> employeeService.delete(employee));
    reset.addClickListener(e -> editEmployee(employee));
    cancel.addClickListener(e -> { setVisible(false); });
    VerticalLayout verticalLayout = new VerticalLayout(name, surname, actions);
    verticalLayout.setMargin(true);
    verticalLayout.setSpacing(true);
    setPosition(100, 100);
    setCaption("Employee editor");
    setContent(verticalLayout);
  }

  public void editEmployee(Employee e) {
    final boolean persisted = e.getId() != null;
    if (persisted) {
      // Find fresh entity for editing
      employee = employeeService.findOne(e.getId());
      name.setValue(employee.getName());
      surname.setValue(employee.getSurname());
    } else {
      name.clear();
      surname.clear();
      employee = e;
    }
    reset.setVisible(persisted);


    // A hack to ensure the whole form is visible
    save.focus();
    // Select all text in firstName field automatically
    name.selectAll();
  }

  public interface ChangeHandler {
    void onChange();
  }
 
  public void setChangeHandler(ChangeHandler h) {
    save.addClickListener(e -> h.onChange());
    delete.addClickListener(e -> h.onChange());  
  }
  
}
