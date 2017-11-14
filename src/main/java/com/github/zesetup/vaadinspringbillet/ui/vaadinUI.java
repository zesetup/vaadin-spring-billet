package com.github.zesetup.vaadinspringbillet.ui;

import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataChangeEvent;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme("valo")
public class vaadinUI extends UI {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeEditorWindow employeeEditor;

  private TextField filterField = new TextField();
  private static final Logger logger = LoggerFactory.getLogger(vaadinUI.class);
  private Grid<Employee> grid = new Grid<Employee>();
  private Employee employee;
  public vaadinUI() {}

  @Override
  protected void init(VaadinRequest request) {
    grid.setSelectionMode(SelectionMode.SINGLE);
    grid.setSizeFull();
    grid.addColumn(p -> String.valueOf(p.getName())).setCaption("Name").setSortProperty("name")
        .setWidth(200);
    grid.addColumn(p -> String.valueOf(p.getSurname())).setCaption("Surname")
        .setSortProperty("surname").setWidth(200);
    grid.addColumn(p -> String.valueOf(p.getLogin())).setCaption("Login").setSortProperty("login")
        .setWidth(200);

    // build layout
    filterField.setPlaceholder("Filter..");
    filterField.setValueChangeMode(ValueChangeMode.LAZY);
    filterField.clear();

    Button addNewBtn = new Button("New", VaadinIcons.PLUS);
    addWindow(employeeEditor);
    employeeEditor.setVisible(false);
    addNewBtn.addClickListener(e -> {
      employee = new Employee();
      employeeEditor.editEmployee(employee);
      employeeEditor.setVisible(true);
    });
    Button editBtn = new Button("Edit", VaadinIcons.EDIT);

    editBtn.addClickListener(e -> {
      if (!grid.getSelectedItems().isEmpty()) {
        employee = grid.asSingleSelect().getValue();
        employeeEditor.editEmployee(employee);
        employeeEditor.setVisible(true);

      } else {
        Notification.show("Please select item");
      }
    });

    Label counterLabel = new Label();
    HorizontalLayout actions = new HorizontalLayout(filterField, addNewBtn, editBtn);
    VerticalLayout verticalLayout = new VerticalLayout(actions, grid, counterLabel);
    setContent(verticalLayout);
    filterField.addValueChangeListener(e -> {
      grid.setDataProvider((sortOrder, offset, limit) -> employeeService.findWithFilter(sortOrder,
          offset, limit, e.getValue()), () -> employeeService.countWithFilter(e.getValue()));
      counterLabel.setValue("Size:" + employeeService.countWithFilter(e.getValue()));
    });
    grid.setDataProvider(
        (sortOrder, offset, limit) -> employeeService.find(sortOrder, offset, limit),
        () -> employeeService.count());
    counterLabel.setValue("Size:" + employeeService.count());

    employeeEditor.setChangeHandler(() -> {
      employeeEditor.setVisible(false);
      grid.getDataProvider().refreshAll();
      //grid.getDataProvider().refreshItem(employee);
      //System.out.println("Selecting Employee: " + employee.getName());      
      //grid.select(employee);
    });

  }
}
