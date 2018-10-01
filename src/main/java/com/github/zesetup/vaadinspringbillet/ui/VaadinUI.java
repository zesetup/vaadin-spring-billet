package com.github.zesetup.vaadinspringbillet.ui;

import com.github.zesetup.vaadinspringbillet.model.Employee;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService;
import com.github.zesetup.vaadinspringbillet.service.EmployeeService.EmployeeSort;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.data.provider.SortOrder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class VaadinUI extends VerticalLayout {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeEditorWindow employeeEditor;

  private TextField filterField = new TextField();
  private Grid<Employee> grid = new Grid<>();
  private Employee employee;
  public VaadinUI() {}

  @Autowired
  protected void init() {
    DataProvider<Employee, Void> dataProvider = DataProvider.fromCallbacks(
        query -> {
          List<EmployeeSort> sortOrders = new ArrayList<>();
          for(SortOrder<String> queryOrder : query.getSortOrders()) {
            EmployeeSort sort = employeeService.createSort(
              // The name of the sorted property
              queryOrder.getSorted(),
              // The sort direction for this property
              queryOrder.getDirection() == SortDirection.DESCENDING);
            sortOrders.add(sort);
          }
          return employeeService.findWithFilter(
              sortOrders,
              query.getOffset(),
              query.getLimit(),
              filterField.getValue()
            );
        },
        // The number of persons is the same regardless of ordering
        query -> employeeService.countWithFilter(filterField.getValue())
      );     

    grid.addColumn(Employee::getName).setHeader("Name")
    .setFlexGrow(0)
    .setWidth("100px")
    .setResizable(false);
    grid.addColumn(Employee::getSurname).setHeader("SurName")
    .setFlexGrow(0)
    .setWidth("100px")
    .setResizable(false);
    grid.addColumn(Employee::getLogin).setHeader("Login")
    .setFlexGrow(0)
    .setWidth("100px")
    .setResizable(false);
    grid.addColumn(Employee::getPosition).setHeader("Position")
    .setFlexGrow(0)
    .setFlexGrow(1)
    .setResizable(false);
    
    grid.setSelectionMode(SelectionMode.SINGLE);
    
    // build layout
    filterField.setPlaceholder("Filter..");
    filterField.setValueChangeMode(ValueChangeMode.EAGER);
    filterField.clear();

    Button addNewBtn = new Button("New");
    //addWindow(employeeEditor);
    addNewBtn.addClickListener(e -> {
      employee = new Employee();
      employeeEditor.editEmployee(employee);
      employeeEditor.open();
    });
    Button editBtn = new Button("Edit", VaadinIcon.EDIT.create());

    editBtn.addClickListener(e -> {
      if (!grid.getSelectedItems().isEmpty()) {
        employee = grid.asSingleSelect().getValue();
        employeeEditor.editEmployee(employee);
        employeeEditor.open();
      } else {
        Notification.show("Please select item");
      }
    });
    Label counterLabel = new Label();
    HorizontalLayout actions = new HorizontalLayout(filterField, addNewBtn, editBtn);
    VerticalLayout verticalLayout = new VerticalLayout(actions, grid, counterLabel);
    verticalLayout.setHeight("500px");
    verticalLayout.setWidth("500px");

    filterField.addValueChangeListener(e -> {
      grid.setDataProvider(dataProvider);
      counterLabel.setText("Size:" + employeeService.countWithFilter(e.getValue()));
    });
        
    grid.setDataProvider(dataProvider);
    counterLabel.setText("Size:" + employeeService.countWithFilter(""));

    employeeEditor.setChangeHandler((e, isDelete) -> {
      employeeEditor.close();
      if(isDelete) {
        grid.getDataProvider().refreshAll();
      } else {
        grid.getDataProvider().refreshItem(e);
        grid.select(employee);
      }
    });
    add(verticalLayout);
  }
}
